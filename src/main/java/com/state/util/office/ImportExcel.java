package com.state.util.office;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;


/**
 * 导入Excel
 * @Title: ImportExcel.java
 * @author Lanxiaowei
 * @date 2012-7-27 下午4:44:18
 * @version V1.0
 */
public class ImportExcel<T> {
	private static final String DATE_FORMAT_PATTERN = "yyyy-MM-dd";
	
	private Class clazz;

	public ImportExcel(Class<T> clazz) {
		this.clazz = clazz;
	}

	/**
	 * 
	 * @author: Lanxiaowei
	 * @date: 2012-7-27 下午5:51:49
	 * @Title: importExcel 
	 * @Description: TODO(导入Excel数据，返回Collection集合) 
	 * @param file
	 * @param pattern      日期格式
	 * @return
	 * @return Collection<T>
	 * @throws
	 */
	public Collection<T> importExcel(File file, String pattern) {
		Collection<T> dist = new ArrayList();
		try {
			Field filed[] = clazz.getDeclaredFields();
			
			Map fieldmap = new HashMap();
			for (int i = 0; i < filed.length; i++) {
				Field f = filed[i];
				ExcelAnnotation ea = f.getAnnotation(ExcelAnnotation.class);
				//若添加了注解
				if (null != ea) {
					String fieldName = f.getName();
					String setMethodName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
					Method setMethod = clazz.getMethod(setMethodName, new Class[] {f.getType()});
					String exportTitle = ea.name();
					if(exportTitle.equals("")){
						fieldmap.put(fieldName, setMethod);
					} else{
						fieldmap.put(ea.name(), setMethod);
					}
					
				}
			}
			
			FileInputStream fis = new FileInputStream(file);
			HSSFWorkbook book = new HSSFWorkbook(fis);
			HSSFSheet sheet = book.getSheetAt(0);
			Iterator<Row> row = sheet.rowIterator();
			Row title = row.next();
			Iterator<Cell> cellTitle = title.cellIterator();
			Map titlemap = new HashMap();
			// 从标题第一列开始
			int i = 0;
			// 循环标题所有的列
			while (cellTitle.hasNext()) {
				Cell cell = cellTitle.next();
				String value = cell.getStringCellValue();
				titlemap.put(i, value);
				i = i + 1;
			}
			
			SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);;
			
			while (row.hasNext()) {
				Row rown = row.next();
				Iterator<Cell> cellbody = rown.cellIterator();
				T tObject = (T)clazz.newInstance();
				int index = 0;
				// 遍历一行的列
				while (cellbody.hasNext()) {
					Cell cell = cellbody.next();
					String titleString = (String) titlemap.get(index);
					if (fieldmap.containsKey(titleString)) {
						Method setMethod = (Method) fieldmap.get(titleString);
						// 得到setter方法的参数
						Class<?>[] ts = setMethod.getParameterTypes();
						Class xclass = ts[0];
						//System.out.println(xclass.toString());
						// 判断参数类型
						if (xclass == String.class) {
							setMethod.invoke(tObject, cell.getStringCellValue());
						} else if (xclass == Date.class) {
							setMethod.invoke(tObject, dateFormat.parse(cell.getStringCellValue()));
						} else if (xclass == Boolean.class || xclass.toString().equals("boolean")) {
							setMethod.invoke(tObject, Boolean.valueOf(cell.getStringCellValue()));
						} else if (xclass == Integer.class || xclass.toString().equals("int")) {
							setMethod.invoke(tObject, Integer.valueOf(cell.getStringCellValue()));
						} else if (xclass == Long.class || xclass.toString().equals("long")) {
							setMethod.invoke(tObject, Long.valueOf(cell.getStringCellValue()));
						} else if (xclass == Short.class || xclass.toString().equals("short")) {
							setMethod.invoke(tObject, Short.valueOf(cell.getStringCellValue()));
						} else if (xclass == Float.class || xclass.toString().equals("float")) {
							setMethod.invoke(tObject, Float.valueOf(cell.getStringCellValue()));
						} else if (xclass == Double.class || xclass.toString().equals("double")) {
							setMethod.invoke(tObject, Double.valueOf(cell.getStringCellValue()));
						} else{
							//其他类型不处理
						}
					}
					index += 1;
				}
				dist.add(tObject);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return dist;
	}

	/**
	 * 
	 * @author: Lanxiaowei
	 * @date: 2012-7-27 下午5:53:02
	 * @Title: importExcel 
	 * @Description: TODO(导入Excel[重载]) 
	 * @param file
	 * @return
	 * @return Collection<T>
	 * @throws
	 */
	public Collection<T> importExcel(File file){
		return importExcel(file,DATE_FORMAT_PATTERN);
	}
	
	public static void importArea() throws IOException{
		 File file = new File("C:\\area.xls");  
		 FileInputStream fis = new FileInputStream(file);
			HSSFWorkbook book = new HSSFWorkbook(fis);
			HSSFSheet sheet = book.getSheetAt(0);
			Iterator<Row> rowIt = sheet.rowIterator();
			Row title = rowIt.next();
			StringBuilder sql = new StringBuilder();
			while(rowIt.hasNext()){
				Row row = rowIt.next();
				sql.append("INSERT INTO EMSHIS.CBPM.CBPM_AREA(AREA,DCODE,DTYPE,REGION) VALUES(");
				Iterator<Cell> cellIt = row.cellIterator();
				// 从标题第一列开始
				int i = 0;
				// 循环标题所有的列
				while (cellIt.hasNext()) {
					i++;
					Cell cell = cellIt.next();
					if(i == 4){
						continue;
					}
					sql.append("'"+cell.getStringCellValue()+"',");
					 
				}
				sql.deleteCharAt(sql.length()-1);
				sql.append(");").append("\r\n");
			}
			System.out.println(sql);
			if(fis != null){
				fis.close();
			}
			
	}
	
	public static void main(String[] args) throws IOException {   
        File file = new File("C:\\a.xls");   
        Long befor = System.currentTimeMillis();   
        importArea();
        Long after = System.currentTimeMillis();   
        System.out.println("此次操作共耗时：" + (after - befor) + "毫秒");   
     
        //System.out.println("共读取到数据：" + result.size() + "条");   
    }   

}
