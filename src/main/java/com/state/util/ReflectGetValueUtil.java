package com.state.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import com.state.po.DeclareDataPo;
import com.state.po.PathResultPo;

public class ReflectGetValueUtil {
	/**
	 * 反射获取各个属性的值并返回一个vector
	 * @param lines
	 */
	public static Vector<Double> getValue(Object declareDataPo){
		Vector<Double> unitVector = new Vector<Double>();
		Field[] fields = declareDataPo.getClass().getDeclaredFields();
		for (Field field : fields) {
				if(field.getName().startsWith("h")){
					field.setAccessible(true);
					try {
						Field lineField = declareDataPo.getClass().getDeclaredField(field.getName());
						lineField.setAccessible(true);
						Double hh = (Double)lineField.get(declareDataPo);
						unitVector.add(hh);
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (SecurityException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (NoSuchFieldException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		
		return unitVector;
		
	}
	
	/**
	 * 反射获取各个属性的值
	 * @param lines
	 */
	public static Vector<Vector<String>> getValueByField(List<?> lines){
		Vector<Vector<String>> vec = new Vector<Vector<String>>();
		Vector<String> unitVector = null;
		if(null == lines || lines.isEmpty()){
			return null;
		}
		Object pathResultPo = lines.get(0);
		Field[] fields =  pathResultPo.getClass().getDeclaredFields();
		
		for (int i=0;i<lines.size();i++) {
			pathResultPo = lines.get(i);
			unitVector = new Vector<String>();
			for (Field field : fields) {
				if(field.getName().startsWith("h")){
					field.setAccessible(true);
					try {
					
						Field lineField = pathResultPo.getClass().getDeclaredField(field.getName());
						lineField.setAccessible(true);
						Double hh = (Double)lineField.get(pathResultPo);
						unitVector.add(hh+"");
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (SecurityException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (NoSuchFieldException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			if(pathResultPo instanceof DeclareDataPo){
				unitVector.insertElementAt(((DeclareDataPo) pathResultPo).getId()+"", 0);
				unitVector.insertElementAt(((DeclareDataPo) pathResultPo).getDtype()+"", 1);
				unitVector.insertElementAt(((DeclareDataPo) pathResultPo).getArea(), 2);
			}
			vec.add(unitVector);
		}
		return vec;
		
	}
	
	
//	/**
//	 * @param lines
//	 */
//	public static Map<String,Vector<String>> getValueByFieldOfProvice(List<?> lines){
//		Map<String,Vector<String>> map = new HashMap<String, Vector<String>>();
//		Vector<String> unitVector = null;
//		if(null == lines || lines.isEmpty()){
//			return null;
//		}
//		Object pathResultPo = lines.get(0);
//		Field[] fields = pathResultPo.getClass().getDeclaredFields();
//		
//		for (int i=0;i<lines.size();i++) {
//			pathResultPo= lines.get(i);
//			unitVector = new Vector<String>();
//			
//			for (Field field : fields) {
//				if(field.getName().startsWith("h")){
//					field.setAccessible(true);
//					try {
//						//Object lineLimitPo = lines.get(i);
//						Field lineField = pathResultPo.getClass().getDeclaredField(field.getName());
//						lineField.setAccessible(true);
//						Double hh = (Double)lineField.get(pathResultPo);
//						unitVector.add(hh+"");
//					} catch (IllegalArgumentException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					} catch (IllegalAccessException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					} catch (SecurityException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					} catch (NoSuchFieldException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//				}
//			}
//			
//            if(pathResultPo instanceof DeclareDataPo){
//            	unitVector.insertElementAt(((DeclareDataPo) pathResultPo).getId()+"", 0);
//            	unitVector.insertElementAt(((DeclareDataPo) pathResultPo).getDtype()+"", 1);
//            	unitVector.insertElementAt(((DeclareDataPo) pathResultPo).getArea(), 2);
//				
//			}
//		}
//		return map;
//		
//	}
	
	public static void main(String[] args) {
		DeclareDataPo po = new DeclareDataPo();
		po.setId(23);
		po.setArea("山西");
		po.setH01(30.09);
		po.setH02(34.08);
		po.setH03(78.98);
		DeclareDataPo po1 = new DeclareDataPo();
		po1.setId(26);
		po1.setArea("北京");
		po1.setH01(301.09);
		po1.setH02(341.08);
		po1.setH03(781.98);
		List<DeclareDataPo> list = new ArrayList<DeclareDataPo>();
		list.add(po);
		list.add(po1);
		//System.out.println(getValueByFieldOfProvice(list));

		
		
	}

}
