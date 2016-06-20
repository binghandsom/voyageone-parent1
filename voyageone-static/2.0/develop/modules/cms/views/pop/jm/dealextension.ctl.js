define([
    'angularAMD',
    'modules/cms/controller/popup.ctl'
], function (angularAMD) {
    angularAMD.controller('popDealExtensionCtl', function ($scope,jmPromotionDetailService,context,$routeParams) {
        $scope.model={};
        $scope.modelPromotion={};
        $scope.selectMinDate=null;
        $scope.initialize = function () {
            $scope.model.promotionId=context.id;
            $scope.modelPromotion=context;
            var myDate = new Date(context.activityEnd);
            myDate.setDate(myDate.getDate() + 1);//日期默认为昨天
            $scope.selectMinDate=myDate;
           // var Year = myDate.getFullYear();   //获取完整的年份(4位,1970-????)
          //  var Month = myDate.getMonth() + 1;      //获取当前月份(0-11,0代表1月)
          //  var Day = myDate.getDate();
            console.log(context);
        };

        $scope.ok=function() {
            //if (context.isBatch) {
            //    updateDealEndTime();
            //}
            //else {
                updateDealEndTimeAll();
            //}
        }
        var updateDealEndTime=function() {
            $scope.model.productIdList = context.getSelectedProductIdList();
            if (! $scope.model.productIdList ||  $scope.model.productIdList.length == 0) {
                $scope.$close();
                return;
            }
            jmPromotionDetailService.updateDealEndTime($scope.model).then(function () {
                $scope.$close();
            }, function (res) {
            })
        };

        var updateDealEndTimeAll=function(){
            jmPromotionDetailService.updateDealEndTimeAll( $scope.model).then(function () {
                //for (var i=$scope.vm.modelList.length-1;i>=0;i--) {
                //
                //    $scope.vm.modelList[i].synchState=1;
                //
                //}
                $scope.$close();
            }, function (res) {
            })
        };
    });
});
