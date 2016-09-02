/**
 * @Description:税号切换
 * @Author piao wenjie
 */

define([
    'cms'
], function (cms) {
    'use strict';
    return cms.controller('HsCodeChangeController', (function () {

        function HsCodeChange(context,$uibModalInstance,productDetailService) {
            this.context = context;
            this.uibModalInstance = $uibModalInstance;
            this.productDetailService = productDetailService;
            this.vm = {
                result:[]
            }
        }

        HsCodeChange.prototype.init = function(){
            this.vm.result = this.context.results;
        };

        HsCodeChange.prototype.update = function(mark){
            this.uibModalInstance.close(mark);
        };

        return HsCodeChange;

    })());
});
