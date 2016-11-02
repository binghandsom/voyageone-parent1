define(['cms'],
    function (cms) {
        'use strict';
        function ConfirmProductRefreshController($uibModalInstance, platformMappingService, notify, context) {
            this.$uibModalInstance = $uibModalInstance;
            this.platformMappingService = platformMappingService;
            this.notify = notify;
            this.context = context;
        }

        ConfirmProductRefreshController.prototype = {
            refresh: function (isAll) {
                var notify = this.notify;
                var modal = this.$uibModalInstance;
                var field = this.context.field;
                var mappingInfo = this.context.mappingInfo;
                this.platformMappingService.refreshProducts({
                    cartId: mappingInfo.cartId,
                    categoryPath: mappingInfo.categoryPath,
                    categoryType: mappingInfo.categoryType,
                    channelId: mappingInfo.channelId,
                    fieldId: field ? field.id : null,
                    allProduct: isAll
                }).then(function () {
                    modal.close();
                    notify.success('已发送冲刷通知。后续任务将进入后台进行。请等待。')
                });
            },

            close: function () {
                this.$uibModalInstance.dismiss();
            }
        };

        return cms.controller('ConfirmProductRefreshController', ['$uibModalInstance', 'platformMappingService', 'notify', 'context', ConfirmProductRefreshController]);
    });