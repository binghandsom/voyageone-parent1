define([
    'cms',
    'underscore'
], function (cms) {
    cms.controller('popNewMrbStockSkuCtl', (function () {

        function PopNewMrbStockSkuCtl(data, taskStockService, confirm, alert, notify, $uibModalInstance) {
            this.parent = data;
            this.alert = alert;
            this.notify = notify;
            this.confirm = confirm;
            this.taskId = data.taskId;
            this.$uibModalInstance = $uibModalInstance;
            this.platformStockList = [];
            this.propertyStockList = [];
            var main = this;
            _.each( data.platformList, function(platform) {
                var obj = {};
                obj.cartId = platform.cartId;
                obj.cartName = platform.cartName;
                obj.qty = "";
                obj.dynamic = false;
                main.platformStockList.push(obj);
            });
            _.each( data.propertyList, function(property) {
                var obj = {};
                obj.property = property.property;
                obj.name = property.name;
                obj.value = "";
                main.propertyStockList.push(obj);
            });
            this.taskStockService = taskStockService;
            this.model = "";
            this.code = "";
            this.sku = "";
            this.usableStock = "";
        }

        PopNewMrbStockSkuCtl.prototype = {
            getUsableStock: function () {
                var main = this;
                main.taskStockService.getUsableStock({
                    "sku": main.sku
                }).then(function (res) {
                    main.usableStock = res.data.usableStock;
                });
            },

            saveNewRecord: function () {
                var main = this;
                main.taskStockService.saveNewRecord({
                    "taskId": main.taskId,
                    "model": main.model,
                    "code": main.code,
                    "sku": main.sku,
                    "usableStock": main.usableStock,
                    "propertyStockList": main.propertyStockList,
                    "platformStockList": main.platformStockList
                }).then(function (res) {
                    main.notify.success('TXT_MSG_INSERT_SUCCESS');
                    main.$uibModalInstance.close();
                    main.parent.search();
                }, function (err) {
                    if (err.displayType == null) {
                        main.alert('TXT_MSG_INSERT_FAIL');
                    }
                });
            },
            cancel: function () {
                this.$uibModalInstance.close();
            }
        };

        return PopNewMrbStockSkuCtl;
    })());
});