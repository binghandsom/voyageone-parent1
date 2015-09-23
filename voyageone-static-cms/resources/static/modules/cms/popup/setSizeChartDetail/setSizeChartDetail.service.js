/**
 * @Name:    popup.service.js
 * @Date:    2015/9/1
 *
 * @User:    Eric
 * @Version: 1.0.0
 */

define (function (require) {

	var cmsApp = require ('modules/cms/cms.module');
	require('components/services/ajax.service');

	cmsApp.service ('popSetSizeChartDetailService', ['$q', 'cmsAction', 'ajaxService',
		function ($q, cmsAction, ajaxService) {

			this.doSaveSizeChart = function (data) {
				var defer = $q.defer ();
				ajaxService.ajaxPostWithData (data, cmsAction.cms_popup_setsizechart_doSaveSizeChart)
					.then (
					function (response) {
						defer.resolve (response);
					});
				return defer.promise;
			};

			this.doSaveSizeChartDetail = function (data) {
				var defer = $q.defer ();
				ajaxService.ajaxPostWithData (data, cmsAction.cms_popup_setsizechart_doSaveSizeChartDetail)
					.then (
					function (response) {
						defer.resolve (response);
					});
				return defer.promise;
			};

			this.doUpdateSizeChartDetailInfo = function (data) {
				var defer = $q.defer ();
				ajaxService.ajaxPostWithData (data, cmsAction.cms_popup_setsizechart_doUpdateSizeChartDetail)
					.then (
					function (response) {
						defer.resolve (response);
					});
				return defer.promise;
			};

			this.doGetBoundSizeChart = function (data) {
				var defer = $q.defer ();
				ajaxService.ajaxPostWithData (data, cmsAction.cms_popup_setsizechart_doGetBindSizeChart)
					.then (
					function (response) {
						defer.resolve (response.data);
					});
				return defer.promise;
			};

			this.doGetSizeChartDetailInfo = function (data) {
				var defer = $q.defer ();
				ajaxService.ajaxPostWithData (data, cmsAction.cms_popup_setsizechart_doGetSizeChartDetailInfo)
					.then (
					function (response) {
						defer.resolve (response.data);
					});
				return defer.promise;
			};

			this.doGetSizeChartModel = function (channelId) {
				var defer = $q.defer ();
				var data = {channelId: channelId};
				ajaxService.ajaxPostWithData (data, cmsAction.cms_popup_setsizechart_doGetSizeChartModel)
					.then (
					function (response) {
						defer.resolve (response.data);
					});
				return defer.promise;
			}
		}]);
	return cmsApp;
});