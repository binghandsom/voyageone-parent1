/**
 * Created by tony-piao on 2016/5/5.
 */
define([
    'cms'
], function (cms) {

    cms.controller("ApproveConfirmController", (function () {

        function ApproveConfirm(context,$uibModalInstance) {
            this.$uibModalInstance = $uibModalInstance;
            this.context = context;
            this.flag = false;
        }

        ApproveConfirm.prototype.init = function () {
            var self = this;

            if(!self.context)
                return;

            if(!self.context[0])
                return;

            if(angular.equals(self.context[0].priceRetail,self.context[0].confPriceRetail)){
                self.equal = true;
            }
        };

       ApproveConfirm.prototype.confirm = function(){
            this.$uibModalInstance.close(this.flag);
       };

        return ApproveConfirm;
    })());
});