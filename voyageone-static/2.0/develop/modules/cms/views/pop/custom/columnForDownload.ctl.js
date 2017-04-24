/**
 * @description 高级检索自定义列
 * @author edward.
 * @date 2015-12-7
 */
define([
    'cms'
], function (cms) {

    cms.controller('popColumnForDownloadCtl', function($scope, $searchAdvanceService2, $modalInstance){

        $scope.vm = {
            customProps:[],
            commonProps:[]
        };

        $scope.initialize = function () {
            $searchAdvanceService2.getCustColumnsInfo().then(function (res) {
                console.log('返回叔叔',res);
                $scope.vm.customProps = res.data.customProps;
                $scope.vm.commonProps = res.data.commonProps;
                $scope.vm.salesTypeList = res.data.salesTypeList;
                $scope.vm.biDataList = res.data.biDataList;

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

    });

});