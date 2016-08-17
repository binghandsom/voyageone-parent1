/**
 * Created by 123 on 2016/8/16.
 */
/**
 * @Description:切换主商品
 * @Author piao wenjie
 */

define([
    'cms',
    'modules/cms/enums/Carts'
], function (cms,carts) {
    'use strict';
    return cms.controller('propertyMappingController', (function () {

        function PropertyMappingController(context,$uibModalInstance,popups) {
            this.context = context;
            this.uibModalInstance = $uibModalInstance;
            this.popups = popups;
            this.valueArr = [];
        }

        PropertyMappingController.prototype = {
          init:function(){
              var self = this;
              if(self.context.cartId)
                self.context.cartName = carts.valueOf(+self.context.cartId).desc;
          },
          openPpPropertySetting:function(){
              var self = this;
              self.popups.openPropertySetting(self.context).then(function(context){
                  //{cat,value}
                  self.valueArr.push(context)
              });
          },
          order:function(arrow){

          },
          remove:function(index){
              this.valueArr.splice(index, 1)
          }
        };

        return PropertyMappingController;

    })());
});