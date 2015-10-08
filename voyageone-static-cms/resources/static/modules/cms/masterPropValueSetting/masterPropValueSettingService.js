/**
 * @Name:    categorySearchService
 * @Date:    2015/06/12
 *
 * @User:    Lewis.liu
 * @Version: 1.0.0
 */

define(["modules/cms/cms.module","components/services/ajax.service"],function (cmsServiceModule) {
	
	cmsServiceModule.service("masterPropValueSettingService",["$q", "cmsAction", "ajaxService",
	    function($q, cmsAction, ajaxService) {
			/**
			 * 页面初始化.
			 */
			this.doInit=function(formData){
				
				 var defer = $q.defer();
		            ajaxService.ajaxPost(formData, cmsAction.masterPropSetting.init,null)
		                .then(function(response) {
		                    
		                    defer.resolve(response);
		                });
		            return defer.promise;
			};
			
			/**
			 * 类目搜索.
			 */
	        this.doSearch = function (formData) {
	            var defer = $q.defer();
	            ajaxService.ajaxPost(formData, cmsAction.masterPropSetting.search,null)
	                .then(function(response) {
	                    
	                    defer.resolve(response);
	                });
	            return defer.promise;
	        };
	        /**
	         * 处理提交.
	         */
	        this.doSubmit = function (formData) {
	            var defer = $q.defer();
	            ajaxService.ajaxPost(formData, cmsAction.masterPropSetting.submit,null)
	                .then(function(response) {
	                    
	                    defer.resolve(response);
	                });
	            return defer.promise;
	        };
	        /**
	         * 获取类目菜单.
	         */
	        this.getCategoryNav = function () {
	            var defer = $q.defer();
	            ajaxService.ajaxPost({}, cmsAction.masterPropSetting.getCategoryNav,null)
	                .then(function(response) {
	                    
	                    defer.resolve(response);
	                });
	            return defer.promise;
	        };
	        
	        /**
	         * 清空表格最后一行数据.
	         */
	        this.clearItem= function(source) {

			    for (var key in source) {
					if (source[key].propType===1) {
						source[key].propValue="";
					}else if (source[key].propType===2) {
						source[key].propValue="";
					}else if (source[key].propType===3) {
						var options = source[key].propValue;
						for (var optionKey in options) {
							options[optionKey].optionValue="";
						}
					}else if (source[key].propType===4) {
						source[key].propValue="";
					}else if (source[key].propType===5 || source[key].propType===6) {
						this.clearItem(source[key].children);
					}
			   } 
			};
			
			/**
			 * 设置必填项样式.
			 */
			this.setBackgroundColor =function() {
				//设定必填项背景颜色
				$('li:contains("必须填写")').parents("fieldset").css('background-color', '#fcf0ef');
				$('li:contains("必须填写")').parents("fieldset").find("fieldset").css('background-color', '#fcf0ef');
				$('li:contains("必须填写")').parent().children().filter('li:contains("禁用规则")').parents("fieldset").css('background-color', '#fbf6ee');
				$('li:contains("必须填写")').parent().children().filter('li:contains("禁用规则")').parents("fieldset").find("fieldset").css('background-color', '#fbf6ee');
				
			};
			
			 /**
	         * 获取类目菜单.
	         */
	        this.doSwitchCategory = function (parms) {
	            var defer = $q.defer();
	            ajaxService.ajaxPost(parms, cmsAction.masterPropSetting.switchCategory,null)
	                .then(function(response) {
	                    
	                    defer.resolve(response);
	                });
	            return defer.promise;
	        };

    }]);

});