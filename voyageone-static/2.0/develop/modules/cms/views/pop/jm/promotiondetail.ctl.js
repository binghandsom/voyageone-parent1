
/**
 * Created by linanbin on 15/12/7.
 */
define([
    'angularAMD',
    'modules/cms/controller/popup.ctl'
], function (angularAMD) {

    angularAMD.controller('popPromotionDetailCtl', function ($scope,jmPromotionService,confirm,$translate) {
        $scope.vm = {"jmMasterBrandList":[]};
        $scope.model = {};
        $scope.datePicker = [];
        $scope.initialize  = function () {
            jmPromotionService.init().then(function (res) {
                $scope.vm.jmMasterBrandList = res.data.jmMasterBrandList;
            });
        };
        $scope.ok = function(){
            console.log("save");
            console.log($scope.model);
            if(!$scope.model.id) {
                jmPromotionService.insert($scope.model).then(function (res) {

                    $scope.$close();
                }, function (res) {
                })
            }else{
                jmPromotionService.update($scope.model).then(function (res) {
                    for (key in $scope.promotion) {
                        items[key] = $scope.promotion[key];
                    }
                    $scope.$close();
                }, function (res) {
                })
            }
        }
    });
});