/**
 * Created by 123 on 2016/8/16.
 */
/**
 * @Description:切换主商品
 * @Author piao wenjie
 */

define([
    'cms'
], function (cms) {
    'use strict';
    return cms.controller('propertyMappingController', (function () {

        function PropertyMappingController(context,$uibModalInstance,popups) {
            this.context = context;
            this.uibModalInstance = $uibModalInstance;
            this.popups = popups;
        }

        PropertyMappingController.prototype = {
          init:function(){
              var self = this;
              console.log(self.context);
          },
          openPpPropertySetting:function(){
              var self = this;
              self.popups.openPropertySetting().then(function(){

              });
          }
        };

        return PropertyMappingController;

    })());
});