// 销售用js[时间]
// 图表实例化------------------
var sales_size_chart = echarts.init(document.getElementById('sales_size_chart'), theme);

var size_bar_line_chart  = new BarLineChart();

function size_init(eventTriggerFlag) {
	//画面Botton初期化
	sizeBtnInit();
	//请求 
	size_refush(eventTriggerFlag);
}

function sizeBtnInit() {
	// 初始化控件
	barLineChart = size_bar_line_chart;
	barLineChart.setChart(sales_size_chart);
	barLineChart.setKpiName("qty");
	barLineChart.setLineKpiName("yoy");
	
	// 注册事件
	barLineChart.setClickEvent(events.CLICK, size_click_handler);

	// 表格控件
	barLineChart.setGridSelector("#sales_size_grid_table");
	// 表格翻页控件
	barLineChart.setPagerSelector("#sales_size_grid_pager");
	// 导出Excel方法
	barLineChart.setGenExportExce(genSizeExportExcel);
	

	//init [窗体大小]
	if (size_report_size == 1) {
		$("#sales_size_block").hide();
	} else if (size_report_size == 2) {
		$("#size_window_ctrl").attr("class","glyphicon glyphicon-minus");
		$("#sales_size_block").attr("class","col-md-12");
		sales_size_chart.resize();
	}
	
	// [窗体大小]按钮
	$("#size_window_ctrl").click(function(){
		//模块大小控制
		if ($("#size_window_ctrl").attr("class") == "glyphicon glyphicon-resize-horizontal") {
			// 扩大显示
			$("#size_window_ctrl").attr("class","glyphicon glyphicon-minus");
			$("#sales_size_block").attr("class","col-md-12");
			
			// color模块[隐藏]
			$("#sales_color_block").hide();
		} else {
			// 缩小显示
			if ($("#sales_size_grid_table").is(":visible")==true) {
				$("#size_table_ctl").click();
			}
			$("#size_window_ctrl").attr("class","glyphicon glyphicon-resize-horizontal");
			$("#sales_size_block").attr("class","col-md-6");
			
			// color模块[显示]
			$("#sales_color_block").show();
		}
		//chart 刷新
		sales_size_chart.resize();
	});
	
	// 表格显示控制
	$("#size_table_ctl").click(function() {
		 if ($("#sales_size_block").hasClass("col-md-6")) {
			 $("#size_window_ctrl").click();
		 }
		 if ($("#sales_size_grid_table").is(":visible")==false) {
			 $("#sales_size_table").slideDown("fast");
			 $("#size_table_ctl").css("color","rgb(3, 127, 216)");	
			barLineChart.groupFrozeDataTable($("#sales_size_grid_table"));
		 } else {
			 $("#sales_size_table").slideUp("fast");
			 $("#size_table_ctl").css("color","");	
		 }
	})
	//创建表格
	barLineChart.initDataTable();
}

//请求 
function size_refush(eventTriggerFlag) {
	doGetSizeDataReq(eventTriggerFlag);
}

//请求 
function doGetSizeDataReq(eventTriggerFlag) {
	// 载入开始
	size_bar_line_chart.doLoading();
	bigdata.post(rootPath + "/manage/getSalesDetailSizeData.html", sales_search_cond, doGetSizeDataReq_end, '', eventTriggerFlag);
}

//请求  结束
function doGetSizeDataReq_end(json, eventTriggerFlag) {
	barLineChart = size_bar_line_chart;
	// 请求后数据缓存
	datas = json.sizeDisBean;
	barLineChart.setData(datas)
	// 载入结束
	barLineChart.endLoading();
	// 图形刷新
	barLineChart.refreshChart();
	// 表格刷新
	barLineChart.refreshTableData();
	// 面包屑刷新
    refresh_size_crumb(eventTriggerFlag);
}

//图表刷新(指定KPI)
function size_chart_refreshChart(kpi_show_obj){
	kpi_show = kpi_show_obj['kpi_name']
	line_kpi_show = kpi_show_obj['line_kpi_name'];
	barLineChart = size_bar_line_chart;
	barLineChart.setKpiName(kpi_show);
	barLineChart.setLineKpiName(line_kpi_show);
	barLineChart.refreshChart();
}

function size_click_handler(param) {
	barLineChart = size_bar_line_chart;
	
	disBeans = barLineChart.getData();
	// 最终层的场合
	if (disBeans.length<=1) {
		return false;
	}
	// 请求时间再设定
	selectedValue = barLineChart.getClickValue(param);
	// 设置Size条件
	refreshPageSearchSizeCond(selectedValue);
	// 发送事件
	$(document).triggerHandler(charts_cust_events.SIZE_PRESS);
}

//面包屑刷新
function refresh_size_crumb(eventTriggerFlag) {
	barLineChart = size_bar_line_chart;
	if (eventTriggerFlag !="0" 
		&& eventTriggerFlag != charts_cust_events.SIZE_PRESS) {
		return;
	}
	if (eventTriggerFlag == "0") {
		barLineChart.setCrumb([]);
	}
	
	crumbsHtml = barLineChart.getCrumbText(click_sizeCrumbs);
	$("#size_breadcrumb").empty().append(crumbsHtml);
}

//面包屑点击事件
//crumbsContent : 全体面包屑
//crumbItem ：当前面包屑
//selectIndex ：选中Index
function click_sizeCrumbs(crumbsContent, crumbItem, selectIndex) {	
	// 面包屑 移除
	var popCount = crumbsContent.length - selectIndex;	
	for (var i = 0; i < popCount; i++) {
		crumbsContent.pop();
	}
	// 设置日期条件
	selectedValue = crumbItem.data;
	refreshPageSearchSizeCond(selectedValue);
	// 发送事件
	$(document).triggerHandler(charts_cust_events.SIZE_PRESS);
}

function genSizeExportExcel() {
	var url_param = $.param(sales_search_cond);
	var download_url =rootPath + "/manage/getSalesDetailSizeDataExcel.html?"+url_param;
	$.fileDownload(download_url, {
		failMessageHtml: "There was a problem generating your report, please try again."
	});
}