/**
 * @Description:切换主商品
 * @Author piao wenjie
 */

define([
    'cms',
    'modules/cms/enums/Carts'
], function (cms,carts) {
    'use strict';
    return cms.controller('SwitchMainController', (function () {

        function SwitchMain(context,$uibModalInstance,menuService,productDetailService,confirm,alert) {
            this.context = context;
           // this.carts = carts;
            this.uibModalInstance = $uibModalInstance;
            this.menuService = menuService;
            this.productDetailService = productDetailService;
            this.confirm = confirm;
            this.alert = alert;
            this.data = {
                platformTypes:null,
                productInList:null,
                initMainCode:null,
                curMainCode:null,
                mainProduct:null,
                mainCode:null,
                cart:null,
                numIId:null,
                selected:null
            }
        }

        SwitchMain.prototype = {
          init:function(){
              var self = this;
              self.data.selected = self.data.selected ? self.data.selected :self.context.cartId;
              self.productDetailService.getChangeMastProductInfo({cartId:self.data.selected , productCode:self.context.productCode}).then(function(res){
                  self.data.productInList = res.data.productInfoList;

                  if(self.data.productInList){
                      self.data.mainProduct = _.find(self.data.productInList,function(product){
                          return product.isMain;
                      });

                      /**一个group中的上心过的商品的numIId是一样的*/
                      self.data.numIId = self.data.productInList[0].numIId;
                      self.data.mainCode = angular.copy(self.data.mainProduct).productCode;
                      self.data.initMainCode = angular.copy(self.data.mainCode);
                      self.data.curMainCode = angular.copy(self.data.mainCode);
                  }

              });

              self.menuService.getPlatformType().then(function(resp){
                  self.data.platformTypes = _.filter(resp,function(item){
                      if(item.value > 1 && item.value != 27)
                        return true;
                  });
              });

              self.data.cart = _.find(carts,function(cart){
                 return cart.id == self.data.selected;
              });

          },
          transferMain:function(productCode){
              var self = this;
              if(self.data.curMainCode == productCode)
                return;

              self.confirm("确定切换店铺渠道:[" +self.data.cart.desc+"]的主商品为[" + productCode + "]吗？").then(function(){
                  self.data.curMainCode = self.data.mainCode;
              },function(){
                  self.data.mainCode = self.data.curMainCode;
              });
          },
          setMastProduct:function(){
              var self = this;
              self.confirm("您确定要执行切换主商品操作吗？").then(function(){
                  self.productDetailService.setMastProduct({cartId:self.data.selected,productCode:self.data.mainCode}).then(function(){
                      self.uibModalInstance.close();
                  },function(res){
                      if(res.code == 5)
                        self.alert("更新失败！");

                      self.data.mainCode = self.data.initMainCode;
                  });
              });
          }
        };

        return SwitchMain;

    })());
});