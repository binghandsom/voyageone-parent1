/**
 * Created by linanbin on 15/12/7.
 */
define([
    'angularAMD'
], function (angularAMD) {

    angularAMD.controller('popNewMrbStockCtl', function ($scope,taskStockService,promotionIdList) {

        $scope.vm = {
            //根据活动ID取得数据隔离渠道的名称
            getInitPromotionCartNameList:null
        };

        //数据初始化
        $scope.initialize = function(){
            taskStockService.initNewTask(promotionIdList).then(function (res) {
                $scope.vm.getInitPromotionCartNameList = res.data;
            },function (err) {
                    if (err.message != null) {
                        $scope.$close();
                    }
                }
            );
        };

        //Save保存按钮
        $scope.saveTask =function(){
            taskStockService.saveTask($scope.vm).then(function(res){
                notify.success ($translate.instant('TXT_MSG_UPDATE_SUCCESS'));
                //$scope.$close();
                $modalInstance.close('');
            })
        }
    });
});