package com.state.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.state.dao.DealResultDaoI;
import com.state.po.ResultPo;
import com.state.po.TreeBean;
import com.state.service.DealResultServiceI;
import com.state.util.CommonUtil;
import com.state.util.SessionUtil;
@Service
@Transactional
public class DealResultServiceImpl implements DealResultServiceI{
	

	@Autowired
	private DealResultDaoI dealResultDao;
	
	public ResultPo getResultById(String id) {
		return dealResultDao.getResultById(id);
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

		List<ResultPo> rpos = dealResultDao.getTreeForResult(area, mdate, status);
		// System.out.println(area+"=="+mdate+"=="+status+"===="+CommonUtil.objToJson(rpos));
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
		// if()
		if (CommonUtil.ifEmpty_List(rpos)) {
			for (ResultPo po : rpos) {
				/*
				 * if(i==6){ break; } i++;
				 */

				// System.out.println("===list==="+CommonUtil.objToJson(list));
				// System.out.println("===22==="+CommonUtil.objToJson(tree));

				// System.out.println("===1==="+CommonUtil.objToJson(po));
				if (tree.containsKey(po.getArea())) {
					// System.out.println("===2==="+CommonUtil.objToJson(tree.get(po.getDrloe())));
					if (SessionUtil.isState() || po.getArea().equals("四川")) {
						isOpen = true;
					} else {
						isOpen = (firstIndex == 1 ? true : false);
					}
					isOpen = true;
					second = (Map<String, Object>) tree.get(po.getArea()).get("children");
					id = String.valueOf(tree.get(po.getArea()).get("id"));
					// System.out.println("===3==="+CommonUtil.objToJson(second));

					if (second.containsKey(po.getSheetName())) {
						secondInfo = (Map<String, Object>) second.get(po.getSheetName());
						third = (Map<String, Object>) secondInfo.get("children");
						// System.out.println("===4==="+CommonUtil.objToJson(third));
						if (third.containsKey(po.getDtype())) {
							thirdInfo = (Map<String, Object>) third.get(po.getDtype());
							forth = (Map<String, Object>) thirdInfo.get("children");
							if (forth.containsKey(po.getSide())) {

							} else {
								forth.put(po.getSide(), po.getId());
								forthNode = new TreeBean(po.getId(), String.valueOf(thirdInfo.get("id")), po.getSide(), isOpen);
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
							thirdNode = new TreeBean(String.valueOf(thirdInfo.get("id")), String.valueOf(secondInfo.get("id")), po.getDtype(), isOpen);
							list.add(thirdNode);

							forth.put(po.getSide(), po.getId());
							forthNode = new TreeBean(po.getId(), thirdNode.getId(), po.getSide(), isOpen);
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

						second.put(po.getSheetName(), secondInfo);

						secondInfo.put("id", id + CommonUtil.decimal(2, second.size()));
						secondInfo.put("children", third);
						secondNode = new TreeBean(String.valueOf(secondInfo.get("id")), id, po.getSheetName(), isOpen);
						list.add(secondNode);

						third.put(po.getDtype(), thirdInfo);
						thirdInfo.put("id", secondNode.getId() + "01");
						thirdInfo.put("children", forth);
						thirdNode = new TreeBean(secondNode.getId() + "01", secondNode.getId(), po.getDtype(), isOpen);
						list.add(thirdNode);

						forth.put(po.getSide(), po.getId());
						forthNode = new TreeBean(po.getId(), thirdNode.getId(), po.getSide(), isOpen);
						forthNode.setArea(po.getArea());
						forthNode.setDrole(po.getSide());
						forthNode.setDtype(po.getDtype());

						list.add(forthNode);
						// System.out.println("===5==="+CommonUtil.objToJson(second));
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
					firstNode = new TreeBean(id, "0", po.getArea(), isOpen);
					list.add(firstNode);
					second.put(po.getSheetName(), secondInfo);
					secondInfo.put("id", id + "01");
					secondInfo.put("children", third);
					secondNode = new TreeBean(id + "01", id, po.getSheetName(), isOpen);
					list.add(secondNode);

					third.put(po.getDtype(), thirdInfo);
					thirdInfo.put("id", id + "01" + "01");
					thirdInfo.put("children", forth);
					thirdNode = new TreeBean(id + "01" + "01", secondNode.getId(), po.getDtype(), isOpen);
					list.add(thirdNode);

					forth.put(po.getSide(), po.getId());
					forthNode = new TreeBean(po.getId(), thirdNode.getId(), po.getSide(), isOpen);
					forthNode.setArea(po.getArea());
					forthNode.setDrole(po.getSide());
					forthNode.setDtype(po.getDtype());

					list.add(forthNode);

				}
			}
		}
		// System.out.println(CommonUtil.objToJson(tree));
		return list;
	}
	
	/**
	 * 
	 * @author 车斯剑
	 * @date 2016年10月12日上午7:48:09
	 * @param area
	 * @param mdate
	 * @return
	 */
	
	public List<ResultPo> getTreeForResult(String area, String mdate) {
		if (CommonUtil.ifEmpty(area)) {
			if (SessionUtil.isState()) {
				area = null;
			}
		}

		List<ResultPo> rpos = dealResultDao.getTreeForResult(area, mdate, null);
		return rpos;
	}

	/**
	 * 
	 * @author 车斯剑
	 * @date 2016年10月18日下午2:34:33
	 * @param string
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public List<ResultPo> getResultByTime(String area, String startTime,String endTime) {
		if (CommonUtil.ifEmpty(area)) {
			if (SessionUtil.isState()) {
				area = null;
			}
		}
		List<ResultPo> rpos = dealResultDao.getResultByTime(area, startTime,endTime, null);
		return rpos;
	}
}
