// 		检索变量
var sales_search_cond = {
	// 检索条件
	//ShopID
	shop_ids : '',
		
	//categorys
	category_child : '0',	
	//categorys
	category_ids : '',

	//brand
	brand_ids : '',

	//color
	color_ids : '',
	
	//size
	size_ids : '',

	//product
	product_codes : '',
	
	// [时间]模块
	time_type : '',
	time_start : '',
	time_end : '',
	sort_col : '',
	sord : ''
};

//画面初期化
function initSearchPage(){
	//画面检索条件初期化
	initSearchParam();
	
	// 画面控件初期值设定
	doPageInitDataReq();
	
	//	[检索]按钮
	$('#search_btn').button();
	$("#search_btn").click(function(){
		sales_search_cond.category_child='0';
		initGetCond();
		checkSalesDetailParam("0");
	});
}

//画面检索条件初期化
function initSearchParam(){
	//日期控件初期设置
	initSearchDate();
	//Category控件初期设置
	initSearchCategory();
	//Brand控件初期设置
	initSearchBrand();
	//Color控件初期设置
	initSearchColor();
	//Size控件初期设置
	initSearchSize();
	//产品控件初期设置
	initSearchProduct();
}

//category
var search_categorys_buf = [];
function initSearchCategory() {
	bigdata.post(rootPath + "/manage/getUserCategoryList.html", sales_search_cond, function (json) {
    	search_categorys_buf = json.cmbCategorys;
    	if (search_brands_buf) {
        	for (var i = 0; i < search_categorys_buf.length; i++){
    			var code = search_categorys_buf[i].code;
    			var level = code.split('-')[1];
    			var name = search_categorys_buf[i].name;
      			$("#search_category_l"+level).append("<option value='" + code + "'>" + name + "</option>");
        	}
    	}
    	$('#search_category_id').selectpicker('refresh');
    }, '');
	
	$('#search_category_clean').click(function(){
		$('#search_category_id').selectpicker('deselectAll');
	});		
}

//brand
var search_brands_buf = [];
function initSearchBrand() {
	bigdata.post(rootPath + "/manage/getUserBrandList.html", sales_search_cond, function (json) {
    	search_brands_buf = json.cmbBrands;
    	if (search_brands_buf) {
        	for (var i = 0; i < search_brands_buf.length; i++){
    			var code = search_brands_buf[i].code;
    			var name = search_brands_buf[i].name;
      			$("#search_brand_id").append("<option value='" + code + "'>" + name + "</option>");
        	}
    	}
    	$('#search_brand_id').selectpicker('refresh');
    }, '');
	
	$('#search_brand_clean').click(function(){
		$('#search_brand_id').selectpicker('deselectAll');
	});	
}

//color
var search_colors_buf = [];
function initSearchColor() {
	bigdata.post(rootPath + "/manage/getUserColorList.html", sales_search_cond, function (json) {
    	search_colors_buf = json.cmbColors;
    	if (search_colors_buf) {
        	for (var i = 0; i < search_colors_buf.length; i++){
    			var code = search_colors_buf[i].code;
    			var name = search_colors_buf[i].name;
      			$("#search_color_id").append("<option value='" + code + "'>" + name + "</option>");
        	}
    	}
    	$('#search_color_id').selectpicker('refresh');
    }, '');
	
	$('#search_color_clean').click(function(){
		$('#search_color_id').selectpicker('deselectAll');
	});	
}

//size
var search_sizes_buf = [];
function initSearchSize() {
	bigdata.post(rootPath + "/manage/getUserSizeList.html", sales_search_cond, function (json) {
    	search_sizes_buf = json.cmbSizes;
    	if (search_colors_buf) {
        	for (var i = 0; i < search_sizes_buf.length; i++){
    			var code = search_sizes_buf[i].code;
    			var name = search_sizes_buf[i].name;
      			$("#search_size_id").append("<option value='" + code + "'>" + name + "</option>");
        	}
    	}
    	$('#search_size_id').selectpicker('refresh');
    }, '');
	
	$('#search_size_clean').click(function(){
		$('#search_size_id').selectpicker('deselectAll');
	});	
}

//product
function initSearchProduct() {
	// 产品search
	$("#search_prod_id").autocomplete({
		source:function(query,process){
			var squery = query.term.trim();
			var queryObj = {productQueryStr:squery}
			if (squery.length>2) {
				bigdata.post(rootPath + "/manage/getUserProductList.html", queryObj, function (json) {
		    		var respData = [];
			    	if (json.cmbProducts.length>0) {
			    		for (var i = 0; i < json.cmbProducts.length; i++){
			    			respData.push(json.cmbProducts[i].name);
			    		}
			    	}
		    		return process(respData);
			    }, '');
			}
        }
	});
	
	$('#search_prod_clean').click(function(){
		$('#search_prod_id').attr("value","");
	});	
}

//日期控件初期设置
function initSearchDate() {
	var dayOption = {format: 'yyyy-mm-dd',
					startView: 2,
					minView: 2,
					maxView: 2,
					todayHighlight: 1,
					autoclose: 1};
	
	$('#search_data_type').on('change', function(){
		var selected = $(this).find("option:selected").val();
		setSelectedSearchDate(selected);
	});
	setSelectedSearchDate('Last 30 days');
}

//日期控件初期设置
function setSelectedSearchDate(option) {
	var searchDateStart = getDateStr(-31);
	var searchDateEnd = getDateStr(-1);
	if (option == 'Day') {
		searchDateStart = getMonthFirstDay();
		searchDateEnd = getDateStr(-1);
	} else if (option == 'Month') {
		searchDateStart = getYear()+'-01';
		searchDateEnd = getMonth;
	} else if (option == 'Year') {
		searchDateStart = getYear()-1;
		searchDateEnd = getYear();
	} else if (option == 'Yesterday') {
		searchDateStart = getDateStr(-1);
		searchDateEnd = getDateStr(-1);
	} else if (option == 'Last 7 Days') {
		searchDateStart = getDateStr(-7);
		searchDateEnd = getDateStr(-1);
	} else if (option == 'Last 30 days') {
		searchDateStart = getDateStr(-30);
		searchDateEnd = getDateStr(-1);
	}
	setSearchDate(option, searchDateStart, searchDateEnd);
}

//日期控件设置
function setSearchDate(option, searchDateStart, searchDateEnd) {
	var dayOption = {format: 'yyyy-mm-dd',
			startView: 2,
			minView: 2,
			maxView: 2,
			todayHighlight: 1,
			autoclose: 1};
	var monthOpion = {format: 'yyyy-mm',
			startView: 3,
			minView: 3,
			maxView: 3,
			todayHighlight: 1,
			autoclose: 1};
	var yearOpton = {format: 'yyyy',
			startView: 4,
			minView: 4,
			maxView: 4,
			todayHighlight: 1,
			autoclose: 1};

	var daySelectOption =  dayOption;
	var enable = false;
	if (option == 'Day') {
		enable = true;
	} else if (option == 'Month') {
		enable = true;
		daySelectOption = monthOpion;
	} else if (option == 'Year') {
		enable = true;
		daySelectOption = yearOpton;
	} else if (option == 'Yesterday') {
		searchDateStart = getDateStr(-1);
		searchDateEnd = getDateStr(-1);
	} else if (option == 'Last 7 Days') {
		searchDateStart = getDateStr(-7);
		searchDateEnd = getDateStr(-1);
	} else if (option == 'Last 30 days') {
		searchDateStart = getDateStr(-30);
		searchDateEnd = getDateStr(-1);
	}
	
	$("#search_date_start").datetimepicker('remove');
	$("#search_date_end").datetimepicker('remove');
	
	// 初期值设定
	$("#search_date_start").val(searchDateStart);
	$("#search_date_end").val(searchDateEnd);
	
	if (enable) {
		$("#search_date_start").datetimepicker(daySelectOption);
		$("#search_date_end").datetimepicker(daySelectOption);
		$('#search_date_start_span').css("color","#525252");
		$('#search_date_end_span').css("color","#525252");
	} else {
		$('#search_date_start_span').css("color","#b3b3b3");
		$('#search_date_end_span').css("color","#b3b3b3");
	}
}

// 画面初期显示内容取得
function doPageInitDataReq() {
	// 画面初期值请求
    bigdata.post(rootPath + "/manage/getUserChannelShopList.html", sales_search_cond, doPageInitDataReq_end, '');
}

//画面初期值取得结束
var channels_buff = [];
function doPageInitDataReq_end(json) {
	// 店铺信息获得
	channels_buff = json.cmbChannels;
	// 店铺初期化
	initChannelShops(channels_buff);
	// 画面检索条件取得
	initGetCond();
	//send event
    $(document).trigger("search_page_loaded");
}

// 店铺初期化
//		shops：店铺信息
function initChannelShops(channels){
	for (var i = 0; i < channels.length; i++){
		// 渠道信息取得
		var channel_code  = channels[i].code;
		var channel_name = channels[i].name;
		$("#search_channel_id").append("<option value='"+channel_code+"'>" + channel_name + "</option>");
		if (i==0) {
			//init shop
			initShops(channels[i].children);
		}
	}
	$('#search_channel_id').selectpicker('refresh');
	$('#search_channel_id').on('change', function(){
		var selected = $(this).find("option:selected").val();
		for (var i = 0; i < channels_buff.length; i++){
			var channel_code  = channels_buff[i].code;
			if (channel_code == selected) {
				initShops(channels_buff[i].children);
				// set shop select all
				$("#shop_selectall").attr("checked",'true');
			}
		}
	});
}

function initShops(shops){
	// 既存项目清空
	$("#search_shop_list").empty();

	for (var i = 0; i < shops.length; i++){
		// 该渠道店铺信息取得
		var code  = shops[i].code;
		var name = shops[i].name;
		$("#search_shop_list").append("<li class='list-group-item' style='padding:5px 15px;'>" +
			"<label class='checkbox inline' style='margin-top: 5px;margin-bottom: 5px;font-weight:500'>" +
			"<input type='checkbox' id='shop_selectall_sub' value='" + code +"'  onclick='setSelectAll(\"shop_selectall\");' checked/>" + name +
			"</label>" +
			"</li>");
	}
}

// 画面检索条件取得
function initGetCond() {
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
	
	// category
	var category_selected = $('#search_category_id').selectpicker('val');
	if (category_selected && category_selected.length>0) {
		sales_search_cond.category_ids = category_selected.join();
	} else {
		sales_search_cond.category_ids = "";
	}
	
	//brand
	var brand_selected = $('#search_brand_id').selectpicker('val');
	if (brand_selected && brand_selected.length>0) {
		sales_search_cond.brand_ids = brand_selected.join();
	} else {
		sales_search_cond.brand_ids = "";
	}
	
	//color
	var color_selected = $('#search_color_id').selectpicker('val');
	if (color_selected && color_selected.length>0) {
		sales_search_cond.color_ids = color_selected.join();
	} else {
		sales_search_cond.color_ids = "";
	}
	
	//size
	var size_selected = $('#search_size_id').selectpicker('val');
	if (size_selected && size_selected.length>0) {
		sales_search_cond.size_ids = size_selected.join();
	} else {
		sales_search_cond.size_ids = "";
	}
	
	//product_codes
	var product_selected = $('#search_prod_id').attr("value");
	if (product_selected && product_selected.length>0) {
		sales_search_cond.product_codes = product_selected;
	} else {
		sales_search_cond.product_codes = "";
	}

	// 日期
	var time_type = 'Day';	
	var search_data_type_selected = $('#search_data_type').selectpicker('val');
	if (search_data_type_selected == 'Day'
			|| search_data_type_selected == 'Yesterday'
			|| search_data_type_selected == 'Last 7 Days'
			|| search_data_type_selected == 'Last 30 days'
			) {
		time_type = 'Day';
	} else if (search_data_type_selected == 'Month') {
		time_type = 'Month';
	} else if (search_data_type_selected == 'Year') {
		time_type = 'Year';
	}
	sales_search_cond.time_type = time_type;
	sales_search_cond.time_start = dateDisplayToDB($("#search_date_start").val());
	sales_search_cond.time_end = dateDisplayToDB($("#search_date_end").val());
	return true;
}

function refreshPageSearchTimeCond(data_type, start_date, end_date){
	$('#search_data_type').selectpicker('val', data_type);
	setSearchDate(data_type, start_date, end_date);
	initGetCond();
}

function refreshPageSearchBrandCond(value) {
	$('#search_brand_id').selectpicker('val', [value]);
	initGetCond();
}

function refreshPageSearchCategoryCond(value) {
	sales_search_cond.category_child='1';
	$('#search_category_id').selectpicker('val', [value]);
	initGetCond();
}

function refreshPageSearchColorCond(value) {
	$('#search_color_id').selectpicker('val', [value]);
	initGetCond();
}

function refreshPageSearchSizeCond(value) {
	$('#search_size_id').selectpicker('val', [value]);
	initGetCond();
}



