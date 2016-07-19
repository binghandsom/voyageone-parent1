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
        }

        SwitchMain.prototype = {
          init:function(){
              alert("a");
          }
        };

        return SwitchMain;

    })());
});