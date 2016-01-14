// 销售用js[时间]
// 图表实例化------------------
var sales_category_chart = echarts.init(document.getElementById('sales_category_chart'), theme);

var category_bar_line_chart  = new BarLineChart();

function category_init(eventTriggerFlag) {
	//画面Botton初期化
	categoryBtnInit();
	//请求 
	category_flush(eventTriggerFlag);
}

function categoryBtnInit() {
	// 初始化控件
	barLineChart = category_bar_line_chart;
	barLineChart.setChart(sales_category_chart);
	barLineChart.setKpiName("qty");
	barLineChart.setLineKpiName("yoy");
	
	// 注册事件
	barLineChart.setClickEvent(events.CLICK, category_click_handler);

	// 表格控件
	barLineChart.setGridSelector("#sales_category_grid_table");
	// 表格翻页控件
	barLineChart.setPagerSelector("#sales_category_grid_pager");
	// 导出Excel方法
	barLineChart.setGenExportExce(genCategoryExportExcel);

	//init [窗体大小]
	if (category_report_size == 1) {
		$("#sales_category_block").hide();
	} else if (category_report_size == 2) {
		$("#category_window_ctrl").attr("class","glyphicon glyphicon-minus");
		$("#sales_category_block").attr("class","col-md-12");
		sales_category_chart.resize();
	}
	
	// [窗体大小]按钮
	$("#category_window_ctrl").click(function(){
		//模块大小控制
		if ($("#category_window_ctrl").attr("class") == "glyphicon glyphicon-resize-horizontal") {
			// 扩大显示
			$("#category_window_ctrl").attr("class","glyphicon glyphicon-minus");
			$("#sales_category_block").attr("class","col-md-12");
			
			// 分类模块[隐藏]
			$("#sales_brand_block").hide();
		} else {
			// 缩小显示
			if ($("#sales_category_grid_table").is(":visible")==true) {
				$("#category_table_ctl").click();
			}
			$("#category_window_ctrl").attr("class","glyphicon glyphicon-resize-horizontal");
			$("#sales_category_block").attr("class","col-md-6");
			
			// 品牌模块[显示]
			$("#sales_brand_block").show();
		}
		//chart 刷新
		sales_category_chart.resize();
	});

	// 表格显示控制
	$("#category_table_ctl").click(function() {
		 if ($("#sales_category_block").hasClass("col-md-6")) {
			 $("#category_window_ctrl").click();
		 }
		 if ($("#sales_category_grid_table").is(":visible")==false) {
			 $("#sales_category_table").slideDown("fast");
			 $("#category_table_ctl").css("color","rgb(3, 127, 216)");	
			barLineChart.groupFrozeDataTable($("#sales_category_grid_table"));
		 } else {
			 $("#sales_category_table").slideUp("fast");
			 $("#category_table_ctl").css("color","");	
		 }
	})
	//创建表格
	barLineChart.initDataTable();
}

function category_flush(eventTriggerFlag) {
	doGetCategoryDataReq(eventTriggerFlag);
}

//请求 
function doGetCategoryDataReq(eventTriggerFlag) {
	// 载入开始
	category_bar_line_chart.doLoading();
	bigdata.post(rootPath + "/manage/getSalesDetailCategoryData.html", sales_search_cond, doGetCategoryDataReq_end, '', eventTriggerFlag);
}

//请求  结束
function doGetCategoryDataReq_end(json, eventTriggerFlag) {
	barLineChart = category_bar_line_chart;
	// 请求后数据缓存
	datas = json.categoryDisBean;
	barLineChart.setData(datas)
	// 载入结束
	barLineChart.endLoading();
	// 图形刷新
	barLineChart.refreshChart();
	// 表格刷新
	barLineChart.refreshTableData();
	// 面包屑刷新
    refresh_category_crumb(eventTriggerFlag);
}

//图表刷新(指定KPI)
function category_chart_refreshChart(kpi_show_obj){
	kpi_show = kpi_show_obj['kpi_name']
	line_kpi_show = kpi_show_obj['line_kpi_name'];
	barLineChart = category_bar_line_chart;
	barLineChart.setKpiName(kpi_show);
	barLineChart.setLineKpiName(line_kpi_show);
	barLineChart.refreshChart();
}

function category_click_handler(param) {
	barLineChart = category_bar_line_chart;
	
	disBeans = barLineChart.getData();
	selectedBean= disBeans[param.dataIndex];
	// 最终层的场合
	if (disBeans.length<=1 && selectedBean.type==0) {
		return false;
	}
	// 请求再设定
	selectedValue = barLineChart.getClickValue(param);
	// 设置品牌条件
	refreshPageSearchCategoryCond(selectedValue);
	// 发送事件
	$(document).triggerHandler(charts_cust_events.CATEGORY_PRESS);
}

//面包屑刷新
function refresh_category_crumb(eventTriggerFlag) {
	barLineChart = category_bar_line_chart;
	if (eventTriggerFlag !="0" 
		&& eventTriggerFlag != charts_cust_events.CATEGORY_PRESS) {
		return;
	}
	if (eventTriggerFlag == "0") {
		barLineChart.setCrumb([]);
	}
	
	crumbsHtml = barLineChart.getCrumbText(clickCategoryCrumbs);
	$("#category_breadcrumb").empty().append(crumbsHtml);
}

//面包屑点击事件
//crumbsContent : 全体面包屑
//crumbItem ：当前面包屑
//selectIndex ：选中Index
function clickCategoryCrumbs(crumbsContent, crumbItem, selectIndex) {
	barLineChart = category_bar_line_chart;
	// 面包屑 移除
	var popCount = crumbsContent.length - selectIndex;	
	for (var i = 0; i < popCount; i++) {
		crumbsContent.pop();
	}
	// 设置条件
	selectedValue = crumbItem.data;
	clickSelectedObj = {"title":crumbItem.title, "data":crumbItem.value};
	barLineChart.setClickSelectedObj(clickSelectedObj);
	
	refreshPageSearchCategoryCond(selectedValue);
	// 发送事件
	$(document).triggerHandler(charts_cust_events.CATEGORY_PRESS);
}

function genCategoryExportExcel() {
	var url_param = $.param(sales_search_cond);
	var download_url =rootPath + "/manage/getSalesDetailCategoryDataExcel.html?"+url_param;
	$.fileDownload(download_url, {
		failMessageHtml: "There was a problem generating your report, please try again."
	});
}