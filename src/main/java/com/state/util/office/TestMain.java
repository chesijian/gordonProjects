package com.state.util.office;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

public class TestMain {

	public static void main(String[] args) {
		exportDocFor03();
		System.out.println("0000099999========");
	}

	public static void exportDocFor03() {

		String modelPath = "D:/model.doc";
		String tampPath = "D:/temp.doc";
		FileInputStream is = null;
		POIFSFileSystem pfs = null;
		OutputStream os = null;
		try {
			Map<String, String> paramMap = new HashMap<String, String>();
			is = new FileInputStream(new File(modelPath));
			pfs = new POIFSFileSystem(is);
			HWPFDocument hwpf = new HWPFDocument(pfs);
			Range range = hwpf.getRange();
			
			paramMap.put("PROJ_NAME_", "车斯剑");
			paramMap.put("PROJ_CODE_", "0007008");
			paramMap.put("POWER_SUPPLY_", "99998");
			
			Set<String> keys = paramMap.keySet();
			for (String key : keys) {
				Object value = paramMap.get(key);
				range.replaceText("${" + key + "}", value == null ? "" : String.valueOf(value));
			}

			//String fileName = "测试.doc";
			//response.setContentType("application/vnd.ms-word");
			//response.addHeader("Content-Disposition", "attachment; filename=\"" + new String(fileName.getBytes("GB2312"), "ISO8859-1") + "\";");//
			os = new FileOutputStream(tampPath);//response.getOutputStream();
			hwpf.write(os);
			os.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (is != null) {
					is.close();
				}
				if (pfs != null) {
					pfs.close();
				}
				if (os != null) {
					os.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
