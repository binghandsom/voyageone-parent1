define([
    'cms'
], function (cms,carts) {
    'use strict';
    return cms.controller('propertySettingController', (function () {

        function PropertySettingController(context,$uibModalInstance) {
            this.context = context;
            this.uibModalInstance = $uibModalInstance;
        }


        return PropertySettingController;

    })());
});