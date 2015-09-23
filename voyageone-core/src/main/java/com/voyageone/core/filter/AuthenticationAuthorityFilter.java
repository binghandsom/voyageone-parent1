package com.voyageone.core.filter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.voyageone.core.CoreConstants;
import com.voyageone.core.MessageConstants;
import com.voyageone.core.ajax.AjaxResponseBean;
import com.voyageone.core.modelbean.UserSessionBean;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AuthenticationAuthorityFilter implements Filter {

	protected FilterConfig config;
	// 静态资源
	protected List<String> staticResource;
	// 不需要验证的URL
	protected List<String> excludeUrl;
	
	protected final Log logger = LogFactory.getLog(getClass());
	
	public void destroy(){
		
	}

	/**
	 * 初始化
	 * 
	 * @param filterConfig
	 */
	public void init(FilterConfig filterConfig) throws ServletException{
		this.config = filterConfig; 
		// 取出Tab，回车，换行，空格
		Pattern p = Pattern.compile("\\s*|\t|\r|\n");
		String urls = config.getInitParameter("excludeUrl").trim();
        Matcher m = p.matcher(urls);
        urls = m.replaceAll("");
		String suffix = config.getInitParameter("staticResource").trim();
		m = p.matcher(suffix);
		suffix = m.replaceAll("");
		excludeUrl = Arrays.asList(urls.split(","));
		staticResource = Arrays.asList(suffix.split(","));
	}

	/**
	 * doFilter
	 * 
	 * @param request
	 * @param response
	 * @param chain
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException{
		
		HttpServletRequest userRequest = (HttpServletRequest)request;
		HttpServletResponse userResponse = (HttpServletResponse)response;
		String url = userRequest.getRequestURI();
		String project = userRequest.getContextPath();
		logger.info(url);
		if(url.lastIndexOf(".") >= 0 && staticResource.contains(url.substring(url.lastIndexOf(".") + 1))){
			chain.doFilter(request,response);
		}else{
			UserSessionBean user = (UserSessionBean)userRequest.getSession().getAttribute(CoreConstants.VOYAGEONE_USER_INFO);
			if(excludeUrl.contains(url.replaceFirst(project, "")) || url.replaceFirst(project, "").equalsIgnoreCase("/")){
				chain.doFilter(request,response);
			}else{
				if(user == null || user.isAuthenticated() == false){
					AjaxResponseBean bean = new AjaxResponseBean();
					bean.setResult(false, MessageConstants.MESSAGE_CODE_300001, 
							MessageConstants.MESSAGE_TYPE_SESSION_TIMEOUT);
					bean.writeTo(userRequest, userResponse);
					
					logger.info(bean.toString());
				}else if(user.isSuperUser()){
					// 系统用户不需要权限校验
					chain.doFilter(request, response);
				}else{
					if(isAuthorization(url.replaceFirst(project, ""), user, request)){
						chain.doFilter(request, response);
					}else{
						AjaxResponseBean bean = new AjaxResponseBean();
						bean.setResult(false, MessageConstants.MESSAGE_CODE_400001, 
								MessageConstants.MESSAGE_TYPE_DIALOG);
						bean.writeTo(userRequest, userResponse);
						
						logger.info(bean.toString());
					}
				}
			}
		}		
	}

	/**
	 * 是否有权限
	 * 
	 * @param uri
	 * @param user
	 * @param request
	 * @return
	 */
	private boolean isAuthorization(String uri, UserSessionBean user, ServletRequest request){
		boolean returnValue = false;
		HttpServletRequest userRequest = (HttpServletRequest)request;
		if(user.getUserName().equalsIgnoreCase("system")){
			returnValue = true;
		}else{
			// 当前request是否设定了property
			String property = userRequest.getParameter("property");
			if(property == null || property.isEmpty()){
				if(user != null && user.getActionPermission().contains(uri)){
					returnValue = true;
				}
			}else{
				if(user != null && user.getPropertyPermissions().get(Integer.parseInt(property)).getPermissions().get(uri) != null &&
						user.getPropertyPermissions().get(Integer.parseInt(property)).getPermissions().get(uri)){
					returnValue = true;
				}
			}
		}
		return returnValue;
	}
}