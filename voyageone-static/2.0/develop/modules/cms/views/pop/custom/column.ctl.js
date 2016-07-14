/**
 * Created by linanbin on 15/12/7.
 */
define([
    'angularAMD'
], function (angularAMD) {

    angularAMD.controller('popCustomColumnCtl', function ($scope, $searchAdvanceService2, $modalInstance) {
        // 从后台取得的全部列
        $scope.cus = {
            customProps:[],
            commonProps:[]
        };

        /**
         * 初始化数据.
         */
        $scope.initialize = function () {
            $searchAdvanceService2.getCustColumnsInfo().then(function (res) {
                $scope.cus.customProps = res.data.customProps;
                $scope.cus.commonProps = res.data.commonProps;
                $scope.cus.salesTypeList = res.data.salesTypeList;
                _.forEach($scope.cus.customProps, function (data) {
                    data.isChk = _.contains(res.data.custAttrList, data.feed_prop_original);
                });
                _.forEach($scope.cus.commonProps, function (data) {
                    data.isChk = _.contains(res.data.commList, data.propId);
                });
                _.forEach($scope.cus.salesTypeList, function (data) {
                    data.isChk = _.contains(res.data.selSalesTypeList, data.value);
                });
                // 检查全选框
                var chkSts = false;
                if ($scope.cus.commonProps && $scope.cus.commonProps.length > 0) {
                    for (keyIdx in $scope.cus.commonProps) {
                        if (!$scope.cus.commonProps[keyIdx].isChk) {
                            chkSts = true;
                        }
                    }
                    $scope.cus.all_commonData = !chkSts;
                } else {
                    $scope.cus.all_commonData = false;
                }

                chkSts = false;
                if ($scope.cus.customProps && $scope.cus.customProps.length > 0) {
                    for (keyIdx in $scope.cus.customProps) {
                        if (!$scope.cus.customProps[keyIdx].isChk) {
                            chkSts = true;
                        }
                    }
                    $scope.cus.all_customData = !chkSts;
                } else {
                    $scope.cus.all_customData = false;
                }

                chkSts = false;
                if ($scope.cus.salesTypeList && $scope.cus.salesTypeList.length > 0) {
                    for (keyIdx in $scope.cus.salesTypeList) {
                        if (!$scope.cus.salesTypeList[keyIdx].isChk) {
                            chkSts = true;
                        }
                    }
                    $scope.cus.all_salesType = !chkSts;
                } else {
                    $scope.cus.all_salesType = false;
                }
            })
        };

        /**
         * 确定返回，数据传回前端及用户自定义保存
         */
        $scope.ok = function() {
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
            var params = {};
            params.customProps = customProps;
            params.commonProps = commonProps;
            params.selSalesTypeList = selSalesTypeList;
            $searchAdvanceService2.saveCustColumnsInfo(params).then(function() {
                $modalInstance.close('');
            });
        };

        $scope.close = function () {
            $modalInstance.dismiss();
        }

        // 全选框的操作
        $scope.chkSelStatus = function (stsType) {
            if (stsType == 1) {
                _.forEach($scope.cus.commonProps, function (data) {
                    data.isChk = $scope.cus.all_commonData;
                });
            } else if (stsType == 2) {
                _.forEach($scope.cus.customProps, function (data) {
                    data.isChk = $scope.cus.all_customData;
                });
            } else if (stsType == 3) {
                _.forEach($scope.cus.salesTypeList, function (data) {
                    data.isChk = $scope.cus.all_salesType;
                });
            }
        };

        // 勾选明细时对全选框的操作
        $scope.chkItemStatus = function (stsType) {
            var chkSts = false;
            if (stsType == 1) {
                for (keyIdx in $scope.cus.commonProps) {
                    if (!$scope.cus.commonProps[keyIdx].isChk) {
                        chkSts = true;
                    }
                }
                $scope.cus.all_commonData = !chkSts;
            } else if (stsType == 2) {
                for (keyIdx in $scope.cus.customProps) {
                    if (!$scope.cus.customProps[keyIdx].isChk) {
                        chkSts = true;
                    }
                }
                $scope.cus.all_customData = !chkSts;
            } else if (stsType == 3) {
                for (keyIdx in $scope.cus.salesTypeList) {
                    if (!$scope.cus.salesTypeList[keyIdx].isChk) {
                        chkSts = true;
                    }
                }
                $scope.cus.all_salesType = !chkSts;
            }
        };
    });

});