/**
 * @description 平台默认属性设置一览=>新建默认设置
 * @date 2016-8-11
 */

define([
    'cms',
    'modules/cms/controller/popup.ctl'
], function (cms) {
    cms.controller('attributeDetailController', (function () {

        function AttributeDetailController($translate, popups) {
            this.$translate = $translate;
            this.popups = popups;
        }

        AttributeDetailController.prototype = {
            init:function(){
                alert("detail!");
            }
        };

        return AttributeDetailController;
    })())
});
