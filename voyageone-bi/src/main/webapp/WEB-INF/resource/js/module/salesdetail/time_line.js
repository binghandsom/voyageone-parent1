// 销售用js[时间]
// 图表实例化------------------
// srcipt标签式引入 

var sales_time_chart_qty = echarts.init(document.getElementById('sales_time_chart_qty'), theme);
var sales_time_chart_order = echarts.init(document.getElementById('sales_time_chart_order'), theme);
var sales_time_chart_uv = echarts.init(document.getElementById('sales_time_chart_uv'), theme);
// time_line_qty_option
var time_line_qty_option = {
		title : {
			x:'center',
			y:5,
	        text: 'QTY & Yoy%',
	        textStyle: {
	            fontSize: 14,
	            fontFamily: 'Arial, Verdana, sans-serif',
	            fontWeight: 'normal'
	        }
	    },
        tooltip: {
        	trigger: 'axis',
        	formatter : function (params) {
        		//kpi_name = getTimelineKpiSetButtonActiveId().toUpperCase();
        		//line_kpi_name = getTimeline_Line_KpiSetButtonActiveId().toUpperCase();
                return params[0].name + ':<br/>'+timeline_kpi_show.toUpperCase()+':'  +  formatNumber(params[0].value,0) +  '<br/>' + timeline_line_kpi_show.toUpperCase() + ':'  +  formatNumber(params[1].value, 0) + "%";
	        },
        	showDelay: 0
        },
        toolbox: {
        	x:50,
        	y:0,
            show : true,
            feature : {
                dataZoom : {show: true},
            }
        },
        legend: {
        	x:500,
            data:['QTY','Yoy']
        },
        xAxis : [
            {
            	axisLabel : { show: false},
            	axisLine : { onZero : false},
            	axisTick: {onGap:false},
            	splitLine : { show : false },
                type : 'category',
                data : []
            }
        ],
        
        yAxis : [
            {
//            	name : 'QTY',
                type : 'value',
                boundaryGap: [0, 0.01],
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
            },
            {
            	name : '%',
                type : 'value',
                boundaryGap: [0, 0.01],
                splitLine : { show : false },
                splitArea : { show : false }
            }
        ],
        grid : { x: 80,  y:30,  x2: 60,  y2: 5 },
        series : [
            {
                "name" : "QTY",
                "type" : "bar",
                "data" : []
            },
            {
                "name" : "Yoy",
                "type" : "line",
                "yAxisIndex" : 1,
                "data" : []
            }
        ]
    };

//time_line_order_option
var time_line_order_option = {
		title : {
			x:'center',
			y:5,
	        text: 'Order & ATV%',
	        textStyle: {
	            fontSize: 14,
	            fontFamily: 'Arial, Verdana, sans-serif',
	            fontWeight: 'normal'
	        },
	    },
        tooltip: {
        	trigger: 'axis',
        	formatter : function (params) {
                return params[0].name + ':<br/>Order:'  +  formatNumber(params[0].value,0) +  '<br/>ATV:'  +  formatNumber(params[1].value, 0);
	        },
        	showDelay: 0,
        },
        legend: {
        	x:500,
            data:['Order','ATV']
        },
        xAxis : [
            {
            	axisLabel : { show: false},
            	axisTick: {onGap:false},
            	splitLine : { show : false },
            	axisLine : { onZero : false},
                type : 'category',
                data : []
            }
        ],
        yAxis : [
            {
//            	name : 'Order',
                type : 'value',
                boundaryGap: [0, 0.01],
                scale : true,
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
            },
            {
//            	name : 'ATV',
                type : 'value',
                boundaryGap: [0, 0.01],
                splitLine : { show : false },
                splitArea : { show : false }
            }
        ],
        grid : { x: 80,  y:20,  x2: 60,  y2: 5 },
        series : [
            {
                "name" : "Order",
                "type" : "line",
                "data" : []
            },
            {
                "name" : "ATV",
                "type" : "line",
                "yAxisIndex" : 1,
                "data" : []
            }
        ]
    };

//time_line_uv_option
var time_line_uv_option = {
		title : {
			x:'center',
			y:5,
	        text: 'UV & TR%',
	        textStyle: {
	            fontSize: 14,
	            fontFamily: 'Arial, Verdana, sans-serif',
	            fontWeight: 'normal'
	        },
	    },
        tooltip: {
        	trigger: 'axis',
        	formatter : function (params) {
                return params[0].name + ':<br/>UV:'  +  formatNumber(params[0].value,0) +  '<br/>TR:'  +  formatNumber(params[1].value, 2) + "%";
	        },
        	showDelay: 0,
        },
        legend: {
        	x:500,
            data:['UV','TR%']
        },
        xAxis : [
            {
            	axisTick: {onGap:false},
            	splitLine : { show : false },
            	axisLine : { onZero : false},
                type : 'category',
                data : []
            }
        ],
        yAxis : [
            {
//            	name : 'UV',
                type : 'value',
                boundaryGap: [0, 0.01],
                scale : true,
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
            },
            {
            	name : '%',
                type : 'value',
                boundaryGap: [0, 0.01],
                splitLine : { show : false },
                splitArea : { show : false }
            }
        ],
        grid : { x: 80,  y:20,  x2: 60,  y2: 30 },
        series : [
            {
                "name" : "UV",
                "type" : "line",
                "data" : []
            },
            {
                "name" : "TR%",
                "type" : "line",
                "yAxisIndex" : 1,
                "data" : []
            }
        ]
    };


var sales_time_chart_qty_sum = echarts.init(document.getElementById('sales_time_chart_qty_sum'), theme);
var sales_time_chart_pie_pcMobel = echarts.init(document.getElementById('sales_time_chart_pie_pcMobel'), theme);
var sales_time_chart_pie_shops = echarts.init(document.getElementById('sales_time_chart_pie_shops'), theme);

time_line_sum_option = {
		title : {
			x:'center',
			y:5,
	        text: 'QTY YOY SUM',
	        textStyle: {
	            fontSize: 14,
	            fontFamily: 'Arial, Verdana, sans-serif',
	            fontWeight: 'normal'
	        }
	    },
	    tooltip : {
	        trigger: 'axis',
	        formatter : function (params) {
                return 'C-Value:'  +  formatNumber(params[1].value,0) +  '<br/>Y-Value:'  +  formatNumber(params[0].value, 0);
	        },
	        textStyle:{ fontSize : 9 }
	    },
	    legend: {
	    	show: false,
	        data:['QTY']
	    },
	    xAxis : [
	        {
	            type : 'category',
	            data : ['QTY']
	        }
	    ],
	    yAxis : [
	        {
	        	axisLine: { show: false },
	        	axisLabel:{ show: false },
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
                }
	        }
	    ],
	    grid : { x: 60,  y:30,  x2: 70,  y2: 30 },
	    series : [
	        {
	            name:'value',
	            type:'bar',
	            boundaryGap: [0, 0.01],
//	            markPoint : {
//	                data : [
//	                    {value: 100, xAxis: 'QTY'}
//	                ]
//	            },
	            itemStyle: {
	            	normal: {
	            		color:  "#2ec7c9"
	            	}
	            },
	            data:[]
	        },
	        {
	            name:'yoy',
	            type:'bar',
	            boundaryGap: [0, 0.01],
//	            markPoint : {
//	                data : [
//	                    {value: 300, xAxis: 'QTY'},
//	                ]
//	            },
	            itemStyle: {
	            	normal: {
	            		color:  "#5ab1ef"
	            	}
	            },
	            data:[]
	        }
	    ]
	};

time_chart_pie_pcMobel_option = {
	    title : {
        	x:'center',
			y:4,
	        text: 'QTY PC vs Mobile',
	        textStyle: {
	            fontSize: 14,
	            fontFamily: 'Arial, Verdana, sans-serif',
	            fontWeight: 'normal'
	        }
	    },
	    tooltip : {
	        trigger: 'item',
	        formatter: "{b}<br/>{d}%",
	        textStyle:{ fontSize : 9 }
	    },
	    legend: {
	    	show : false,
	        x : 'center',
	        y : 'bottom',
	        data:['PC','Mobile']
	    },
	    series : [
	        {
	            name:'QTY',
	            type:'pie',
	            radius : '70%',
	            itemStyle : {
				    normal : {
				        label : {
				            show : true,
				            position : 'inner',
				            formatter : "{b}\n{d}%"
				        },
				        labelLine : {
				            show : false
				        }
				    }
				},
				center: ['50%', '60%'],
	            data:[]		        
	        }
	    ]
	};

time_chart_pie_shops_option = {
	    title : {
        	x:'center',
			y:5,
	        text: 'QTY Shops',
	        textStyle: {
	            fontSize: 14,
	            fontFamily: 'Arial, Verdana, sans-serif',
	            fontWeight: 'normal'
	        }
	    },
	    tooltip : {
	        trigger: 'item',
	        formatter: "{b}<br/>{c}",
	        textStyle:{ fontSize : 9 }
	    },
	    legend: {
	    	show : false,
	        x : 'center',
	        y : 'bottom',
	        data:[]
	    },
	    series : [
	        {
	            name:'QTY',
	            type:'pie',
	            radius : '70%',
	            itemStyle : {
				    normal : {
				        label : {
				            show : true,
//				            position : 'inner',
				            formatter : "{b}\n{d}%"
				        },
				        labelLine : {
				            show : true,
				            length : 0
				        }
				    }
				},
				center: ['50%', '60%'],
	            data:[]		        
	        }
	    ]
	};

//X轴线显示用Data
var time_line_xAxis = [];
// Y轴线显示用Data
var time_line_yAxis = [];
// Y轴线显示用Data
var time_line_yAxis_2 = [];


//按时间请求 开始
//eventTriggerFlag：时间触发Flag
//					"0"：不触发	"1"：触发
function initTimeline(eventTriggerFlag) {
	//画面Botton初期化
	initTimeLineBtnInit();
	
	timeline_reflush(eventTriggerFlag);
}

function initTimeLineBtnInit() {
	$("#time_window_ctrl").click(function(){
		if ($("#time_body").is(":visible")==false) {
			$("#time_window_ctrl").attr("class","glyphicon glyphicon-minus");
			$("#time_body").slideDown("fast");			
		} else {
			$("#time_window_ctrl").attr("class","glyphicon glyphicon-resize-vertical");			
			$("#time_body").slideUp("fast");
		}
	});
	
	//$('#timeline_kpi_set').buttonset();
	$('.dropdown-menu input, .dropdown-menu label, .dropdown-menu .col-lg-12').click(function(e) {
	    e.stopPropagation();
	});
	$("#timeline_kpi_btn").click(function(){
		kpi_name = getTimelineKpiSetButtonActiveId();
		line_kpi_name = getTimeline_Line_KpiSetButtonActiveId();
		// TimeLine
		timeline_kpi_show = kpi_name;
		timeline_line_kpi_show = line_kpi_name;
		refreshTimeLineChart();
		kpi_show_obj = {'kpi_name':kpi_show, 'line_kpi_name':line_kpi_name};
		// 品牌
		brand_chart_refreshChart(kpi_show_obj);
		// 分类
		category_chart_refreshChart(kpi_show_obj);
		// Color
		color_chart_refreshChart(kpi_show_obj);
		// Size
		size_chart_refreshChart(kpi_show_obj);
	});
	
	$("#time_line_table_ctl").click(function(){
		if ($("#sales_time_grid_table").is(":visible")==false) {
			$("#sales_time_table").slideDown("fast");
			$("#time_line_table_ctl").css("color","rgb(3, 127, 216)");
			 show_timeline_table();
		} else {
			$("#sales_time_table").slideUp("fast");
			$("#time_line_table_ctl").css("color","");	
		}
	})
	init_timeline_table();
	
	// 注册事件
	sales_time_chart_qty.on(events.CLICK, timeLine_click_handler);
}

function getTimelineKpiSetButtonActiveId(){
	result = 'qty';
	$("input[name='timeline_kpi_radio']:checked").each(function () {
		id = $(this).attr("id");
		if (id == "timeline_kpi_qty") {
			result = 'qty'
		} else if (id == "timeline_kpi_amt") {
			result = 'amt'
		} else if (id == "timeline_kpi_order") {
			result = 'order'
		}
		return result
	});
	return result
}

function getTimeline_Line_KpiSetButtonActiveId(){
	result = 'yoy';
	$("input[name='timeline_line_kpi_radio']:checked").each(function () {
		id = $(this).attr("id");
		if (id == "timeline_linekpi_yoy") {
			result = 'yoy'
		} else if (id == "timeline_linekpi_mom") {
			result = 'mom'
		}
		return result
	});
	return result
}

function timeline_reflush(eventTriggerFlag) {
	doGetTimelineDataReq(eventTriggerFlag)
}

// 按时间请求 开始
//		eventTriggerFlag：时间触发Flag
//							"0"：不触发	"1"：触发
function doGetTimelineDataReq(eventTriggerFlag) {
	 if (eventTriggerFlag == "0") {
		 timeline_crumbs = [];
	 }
	// 载入开始
	time_line_doLoading();
	// 月销量请求
    bigdata.post(rootPath + "/manage/getSalesDetailTimelineData.html", sales_search_cond, doGetTimelineDataReq_end, '', eventTriggerFlag);
}

// time_line_doLoading
function time_line_doLoading() {
	doLoading(sales_time_chart_qty);
	doLoading(sales_time_chart_order);
	doLoading(sales_time_chart_uv);
	doLoading(sales_time_chart_qty_sum);
	doLoading(sales_time_chart_pie_pcMobel);
	doLoading(sales_time_chart_pie_shops);	
}

//time_line_enLoading
function time_line_enLoading() {
	endLoading(sales_time_chart_qty);
	endLoading(sales_time_chart_order);
	endLoading(sales_time_chart_uv);
	endLoading(sales_time_chart_qty_sum);
	endLoading(sales_time_chart_pie_pcMobel);
	endLoading(sales_time_chart_pie_shops);	
}

//图表最终层标志位  初期值：0 中间层
var chartEndLevelFlg = "0";
//timeLine销售数据（请求返回缓存）
var time_line_json_buf = [];
//面包屑
var timeline_crumbs = [];
//Show KPI
var timeline_kpi_show = "qty";
var timeline_line_kpi_show = "yoy";
//按时间请求  结束
function doGetTimelineDataReq_end(json, eventTriggerFlag) {
	time_line_json_buf = json;
	// 载入结束
	time_line_enLoading();
	// 图形刷新
    refreshTimeLineChart();
	// 表格刷新
    refresh_timeline_table_data();
    // 面包屑刷新
    refresh_timeline_crumb(eventTriggerFlag);
}

// 图表刷新
function refreshTimeLineChart(){
	kpi_show = timeline_kpi_show;
	line_kpi_show = timeline_line_kpi_show;
	// 上次显示数据清空
	sales_time_chart_qty.clear();
	sales_time_chart_order.clear();
	sales_time_chart_uv.clear();
	sales_time_chart_qty_sum.clear();
	sales_time_chart_pie_pcMobel.clear();
	sales_time_chart_pie_shops.clear();

	// 请求后数据缓存
	time_line_data_buf = time_line_json_buf.timeLineDisBean;
	// QTY&YOY
	time_line_qty_xAxis = [];
	time_line_qty_yAxis = [];
	time_line_qty_yAxis_2 = [];
	kpi_show_name = kpi_show + '_kpi';
	line_kpi_show_name = 'value_y_rate_up';
	if (line_kpi_show == 'mom') {
		line_kpi_show_name = 'value_r_rate_up';
	}
	
	for (var i = 0; i < time_line_data_buf.length; i++){
		// echart X轴设定
		time_line_qty_xAxis.push(time_line_data_buf[i].title);
		// echart Y轴设定
		time_line_qty_yAxis.push(roundNumber(time_line_data_buf[i][kpi_show_name]['value'],0));
		time_line_qty_yAxis_2.push(roundNumber(time_line_data_buf[i][kpi_show_name][line_kpi_show_name]*100,0));
	}
	time_line_qty_option.xAxis[0].data = arrIsEmptyAddOneDefault(time_line_qty_xAxis);
	time_line_qty_option.series[0].data = arrIsEmptyAddOneDefault(time_line_qty_yAxis);
	time_line_qty_option.series[1].data = arrIsEmptyAddOneDefault(time_line_qty_yAxis_2);
	// 图例设置
	time_line_qty_option.title.text = kpi_show.toUpperCase() + '  & ' + line_kpi_show.toUpperCase()  +'%';
	time_line_qty_option.legend.data[0] = kpi_show.toUpperCase();
	time_line_qty_option.legend.data[1] = line_kpi_show.toUpperCase();
	time_line_qty_option.series[0].name = kpi_show.toUpperCase();
	time_line_qty_option.series[1].name = line_kpi_show.toUpperCase();

	// ORDER&ATV
	time_line_order_xAxis = [];
	time_line_order_yAxis = [];
	time_line_order_yAxis_2 = [];
	kpi_show_name_o = 'order_kpi';
	kpi_show1_name_o = 'atv_kpi';
	for (var i = 0; i < time_line_data_buf.length; i++){
		// echart X轴设定
		time_line_order_xAxis.push(time_line_data_buf[i].title);
		// echart Y轴设定
		time_line_order_yAxis.push(roundNumber(time_line_data_buf[i][kpi_show_name_o]['value'],0));
		time_line_order_yAxis_2.push(roundNumber(time_line_data_buf[i][kpi_show1_name_o]['value'],0));
	}
	time_line_order_option.xAxis[0].data = arrIsEmptyAddOneDefault(time_line_order_xAxis);
	time_line_order_option.series[0].data = arrIsEmptyAddOneDefault(time_line_order_yAxis);
	time_line_order_option.series[1].data = arrIsEmptyAddOneDefault(time_line_order_yAxis_2);
	
	//UV&TR%
	time_line_uv_xAxis = [];
	time_line_uv_yAxis = [];
	time_line_uv_yAxis_2 = [];
	kpi_show_name_u = 'uv_kpi';
	//kpi_show1 = 'tr_kpi';
	kpi_show1_name_u = 'tr_kpi';
	for (var i = 0; i < time_line_data_buf.length; i++){
		// echart X轴设定
		time_line_uv_xAxis.push(time_line_data_buf[i].title);
		// echart Y轴设定
		time_line_uv_yAxis.push(roundNumber(time_line_data_buf[i][kpi_show_name_u]['value'],0));
		time_line_uv_yAxis_2.push(roundNumber(time_line_data_buf[i][kpi_show1_name_u]['value'],2));
	}
	time_line_uv_option.xAxis[0].data = arrIsEmptyAddOneDefault(time_line_uv_xAxis);
	time_line_uv_option.series[0].data = arrIsEmptyAddOneDefault(time_line_uv_yAxis);
	time_line_uv_option.series[1].data = arrIsEmptyAddOneDefault(time_line_uv_yAxis_2);

	sales_time_chart_qty.setOption(time_line_qty_option);
	sales_time_chart_order.setOption(time_line_order_option);
	sales_time_chart_uv.setOption(time_line_uv_option);
	
	sales_time_chart_qty.connect([sales_time_chart_order, sales_time_chart_uv]);
	sales_time_chart_order.connect([sales_time_chart_qty, sales_time_chart_uv]);
	sales_time_chart_uv.connect([sales_time_chart_qty, sales_time_chart_order]);
	setTimeout(function (){
	    window.onresize = function () {
	    	sales_time_chart_qty.resize();
	    	sales_time_chart_order.resize();
	    	sales_time_chart_uv.resize();
	    }
	},200)
	
	// TimeLine SUM
	time_line_sum_data_buf = time_line_json_buf.timeLineSumDisBean;
	time_line_sum_yAxis = [time_line_sum_data_buf[kpi_show_name]['value_y']];
	time_line_sum_yAxis_2 = [time_line_sum_data_buf[kpi_show_name]['value']];
	time_line_sum_option.series[0].data = arrIsEmptyAddOneDefault(time_line_sum_yAxis);
	time_line_sum_option.series[1].data = arrIsEmptyAddOneDefault(time_line_sum_yAxis_2);
	time_line_sum_option.xAxis[0].data[0] = kpi_show.toUpperCase();
	time_line_sum_option.title.text = kpi_show.toUpperCase() + ' SUM';
	sales_time_chart_qty_sum.setOption(time_line_sum_option);
	
	// TimeLine PC vs Mobile SUM
	time_chart_pie_pcMobel_data = [];
	timeLineSumPcMobileDisBean = time_line_json_buf.timeLineSumPcMobileDisBean;
	for (var i = 0; i < timeLineSumPcMobileDisBean.length; i++){
		time_chart_pie_pcMobel_data.push({ 'name':timeLineSumPcMobileDisBean[i].title, 'value':timeLineSumPcMobileDisBean[i][kpi_show_name]['value'] });
	}
	time_chart_pie_pcMobel_option.series[0].data = arrIsEmptyAddOneDefault(time_chart_pie_pcMobel_data);
	time_chart_pie_pcMobel_option.title.text = kpi_show.toUpperCase() + '  PC vs Mobile';
	sales_time_chart_pie_pcMobel.setOption(time_chart_pie_pcMobel_option);
	
	// TimeLine shop SUM
	time_chart_pie_shops_data = [];
	timeLineSumShopDisBean = time_line_json_buf.timeLineSumShopDisBean;
	for (var i = 0; i < timeLineSumShopDisBean.length; i++){
		time_chart_pie_shops_data.push({ 'name':timeLineSumShopDisBean[i].sTitle, 'value':timeLineSumShopDisBean[i][kpi_show_name]['value'] });
	}
	time_chart_pie_shops_option.series[0].data = arrIsEmptyAddOneDefault(time_chart_pie_shops_data);
	time_chart_pie_shops_option.title.text = kpi_show.toUpperCase() + '  Shops';
	sales_time_chart_pie_shops.setOption(time_chart_pie_shops_option);
}


//表格数据Init
function init_timeline_table() {
	// 表格控件
	var grid_selector = "#sales_time_grid_table";
	// 表格翻页控件
	var pager_selector = "#sales_time_grid_pager";
	
	$(grid_selector).jqGrid({
		datatype: "local",
		height: 238,
		colNames:[			
    		          		'date',
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
    		          		'mom%',
    		          		'value',
    		          		'yoy%',
    		          		'mom%'
		          		],
		colModel:[	
			{name:'title',index:'title', width:60, editable: false,frozen : true, align:"center"},
			{name:'qty',index:'qty',width:75, sorttype:"int", editable:false,align:"right"},
			{name:'yoy_qty',index:'yoy_qty',width:75, sorttype:"int", editable:false,align:"right"},
			{name:'mom_qty',index:'mom_qty',width:75, sorttype:"int", editable:false,align:"right"},
			{name:'amt',index:'amt',width:75, sorttype:"int", editable:false,align:"right"},
			{name:'yoy_amt',index:'yoy_amt',width:75, sorttype:"int", editable:false,align:"right"},
			{name:'mom_amt',index:'mom_amt',width:75, sorttype:"int", editable:false,align:"right"},
			{name:'order',index:'order',width:75, sorttype:"int", editable:false,align:"right"},
			{name:'yoy_order',index:'yoy_order',width:75, sorttype:"int", editable:false, align:"right"},
			{name:'mom_order',index:'mom_order',width:75, sorttype:"int", editable:false, align:"right"},
			{name:'atv',index:'atv',width:75, sorttype:"int", editable:false,align:"right"},
			{name:'yoy_atv',index:'yoy_atv',width:75, sorttype:"int", editable:false,align:"right"},
			{name:'mom_atv',index:'mom_atv',width:75, sorttype:"int", editable:false,align:"right"},
			{name:'uv',index:'uv',width:75, sorttype:"int", editable:false,align:"right"},
			{name:'yoy_uv',index:'yoy_uv',width:75, sorttype:"int", editable:false,align:"right"},
			{name:'mom_uv',index:'mom_uv',width:75, sorttype:"int", editable:false,align:"right"}
		],
		viewrecords : true,
		rowNum:10,
		pager : pager_selector,
		pginput:false,
		width: 980,
		shrinkToFit:false		
	});
	$(grid_selector).jqGrid('navGrid', pager_selector, {
		add: false, edit: false, del: false, search: false, refresh: false
	}).jqGrid('navButtonAdd',
			pager_selector,
			{ caption: "Export", title: "Export to Excel", buttonicon: "ui-icon-bookmark", onClickButton: genTimeLineExportExcel, position: "last"}
	);
	
	$(grid_selector).jqGrid('setGroupHeaders', {
	  useColSpanStyle: true, 
	  groupHeaders:[
		{startColumnName: 'qty', numberOfColumns: 3, titleText: 'QTY'},
		{startColumnName: 'amt', numberOfColumns: 3, titleText: 'AMT'},
		{startColumnName: 'order', numberOfColumns: 3, titleText: 'Order'},
		{startColumnName: 'atv', numberOfColumns: 3, titleText: 'ATV'},
		{startColumnName: 'uv', numberOfColumns: 3, titleText: 'UV'}
	  ]	
	});
//	$(grid_selector).jqGrid("setFrozenColumns");
}

function show_timeline_table() {
	var grid_selector = "#sales_time_grid_table";
	$(grid_selector).jqGrid('destroyGroupHeader')
		.jqGrid('setGroupHeaders', {
		  useColSpanStyle: true, 
		  groupHeaders:[
			{startColumnName: 'qty', numberOfColumns: 3, titleText: 'QTY'},
			{startColumnName: 'amt', numberOfColumns: 3, titleText: 'AMT'},
			{startColumnName: 'order', numberOfColumns: 3, titleText: 'Order'},
			{startColumnName: 'atv', numberOfColumns: 3, titleText: 'ATV'},
			{startColumnName: 'uv', numberOfColumns: 3, titleText: 'UV'}
		  ]	
		})
		.jqGrid("destroyFrozenColumns")
		.jqGrid("setFrozenColumns");
}

//表格数据刷新
function refresh_timeline_table_data() {
	grid_data = [];
	timeLineDisBean = time_line_json_buf.timeLineDisBean;
	for (var i=0; i<timeLineDisBean.length; i++) {
		row = timeLineDisBean[i];
		grid_data.push(
		{
			'title':row.title, 
			'qty':roundNumber(row.qty_kpi.value,0),
			'yoy_qty':roundNumber(row.qty_kpi.value_y_rate_up*100,0),
			'mom_qty':roundNumber(row.qty_kpi.value_r_rate_up*100,0),
			'amt':roundNumber(row.amt_kpi.value,0),
			'yoy_amt':roundNumber(row.amt_kpi.value_y_rate_up*100,0),
			'mom_amt':roundNumber(row.amt_kpi.value_r_rate_up*100,0),
			'order':roundNumber(row.order_kpi.value,0),
			'yoy_order':roundNumber(row.order_kpi.value_y_rate_up*100,0),
			'mom_order':roundNumber(row.order_kpi.value_r_rate_up*100,0),
			'atv':roundNumber(row.atv_kpi.value,0),
			'yoy_atv':roundNumber(row.atv_kpi.value_y_rate_up*100,0),
			'mom_atv':roundNumber(row.atv_kpi.value_r_rate_up*100,0),
			'uv':roundNumber(row.uv_kpi.value,0),
			'yoy_uv':roundNumber(row.uv_kpi.value_y_rate_up*100,0),
			'mom_uv':roundNumber(row.uv_kpi.value_r_rate_up*100,0)
		});
	}
	// 表格控件
	var grid_selector = "#sales_time_grid_table";
	// 表格数据清空
	$(grid_selector).clearGridData();
	$(grid_selector).setGridParam({data: grid_data}).trigger("reloadGrid");
}

function timeLine_click_handler(param) {
	// 点击数据缓存
//	seriesIndex_buf = param.seriesIndex;
	dataIndex_buf = param.dataIndex;
	// 请求时间再设定
	time_line_data_buf = time_line_json_buf.timeLineDisBean;
	selectedObj = time_line_data_buf[dataIndex_buf];
	start_date = selectedObj.value;
	end_date = selectedObj.value;
	data_type = selectedObj.type;
	
	// 最终层的场合
	if (selectedObj.type == 'Day' && time_line_data_buf.length<=1) {
		return false;
	}
	
	if (selectedObj.type == 'Day') {
		start_date = selectedObj.value;
		end_date = selectedObj.value;
		data_type = 'Day';
	} else if (selectedObj.type == 'Month') {
		selectedValue = selectedObj.value
		selectedArr = selectedValue.split("-");
		start_date = getMonthStartDay(selectedArr[0], Number(selectedArr[1]));
		end_date = getMonthEndDay(selectedArr[0], Number(selectedArr[1]));
		data_type = 'Day';
	} else if (selectedObj.type == 'Year') {
		selectedValue = selectedObj.value
		start_date = selectedValue + '-01';
		end_date = selectedValue + '-12';
		data_type = 'Month';
	}
	// 设置日期条件
	refreshPageSearchTimeCond(data_type, start_date, end_date);
	// 请求数据
	//doGetTimelineDataReq("1");
	// 发送事件
	$(document).triggerHandler(charts_cust_events.TIME_PRESS);
}

//销售时间导航刷新
function refresh_timeline_crumb(eventTriggerFlag) {
	if (eventTriggerFlag !="0" 
		&& eventTriggerFlag != charts_cust_events.TIME_PRESS) {
		return;
	}
	// 面包屑追加（点击时，数据取得）
	var crumbItem = {};
	crumbItem['title'] = time_line_data_buf[0].title; 
	crumbItem['data'] = time_line_data_buf[0].value;
	if (time_line_data_buf.length>1) {
		crumbItem['title'] = time_line_data_buf[0].title + '～' + time_line_data_buf[time_line_data_buf.length-1].title; 
		crumbItem['data'] = time_line_data_buf[0].value + ',' +  time_line_data_buf[time_line_data_buf.length-1].value;
	}
	crumbItem['type'] = time_line_data_buf[0].type;
	timeline_crumbs.push(crumbItem);
	$("#timeline_breadcrumb").empty().append(getCrumbsHtml(timeline_crumbs, clickTimeLineCrumbs));
}


//面包屑点击事件
//crumbsContent : 全体面包屑
//crumbItem ：当前面包屑
//selectIndex ：选中Index
function clickTimeLineCrumbs(crumbsContent, crumbItem, selectIndex) {	
	// 面包屑 移除
	var popCount = crumbsContent.length - selectIndex;	
	for (var i = 0; i < popCount; i++) {
		crumbsContent.pop();
	}
	// 设置日期条件
	selectedArr = crumbItem.data.split(",");
	refreshPageSearchTimeCond(crumbItem.type, selectedArr[0], selectedArr[1]);
	// 请求数据
	//doGetTimelineDataReq("1");
	// 发送事件
	$(document).triggerHandler(charts_cust_events.TIME_PRESS);
}

function genTimeLineExportExcel() {
	var url_param = $.param(sales_search_cond);
	
	var download_url =rootPath + "/manage/getSalesTimeLineDataExcel.html?"+url_param;
	
	$.fileDownload(download_url, {
		failMessageHtml: "There was a problem generating your report, please try again."
	});
}