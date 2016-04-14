/**
 * Created by linanbin on 15/12/7.
 */
define([
    'angularAMD',
    'modules/cms/controller/popup.ctl'
], function (angularAMD) {

    angularAMD.controller('popAddStockIncrementCtl', function ($scope,data,taskStockIncrementService) {
        var task = data.task;
        var ctrl = data.ctrl;
        //定义常量
        $scope.vm = {
            //隔离渠道的ID
            incrementCartIdList:[],
            //隔离渠道ID
            incrementCartId:null,
            //隔离渠道的名称
            incrementCartName:null,
            //任务ID
            incrementTaskId:null,
            //增量任务ID
            incrementSubTaskId:null,
            //增量类型
            incrementType:null,
            //增量数值
            incrementValue:'',
            //任务名
            incrementTaskName:null,
            //新建增量库存隔离任务:1新规的场 2合更新的场合
            promotionType:null,
            //增量类型返回的类型值
            incrementTypeDis:null,
            //是否显示
            incrementShow:null
        };
        //判断增量任务:1新规的场 2合更新的场合
        if(task){
            $scope.initialize = function(){
                //增量任务:
                $scope.vm.promotionType="2";
                //增量任务ID
                $scope.vm.incrementSubTaskId=task.subTaskId;
                //增量任务名
                $scope.vm.incrementCartName=task.cartName;
                taskStockIncrementService.searchSubTask($scope.vm).then(
                    function (res) {
                        //增量类型
                        $scope.vm.incrementType=res.data.incrementType;
                        //增量值
                        $scope.vm.incrementValue=res.data.incrementValue;
                        //任务名称
                        $scope.vm.incrementTaskName=res.data.incrementTaskName;
                        //任务名称
                        $scope.vm.incrementCartId=res.data.incrementCartId;
                        //是否可编辑
                        $scope.vm.incrementShow=true;

                        if($scope.vm.incrementValue=="1"){
                            $scope.vm.incrementTypeDis='TXT_INCREMENT_PERCENTAGE';
                        }
                        if($scope.vm.incrementValue=="2"){
                            $scope.vm.incrementTypeDis='TXT_INCREMENT_CNT';
                        }
                    },
                    function (err) {
                        if (err.message != null) {
                            $scope.$close();
                        }
                    }
                );
            }
        }else{
            $scope.initialize = function(){
                //增量任务:1新规的场
                $scope.vm.promotionType="1";
                //取得对应的活动ID
                $scope.vm.incrementTaskId=data.ctrl.taskId;
                //隔离渠道的ID的信息
                angular.forEach(data.ctrl.platformList, function(val,key) {
                    if(val){
                        $scope.vm.incrementCartIdList.push(val);
                    }
                });
                //隔离渠道的ID
                $scope.vm.incrementCartId=$scope.vm.incrementCartIdList.cartId;
            }
        }
        //Save保存按钮
        $scope.saveTask =function(){
            taskStockIncrementService.saveTask($scope.vm).then(function(res){
                $scope.$close();
                ctrl.search();
            });
        }
    });
});