package com.state.util;

import java.io.File;

public class VersionCtrl {
	
	
    private static String version="4.5.4";//版本号
    
    public static String getVesrsion(){
    	return version;
    }
    /**
     * 版本控制函数，每次更新版本执行此函数会更新state下的js  和css下的样式
     * @param arg
     */
    public static void main(String arg[]){
    	String url=System.getProperty("user.dir");
    	String jsurl=url+"\\src\\main\\webapp\\js\\state";//获取js路径
    	String cssurl=url+"\\src\\main\\webapp\\css";//获取css路径
    	System.out.println(jsurl);
    	File jsfile=new File(jsurl);
    	File cssfile=new File(cssurl);
    	try {
			showAllFiles(jsfile);
			showAllFiles(cssfile);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    final static void showAllFiles(File fl) throws Exception{
    	String[] files=fl.list();
    	File f=null;
    	String filename="";
    	for(String file:files){
    		f=new File(fl,file);
    		filename=f.getName();
    		if(filename.contains(".js")){
    			//注意每次改变版本号时 都需要同时改动此处,替换掉上次的版本号
    			f.renameTo(new File(fl.getAbsoluteFile()+"//"+filename.replace("1.02.js",version+".js")));
    		}else if(filename.contains(".css")){
    			f.renameTo(new File(fl.getAbsoluteFile()+"//"+filename.replace("1.02.css",version+".css")));
    		}
    		if(f.isDirectory()){
    			try{
    				showAllFiles(f);
    			}catch(Exception e){
    				
    			}
    		}
    	}
    }
}
