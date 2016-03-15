define([
    'cms'
], function (cms) {
    cms.controller('popNewMrbStockSkuCtl', (function () {

        function PopNewMrbStockSkuCtl(data, taskStockService, confirm, alert, notify) {
            this.alert = alert;
            this.notify = notify;
            this.confirm = confirm;
            this.taskId = data.taskId;
            this.platformList = data.platformList;
            this.taskStockService = taskStockService;
            this.model;
            this.code;
            this.sku;
            this.usableStock;
            this.platformStockList;
        }

        PopNewMrbStockSkuCtl.prototype = {
            getUsableStock: function () {
                var main = this;
                main.taskStockService.getUsableStock({
                    "taskId": main.task_id,
                    "sku": main.sku
                }).then(function (res) {
                    main.usableStock = res.data.usableStock;
                });
            },

            saveNewRecord: function () {
                var main = this;
                main.taskStockService.saveNewRecord({
                    "taskId": main.task_id,
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