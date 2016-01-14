// 共通变数
// 		事件关联（自定义事件）
var charts_cust_events = {
    	// 时间chart压下
		TIME_PRESS: 'time_click',
		// 品类chart压下
		CATEGORY_PRESS: 'category_click',
		// 品牌chart压下
		BRAND_PRESS: 'brand_click',
		// 尺码分类chart压下
		SIZE_PRESS: 'size_click',
		// 价格带chart压下
		COLOR_PRESS: 'color_click'
}

// 画面初期显示js
$(function(){
	// 画面控件初期设定
	initPageControl();
	
	// search page init 
	initSearchPage();
});

var brand_report_size = 0;
var category_report_size = 0;
var color_report_size = 0;
var size_report_size = 0;

//画面控件初期设定
function initPageControl(){
	$("#pageName").html("Sales Detail Page");
	user_menu_reportl_size = $("#user_menu_reportl_size").attr("value");
	if (user_menu_reportl_size) {
		user_menu_reportl_size_arr = user_menu_reportl_size.split(",");
		if (user_menu_reportl_size_arr.length>0) {
			brand_report_size = user_menu_reportl_size_arr[0];
		}
		if (user_menu_reportl_size_arr.length>1) {
			category_report_size = user_menu_reportl_size_arr[1];
		}
		if (user_menu_reportl_size_arr.length>2) {
			color_report_size = user_menu_reportl_size_arr[2];
		}
		if (user_menu_reportl_size_arr.length>3) {
			size_report_size = user_menu_reportl_size_arr[3];
		}
	}

	$(document).bind("search_page_loaded",function(){
		initChartReq();
	});
	
	$(document).bind(charts_cust_events.TIME_PRESS,function(){
		checkSalesDetailParam(charts_cust_events.TIME_PRESS);
	});
	$(document).bind(charts_cust_events.CATEGORY_PRESS,function(){
		checkSalesDetailParam(charts_cust_events.CATEGORY_PRESS);
	});
	$(document).bind(charts_cust_events.BRAND_PRESS,function(){
		checkSalesDetailParam(charts_cust_events.BRAND_PRESS);
	});
	$(document).bind(charts_cust_events.SIZE_PRESS,function(){
		checkSalesDetailParam(charts_cust_events.SIZE_PRESS);
	});
	$(document).bind(charts_cust_events.COLOR_PRESS,function(){
		checkSalesDetailParam(charts_cust_events.COLOR_PRESS);
	});
}

// 图形控件初期加载
function initChartReq(){
	
	// [时间]销量请求
	initTimeline("0");
	
	//[品牌][品类]销量请求
	brand_init("0");
	
	//[品类]销量请求
	category_init("0");
	
	//[color]销量请求	
	color_init("0");

	//[size]销量请求	
	size_init("0");
	
	//[product]销量请求	
	product_sku_init("0");
}

function checkSalesDetailParam(eventTriggerFlag) {
	bigdata.post(rootPath + "/manage/checkSalesDetailParam.html", sales_search_cond, function (json){
		searchButtonChartReq(eventTriggerFlag);
    }, '');
}

//图形控件初期加载
function searchButtonChartReq(eventTriggerFlag){
	// [时间]销量请求
	timeline_reflush(eventTriggerFlag);
	//[分类][品牌]数据请求
	brand_reflush(eventTriggerFlag);
	//[品类]销量请求
	category_flush(eventTriggerFlag);
	//[color][品牌]数据请求
	color_refush(eventTriggerFlag);
	//[size]销量请求
	size_refush(eventTriggerFlag);
	//[product]销量请求	
	product_sku_refush(eventTriggerFlag);
}



//面包屑 Html 取得
//crumbsContent : 全体面包屑
//moduleName ：模块名
function getCrumbsHtml(crumbsContent, moduleName){
	// 面包屑项目取得
	function getLi(obj, index, attEvent) {		
		if (attEvent) {
			var li = $("<li>"),
			a = $("<a>", {"href":"javascript:void(0)"})
				.text(obj.title)
				.appendTo(li);
			
			a.click(function() {
				moduleName(crumbsContent, obj, index);
				//doCallback(callback2,['a','b']);
			});
			
			return li;
		} else {
			var li_end = $("<li>", {"class": "active"}).text(obj.title);
			
			return li_end;
		}
	}
	
	// 初始面包屑不显示
	if (crumbsContent.length <= 1) return null;
	
	var ul;	
	ul = $("<ul>", {"class": "pull-left breadcrumb", "style": "height:5px;top:0px;padding: 1px 100px; margin-bottom:0px"});
	
	// 面包屑项目生成
	$(crumbsContent).each(function(i, o){ 
		ul.append(getLi(o, i, i != crumbsContent.length - 1));
	})	
	return ul;
}