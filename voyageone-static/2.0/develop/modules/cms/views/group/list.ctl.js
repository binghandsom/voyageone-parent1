/**
 * Created by linanbin on 15/12/7.
 */

define([
    'cms',
    'modules/cms/controller/popup.ctl',
    'modules/cms/service/groupList.service'
], function (cms) {

    function groupListController($scope, $routeParams, groupListService, notify) {

        $scope.vm = {
            masterData: null,
            productList: [],
            productIds: [],
            productPageOption: {curr: 1, total: 0, size: 20, fetch: getProductList}
        };

        $scope.initialize = initialize;
        $scope.getProductList = getProductList;
        $scope.setMainProduct = setMainProduct;

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
                    this.vm.productList = res.data.productList;
                    this.vm.productPageOption.total = res.data.productListTotal;
                    this.vm.productIds = res.data.productIds;
                });
        }

        /**
         * 设置group的主商品
         */
        function setMainProduct (productId) {
            // TODO该功能还未实现
            //groupListService.setMainProduct(productId).then(function () {
            //    notify.success (this.translate.instant('TXT_COM_UPDATE_SUCCESS'));
            //})
        }
    }

    groupListController.$inject = ['$scope', '$routeParams', 'groupListService'];
    return groupListController;
});
