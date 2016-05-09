
define([
    'modules/cms/controller/popup.ctl'
], function () {
    function indexController($scope, imageTemplateService, confirm, $translate, cActions, notify, $location, cRoutes) {
        $scope.vm = {"modelList": [],
            "platformList":[],
            "brandNameList":[],
            "productTypeList":[],
            "sizeTypeList":[],
            status: {
                open: true
            }};
        $scope.searchInfo = { };
        $scope.datePicker = [];
        $scope.initialize = function () {
            imageTemplateService.imageGroupService.init().then(function (res) {
                $scope.vm.platformList = res.data.platformList;
                $scope.vm.brandNameList = res.data.brandNameList;
                $scope.vm.productTypeList = res.data.productTypeList;
                $scope.vm.sizeTypeList = res.data.sizeTypeList;
                $scope.search();
            })
        };
        $scope.clear = function () {
            $scope.searchInfo = {brandName:[],sizeType:[],productType:[]};
        };
        $scope.search = function () {
            imageTemplateService.getPage($scope.searchInfo).then(function (res) {
                $scope.vm.modelList = res.data;
            }, function (res) {
            })
        };
        $scope.del = function (data) {
            confirm($translate.instant('TXT_MSG_DO_DELETE') + data.name).result.then(function () {
                var index = _.indexOf($scope.vm.modelList, data);
                data.active = 0;
                imageTemplateService.update(data).then(function () {
                    $scope.vm.modelList.splice(index, 1);
                }, function (res) {
                })
            })
        };
    }
    indexController.$inject = ['$scope', 'imageTemplateService', 'confirm', '$translate', 'cActions', 'notify', '$location', 'cRoutes'];
    return indexController;
});