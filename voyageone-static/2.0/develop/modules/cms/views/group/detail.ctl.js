/**
 * Created by linanbin on 15/12/7.
 */

define([
    'modules/cms/controller/popup.ctl',
    'modules/cms/service/group.detail.service'
], function () {

    function detailController($scope, $routeParams, $translate, groupDetailService, feedMappingService, productDetailService, notify, confirm, alert) {

        $scope.vm = {
            masterData: null,
            productList: [],
            productIds: [],
            //productPageOption: {curr: 1, total: 0, fetch: getProductList},
            groupInfo: null
        };

        $scope.initialize = initialize;
        $scope.getProductList = getProductList;
        $scope.setMainProduct = setMainProduct;
        $scope.openCategoryMapping = openCategoryMapping;
        $scope.bindCategory = bindCategory;

        /**
         * 初始化数据:获取masterdata数据及该group下的productList数据
         */
        function initialize () {
            groupDetailService.init($routeParams.id)
                .then(function (res) {
                    $scope.vm.masterData = res.data.masterData;
                    $scope.vm.productList = res.data.productList;
                    $scope.vm.productListCopy = angular.copy($scope.vm.productList);
                    //$scope.vm.productPageOption.total = res.data.productListTotal;
                    $scope.vm.productIds = res.data.productIds;
                    $scope.vm.groupInfo = res.data.groupInfo;
                });
        }

        /**
         * 获取该group下的productList数据,主要用于翻页
         */
        function getProductList () {

            groupDetailService.getProductList($routeParams.id)
                .then(function (res) {
                    $scope.vm.productList = res.data.productList;
                    $scope.vm.productListCopy = angular.copy($scope.vm.productList);
                    //$scope.vm.productPageOption.total = res.data.productListTotal;
                    $scope.vm.productIds = res.data.productIds;
                    $scope.vm.groupInfo = res.data.groupInfo;
                });
        }

        /**
         * 设置group的主商品
         */
        function setMainProduct (code) {

            confirm($translate.instant('TXT_MSG_CONFIRM_CHANGE_MASTER_PRODUCT')).result
                .then(function () {
                    groupDetailService.setMainProduct({groupId: $scope.vm.groupInfo.groupId, mainProductCode: code}).then(function () {
                        notify.success ($translate.instant('TXT_MSG_UPDATE_SUCCESS'));
                    });
                }, function () {
                    $scope.vm.productList = angular.copy($scope.vm.productListCopy);
                })
        }

        function openCategoryMapping (popupNewCategory) {

            feedMappingService.getMainCategories()
                .then(function (res) {

                    popupNewCategory({

                        categories: res.data,
                        from: null
                    }).then( function (res) {
                            bindCategory (res)
                        }
                    );
                });
        }

        function bindCategory (context) {

            confirm($translate.instant('TXT_MSG_CONFIRM_IS_CHANGE_CATEGORY')).result
                .then(function () {
                    var productIds = [];
                    _.forEach($scope.vm.productIds, function (object) {
                            productIds.push(object.id);
                        });
                    var data = {
                        prodIds: productIds,
                        catId: context.selected.catId,
                        catPath: context.selected.catPath
                    };
                    productDetailService.changeCategory(data).then(function (res) {
                        if(res.data.isChangeCategory) {
                            notify.success($translate.instant('TXT_MSG_UPDATE_SUCCESS'));
                            $scope.search();
                        }
                        else
                            notify($translate.instant('TXT_MSG_PRODUCT_IS_PUBLISHING'));

                    })
                });
        }
    }

    detailController.$inject = ['$scope', '$routeParams', '$translate', 'groupDetailService', 'feedMappingService', '$productDetailService', 'notify', 'confirm', 'alert'];
    return detailController;
});
