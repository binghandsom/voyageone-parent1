<!-- define -->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/include/define.jsp"%>
<!-- End define -->
<head>
<title>BI | Sales Home Page</title>

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
					<div class="col-md-10 mmCustomScrollbar"
						style="padding-left: 1px; padding-right: 10px; max-height: 800px; overflow-x: hidden;">
						<div class="panel panel-patit rounded">
							<div class="panel-heading margin-bottom-5" >Yesterday Main KPI</div>
							<div class="panel-body" >
								<!-- 主要KPI[模块] -->
								<%@  include file="./yesterday_kpi.jsp"%>
								<!-- 占比模块 -->
								<%@ include file="./compare_rate.jsp"%>
							</div>
						</div>

						<div class="panel panel-patit rounded">
							<div class="panel-heading" >Last 30 Days TimeLine</div>
							<div class="panel-body" >
								<!-- 按时间Chart[模块] -->
								<%@ include file="./time_line.jsp"%>
							</div>
						</div>
						<div class="panel panel-patit rounded">
							<div class="panel-heading" >
								<div class="row" >
									<div class="col-lg-9" >Sales Ranking Report</div>
									<div class="col-lg-3" >
										<div id="brand_category_time_radioset" class="ui-buttonset" style="margin-top:-3px;margin-bottom:-6px">
							                <input type="radio" id="brand_category_time_1" name="brand_category_time_radioset" checked="checked" class="ui-helper-hidden-accessible">
							                <label for="brand_category_time_1" class="ui-button ui-widget ui-state-default ui-button-text-only ui-corner-left ui-state-active" role="button" aria-disabled="false" aria-pressed="true">
												<span class="ui-button-text">YDAY</span>
							                </label>
							                <input type="radio" id="brand_category_time_2" name="brand_category_time_radioset" class="ui-helper-hidden-accessible">
							            	<label for="brand_category_time_2" class="ui-button ui-widget ui-state-default ui-button-text-only" role="button" aria-disabled="false" aria-pressed="false">
							            		<span class="ui-button-text">30Days</span>
							            	</label>
							                <input type="radio" id="brand_category_time_3" name="brand_category_time_radioset" class="ui-helper-hidden-accessible">
							                <label for="brand_category_time_3" class="ui-button ui-widget ui-state-default ui-button-text-only ui-corner-right" role="button" aria-disabled="false" aria-pressed="false">
							                	<span class="ui-button-text">YTD</span>
							                </label>
							           </div>
									</div>
								</div>
							</div>
							<div class="panel-body" >
								<!-- 产品 品牌/分类模块 -->
								<%@ include file="./brand_category.jsp"%>
							</div>
						</div>
					</div>
					<!-- search area start -->
					<div class="col-md-2"
						style="padding-left: 1px; padding-right: 1px;">
						<!-- 检索模块 -->
						<%@ include file="./search.jsp"%>
					</div>
					<!-- search area end -->
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
		src="<%=webRootPath%>/resource/js/module/saleshome/main.js"></script>

</body>
</html>