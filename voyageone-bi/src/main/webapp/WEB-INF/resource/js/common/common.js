// 共通函数
// 小数点判定
function hasDot(num) {
    if (!isNaN(num)) {
        return ((num + '').indexOf('.') != -1) ? true : false;
    }
}

//数字格式化
function roundNumber(num, precision) {
    return pformatNumber(num, precision, '', '');
}

// 数字格式化
function formatNumber(num, precision, separator) {
    return pformatNumber(num, precision, separator, ',');
}

//数字格式化
function pformatNumber(num, precision, separator, spider) {
    var parts;
    // 判断是否为数字
    if (!isNaN(parseFloat(num)) && isFinite(num)) {
        // 把类似 .5, 5. 之类的数据转化成0.5, 5, 为数据精度处理做准, 至于为什么
        // 不在判断中直接写 if (!isNaN(num = parseFloat(num)) && isFinite(num))
        // 是因为parseFloat有一个奇怪的精度问题, 比如 parseFloat(12312312.1234567119)
        // 的值变成了 12312312.123456713
        num = Number(num);
        // 处理小数点位数
        num = (typeof precision !== 'undefined' ? num.toFixed(precision) : num).toString();
        // 分离数字的小数部分和整数部分
        parts = num.split('.');
        // 整数部分加[separator]分隔, 借用一个著名的正则表达式
        parts[0] = parts[0].toString().replace(/(\d)(?=(\d{3})+(?!\d))/g, '$1' + (separator || spider));

        return parts.join('.');
    }
    return NaN;
}


// X轴数据格式化显示
//		xAxisDataOrigin：X轴输入数据
function formatXAxisData(xAxisDataOrigin) {
	var output = xAxisDataOrigin;
	
	if (xAxisDataOrigin.length > 16){
		output = xAxisDataOrigin.substr(0,13) + "...";
	}
	
	return output;
}

// X轴数据格式化显示
//		xAxisDataOrigin：X轴输入数据
function formatXAxisData_short(xAxisDataOrigin) {
	var output = xAxisDataOrigin;
	
	if (xAxisDataOrigin.length > 6){
	output = xAxisDataOrigin.substr(0,4) + "..";
	}
	
	return output;
}

// 缓存数据排序
//		buf_data：缓存数据
//		sortkpi：排序KPI
function sortKpiData(buf_data, sortkpi){
//	// sort data 取得
//	for (var i = 0; i < buf_data.length; i++){
//		// 根据Sort KPI 取得 sort 用Data
//		for (var j = 0; j < buf_data[i].y_data.length; j++) {
//			// 根据显示KPI，取得对应的值
//			if (buf_data[i].y_data[j].name == sortkpi) {
//				buf_data[i].sort_data = buf_data[i].y_data[j].value;
//				
//				break;
//			}
//		}
//	}
	for (var i = 0; i < buf_data.length; i++){
		// 根据Sort KPI 取得 sort 用Data
		for (var propName in buf_data[i]){
			if (propName == sortkpi) {
				
				buf_data[i]["sort_data"] = buf_data[i][propName];
				break;
			}
		}
	}
	
	// 根据KPI排序（降序）
	buf_data.sort(sortKpiData_descSortByField);
}

// 排序规则 降序
function sortKpiData_descSortByField(x, y) {
	return y.sort_data - x.sort_data;
}
//排序规则 升序
function sortKpiData_ascSortByField(x, y) {
	return x.sort_data - y.sort_data;
}

// DB日期  -> 显示日期
//		yyyymmdd -> yyyy-mm-dd
function dateDBToDisplay(dbDate){
	var ret = dbDate;
	
	if (dbDate.length == 4) {
		ret = dbDate;
	} else if (dbDate.length == 6) {
		ret = dbDate.substr(0,4) + "-" + dbDate.substr(4,2);
	} else if (dbDate.length == 8) {
		ret = dbDate.substr(0,4) + "-" + dbDate.substr(4,2) + "-" + dbDate.substr(6,2);
	}
	
	return ret;
}

// 显示日期  -> DB日期 
//		yyyy-mm-dd -> yyyymmdd 
function dateDisplayToDB(dspDate){
	var ret = "";
	
	ret = dspDate.replace(/-/g, "");
	
	return ret;
}

// 检索日期初期值取得
//		ret[0]：开始检索年月
//		ret[1]：终了检索年月
function getInitSearchDateFromTo() {
	var ret = ["",""]; 
	
	var now = new Date();
	var year = now.getFullYear();
	
	ret[0] = (year - 1) + "-" + "01";
	ret[1] = (year - 1) + "-" + "12";
	
	return ret;
}

// 同比增幅计算
// 		yoyValue：同比值
//		curValue：当前值
function getUpRate(yoyValue, curValue) {
	if (yoyValue == "0") {
		return "-";
	} else {
		var ret = ((parseFloat(curValue) - parseFloat(yoyValue)) / parseFloat(yoyValue)) * 100;
	
	    // 	小数点后保留两位
	    return ret.toFixed(2);
	}
}

// 占比计算
//		blockValue：部分值
//		totalValue：整体值
function getRate(blockValue, totalValue) {
	if (totalValue == "0") {
		return "-";
	} else {
		var ret = (parseFloat(blockValue) / parseFloat(totalValue)) * 100;
		
		// 	小数点后保留两位
		return ret.toFixed(2);
	}		
}

// 单价计算
//		quantity：数量
//		value：价值
function getUnitPrice(quantity, value) {
	if (quantity == "0") {
		return "-";
	} else {		
		var ret = parseFloat(value) / parseFloat(quantity);
		
		// 	小数点后保留两位
		return ret.toFixed(2);
	}
}

// 元素是否在列表中
//		item：元素
//		itemCollection：元素列表
function isItemInArray(item, itemCollection) {
	var ret = false;
	
	for (var i = 0; i < itemCollection.length; i++) {
		if (itemCollection[i] == item) {
			ret = true;
			break;
		}
	}
	
	return ret;
}

// 是否数字
//		item：元素
function isNumeric(item) {
	if (item == null || item ==""){
		return false;
	} else {		
		if (!isNaN(item)) {
			return true;
		} else {
			return false;
		}
	}
}

//复选框事件
//全选、取消全选的事件
function selectAll(checkname){
	if ($("#" + checkname).attr("checked")) {
		  $("input[type='checkbox'][id='"+ checkname +"_sub']").each(function () {
			  $(this).attr('checked',true);
		  });
	    //$(":checkbox").attr("checked", true); 
	} else {
		  $("input[type='checkbox'][id='"+ checkname +"_sub']").each(function () {
			  $(this).attr('checked',false);
		  });
	}
}

//子复选框的事件  
function setSelectAll(checkname){  
	//当没有选中某个子复选框时，SelectAll取消选中  
	if (!$("#" + checkname + "_sub").checked) {  
	    $("#" + checkname).attr("checked", false);  
	}  
	var chsub = $("input[type='checkbox'][id='"+ checkname +"_sub']").length; //获取subcheck的个数  
	var checkedsub = $("input[type='checkbox'][id='"+ checkname +"_sub']:checked").length; //获取选中的subcheck的个数  
	if (checkedsub == chsub) {  
	    $("#" + checkname).attr("checked", true);  
	}
}

//获取AddDayCount天后的日期   
function getDateStr(AddDayCount) {   
	var dd = new Date();   
	dd.setDate(dd.getDate()+AddDayCount);//获取AddDayCount天后的日期   
	var y = dd.getFullYear();   
	var m = dd.getMonth()+1;//获取当前月份的日期   
	var d = dd.getDate();   
	return y+"-"+pad(m,2)+"-"+pad(d,2);   
}

//检索日期初期值取得
function getMonthFirstDay() {
	var dd = new Date();   
	var y = dd.getFullYear();   
	var m = dd.getMonth()+1;//获取当前月份的日期   
	return y+"-"+pad(m,2)+"-01";   
}

//检索日期初期值取得
function getMonthStartDay(year, month) {
	var dd = new Date(year, month-1, 1);   
	var y = dd.getFullYear();   
	var m = dd.getMonth()+1;//获取当前月份的日期   
	return y+"-"+pad(m,2)+"-01"; 
}

//检索日期初期值取得
function getMonthEndDay(year, month) {
	var dd = new Date(year, month, 1);   
	dd.setDate(dd.getDate()-1);   
	var y = dd.getFullYear();   
	var m = dd.getMonth()+1;//获取当前月份的日期   
	var d = dd.getDate();   
	return y+"-"+pad(m,2)+"-"+pad(d,2);   
}

//检索日期初期值取得
function getMonth() {
	var dd = new Date();   
	var y = dd.getFullYear();   
	var m = dd.getMonth()+1;//获取当前月份的日期   
	return y+"-"+pad(m,2);   
}

//检索日期初期值取得
function getYear() {
	var dd = new Date();   
	var y = dd.getFullYear();   
	return y;   
}

//pad
function pad(num, n) {  
    var len = num.toString().length;  
    while(len < n) {  
        num = "0" + num;  
        len++;  
    }  
    return num;  
}

//pad
function arrIsEmptyAddOneDefault(dataArr) {  
	if (dataArr.length == 0) {
		dataArr.push('');
	}
    return dataArr;  
}