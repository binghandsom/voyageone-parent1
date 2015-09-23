// 销售用js[时间]
// 图表实例化------------------
// srcipt标签式引入 
var column_bar_grid={ y: 40, x2:40, y2:30 };
var  textStyle = { fontSize : 9 };
brand_bar_option = {
    title : {
        text: 'Top 10 Brand',
        x: 'center',
        textStyle: {
            fontSize: 14,
            fontFamily: 'Arial, Verdana, sans-serif',
            fontWeight: 'normal'
        }
    },
    tooltip : {
        trigger: 'axis',
        textStyle:textStyle
    },
    legend: {
        x : 'center',
        y : 'top',
        data:[
            ''
        ]
    },
    toolbox: {
        show : false
    },
    grid : column_bar_grid,
    calculable : true,
    xAxis : [
        {
        	name:'',
            type : 'value',
            boundaryGap : [0, 0.01],
            splitLine: {           // 分隔线
                show: true,        // 默认显示，属性show控制显示与否
                lineStyle: {       // 属性lineStyle（详见lineStyle）控制线条样式
                    color: ['rgba(200,200,200,0.5)'],
                    width: 1,
                    type: 'solid'
                }
            },
            splitArea: {           // 分隔区域
                show: true,       // 默认不显示，属性show控制显示与否
                areaStyle: {       // 属性areaStyle（详见areaStyle）控制区域样式
                    color: ['rgba(250,250,250,0.3)','rgba(200,200,200,0.3)']
                }
            }
        }
    ],
    yAxis : [
        {
            type : 'category',
            data : []
        }
    ],
    series : [
        {
            type:'bar',
            data:[]
        }
    ]
};

//brand_bar
var brand_bar_chart = echarts.init(document.getElementById('brand_bar_chart'), theme);

brand_pop_option = {
	    title : {
	        text: 'QTY&AMT&ORDER',
	        x: 'center',
	        textStyle: {
	            fontSize: 14,
	            fontFamily: 'Arial, Verdana, sans-serif',
	            fontWeight: 'normal'
	        }
	    },
	    tooltip : {
	    	trigger: 'axis',
	        showDelay : 0,
	        formatter : function (params) {
                return params.value[3]+  '<br/>'
                + 'AMT:'  + formatNumber(params.value[0],0) + '<br/>'
                + 'QTY:'  + formatNumber(params.value[1],0) + '<br/> ' 
                + 'Order:'  + formatNumber(params.value[2],0);
	        },
	        textStyle:textStyle,
	        axisPointer:{
	            show: true,
	            type : 'cross',
	            lineStyle: {
	                type : 'dashed',
	                width : 1
	            }
	        }
	    },
	    legend: {
	        data:['']
	    },
	    toolbox: {
	        show : false
	    },
	    xAxis : [
	        {
	        	name:'AMT',
	        	type : 'value',
	            scale:true,
	            axisLabel : {
	                formatter: '{value}'
	            }
	        }
	    ],
	    yAxis : [
	        {
	        	name:'QTY',
	            type : 'value',
	            scale:true,
	            axisLabel : {
	                formatter: '{value}'
	            }
	        }
	    ],
	    grid : column_bar_grid,
	    series : [
	        {
	            name:'Order',
	            type:'scatter',
	            symbolSize: function (value){
	                 current = Math.round(value[2]/4);
	                 if (current < 4) {
	                	 current = 4;
	                 } else if (current > 20) {
	                	 current = 20;
	                 }
	                 return current;
	            },
	            data: []
	        }
	    ]
	};


var brand_pop_chart = echarts.init(document.getElementById('brand_pop_chart'), theme);


category_bar_option = {
	    title : {
	        text: 'Top 10 Category',
	        x: 'center',
	        textStyle: {
	            fontSize: 14,
	            fontFamily: 'Arial, Verdana, sans-serif',
	            fontWeight: 'normal'
	        }
	    },
	    tooltip : {
	        trigger: 'axis',
	        textStyle:textStyle
	    },
	    legend: {
	        x : 'center',
	        y : 'top',
	        data:[
	            ''
	        ]
	    },
	    toolbox: {
	        show : false
	    },
	    grid : column_bar_grid,
	    calculable : true,
	    xAxis : [
	        {
	        	name:'',
	            type : 'value',
	            boundaryGap : [0, 0.01],
	            splitLine: {           // 分隔线
	                show: true,        // 默认显示，属性show控制显示与否
	                lineStyle: {       // 属性lineStyle（详见lineStyle）控制线条样式
	                    color: ['rgba(200,200,200,0.5)'],
	                    width: 1,
	                    type: 'solid'
	                }
	            },
	            splitArea: {           // 分隔区域
	                show: true,       // 默认不显示，属性show控制显示与否
	                areaStyle: {       // 属性areaStyle（详见areaStyle）控制区域样式
	                    color: ['rgba(250,250,250,0.3)','rgba(200,200,200,0.3)']
	                }
	            }
	        }
	    ],
	    yAxis : [
	        {
	        	name:'Category',
	            type : 'category',
	            data : []
	        }
	    ],
	    series : [
	        {
	            name:'QTY',
	            type:'bar',
	            data:[]
	        }
	    ]
	};

//QTY LW% 
var category_bar_chart = echarts.init(document.getElementById('category_bar_chart'), theme);

category_pop_option = {
	    title : {
	        text: 'QTY&AMT&Order',
	        x: 'center',
	        textStyle: {
	            fontSize: 14,
	            fontFamily: 'Arial, Verdana, sans-serif',
	            fontWeight: 'normal'
	        }
	    },
	    tooltip : {
	    	trigger: 'axis',
	        showDelay : 0,
	        formatter : function (params) {
	        	return params.value[3]+  '<br/>'
                + 'AMT:'  + formatNumber(params.value[0],0) + '<br/>'
                + 'QTY:'  + formatNumber(params.value[1],0) + '<br/> ' 
                + 'Order:'  + formatNumber(params.value[2],0);
	        },
	        textStyle:textStyle,
	        axisPointer:{
	            show: true,
	            type : 'cross',
	            lineStyle: {
	                type : 'dashed',
	                width : 1
	            }
	        }
	    },
	    legend: {
	        data:['']
	    },
	    toolbox: {
	        show : false
	    },
	    xAxis : [
	        {
	        	name:'QTY',
	        	type : 'value',
	            scale:true,
	            axisLabel : {
	                formatter: '{value}'
	            }
	        }
	    ],
	    yAxis : [
	        {
	        	name:'AMT',
	            type : 'value',
	            scale:true,
	            axisLabel : {
	                formatter: '{value}'
	            }
	        }
	    ],
	    grid : column_bar_grid,
	    series : [
	        {
	            name:'Order',
	            type:'scatter',
	            symbolSize: function (value){
	                 current = Math.round(0.5+value[2]/20);
	                 if (current < 4) {
	                	 current = 4;
	                 } else if (current > 10) {
	                	 current = 20;
	                 }
	                 return current;
	            },
	            data: []
	        }
	    ]
	};


var category_pop_chart = echarts.init(document.getElementById('category_pop_chart'), theme);

function brand_category_init(eventTriggerFlag) {
	$('#brand_category_time_radioset').buttonset();
	
	$("#brand_category_time_radioset :radio").click(function(){
		brand_refreshChart();
		category_refreshChart();
		refresh_product_table();
	});
	
	$('#brand_kpi_radioset').buttonset();
	
	$("#brand_kpi_radioset :radio").click(function(){
		var value = $(this).attr("id");
		var kpi_show = 'qty';
		if (value == 'brand_qty') {
			kpi_show =  'qty';
		} else if (value == 'brand_amt') {
			kpi_show =  'amt';
		} else if (value == 'brand_order') {
			kpi_show =  'order';
		}
		brand_sort_col = kpi_show+ "_n";
		post_brand("0");
	});
	
	$('#category_kpi_radioset').buttonset();
	$("#category_kpi_radioset :radio").click(function(){
		var value = $(this).attr("id");
		var kpi_show = 'qty';
		if (value == 'category_qty') {
			kpi_show =  'qty';
		} else if (value == 'category_amt') {
			kpi_show =  'amt';
		} else if (value == 'category_order') {
			kpi_show =  'order';
		}
		category_sort_col = kpi_show+ "_n";
		post_category("0");
	});
	//init 产品
	init_product_table();
	
	//请求 
	doGetSalesHomeBrandCategoryDataReq(eventTriggerFlag);
}

//请求返回缓存
var objBrand_buf = [];
var brand_sort_col = 'qty_n';
var objCategory_buf = [];
var category_sort_col = 'qty_n';
var objProduct_buf = [];
var product_sort_col = 'qty_n';
var product_sord = 'DESC';

//请求 
function doGetSalesHomeBrandCategoryDataReq(eventTriggerFlag) {
	post_brand(eventTriggerFlag);
	post_category(eventTriggerFlag);
	objProduct_buf = [];
	post_product(eventTriggerFlag);
}

function post_brand(eventTriggerFlag) {
	brand_doLoading();
	sales_search_cond.sort_col = brand_sort_col;
	bigdata.post(rootPath + "/manage/getSalesHomeBrandData.html", sales_search_cond, doGetSalesHomeBrandDataReq_end, '');
}
function post_category(eventTriggerFlag) {
	category_doLoading();
	sales_search_cond.sort_col = category_sort_col;
	bigdata.post(rootPath + "/manage/getSalesHomeCategoryData.html", sales_search_cond, doGetSalesHomeCategoryDataReq_end, '');
}
function post_product(eventTriggerFlag) {
	$("#gbox_product_table .loading").css("display", "block");
	sales_search_cond.sort_col = product_sort_col;
	sales_search_cond.sord = product_sord;
	sales_search_cond.productTimeType = getBrandCategoryTimeActiveId();
	bigdata.post(rootPath + "/manage/getSalesHomeProductData.html", sales_search_cond, doGetSalesHomeProductDataReq_end, '');
}

//brand_doLoading
function brand_doLoading() {
	doLoading(brand_bar_chart);
	doLoading(brand_pop_chart);
}

//category_doLoading
function category_doLoading() {
	doLoading(category_bar_chart);
	doLoading(category_pop_chart);
}

//品牌请求  结束
function doGetSalesHomeBrandDataReq_end(json) {
	// 请求后数据缓存
	objBrand_buf = json.brandDisBean;
	// 载入结束
	brand_endLoading();
	// 图表刷新
	brand_refreshChart();
}

//分类请求  结束
function doGetSalesHomeCategoryDataReq_end(json) {
	// 请求后数据缓存
	objCategory_buf = json.categoryDisBean;
	// 载入结束
	category_endLoading();
	// 图表刷新
	category_refreshChart();
}
//产品请求  结束
function doGetSalesHomeProductDataReq_end(json) {
	// 请求后数据缓存
	for (var i =0; i<json.productDisBean.length; i++){
		if (!objProduct_buf[i]) {
			objProduct_buf[i] = json.productDisBean[i];
		}
	}
	//objProduct_buf = json.productDisBean;
	//产品
	refresh_product_table();
	// end loading
	$("#gbox_product_table .loading").css("display", "none");
}

//brand_endLoading
function brand_endLoading() {
	endLoading(brand_bar_chart);
	endLoading(brand_pop_chart);
}

//category_endLoading
function category_endLoading() {
	endLoading(category_bar_chart);
	endLoading(category_pop_chart);
}

//品牌图表刷新
function brand_refreshChart(){
	// 品牌
	time_type_show = getBrandCategoryTimeActiveId();
	brand_kpi_show = getBrandCategoryButtonActiveId('brand');
	brand_bar_refreshChart(time_type_show, brand_kpi_show);
	brand_pop_refreshChart(time_type_show);
}

//分类图表刷新
function category_refreshChart(){
	// 分类
	time_type_show = getBrandCategoryTimeActiveId();
	category_kpi_show = getBrandCategoryButtonActiveId('category');
	category_bar_refreshChart(time_type_show, category_kpi_show);
	category_pop_refreshChart(time_type_show);
}

//图表刷新
function brand_bar_refreshChart(time_type_show, kpi_show){
	// 上次显示数据清空
	brand_bar_chart.clear();
	// 显示用数据清空
	brand_yAxis = [];
	brand_series = [];
	brandDisBean = objBrand_buf[time_type_show];
	// 显示用数据设置
	for (var i = brandDisBean.length-1; i>=0; i--){
		// echart X轴设定
		brand_yAxis.push(brandDisBean[i].title);
		kpi_show_bean = kpi_show + "_kpi"
		// echart Y轴设定
		brand_series.push(brandDisBean[i][kpi_show_bean].value);
	}
	// 图例变更
	// 销售量
	if (kpi_show == 'qty') {
		brand_bar_option.xAxis[0].name = "QTY";
		brand_bar_option.series[0].name = "QTY";
	} else if (kpi_show == 'amt') {
		brand_bar_option.xAxis[0].name = "AMT";
		brand_bar_option.series[0].name = "AMT";
	} else if (kpi_show == 'order') {
		brand_bar_option.xAxis[0].name = "Order";
		brand_bar_option.series[0].name = "Order";
	} else {
		brand_bar_option.xAxis[0].name = "QTY";
		brand_bar_option.series[0].name = "QTY";
	}
	// 图形控件设值
	brand_bar_option.yAxis[0].data = arrIsEmptyAddOneDefault(brand_yAxis);
	brand_bar_option.series[0].data = arrIsEmptyAddOneDefault(brand_series);
	
	brand_bar_chart.setOption(brand_bar_option);
}

//图表刷新
function brand_pop_refreshChart(time_type_show){
	// 上次显示数据清空
	brand_pop_chart.clear();
	brandDisBean = objBrand_buf[time_type_show];
	brand_pop_series_data = [];
	for (var i = brandDisBean.length-1; i>=0; i--) {
		// echart Y轴设定
		brand_pop_series_data.push([brandDisBean[i]['amt_kpi'].value, brandDisBean[i]['qty_kpi'].value, brandDisBean[i]['order_kpi'].value,brandDisBean[i].title]);
	}
	brand_pop_option.series[0].data = arrIsEmptyAddOneDefault(brand_pop_series_data);
	brand_pop_chart.setOption(brand_pop_option);
}

//图表刷新
function category_bar_refreshChart(time_type_show, kpi_show){
	// 上次显示数据清空
	category_bar_chart.clear();
	// 显示用数据清空
	category_yAxis = [];
	category_series = [];
	categoryDisBean = objCategory_buf[time_type_show];
	// 显示用数据设置
	for (var i = categoryDisBean.length-1; i>=0; i--){
		// echart X轴设定
		category_yAxis.push(categoryDisBean[i].title);
		// echart Y轴设定
		kpi_show_bean = kpi_show + "_kpi"
		category_series.push(categoryDisBean[i][kpi_show_bean].value);
	}
	// 图例变更
	// 销售量
	if (kpi_show == 'qty') {
		category_bar_option.xAxis[0].name = "QTY";
		category_bar_option.series[0].name = "QTY";
	} else if (kpi_show == 'amt') {
		category_bar_option.xAxis[0].name = "AMT";
		category_bar_option.series[0].name = "AMT";
	} else if (kpi_show == 'order') {
		category_bar_option.xAxis[0].name = "Order";
		category_bar_option.series[0].name = "Order";
	} else {
		category_bar_option.xAxis[0].name = "QTY";
		category_bar_option.series[0].name = "QTY";
	}
	// 图形控件设值
	category_bar_option.yAxis[0].data = arrIsEmptyAddOneDefault(category_yAxis);
	category_bar_option.series[0].data = arrIsEmptyAddOneDefault(category_series);
	
	category_bar_chart.setOption(category_bar_option);
}

//图表刷新
function category_pop_refreshChart(time_type_show){
	// 上次显示数据清空
	category_pop_chart.clear();
	categoryDisBean = objCategory_buf[time_type_show];
	category_pop_series_data = [];
	for (var i = categoryDisBean.length-1; i>=0; i--) {
		category_pop_series_data.push([categoryDisBean[i]['amt_kpi'].value, categoryDisBean[i]['qty_kpi'].value, categoryDisBean[i]['order_kpi'].value,categoryDisBean[i].title]);
	}
	category_pop_option.series[0].data = arrIsEmptyAddOneDefault(category_pop_series_data);
	category_pop_chart.setOption(category_pop_option);
}

function getBrandCategoryButtonActiveId(preStr){
	result = 'qty';
	$("input[name='" + preStr+ "_radio']:checked").each(function () {
		id = $(this).attr("id");
		if (id == preStr + "_qty") {
			result = 'qty'
		} else if (id == preStr + "_amt") {
			result = 'amt'
		} else if (id == preStr + "_order") {
			result = 'order'
		}
		return result
	});
	return result
}

function getBrandCategoryTimeActiveId(){
	result = 0;
	$("input[name='brand_category_time_radioset']:checked").each(function () {
		id = $(this).attr("id");
		if (id == "brand_category_time_1") {
			result = 0
		} else if (id == "brand_category_time_2") {
			result = 1
		} else if (id == "brand_category_time_3") {
			result = 2
		}
		return result
	});
	return result
}

var product_table_data = [];

//表格数据刷新
function init_product_table() {
	// 表格控件
	var grid_selector = "#product_table";
	// 表格翻页控件
	var pager_selector = "#product_gridPager";
	
	$(grid_selector).jqGrid({
		datatype: "local",
		height: 351,
		colNames:[			
							'ID',
							'value',
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
		    {name:'id',index:'id', width:100, editable: false,sortable:false,frozen : true},
		    {name:'value',index:'value', width:150, editable: false,sortable:false,frozen : true},
			{name:'title',index:'title', width:150, editable: false,sortable:false,frozen : true},
			{name:'qty_n',index:'qty_n',width:75, sorttype:"int", editable:false,align:"right"},
			{name:'qty_n_y_r_u',index:'qty_n_y_r_u',width:75, sorttype:"int", editable:false,align:"right"},
			{name:'qty_n_l_r_u',index:'qty_n_l_r_u',width:75, sorttype:"int", editable:false,align:"right"},
			{name:'amt_n',index:'amt_n',width:75, sorttype:"int", editable:false,align:"right"},
			{name:'amt_n_y_r_u',index:'amt_n_y_r_u',width:75, sorttype:"int", editable:false,align:"right"},
			{name:'amt_n_l_r_u',index:'amt_n_l_r_u',width:75, sorttype:"int", editable:false,align:"right"},
			{name:'order_n',index:'order_n',width:75, sorttype:"int", editable:false,align:"right"},
			{name:'order_n_y_r_u',index:'order_n_y_r_u',width:75, sorttype:"int", editable:false, align:"right"},
			{name:'order_n_l_r_u',index:'order_n_l_r_u',width:75, sorttype:"int", editable:false, align:"right"},
			{name:'atv_n',index:'atv_n',width:75, sorttype:"int", editable:false,align:"right"},
			{name:'atv_n_y_r_u',index:'atv_n_y_r_u',width:75, sorttype:"int", editable:false,align:"right"},
			{name:'atv_n_l_r_u',index:'atv_n_l_r_u',width:75, sorttype:"int", editable:false,align:"right"}
/*			{name:'uv_n',index:'uv_n',width:75, sorttype:"int", editable:false,align:"right"},
			{name:'uv_n_y_r_u',index:'uv_n_y_r_u',width:75, sorttype:"int", editable:false,align:"right"},
			{name:'uv_n_l_r_u',index:'uv_n_l_r_u',width:75, sorttype:"int", editable:false,align:"right"}*/
		],
		viewrecords : true,
		rowNum:10,
		pager : pager_selector,
		width: 980,
		shrinkToFit:false,
		scroll:true,
		treeGrid: true,
		ExpandColumn : 'ID',
		treeGridModel: 'adjacency',
		sortname: 'id',
		treedatatype: "local",
		data: product_table_data
	});
	
	$(grid_selector).jqGrid('setGroupHeaders', {
	  useColSpanStyle: true, 
	  groupHeaders:[
	        		{startColumnName: 'qty_n', numberOfColumns: 3, titleText: 'QTY'},
	        		{startColumnName: 'amt_n', numberOfColumns: 3, titleText: 'AMT'},
	        		{startColumnName: 'order_n', numberOfColumns: 3, titleText: 'Order'},
	        		{startColumnName: 'atv_n', numberOfColumns: 3, titleText: 'ATV'}
//	        		{startColumnName: 'uv_n', numberOfColumns: 3, titleText: 'UV'}
	        	  ]	
	});
	$(grid_selector).jqGrid("setFrozenColumns");
	$(grid_selector).bind("jqGridSortCol", 
			function (e, index, iCol, sord) { 
				product_sort_col = index;
				product_sord = sord;
				objProduct_buf = [];
				post_product("0");
				return 'stop';
			}
		);
}
function refresh_product_table() {
	time_type_show = getBrandCategoryTimeActiveId();
	if (objProduct_buf[time_type_show]) {
		refresh_product_table_data(time_type_show);
	} else {
		post_product("0");
	}
}

//表格数据刷新
function refresh_product_table_data(time_type_show) {
	grid_data = [];
	productDisBean = objProduct_buf[time_type_show];
	for (var i=0; i<productDisBean.length; i++) {
		row = productDisBean[i];
		isLeaf = false;
		level = "0";
		parent = "";
		if (row.type=='sku') {
			isLeaf = true;
			level = "1";
			parent = row.parent;
		}
		grid_data.push(
		{
			'title':row.title, 
			'value':row.value,
			'qty_n':formatNumber(row.qty_kpi.value,0),
			'qty_n_y_r_u':formatNumber(row.qty_kpi.value_y_rate_up*100,1),
			'qty_n_l_r_u':formatNumber(row.qty_kpi.value_r_rate_up*100,1),
			'amt_n':formatNumber(row.amt_kpi.value,2),
			'amt_n_y_r_u':formatNumber(row.amt_kpi.value_y_rate_up*100,1),
			'amt_n_l_r_u':formatNumber(row.amt_kpi.value_r_rate_up*100,1),
			'order_n':formatNumber(row.order_kpi.value,0),
			'order_n_y_r_u':formatNumber(row.order_kpi.value_y_rate_up*100,1),
			'order_n_l_r_u':formatNumber(row.order_kpi.value_r_rate_up*100,1),
			'atv_n':formatNumber(row.atv_kpi.value,2),
			'atv_n_y_r_u':formatNumber(row.atv_kpi.value_y_rate_up*100,1),
			'atv_n_l_r_u':formatNumber(row.atv_kpi.value_r_rate_up*100,1),
//			'uv_n':formatNumber(row.uv_kpi.value,0),
//			'uv_n_y_r_u':formatNumber(row.uv_kpi.value_y_rate_up*100,0),
//			'uv_n_l_r_u':formatNumber(row.uv_kpi.value_r_rate_up*100,0),
			'id':row.id,
			"isLeaf":isLeaf,
			"level":level,
			"parent":parent,
			"expanded":true
		});
	}
	// 表格控件
	var grid_selector = "#product_table";
	// 表格数据清空
	$(grid_selector).clearGridData();
	//$(grid_selector).setGridParam({data: grid_data}).trigger("reloadGrid");
	$(grid_selector)[0].addJSONData({
        total: 1,
        page: 1,
        records: grid_data.length,
        rows: grid_data
    });
}