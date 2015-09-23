// 销售用js[时间]
// 图表实例化------------------
var sales_color_chart = echarts.init(document.getElementById('sales_color_chart'), theme);

var color_bar_line_chart  = new BarLineChart();

function color_init(eventTriggerFlag) {
	//画面Botton初期化
	colorBtnInit();
	//请求 
	color_refush(eventTriggerFlag);
}

function colorBtnInit() {
	// 初始化控件
	barLineChart = color_bar_line_chart;
	barLineChart.setChart(sales_color_chart);
	barLineChart.setKpiName("qty");
	barLineChart.setLineKpiName("yoy");
	
	// 注册事件
	barLineChart.setClickEvent(events.CLICK, color_click_handler);

	// 表格控件
	barLineChart.setGridSelector("#sales_color_grid_table");
	// 表格翻页控件
	barLineChart.setPagerSelector("#sales_color_grid_pager");
	// 导出Excel方法
	barLineChart.setGenExportExce(genColorExportExcel);
	
	//init [窗体大小]
	if (color_report_size == 1) {
		$("#sales_color_block").hide();
	} else if (color_report_size == 2) {
		$("#color_window_ctrl").attr("class","glyphicon glyphicon-minus");
		$("#sales_color_block").attr("class","col-md-12");
		sales_color_chart.resize();
	}
	
	// [窗体大小]按钮
	$("#color_window_ctrl").click(function(){
		//模块大小控制
		if ($("#color_window_ctrl").attr("class") == "glyphicon glyphicon-resize-horizontal") {
			// 扩大显示
			$("#color_window_ctrl").attr("class","glyphicon glyphicon-minus");
			$("#sales_color_block").attr("class","col-md-12");
			
			// size模块[隐藏]
			$("#sales_size_block").hide();
		} else {
			// 缩小显示
			if ($("#sales_color_grid_table").is(":visible")==true) {
				$("#color_table_ctl").click();
			}
			$("#color_window_ctrl").attr("class","glyphicon glyphicon-resize-horizontal");
			$("#sales_color_block").attr("class","col-md-6");
			
			// size模块[显示]
			$("#sales_size_block").show();
		}
		//chart 刷新
		sales_color_chart.resize();
	});

	// 表格显示控制
	$("#color_table_ctl").click(function() {
		 if ($("#sales_color_block").hasClass("col-md-6")) {
			 $("#color_window_ctrl").click();
		 }
		 if ($("#sales_color_grid_table").is(":visible")==false) {
			 $("#sales_color_table").slideDown("fast");
			 $("#color_table_ctl").css("color","rgb(3, 127, 216)");	
			barLineChart.groupFrozeDataTable($("#sales_color_grid_table"));
		 } else {
			 $("#sales_color_table").slideUp("fast");
			 $("#color_table_ctl").css("color","");	
		 }
	})
	//创建表格
	barLineChart.initDataTable();
}

function color_refush(eventTriggerFlag) {
	doGetColorDataReq(eventTriggerFlag);
}

//请求 
function doGetColorDataReq(eventTriggerFlag) {
	// 载入开始
	color_bar_line_chart.doLoading();
	bigdata.post(rootPath + "/manage/getSalesDetailColorData.html", sales_search_cond, doGetColorDataReq_end, '', eventTriggerFlag);
}

//请求  结束
function doGetColorDataReq_end(json, eventTriggerFlag) {
	barLineChart = color_bar_line_chart;
	// 请求后数据缓存
	datas = json.colorDisBean;
	barLineChart.setData(datas)
	// 载入结束
	barLineChart.endLoading();
	// 图形刷新
	barLineChart.refreshChart();
	// 表格刷新
	barLineChart.refreshTableData();
	// 面包屑刷新
    refresh_color_crumb(eventTriggerFlag);
}

//图表刷新(指定KPI)
function color_chart_refreshChart(kpi_show_obj){
	kpi_show = kpi_show_obj['kpi_name']
	line_kpi_show = kpi_show_obj['line_kpi_name'];
	barLineChart = color_bar_line_chart;
	barLineChart.setKpiName(kpi_show);
	barLineChart.setLineKpiName(line_kpi_show);
	barLineChart.refreshChart();
}

function color_click_handler(param) {
	barLineChart = color_bar_line_chart;
	
	disBeans = barLineChart.getData();
	// 最终层的场合
	if (disBeans.length<=1) {
		return false;
	}
	// 请求时间再设定
	selectedValue = barLineChart.getClickValue(param);
	// 设置品牌条件
	refreshPageSearchColorCond(selectedValue);
	// 发送事件
	$(document).triggerHandler(charts_cust_events.COLOR_PRESS);
}

//面包屑刷新
function refresh_color_crumb(eventTriggerFlag) {
	barLineChart = color_bar_line_chart;
	if (eventTriggerFlag !="0" 
		&& eventTriggerFlag != charts_cust_events.COLOR_PRESS) {
		return;
	}
	if (eventTriggerFlag == "0") {
		barLineChart.setCrumb([]);
	}
	
	crumbsHtml = barLineChart.getCrumbText(click_colorCrumbs);
	$("#color_breadcrumb").empty().append(crumbsHtml);
}

//面包屑点击事件
//crumbsContent : 全体面包屑
//crumbItem ：当前面包屑
//selectIndex ：选中Index
function click_colorCrumbs(crumbsContent, crumbItem, selectIndex) {	
	// 面包屑 移除
	var popCount = crumbsContent.length - selectIndex;	
	for (var i = 0; i < popCount; i++) {
		crumbsContent.pop();
	}
	// 设置日期条件
	selectedValue = crumbItem.data;
	refreshPageSearchColorCond(selectedValue);
	// 发送事件
	$(document).triggerHandler(charts_cust_events.COLOR_PRESS);
}

function genColorExportExcel() {
	var url_param = $.param(sales_search_cond);
	var download_url =rootPath + "/manage/getSalesDetailColorDataExcel.html?"+url_param;
	$.fileDownload(download_url, {
		failMessageHtml: "There was a problem generating your report, please try again."
	});
}