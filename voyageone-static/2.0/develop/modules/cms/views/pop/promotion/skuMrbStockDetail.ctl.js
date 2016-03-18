/**
 * Created by linanbin on 15/12/7.
 */
define([
    'cms'
], function (cms) {
    cms.controller('popSkuMrbStockDetailCtl', (function () {

        function PopSkuMrbStockDetailCtl(taskId, cartId, data, taskStockService) {
            this.taskId = taskId;
            this.cartId = cartId;
            this.model = data.model;
            this.code = data.code;
            this.sku = data.sku;
            this.stockHistoryList = [];
            this.taskStockService = taskStockService;
        }

        PopSkuMrbStockDetailCtl.prototype = {
            init: function () {
                var main = this;
                main.taskStockService.getSkuSeparationDetail({
                    "taskId": main.taskId,
                    "cartId": main.cartId,
                    "sku": main.sku
                }).then(function (res) {
                    main.stockHistoryList = res.data.stockHistoryList;
                });
            }
        };

        return PopSkuMrbStockDetailCtl;
    })());
});