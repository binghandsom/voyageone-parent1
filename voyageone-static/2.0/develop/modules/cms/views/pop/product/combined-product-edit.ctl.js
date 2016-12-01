/**
 * Created by rex.wu on 2016/11/30.
 */
define([
           "cms",
           "modules/cms/controller/popup.ctl"
       ], function (cms) {
           cms.controller("CombinedProductEditController", (function () {

               function CombinedProductEditController($scope, context, combinedProductService, popups, confirm) {
                   $scope.vm = {
                       config:{
                           showFlag: true
                       },
                       carts:{},
                       product:{}
                   };
                   $scope.vm.carts = context.carts;

                   $scope.initialize = function () {
                       combinedProductService.getCombinedProductDetail(context.product).then(function (resp) {
                           $scope.vm.product = resp.data.product == null ? {} : resp.data.product;
                           if ($scope.vm.product != null) {
                               $scope.vm.product.cartId = $scope.vm.product.cartId + "";
                           }
                           _.each($scope.vm.product.skus, function (element, index, list) {
                               _.extend(element, {'tempSuitSellingPriceCn': element.suitSellingPriceCn}, {'tempSuitPreferentialPrice':element.suitPreferentialPrice})
                           });
                       });
                   };


                   $scope.getCombinedProductInfo = function () {
                       var cartId = $scope.vm.product.cartId;
                       var numID = $scope.vm.product.numID;
                       if(!cartId || !numID) {
                           return;
                       }
                       combinedProductService.getCombinedProductDetail({"cartId" : cartId, "numID" : numID}).then(function (resp) {
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
                       var suitSellingPriceCn = 0;
                       _.each(sku.skuItems, function (item, index, list) {
                           suitSellingPriceCn += item.sellingPriceCn;
                       });
                       sku.tempSuitSkuCode = suitSellingPriceCn;

                   }

                   $scope.dynamicPrice = function (sku) {
                       dynamicSkuPrice(sku);
                   };

                   $scope.editSubmit = function (status) {
                       combinedProductService.edit(_.extend($scope.vm.product, {"status": status})).then(function (resp) {
                           $scope.$close();
                       });
                   };
               }

               return CombinedProductEditController;

           })());
       }
);