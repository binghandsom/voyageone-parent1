/**
 * Created by 123 on 2016/4/13.
 */
define([
    'angularAMD',
    'underscore',
    'modules/cms/controller/popup.ctl'
], function (angularAMD, _) {

    angularAMD.controller('popCartListCtl', ['$scope', '$routeParams', 'context', 'systemCartService', '$modalInstance',
        function ($scope, $routeParams, context, cartService, $modalInstance) {

            $scope.CART_TYPES = [
                ['1', '中国店铺'],
                ['2', '国外店铺'],
                ['3', 'MiniMall']
            ];

            //$scope.PLATFORMS = [
            //    {
            //        "platform_id": 1,
            //        "platform": "TB",
            //        "comment": "淘宝平台"
            //    },
            //    {
            //        "platform_id": 2,
            //        "platform": "JD",
            //        "comment": "京东平台"
            //    },
            //    {
            //        "platform_id": 3,
            //        "platform": "CN",
            //        "comment": "独立域名"
            //    },
            //    {
            //        "platform_id": 4,
            //        "platform": "JM",
            //        "comment": "聚美优品"
            //    },
            //    {
            //        "platform_id": 5,
            //        "platform": "OF",
            //        "comment": "线下平台"
            //    }
            //];

            $scope.PLATFORM_DICT=context.PLATFORM_DICT;


            $scope.vm = _.clone(context.el||{});
            $scope.is_add = !(context|| {}).hasOwnProperty("el");

            /**
             * 初始化数据.
             */
            $scope.initialize = function () {

            };
            $scope.saveOrUpdate = function () {
                if (!$scope.vm.cart_id || !/[0-9]+/.test($scope.vm.cart_id) ||  !$scope.vm.name || !$scope.vm.short_name
                    || !$scope.vm.platform_id || !$scope.vm.cart_type) {
                    return ; //表单不完整
                }
                var invok=$scope.is_add?cartService.save:cartService.saveOrUpdate; //根据是否新增调用方法
                invok($scope.vm).then(function () {
                    $modalInstance.close($scope.vm);
                });
            };
        }]);
});
