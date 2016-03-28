/**
 * Created by linanbin on 15/12/7.
 */
define([
    'angularAMD'
], function (angularAMD) {

    angularAMD.controller('popNewMrbStockCtl', function ($scope,taskStockService,promotionIdList,alert) {

        $scope.vm = {
            //根据活动ID取得数据隔离渠道的名称
            promotionList:{},
            isChoicePromotionIdList:[]
        };
        //判断是否选择活动
        if(promotionIdList){
            angular.forEach(promotionIdList.selFlag, function(val,key) {
                if(val){
                    $scope.vm.isChoicePromotionIdList.push({key,val});
                }
            });
        }else{
            $scope.vm.isChoicePromotionIdList=null;
        };
        //数据初始化
        $scope.initialize = function(){
            if($scope.vm.isChoicePromotionIdList!=null&&$scope.vm.isChoicePromotionIdList.length!=0){
                taskStockService.initNewTask(promotionIdList).then(
                    function (res) {
                        $scope.vm.promotionList = res.data;
                    },
                    function (err) {
                        if (err.message != null) {
                            $scope.$close();
                        }
                    }
                );
            }else{
                $scope.$close();
                alert('请选择对应的活动');
            }
        };
        //Save保存按钮
        $scope.saveTask =function(){
            taskStockService.saveTask($scope.vm).then(function(res){
            })
        }
    });
});