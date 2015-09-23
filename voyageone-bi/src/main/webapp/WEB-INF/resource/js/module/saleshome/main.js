// 画面初期显示js
// 检索模块初期化

$(function(){
	//画面控件初期设定
	initPageControl();
	// search page init 
	initSearchPage();
});

//画面控件初期设定
function initPageControl(){
	$("#pageName").html("Sales Home Page");

	$('#container').bind("search_page_loaded",function(){
		initChartReq();
	});
}

// 图形控件初期加载
function initChartReq(){
	// 画面控件初期设定
	initPageControl();
	
	//昨天主要KPI
	initYesterdayPKI();
	
	// [时间]销量请求
	initTimeline("0");
	
	//占比数据请求
	compare_rate_init("0");
	
	//[品牌][品类]销量请求
	brand_category_init("0");
}


function checkSalesHomeParam(eventTriggerFlag) {
	bigdata.post(rootPath + "/manage/checkSalesHomeParam.html", sales_search_cond, function (json){
		searchButtonChartReq();
    }, '');
}

//图形控件初期加载
function searchButtonChartReq(){
	// 昨天主要KPI请求
	doGetYesterdayPKReq("0");
	
	// [时间]销量请求
	doGetSalesHomeTimelineDataReq("0");
	
	//占比数据请求
	doGetSalesHomeCompareData("0");
	
	//[品牌][品类]销量请求
	doGetSalesHomeBrandCategoryDataReq("0");
}

//昨天主要KPI
function initYesterdayPKI(){
	// 昨天主要KPI请求
	doGetYesterdayPKReq("0");
}

//按时间请求 开始
//eventTriggerFlag：时间触发Flag
//					"0"：不触发	"1"：触发
function doGetYesterdayPKReq(eventTriggerFlag) {
	// 月销量请求
	bigdata.post(rootPath + "/manage/getSalesHomeYPKData.html", sales_search_cond, doGetYesterdayPKReq_end, '');
}

//按时间请求  结束
function doGetYesterdayPKReq_end(json) {
	$('#p_kpi1').text(formatNumber(json.yesterdayKpi.qty_kpi.value,0));
	$("#p_kpi1_ld_up").attr('title',formatNumber(json.yesterdayKpi.qty_kpi.value_r_rate_up*100, 0)+'%');
	$("#p_kpi1_ld_down").attr('title',formatNumber(json.yesterdayKpi.qty_kpi.value_r_rate_up*100, 0)+'%');
	if (json.yesterdayKpi.qty_kpi.value_r_rate >= 1) {
		$("#p_kpi1_ld_up").css('display','block');
		$("#p_kpi1_ld_down").css('display','none');
	} else {
		$("#p_kpi1_ld_up").css('display','none');
		$("#p_kpi1_ld_down").css('display','block');
	}
	$('#p_kpi2').text(formatNumber(json.yesterdayKpi.amt_kpi.value, 0));
	$("#p_kpi2_ld_up").attr('title',formatNumber(json.yesterdayKpi.amt_kpi.value_r_rate_up*100, 0)+'%');
	$("#p_kpi2_ld_down").attr('title',formatNumber(json.yesterdayKpi.amt_kpi.value_r_rate_up*100, 0)+'%');
	if (json.yesterdayKpi.amt_kpi.value_r_rate >= 1) {
		$("#p_kpi2_ld_up").css('display','block');
		$("#p_kpi2_ld_down").css('display','none');
	} else {
		$("#p_kpi2_ld_up").css('display','none');
		$("#p_kpi2_ld_down").css('display','block');
	}
	$('#p_kpi3').text(formatNumber(json.yesterdayKpi.order_kpi.value,0));
	$("#p_kpi3_ld_up").attr('title',formatNumber(json.yesterdayKpi.order_kpi.value_r_rate_up*100, 0)+'%');
	$("#p_kpi3_ld_down").attr('title',formatNumber(json.yesterdayKpi.order_kpi.value_r_rate_up*100, 0)+'%');
	if (json.yesterdayKpi.order_kpi.value_r_rate >= 1) {
		$("#p_kpi3_ld_up").css('display','block');
		$("#p_kpi3_ld_down").css('display','none');
	} else {
		$("#p_kpi3_ld_up").css('display','none');
		$("#p_kpi3_ld_down").css('display','block');
	}
	$('#p_kpi4').text(formatNumber(json.yesterdayKpi.atv_kpi.value,0));
	$("#p_kpi4_ld_up").attr('title',formatNumber(json.yesterdayKpi.atv_kpi.value_r_rate_up*100, 0)+'%');
	$("#p_kpi4_ld_down").attr('title',formatNumber(json.yesterdayKpi.atv_kpi.value_r_rate_up*100, 0)+'%');
	if (json.yesterdayKpi.atv_kpi.value_r_rate >= 1) {
		$("#p_kpi4_ld_up").css('display','block');
		$("#p_kpi4_ld_down").css('display','none');
	} else {
		$("#p_kpi4_ld_up").css('display','none');
		$("#p_kpi4_ld_down").css('display','block');
	}
	$('#p_kpi5').text(formatNumber(json.yesterdayKpi.uv_kpi.value,0));
	$("#p_kpi5_ld_up").attr('title',formatNumber(json.yesterdayKpi.uv_kpi.value_r_rate_up*100, 0)+'%');
	$("#p_kpi5_ld_down").attr('title',formatNumber(json.yesterdayKpi.uv_kpi.value_r_rate_up*100, 0)+'%');
	if (json.yesterdayKpi.uv_kpi.value_r_rate >= 1) {
		$("#p_kpi5_ld_up").css('display','block');
		$("#p_kpi5_ld_down").css('display','none');
	} else {
		$("#p_kpi5_ld_up").css('display','none');
		$("#p_kpi5_ld_down").css('display','block');
	}
}

