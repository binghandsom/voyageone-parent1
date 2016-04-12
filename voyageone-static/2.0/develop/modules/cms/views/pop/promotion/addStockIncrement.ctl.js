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
            //隔离渠道的ID
            incrementCartIdList:[],
            //隔离渠道的ID
            incrementCartId:null,
            //增量任务ID
            incrementTaskId:null,
            //增量类型
            incrementType:null,
            //增量数值
            incrementValue:'',
            //任务名
            incrementTaskName:null,
            //新建增量库存隔离任务:1新规的场 2合更新的场合
            promotionType:null
        }
        //判断增量任务:1新规的场 2合更新的场合
        if(data.sub_task_id){
            $scope.vm.promotionType="2";
        }else{
            //增量任务:1新规的场
            $scope.vm.promotionType="1";
            //取得对应的活动ID
            $scope.vm.incrementTaskId=data.taskId;
            //隔离渠道的ID的信息
            angular.forEach(data.platformList, function(val,key) {
                if(val){
                    $scope.vm.incrementCartIdList.push(val);
                }
            });
            //隔离渠道的ID
            $scope.vm.incrementCartId=$scope.vm.incrementCartIdList.cartId;
        }
        //Save保存按钮
        $scope.saveTask =function(){
            taskStockIncrementService.saveTask($scope.vm).then(function(res){
            });
            $scope.$close();
        }
    });
});