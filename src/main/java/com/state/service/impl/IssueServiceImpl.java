package com.state.service.impl;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.state.dao.AreaDao;
import com.state.dao.DealResultDaoI;
import com.state.dao.IDeclareDao;
import com.state.dao.IPathResultDao;
import com.state.dao.IssueDao;
import com.state.dao.LineDefineDao;
import com.state.dao.LineLimitDao;
import com.state.dao.TransTielineDao;
import com.state.dao.UserDaoI;
import com.state.enums.Enums_DeclareType;
import com.state.enums.Enums_SystemConst;
import com.state.exception.MsgException;
import com.state.po.AreaReportBean;
import com.state.po.DeclareExtraPo;
import com.state.po.DeclarePo;
import com.state.po.LineLimitPo;
import com.state.po.LineReportBean;
import com.state.po.PathResultPo;
import com.state.po.ResultPo;
import com.state.po.SdTotalCostPo;
import com.state.po.SdcostResultPo;
import com.state.po.TransTielinePo;
import com.state.po.TreeBean;
import com.state.po.UserPo;
import com.state.service.IDeclareService;
import com.state.service.IssueService;
import com.state.service.ServiceHelper;
import com.state.util.CommonUtil;
import com.state.util.DeclareUtil;
import com.state.util.FileManager;
import com.state.util.IssueUtil;
import com.state.util.ResultUtil;
import com.state.util.SessionUtil;
import com.state.util.SystemTools;
import com.state.util.office.WordUtil;

@Service
@Transactional
public class IssueServiceImpl implements IssueService {

	@Autowired
	private IssueDao issueDao;
	@Autowired
	private IPathResultDao pathResultDao;
	@Autowired
	private IDeclareDao declareDao;
	@Autowired
	private LineDefineDao lineDefineDao;
	@Autowired
	private LineLimitDao lineLimitDao;

	@Autowired
	private DealResultDaoI dealResultDao;
	@Autowired
	private IDeclareService declareService;
	@Autowired
	private AreaDao areaDao;
	
	/**
	 * 根据地区获取发布单列表
	 * 
	 * @param area
	 * @return
	 */
	public List<DeclarePo> getResultNameList(String area, String time) {
		return issueDao.selectSheetOfResultByArea(area, time);
	}

	/**
	 * 根据申报单号、类型查找发布单
	 * 
	 * @param dsheet
	 * @param dtype
	 * @return
	 */
	public ResultPo getResultBySheetId(String dsheet, String dtype) {
		return issueDao.getResultBySheetId(issueDao.getResultIdBySheetName(dsheet).getId() + "", dtype);
	}

	/**
	 * 山东单子汇总
	 * 
	 * @param time
	 * @return
	 */
	public List<SdcostResultPo> geSdBillMeg(String time) {
		return issueDao.getSdBillList(time);
	}

	/**
	 * 山东费用汇总
	 * 
	 * @param time
	 * @return
	 */
	public List<SdTotalCostPo> getSdAreaMeg(String time) {
		return issueDao.getSdAreaList(time);
	}

	/**
	 * 审核更新状态
	 * 
	 * @return
	 */
	public void issue(String time) {
		pathResultDao.updatePrint(time, "1");
		issueDao.updatePrint(time, "1");
		issueDao.updateSdBill(time, "1");
		issueDao.updateSdCost(time, "1");
		issueDao.updateDeclare(time, "1");
		issueDao.updateLineLimit(time, "1");
		dealResultDao.updatePrint(time, "1");
	}

	/**
	 * 根据时间获取树结构
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	public List<DeclarePo> getAreaTree(String time) {
		return issueDao.getAreaTree(time);
	}

	/**
	 * 根据地区获取发布单数据汇总
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	public ResultPo getResultByArea(String area, String time) {
		return issueDao.getResultByArea(area, time);
	}

	public DeclarePo getDeclarePoById(String sheetId) {
		return issueDao.getDeclarePoById(sheetId);
	}

	/**
	 * 根据月度获取地区统计信息
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	public List<AreaReportBean> getAreaReportByMonth(String startTime, String endTime) {
		return issueDao.getAreaReportByMonth(startTime, endTime);
	}

	/**
	 * 根据年度获取地区统计信息
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	public List<AreaReportBean> getAreaReportByYear(String startTime, String endTime, int start, int end) {
		return issueDao.getAreaReportByYear(startTime, endTime, start, end);
	}

	/**
	 * 根据年度获取联络新统计信息
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	public List<LineReportBean> getLineReportByYear(String startTime, String endTime) {
		return issueDao.getLineReportByYear(startTime, endTime);
	}

	/**
	 * 根据月度获取联络新统计信息
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	public List<LineReportBean> getLineReportByMonth(String startTime, String endTime) {
		return issueDao.getLineReportByMonth(startTime, endTime);
	}

	/**
	 * 发布页面左边获取发布信息树结构
	 * 
	 * @description
	 * @author 大雄
	 * @date 2016年8月27日上午11:24:49
	 * @param mdate
	 * @return
	 */
	public List<TreeBean> getTree(String area, String mdate, Integer status) {
		List<TreeBean> list = new ArrayList();
		if (CommonUtil.ifEmpty(area)) {
			if (SessionUtil.isState()) {
				area = null;
			}
		}

		List<ResultPo> rpos = issueDao.getTreeForResult(area, mdate, status,null);
		int firstIndex = 0;
		Map<String, Map<String, Object>> tree = new HashMap<String, Map<String, Object>>();
		TreeBean firstNode = null;
		TreeBean secondNode = null;
		TreeBean thirdNode = null;
		TreeBean forthNode = null;

		Map<String, Object> treeInfo = null;
		Map<String, Object> first = null;
		Map<String, Object> firstInfo = null;
		Map<String, Object> second = null;
		Map<String, Object> secondInfo = null;
		Map<String, Object> third = null;
		Map<String, Object> thirdInfo = null;
		Map<String, Object> forth = null;
		int i = 0;
		String id;
		boolean isOpen = false;
		//判断单子是否成交，比较电力值是否为0
		Map<String,Boolean> sheetStatus = new HashMap<String, Boolean>();
		if (CommonUtil.ifEmpty_List(rpos)) {
			
			for (ResultPo po : rpos) {
				//如果这个单子不存在或者这个单子的状态是false
				if(!sheetStatus.containsKey(po.getCorridor()) || !sheetStatus.get(po.getCorridor())){
					//如果是电力
					if(po.getDtype().equals(Enums_DeclareType.DATATYPE_POWER)){
						boolean flag = true;
						for (int m = 1; m < 97; m++) {
							String getAttributeMethodName = "getH" + (m< 10 ? "0" + m : m);
							Method getAttributeMethod = null;
							try {
								getAttributeMethod = ResultPo.class.getDeclaredMethod(getAttributeMethodName);
								Object v = getAttributeMethod.invoke(po);
								if(v!= null && (Double)v != 0){
										flag = false;
										break;
								}
							} catch (NoSuchMethodException e) {
								e.printStackTrace();
							} catch (SecurityException e) {
								e.printStackTrace();
							} catch (IllegalArgumentException e) {
								e.printStackTrace();
							} catch (IllegalAccessException e) {
								e.printStackTrace();
							} catch (InvocationTargetException e) {
								e.printStackTrace();
							}
						}
						if(flag == false){
							sheetStatus.put(po.getCorridor(), false);
						}else{
							sheetStatus.put(po.getCorridor(), true && sheetStatus.get(po.getCorridor()) == null?true:sheetStatus.get(po.getCorridor()));
						}
					}
					
				}
				
			}
		}
		if (CommonUtil.ifEmpty_List(rpos)) {
			for (ResultPo po : rpos) {
				
				if (tree.containsKey(po.getArea())) {
					if (SessionUtil.isState() || po.getArea().equals("四川")) {
						isOpen = true;
					} else {
						isOpen = (firstIndex == 1 ? true : false);
					}
					isOpen = true;
					second = (Map<String, Object>) tree.get(po.getArea()).get("children");
					id = String.valueOf(tree.get(po.getArea()).get("id"));

					if (second.containsKey(po.getCorridor())) {
						secondInfo = (Map<String, Object>) second.get(po.getCorridor());
						third = (Map<String, Object>) secondInfo.get("children");
						if (third.containsKey(po.getDtype())) {
							thirdInfo = (Map<String, Object>) third.get(po.getDtype());
							forth = (Map<String, Object>) thirdInfo.get("children");
							if (forth.containsKey(po.getSide())) {

							} else {
								if(!CommonUtil.ifEmpty(po.getSide())){
									continue;
								}
								forth.put(po.getSide(), po.getId());
								forthNode = new TreeBean(po.getId(), String.valueOf(thirdInfo.get("id")), po.getSide(), isOpen,1);
								forthNode.setArea(po.getArea());
								forthNode.setDrole(po.getSide());
								forthNode.setDtype(po.getDtype());
								list.add(forthNode);
							}
						} else {
							thirdInfo = new HashMap<String, Object>();
							forth = new HashMap<String, Object>();

							third.put(po.getDtype(), thirdInfo);
							thirdInfo.put("id", String.valueOf(secondInfo.get("id")) + CommonUtil.decimal(2, third.size()));
							thirdInfo.put("children", forth);
							if("电价".equals(po.getDtype())){
								thirdNode = new TreeBean(po.getId(), String.valueOf(secondInfo.get("id")), po.getDtype(), isOpen,2);
							}else{
								thirdNode = new TreeBean(String.valueOf(thirdInfo.get("id")), String.valueOf(secondInfo.get("id")), po.getDtype(), isOpen,1);
							}
							list.add(thirdNode);

							if(!CommonUtil.ifEmpty(po.getSide())){
								continue;
							}
							forth.put(po.getSide(), po.getId());
							forthNode = new TreeBean(po.getId(), thirdNode.getId(), po.getSide(), isOpen,1);
							forthNode.setArea(po.getArea());
							forthNode.setDrole(po.getSide());
							forthNode.setDtype(po.getDtype());

							list.add(forthNode);
						}
					} else {
						// second = new HashMap<String,Object>();

						secondInfo = new HashMap<String, Object>();
						third = new HashMap<String, Object>();
						thirdInfo = new HashMap<String, Object>();
						forth = new HashMap<String, Object>();

						second.put(po.getCorridor(), secondInfo);

						secondInfo.put("id", id + CommonUtil.decimal(2, second.size()));
						secondInfo.put("children", third);
						if(po.getCorridor().equals("汇总值")){
							secondNode = new TreeBean(String.valueOf(secondInfo.get("id")), id, "交易汇总值", isOpen,1);
						}else{
							secondNode = new TreeBean(String.valueOf(secondInfo.get("id")), id, po.getCorridor(), isOpen,sheetStatus.get(po.getCorridor()) ? "../img/icon/circle-gray.png":"../img/icon/circle-green.png",2);
						}

						list.add(secondNode);

						third.put(po.getDtype(), thirdInfo);
						thirdInfo.put("id", secondNode.getId() + "01");
						thirdInfo.put("children", forth);
						if("电价".equals(po.getDtype())){
							thirdNode = new TreeBean(po.getId(), secondNode.getId(), po.getDtype(), isOpen,2);
						}else{
							thirdNode = new TreeBean(secondNode.getId() + "01", secondNode.getId(), po.getDtype(), isOpen,1);
						}
						list.add(thirdNode);

						if(!CommonUtil.ifEmpty(po.getSide())){
							continue;
						}
						forth.put(po.getSide(), po.getId());
						forthNode = new TreeBean(po.getId(), thirdNode.getId(), po.getSide(), isOpen);
						forthNode.setArea(po.getArea());
						forthNode.setDrole(po.getSide());
						forthNode.setDtype(po.getDtype());

						list.add(forthNode);
					}
				} else {
					// 送电侧或者受电侧
					id = CommonUtil.decimal(2, ++firstIndex);
					id = "-" + id;
					treeInfo = new HashMap<String, Object>();
					first = new HashMap<String, Object>();
					firstInfo = new HashMap<String, Object>();
					second = new HashMap<String, Object>();
					secondInfo = new HashMap<String, Object>();
					third = new HashMap<String, Object>();
					thirdInfo = new HashMap<String, Object>();
					forth = new HashMap<String, Object>();

					tree.put(po.getArea(), treeInfo);
					treeInfo.put("id", id);
					treeInfo.put("children", second);

					if (SessionUtil.isState() || po.getArea().equals("四川")) {
						isOpen = true;
					} else {
						isOpen = (firstIndex == 1 ? true : false);
					}
					isOpen = true;
					firstNode = new TreeBean(id, "0", po.getArea(), isOpen,1);
					list.add(firstNode);
					second.put(po.getCorridor(), secondInfo);
					secondInfo.put("id", id + "01");
					secondInfo.put("children", third);
					
					if(po.getCorridor().equals("汇总值")){
						secondNode = new TreeBean(id + "01", id, "交易汇总值", isOpen,1);
					}else{
						secondNode = new TreeBean(id + "01", id, po.getCorridor(), isOpen,sheetStatus.get(po.getCorridor()) ? "../img/icon/circle-gray.png":"../img/icon/circle-green.png",2);
					}
					list.add(secondNode);

					third.put(po.getDtype(), thirdInfo);
					thirdInfo.put("id", id + "01" + "01");
					thirdInfo.put("children", forth);
					if("电价".equals(po.getDtype())){
						thirdNode = new TreeBean(po.getId(), secondNode.getId(), po.getDtype(), isOpen,2);
					}else{
						thirdNode = new TreeBean(id + "01" + "01", secondNode.getId(), po.getDtype(), isOpen,1);
					}
					list.add(thirdNode);

					if(!CommonUtil.ifEmpty(po.getSide())){
						continue;
					}
					forth.put(po.getSide(), po.getId());
					forthNode = new TreeBean(po.getId(), thirdNode.getId(), po.getSide(), isOpen,1);
					forthNode.setArea(po.getArea());
					forthNode.setDrole(po.getSide());
					forthNode.setDtype(po.getDtype());

					list.add(forthNode);

				}
			}
		}
		return list;
	}

	public ResultPo getResultById(String id) {
		return issueDao.getResultById(id);
	}

	/**
	 * @author 车斯剑
	 */
	
	public Integer getAreaReportByMonthCount(String startTime, String endTime) {
		return issueDao.getAreaReportByMonthCount(startTime, endTime);
	}

	
	public List<AreaReportBean> getAreaReportByMonthPage(String startTime, String endTime, int start, int end) {
		// TODO Auto-generated method stub
		return issueDao.getAreaReportByMonthPage(startTime, endTime, start, end);
	}

	
	public Integer getAreaReportByYearCount(String startTime, String endTime) {
		// TODO Auto-generated method stub
		return issueDao.getAreaReportByYearCount(startTime, endTime);
	}

	
	public List getLineReportByMonthPage(String startTime, String endTime, int start, int end) {
		// TODO Auto-generated method stub
		return issueDao.getLineReportByMonthPage(startTime, endTime, start, end);
	}

	
	public List getLineReportByYearPage(String startTime, String endTime, int start, int end) {
		// TODO Auto-generated method stub
		return issueDao.getLineReportByYearPage(startTime, endTime, start, end);
	}

	
	public Integer getLineReportByMonthCount(String startTime, String endTime) {
		// TODO Auto-generated method stub
		return issueDao.getLineReportByMonthCount(startTime, endTime);
	}

	
	public Integer getLineReportByYearCount(String startTime, String endTime) {
		// TODO Auto-generated method stub
		return issueDao.getLineReportByYearCount(startTime, endTime);
	}

	
	public Map<String, Map<String, String>> getExcelDeclareData(String mdate) {
		// TODO Auto-generated method stub
		Map<String, Map<String, String>> result = new HashMap<String, Map<String, String>>();
		List<DeclarePo> pos = declareService.getListAndDataByMdate(mdate, null,null);
		Map<String, String> info = null;
		List<DeclareExtraPo> dexs = null;
		if (pos != null) {
			for (DeclarePo po : pos) {
				if (result.containsKey(po.getArea())) {
					info = result.get(po.getArea());
				} else {
					info = new HashMap<String, String>();
					info.put("drole", po.getDrloe() == "buy" ? "买方" : "卖方");
					// info.put("maxPrice", "0");
					// info.put("minPrice", "0");
					info.put("sumPower", "0");
					result.put(po.getArea(), info);
				}
				dexs = po.getDeclareExtras();
				// System.out.println(CommonUtil.objToJson(dexs));
				if (dexs != null) {
					for (DeclareExtraPo dex : dexs) {
						if (info.get("maxPrice") == null) {
							info.put("maxPrice", String.valueOf(dex.getPrice()));
						} else {
							if (dex.getPrice() > Float.parseFloat(info.get("maxPrice"))) {
								info.put("maxPrice", String.valueOf(dex.getPrice()));
							}
						}
						if (info.get("minPrice") == null) {
							info.put("minPrice", String.valueOf(dex.getPrice()));
						} else {
							if (dex.getPrice() < Float.parseFloat(info.get("minPrice"))) {
								info.put("minPrice", String.valueOf(dex.getPrice()));
							}
						}
						int startInterIndex = DeclareUtil.getIndex(dex.getStartTime());
						int endInterIndex = DeclareUtil.getIndex(dex.getEndTime());

						int interNum = endInterIndex - startInterIndex + 1;

						info.put("sumPower", String.valueOf((dex.getPower() * interNum + Float.parseFloat(info.get("sumPower")))));
					}
				}
			}
		}
		return result;
	}

	public Map<String, Map<String, Double>> getExcelPathResultData(String mdate) {
		Map<String, Map<String, Double>> result = new HashMap<String, Map<String, Double>>();
		List<PathResultPo> pros = pathResultDao.getSumResultByMdate(mdate);
		Map<String, Double> info = null;

		for (PathResultPo po : pros) {
			if (result.containsKey(po.getMpath())) {
				info = result.get(po.getMpath());
			} else {
				info = new HashMap<String, Double>();
				result.put(po.getMpath(), info);
			}
			info.put(po.getDtype(), po.getSum());
		}

		return result;
	}

	/**
	 * 导出日报时获取通道运行情况数据
	 * 
	 * @description
	 * @author 大雄
	 * @date 2016年9月12日下午4:16:45
	 * @param mdate
	 * @return
	 */
	public Map<String, Map<String, Double>> getExcelLineLimitResultData(String mdate) {
		Map<String, Map<String, Double>> result = new HashMap<String, Map<String, Double>>();
		List<LineLimitPo> list = lineLimitDao.getSumResultByMdate(mdate);
		Map<String, Double> info = null;

		for (LineLimitPo po : list) {
			if (result.containsKey(po.getMcorhr())) {
				info = result.get(po.getMcorhr());
			} else {
				info = new HashMap<String, Double>();
				result.put(po.getMcorhr(), info);
			}
			info.put(po.getDtype(), po.getSum());
		}
		return result;
	}

	public Map<String, Double> getExcelResultData(String mdate) {
		Map<String, Double> result = new HashMap<String, Double>();
		List<ResultPo> list = issueDao.getSumResultByMdate(mdate, null, Enums_DeclareType.DATATYPE_POWER);

		for (ResultPo po : list) {
			if (!result.containsKey(po.getArea())) {
				if (po.getArea().equals("四川")) {
					if (po.getSide().equals(Enums_DeclareType.DROLE_SEND)) {
						result.put(po.getArea(), po.getSumQ());
					}
				} else {
					if (po.getSide().equals(Enums_DeclareType.DROLE_RECV)) {
						result.put(po.getArea(), po.getSumQ());
					}
				}
			}
		}
		return result;
	}

	public List<ResultPo> getTreeForResult(String area, String mdate) {
		if (CommonUtil.ifEmpty(area)) {
			if (SessionUtil.isState()) {
				area = null;
			}
		}
		return issueDao.getTreeForResult(area, mdate, null,null);
	}

	/**
	 * 通过日期获取所有当前时间的交易结果字符串，导出到word
	 * 
	 * @description
	 * @author 大雄
	 * @date 2016年9月25日下午8:34:45
	 * @param time
	 * @return
	 */
	public Map<String, StringBuilder> getResultMap(String time) {
		Map<String, StringBuilder> map = new HashMap<String, StringBuilder>();
		StringBuilder tempSb = new StringBuilder();
		List<ResultPo> list = issueDao.selectResultListByParam(null, time.replaceAll("-", ""), null);
		if (list == null || list.size() == 0) {
			throw new MsgException("没有数据");
		}
		//String drole = null;
		String dtype = null;
		int indexTemp = 0;
		for (ResultPo po : list) {
			//System.out.println(po.getArea());
			if (po.getArea().equals("四川")) {
				if (!po.getSide().equals(Enums_DeclareType.DROLE_SEND)) {
					continue;
				}

			} else {
				
				if (!po.getSide().equals(Enums_DeclareType.DROLE_RECV)) {
					continue;
				}else{
					//continue;
				}
			}
			if (po.getDtype().equals(Enums_DeclareType.DATATYPE_POWER)) {
				dtype = "电量";
			} else {
				dtype = "电价";
			}
			indexTemp++;
			// System.out.println(CommonUtil.objToJson(po));
			Map<Integer, Integer> statusData = IssueUtil.getStatusData(po);
			// System.out.println(po.getArea() + "--" +
			// CommonUtil.objToJson(statusData));
			if (map.containsKey(po.getArea())) {
				tempSb = map.get(po.getArea());
				tempSb.append("\t\t " + dtype);
			} else {
				tempSb = new StringBuilder();
				map.put(po.getArea(), tempSb);
				tempSb.append( " " + dtype);
			}
			int num = 0;
			int startIndex = 1;
			int endIndex = 0;
			int intervalIndex = 1;

			for (Entry<Integer, Integer> entry : statusData.entrySet()) {
				if (intervalIndex == 1) {
					startIndex = 1;
				} else {
					int valuePre = statusData.get(startIndex);
					int valueNow = entry.getValue();
					// System.out.println("intervalIndex:"+intervalIndex+"   startIndex:"+startIndex+"  valuePre:"+valuePre+"    valueNow:"+valueNow);

					if ((valuePre - valueNow) != 0) {
						endIndex = intervalIndex - 1;

						String getAttributeMethodName = "getH" + CommonUtil.decimal(2, startIndex);
						Method getAttributeMethod = null;
						double value = 0d;
						try {

							getAttributeMethod = ResultPo.class.getDeclaredMethod(getAttributeMethodName);
							// System.out.println(po+"==="+getAttributeMethod);
							Object o = getAttributeMethod.invoke(po);
							if (o != null) {
								value = (Double) o;
							}
							StringBuilder tSb = new StringBuilder();
							boolean isNull = true;
							if (value != 0) {
								tSb.append("第" + startIndex + "时段到第" + endIndex + "时段:" + CommonUtil.toFix(po.getDtype().equals(Enums_DeclareType.DATATYPE_POWER) ? value / 4 : value) + "MWh").append(SystemTools._BR);
								isNull = false;
							}
							if (!isNull) {
								if (num == 0) {
									tempSb.append(" ");

								} else {
									tempSb.append("\t\t\t  ");
								}
								tempSb.append(tSb);
								num++;
							}
							startIndex = intervalIndex;
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
				intervalIndex++;
			}
			if (startIndex < intervalIndex) {
				endIndex = intervalIndex - 1;
				String getAttributeMethodName = "getH" + CommonUtil.decimal(2, startIndex);
				Method getAttributeMethod = null;
				double value = 0d;
				try {

					getAttributeMethod = ResultPo.class.getDeclaredMethod(getAttributeMethodName);
					// System.out.println(po+"==="+getAttributeMethod);
					Object o = getAttributeMethod.invoke(po);
					if (o != null) {
						value = (Double) o;
					}
					StringBuilder tSb = new StringBuilder();
					boolean isNull = true;
					if (value != 0) {
						tSb.append("第" + startIndex + "时段到第" + endIndex + "时段:" + CommonUtil.toFix(po.getDtype().equals(Enums_DeclareType.DATATYPE_POWER)?value/4:value ) + "MWh").append(SystemTools._BR);
						isNull = false;
					}
					if (!isNull) {
						if (num == 0) {
							tempSb.append(" ");
							
						} else {
							tempSb.append("\t\t\t  ");
						}
						tempSb.append(tSb);
						num++;
					}
					startIndex = intervalIndex;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		return map;
	}

	/**
	 * @author 车斯剑
	 */
	
	public void updateResult(ResultPo result) {
		// TODO Auto-generated method stub
		dealResultDao.updateResult(result);
	}

	/**
	 * 根据日期导出日报，如有是国调，则area is null，导出所有省，每个省一份word打包成zip 如果是省调则只导出word
	 * 
	 * @description
	 * @author 大雄
	 * @date 2016年9月26日下午2:12:24
	 * @param area
	 * @param mdate
	 * @param dateStr
	 * @param tempPath
	 * @return
	 * @throws IOException 
	 */
	public String exportDailyFile(String area, String mdate, String dateStr, String tempPath) throws IOException,MsgException {
		String fileName = null;
		List<DeclarePo> pos = declareService.getListAndDataByMdate(mdate, area,null);
		List<ResultPo> list = issueDao.selectResultListByParam(area, mdate, null);
		List<TransTielinePo> executeData = issueDao.getExecuteDataForArea(mdate,"",null);
		
		List<TransTielinePo> pathData = ServiceHelper.getBean(TransTielineDao.class).getTradeData(mdate,area);
		if (pos == null || pos.size() == 0) {
			throw new MsgException("没有申报数据");
		}
		if (list == null || list.size() == 0) {
			throw new MsgException("没有交易结果数据");
		}
		if (executeData == null || executeData.size() == 0) {
			throw new MsgException("没有执行结果数据");
		}
		
		//封装交易结果数据
		Map<String, Map<String, Map<String, ResultPo>>> resultMap = new HashMap<String, Map<String, Map<String, ResultPo>>>();
		Map<String, Map<String, ResultPo>> areaResultM = null;
		Map<String, ResultPo> resultM = null;
		//System.out.println("list=="+CommonUtil.objToJson(list));
		Map<String, ResultPo> salePrice = new HashMap<String, ResultPo>();
		for (ResultPo po : list) {
			if("sale".equals(po.getDrole()) && "电价".equals(po.getDtype()) && "汇总值".equals(po.getCorridor())){
				salePrice.put(po.getArea(), po);
			}
		}
		for (ResultPo po : list) {
			if (po.getSumQ()==0 && !"汇总值".equals(po.getCorridor())) {
				continue;
			}
			if(!"汇总值".equals(po.getCorridor())){
				if (resultMap.containsKey(po.getArea())) {
					areaResultM = resultMap.get(po.getArea());
				} else {
					areaResultM = new HashMap<String, Map<String,ResultPo>>();
					resultMap.put(po.getArea(), areaResultM);
					
				}
				if(areaResultM.containsKey(po.getCorridor())){
					resultM = areaResultM.get(po.getCorridor());
				}else{
					resultM = new HashMap<String, ResultPo>();
					areaResultM.put(po.getCorridor(), resultM);
				}
				
				if(salePrice.containsKey(po.getArea())){
					resultM.put(po.getDtype(), po);
					resultM.put("电价", salePrice.get(po.getArea()));
				}else{
					resultM.put(po.getDtype(), po);
				}
			}
			
		
		}
		
		//计算结果
		List<Map<String,List<Map<String, Float>>>> declareData = new ArrayList<Map<String,List<Map<String,Float>>>>();
		List<Map<String,Map<String, Map<String,Double>>>> dealData = new ArrayList<Map<String,Map<String,Map<String,Double>>>>();
		Map<String,List<Map<String, Float>>>  declareMap = null;
		Map<String,Map<String, Map<String,Double>>> dealMap = null;
		List<String> areas = new ArrayList<String>();
		List<UserPo> userIds = new ArrayList<UserPo>();
		//执行结果数据
		List<Map<String,Map<String,String>>> executeDatas = new ArrayList<Map<String,Map<String,String>>>();
		List<Map<String,Map<String,String>>> pathDatas = new ArrayList<Map<String,Map<String,String>>>();
		Map<String,Map<String,String>> exceteMap =null;
		Map<String,Map<String,String>> pathMap =null;
		for (DeclarePo po : pos) {
			declareMap = DeclareUtil.getDeclareTradeData(po);
			declareData.add(declareMap);
			//获取签名
			List<String> userIdList = declareService.getUserIdList(String.valueOf(po.getId()));
			if(userIdList==null||userIdList.size()==0){
				UserPo user = new UserPo();
				userIds.add(user);
			}else{
				UserPo user = ServiceHelper.getBean(UserDaoI.class).selectById(userIdList.get(0));
				userIds.add(user);
			}
			areas.add(po.getArea());
			//成交结果数据
			areaResultM = resultMap.get(po.getArea());
			//System.out.println("po.getArea()==="+po.getArea());
			//System.out.println("areaResultM===="+CommonUtil.objToJson(areaResultM));
			if(areaResultM!=null){
				dealMap = IssueUtil.getdealTradeData(areaResultM);
				dealData.add(dealMap);
			}else{
				dealData.add(null);
			}
						
		}
		
		//通道、执行结果
		List<TransTielinePo> lineExecuteDatas = null;
		List<TransTielinePo> lineDatas = null;
		Map<String,List<TransTielinePo>> linePathMap = new HashMap<String, List<TransTielinePo>>();
		Map<String,List<TransTielinePo>> executeMap = new HashMap<String, List<TransTielinePo>>();
		List<String> linePaths = new ArrayList<String>();
		List<String> lineExecutes = new ArrayList<String>();
		
		for (TransTielinePo tielinePo : executeData) {
			if(!executeMap.isEmpty() && executeMap.containsKey(tielinePo.getTielineName())){
				lineExecuteDatas = executeMap.get(tielinePo.getTielineName());
				
			}else{
				
				lineExecuteDatas = new ArrayList<TransTielinePo>();
				executeMap.put(tielinePo.getTielineName(), lineExecuteDatas);
			}
			//System.out.println("lineExecuteDatas=="+lineExecuteDatas);
			lineExecuteDatas.add(tielinePo);
		}
		for (TransTielinePo tielinePo : pathData) {
			if(linePathMap.containsKey(tielinePo.getTielineName())){
				lineDatas = linePathMap.get(tielinePo.getTielineName());
				
			}else{
				lineDatas = new ArrayList<TransTielinePo>();
				linePathMap.put(tielinePo.getTielineName(), lineDatas);
			}
			lineDatas.add(tielinePo);
		}
		//计算
		for (Entry<String,List<TransTielinePo>> entry : linePathMap.entrySet()) {
			pathMap =IssueUtil.getExecuteTradeData(entry.getValue());
			pathDatas.add(pathMap);
			linePaths.add(entry.getKey());
		}
		for (Entry<String,List<TransTielinePo>> entry : executeMap.entrySet()) {
			exceteMap =IssueUtil.getExecuteTradeData(entry.getValue());
			executeDatas.add(exceteMap);
			lineExecutes.add(entry.getKey());
		}

		List<UserPo> userList = null;
		//获取出清发布的签名
		List<UserPo> issueUserGraList = declareDao.getUserGraPhPoListByMdateAndType(mdate, Enums_SystemConst.SIGN_TYPE_ISSUE.getValue());
		
		List<UserPo> dealUserGraList = null;
		/*if(dealResult != null){
			//获取执行结果修改的签名
			dealUserGraList = declareDao.getUserGraPhPoListBySheetUid(dealResult.getId());
			
		}*/
		String fileNameTemp = WordUtil.exportDoc(userIds,areas,tempPath, dateStr, declareData, dealData, pathDatas,executeDatas,userList,issueUserGraList,dealUserGraList);
		//System.out.println("-----------"+CommonUtil.objToJson(executeDatas));
		return fileNameTemp;
	}
	

	/**
	 * 
	 * @author 车斯剑
	 * @date 2016年10月15日下午1:46:31
	 * @param ids
	 * @return
	 */
	
	public double getResultByIds(List<String> ids) {
		
		String[] idss = new String[ids.size()];
		ids.toArray(idss);
		ResultPo po = issueDao.getResultByIds(idss);
		return po==null?0:po.getSumQ();
	}

	/**
	 * 获取日报左边树
	 * @author 车斯剑
	 * @date 2016年10月17日下午2:03:13
	 * @param area
	 * @param time
	 * @param status
	 * @return
	 */
	
	public List<TreeBean> getDailyTree(String area, String mdate, Integer status) {
		List<TreeBean> list = new ArrayList<TreeBean>();
		if (CommonUtil.ifEmpty(area)) {
			if (SessionUtil.isState()) {
				area = null;
			}
		}
		List<ResultPo> rpos = issueDao.getTreeForResult(area, mdate, status,null);
		
		TreeBean secondNode = null;
		boolean isOpen = false;
		
		if (CommonUtil.ifEmpty_List(rpos)) {
			TreeBean firstNode1 = new TreeBean("01", "0", "送端电力出清结果", true);
			TreeBean firstNode2 = new TreeBean("02", "0", "送端电价出清结果", true);
			TreeBean firstNode3 = new TreeBean("03", "0", "送端电力出清结果详细", true);
			TreeBean firstNode4 = new TreeBean("04", "0", "受端电力出清结果", true);
			TreeBean firstNode5 = new TreeBean("05", "0", "受端电价出清结果", true);
			list.add(firstNode1);
			list.add(firstNode2);
			list.add(firstNode3);
			list.add(firstNode4);
			list.add(firstNode5);
			for (ResultPo po : rpos) {
				if(!po.getCorridor().equals("汇总值")){
					if("sale".equals(po.getDealer())&&"电力".equals(po.getDtype())){
						secondNode = new TreeBean(po.getId(), "01", po.getCorridor(), isOpen);
						list.add(secondNode);
					}else if("sale".equals(po.getDealer())&&"电价".equals(po.getDtype())){
						secondNode = new TreeBean(po.getId(), "02", po.getCorridor(), isOpen);
						list.add(secondNode);
					}else if("buy".equals(po.getDealer())&&"电力".equals(po.getDtype())&&"送电侧".equals(po.getSide())){
						secondNode = new TreeBean(po.getId(), "03", po.getCorridor(), isOpen);
						list.add(secondNode);
					}else if("buy".equals(po.getDealer())&&"电力".equals(po.getDtype())&&"受电侧".equals(po.getSide())){
						secondNode = new TreeBean(po.getId(), "04", po.getCorridor(), isOpen);
						list.add(secondNode);
					}else if("buy".equals(po.getDealer())&&"电价".equals(po.getDtype())&&"受电侧".equals(po.getSide())){
						secondNode = new TreeBean(po.getId(), "05", po.getCorridor(), isOpen);
						list.add(secondNode);
					}
				}
			
			}
		}
		return list;
	}

	/**
	 * 导出周报组装获取成交数据
	 * @author 车斯剑
	 * @date 2016年10月19日上午9:33:05
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public Map<String, Object> getDealDatasByMdateInterval(String startTime,String endTime) {
		StringBuilder logContent = new StringBuilder(); //过程日志记录
		String _N = "\r\n"; 
		 List<ResultPo> rpos = issueDao.getResultByTime(startTime, endTime);
		 Map<String, Map<String, Map<String,Map<String,ResultPo>>>> dealMap = new HashMap<String, Map<String,Map<String,Map<String,ResultPo>>>>();
		 Map<String, Map<String, Double>> DealData = new HashMap<String, Map<String, Double>>();
		 Map<String, Object> result = new HashMap<String, Object>();
		 double dealPower = 0; //交易成交电量总和
		 double dealPrice  = 0; //出清电价总和
		 if(CommonUtil.ifEmpty(rpos)){
			for (ResultPo po : rpos) {
				if((po.getSumQ()==null || po.getSumQ()==0) && !"汇总值".equals(po.getCorridor())){
					continue;
				}
				Map<String, Map<String,Map<String,ResultPo>>> dayMap = null;
				Map<String,Map<String,ResultPo>> sheetList = null;
				Map<String, ResultPo> valueMap = null;
				
				if(dealMap.containsKey(po.getArea())){
					dayMap = dealMap.get(po.getArea());
				}else{
					dayMap = new HashMap<String, Map<String,Map<String,ResultPo>>>();
					dealMap.put(po.getArea(), dayMap);
				}
				if (dayMap.containsKey(po.getMdate())) {
					sheetList = dayMap.get(po.getMdate());
					if(sheetList.containsKey(po.getCorridor())){
						valueMap = sheetList.get(po.getCorridor());
						if("电力".equals(po.getDtype())){
							valueMap.put("电力", po);
						}else{
							valueMap.put("电价", po);
						}
					}else{
						valueMap = new HashMap<String, ResultPo>();
						if("电力".equals(po.getDtype())){
							valueMap.put("电力", po);
						}else{
							valueMap.put("电价", po);
						}
						sheetList.put(po.getCorridor(), valueMap);
					}
					
					
				} else {
					sheetList = new HashMap<String, Map<String,ResultPo>>();
					valueMap = new HashMap<String, ResultPo>();
					if("电力".equals(po.getDtype())){
						valueMap.put("电力", po);
					}else{
						valueMap.put("电价", po);
					}
					sheetList.put(po.getCorridor(), valueMap);
					dayMap.put(po.getMdate(), sheetList);
					
				}
				
			}
			
			for (Entry<String, Map<String, Map<String,Map<String,ResultPo>>>> area : dealMap.entrySet()) {
				Map<String, Map<String,Map<String,ResultPo>>> dayMap = area.getValue();
				int dayLen = dayMap.size();
				double daySumPower = 0;
				double daySumPrice = 0;
				double areasumQuantity = 0;//地区累计成交电量
				for (Entry<String,  Map<String,Map<String,ResultPo>>> dayEnt : dayMap.entrySet()) {
					int sheetLen = dayEnt.getValue().size();
					double sumPower = 0;
					double sumPrice = 0;
					
					Map<String,Map<String,ResultPo>> valueMap = dayEnt.getValue();
					for (Entry<String,Map<String,ResultPo>> entry : valueMap.entrySet()) {
						
						Map<String, Double> resultMap = ResultUtil.getResultValueMap(entry.getValue());
						sumPower += resultMap.get("powerAvg");
						sumPrice += resultMap.get("priceAvg");
						areasumQuantity += resultMap.get("sumQuantity");
						dealPower += resultMap.get("dealPower");
						logContent.append("resultMap==="+CommonUtil.objToJson(resultMap)+_N);
					}

					daySumPower += (sheetLen == 0? 0:sumPower / sheetLen);
					daySumPrice += (sheetLen == 0? 0:sumPrice / sheetLen);


				}
				String areaDrole = areaDao.getDroleByArea(area.getKey());//查询地区的买卖属性
				if("sale".equals(areaDrole)){
					//dealPower += areasumQuantity;
					dealPrice += (dayLen==0? 0:daySumPrice / dayLen);
				}
				
				Map<String, Double> attrMap = new HashMap<String, Double>();
				attrMap.put("power", new BigDecimal(daySumPower).divide(new BigDecimal(dayLen),2, BigDecimal.ROUND_HALF_UP).doubleValue());
				attrMap.put("price", Double.valueOf(CommonUtil.toFix(dayLen==0? 0:daySumPrice / dayLen)));
				attrMap.put("quantity", Double.valueOf(CommonUtil.toFix(areasumQuantity)));
				DealData.put(area.getKey(), attrMap);
			}
		 }
		 result.put("dealPower", dealPower);
		 result.put("dealPrice", dealPrice);
		 result.put("DealData", DealData);
		 logContent.append("result==="+CommonUtil.objToJson(result)+_N);
		 FileManager.writeToFile("D:\\temp\\WeekLog_CBPM.txt", logContent.toString(), false);
		 return result;
	}

	/**
	 * 获取日报数据
	 * @author 车斯剑
	 * @date 2016年10月23日上午10:28:16
	 * @param area
	 * @param mdate
	 * @return
	 */
	public Map<String, List<ResultPo>> getDailyResults(String area, String mdate, Integer status) {
		if (CommonUtil.ifEmpty(area)) {
			if (SessionUtil.isState()) {
				area = null;
			}
		}
		List<ResultPo> rpos = issueDao.getTreeForResult(area, mdate, status,null);
		
		Map<String,List<ResultPo>> dailyDatas = new HashMap<String, List<ResultPo>>();
		List<ResultPo> salePowers = new ArrayList<ResultPo>();
		List<ResultPo> salePrices = new ArrayList<ResultPo>();
		List<ResultPo> saleEachPower = new ArrayList<ResultPo>();
		List<ResultPo> buyPowers = new ArrayList<ResultPo>();
		List<ResultPo> buyPrices = new ArrayList<ResultPo>();
		
		if (CommonUtil.ifEmpty_List(rpos)) {
			for (ResultPo po : rpos) {
				if(!po.getCorridor().equals("汇总值")){
					if("sale".equals(po.getDrole())&&"电力".equals(po.getDtype())){
						ResultPo result = issueDao.getResultById(po.getId());
						salePowers.add(result);
					}else if("sale".equals(po.getDrole())&&"电价".equals(po.getDtype())){
						ResultPo result = issueDao.getResultById(po.getId());
						salePrices.add(result);
					}else if("buy".equals(po.getDrole())&&"电力".equals(po.getDtype())&&"送电侧".equals(po.getSide())){
						ResultPo result = issueDao.getResultById(po.getId());
						saleEachPower.add(result);
					}else if("buy".equals(po.getDrole())&&"电力".equals(po.getDtype())&&"受电侧".equals(po.getSide())){
						ResultPo result = issueDao.getResultById(po.getId());
						buyPowers.add(result);
					}else if("buy".equals(po.getDrole())&&"电价".equals(po.getDtype())&&"受电侧".equals(po.getSide())){
						ResultPo result = issueDao.getResultById(po.getId());
						buyPrices.add(result);
					}
				}
				
			}
		}
		dailyDatas.put("送端电力出清结果", salePowers);
		dailyDatas.put("送端电价出清结果", salePrices);
		dailyDatas.put("送端电力出清结果详细", saleEachPower);
		dailyDatas.put("受端电力出清结果", buyPowers);
		dailyDatas.put("受端电价出清结果", buyPrices);
		return dailyDatas;
	}
	/**
	 * 发布成功后更新数据状态
	 * @param mdate
	 */
	public void updateIssueStatus(String mdate){
		issueDao.updatePrint(mdate, "1");
		issueDao.updateTransTieLine(mdate, "1");
	}

	/**
	 * 获取地区交易结果
	 * @author 车斯剑
	 * @date 2016年12月14日上午10:10:30
	 * @param area
	 * @param time
	 * @return
	 */
	public List<ResultPo> getResultForAreaByTime(String area, String time) {
		return issueDao.getResultForAreaByTime(area,time);
	}

	/**
	 * 获取交易单地区
	 * @author 车斯剑
	 * @date 2016年12月15日上午11:06:40
	 * @param area
	 * @param time
	 * @return
	 */
	public List<String> getAreaForTradeByTime(String time) {
		return issueDao.getAreaForTradeByTime(time);
	}

	/**
	 * 获取执行结果左边树
	 * @author 车斯剑
	 * @date 2016年12月15日下午3:55:53
	 * @param area
	 * @param time
	 * @param status
	 * @return
	 */
	public List<TreeBean> getExecuteTree(String area, String time,Integer status) {

		List<TransTielinePo> lineList = issueDao.getExecuteTree(time,area, status);
		List<TreeBean> list=new ArrayList<TreeBean>();
		TreeBean firstNode = null;
		TreeBean secondNode = null;
		Map<String, Map<String, Object>> tree = new HashMap<String, Map<String, Object>>();
		
		Map<String, Object> treeInfo = null;
		Map<String, Object> first = null;
		Map<String, Object> firstInfo = null;
		Map<String, Object> second = null;
		Map<String, Object> secondInfo = null;
		String id;
		int firstIndex = 0;
		boolean isOpen = false;
		if (CommonUtil.ifEmpty_List(lineList)) {
			for (TransTielinePo po : lineList) {
				
				if (tree.containsKey(po.getTielineName())) {
					if (SessionUtil.isState()) {
						isOpen = true;
					} else {
						isOpen = (firstIndex == 1 ? true : false);
					}
					isOpen = true;
					second = (Map<String, Object>) tree.get(po.getTielineName()).get("children");
					id = String.valueOf(tree.get(po.getTielineName()).get("id"));

					if (second.containsKey(po.getTransCorridorName())) {
						secondInfo = (Map<String, Object>) second.get(po.getTielineName());
						
					} else {

						secondInfo = new HashMap<String, Object>();
						second.put(po.getTransCorridorName(), secondInfo);
						secondInfo.put("id", po.getId());
						
						secondNode = new TreeBean(po.getId(), id, po.getTransCorridorName(), isOpen);
						list.add(secondNode);
					}
				} else {
					// 送电侧或者受电侧
					id = CommonUtil.decimal(2, ++firstIndex);
					id = "-" + id;
					treeInfo = new HashMap<String, Object>();
					first = new HashMap<String, Object>();
					firstInfo = new HashMap<String, Object>();
					second = new HashMap<String, Object>();
					secondInfo = new HashMap<String, Object>();

					tree.put(po.getTielineName(), treeInfo);
					treeInfo.put("id", id);
					treeInfo.put("children", second);

					if (SessionUtil.isState()) {
						isOpen = true;
					} else {
						isOpen = (firstIndex == 1 ? true : false);
					}
					isOpen = true;
					firstNode = new TreeBean(id, "0", po.getTielineName(), isOpen);
					list.add(firstNode);
					second.put(po.getTransCorridorName(), secondInfo);
					secondInfo.put("id", po.getId());
					secondNode = new TreeBean(po.getId(), id, po.getTransCorridorName(), isOpen);
					list.add(secondNode);
				}
			}
		}
		return list;
	}

	/**
	 * 根据Id获取执行结果数据
	 * @author 车斯剑
	 * @date 2016年12月15日下午4:42:28
	 * @param id
	 * @return
	 */
	public TransTielinePo getExecuteResultById(String id) {
		return issueDao.getExecuteResultById(id);
	}

	/**
	 * 获取地区执行结果数据
	 * @author 车斯剑
	 * @date 2016年12月15日下午5:46:52
	 * @param time
	 * @param area
	 * @param lines
	 * @return
	 */
	public List<TransTielinePo> getExecuteDataForArea(String time, String area,List<String> lines) {
		
		return issueDao.getExecuteDataForArea(time,area,lines);
	}
}
