package com.state.listener;

import java.text.ParseException;
import java.util.TimerTask;

import com.jrsoft.util.process.AutoExecuteUtil;
import com.state.service.SystemServiceI;
import com.state.service.sys.LoginCountServiceI;
import com.state.util.LoggerUtil;
import com.state.util.LoginUtil;

/**
 * 定时清除登录次数限制表
 * 
 * @description
 * @author 大雄
 * @date 2016年8月18日下午2:08:49
 */
public class SystemTimerTask extends Thread {

	private LoginCountServiceI loginCountService;
	private SystemServiceI systemService;

	public SystemTimerTask(LoginCountServiceI loginCountService, SystemServiceI systemService) {
		super();
		this.loginCountService = loginCountService;
		this.systemService = systemService;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			// 修改流程按钮的状态
			systemService.updateProcessBtnStatus();
			while (true) {
				Thread.sleep(60000);
				AutoExecuteUtil.execute();
				LoggerUtil.log(this.getClass().getName(), "定时器定时执行清除超时登陆记录", 0);
				loginCountService.remove(false);
				systemService.updateProcessBtnStatus();
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
