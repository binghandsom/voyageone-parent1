/**
 * @description 平台默认属性设置一览
 * @date 2016-8-11
 */

define([
    'cms',
    'modules/cms/controller/popup.ctl'
], function (cms) {
    cms.controller('defaultAttributeController', (function () {

        function DefaultAttributeController($translate, popups) {
            this.$translate = $translate;
            this.popups = popups;
        }

        DefaultAttributeController.prototype = {
          init:function(){

          }
        };

        return DefaultAttributeController;
    })())
});