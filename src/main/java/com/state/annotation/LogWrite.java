package com.state.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用来标注需要记录日志的方法
 * @description
 * @author 大雄
 * @date 2016年8月3日上午10:28:47
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented  
@Inherited
public @interface LogWrite {
	 /**
     *@param 模块名字 
     */
    String modelName() default "";
    /**
     * 方法名称
     * @description
     * @author 大雄
     * @date 2016年8月17日下午2:32:44
     * @return
     */
    String operateName();
    
    /*
     *@param 操作类型 
     */
    //String option();
}
