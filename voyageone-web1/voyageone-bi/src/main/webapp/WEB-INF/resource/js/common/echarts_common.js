// echarts共通定义
var events = {
	// -------全局通用
	REFRESH: 'refresh',
	RESTORE: 'restore',
	RESIZE: 'resize',
	CLICK: 'click',
	DBLCLICK: 'dblclick',
	HOVER: 'hover',
	MOUSEOUT: 'mouseout',
	// -------业务交互逻辑
	DATA_CHANGED: 'dataChanged',
	DATA_ZOOM: 'dataZoom',
	DATA_RANGE: 'dataRange',
	DATA_RANGE_HOVERLINK: 'dataRangeHoverLink',
	LEGEND_SELECTED: 'legendSelected',
	LEGEND_HOVERLINK: 'legendHoverLink',
	MAP_SELECTED: 'mapSelected',
	PIE_SELECTED: 'pieSelected',
	MAGIC_TYPE_CHANGED: 'magicTypeChanged',
	DATA_VIEW_CHANGED: 'dataViewChanged',
	TIMELINE_CHANGED: 'timelineChanged',
	MAP_ROAM: 'mapRoam'
};

// 载入开始
function doLoading(varChart){
	varChart.showLoading({
	    text: '正在努力的读取数据中...',    //loading话术
	});
}

// 载入结束
function endLoading(varChart){
	// ajax callback
	varChart.hideLoading();
}
        
