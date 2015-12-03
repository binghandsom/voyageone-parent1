// 		检索变量
var sales_search_cond = {
							// [时间]模块
							// 		检索时间
							time : '',
							// 检索条件
							// 		ShopID
							shop_ids : ''
						};

//画面初期化
function initSearchPage(){
	//画面检索条件初期化
	initSearchParam();
	
	// 画面控件初期值设定
	doPageInitDataReq();
	
	//	[检索]按钮
	$('#search').button();
	$("#search").click(function(){
		initGetCond();
		checkSalesHomeParam();
	})
}


//画面检索条件初期化
function initSearchParam(){
	//日期控件初期设置
	initSearchDate();
}

//日期控件初期设置
function initSearchDate() {
	var dayOption = {format: 'yyyy-mm-dd',
					startView: 2,
					minView: 2,
					maxView: 2,
					todayHighlight: 1,
					autoclose: 1};
	
	// 日
	$("#search_date").datetimepicker('remove');
	$("#search_date").datetimepicker(dayOption);
	// 初期值设定
	var initSearchDate = getDateStr(-1);
	$("#search_date").val(initSearchDate);
	
}

// 画面初期显示内容取得
function doPageInitDataReq() {
	// 画面初期值请求
    bigdata.post(rootPath + "/manage/getUserShopList.html", sales_search_cond, doPageInitDataReq_end, '');
}

//画面初期值取得结束
function doPageInitDataReq_end(json) {
	// 店铺信息获得
	shops = json.cmbShops;
	// 店铺初期化
	initShops(shops);
	// 画面检索条件取得
	initGetCond();
    $('#container').trigger("search_page_loaded");
}

// 店铺初期化
//		shops：店铺信息
function initShops(shops){
	// 既存项目清空
	$("#search_shop_list").empty();

	for (var i = 0; i < shops.length; i++){
		// 该渠道店铺信息取得
		var code  = shops[i].code;
		var name = shops[i].name;
		$("#search_shop_list").append("<li class='list-group-item'><label class='checkbox inline' style='margin-top: 1px;margin-bottom: 1px;font-weight:500'><input type='checkbox' id='shop_selectall_sub' value='" + code +"'  onclick='setSelectAll(\"shop_selectall\");' checked/>" + name + "</label></li>");
	}
}

// 画面检索条件取得
function initGetCond() {
	// 日期
	sales_search_cond.time = dateDisplayToDB($("#search_date").val());
	// 门店
	str_shop_ids = '';
	checkname = 'shop_selectall';
	$("input[type='checkbox'][id='"+ checkname +"_sub']:checked").each(function () {
		str_shop_ids = str_shop_ids + $(this).val() + ",";
	  });
	if (str_shop_ids.length==0) {
		alert("please select shops.");
		return false;
	}
	sales_search_cond.shop_ids = str_shop_ids.substr(0, str_shop_ids.length-1);
	return true;
}

