/**
 * Created by linanbin on 15/12/7.
 */

define([
    'modules/cms/controller/popup.ctl',
    'modules/cms/service/groupList.service'
], function () {

    function groupListController($scope, $routeParams, $translate, groupListService, feedMappingService, productDetailService, notify, confirm) {

        $scope.vm = {
            masterData: null,
            productList: [],
            productIds: [],
            productPageOption: {curr: 1, total: 0, size: 20, fetch: getProductList},
            mainProduct: null
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
            groupListService.init($routeParams.id, $scope.vm.productPageOption)
                .then(function (res) {
                    $scope.vm.masterData = res.data.masterData;
                    $scope.vm.productList = res.data.productList;
                    $scope.vm.productPageOption.total = res.data.productListTotal;
                    $scope.vm.productIds = res.data.productIds;
                });
        }

        /**
         * 获取该group下的productList数据,主要用于翻页
         */
        function getProductList () {

            groupListService.getProductList($routeParams.id, $scope.vm.productPageOption)
                .then(function (res) {
                    $scope.vm.productList = res.data.productList;
                    $scope.vm.productPageOption.total = res.data.productListTotal;
                    $scope.vm.productIds = res.data.productIds;
                });
        }

        /**
         * 设置group的主商品
         */
        function setMainProduct (product) {

            groupListService.setMainProduct({groupId: product.groups.platforms[0].groupId, prodId: product.prodId}).then(function () {
                notify.success ($translate.instant('TXT_COM_UPDATE_SUCCESS'));
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
                            notify.success($translate.instant('TXT_COM_UPDATE_SUCCESS'));
                            $scope.search();
                        }
                        else
                            // TODO 需要enka设计一个错误页面 res.data.publishInfo
                            notify("有商品处于上新状态,不能切换类目");

                    })
                });
        }
    }

    groupListController.$inject = ['$scope', '$routeParams', '$translate', 'groupListService', 'feedMappingService', '$productDetailService', 'notify', 'confirm'];
    return groupListController;
});
