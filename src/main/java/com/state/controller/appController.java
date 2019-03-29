package com.state.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/app")
public class appController {
	 /*
     * RequestMapping 支持 Ant 风格的URL配置 ：
     *  请求路径：
     *     /urlinfo/geturlant/config.html?key=adddd
     */
    @ResponseBody
    @RequestMapping(value = "/json.html")
    public String getUrlAnt(HttpServletRequest request) {
        String result = "?后面的参数为：" + request.getQueryString();
        return result;
    }
    /*
     * 配置指定格式的URL，映射到对应的参数
     *   请求路径：/web1/urlinfo/geturlparam/12_123.html
     *     
     * */
    
    @RequestMapping(value = "/geturlparam/{id}_{menuId}.html")
    public ModelAndView getUrlParam(@PathVariable("id") String id,
            @PathVariable("menuId") String menuId) {
        ModelAndView mode = new ModelAndView();
        mode.addObject("msg", "获取到的Id:" + id + ",menuId:" + menuId);
        return mode;
    }
    /*
     * 只接收Post 请求
     */
    @ResponseBody
    @RequestMapping(value = "/posturl.html", method = RequestMethod.POST)
    public String UrlMethod(@RequestParam String name) {
       // return "只能是Post请求,获取到的Id:" + id;
        if(name=="1"){
        return "sucess";
        } 
        return "fail";
        
    }
}
