<!-- define -->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include  file="/WEB-INF/jsp/common/include/define.jsp"%>
<!-- End define -->
<head>
    <title>BI | Sales Detail Page</title>

    <!-- commoncss 导入-->
    <%@ include  file="/WEB-INF/jsp/common/include/commoncss.jsp"%>
    
	<!-- commonjs.jsp 导入 -->
    <%@ include  file="/WEB-INF/jsp/common/include/commonjs.jsp"%>
    
    <script charset="UTF-8" type="text/javascript" src="<%=webRootPath%>/resource/js/common/components/bar_line_chart.js"></script>
</head>  

<body>

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
    <div>

	<!--=== Content Part ===-->
	<div id="container" class="container" style="width:1200px;margin-bottom:5px;margin-top:5px;">
		<!-- 用户信息 userInfoDefine.jsp中定义 -->
		<%@ include  file="/WEB-INF/jsp/common/include/userInfoDefine.jsp"%>
			
		<!-- Begin Content -->
		<div>
			<div class="row">
				<div class="col-md-10 mmCustomScrollbar"  style="padding-left: 1px; padding-right: 10px; max-height:800px;overflow-x:hidden;">
					<!-- 按时间Chart[模块] -->
					<%@ include  file="./time_line.jsp"%>
					
					<!-- 品牌/分类模块 -->
	               	<%@ include  file="./brand_category.jsp"%>
	               	<!-- Color/Size模块 -->
	               	<%@ include  file="./color_size.jsp"%>
	               	<!-- 产品模块 -->
	               	<%@ include  file="./product_sku.jsp"%>
				</div>
					
				<!-- search area start -->
				<div class="col-md-2" style="padding-left: 1px; padding-right: 1px;">
					<!-- 检索模块 -->
					<%@ include  file="./search.jsp"%>
				</div>
				<!-- search area end -->
			</div>
           <!-- End Content -->          
    </div><!--/container-->     
    <!--=== End Content Part ===-->

</div><!--/End Wrapepr-->


<!-- 画面初期 js载入 -->
<script charset="UTF-8" type="text/javascript" src="<%=webRootPath%>/resource/js/module/salesdetail/main.js"></script>



</body>
</html>