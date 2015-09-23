/**
 * @Name: customerDetailService.js
 * @Date: 2015/3/31
 * 
 * @User: sky
 * @Version: 1.0.0
 */

define(function (require) {
	var omsApp = require('modules/oms/oms.module');
    //require('components/services/ajaxService');
    require('modules/oms/common/common.service');

	omsApp.service('customerDetailService',['$q', 'omsAction', 'ajaxService',
			function ($q, omsAction, ajaxService) {

		var CUSTOMER_ID = 'customer.customerId';
		var BEFORE_PAGE_URL = 'customer.beforePageUrl';
		var NOTES_ID = 'customerNotes.id';
		var NOTES_CONTENT = 'customerNotes.notesContent';

		this.setCustomerId = function (value) {
			sessionStorage.setItem(CUSTOMER_ID, value);
		};

		this.setBeforePageUrl = function (value) {
			sessionStorage.setItem(BEFORE_PAGE_URL, value);
		};

		this.getCustomerId = function () {
			return sessionStorage.getItem(CUSTOMER_ID);
		};
		
		this.setNotesId = function (value) {
			sessionStorage.setItem(NOTES_ID, value);
		}
		
		this.getNotesId = function () {
			return sessionStorage.getItem(NOTES_ID);
		};
		
		this.setNotesContent = function (value) {
			sessionStorage.setItem(NOTES_CONTENT, value);
		}
		
		this.getNotesContent = function () {
			return sessionStorage.getItem(NOTES_CONTENT);
		};

		//查询客户详细信息
		this.doCustomerDetailSearch = function () {
			var data = {};
			data.customerId = this.getCustomerId();
			var defer = $q.defer();
			ajaxService.ajaxPost(data,omsAction.oms_customer_doDetailSearch)
			.then(
					function (response) {
						defer.resolve(response);
			});
			return defer.promise;
		};

	}]);

	return omsApp;
});