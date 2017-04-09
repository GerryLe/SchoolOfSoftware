package com.rosense.basic.util.springmvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
/**
* @ClassName: ContextHolderUtils 
*
 */
public class ContextHolderUtils {
	/**
	 * SpringMvc下获取request
	 */
	public static HttpServletRequest getRequest() {
		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes() ;
		if(null != requestAttributes) {
			HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest() ;
			return request ;
		} else {
			return null ;
		} 

	}
	/**
	 * SpringMvc下获取session
	 */
	public static HttpSession getSession() {
		if(null != getRequest()) {
			return getRequest().getSession();
		} else {
			return null ;
		}
	}

}
