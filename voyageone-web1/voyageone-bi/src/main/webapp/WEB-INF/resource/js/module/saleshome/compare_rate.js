// 销售用js[时间]
// 图表实例化------------------
// srcipt标签式引入 
var compare_radius = ['40%', '60%'];
var textStyle = {fontSize : 15, fontFamily:'sans-serif'};
var itemStyle = {
    normal : {
        label : {
            show : true,
//	        position : 'inner',
            formatter : "{d}%"
        },
        labelLine : {
            show : true,
            length : 0
        }
    }
};

var compare_qty_chart = echarts.init(document.getElementById('compare_qty'), theme);
var compare_amt_chart = echarts.init(document.getElementById('compare_amt'), theme);
var compare_order_chart = echarts.init(document.getElementById('compare_order'), theme);
var compare_uv_chart = echarts.init(document.getElementById('compare_uv'), theme);

var compare_option = {
	title : {
        text: '',
        x: 'center',
        textStyle:textStyle
    },
    tooltip : {
        trigger: 'item',
        formatter: "{b} :<br/>{c}",
        textStyle:{fontSize : 9}
    },
    legend: {
    	show : false,
        x : 'center',
        y : 'bottom',
        data:[]
    },
    toolbox: {
        show : false
    },
    series : [
        {
            type:'pie',
            radius : compare_radius,
            itemStyle : itemStyle,
            data:[]
        }
    ]
};

function compare_rate_init(eventTriggerFlag) {
	doGetSalesHomeCompareData(eventTriggerFlag);
}

function doGetSalesHomeCompareData(eventTriggerFlag) {
	compare_rate_doLoading();
	bigdata.post(rootPath + "/manage/getSalesHomeCompareData.html", sales_search_cond, doGetSalesHomeCompareDataReq_end, '');
}

// compare_rate_doLoading
function compare_rate_doLoading() {
	doLoading(compare_qty_chart);
	doLoading(compare_amt_chart);
	doLoading(compare_order_chart);
	doLoading(compare_uv_chart);

}

//请求  结束
function doGetSalesHomeCompareDataReq_end(json) {
	// 请求后数据缓存
	//		图表显示内容（数组）
	objDisBeans_buf = json;
	// 载入结束
	compare_rate_endLoading();
	// 图表刷新
	compare_refreshChart(json);
}

function compare_rate_endLoading() {
	endLoading(compare_qty_chart);
	endLoading(compare_amt_chart);
	endLoading(compare_order_chart);
	endLoading(compare_uv_chart);
}

//图表刷新
function compare_refreshChart(json){
	// 上次显示数据清空
	compare_qty_chart.clear();
	compare_amt_chart.clear();
	compare_order_chart.clear();
	compare_uv_chart.clear();
	
	compare_datas = json.compareBean;
	// qty
	chart_data1 = getCompareChartListValue(compare_datas, "sTitle", "qty_kpi");
	var compare_qty_option = jQuery.extend(true, {}, compare_option)
	compare_qty_option.title.text = "QTY[Stores]";
	compare_qty_option.series[0].data = chart_data1;
	compare_qty_chart.setOption(compare_qty_option);
	// amt
	chart_data1 = getCompareChartListValue(compare_datas, "sTitle", "amt_kpi");
	var compare_amt_option = jQuery.extend(true, {}, compare_option)
	compare_amt_option.title.text = "AMT[Stores]";
	compare_amt_option.series[0].data = chart_data1;
	compare_amt_chart.setOption(compare_amt_option);
	// order
	chart_data1 = getCompareChartListValue(compare_datas, "sTitle", "order_kpi");
	var compare_order_option = jQuery.extend(true, {}, compare_option)
	compare_order_option.title.text = "Order[Stores]";
	compare_order_option.series[0].data = chart_data1;
	compare_order_chart.setOption(compare_order_option);
	// UV
	chart_data1 = getCompareChartListValue(compare_datas, "sTitle", "uv_kpi");
	var compare_uv_option = jQuery.extend(true, {}, compare_option)
	compare_uv_option.title.text = "UV[Stores]";
	compare_uv_option.series[0].data = chart_data1;
	compare_uv_chart.setOption(compare_uv_option);
}

function getCompareChartListValue(compare_datas, titleName, kpiName){
	chart_data = [];
	for (var i=0;  i<compare_datas.length; i++){
		kpi_compare_data = compare_datas[i][kpiName];
		dataCell = {name:compare_datas[i][titleName], value:roundNumber(kpi_compare_data["value"], 0)};
		chart_data.push(dataCell);
	}
	chart_data = arrIsEmptyAddOneDefault(chart_data);
	return chart_data;
}
