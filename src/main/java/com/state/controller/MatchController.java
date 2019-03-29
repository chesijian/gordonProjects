package com.state.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.jrsoft.util.process.ProcessUtils;
import com.jrsoft.util.process.ProcessUtils.ProcessStatus;
import com.state.enums.Enums_SessionType;
import com.state.enums.Enums_SystemConst;
import com.state.exception.MsgException;
import com.state.po.LineLimitPo;
import com.state.po.PathDefinePo;
import com.state.po.PathResultPo;
import com.state.po.UserPo;
import com.state.po.extend.JsonMsg;
import com.state.service.AreaService;
import com.state.service.IDeclareService;
import com.state.service.IEvaluateService;
import com.state.service.ILineService;
import com.state.service.ILoginService;
import com.state.service.IPathService;
import com.state.service.LineLimitService;
import com.state.service.MatchService;
import com.state.service.ServiceHelper;
import com.state.service.ShellExecuteServiceI;
import com.state.service.SystemServiceI;
import com.state.util.CommonUtil;
import com.state.util.FileManager;
import com.state.util.LoggerUtil;
import com.state.util.SessionUtil;
import com.state.util.SystemTools;
import com.state.util.TimeUtil;
import com.state.util.sys.SystemConstUtil;

/**
 * 撮合
 * 
 * @author 帅
 * 
 */
@Controller
@RequestMapping("/match")
public class MatchController {
	private static final transient Logger log = Logger.getLogger(MatchController.class);
	@Autowired
	private MatchService matchService;
	@Autowired
	private IPathService pathService;
	@Autowired
	private IEvaluateService evaluateService;

	/**
	 * 跳转撮合页面
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/init")
	public String init(HttpServletRequest request, HttpServletResponse response, Model model) {
		log.info("@ init match ");
		request.setAttribute("jspType", "match");
		//获取当前服务器时间
		model.addAttribute("serviceDate", TimeUtil.getStringDate());
		return "match";
	}

	/**
	 * 点击撮合的时候，设置session，是否优化计算
	 * 
	 * @description
	 * @author 大雄
	 * @date 2016年9月22日下午9:22:18
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/jump")
	public JsonMsg jump(HttpServletRequest request, HttpServletResponse response) {
		log.info("@ init jump ");
		JsonMsg j = new JsonMsg();
		SessionUtil.setAttribute(Enums_SessionType.IS_MATCH.getValue(), true);
		j.setStatus(true);
		j.setMsg("成功！");
		return j;
	}

	/**
	 * 查询所有的通道
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getAllPath")
	public List<PathDefinePo> getAllPath(HttpServletRequest request, HttpServletResponse response, String time) {
		log.info("@getAllPath ");

		return pathService.getAllPath(time);
	}

	/**
	 * 按通道名、类型查询通道结果
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getPathResult")
	public PathResultPo getPathResult(String mpath, String dtype, String time) {
		log.info("@ getPathResult ");

		return matchService.getPathResult(mpath, dtype, time);
	}

	/**
	 * 判断申报单类型是否一致
	 */
	@ResponseBody
	@RequestMapping(value = "/checkDtype")
	public String checkDtype(String time) {
		log.info("@ getPathResult ");
		return matchService.getCheckDtype(time);
	}

	/**
	 * 撮合
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/matchData")
	public Map<String,Object> match(HttpServletRequest request, HttpServletResponse response, String time, String time1, String dtype) {
		log.info("@ match ");
		return ServiceHelper.getBean(ShellExecuteServiceI.class).match(false, time, time1, dtype);
		
	}

	/**
	 * 发布
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/issue")
	public String issue() {
		matchService.issue();
		return "success";
	}

	/**
	 * 导出数据
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@Test
	@ResponseBody
	@RequestMapping(value = "/exportData")
	public String exportData(HttpServletRequest request, HttpServletResponse response, String time, String dtype) {
		log.info("@ init getAllPath ");
		String path = request.getSession().getServletContext().getRealPath("/bat/");
		matchService.createFile(path, time, dtype);// 创建bat文件
		return "success";
	}

	/**
	 * 导入撮合数据结果集
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@Test
	@ResponseBody
	@RequestMapping(value = "/importData")
	public String importData(HttpServletRequest request, HttpServletResponse response, String time) {
		log.info("@ init getAllPath ");
		String path = request.getSession().getServletContext().getRealPath("/bat/");
		matchService.readFile(path, time);// 本地测试读取bat文件
		return "success";
	}

	/**
	 * 竞价计算时显示进度
	 * 
	 * @description
	 * @author 大雄
	 * @date 2016年11月18日下午3:54:16
	 * @param model
	 * @param mdate
	 * @return
	 */
	@RequestMapping(value = "/issueProcess")
	public ModelAndView initClear(Model model, String mdate) {
		log.info("@ init initClear ");
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("mdate", mdate);
		ModelAndView view = new ModelAndView("issue/issueProcess", null);
		return view;
	}

	/**
	 * 出清过程中控制台打印的数据
	 */
	public List<String> issueData = new ArrayList<String>();

	@ResponseBody
	@RequestMapping(value = "/getIssueData")
	public List<String> match(HttpServletRequest request, HttpServletResponse response, String time, int pageIndex, int pageSize) {
		List<String> data = new ArrayList<String>();
		// System.out.println("-----------"+issueData.toString());
		// if(issueData.size()>=pageIndex){
		// int total = pageIndex*pageSize;
		// if(issueData.size()>=total){
		// data = issueData.subList(pageIndex==1?0:(pageIndex-1)*pageSize,
		// (pageIndex)*pageSize);
		// }
		// }
		try {
			time = time.replaceAll("-", "");
			// data = issueData;
			String logFilePath = SystemConstUtil.getMatchPath() + File.separator + "log";
			String logFileName = logFilePath + File.separator + "match_" + time + ".log";

			//File file = new File(logFileName);

			// 给该文件加锁
			RandomAccessFile fis = new RandomAccessFile(logFileName, "rw");
			FileChannel fcin = fis.getChannel();
			FileLock flin = null;
			while (true) {
				try {
					flin = fcin.tryLock();
					break;
				} catch (Exception e) {
					System.out.println("有其他线程正在操作该文件，当前线程休眠1000毫秒");
					Thread.sleep(1000);
				}

			}

			byte[] buf = new byte[1024];
			//StringBuffer sb = new StringBuffer();
			String str;
			int index = 1;

			while ((str = fis.readLine()) != null) {
				// str = new String(buf, "utf-8");
				//System.out.println(new String(str.getBytes("8859_1"),"utf-8"));
				data.add(new String(str.getBytes("8859_1"),"utf-8"));
				// data.add(str+"</br>");
				// sb.append(str);
				index++;
				//buf = new byte[1024];
			}
			//System.out.println("size----------" + index);
			// System.out.println("督导的内容------------------------"+sb.toString());

			flin.release();
			fcin.close();
			fis.close();
			fis = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//System.out.println("datasize----------" + data.size());
		return data;
	}
}
