define([
    'cms',
    'underscore'
], function (cms) {
    cms.controller('popNewMrbStockSkuCtl', (function () {

        function PopNewMrbStockSkuCtl(data, taskStockService, confirm, alert, notify) {
            this.alert = alert;
            this.notify = notify;
            this.confirm = confirm;
            this.taskId = data.taskId;
            this.platformStockList = [];
            var main = this;
            _.each( data.platformList, function(platform) {
                var obj = {};
                obj.cartId = platform.cartId;
                obj.cartName = platform.cartName;
                obj.qty = "";
                obj.dynamic = false;
                main.platformStockList.push(obj);
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
                    "platformStockList": main.platformStockList
                }).then(function (res) {
                    main.notify.success('更新成功');
                }, function (err) {
                    //main.alert('TXT_MSG_UPDATE_FAIL');
                });
            },
        };

        return PopNewMrbStockSkuCtl;
    })());
});