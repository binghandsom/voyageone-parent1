/**
 * Created by sofia on 2016/10/12.
 */
define([
    'cms',
    'modules/cms/controller/popup.ctl'
], function (cms) {

    cms.controller("SkuMoveConfirmController", (function () {

        function SkuMoveConfirm(context, popups, $uibModalInstance) {
            this.context = context;
            this.popups = popups;
            this.$uibModalInstance = $uibModalInstance;
            this.move;
            this.moveType = this.context == 'Sku' ? 'Sku' : 'Code';
        }

        SkuMoveConfirm.prototype.init = function () {
            window.setTimeout(focusMove,300);
            function focusMove() {
                angular.element(document.getElementsByName('moveInput')).focus();
            }
        };



        SkuMoveConfirm.prototype.confirm = function () {
            this.$uibModalInstance.close();
        };

        return SkuMoveConfirm;
    })());
});