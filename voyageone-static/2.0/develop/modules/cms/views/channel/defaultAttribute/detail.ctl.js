/**
 * @description 平台默认属性设置一览=>新建默认设置
 * @date 2016-8-11
 */

define([
    'cms',
    'modules/cms/controller/popup.ctl'
], function (cms) {
    cms.controller('attributeDetailController', (function () {

        function AttributeDetailController($translate,$routeParams,popups,menuService) {

            var self = this;

            self.$translate = $translate;
            self.popups = popups;
            self.upEntity = angular.fromJson($routeParams.upEntity);
            self.menuService = menuService;
            self.searchInfo = {
                cartId:self.upEntity.cartId,
                categoryType:self.upEntity.categoryType,
                categoryId:self.upEntity.categoryId,
                categoryPath:self.upEntity.categoryPath
            };
        }

        AttributeDetailController.prototype = {
          init:function(){
              var self = this;
              self.menuService.getPlatformType().then(function(resp){
                  self.platformTypes = _.filter(resp,function(cart){return cart.value != 0 && cart.value != 1 }) ;
              });

          }
        };

        return AttributeDetailController;
    })())
});
