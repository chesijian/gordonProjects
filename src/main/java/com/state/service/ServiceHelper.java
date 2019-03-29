package com.state.service;

import org.springframework.web.context.ContextLoader;


public class ServiceHelper {
	/**
     * 根据class获得bean.
     * 
     * @param clz
     *            Class
     * @return T
     */
    public static <T> T getBean(Class<T> clz) {
        return ContextLoader.getCurrentWebApplicationContext()
                .getBean(clz);
    }
}
