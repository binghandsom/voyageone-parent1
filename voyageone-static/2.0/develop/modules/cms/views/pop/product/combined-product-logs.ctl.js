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
                       logs:[],
                       logPageOption: {page: 1, total: 0, fetch: goPage.bind(this)}
                   };
                   $scope.vm.product = context.product;
                   $scope.vm.carts = context.carts;
                   $scope.vm.statuses = context.statuses;
                   $scope.vm.platformStatuses = context.platformStatuses;

                   $scope.initialize = function () {
                       getLogList();
                   };

                   //跳转指定页
                   function goPage(page, pageSize) {
                       var pageParameter = angular.copy($scope.vm.product);
                       pageParameter.page = page;
                       pageParameter.pageSize = pageSize;
                       combinedProductService.getOperateLogs(pageParameter).then(function (res) {
                           $scope.vm.logs = res.data.logs == null ? [] : res.data.logs;
                           $scope.vm.logPageOption.total = res.data.total;
                       }, function (res) {
                       })
                   };

                   /**
                    * 分页处理log数据
                    */
                   function getLogList() {
                       combinedProductService.getOperateLogs($scope.vm.product, $scope.vm.logPageOption)
                           .then(function (resp) {
                               $scope.vm.logs = resp.data.logs == null ? [] : resp.data.logs;
                               $scope.vm.logPageOption.total = resp.data.total;
                           });
                   }

               }

               return CombinedProductLogsController;

           })());
       }
);