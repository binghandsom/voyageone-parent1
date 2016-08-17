define([
    'cms'
], function (cms) {
    'use strict';
    return cms.controller('propertySettingController', (function () {

        function PropertySettingController(context,$uibModalInstance) {
            this.context = context;
            this.uibModalInstance = $uibModalInstance;
        }

        PropertySettingController.prototype = {
            init:function(){
                //初始化操作
            },
            save:function(){
                var self = this;
                var newValue = {
                  cat:self.cat,
                  value:self.fixValue
                };

                self.uibModalInstance.close(newValue);
            }
        };

        return PropertySettingController;

    })());
});