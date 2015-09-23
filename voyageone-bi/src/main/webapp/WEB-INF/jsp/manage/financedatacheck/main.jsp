<!-- define -->
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/include/define.jsp"%>
<!-- End define -->
<head>
<title>BI | Welcome...</title>

<!-- commoncss 导入-->
<%@ include file="/WEB-INF/jsp/common/include/commoncss.jsp"%>

<!-- please add css start -->
<%-- <link rel="stylesheet" href="<%=assetsPath%>/css/plugins/brand-buttons/brand-buttons.css"> --%>

<!-- commonjs.jsp 导入 -->
<%@ include file="/WEB-INF/jsp/common/include/commonjs.jsp"%>
</head>

<body>
	<style type="text/css">
       .panel-patit {
		  margin-bottom:10px;
		  border-color: #CACACA;
		}
		.panel-patit > .panel-heading {
		  font-size:15px;
		  color:#000000;
		  background-color: #ECECEC;
		  border-color: #CACACA;
		}
		.panel-patit > .panel-body {
		  margin-left: -1px;
		  margin-top: -1px;
		  margin-bottom: -21px;
		  padding: 0px 0px;
		}
		.panel-patit > .panel-heading + .panel-collapse .panel-body {
		  border-top-color: #CACACA;
		}
		.panel-patit > .panel-footer + .panel-collapse .panel-body {
		  border-bottom-color: #CACACA;
		}
	</style>
    
	<div class="wrapper">
		<!--=== Header ===-->
		<div class="header">
			<!-- Topbar -->
			<%@ include file="/WEB-INF/jsp/common/include/topbar.jsp"%>
			<!-- End Topbar -->

			<!-- Navbar -->
			<%@ include file="/WEB-INF/jsp/common/include/navbar.jsp"%>
			<!-- End Navbar -->
		</div>
		<!--=== End Header ===-->
		<div></div>
		<!--=== Content Part ===-->
		<div id="container" class="container"
			style="width: 1200px; margin-bottom: 5px; margin-top: 5px;">
			<!-- 用户信息 userInfoDefine.jsp中定义 -->
			<%@ include file="/WEB-INF/jsp/common/include/userInfoDefine.jsp"%>

			<!-- Begin Content -->
			<div>
				<div class="row">
					<!-- search area end -->
					<div class="col-md-10 mmCustomScrollbar"
						style="padding-left: 1px; padding-right: 10px; max-height: 1200px; overflow-x: hidden;">
						<div class="panel panel-patit rounded">
							<div class="panel-heading" >
								<div class="row" >
									<div class="col-lg-12" >Check Upload Data</div>
								</div>
							</div>
							<div class="panel-body" >
								<!-- 成本报表模块 -->
								<%@ include file="check.jsp"%>
							</div>
						</div>
					</div>
					<!-- search area start -->
					<div class="col-md-2"
						 style="padding-left: 1px; padding-right: 1px;">
						<!-- 检索模块 -->
						<%@ include file="./search.jsp"%>
					</div>
				</div>
			</div>
			<!-- End Content -->

		</div>
		<!--/container-->
		<!--=== End Content Part ===-->
	</div>
	<!--/End Wrapepr-->


	<!-- 画面初期 js载入 -->
	<script charset="UTF-8" type="text/javascript"
		src="<%=webRootPath%>/resource/js/module/financedatacheck/main.js"></script>

</body>
</html>