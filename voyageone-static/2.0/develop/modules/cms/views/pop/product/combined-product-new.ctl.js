/**
 * Created by rex.wu on 2016/11/24.
 */
define([
           "cms",
           "modules/cms/controller/popup.ctl"
       ], function (cms) {
           cms.controller("CombinedProductNewController", (function () {

               function CombinedProductNewController($scope, context, combinedProductService, popups, confirm) {
                   $scope.vm = {
                       config:{
                           open:true,
                           showFlag: false
                       },
                       carts:{},
                       product:{}
                   };
                   $scope.vm.carts = context.carts;

                   $scope.getCombinedProductInfo = function () {
                       var cartId = $scope.vm.product.cartId;
                       var numID = $scope.vm.product.numID;
                       if(!cartId || !numID) {
                           return;
                       }
                       combinedProductService.getCombinedProductPlatformDetail({"cartId" : cartId, "numID" : numID}).then(function (resp) {
                           $scope.vm.config.showFlag = true;
                           $scope.vm.product = resp.data.product == null ? {} : resp.data.product;
                           // carts集合中cart为string, product为int
                           $scope.vm.product.cartId = $scope.vm.product.cartId == null ? "" : $scope.vm.product.cartId + "";
                       });
                   };
                   
                   $scope.getSkuDetail = function (sku, skuItem) {
                       if (!skuItem || !skuItem.skuCode) {
                           return;
                       }
                       combinedProductService.getSkuDetail({"skuCode":skuItem.skuCode, "cartId":$scope.vm.product.cartId}).then(function (resp) {
                            _.extend(skuItem, resp.data.skuItem == null ? {} : resp.data.skuItem);
                            // 统计组合套装中国最终售价
                           dynamicSkuPrice(sku);
                       });
                   };
                   
                   $scope.copySkuItem = function (skuItems) {
                       if(!skuItems || skuItems.length ==0)
                           return;
                       skuItems.push({});
                   };

                   $scope.deleteSkuItem = function (sku, index) {
                       if(!sku || !sku.skuItems || sku.skuItems.length <=1) {
                           return;
                       }
                       sku.skuItems.splice(index, 1);
                       dynamicSkuPrice(sku);
                   };

                   function dynamicSkuPrice(sku) {
                       // 动态统计套装组合SKU【组合套装优惠售价 合计】
                       var tempSuitPreferentialPrice =  0 ;
                       if(!sku || !sku.skuItems || sku.skuItems.length ==0) {
                           tempSuitPreferentialPrice = 0;
                       }else {
                           _.each(sku.skuItems, function (element, index, list) {
                               tempSuitPreferentialPrice += element.preferentialPrice;
                           });
                       }
                       sku.warn = sku.suitPreferentialPrice != tempSuitPreferentialPrice;
                       sku.tempSuitPreferentialPrice = tempSuitPreferentialPrice;
                       // 动态统计套装组合SKU【组合套装中国最终售价 合计】
                       var tempSuitSellingPriceCn = 0;
                       _.each(sku.skuItems, function (item, index, list) {
                           tempSuitSellingPriceCn += item.sellingPriceCn;
                       });
                       sku.tempSuitSellingPriceCn = tempSuitSellingPriceCn;

                   }

                   $scope.dynamicPrice = function (sku) {
                       dynamicSkuPrice(sku);
                   };

                   $scope.addSubmit = function (status) {
                       combinedProductService.add(_.extend($scope.vm.product, {"status": status})).then(function (resp) {
                           $scope.$close();
                       });
                   };

                   /**
                    * 模板展示
                    * @type {{content: string, templateUrl: string, title: string}}
                    */
                   $scope.dynamicPopover = {
                       templateUrl: 'example.html',
                       title: '事例页面'
                   };
               }

               return CombinedProductNewController;

           })());
       }
);

