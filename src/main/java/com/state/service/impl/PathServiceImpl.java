package com.state.service.impl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.state.dao.IPathDefineDao;
import com.state.dao.IPathResultDao;
import com.state.dao.TransTielineDao;
import com.state.po.PathDefinePo;
import com.state.po.PathResultPo;
import com.state.po.TransTielinePo;
import com.state.po.TreeBean;
import com.state.service.IPathService;
import com.state.util.CommonUtil;

@Service
public class PathServiceImpl implements IPathService {

	@Autowired
	private IPathDefineDao pathDefineDao;
	@Autowired
	private IPathResultDao pathResultDao;
	@Autowired
	private TransTielineDao transTielineDao;
	/**
	 * 获取所有的通道定义数据
	 * @return
	 */
	public List<PathDefinePo> getAllPath(String time){
		List<PathDefinePo> list = pathDefineDao.selectPathALL(time);
		return list;
	}
	
	/**
	 * 根据通道名判断通道是否存在
	 * @param mpath
	 * @return
	 */
	public boolean existsPath(String id){
		return pathDefineDao.countPathByName(id) > 0;
	}
	
	/**
	 * 添加通道定义
	 * @param pathDefine
	 */
	public void addPath(PathDefinePo pathDefine){
		pathDefineDao.insertPathDefine(pathDefine);
	}
	
	/**
	 * 删除通道
	 * @param mpath
	 */
	public void deletePath(String pathId){
		pathDefineDao.deletePathDefine(pathId);
	}
	/**
	 * 统计通道个数
	 * @param mpath
	 */
	public int getPathNum() {
		// TODO Auto-generated method stub
		return pathDefineDao.getPathNum();
	}
	
	String[] removeItem = {"正向限额","反向限额","受端电力","送端电力"};
	String[] sendItems = {"送端上网费用","省内输电费用","跨区输电费用","从送端累加的总费用"};
	String[] sendArray = {"电厂上网费用","省内输电费用","跨区输电费用","总加费用"};
	String[] sendArray1 = {"电厂上网费用","省内输电费用","跨区输电费用","总加费用","华中输电费用","西北输电费用","德宝输电费用","宁东输电费用"};
	String[] sendItems1 = {"送端上网费用","省内输电费用","跨区输电费用","从送端累加的总费用","华中输电费用","西北输电费用","德宝输电费用","宁东输电费用"};
	String[] receiveItems = {"受端总费用"};
	public List<TreeBean> getTree(String mdate,Integer status){
		Map<String,Integer> sendMap = new HashMap<String, Integer>();
		Map<String,Integer> sendMap1 = new HashMap<String, Integer>();
		
		
		
		Vector<String> removeVector = new Vector<String>();
		Vector<String> sendVector = new Vector<String>();
		Vector<String> sendVector1 = new Vector<String>();
		Vector<String> receiveVector = new Vector<String>();
		for(int i=0;i<removeItem.length;i++){
			removeVector.add(removeItem[i]);
		}
		for(int i=0;i<sendItems.length;i++){
			sendVector.add(sendItems[i]);
		}
		for(int i=0;i<sendArray.length;i++){
			sendMap.put(sendArray[i], (i+1));
		}
		for(int i=0;i<sendArray1.length;i++){
			sendMap1.put(sendArray1[i], (i+1));
		}
		
		for(int i=0;i<sendItems1.length;i++){
			sendVector1.add(sendItems1[i]);
			sendMap1.put(sendItems1[i], (i+1));
		}
		for(int i=0;i<receiveItems.length;i++){
			receiveVector.add(receiveItems[i]);
		}
		
		List<TreeBean> list=new ArrayList();
		
		List<PathResultPo> rpos = pathResultDao.getTreeForResult(mdate,status);
		//System.out.println(CommonUtil.objToJson(rpos));
		int firstIndex = 0;
		Map<String,Map<String,Object>> tree = new HashMap<String,Map<String,Object>>();
		TreeBean firstNode = null;
		TreeBean secondNode = null;
		TreeBean thridNode = null;
		Map<String,Object> treeInfo = null;
		Map<String,Object> first = null;
		Map<String,Object> firstInfo = null;
		Map<String,Object> second = null;
		Map<String,Object> secondInfo = null;
		String id ;
		if(CommonUtil.ifEmpty_List(rpos)){
			for(PathResultPo po : rpos){
				if(tree.containsKey(po.getMpath())){
					//System.out.println("===2==="+CommonUtil.objToJson(tree.get(po.getDrloe())));
					second = (Map<String,Object>)tree.get(po.getMpath()).get("children");
					id = String.valueOf(tree.get(po.getMpath()).get("id"));
					
					if(second.containsKey(po.getDtype())){
						//secondInfo = (Map<String,Object>)second.get(po.getDtype());
					}else{
						//second = new HashMap<String,Object>();
						secondInfo = new HashMap<String,Object>();
						second.put(po.getDtype(), po.getId());
						
						secondNode = new TreeBean(po.getId(), id, po.getDtype(),false);
						list.add(secondNode);
						
					}
				}else{
					//送电侧或者受电侧
					id = CommonUtil.decimal(2, ++firstIndex);
					id = "-"+id;
					treeInfo = new HashMap<String,Object>();
					first = new HashMap<String,Object>();
					firstInfo  = new HashMap<String,Object>();
					second = new HashMap<String,Object>();
					
					tree.put(po.getMpath(), treeInfo);
					treeInfo.put("id", id);
					treeInfo.put("children", second);
					
					firstNode = new TreeBean(id, "0", po.getMpath(),firstIndex== 1?true:false);
					list.add(firstNode);
					second.put(po.getDtype(), po.getId());
					
					secondNode = new TreeBean(po.getId(), id, po.getDtype(),firstIndex== 1?true:false);
					list.add(secondNode);
					
					
				}
			}
		}
		TreeBean sendBean = new TreeBean("11", "-01", "送端", true);
//		Attributes attributes = new Attributes();
//		attributes.setRname("8888");
//		sendBean.setAttributes(attributes);
		list.add(sendBean);
		TreeBean receiveBean = new TreeBean("22", "-01", "受端", true);
		list.add(receiveBean);
		
		TreeBean sendBean1 = new TreeBean("33", "-02", "送端", false);
		list.add(sendBean1);
		TreeBean receiveBean1 = new TreeBean("44", "-02", "受端", false);
		list.add(receiveBean1);
		
		for(int i=list.size()-1;i>=0;i--){
			TreeBean bean = list.get(i);
			if(removeVector.contains(bean.getName())&&(bean.getPID().equals("-01")||bean.getPID().equals("-02")||bean.getPID().equals("-03")||bean.getPID().equals("-04"))){
				list.remove(bean);
			}
			if(bean.getPID().equals("-01")){
				if(sendVector.contains(bean.getName())){
					if(bean.getName().equals("从送端累加的总费用")){
						bean.setName("总加费用");
					}
					if(bean.getName().equals("送端上网费用")){
						bean.setName("电厂上网费用");
					}
					bean.setPID("11");
				}
				if(receiveVector.contains(bean.getName())){
					if(bean.getName().equals("受端总费用")){
						bean.setName("受端购电费用");
					}
					if(bean.getName().equals("送端上网费用")){
						bean.setName("电厂上网费用");
					}
					bean.setPID("22");
				}
			}
			
			if(bean.getPID().equals("-02")){
				if(sendVector1.contains(bean.getName())){
					if(bean.getName().equals("从送端累加的总费用")){
						bean.setName("总加费用");
					}
					bean.setPID("33");
				}
				if(receiveVector.contains(bean.getName())){
					if(bean.getName().equals("受端总费用")){
						bean.setName("受端购电费用");
					}
					bean.setPID("44");
				}
			}
			
		}
//		List<TreeBean> newlist = new ArrayList<TreeBean>();
//		for(TreeBean bean :list){
//			if(bean.getId().equals("-01")||bean.getPID().equals("-01")){
//				list.remove(bean);
//				
//			}
//			
//		}
		
		return list;
	}
	
	
	/**
	 * 发布界面获取交易通道各种类型数据
	 * @description
	 * @author 大雄
	 * @date 2016年8月27日下午2:49:24
	 * @param area
	 * @param time
	 * @return
	 */
	public PathResultPo getResultById(String id){
		return pathResultDao.getResultById(id);
	}

	/**
	 * 根据Id获取交易通道信息
	 */
	public PathDefinePo getPathDefineById(String pathId) {
		return pathDefineDao.getPathDefineById(pathId);
	}

	
	public void updatePathDefine(PathDefinePo pdp) {
		// TODO Auto-generated method stub
		pathDefineDao.updatePathDefine(pdp);
	}
	
	public Map<String,Map<String,Map<String,Object>>> getResultByMdateAndDtype(String mdate){
		//String[] dtypes = {"电力"};
		//List<IntraPathResultPo> list = pathResultDao.getResultByMdateAndDtype(mdate, dtypes);
		List<TransTielinePo> list = transTielineDao.getTree(mdate,null,null);
		Map<String,Map<String,Map<String,Object>>> data = new HashMap<String,Map<String,Map<String,Object>>>();
		Map<String,Map<String,Object>> intervalMap = null;
		if(list != null){
			for(TransTielinePo po : list){
				//String dtype = "电力";
				String dtype = po.getTransCorridorName();
				if(data.containsKey(po.getTielineName())){
					intervalMap = data.get(po.getTielineName());
				}else{
					intervalMap = new HashMap<String,Map<String,Object>>();
					data.put(po.getTielineName(), intervalMap);
				}
				for (int j = 1; j <= 96; j++) {// 映射存储96时段
					String getAttributeMethodName = "getH" + (j < 10 ? "0" + j : j);
					Method getAttributeMethod = null;
					String key = String.valueOf(j);
					try {
						getAttributeMethod = TransTielinePo.class.getDeclaredMethod(getAttributeMethodName);
						try {
							Object value = getAttributeMethod.invoke(po);
							if(intervalMap.containsKey(key)){
								intervalMap.get(key).put(dtype, value);
							}else{
								Map<String,Object> temp = new HashMap<String,Object>();
								temp.put(dtype, value);
								intervalMap.put(key, temp);
							}
						} catch (IllegalAccessException e) {
							e.printStackTrace();
						} catch (IllegalArgumentException e) {
							e.printStackTrace();
						} catch (InvocationTargetException e) {
							e.printStackTrace();
						}
					} catch (NoSuchMethodException e) {
						e.printStackTrace();
					} catch (SecurityException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return data;
	}

	public List<PathDefinePo> selectPathByName(String time, String mpath) {
		return pathDefineDao.selectPathByName(time, mpath);
	}
	
}
