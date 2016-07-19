/**
 *
 */

define([
    'cms'
], function (cms) {
    'use strict';
    return cms.controller('SwitchMainController', (function () {

        function SwitchMain(context, $uibModalInstance,productDetailService) {
            this.context = context;
            this.uibModalInstance = $uibModalInstance;
            this.productDetailService = productDetailService;
            this.data = {
                productInList:null
            }
        }

        SwitchMain.prototype = {
          init:function(){
              var self = this;
              self.productDetailService.getChangeMastProductInfo({cartId:self.context.cartId,productCode:self.context.productCode}).then(function(res){
                  self.data.productInList = res.data.productInList;
              });
          }
        };

        return SwitchMain;

    })());
});