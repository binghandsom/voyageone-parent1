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
                productInList:null,
                mainCode:null
            }
        }

        SwitchMain.prototype = {
          init:function(){
              var self = this;
              self.productDetailService.getChangeMastProductInfo({cartId:self.context.cartId,productCode:self.context.productCode}).then(function(res){
                  self.data.productInList = res.data.productInList;

                  if(self.data.productInList){
                      self.data.mainCode = _.find(self.data.productInList,function(product){
                          return product.isMain;
                      }).productCode;
                  }

              });
          }
        };

        return SwitchMain;

    })());
});