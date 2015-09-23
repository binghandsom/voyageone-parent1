// 画面初期显示js
// 检索模块初期化
// 		检索变量

$(function(){
	$("#pageName").html("Tax Report Page");
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
	tax_init();
}

//画面控件初期设定
function initPageControl(){
}

function checkFinanceTaxParam(eventTriggerFlag) {
	bigdata.post(rootPath + "/manage/checkFinanceTaxReportDataParam.html", finance_search_cond, function (json){
		//请求
		doGetFinancialReportTaxReportDataReq();
	}, '');
}

function checkFinanceTaxParamExport(eventTriggerFlag) {
	bigdata.post(rootPath + "/manage/checkFinanceTaxReportDataParam.html", finance_search_cond, function (json) {
		doGetFinancialTaxExportReq();
	}, '');
}

function doGetFinancialTaxExportReq() {
	bigdata.post(rootPath + "/manage/getReportTax.html", finance_search_cond, doExportExcel, '');

}

function doExportExcel(json) {
	var report_file_name = json.report_file_name;
	$.fileDownload(rootPath + "/manage/downloadReportTax.html?report_file_name=" + report_file_name, {
		failMessageHtml: "There was a problem generating your report, please try again."
	});

	doGetDataReportTaxDataReq_end;

}