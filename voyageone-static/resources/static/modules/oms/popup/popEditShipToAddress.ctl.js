/**
 * @Name:    popShipAddressService.js
 * @Date:    2015/5/4
 *
 * @User:    Eric
 * @Version: 1.0.0
 */

define (function (require) {
    var omsApp = require ('modules/oms/oms.module');
    require ('modules/oms/common/common.service');
    require ('modules/oms/popup/popup.service');

    omsApp.controller ('popEditShipToAddressController',
        ['$scope', 'popShipToAddressService', 'omsCommonService', 'omsType',
            function ($scope, popShipToAddressService, omsCommonService, omsType) {

                popShipToAddressService.doGetCodeList ()
                    .then (function (data) {
                    $scope.countryList = omsCommonService.doGetOneCodeFromList (data, omsType.country);

                    // 初始化当前用户信息
                    $scope.shipToOrderInfo = $scope.popShipToOrderToUseInfo;
                });


                $scope.doOk = function () {

                    if (!$scope.popForm.$valid) {
                        // TODO 显示请输入reason的message.
                    } else {
                        // TODO 这种做法不好，但是没有办法， 因为orderdetail页面的sold to  不是对象
                        var data = {};
                        data.sourceOrderId = $scope.shipToOrderInfo.sourceOrderId;
                        data.orderNumber = $scope.shipToOrderInfo.orderNumber;
                        data.shipName = $scope.shipToOrderInfo.shipName;
                        data.shipEmail = $scope.shipToOrderInfo.shipEmail;
                        data.shipPhone = $scope.shipToOrderInfo.shipPhone;
                        data.shipCompany = $scope.shipToOrderInfo.shipCompany;
                        data.shipAddress = $scope.shipToOrderInfo.shipAddress;
                        data.shipAddress2 = $scope.shipToOrderInfo.shipAddress2;
                        data.shipCity = $scope.shipToOrderInfo.shipCity;
                        data.shipState = $scope.shipToOrderInfo.shipState;
                        data.shipZip = $scope.shipToOrderInfo.shipZip;
                        data.shipCountry = $scope.shipToOrderInfo.shipCountry;

                        data.origShipName = $scope.shipToOrderInfo.origShipName;
                        data.origShipEmail = $scope.shipToOrderInfo.origShipEmail;
                        data.origShipPhone = $scope.shipToOrderInfo.origShipPhone;
                        data.origShipCompany = $scope.shipToOrderInfo.origShipCompany;
                        data.origShipAddress = $scope.shipToOrderInfo.origShipAddress;
                        data.origShipAddress2 = $scope.shipToOrderInfo.origShipAddress2;
                        data.origShipCity = $scope.shipToOrderInfo.origShipCity;
                        data.origShipCountry = $scope.shipToOrderInfo.origShipCountry;
                        data.origShipState = $scope.shipToOrderInfo.origShipState;
                        data.origShipZip = $scope.shipToOrderInfo.origShipZip;
                        popShipToAddressService.doUpdateShipToAddress (data)
                            .then (function (data) {

                            	$scope.shipToOrderInfo.origShipName = $scope.shipToOrderInfo.shipName;
                            	$scope.shipToOrderInfo.origShipEmail = $scope.shipToOrderInfo.shipEmail;
                            	$scope.shipToOrderInfo.origShipPhone = $scope.shipToOrderInfo.shipPhone;
                            	$scope.shipToOrderInfo.origShipCompany = $scope.shipToOrderInfo.shipCompany;
                            	$scope.shipToOrderInfo.origShipAddress = $scope.shipToOrderInfo.shipAddress;
                            	$scope.shipToOrderInfo.origShipAddress2 = $scope.shipToOrderInfo.shipAddress2;
                            	$scope.shipToOrderInfo.origShipCity = $scope.shipToOrderInfo.shipCity;
                            	$scope.shipToOrderInfo.origShipCountry = $scope.shipToOrderInfo.shipCountry;
                            	$scope.shipToOrderInfo.origShipState = $scope.shipToOrderInfo.shipState;
                            	$scope.shipToOrderInfo.origShipZip = $scope.shipToOrderInfo.shipZip;
                            $scope.$parent.setPopupEditAddress ($scope.shipToOrderInfo, data);
                            $scope.doClose();
                        });
                    }
                };

                /**
                 * 关闭窗口，并初始化该页面输入内容.
                 */
                $scope.doClose = function () {
                    $scope.$parent.doPopupClose();
                    $scope.closeThisDialog();
                };
            }])
});			
