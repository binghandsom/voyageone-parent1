<!-- define -->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include  file="/WEB-INF/jsp/common/include/define.jsp"%>

<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<!-- End define -->
<html>
<head>
    <title>BI | Change Passowrd</title>

    <!-- commoncss 导入-->
    <%@ include  file="/WEB-INF/jsp/common/include/commoncss.jsp"%>
    
	<!-- commonjs.jsp 导入 -->
    <%@ include  file="/WEB-INF/jsp/common/include/commonjs.jsp"%>

</head> 

<body onload="$('#password').focus();">

<div class="wrapper">
    <!--=== Header ===-->
    <div class="header">
        <!-- Topbar -->
        <%@ include  file="/WEB-INF/jsp/common/include/topbar.jsp"%>
        <!-- End Topbar -->        
        
        <!-- Navbar -->
        <%@ include  file="/WEB-INF/jsp/common/include/navbar.jsp"%>
        <!-- End Navbar -->
    </div>
   	<!--=== End Header ===-->

    <!--=== Content Part ===-->
    <div class="container content">		
    	<div class="row">
            <div class="col-md-4 col-md-offset-4 col-sm-6 col-sm-offset-3">
                <form:form  modelAttribute="password" action="doChangePwd.html" class="reg-page">
                    <div class="reg-header margin-bottom-40" style="width:350px;text-align:center">            
                        <h2>Change your password</h2>
                    </div>
                    <span >
                        <form:errors path="orgPassword" cssClass="color-red"/>
                    </span>
                    <div class="input-group margin-bottom-20" style="width:350px">
                        <span class="input-group-addon" style="width:40%">current password</span>
                        <form:input path="orgPassword" type="password" class="form-control"  value=""/>
                    </div>                    
                    <span >
                        <form:errors path="newPassword1" cssClass="color-red"/>
                    </span>
                    <div class="input-group margin-bottom-20" style="width:350px">
                        <span class="input-group-addon" style="width:40%">new password</span>
                        <form:input path="newPassword1" type="password" class="form-control"    value=""/>
                    </div>
                    <span >
                        <form:errors path="newPassword2" cssClass="color-red"/>
                    </span>
                    <div class="input-group margin-bottom-20" style="width:350px">
                        <span class="input-group-addon" style="width:40%">try password</span>
                        <form:input path="newPassword2" type="password" class="form-control"    value=""/>
                    </div>
                    
                    <div class="row">
                        <div class="col-md-11">
                            <button class="btn-u pull-right" type="submit">Submit</button>                        
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