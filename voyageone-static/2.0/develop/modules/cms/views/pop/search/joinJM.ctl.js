/**
 * Created by 123 on 2016/4/12.
 */
define([
    'underscore',
    'angularAMD',
    'modules/cms/controller/popup.ctl'
], function (_,angularAMD) {

    angularAMD.controller('popJoinJMCtl', function ($scope, $routeParams,context,jmPromotionProductAddService) {

        $scope.vm = _.extend({}, context);//包含promotion和selected products


        $scope.hasDiscount=true; //是否有折扣
        $scope.vm.discount = 1.0;//无折扣
        $scope.vm.priceType="1";
        $scope.discountForShow = 10;
        $scope.$watch('discountForShow', function (newVal, oldVal) {
            if(newVal>10 || newVal<0) {
               $scope.discountForShow=newVal>10?10:0;//设置合理的值
            }
            $scope.vm.discount=$scope.discountForShow/10;
        });

        /**
         * 初始化数据.
         */
        $scope.initialize = function () {

        };
        $scope.save = function () {
            jmPromotionProductAddService.add($scope.vm).then(function(resp) {
                console.log(resp);
            });
            $scope.$close();
        };

    });
});
