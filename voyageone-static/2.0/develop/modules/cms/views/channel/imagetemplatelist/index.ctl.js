
define([
    'modules/cms/controller/popup.ctl'
], function () {
    function indexController($scope, imageTemplateService, confirm, $translate, cActions, notify, $location, cRoutes) {
        $scope.vm = {"modelList": [],
            "platformList":[],
            "brandNameList":[],
            "productTypeList":[],
            "sizeTypeList":[],
            "imageTemplateList":[]
            };
        $scope.dataPageOption = {curr: 0, total: 0, size: 10, fetch: goPage.bind(this)}
        $scope.searchInfo = { };
        $scope.datePicker = [];
        $scope.initialize = function () {
            $scope.clear();
            imageTemplateService.init().then(function (res) {
                $scope.vm.platformList = res.data.platformList;
                $scope.vm.brandNameList = res.data.brandNameList;
                $scope.vm.productTypeList = res.data.productTypeList;
                $scope.vm.sizeTypeList = res.data.sizeTypeList;
                $scope.vm.imageTemplateList=res.data.imageTemplateList;
             //   $scope.search();
            })
        };
        $scope.search = function () {
            //获取查询条件
            var data = getSearchData();

            //获取总数量
            imageTemplateService.getCount(data).then(function (res) {
                $scope.dataPageOption.total = res.data;
            }, function (res) {
            });

            //跳转首页
            $scope.dataPageOption.setPageIndex(1);
        };
        function  getSearchData() {
            var data = angular.copy($scope.searchInfo);
            var length = $scope.vm.platformList.length;
            var platformList = $scope.vm.platformList;
            var cartIdList = [];
            for (var i = 0; i < length; i++) {
                if (platformList[i].show) {
                    cartIdList.push(parseInt(platformList[i].value))
                }
            }
            data.cartIdList = cartIdList;
            return data;
        }
        function  goPage(pageIndex,pageSize) {
            var data = getSearchData();
            data.pageIndex = pageIndex;
            data.pageSize = pageSize;
            imageTemplateService.getPage(data).then(function (res) {
                $scope.vm.modelList = res.data;
            }, function (res) {
            });
        }
        $scope.clear = function () {
            var length = $scope.vm.platformList.length;
            var platformList = $scope.vm.platformList;

            for (var i = 0; i < length; i++) {
                if (platformList[i].show) {
                    platformList[i].show=false;
                }
            }
            $scope.searchInfo = {brandName:[],sizeType:[],productType:[]};
        };
        $scope.del = function (data) {
            confirm($translate.instant('TXT_MSG_DO_DELETE') + data.imageTemplateName).result.then(function () {
                var index = _.indexOf($scope.vm.modelList, data);
                data.active = 0;
                imageTemplateService.delete(data.imageTemplateId).then(function () {
                    $scope.vm.modelList.splice(index, 1);
                }, function (res) {
                })
            })
        };
    }
    indexController.$inject = ['$scope', 'imageTemplateService', 'confirm', '$translate', 'cActions', 'notify', '$location', 'cRoutes'];
    return indexController;
});