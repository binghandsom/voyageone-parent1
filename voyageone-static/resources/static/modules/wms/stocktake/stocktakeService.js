/**
 * @Name:    stocktakeService.js
 * @Date:    2015/05/18
 * @User:    sky
 * @Version: 1.0.0
 */

define(function (require) {

    var wmsApp = require('modules/wms/wms.module');
    require('components/services/ajax.service');

    wmsApp.service('stocktakeService',['$q', 'wmsActions', 'ajaxService',
        function ($q, wmsActions, ajaxService) {

			//【stocktake SessionList页面】初始化
			this.doSessionListInit = function (data, scope) {
				return ajaxService.ajaxPost(data, wmsActions.stockTake.sessionList.init, scope);
			};

            //【stocktake SessionList页面】sessionList查询
            this.doSessionListSearch = function (data, scope) {
                return ajaxService.ajaxPost(data, wmsActions.stockTake.sessionList.search, scope);
            };

            //【stocktake SessionList页面】删除session
            this.deleteSession = function (data, scope) {
                return ajaxService.ajaxPost(data, wmsActions.stockTake.sessionList.delete, scope);
            };

            //【stocktake SessionList页面】修改processing为Stock
            this.stockSession = function (data, scope) {
                return ajaxService.ajaxPost(data, wmsActions.stockTake.sessionList.close, scope);
            };

            //【stocktake SessionList页面】newSession按钮
            this.newSession = function (data, scope) {
                return ajaxService.ajaxPost(data, wmsActions.stockTake.sessionList.newSession, scope);
            };

            //【stocktake SectionList页面】初始化
            this.doSectionListInit = function (data, scope) {
                return ajaxService.ajaxPost(data, wmsActions.stockTake.sectionList.init, scope);
            };

            //【stocktake SectionList页面】newSection
            this.newSection = function (data, scope) {
                return ajaxService.ajaxPost(data, wmsActions.stockTake.sectionList.newSection, scope);
            };

            //【stocktake SectionList页面】deleteSection
            this.deleteSection = function (data, scope) {
                return ajaxService.ajaxPost(data, wmsActions.stockTake.sectionList.deleteSection, scope);
            };

            //【stocktake Inventory页面】Init
            this.doInventoryInit = function (data, scope) {
                return ajaxService.ajaxPost(data, wmsActions.stockTake.inventory.init, scope);
            };

            //【stocktake Inventory页面】 ScanUpc
            this.doUpcScan = function (data, scope) {
                return ajaxService.ajaxPost(data, wmsActions.stockTake.inventory.scan, scope);
            };

            //【stocktake sectionDetail页面】 初始化
            this.doSectionDetailInit = function (data, scope) {
                return ajaxService.ajaxPost(data, wmsActions.stockTake.sectionDetail.init, scope);
            };

            //【stocktake sectionDetail页面】 doCloseSection
            this.doCloseSection = function (data, scope) {
                return ajaxService.ajaxPost(data, wmsActions.stockTake.sectionDetail.closeSection, scope);
            };

            //【stocktake Inventory页面】 reScan按钮
            this.doReScan = function (data, scope) {
                return ajaxService.ajaxPost(data, wmsActions.stockTake.sectionDetail.reScan, scope);
            };

            //【stocktake sessionList页面】 列表compare按钮
            this.doCompareInit = function (data, scope) {
                return ajaxService.ajaxPost(data, wmsActions.stockTake.compare.init, scope);
            };

            //【stocktake compare页面】 isFixed CheckBox 状态变化出发事件
            this.doCheckBoxValueChange = function (data, scope) {
                return ajaxService.ajaxPost(data, wmsActions.stockTake.compare.checkBoxValueChange, scope);
            };

            //获取sessionInfo
            this.doCheckSessionStatus = function (data, scope) {
                return ajaxService.ajaxPost(data, wmsActions.stockTake.compare.checkSessionStatus, scope);
            };

            //修改session的状态为Done
            this.doSessionDone = function (data, scope) {
                return ajaxService.ajaxPost(data, wmsActions.stockTake.compare.sessionDone, scope);
            };

            //【inventory页面】初始化，根据section状态判断按钮状态（是否disable）
            this.doCheckSectionStatus = function (data, scope) {
                return ajaxService.ajaxPost(data, wmsActions.stockTake.inventory.checkSectionStatus, scope);
            };
        }]);
    return wmsApp;
});