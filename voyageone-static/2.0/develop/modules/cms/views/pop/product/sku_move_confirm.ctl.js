/**
 * Created by sofia on 2016/10/12.
 */
define([
    'cms'
], function (cms) {

    cms.controller("SkuMoveConfirmController", (function () {

        function SkuMoveConfirm(context,$uibModalInstance) {
            this.$uibModalInstance = $uibModalInstance;
            this.context = context;
            this.flag = false;
        }

        SkuMoveConfirm.prototype.init = function () {
            var self = this;

            if(!self.context)
                return;

            if(!self.context[0])
                return;

            if(angular.equals(self.context[0].priceRetail,self.context[0].confPriceRetail)){
                self.equal = true;
            }
        };

        return SkuMoveConfirm;
    })());
});