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

    omsApp.controller ('popEditSoldToAddressController',
        ['$scope', 'popSoldToAddressService', 'omsCommonService', 'omsType',
            function ($scope, popSoldToAddressService, omsCommonService, omsType) {

                popSoldToAddressService.doGetCodeList ()
                    .then (function (data) {
                    $scope.countryList = omsCommonService.doGetOneCodeFromList (data, omsType.country);

                    // 初始化当前用户信息
                    $scope.currentCustomerInfo = {};
                    $scope.soldToOrderInfo = $scope.popSoldToOrderToUseInfo;
                    $scope.soldToOrderInfo.copyToCustomer = false;

                    // 初始化取得customer信息
                    popSoldToAddressService.doGetCustomerInfo ({customerId: $scope.popSoldToOrderToUseInfo.customerId})
                        .then (function (data) {
                        $scope.currentCustomerInfo = data.customerInfo;
                    });
                });


                /**
                 * 更改sold地址
                 */
                $scope.doOk = function () {

                    if (!$scope.popForm.$valid) {
                        // TODO 显示请输入reason的message.
                    } else {
                        // TODO 这种做法不好，但是没有办法， 因为orderdetail页面的sold to  不是对象
                        var data = {};
                        data.sourceOrderId = $scope.soldToOrderInfo.sourceOrderId;
                        data.orderNumber = $scope.soldToOrderInfo.orderNumber;
                        data.name = $scope.soldToOrderInfo.name;
                        data.email = $scope.soldToOrderInfo.email;
                        data.phone = $scope.soldToOrderInfo.phone;
                        data.company = $scope.soldToOrderInfo.company;
                        data.address = $scope.soldToOrderInfo.address;
                        data.address2 = $scope.soldToOrderInfo.address2;
                        data.city = $scope.soldToOrderInfo.city;
                        data.country = $scope.soldToOrderInfo.country;
                        data.state = $scope.soldToOrderInfo.state;
                        data.zip = $scope.soldToOrderInfo.zip;

                        data.origName = $scope.soldToOrderInfo.origName;
                        data.origEmail = $scope.soldToOrderInfo.origEmail;
                        data.origPhone = $scope.soldToOrderInfo.origPhone;
                        data.origCompany = $scope.soldToOrderInfo.origCompany;
                        data.origAddress = $scope.soldToOrderInfo.origAddress;
                        data.origAddress2 = $scope.soldToOrderInfo.origAddress2;
                        data.origCity = $scope.soldToOrderInfo.origCity;
                        data.origCountry = $scope.soldToOrderInfo.origCountry;
                        data.origState = $scope.soldToOrderInfo.origState;
                        data.origZip = $scope.soldToOrderInfo.origZip;
                        if ($scope.soldToOrderInfo.copyToCustomer) {
                        	data.customerId = $scope.soldToOrderInfo.customerId;
                        }

                        popSoldToAddressService.doUpdateSoldToAddress (data)
                            .then (function (data) {

                            	$scope.soldToOrderInfo.origName = $scope.soldToOrderInfo.name;
                            	$scope.soldToOrderInfo.origEmail = $scope.soldToOrderInfo.email;
                            	$scope.soldToOrderInfo.origPhone = $scope.soldToOrderInfo.phone;
                            	$scope.soldToOrderInfo.origCompany = $scope.soldToOrderInfo.company;
                            	$scope.soldToOrderInfo.origAddress = $scope.soldToOrderInfo.address;
                            	$scope.soldToOrderInfo.origAddress2 = $scope.soldToOrderInfo.address2;
                            	$scope.soldToOrderInfo.origCity = $scope.soldToOrderInfo.city;
                            	$scope.soldToOrderInfo.origCountry = $scope.soldToOrderInfo.country;
                            	$scope.soldToOrderInfo.origState = $scope.soldToOrderInfo.state;
                            	$scope.soldToOrderInfo.origZip = $scope.soldToOrderInfo.zip;
                            $scope.$parent.setPopupEditAddress ($scope.soldToOrderInfo, data);
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