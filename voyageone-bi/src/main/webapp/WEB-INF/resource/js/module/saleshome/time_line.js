// 销售用js[时间]
// 图表实例化------------------
// srcipt标签式引入 
var time_line_chart = echarts.init(document.getElementById('time_line'), theme);
// 图表显示用
var time_line_option = {
		tooltip : {
	        trigger: 'item',
	        formatter : function (params) {
	        	kpi_show_name = getButtonActiveId() + "_kpi";
	        	var bean = getListTimeLineBeanValue(time_line_data_buf, params.name, kpi_show_name);
                return params.name + ':<br/>&nbsp;&nbsp;'  +  formatNumber(bean.value,0) +  '<br/>&nbsp;&nbsp;'  +  formatNumber(bean.value_y_rate_up*100, 1) + "%";
	        },
	        textStyle:{ fontSize : 9 }
	    },
	    calculable : true,
        legend: {
            data:['QTY','Yoy_QTY']
        },
        xAxis : [
            {
            	axisLabel : { rotate : 0, 
            		textStyle: {
            			fontFamily: 'Arial, Verdana, sans-serif',
            		}
            	},
            	axisLine : { onZero : false},
            	splitLine : { show : false },
                type : 'category',
                data : []
            }
        ],
        yAxis : [
            {
//            	name : 'QTY',
                type : 'value',
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
                },
                boundaryGap: [0, 0.1],
            },
            {
            	name : 'Yoy%',
                type : 'value',
                boundaryGap: [0, 0.1],
                splitLine : { show : false },
                splitArea : { show : false }
            }
        ],
        grid : { x: 65,  y:40,  x2: 65,  y2: 40 },
        series : [
            {
            	name : 'QTY',
                type : "bar",
                data : []
            },
            {
            	name : 'Yoy_QTY',
                type : 'line',
                yAxisIndex : 1,
                data : []
            }
        ]
    };

//X轴线显示用Data
var time_line_xAxis = [];
// Y轴线显示用Data
var time_line_yAxis = [];
// Y轴线显示用Data
var time_line_yAxis_2 = [];

// timeLine销售数据（请求返回缓存）
var time_line_data_buf = [];

//按时间请求 开始
//eventTriggerFlag：时间触发Flag
//					"0"：不触发	"1"：触发
function initTimeline(eventTriggerFlag) {
	//画面Botton初期化
	initTimeLineSumButton();
	doGetSalesHomeTimelineDataReq(eventTriggerFlag);
}

// 按时间请求 开始
//		eventTriggerFlag：时间触发Flag
//							"0"：不触发	"1"：触发
function doGetSalesHomeTimelineDataReq(eventTriggerFlag) {
	// 载入开始
	doLoading(time_line_chart);
	// 月销量请求
    bigdata.post(rootPath + "/manage/getSalesHomeTimelineData.html", sales_search_cond, doGetSalesHomeTimelineDataReq_end, '');
}

//画面Botton初期化
function initTimeLineSumButton(){
	$("#timeline_sum_qty").click(function(){
		clearTimeLineSumButtonActive();
		$("#timeline_sum_qty").addClass('active');
		refreshTimeLineChart("qty");
	});
	$("#timeline_sum_amt").click(function(){
		clearTimeLineSumButtonActive();
		$("#timeline_sum_amt").addClass('active');
		refreshTimeLineChart("amt");
	});
	$("#timeline_sum_uv").click(function(){
		clearTimeLineSumButtonActive();
		$("#timeline_sum_uv").addClass('active');
		refreshTimeLineChart("uv");
	});
	$("#timeline_sum_order").click(function(){
		clearTimeLineSumButtonActive();
		$("#timeline_sum_order").addClass('active');
		refreshTimeLineChart("order");
	});
	$("#timeline_sum_orderprice").click(function(){
		clearTimeLineSumButtonActive();
		$("#timeline_sum_orderprice").addClass('active');
		refreshTimeLineChart("atv");
	});
}

//按时间请求  结束
function doGetSalesHomeTimelineDataReq_end(json) {
	// 请求后数据缓存
	//		图表显示内容（数组）
	time_line_data_buf = json.timeLineDisBean;
	// initTimelineSum
	initTimelineSum(json);
	// 载入结束
	endLoading(time_line_chart);
	
	// 图表刷新
	kpi_show = getButtonActiveId();
    refreshTimeLineChart(kpi_show);
}


//按时间请求  结束
function initTimelineSum(json) {
	// 正确返回的场合
	if (json.reqResult == "1") {
		salesSumDisBeans_buf = json.timeLineSumDisBean;
		l30_sumBean = salesSumDisBeans_buf[0];
		ytd_sumBean = salesSumDisBeans_buf[1];
		$('#time_line_sum_qty_mtd').text(formatNumber(l30_sumBean.qty_kpi.value, 0));
		$('#time_line_sum_qty_ytd').text(formatNumber(ytd_sumBean.qty_kpi.value, 0));
		$('#time_line_sum_amt_mtd').text(formatNumber(l30_sumBean.amt_kpi.value, 0));
		$('#time_line_sum_amt_ytd').text(formatNumber(ytd_sumBean.amt_kpi.value, 0));
		$('#time_line_sum_uv_mtd').text(formatNumber(l30_sumBean.uv_kpi.value, 0));
		$('#time_line_sum_uv_ytd').text(formatNumber(ytd_sumBean.uv_kpi.value, 0));
		$('#time_line_sum_order_mtd').text(formatNumber(l30_sumBean.order_kpi.value, 0));
		$('#time_line_sum_order_ytd').text(formatNumber(ytd_sumBean.order_kpi.value, 0));
		$('#time_line_sum_orderprice_mtd').text(formatNumber(l30_sumBean.atv_kpi.value, 0));
		$('#time_line_sum_orderprice_ytd').text(formatNumber(ytd_sumBean.atv_kpi.value, 0));
	}
}


// 图表刷新
function refreshTimeLineChart(kpi_show){
	// 上次显示数据清空
	time_line_chart.clear();
	
	// 显示用数据清空
	time_line_xAxis = [];
	time_line_yAxis = [];
	time_line_yAxis_2 = [];
	
	// 显示用数据设置
	for (var i = 0; i < time_line_data_buf.length; i++){
		// echart X轴设定
		time_line_xAxis.push(dateDBToDisplay(time_line_data_buf[i].title));
		
		kpibean_show_name = kpi_show+"_kpi";
		// echart Y轴设定
		time_line_yAxis.push(roundNumber(time_line_data_buf[i][kpibean_show_name].value, 0));
		
		// 销售量
		time_line_yAxis_2.push(roundNumber(time_line_data_buf[i][kpibean_show_name]["value_y_rate_up"]*100, 0));
	}
	// 图形控件设值
	time_line_option.xAxis[0].data = arrIsEmptyAddOneDefault(time_line_xAxis);
	time_line_option.series[0].data = arrIsEmptyAddOneDefault(time_line_yAxis);
	time_line_option.series[1].data = arrIsEmptyAddOneDefault(time_line_yAxis_2);

	// 图例变更
	if (kpi_show == 'qty') {
		time_line_option.legend.data[0] = "QTY";	
		time_line_option.legend.data[1] = "Yoy_QTY";
		time_line_option.series[0].name = "QTY";	
		time_line_option.series[1].name = "Yoy_QTY";
	} else if (kpi_show == 'amt') {
		time_line_option.legend.data[0] = "AMT";	
		time_line_option.legend.data[1] = "Yoy_AMT";
		time_line_option.series[0].name = "AMT";	
		time_line_option.series[1].name = "Yoy_AMT";
	} else if (kpi_show == 'order') {
		time_line_option.legend.data[0] = "Order";
		time_line_option.legend.data[1] = "Yoy_Order";
		time_line_option.series[0].name = "Order";	
		time_line_option.series[1].name = "Yoy_Order";
	} else if (kpi_show == 'atv') {
		time_line_option.legend.data[0] = "ATV";	
		time_line_option.legend.data[1] = "Yoy_ATV";
		time_line_option.series[0].name = "ATV";	
		time_line_option.series[1].name = "Yoy_ATV";
	} else if (kpi_show == 'uv') {
		time_line_option.legend.data[0] = "UV";	
		time_line_option.legend.data[1] = "Yoy_UV";
		time_line_option.series[0].name = "UV";	
		time_line_option.series[1].name = "Yoy_UV";
	} else {
		time_line_option.legend.data[0] = "QTY";	
		time_line_option.legend.data[1] = "Yoy_QTY";
		time_line_option.series[0].name = "QTY";	
		time_line_option.series[1].name = "Yoy_QTY";
	}
	
	time_line_chart.setOption(time_line_option);
}

//SUM按钮
function clearTimeLineSumButtonActive() {
	$("a[role='timeline_sum']").each(function () {
		  $(this).removeClass('active');
	});
}

function getButtonActiveId(){
	result = 'qty';
	$("a[role='timeline_sum'].active").each(function () {
		id = $(this).attr("id");
		if (id == "timeline_sum_qty") {
			result = 'qty'
		} else if (id =="timeline_sum_amt") {
			result = 'amt'
		} else if (id =="timeline_sum_uv") {
			result = 'uv'
		} else if (id =="timeline_sum_order") {
			result = 'order'
		} else if (id =="timeline_sum_orderprice") {
			result = 'atv'
		}
		return result
	});
	return result
}

function getListTimeLineBeanValue(lstBean, keyName, columnName){
	for (var i = 0; i < lstBean.length; i++){
		bean = lstBean[i];
		if (bean["title"]==keyName) {
			return bean[columnName];
		}
	}
	return "";
}
