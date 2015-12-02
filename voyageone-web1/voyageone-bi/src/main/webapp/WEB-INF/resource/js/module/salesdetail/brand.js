// 销售用js[时间]
// 图表实例化------------------
var sales_brand_chart = echarts.init(document.getElementById('sales_brand_chart'), theme);

var brand_bar_line_chart  = new BarLineChart();

function brand_init(eventTriggerFlag) {
	//画面Botton初期化
	brandBtnInit();
	//flush
	brand_reflush(eventTriggerFlag);
}

function brandBtnInit() {
	// 初始化控件
	barLineChart = brand_bar_line_chart;
	barLineChart.setChart(sales_brand_chart);
	barLineChart.setKpiName("qty");
	barLineChart.setLineKpiName("yoy");
	
	// 注册事件
	barLineChart.setClickEvent(events.CLICK, brand_click_handler);

	// 表格控件
	barLineChart.setGridSelector("#sales_brand_grid_table");
	// 表格翻页控件
	barLineChart.setPagerSelector("#sales_brand_grid_pager");
	// 导出Excel方法
	barLineChart.setGenExportExce(genBrandExportExcel);
	
	//init [窗体大小]
	if (brand_report_size == 1) {
		$("#sales_brand_block").hide();
	} else if (brand_report_size == 2) {
		$("#brand_window_ctrl").attr("class","glyphicon glyphicon-minus");
		$("#sales_brand_block").attr("class","col-md-12");
		sales_brand_chart.resize();
	}
	
	//[窗体大小]按钮
	$("#brand_window_ctrl").click(function(){
		// [品牌]模块大小控制
		if ($("#brand_window_ctrl").attr("class") == "glyphicon glyphicon-resize-horizontal") {
			// 扩大显示
			$("#brand_window_ctrl").attr("class","glyphicon glyphicon-minus");
			$("#sales_brand_block").attr("class","col-md-12");
			
			// 分类模块[隐藏]
			$("#sales_category_block").hide();			
		} else {
			// 缩小显示
			if ($("#sales_brand_grid_table").is(":visible")==true) {
				$("#brand_table_ctl").click();
			}
			$("#brand_window_ctrl").attr("class","glyphicon glyphicon-resize-horizontal");
			$("#sales_brand_block").attr("class","col-md-6");
			
			// 分类模块[显示]
			$("#sales_category_block").show();
		}
		//chart 刷新
		sales_brand_chart.resize();
	});
	
	// 表格显示控制
	$("#brand_table_ctl").click(function() {
		 if ($("#sales_brand_block").hasClass("col-md-6")) {
			 $("#brand_window_ctrl").click();
		 }
		 if ($("#sales_brand_grid_table").is(":visible")==false) {
			 $("#sales_brand_table").slideDown("fast");
			 $("#brand_table_ctl").css("color","rgb(3, 127, 216)");	
			 barLineChart.groupFrozeDataTable($("#sales_brand_grid_table"));
		 } else {
			 $("#sales_brand_table").slideUp("fast");
			 $("#brand_table_ctl").css("color","");	
		 }
	})
	//创建表格
	barLineChart.initDataTable();
}

function brand_reflush(eventTriggerFlag) {
	doGetBrandDataReq(eventTriggerFlag);
}

//请求 
function doGetBrandDataReq(eventTriggerFlag) {
	// 载入开始
	brand_bar_line_chart.doLoading();
	bigdata.post(rootPath + "/manage/getSalesDetailBrandData.html", sales_search_cond, doGetBrandDataReq_end, '', eventTriggerFlag);
}

//请求  结束
function doGetBrandDataReq_end(json, eventTriggerFlag) {
	barLineChart = brand_bar_line_chart;
	// 请求后数据缓存
	datas = json.brandDisBean;
	barLineChart.setData(datas)
	// 载入结束
	barLineChart.endLoading();
	// 图形刷新
	barLineChart.refreshChart();
	// 表格刷新
	barLineChart.refreshTableData();
	// 面包屑刷新
    refresh_brand_crumb(eventTriggerFlag);
}

//图表刷新(指定KPI)
function brand_chart_refreshChart(kpi_show_obj){
	kpi_show = kpi_show_obj['kpi_name'];
	line_kpi_show = kpi_show_obj['line_kpi_name'];
	barLineChart = brand_bar_line_chart;
	barLineChart.setKpiName(kpi_show);
	barLineChart.setLineKpiName(line_kpi_show);
	barLineChart.refreshChart();
}

function brand_click_handler(param) {
	barLineChart = brand_bar_line_chart;
	
	disBeans = barLineChart.getData();
	// 最终层的场合
	if (disBeans.length<=1) {
		return false;
	}
	// 请求时间再设定
	selectedValue = barLineChart.getClickValue(param);
	// 设置品牌条件
	refreshPageSearchBrandCond(selectedValue);
	// 发送事件
	$(document).triggerHandler(charts_cust_events.BRAND_PRESS);
}

//面包屑刷新
function refresh_brand_crumb(eventTriggerFlag) {
	barLineChart = brand_bar_line_chart;
	if (eventTriggerFlag !="0" 
		&& eventTriggerFlag != charts_cust_events.BRAND_PRESS) {
		return;
	}
	if (eventTriggerFlag == "0") {
		barLineChart.setCrumb([]);
	}
	
	crumbsHtml = barLineChart.getCrumbText(clickBrandCrumbs);
	$("#brand_breadcrumb").empty().append(crumbsHtml);
}

//面包屑点击事件
//crumbsContent : 全体面包屑
//crumbItem ：当前面包屑
//selectIndex ：选中Index
function clickBrandCrumbs(crumbsContent, crumbItem, selectIndex) {	
	// 面包屑 移除
	var popCount = crumbsContent.length - selectIndex;	
	for (var i = 0; i < popCount; i++) {
		crumbsContent.pop();
	}
	// 设置日期条件
	selectedValue = crumbItem.data;
	refreshPageSearchBrandCond(selectedValue);
	// 发送事件
	$(document).triggerHandler(charts_cust_events.BRAND_PRESS);
}

function genBrandExportExcel() {
	var url_param = $.param(sales_search_cond);
	var download_url =rootPath + "/manage/getSalesDetailBrandDataExcel.html?"+url_param;
	$.fileDownload(download_url, {
		failMessageHtml: "There was a problem generating your report, please try again."
	});
}