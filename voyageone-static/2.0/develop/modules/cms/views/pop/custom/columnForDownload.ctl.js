/**
 * @description 高级检索自定义列
 * @author edward.
 * @date 2015-12-7
 */
define([
    'cms'
], function (cms) {

    cms.controller('popColumnForDownloadCtl', function ($scope, $searchAdvanceService2, $modalInstance) {

        $scope.vm = {
            customProps: [],
            commonProps: []
        };

        $scope.init = function () {
            $searchAdvanceService2.getCustColumnsInfo().then(function (res) {

                $scope.vm.customProps = res.data.customProps;
                $scope.vm.commonProps = res.data.commonProps;
                $scope.vm.salesTypeList = res.data.salesTypeList;
                $scope.vm.biDataList = res.data.biDataList;
                $scope.vm.platformDataList = res.data.platformDataList;

                _.forEach($scope.vm.customProps, function (data) {
                    data.isChk = _.contains(res.data.custAttrList, data.feed_prop_original);
                });
                _.forEach($scope.vm.commonProps, function (data) {
                    data.isChk = _.contains(res.data.commList, data.propId);
                });
                _.forEach($scope.vm.salesTypeList, function (data) {
                    data.isChk = _.contains(res.data.selSalesTypeList, data.value);
                });
                _.forEach($scope.vm.biDataList, function (data) {
                    data.isChk = _.contains(res.data.selBiDataList, data.value);
                });

                // 检查全选框
                var chkSts = false;
                if ($scope.vm.commonProps && $scope.vm.commonProps.length > 0) {
                    for (keyIdx in $scope.vm.commonProps) {
                        if (!$scope.vm.commonProps[keyIdx].isChk) {
                            chkSts = true;
                        }
                    }
                    $scope.vm.all_commonData = !chkSts;
                } else {
                    $scope.vm.all_commonData = false;
                }

                chkSts = false;
                if ($scope.vm.customProps && $scope.vm.customProps.length > 0) {
                    for (keyIdx in $scope.vm.customProps) {
                        if (!$scope.vm.customProps[keyIdx].isChk) {
                            chkSts = true;
                        }
                    }
                    $scope.vm.all_customData = !chkSts;
                } else {
                    $scope.vm.all_customData = false;
                }

                chkSts = false;
                if ($scope.vm.salesTypeList && $scope.vm.salesTypeList.length > 0) {
                    for (keyIdx in $scope.vm.salesTypeList) {
                        if (!$scope.vm.salesTypeList[keyIdx].isChk) {
                            chkSts = true;
                        }
                    }
                    $scope.vm.all_salesType = !chkSts;
                } else {
                    $scope.vm.all_salesType = false;
                }

                chkSts = false;
                if ($scope.vm.biDataList && $scope.vm.biDataList.length > 0) {
                    for (keyIdx in $scope.vm.biDataList) {
                        if (!$scope.vm.biDataList[keyIdx].isChk) {
                            chkSts = true;
                        }
                    }
                    $scope.vm.all_biData = !chkSts;
                } else {
                    $scope.vm.all_biData = false;
                }
            })
        };

        /**全选框的操作*/
        $scope.chkSelStatus = function (stsType, checkName) {
            var tmpSource = [];
            switch (stsType) {
                case 1:
                    tmpSource = $scope.vm.commonProps;
                    break;
                case 2:
                    tmpSource = $scope.vm.customProps;
                    break;
                case 3:
                    tmpSource = $scope.vm.salesTypeList;
                    break;
                case 4:
                    tmpSource = $scope.vm.biDataList;
                    break;
            }
            _.each(tmpSource, function (ele) {
                ele.isChk = $scope.vm[checkName];
            });

        };

        $scope.chkItemStatus = function (stsType) {

            switch (stsType){
                case 1:
                    $scope.vm.all_commonData = _.every($scope.vm.commonProps, function (item) {
                        return item.isChk === true;
                    });
                    break;
                case 2:
                    $scope.vm.all_customData = _.every($scope.vm.customProps, function (item) {
                        return item.isChk === true;
                    });
                    break;
                case 3:
                    $scope.vm.all_salesType = _.every($scope.vm.salesTypeList, function (item) {
                        return item.isChk === true;
                    });
                    break;
                case 4:
                    $scope.vm.all_biData = _.every($scope.vm.biDataList, function (item) {
                        return item.isChk === true;
                    });
                    break;
            }

        };

        $scope.confirm = function () {
            var customProps = [];
            _.forEach($scope.cus.customProps, function (data) {
                if (data.isChk != undefined && data.isChk) {
                    customProps.push(data.feed_prop_original);
                }
            });
            var commonProps = [];
            _.forEach($scope.cus.commonProps, function (data) {
                if (data.isChk != undefined && data.isChk) {
                    commonProps.push(data.propId);
                }
            });
            var selSalesTypeList = [];
            _.forEach($scope.cus.salesTypeList, function (data) {
                if (data.isChk != undefined && data.isChk) {
                    selSalesTypeList.push(data.value);
                }
            });
            var selBiDataList = [];
            _.forEach($scope.cus.biDataList, function (data) {
                if (data.isChk != undefined && data.isChk) {
                    selBiDataList.push(data.value);
                }
            });

            var params = {};
            params.customProps = customProps;
            params.commonProps = commonProps;
            params.selSalesTypeList = selSalesTypeList;
            params.selBiDataList = selBiDataList;
            $searchAdvanceService2.saveCustColumnsInfo(params).then(function () {
                $modalInstance.close('');
            });
        }

    });

});