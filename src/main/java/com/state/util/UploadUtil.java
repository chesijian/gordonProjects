package com.state.util;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 * 上传工具
 * @author Administrator
 *
 */
public class UploadUtil {

	public static final String uploadImagePath = "e:/uploadImages";

	public static String upload(MultipartFile file, String basePath,String fileName) {
		
		try {
			
			File sourceFile = new File(basePath, fileName);
			FileUtils.writeByteArrayToFile(sourceFile, file.getBytes());
			/*
			File targetFile = new File(uploadImagePath,
					FilenameUtils.getName(sourceFile.getAbsolutePath()));
			FileUtils.copyFile(sourceFile, targetFile);
			*/
		} catch (IOException e) {
			e.printStackTrace();
		}
		return fileName;
	}
}
