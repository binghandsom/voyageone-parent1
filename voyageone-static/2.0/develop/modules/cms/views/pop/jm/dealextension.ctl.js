define([
    'angularAMD',
    'modules/cms/controller/popup.ctl'
], function (angularAMD) {
    angularAMD.controller('popDealExtensionCtl', function ($scope,jmPromotionDetailService,context,$routeParams) {
        $scope.model={};
        $scope.initialize = function () {
            $scope.model.promotionId=context.promotionId;
        };

        $scope.ok=function() {
            if (context.isBatch) {
                updateDealEndTime();
            }
            else {
                updateDealEndTimeAll();
            }
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
