/**
 * Created by linanbin on 15/12/7.
 */
define([
    'angularAMD',
    'modules/cms/controller/popup.ctl'
], function (angularAMD) {

    angularAMD.controller('popAddStockIncrementCtl', function ($scope,data,taskStockIncrementService) {
        //Save保存按钮
        $scope.saveTask =function(){
            taskStockIncrementService.saveTask($scope.vm).then(function(res){
            });
            $scope.$close();
        }
    });
});