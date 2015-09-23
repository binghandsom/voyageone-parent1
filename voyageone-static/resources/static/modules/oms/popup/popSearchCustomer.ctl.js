/**
 * @Name:    popSearchCustomer.ctl.js
 * @Date:    2015/5/6
 *
 * @User:    Edward
 * @Version: 1.0.0
 */

define (function (require) {
    var omsApp = require ('modules/oms/oms.module');
    require ('modules/oms/common/common.service');
    require ('modules/oms/popup/popup.service');

    omsApp.controller ('popSearchCustomerController'
        , ['$scope', 'omsCommonService', 'popSearchCustomerService', 'omsType'
            , function ($scope, omsCommonService, popSearchCustomerService, omsType) {
                var _ = require ('underscore');

                // 初始化检索条件
                $scope.searchInfo = {};
                $scope.searchType = 1;
                // 初始化检索返回结果
                $scope.customerList = [];
                // 初始化检索结果
                $scope.selectCustomerInfo = {};
                $scope.selectCustomerInfo.index = -1;
                // 初始化是否选中信息
                $scope.isSelectCustomerInfo = false;

                popSearchCustomerService.doGetCodeList ()
                    .then (function (data) {
                        $scope.countryList = omsCommonService.doGetOneCodeFromList (data, omsType.country);
                    });

                /**
                 * 检测searchType，一旦发生变化将所有检索条件清空.
                 */
                $scope.$watch ("searchType", function () {
                    $scope.searchInfo = {};
                });

                /**
                 * 检索客户信息，并返回Customer List.
                 */
                $scope.doSearch = function () {
                	var data = $scope.searchInfo;
                	data.order_channel_id = [];
                	data.order_channel_id.push($scope.popUpUseInfo.orderChannelId);
                    popSearchCustomerService.doGetCustomerList (data)
                        .then (function (data) {
                        $scope.customerList = data.customerList;
                        if (!_.isEmpty ($scope.customerList)) {
                            selectCustomer (0);
                        }
                    })
                };

                /**
                 * 让某个Customer被选中.
                 * @param index
                 */
                $scope.doSelectCustomer = function (index) {
                    selectCustomer (index);
                };

                /**
                 * 给主画面的Customer信息赋值.
                 */
                $scope.doSelect = function () {
                    $scope.$parent.setPopupCustomerInfo ($scope.selectCustomerInfo);
                    $scope.doClose();
                };

                /**
                 * 关闭窗口，并初始化该页面输入内容.
                 */
                $scope.doClose = function () {
                    $scope.$parent.doPopupClose();
                    $scope.closeThisDialog();
                };

                /**
                 * 选中一条CustomerInfo.
                 * @param index
                 */
                function selectCustomer (index) {
                    if (index >= 0) {

                        //默认检索的第一条数据被选中
                        $scope.selectCustomerInfo = $scope.customerList[index];
                        $scope.selectCustomerInfo.index = index;
                        $scope.isSelectCustomerInfo = true;
                    }
                }

            }])
});
