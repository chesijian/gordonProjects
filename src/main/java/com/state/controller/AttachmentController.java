package com.state.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.UUID;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.state.exception.MsgException;
import com.state.po.DeclareDataPo;
import com.state.po.DeclareExtraPo;
import com.state.po.DeclarePo;
import com.state.po.SdcostResultPo;
import com.state.po.TypePo;
import com.state.po.UserPo;
import com.state.po.extend.JsonMsg;
import com.state.po.org.OrgDepartUserPo;
import com.state.po.org.OrgUserRolePo;
import com.state.service.AreaService;
import com.state.service.IDeclareService;
import com.state.service.IssueService;
import com.state.util.Attributes;
import com.state.util.AuthoritiesUtil;
import com.state.util.BillCountBean;
import com.state.util.CommonUtil;
import com.state.util.FileManager;
import com.state.util.MParentTree;
import com.state.util.SessionUtil;
import com.state.util.UploadUtil;

@Controller
@RequestMapping("/attachment")
public class AttachmentController {
	private static final transient Logger log = Logger
			.getLogger(AttachmentController.class);

	@Autowired
	private IDeclareService declareService;
	
	
	/**
	 * 跳转申报页面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/init")
	public ModelAndView init(Model model,String xtype) {
		
		model.addAttribute("xtype", xtype);
		ModelAndView view = new ModelAndView("upload/attachment",null);
		return view;
	}
	
	@RequestMapping(value ="/upload")
	@ResponseBody
	public Map<String,Object> upload(HttpServletRequest request,     
            HttpServletResponse response,String mdate){
		Map<String,Object> j = new HashMap<String,Object>();
		try{
			//System.out.println("===========");
			// 转型为MultipartHttpRequest：     
	        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;     
	        // 获得文件：     
	        MultipartFile file = multipartRequest.getFile("attachment"); 
	        
	       // System.out.println(file.getName()+"=="+file.getContentType());
	        String orgFileName = file.getOriginalFilename();
	        String id = CommonUtil.getUUID();
			String fileName = id.toString() + "."
					+ FilenameUtils.getExtension(orgFileName);
			if(!FilenameUtils.getExtension(orgFileName).equals("dat")){
				throw new MsgException("文件类型不正确!");
			}
			/*
			if(file.getSize()>1024000){
				throw new MsgException("文件不能超过1M!");
			}
			*/
	       String filePath = request.getRealPath("/uploadfile");
	      
	      
		   UploadUtil.upload(file, filePath,fileName);
		   String size = FileManager.FormetFileSize(file.getSize());
	        
		   
		   	declareService.importDeclare(filePath+File.separator+fileName);
		   	DeclarePo last = null;
		   	//System.out.println("mdate======"+mdate);
		   	if(mdate != null){
		   		mdate = mdate.replace("-", "");
		   	}
		   	//返回最新的单子
		   	if(SessionUtil.isState()){
		   		last = declareService.getLastDeclare(null, mdate);
		   	}else{
		   		last = declareService.getLastDeclare(SessionUtil.getArea(), mdate);
		   	}
		   	if(last != null){
		   		j.put("lastId",last.getId());
		   	}
		   	j.put("status",true);
			j.put("id", id);
			j.put("fileName", orgFileName);
			j.put("size",size);
			j.put("type",FilenameUtils.getExtension(orgFileName));
			
			
		}catch(MsgException ex){
			j.put("status",false);
			j.put("msg",ex.getMessage());
		}catch(Exception e){
			e.printStackTrace();
			j.put("status",false);
			j.put("msg","上传失败！");
		}
		//return CommonUtil.objToJson(j);
		return j;
	}
}
