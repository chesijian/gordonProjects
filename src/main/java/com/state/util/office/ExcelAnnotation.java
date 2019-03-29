package com.state.util.office;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Title: ExcelAnnotation.java
 * @author Lanxiaowei
 * @date 2012-7-27 下午4:07:29
 * @version V1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ExcelAnnotation {
	// excel导出时标题显示的名字，
	// 如果没有设置此注解，将不会被导出和导入
	public String name() default "";
}
