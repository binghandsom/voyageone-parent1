/**
 * Created by linanbin on 15/12/7.
 */

define([
    'modules/cms/controller/popup.ctl',
    'modules/cms/service/search.advance.service'
], function () {

    function searchIndex($scope, $routeParams, searchAdvanceService, feedMappingService, productDetailService, confirm, $translate, notify, alert) {

        $scope.vm = {
            searchInfo: {
                compareType: null,
                brand: null,
                promotion: null,
                tags:[]
            },
            groupPageOption: {curr: 1, total: 0, size: 20, fetch: getGroupList},
            productPageOption: {curr: 1, total: 0, size: 20, fetch: getProductList},
            groupList: [],
            productList: [],
            currTab: "group",
            status: {
                open: true
            },
            groupSelList: { selList: []},
            productSelList: { selList: []}
        };
        $scope.datePicker = [];

        $scope.initialize = initialize;
        $scope.clear = clear;
        $scope.search = search;
        $scope.exportFile = exportFile;
        $scope.getGroupList = getGroupList;
        $scope.getProductList = getProductList;
        $scope.openCategoryMapping = openCategoryMapping;
        $scope.bindCategory = bindCategory;

        /**
         * 初始化数据.
         */
        function initialize () {
            // 如果来至category 或者 header的检索,将初始化检索条件
            if ($routeParams.type == "1") {
                $scope.vm.searchInfo.catId = $routeParams.value;
            } else if ($routeParams.type == "2") {
                $scope.vm.searchInfo.codeList = $routeParams.value;
            }
            searchAdvanceService.init().then(function (res) {
                $scope.vm.masterData = res.data;
            })
            .then(function() {
                // 如果来至category 或者header search 则默认检索
                if ($routeParams.type == "1"
                    || $routeParams.type == "2")
                search();
            })
        }

        /**
         * 清空画面上显示的数据
         */
        function clear () {
            $scope.vm.searchInfo = {
                compareType: null,
                brand: null,
                promotion: null,
                tags:[]
            };
        }

        /**
         * 检索
         */
        function search () {
            // 默认设置成第一页
            $scope.vm.groupPageOption.curr = 1;
            $scope.vm.productPageOption.curr = 1;
            // 对应根据父类目检索
            var catInfo = getCatPath($scope.vm.searchInfo.catId);
            if (catInfo)
                $scope.vm.searchInfo.catPath = catInfo.catPath;
            else
                $scope.vm.searchInfo.catPath = null;
                    searchAdvanceService.search($scope.vm.searchInfo, $scope.vm.groupPageOption, $scope.vm.productPageOption)
                .then(function (res) {
                    $scope.vm.groupList = res.data.groupList;
                    $scope.vm.groupPageOption.total = res.data.groupListTotal;
                    $scope.vm.groupSelList = res.data.groupSelList;

                    $scope.vm.productList = res.data.productList;
                    $scope.vm.productPageOption.total = res.data.productListTotal;
                    $scope.vm.productSelList = res.data.productSelList;
            })
        }

        /**
         * 数据导出
         */
        function exportFile () {
            searchAdvanceService.exportFile($scope.vm.searchInfo);
        }

        /**
         * 分页处理group数据
         */
        function getGroupList () {

            searchAdvanceService.getGroupList($scope.vm.searchInfo, $scope.vm.groupPageOption, $scope.vm.groupSelList)
            .then(function (res) {
                $scope.vm.groupList = res.data.groupList == null ? [] : res.data.groupList;
                $scope.vm.groupPageOption.total = res.data.groupListTotal;
                $scope.vm.groupSelList = res.data.groupSelList;
            });
        }

        /**
         * 分页处理product数据
         */
        function getProductList () {

            searchAdvanceService.getProductList($scope.vm.searchInfo, $scope.vm.productPageOption, $scope.vm.productSelList)
                .then(function (res) {
                    $scope.vm.productList = res.data.productList == null ? [] : res.data.productList;
                    $scope.vm.productPageOption.total = res.data.productListTotal;
                    $scope.vm.productSelList = res.data.productSelList;
                });
        }

        function openCategoryMapping (popupNewCategory) {

            var selList = $scope.vm.currTab === 'group' ? $scope.vm.groupSelList.selList : $scope.vm.productSelList.selList;
            if (selList && selList.length) {

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
            } else {
                alert($translate.instant('TXT_MSG_NO_ROWS_SELECT'));
            }
        }

        function bindCategory (context) {

            confirm($translate.instant('TXT_MSG_CONFIRM_IS_CHANGE_CATEGORY')).result
                .then(function () {
                    var productIds = [];
                    _.forEach($scope.vm.currTab === 'group' ? $scope.vm.groupSelList.selList : $scope.vm.productSelList.selList
                        , function (object) {
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
                        // TODO 需要enka设计一个错误页面 res.data.publishInfo
                            notify($translate.instant('TXT_MSG_PRODUCT_IS_PUBLISHING'));
                    })
                });
        }

        function getCatPath (catId) {
            return _.findWhere($scope.vm.masterData.categoryList, {catId: catId});
        }
    };

    searchIndex.$inject = ['$scope', '$routeParams', 'searchAdvanceService', 'feedMappingService', '$productDetailService', 'confirm', '$translate', 'notify', 'alert'];
    return searchIndex;
});
