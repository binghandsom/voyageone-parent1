// 销售用js[时间]
// 图表实例化------------------
// srcipt标签式引入
function product_sku_init(eventTriggerFlag) {
	//画面Botton初期化
	productSkuBtnInit();
	//请求 
	product_sku_refush(eventTriggerFlag);
}

function getProductSkuBtnActiveId(){
	result = "";
	$("input[name='product_sku_radioset']:checked").each(function () {
		result = $(this).attr("id");
	});
	return result
}

function productSkuBtnInit() {
	//[窗体大小]按钮
	$("#product_window_ctrl").click(function(){
		if ($("#product_body").is(":visible")==false) {
			$("#product_window_ctrl").attr("class","glyphicon glyphicon-minus");
			$("#product_body").slideDown("fast");			
		} else {
			$("#product_window_ctrl").attr("class","glyphicon glyphicon-resize-vertical");			
			$("#product_body").slideUp("fast");
		}
	});
	
	//product_sku_radioset
	$('#product_sku_radioset').buttonset();
	$("#product_sku_radioset :radio").click(function(){
		var value = getProductSkuBtnActiveId();
		if (value == 'product_radioset') {
			if (isHaveProduct_buf) {
				refresh_product_table_data();
			} else {
				doGetProductDataReq("0")
			}
		} else if (value == 'sku_radioset') {
			if (isHaveSku_buf) {
				refresh_sku_table_data()
			} else {
				doGetSkuDataReq("0")
			}
		}
	});
	//创建表格
	init_product_table();
}

//请求返回缓存
var objProduct_buf = [];
var isHaveProduct_buf = false;
//请求返回缓存
var objSku_buf = [];
var isHaveSku_buf = false;

// flush
function product_sku_refush(eventTriggerFlag) {
	isHaveProduct_buf = false;
	isHaveSku_buf = false;
	var value = getProductSkuBtnActiveId();
	if (value == 'product_radioset') {
		doGetProductDataReq(eventTriggerFlag);
	} else if (value == 'sku_radioset') {
		doGetSkuDataReq(eventTriggerFlag);
	}
}

//请求 
function doGetProductDataReq(eventTriggerFlag) {
	// show loading
	$("#gbox_sales_product_grid_table .loading").css("display", "block");
	// 载入开始
	bigdata.post(rootPath + "/manage/getSalesDetailProductData.html", sales_search_cond, doGetProductDataReq_end, '', eventTriggerFlag);
}

//请求  结束
function doGetProductDataReq_end(json, eventTriggerFlag) {
	// 请求后数据缓存
	//		图表显示内容（数组）
	objProduct_buf = json;
	isHaveProduct_buf = true;
	// 刷新表格
	refresh_product_table_data();
}

//请求 
function doGetSkuDataReq(eventTriggerFlag) {
	// show loading
	$("#gbox_sales_product_grid_table .loading").css("display", "block");
	// 载入开始
	bigdata.post(rootPath + "/manage/getSalesDetailSkuData.html", sales_search_cond, doGetSkuDataReq_end, '', eventTriggerFlag);
}

//请求  结束
function doGetSkuDataReq_end(json, eventTriggerFlag) {
	// 请求后数据缓存
	//		图表显示内容（数组）
	objSku_buf = json;
	isHaveSku_buf = true;
	// 刷新表格
	refresh_sku_table_data();
}

//表格数据Init
function init_product_table() {
	sales_search_cond.sort_col = "qty_n";
	
	// 表格控件
	var grid_selector = "#sales_product_grid_table";
	// 表格翻页控件
	var pager_selector = "#sales_product_grid_pager";
	
	$(grid_selector).jqGrid({
		datatype: "local",
		height: 238,
		colNames:[		
	          		'ID',
	          		'name',
	          		'value',
	          		'yoy%',
	          		'mom%',
	          		'value',
	          		'yoy%',
	          		'mom%',
	          		'value',
	          		'yoy%',
	          		'mom%',
	          		'value',
	          		'yoy%',
	          		'mom%'
	          		/*'value',
	          		'yoy%',
	          		'mom%'*/
	          		],
		colModel:[	
		    {name:'id',index:'id', width:150, editable: false,sortable:false,frozen : true, align:"left"},
			{name:'title',index:'title', width:150, editable: false,sortable:false,frozen : true, align:"left"},
			{name:'qty_n',index:'qty_n',width:75, sorttype:"int", editable:false,align:"right"},
			{name:'qty_n_y_r_u',index:'qty_n_y_r_u',width:75, sorttype:"int", align:"right"},
			{name:'qty_n_l_r_u',index:'qty_n_l_r_u',width:75, sorttype:"int", align:"right"},
			{name:'amt_n',index:'amt_n',width:75, sorttype:"int", editable:false,align:"right"},
			{name:'amt_n_y_r_u',index:'amt_n_y_r_u',width:75, sorttype:"int", align:"right"},
			{name:'amt_n_l_r_u',index:'amt_n_l_r_u',width:75, sorttype:"int", align:"right"},
			{name:'order_n',index:'order_n',width:75, sorttype:"int", editable:false,align:"right"},
			{name:'order_n_y_r_u',index:'order_n_y_r_u',width:75, sorttype:"int",  align:"right"},
			{name:'order_n_l_r_u',index:'order_n_l_r_u',width:75, sorttype:"int",  align:"right"},
			{name:'atv_n',index:'atv_n',width:75, sorttype:"int", editable:false,align:"right"},
			{name:'atv_n_y_r_u',index:'atv_n_y_r_u',width:75, sorttype:"int", align:"right"},
			{name:'atv_n_l_r_u',index:'atv_n_l_r_u',width:75, sorttype:"int", align:"right"}
			/*{name:'uv_n',index:'uv_n',width:75, sorttype:"int", align:"right"},
			{name:'uv_n_y_r_u',index:'uv_n_y_r_u',width:75, sorttype:"int", align:"right"},
			{name:'uv_n_l_r_u',index:'uv_n_l_r_u',width:75, sorttype:"int", align:"right"}*/
		],
		viewrecords : true,
		rowNum:10,
		rowList:[],
		pager : pager_selector,
		pginput:false,
		autowidth:true,
		shrinkToFit:false,
		altRows: false
	});

	$(grid_selector).jqGrid('navGrid', pager_selector, {
		add: false, edit: false, del: false, search: false, refresh: false
	}).jqGrid('navButtonAdd',
			pager_selector,
			{ caption: "Export", title: "Export to Excel", buttonicon: "ui-icon-bookmark", onClickButton: genProductExportExcel, position: "last"}
	);

	
	$(grid_selector).jqGrid('setGroupHeaders', {
	  useColSpanStyle: true, 
	  groupHeaders:[
		{startColumnName: 'qty_n', numberOfColumns: 3, titleText: 'QTY'},
		{startColumnName: 'amt_n', numberOfColumns: 3, titleText: 'AMT'},
		{startColumnName: 'order_n', numberOfColumns: 3, titleText: 'Order'},
		{startColumnName: 'atv_n', numberOfColumns: 3, titleText: 'ATV'}
		/*{startColumnName: 'uv_n', numberOfColumns: 3, titleText: 'UV'}*/
	  ]	
	});
	$(grid_selector).jqGrid("setFrozenColumns");
	$(grid_selector).bind("jqGridSortCol", 
		function (e, index, iCol, sord) { 
			sales_search_cond.sort_col = index;
			sales_search_cond.sord = sord;
			var value = getProductSkuBtnActiveId();
			if (value == 'product_radioset') {
				isHaveProduct_buf = false;
				doGetProductDataReq("0");
			} else if (value == 'sku_radioset') {
				isHaveSku_buf = false;
				doGetSkuDataReq("0")
			}
			return 'stop';
		}
	);
}

function genProductExportExcel() {
	var url_param = $.param(sales_search_cond);
	
	var download_url =rootPath + "/manage/getSalesDetailProductDataExcel.html?"+url_param;
	var value = getProductSkuBtnActiveId();
	if (value == 'sku_radioset') {
		var download_url =rootPath + "/manage/getSalesDetailSkuDataExcel.html?"+url_param;
	}
	
	$.fileDownload(download_url, {
		failMessageHtml: "There was a problem generating your report, please try again."
	});
}

//product表格数据刷新
function refresh_product_table_data() {
	$('#product_sku_title').empty().append("Top 100 Product List");
	disBeans = objProduct_buf.productDisBean;
	refresh_product_sku_table_data(disBeans);
}

//sku表格数据刷新
function refresh_sku_table_data() {
	$('#product_sku_title').empty().append("Top 100 Sku List");
	disBeans = objSku_buf.productDisBean;
	refresh_product_sku_table_data(disBeans);
}

//表格数据刷新
function refresh_product_sku_table_data(disBeans) {
	grid_data = [];
	if (disBeans) {
		for (var i=0; i<disBeans.length; i++) {
			row = disBeans[i];
			grid_data.push(
			{
				'id':row.value, 
				'title':row.title, 
				'qty_n':formatNumber(row.qty_kpi.value,0),
				'qty_n_y_r_u':formatNumber(row.qty_kpi.value_y_rate_up*100,0),
				'qty_n_l_r_u':formatNumber(row.qty_kpi.value_r_rate_up*100,0),
				'amt_n':formatNumber(row.amt_kpi.value,0),
				'amt_n_y_r_u':formatNumber(row.amt_kpi.value_y_rate_up*100,0),
				'amt_n_l_r_u':formatNumber(row.amt_kpi.value_r_rate_up*100,0),
				'order_n':formatNumber(row.order_kpi.value,0),
				'order_n_y_r_u':formatNumber(row.order_kpi.value_y_rate_up*100,0),
				'order_n_l_r_u':formatNumber(row.order_kpi.value_r_rate_up*100,0),
				'atv_n':formatNumber(row.atv_kpi.value,0),
				'atv_n_y_r_u':formatNumber(row.atv_kpi.value_y_rate_up*100,0),
				'atv_n_l_r_u':formatNumber(row.atv_kpi.value_r_rate_up*100,0),
				'uv_n':formatNumber(row.uv_kpi.value,0),
				'uv_n_y_r_u':formatNumber(row.uv_kpi.value_y_rate_up*100,0),
				'uv_n_l_r_u':formatNumber(row.uv_kpi.value_r_rate_up*100,0)
			});
		}
	}
	
	// 表格控件
	var grid_selector = "#sales_product_grid_table";
	// 表格数据清空
	$(grid_selector).clearGridData();
	$(grid_selector).setGridParam({data: grid_data}).trigger("reloadGrid");
	$("#gbox_sales_product_grid_table .loading").css("display", "none");
}
