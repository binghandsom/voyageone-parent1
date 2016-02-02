/**
 * @Name:    printService.js
 * @Date:    2015/5/11
 * @User:    sky
 * @Version: 1.0.0
 */

define(['modules/wms/wms.module'], function (wmsApp) {
    wmsApp.service('printService',
    			   ['$q',
    			    "userService",
    			    "wmsConstant",
    			    function ($q, userService, wmsConstant) {
    				   var activeX = document.getElementById("AutoScale"); 
    				   var printerName = "", printerFixVal1 = "",  printerFixVal2 = "";
    		            this.doPrint = function (business, hardware_key, jsonData) {
    		            	if(activeX.object == null){
    		            		//TODO 国际化
    		            		alert("请使用IE浏览器，如果仍无法打印请确认是否安装打印控件！");
    		            		return false;
    		            	}
    		            	//获取用户打印机配置信息
    		            	getUserConfig(hardware_key);
    		            	return print(business, jsonData);
    		            };
    		            
    		            //获取打印机配置信息
    		            function getUserConfig(hardware_key){
    		            	var printConfig = _.find(userService.getHardware(), function(obj){
    		            		return obj.hardware_key == hardware_key;
    		            	});
    		            	if(printConfig.length == 0){
    		            		alert("没有获取到用户配置的打印机信息！");
    		            		return;
    		            	}
    		            	printerName = printConfig.hardware_name;
    		            	printerFixVal1 = printConfig.fix_val1;
    		            	printerFixVal2 = printConfig.fix_val2;
    		            }
    		            
    		            function print(business, jsonData) {
    		        	    var printResult = true;
    		        	    try {
    		        	    	if(business == wmsConstant.print.business.ReturnLabel){
    		        	    		//打印退货单
    		            	    	activeX.doPrintReturnLabel(jsonData, printerName, printerFixVal1, printerFixVal2);
    		        	    	}else if(business == wmsConstant.print.business.sf){
    		        	    		activeX.doPrintSF(jsonData, printerName, printerFixVal1, printerFixVal2);
								}else if(business == wmsConstant.print.business.PickUp){
									activeX.doPrintPickUpLabel(jsonData, printerName, printerFixVal1, printerFixVal2);
								}else if(business == wmsConstant.print.business.Location){
									activeX.doPrintLocationLabel(jsonData, printerName, printerFixVal1, printerFixVal2);
								}else if(business == wmsConstant.print.business.SKU){
									activeX.doPrintSkuLabel(jsonData, printerName, printerFixVal1, printerFixVal2);
    		        	    	}else{
    		        	    		alert("未找到对应的打印配置信息！");
    		        	    	}
    		        	    }catch(e){
    		        	    	printResult = false;
    		        	    	alert("Print Error " + "add_line_print():" + e.name + "||" + e.message);
    		        	    }
    		        	    return printResult;
    		        	}
    			   }]);
    
    return wmsApp;
});