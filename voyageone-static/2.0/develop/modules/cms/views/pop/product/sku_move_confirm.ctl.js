/**
 * Created by sofia on 2016/10/12.
 */
define([
    'cms'
], function (cms) {

    cms.controller("SkuMoveConfirmController", (function () {

        function SkuMoveConfirm(context, $uibModalInstance) {
            this.context = context;
            this.$uibModalInstance = $uibModalInstance;
            this.move;
            this.moveType = this.context == 'Sku' ? 'Sku' : 'Code';
        }

        SkuMoveConfirm.prototype.init = function () {

        };

        SkuMoveConfirm.prototype.confirm = function () {
            this.$uibModalInstance.close();
        };

        return SkuMoveConfirm;
    })());
});