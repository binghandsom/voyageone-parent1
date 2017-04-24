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
        $scope.chkItemStatus = chkItemStatus;

        $scope.init = function () {
            $searchAdvanceService2.getCustColumnsInfo().then(function (res) {
                console.log('返回结果',res.data);
                $scope.vm.customProps = res.data.customProps;
                $scope.vm.commonProps = res.data.commonProps;
                $scope.vm.salesTypeList = res.data.salesTypeList;
                $scope.vm.biDataList = res.data.biDataList;
                $scope.vm.platformDataList = res.data.platformDataList;

                _.each($scope.vm.customProps, function (data) {
                    data.isChk = _.contains(res.data.custAttrList, data.feed_prop_original);
                });

                _.each($scope.vm.commonProps, function (data) {
                    data.isChk = _.contains(res.data.commList, data.propId);
                });

                _.each($scope.vm.salesTypeList, function (data) {
                    data.isChk = _.contains(res.data.selSalesTypeList, data.value);
                });

                _.each($scope.vm.biDataList, function (data) {
                    data.isChk = _.contains(res.data.selBiDataList, data.value);
                });

                _.each($scope.vm.platformDataList, function (item) {
                    item.isChk = _.contains(res.data.platformProps, item.value);
                });
                // 检查全选框
                for (var i = 1; i <= 6; i++) {
                    chkItemStatus(i);
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
                case 5:
                    tmpSource = $scope.vm.platformDataList;
                    break;
            }
            _.each(tmpSource, function (ele) {
                ele.isChk = $scope.vm[checkName];
            });

        };

        function chkItemStatus(stsType) {

            switch (stsType) {
                case 1:
                    $scope.vm.all_commonData = _.every($scope.vm.commonProps, function (item) {
                        return item.isChk;
                    });
                    break;
                case 2:
                    $scope.vm.all_customData = _.every($scope.vm.customProps, function (item) {
                        return item.isChk;
                    });
                    break;
                case 3:
                    $scope.vm.all_salesType = _.every($scope.vm.salesTypeList, function (item) {
                        return item.isChk;
                    });
                    break;
                case 4:
                    $scope.vm.all_biData = _.every($scope.vm.biDataList, function (item) {
                        return item.isChk;
                    });
                case 5:
                    $scope.vm.all_platformData = _.every($scope.vm.platformDataList, function (item) {
                        return item.isChk;
                    });
                    break;
            }

        }

        $scope.confirm = function () {

            $searchAdvanceService2.saveCustColumnsInfo({
                customProps: contructData($scope.vm.customProps, 'feed_prop_original'),
                commonProps: contructData($scope.vm.commonProps, 'propId'),
                selSalesTypeList: contructData($scope.vm.salesTypeList, 'value'),
                selBiDataList: contructData($scope.vm.biDataList, 'value'),
                platformProps: contructData($scope.vm.platformDataList, 'value')
            }).then(function () {
                $modalInstance.close();
            });
        };

        function contructData(dataList, attrName) {
            return _(dataList)
                .chain()
                .filter(function (item) {
                    return item.isChk;
                })
                .pluck(attrName)
                .value();
        }

    });

});