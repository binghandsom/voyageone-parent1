// 自定义组件
function BarLineChart() {
    
    //图形
    var chart;
    this.getChart = function(){ return chart;  }
    this.setChart = function(chart1){ chart=chart1;  }
    
    //图形参数
    var chart_bar_grid={ x:60, y: 40, x2:40, y2:30 };
    option = {
        title : {
            text: chartTitle,
            x: 'center',
    		y:5,
            textStyle: {
                fontSize: 14,
                fontFamily: 'Arial, Verdana, sans-serif',
                fontWeight: 'normal'
            }
        },
        tooltip : {
            trigger: 'axis',
            formatter : function (params) {
                return params[0].name + ':<br/>Value:'  +  formatNumber(params[0].value,0) +  '<br/>'  + lineKpiName.toUpperCase() + ':' +  formatNumber(params[1].value, 2) + "%";
	        }
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
            x : 'right',
            y : 0,
            data : ['QTY','Yoy']
        },
        grid : chart_bar_grid,
        xAxis : [
            {
                type : 'category',
                axisLine : { onZero : false},
            	data : []
            }
        ],
        yAxis : [
            {
//            	name : 'QTY',
                type : 'value',
                boundaryGap : [0, 0.05],
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
                data : []
            },
            {
            	name : '%',
                type : 'value',
                boundaryGap: [0, 0.05],
                splitLine : { show : false },
                splitArea : { show : false }
            }
        ],
        series : [
            {
                name:'QTY',
                type:'bar',
                data:[]
            },
            {
                "name" : "Yoy",
                "type" : "line",
                "yAxisIndex" : 1,
                "data" : []
            }
        ]
    };
    this.getOption = function(){ return option;  }
    this.setOption = function(option1){ option=option1;  }
    
    //图形
    var chartTitle = "";
    this.getChartTitle = function(){ return chartTitle;  }
    this.setChartTitle = function(chartTitle1){ chartTitle=chartTitle1;  }

    //数据
    var data = [];
    this.getData = function(){ return data;  }
    this.setData = function(data1){ data=data1;  }
    
    //面包屑
    var crumb = [];
    this.getCrumb = function(){ return crumb;  }
    this.setCrumb = function(crumb1){ crumb=crumb1;  }
    
    //Show KPI
    var kpiName;
    this.getKpiName = function(){ return kpiName;  }
    this.setKpiName = function(kpiName1){ kpiName=kpiName1;  }
    //Show KPI
    var lineKpiName;
    this.getLineKpiName = function(){ return lineKpiName;  }
    this.setLineKpiName = function(lineKpiName1){ lineKpiName=lineKpiName1;  }
    
//    //私有方法
//    function sayName(){
//        return msg;
//    }
    
	// chart 注册事件
    this.setClickEvent = function(event, click_handler){
    	chart.on(event, click_handler);
    }
    
    //chart 刷新
    this.chart_resize = function(){
    	chart.resize();
    }
   
    // chart 载入开始
    this.doLoading = function (){
    	chart.showLoading({text: '正在努力的读取数据中...'	});
    }

    // chart 载入结束
    this.endLoading = function (){
    	chart.hideLoading();
    }
    
    //图表刷新
    this.refreshChart = function (){
    	// 上次显示数据清空
    	chart.clear();
    	// 显示用数据清空
    	xAxis = [];
    	series_1 = [];
    	series_2 = [];
    	kpi_show_name = kpiName + '_kpi';
    	//arrDemo.sort(function(a,b){return a>b?1:-1});
    	row_datas =$.extend(true, [], data);
    	row_datas.sort(function(a,b){return a>b?1:-1});
    	row_datas = row_datas.sort(function(a,b){
    		return b[kpi_show_name]['value']-a[kpi_show_name]['value'];
    	});
    	
    	line_kpi_show_name = 'value_y_rate_up';
    	if (lineKpiName == 'mom') {
    		line_kpi_show_name = 'value_r_rate_up';
    	}
    	
    	// 显示用数据设置
    	for (var i=0; i<row_datas.length; i++){
    		row = row_datas[i];
    		// echart X轴设定
    		xAxis.push(row.title);
    		// echart Y1轴设定
    		series_1.push(roundNumber(row[kpi_show_name]['value'],0));
    		// echart Y2轴设定
    		series_2.push(roundNumber(row[kpi_show_name][line_kpi_show_name]*100,1));
    	}
    	// 图例变更
    	option.series[0].name = kpiName.toUpperCase();
    	// 图形控件设值
    	option.xAxis[0].data = arrIsEmptyAddOneDefault(xAxis);
    	option.series[0].data = arrIsEmptyAddOneDefault(series_1);
    	option.series[1].data = arrIsEmptyAddOneDefault(series_2);

    	// 图例设置
    	option.title.text = kpiName.toUpperCase() + ' & ' + lineKpiName.toUpperCase() + '%';
    	option.legend.data[0] = kpiName.toUpperCase();
    	option.legend.data[1] = lineKpiName.toUpperCase();
    	option.series[0].name = kpiName.toUpperCase();
    	option.series[1].name = lineKpiName.toUpperCase();
    	
    	// 刷新图形
    	chart.setOption(option);
    }

    var clickSelectedObj;
    this.getClickValue = function (param){
    	// 点击数据缓存
    	dataIndex_buf = param.dataIndex;
    	// 请求再设定
    	clickSelectedObj = data[dataIndex_buf];
    	selectedValue = clickSelectedObj.value;
    	return selectedValue;
    }
    
    this.setClickSelectedObj = function (clickSelected){
    	clickSelectedObj = clickSelected;
    }

    //面包屑刷新
    this.getCrumbText = function (clickHandle){
    	// 面包屑追加（点击时，数据取得）
    	var crumbItem = {};
    	if (clickSelectedObj) {
    		crumbItem['title'] = clickSelectedObj.title;
    		crumbItem['data'] = clickSelectedObj.value;
    	} else {
    		crumbItem['title'] = 'ALL'; 
    		crumbItem['data'] = '';
    	}
    	clickSelectedObj = null;
    	
    	crumb.push(crumbItem);
    	return getCrumbsHtml(crumb, clickHandle);
    }

    //gridSelector
    var gridSelector;
    this.getGridSelector = function(){ return gridSelector;  }
    this.setGridSelector = function(gridSelector1){ gridSelector=gridSelector1;  }
    
    //pager selector
    var pagerSelector;
    this.getPagerSelector = function(){ return pagerSelector;  }
    this.setPagerSelector = function(pagerSelector1){ pagerSelector=pagerSelector1;  }

    //pager selector
    var genExportExcel;
    this.getGenExportExcel = function(){ return genExportExcel;  }
    this.setGenExportExce = function(genExportExcel1){ genExportExcel=genExportExcel1;  }

	//表格数据INIT
	this.initDataTable = function (){
		$(gridSelector).jqGrid({
			datatype: "local",
			height: 238,
			colNames:[			
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
				          		'mom%',
				          		'value',
				          		'yoy%',
				          		'mom%'
			          		],
			colModel:[	
				{name:'title',index:'title', width:150, editable: false,frozen : true, align:"left"},
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
		  		rowList:[],
		  		pager : pagerSelector,
		  		altRows: false,
		  		pginput:false,
		  		width: 980,
		  		shrinkToFit:false
		  	}).jqGrid('navGrid', pagerSelector, {
		  		add: false, edit: false, del: false, search: false, refresh: false
		  	}).jqGrid('navButtonAdd',
				pagerSelector,
				{ caption: "Export", title: "Export to Excel", buttonicon: "ui-icon-bookmark", onClickButton: genExportExcel, position: "last"}
		  	);
	}

	this.groupFrozeDataTable = function (gridSelector){
		gridSelector.jqGrid('destroyGroupHeader')
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
	this.refreshTableData = function (){
		grid_data = [];
		for (var i=0; i<data.length; i++) {
			row = data[i];
			grid_data.push(
			{
				'title':row.title, 
				'qty':formatNumber(row.qty_kpi.value,0),
				'yoy_qty':formatNumber(row.qty_kpi.value_y_rate_up*100,1),
				'mom_qty':formatNumber(row.qty_kpi.value_r_rate_up*100,1),
				'amt':formatNumber(row.amt_kpi.value,0),
				'yoy_amt':formatNumber(row.amt_kpi.value_y_rate_up*100,1),
				'mom_amt':formatNumber(row.amt_kpi.value_r_rate_up*100,1),
				'order':formatNumber(row.order_kpi.value,0),
				'yoy_order':formatNumber(row.order_kpi.value_y_rate_up*100,1),
				'mom_order':formatNumber(row.order_kpi.value_r_rate_up*100,1),
				'atv':formatNumber(row.atv_kpi.value,0),
				'yoy_atv':formatNumber(row.atv_kpi.value_y_rate_up*100,1),
				'mom_atv':formatNumber(row.atv_kpi.value_r_rate_up*100,1),
				'uv':formatNumber(row.uv_kpi.value,0),
				'yoy_uv':formatNumber(row.uv_kpi.value_y_rate_up*100,1),
				'mom_uv':formatNumber(row.uv_kpi.value_r_rate_up*100,1)
			});
		}
		// 表格数据清空
		$(gridSelector).clearGridData();
		$(gridSelector).setGridParam({data: grid_data}).trigger("reloadGrid");
	}

}
