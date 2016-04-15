/**
 * Created by linanbin on 15/12/7.
 */
define([
    'angularAMD'
], function (angularAMD) {

    angularAMD.controller('popNewMrbStockCtl', function ($scope,taskStockService,data,alert) {
        //定义常量
        $scope.vm = {
            //根据活动ID取得数据隔离渠道的名称
            promotionList:{},
            onlySku:null,
            //选择的活动ID
            selFlag:{},
            //已经选择的活动ID
            isChoicePromotionIdList:[],
            //判断隔离任务:1新规的场 2合更新的场合
            promotionType:null,
            //取得的TaskID
            taskId:null,
            //取得的TaskID
            taskName:null,
            //还原时间
            revertTime:null,
            //updateFlag
            updateFlag:null
        };
        if(data){
            //判断隔离任务:1新规的场 2合更新的场合
            if(data.task_id){
                //判断隔离任务:更新的场合
                $scope.vm.promotionType="2";
                taskStockService.initNewTask($scope.vm).then(
                    function (res) {
                        $scope.vm.promotionList = res.data.platformList;
                        $scope.vm.onlySku = true;
                        $scope.vm.revertTime=res.data.revertTime;
                    }
                );
                $scope.vm.taskId=data.task_id;
                $scope.vm.taskName=data.task_name;
            }else{
                $scope.vm.promotionType="1";
                //取得选择的活动ID
                angular.forEach(data.selFlag, function(val,key) {
                    if(val){
                        $scope.vm.selFlag[key]=val;
                        $scope.vm.isChoicePromotionIdList.push({key,val});
                    }
                });
                $scope.initialize = function(){
                    if($scope.vm.isChoicePromotionIdList!=null&&$scope.vm.isChoicePromotionIdList.length!=0){
                        //判断隔离任务:新规的场合
                        taskStockService.initNewTask($scope.vm).then(
                            function (res) {
                                $scope.vm.promotionList = res.data.platformList;
                                $scope.vm.onlySku = res.data.onlySku;
                                $scope.vm.taskName=res.data.taskName;
                                $scope.vm.revertTime=res.data.revertTime;
                    },
                            function (err) {
                                if (err.message != null) {
                                    $scope.$close();
                                }
                            }
                        );
                    }else{
                        $scope.$close();
                        alert('TXT_MSG_ACTIVITIES');
                    }
                }　;
            }

            //Save保存按钮
            $scope.saveTask =function(){
                taskStockService.saveTask($scope.vm).then(function(res){
                    $scope.$close();
                });

            }

        }else{
            $scope.initialize = function(){
                $scope.$close();
                alert('TXT_MSG_ACTIVITIES');
            }
        }
    });
});