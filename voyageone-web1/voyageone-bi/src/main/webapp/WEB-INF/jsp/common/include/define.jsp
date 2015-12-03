<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<%@ page import="com.voyageone.bi.tranbean.UserInfoBean"%>
<%@ page import="com.voyageone.bi.commonutils.SessionKey"%>
<%@ page import="com.voyageone.bi.commonutils.Contants"%>
<%@ page import="com.voyageone.bi.bean.UserMenuBean"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.Map.Entry"%>

<%
	// 当前登录用户信息取得
	String assetsPath = request.getContextPath()+"/resource/assetsUnify";
	String webRootPath = request.getContextPath();	
	
	HttpSession s = request.getSession(); 
	UserInfoBean user = (UserInfoBean)s.getAttribute(SessionKey.LOGIN_INFO);
	String channel_name = "";
	String uid = "";
	String dis_name = "";
	// 用户类型（客服/管理）
	String user_kind = "";
	// 当前用户OrderChannelId
	String user_order_channel_id = "";
	//MENU
	Map userMenuMap = null;
	if (user != null){		
		try{
			// User info
			//channel_name = user.getOrder_channel_name();
			uid = user.getUid();
			dis_name = uid;
			user_kind = user.getUser_kind();
			userMenuMap = user.getUserMenuMap();

			//user_order_channel_id = user.getOrder_channel_id();
		}catch(Exception e){
			System.out.println("Get user info error!");
			e.printStackTrace();
		}
	} 
	 
%>

<!DOCTYPE html>
<!--[if IE 8]> <html lang="en" class="ie8"> <![endif]-->  
<!--[if IE 9]> <html lang="en" class="ie9"> <![endif]-->  
<!--[if !IE]><!--> <html lang="en"> <!--<![endif]-->  