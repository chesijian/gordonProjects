package com.state.util;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;

import com.state.listener.TomcatListener;

public class UploadImageTool {
	public static Map<String,String> uploadFileMap = new HashMap<String, String>();
	
	public static Map<String, String> getUploadFileMap() {
		return uploadFileMap;
	}
	public static void setUploadFileMap(Map<String, String> uploadFileMap) {
		UploadImageTool.uploadFileMap = uploadFileMap;
	}
	
	
	/**
	 * 拷贝文件
	 * @param file
	 * @param path
	 */
	public static void copyFile(String file,String path){
		File srcFile = new File(file);
	    File destDir = new File(path);
	    if(!destDir.exists()){
	    	destDir.mkdir();
	    }
        try {
        	FileUtils.copyFileToDirectory(srcFile, destDir);
          
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	/**
	 * 根据图片创建缩略图
	 */
	public static String createIcon(String imagePath,String fileName,String stuff) {
		String iconName = "";
        try {
            File fiBig = new File(imagePath+File.separator+fileName+"."+stuff); // 大图文件
            String iconPath = TomcatListener.getIconPath();
            System.out.println(iconPath);
            File IconPath = new File(iconPath);
            if(!IconPath.exists()){
            	IconPath.mkdir();
            }
            File foSmall = new File(IconPath+File.separator+fileName+"Icon"+"."+stuff); // 将要转换出的小图文件
            iconName = foSmall.getName();
            
            if(true){
            	FileManager.copyFile(fiBig, foSmall);
            	return iconName;
            }
            
            AffineTransform transform = new AffineTransform();
            //读取图片
            BufferedImage bis = ImageIO.read(fiBig);
            //获得图片原来的高宽
            int w = bis.getWidth();
            int h = bis.getHeight();
            double scale = (double) w / h;

            int nowWidth = 100;
            int nowHeight = (nowWidth * h) / w;
            if (nowHeight > 100) {
                nowHeight = 100;
                nowWidth = (nowHeight * w) / h;
            }

            double sx = (double) nowWidth / w;
            double sy = (double) nowHeight / h;

            transform.setToScale(sx, sy);

            AffineTransformOp ato = new AffineTransformOp(transform, null);
            BufferedImage bid = new BufferedImage(nowWidth, nowHeight,
                    BufferedImage.TYPE_3BYTE_BGR);
            ato.filter(bis, bid);
            //System.out.println("-----------1-------------");
            ImageIO.write(bid, stuff, foSmall);
            System.out.println(foSmall.getAbsolutePath()+"-----------2-------------"+foSmall.exists());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return iconName;
    }

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		UploadImageTool te = new UploadImageTool();
		 String srcFile = "D:/workspace/old_demo/.metadata/.plugins/org.eclipse.wst.server.core/tmp1/wtpwebapps/driverPicTemp/20160926_183309.jpg";
	     String  destDir = "D:/workspace/old_demo/.metadata/.plugins/org.eclipse.wst.server.core/tmp1/wtpwebapps/driverPic";
		 te.copyFile(srcFile,destDir);
//		 te.createIcon("D:/log/", "tickit","png");
//		 SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd_HHmmss");
//	        String userName = format.format(new Date());
//	        System.out.println(userName);
//		String name = "20160926_101621.gif";
//		String str = name.split("\\.")[0];
//		System.out.println(str);
	        

	}

}
