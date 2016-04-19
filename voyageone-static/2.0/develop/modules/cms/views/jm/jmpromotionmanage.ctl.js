
define([
    'modules/cms/controller/popup.ctl'
], function () {
    function indexController($scope, jmPromotionService, confirm, $translate, cActions, notify, $location, cRoutes) {
        $scope.vm = {"modelList": [],
                    "jmMasterBrandList":[],
                    status: {
                        open: true
                    }};
        $scope.searchInfo = {};
        $scope.datePicker = [];
        $scope.initialize = function () {
            jmPromotionService.init().then(function (res) {
                $scope.vm.jmMasterBrandList = res.data.jmMasterBrandList;
                $scope.search();
            });
        };
        $scope.clear = function () {
            $scope.searchInfo = {};
        };
        $scope.search = function () {
            console.log("searchInfo");
           console.log($scope.searchInfo);

            for(var key in $scope.searchInfo)
            {
                if(!$scope.searchInfo[key])
                {
                    delete $scope.searchInfo[key];
                }
            }
            console.log($scope.searchInfo);
            jmPromotionService.getListByWhere($scope.searchInfo).then(function (res) {
                console.log(res);
                $scope.vm.modelList = res.data;
               // $scope.groupPageOption.total = $scope.vm.modelList.size;
            }, function (res) {
            })
        };
        $scope.del = function (data) {
            confirm($translate.instant('TXT_MSG_DO_DELETE') + data.name).result.then(function () {
                var index = _.indexOf($scope.vm.modelList, data);
                data.isDelete = 1;
                jmPromotionService.update(data).then(function () {
                    $scope.vm.modelList.splice(index, 1);
                }, function (res) {
                })
            })

        };
    }
    indexController.$inject = ['$scope', 'jmPromotionService', 'confirm', '$translate', 'cActions', 'notify', '$location', 'cRoutes'];
    return indexController;
});