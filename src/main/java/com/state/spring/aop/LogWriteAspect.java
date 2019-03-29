package com.state.spring.aop;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.state.annotation.LogWrite;
import com.state.po.PfLoggerPo;
import com.state.service.PfLoggerServiceI;
import com.state.service.impl.LoginServiceImpl;
import com.state.util.CommonUtil;
import com.state.util.LoggerUtil;

/**
 * 
 * @description
 * @author 大雄
 * @date 2016年8月2日下午5:12:51
 */
@Order(1)
@Aspect
public class LogWriteAspect {

	@Autowired
	PfLoggerServiceI loggerService;
	
	// 配置切入点,该方法无方法体,主要为方便同类中其他方法使用此处配置的切入点
	@Pointcut("execution(* com.state.service..*Impl.*(..)) or or execution(* com.state.service.org..*Impl.*(..)")
	public void aspect() {

		//System.out.println("==========2222===========");
	}

	/*
	 * 配置前置通知,使用在方法aspect()上注册的切入点 同时接受JoinPoint切入点对象,可以没有该参数
	 */
	@Before("aspect()")
	public void before(JoinPoint joinPoint) {
		//System.out.println("==========before===========");
	}

	// 配置后置通知,使用在方法aspect()上注册的切入点
	@After("aspect()")
	public void after(JoinPoint joinPoint) {
		//System.out.println("==========after===========");
	}

	// 环绕通知
	// @Around("execution(* com.state.service..*.*(..))")
	// @Around("execution(* com.state.service..*Impl.*(..))")
	// 配置环绕通知,使用在方法aspect()上注册的切入点
	@Around("aspect()")    
    public Object aroundAdvice(ProceedingJoinPoint pjp) throws Throwable {    
        //System.out.println("-----aroundAdvice().invoke-----");  
        //System.out.println(" 此处可以做类似于Before Advice的事情");  
          
        PfLoggerPo log = null;
        Object ret = null;
        boolean isLog = false;
		try{
			// 拦截的实体类
			Object target = pjp.getTarget();
			// 拦截的方法名称
			String methodName = pjp.getSignature().getName();
			

			// 拦截的放参数类型
			Class[] parameterTypes = ((MethodSignature) pjp.getSignature())
					.getMethod().getParameterTypes();
			// 获得被拦截的方法
			Method method = target.getClass().getMethod(methodName, parameterTypes);
			//System.out.println(method+"==="+method.isAnnotationPresent(LogWrite.class));
			
			if (null != method) {
				// 判断是否包含自定义的注解
				if (method.isAnnotationPresent(LogWrite.class)) {
					isLog = true;
					log = new PfLoggerPo();
					LogWrite logW = method.getAnnotation(LogWrite.class);
					log.setOperNameCn(logW.operateName());
					log.setModelName(logW.modelName());
					log.setClassName(target.getClass().getName());
					//操作名称
					String mname = pjp.getSignature().getName();
					log.setOperName(mname);
					//操作参数
					Object[] params = pjp.getArgs();
					log.setOperParams(CommonUtil.objToJson(params));
					//如果是登陆
					if(target.getClass().getName().equals(LoginServiceImpl.class.getName()) && method.getName().equals("judgeUser")){
						log.setAddName(String.valueOf(params[0]));
						log.setAddNameCn(String.valueOf(params[0]));
					}
						
					//调用目标对象的方法
					ret = pjp.proceed();
					//设置操作结果
					log.setOperResult("success");
					//设置结果消息
					log.setResultMsg(ret == null?"":CommonUtil.objToJson(ret));
					//System.out.println(" 此处可以做类似于After Advice的事情");  
				    System.out.println("-----End of aroundAdvice()------"); 
				}else{
					//调用目标对象的方法
					ret = pjp.proceed();
				}
			}else{
				//调用目标对象的方法
				ret = pjp.proceed();
			}
			
			return ret;
		} catch (Throwable e) {
			e.printStackTrace();
			if(isLog){
				log.setOperResult("failure");
				log.setResultMsg(e.getMessage());
			}
			// TODO Auto-generated catch block
			//System.out.println("++++++++++++++++++++++++");
			//LoggerInfo _logger = new LoggerInfo("com.platform.advice.Advice_Logger操作失败！");
	        //_logger.errorAll(e.getMessage(),e);
	        throw e;
	        
		}finally{
			//System.out.println(new Date().getTime()+"))))))))))))))))");
			//记录日志
			//绑定token到当前线程
			//ThreadLocalToken token = new ThreadLocalToken();
			//token.setLogger(log);
			//ThreadLocalToken.bindToken(token);
			//System.out.println("getCurrentToken-->"+ThreadLocalToken.getCurrentToken());
			//try{
				if(isLog)loggerService.insert(log);
			/*}catch(Exception e){
				LoggerInfo _logger = new LoggerInfo("----日志记录失败！");
		        _logger.errorAll(e.getMessage(),e);
			}*/
			//ThreadLocalToken.ubBindToken();
			
			//throw new Throwable();
				
		}
    } 

	// 配置后置返回通知,使用在方法aspect()上注册的切入点
	@AfterReturning("aspect()")
	public void afterReturn(JoinPoint joinPoint) {
		//System.out.println("==========afterReturn===========");
	}

	// 配置抛出异常后通知,使用在方法aspect()上注册的切入点
	@AfterThrowing(pointcut = "aspect()", throwing = "ex")
	public void afterThrow(JoinPoint joinPoint, Exception ex) {
		//System.out.println("==========afterThrow===========");
	}

	
	public void setApplicationContext(ApplicationContext arg0)
			throws BeansException {
		// TODO Auto-generated method stub

	}
}
