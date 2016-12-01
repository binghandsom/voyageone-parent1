/**
 * Created by rex.wu on 2016/12/1.
 */
/**
 * Created by rex.wu on 2016/11/30.
 */
define([
           "cms"
       ], function (cms) {
           cms.controller("CombinedProductLogsController", (function () {

               function CombinedProductLogsController($scope, context, combinedProductService) {
                   $scope.vm = {
                       product:{},
                       carts:{},
                       statuses:{},
                       platformStatuses:{},
                       logs:[]
                   };
                   $scope.vm.product = context.product;
                   $scope.vm.carts = context.carts;
                   $scope.vm.statuses = context.statuses;
                   $scope.vm.platformStatuses = context.platformStatuses;

                   $scope.initialize = function () {
                       combinedProductService.getOperateLogs($scope.vm.product).then(function (resp) {
                           $scope.vm.logs = resp.data.logs == null ? [] : resp.data.logs;
                       });
                   };
               }

               return CombinedProductLogsController;

           })());
       }
);