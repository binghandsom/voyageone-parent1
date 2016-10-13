/**
 * Created by sofia on 2016/10/12.
 */
define([
    'cms'
], function (cms) {

    cms.controller("SkuMoveConfirmController", (function () {

        function SkuMoveConfirm(context) {
            this.context = context;
            this.moveType = this.context == 'Sku' ? 'Sku' : 'Code';
        }

        SkuMoveConfirm.prototype.init = function () {

        };

        return SkuMoveConfirm;
    })());
});