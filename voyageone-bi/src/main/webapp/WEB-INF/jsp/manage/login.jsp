<!-- define -->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ include  file="/WEB-INF/jsp/common/include/define_express.jsp"%>
<!-- End define -->
<html>
<head>
    <title>BI | Login Page...</title>

    <!-- commoncss -->
    <%@ include  file="/WEB-INF/jsp/common/include/commoncss.jsp"%>
    <!-- End commoncss -->
    
    <!-- please add css start -->
    <!-- CSS Page Style -->    
    <link rel="stylesheet" href="<%=assetsPath%>/css/pages/page_log_reg_v1.css">    
    <!-- please add css end -->
</head> 

<body onload="$('#username').focus();">

<div class="wrapper">
    <!--=== Header ===-->    
    <div class="header">
        <!-- Topbar -->
        <%@ include  file="/WEB-INF/jsp/common/include/topbar_out.jsp"%>
        <!-- End Topbar -->        
        
        <!-- Navbar -->
        <!-- End Navbar -->
    </div>
    <!--=== End Header ===-->    

    <!--=== Content Part ===-->
    <div class="container content">		
    	<div class="row">
            <div class="col-md-4 col-md-offset-4 col-sm-6 col-sm-offset-3">
                <form:form  modelAttribute="user" action="dologin.html" class="reg-page">
                    <div class="reg-header">            
                        <h2>Login to your account</h2>
                    </div>
                    <span >
                        <form:errors path="password" cssClass="color-red"/>
                    </span>
                    <div class="input-group margin-bottom-20">
                        <span class="input-group-addon"><i class="fa fa-user"></i></span>
                        <form:input path="username" type="text" class="form-control"  value="admin"/>
                    </div>                    
                    <div class="input-group margin-bottom-20">
                        <span class="input-group-addon"><i class="fa fa-lock"></i></span>
                        <form:input path="password" type="password" class="form-control"    value="bi_admin!@#"/>
                    </div>

                    <div class="row">
                        <div class="col-md-6">
                            <label class="checkbox">
                                <form:checkbox path="isRemember" value="0"/> Remember me
                            </label>                        
                        </div>
                        <div class="col-md-6">
                            <button class="btn-u pull-right" type="submit">Login</button>                        
                        </div>
                    </div>

                    <hr>
                    <!--=== no use
                    <h4>Forget your Password ?</h4>
                    <p>no worries, <a class="color-green" href="#">click here</a> to reset your password.</p>
                    -->
                </form:form>
            </div>
        </div><!--/row-->
    </div><!--/container-->		
    <!--=== End Content Part ===-->

    <!--=== Footer ===-->
    <%@ include  file="/WEB-INF/jsp/common/include/footer.jsp"%>
    <!--=== End Footer ===-->

    <!--=== Copyright ===-->
    <%@ include  file="/WEB-INF/jsp/common/include/copyright.jsp"%>
    <!--=== End Copyright ===-->
</div><!--/wrapper-->

<%@ include  file="/WEB-INF/jsp/common/include/commonjs.jsp"%>
</body>
</html> 