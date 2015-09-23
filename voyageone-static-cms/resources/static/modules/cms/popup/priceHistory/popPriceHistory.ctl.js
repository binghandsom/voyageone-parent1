/**
 * Created by sky on 2015/09/07
 */

define(function (require) {
    var cmsApp = require('modules/cms/cms.module');
    require('modules/cms/popup/priceHistory/popPriceHistory.service');
    cmsApp.controller('popPriceHistoryController', ['$scope', 'popPriceHistoryService', 'userService', '$modalInstance', 'parameters','DTOptionsBuilder', 'DTColumnBuilder', '$translate','$compile',
        function ($scope, popPriceHistoryService, userService, $modalInstance, parameters, DTOptionsBuilder, DTColumnBuilder, $translate, $compile) {

            var commonUtil = require ('components/util/commonUtil');

            //参数Map
            var vm = $scope.vm = {};
            //vm.cartId = parameters.cartId;
            //vm.productId = parameters.productId;
            //vm.channelId = userService.getSelChannel();

            // 关闭对话框
            $scope.close = closeDialog;
            // 初始化
            $scope.init = init;

            function init() {
                createDT();
            }

            function closeDialog() {
                $modalInstance.dismiss('close');
            }

            function doGetPriceHistory(data, draw) {
                data.param = parameters;
                data.param.channelId = userService.getSelChannel();
                popPriceHistoryService.doGetPriceHistory(data).then(function (res) {
                    return draw(res)
                });
            }
            /**
             * 创建 DataTable.
             */
            function createDT () {
                $scope.doGetPriceHistory = {
                    options: DTOptionsBuilder.newOptions()
                        .withOption('processing', true)
                        .withOption('serverSide', true)
                        .withOption('scrollY', '400px')
                        .withOption('scrollX', '100%')
                        .withOption('scrollCollapse', true)
                        .withOption('ajax', doGetPriceHistory)
                        .withOption('createdRow',  function(row) {
                            $compile(angular.element(row).contents())($scope);
                        })
                        .withDataProp('data')
                        .withPaginationType('full_numbers'),
                    columns: [
                        DTColumnBuilder.newColumn('price', $translate('CMS_TXT_PROMOTION_PRICE')).withClass('wtab-xs text-right'),
                        DTColumnBuilder.newColumn('', $translate('CMS_TXT_COMMENT')).withClass('wtab-xsm').renderWith(function (val, type, row, cell) {
                            var comment = commonUtil.isNotEmpty(row.comment) ? row.comment : '';
                            return ('<span title="'+comment+'">'+comment.substring(0,15)+'</span>');
                        }),
                        DTColumnBuilder.newColumn('modified', $translate('CMS_TXT_LAST_UPDATED_ON')).withClass('wtab-sm'),
                        DTColumnBuilder.newColumn('modifier', $translate('CMS_TXT_LAST_UPDATED_BY')).withClass('wtab-sm')
                    ],
                    dtInstance: null
                }
            }

        }]);

});