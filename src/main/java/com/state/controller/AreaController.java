package com.state.controller;

import java.io.FileInputStream;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.state.po.AreaPo;
import com.state.service.AreaService;
import com.state.util.CommonUtil;

@Controller
@RequestMapping("/area")
public class AreaController {
	
	@Autowired
	private AreaService areaService;
	
	@ResponseBody
	@RequestMapping(value="/getAllArea",method=RequestMethod.POST)
	public List<AreaPo> getAllArea(){
		//System.out.println("dsfdfddddd");
		return areaService.getAllArea();
	}
	
	/**
	 * 加载所有省份
	 * @author 车斯剑
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getAllAreas",method=RequestMethod.POST)
	public String getAllAreas(){
		List<AreaPo> areas = areaService.getAllArea();
		return CommonUtil.objToJson(areas);
	}
	
	
}
