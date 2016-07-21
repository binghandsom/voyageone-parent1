/**
 * @Description:商品下线
 * @Author piao wenjie
 */

define([
    'cms',
    'modules/cms/enums/Carts'
], function (cms,carts) {
    'use strict';
    return cms.controller('ProductOffLineController', (function () {

        function ProductOffLine(context,$uibModalInstance,menuService,productDetailService,confirm,alert) {
            this.context = context;
           // this.carts = carts;
            this.uibModalInstance = $uibModalInstance;
            this.menuService = menuService;
            this.productDetailService = productDetailService;
            this.confirm = confirm;
            this.alert = alert;
            this.data = {
                platformTypes: null,
                productInList: null,
                mainProduct: null,
                mainCode: null,
                cart: null,
                numIId: null,
                comment:""
            }
        }

        ProductOffLine.prototype = {
          init:function(){
              var self = this;
              self.productDetailService.getChangeMastProductInfo({cartId:self.context.cartId , productCode:self.context.productCode}).then(function(res){
                  self.data.productInList = res.data.productInfoList;

                  if(self.data.productInList){
                      self.data.mainProduct = _.find(self.data.productInList,function(product){
                          return product.isMain;
                      });

                      /**一个group中的上心过的商品的numIId是一样的*/
                      self.data.numIId = self.data.productInList[0].numIId;
                      self.data.mainCode = angular.copy(self.data.mainProduct).productCode;
                  }

              });

              self.menuService.getPlatformType().then(function(resp){
                  self.data.platformTypes = _.filter(resp,function(item){
                      if(item.value > 1 && item.value != 27)
                        return true;
                  });
              });

              self.data.cart = _.find(carts,function(cart){
                 return cart.id == self.context.cartId;
              });

          },
          delisting:function(){
              var self = this;
              self.confirm("确定下线店铺渠道:[" +self.data.cart.desc+"]的商品为[" + self.context.productCode + "]吗？").then(function(){
                  self.productDetailService.delisting({cartId:self.context.cartId ,
                                                  productCode:self.context.productCode,
                                                      comment:self.data.comment
                  }).then(function(){
                      self.uibModalInstance.close();
                  },function(res){
                      if(res.code == 5)
                          self.alert("更新失败！");
              });
              });

          }
        };

        return ProductOffLine;

    })());
});