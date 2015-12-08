// 画面初期显示js
// 检索模块初期化
// 		检索变量

$(function(){
	$("#pageName").html("Check Upload Data Page");
	$('#container').bind("search_page_loaded",function(){
		initChartReq();
	});
	// search page init
	initSearchPage();
	
});


// 图形控件初期加载
function initChartReq(){
	// 画面控件初期设定
	initPageControl();

	//[品牌][品类]销量请求
	check_explanation_init();
}

//画面控件初期设定
function initPageControl(){
}

function checkFinanceUploadDataParam(eventTriggerFlag) {
	bigdata.post(rootPath + "/manage/checkFinanceErrorDataParam.html", finance_search_cond, function (json){
		//请求
		doGetFinancialCheckUploadDataReq();
	}, '');
}

function updateFinanceUploadDataParam(eventTriggerFlag) {
	bigdata.post(rootPath + "/manage/checkFinanceErrorDataParam.html", finance_search_cond, function (json){
		//请求
		doUpdateFinancialCheckUploadDataReq();
	}, '');
}