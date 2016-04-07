/**
 * Created by linanbin on 15/12/7.
 */
define([
    'angularAMD',
    'modules/cms/controller/popup.ctl'
], function (angularAMD) {

    angularAMD.controller('popAddStockIncrementCtl', function ($scope,data,taskStockIncrementService) {
        //定义常量
        $scope.vm = {
            //根据活动ID取得数据隔离渠道的名称
            promotionList:{},
        }
        $scope.vm.promotionList = data;
        //Save保存按钮
        $scope.saveTask =function(){
            taskStockIncrementService.saveTask($scope.vm).then(function(res){
            });
            $scope.$close();
        }
    });
});