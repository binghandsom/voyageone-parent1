/**
 * Created by linanbin on 15/12/7.
 */
define([
    'angularAMD'
], function (angularAMD) {

    angularAMD.controller('popNewMrbStockCtl', function ($scope,taskStockService,promotionIdList) {
        //取得pop画面穿过的值
        $scope.promotionIdList = promotionIdList;
        $scope.promotionList = {};
        //数据初始化
        $scope.initialize = function(){
            taskStockService.initNewTask(promotionIdList).then(function (res) {
                $scope.promotionList = res.data.platformTypeList;
                $scope.search();
            });
        }
    });
});