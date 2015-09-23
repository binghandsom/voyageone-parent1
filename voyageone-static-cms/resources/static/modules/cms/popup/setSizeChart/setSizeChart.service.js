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

	cmsApp.service ('popSetSizeChartService', ['$q', 'cmsAction', 'ajaxService',
		function ($q, cmsAction, ajaxService) {

			this.doGetBoundSizeChartList = function (data) {
				var defer = $q.defer ();
				ajaxService.ajaxPostWithData (data, cmsAction.cms_popup_setsizechart_doGetBindedSizeChartList)
					.then (function (response) {
					defer.resolve (response);
				});
				return defer.promise;
			};

			this.doGetOtherSizeChartList = function (data) {
				var defer = $q.defer ();
				ajaxService.ajaxPostWithData (data, cmsAction.cms_popup_setsizechart_doGetOtherSizeChartList)
					.then (function (response) {
					defer.resolve (response);
				});
				return defer.promise;
			};

			this.doSaveCategorySizeChart = function (data) {
				var defer = $q.defer ();
				ajaxService.ajaxPostWithData (data, cmsAction.cms_popup_setsizechart_doSaveCategorySizeChart)
					.then (function (response) {
					defer.resolve (response);
				});
				return defer.promise;
			};
			this.doSaveModelSizeChart = function (data) {
				var defer = $q.defer ();
				ajaxService.ajaxPostWithData (data, cmsAction.cms_popup_setsizechart_doSaveModelSizeChart)
					.then (function (response) {
					defer.resolve (response);
				});
				return defer.promise;
			};

		}]);
	return cmsApp;
});