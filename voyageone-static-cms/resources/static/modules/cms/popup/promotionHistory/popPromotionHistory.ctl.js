/**
 * Created by sky on 2015/09/07
 */

define(function (require) {
    var cmsApp = require('modules/cms/cms.module');
    require('modules/cms/popup/promotionHistory/popPromotionHistory.service');
    cmsApp.controller('popPromotionHistoryController', ['$scope', 'popPromotionHistoryService', 'userService', '$modalInstance', 'parameters','DTOptionsBuilder', 'DTColumnBuilder', '$translate','$compile',
        function ($scope, popPromotionHistoryService, userService, $modalInstance, parameters, DTOptionsBuilder, DTColumnBuilder, $translate, $compile) {

            //参数Map
            var vm = $scope.vm = {};

            vm.cartId = parameters.cartId;
            vm.productId = parameters.productId;
            vm.channelId = userService.getSelChannel();

            //结果集Map
            var rm =$scope.rm = {};

            // 关闭对话框
            $scope.close = closeDialog;
            // 初始化
            $scope.init = init;

            function init() {
                createDt();
            }

            function search(data, draw){
                data.param = vm;
                popPromotionHistoryService.searchPromotionHistory(data).then(function (res) {
                    return draw(res)
                });
            }

            function closeDialog() {
                $modalInstance.dismiss('close');
            }

            function createDt () {
                $scope.promotionHistory = {
                    options: DTOptionsBuilder.newOptions()
                        .withOption('processing', true)
                        .withOption('serverSide', true)
                        .withOption('scrollY', '600px')
                        .withOption('scrollX', '100%')
                        .withOption('scrollCollapse', true)
                        .withOption('ajax', search)
                        .withOption('createdRow',  function(row) {
                            $compile(angular.element(row).contents())($scope);
                        })
                        //.withOption('headerCallback', function(header) {
                        //    if (!$scope.headerCompiledCn) {
                        //        // Use this headerCompiled field to only compile header once
                        //        $scope.headerCompiledCn = true;
                        //        $compile(angular.element(header).contents())($scope);
                        //    }
                        //})
                        .withDataProp('data')
                        .withPaginationType('full_numbers'),
                    columns: [
                        DTColumnBuilder.newColumn('name', $translate('CMS_TXT_NAME')).withClass('wtab-xsm').notSortable(),
                        DTColumnBuilder.newColumn('', $translate('CMS_TXT_EFFECTIVE_DATE')).renderWith(function(data, type, full){
                            return full.effectiveDateStart + ' - ' + full.effectiveDateEnd;
                        }).withClass('wtab-xs'),
                        DTColumnBuilder.newColumn('', $translate('CMS_TXT_PROMOTION_DISCOUNT_PERCENT')).withClass('wtab-sm').renderWith(function(data, type, full){
                            return full.discountSalePrice + '(' + full.discountPercent + '% Off)';
                        })
                    ],
                    dtInstance: null
                }
            }
        }]);

});