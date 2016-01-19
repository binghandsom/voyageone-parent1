define([
    'cms',
    'modules/cms/views/pop/platformMapping/ppPlatformMapping.serv',
    'modules/cms/controller/popup.ctl'
], function (cms) {
    'use strict';
    return cms.controller('simpleListMappingPopupController', (function () {

        function SimpleListMappingPopupController(context, $uibModalInstance, ppPlatformMappingService, alert) {

            this.$uibModalInstance = $uibModalInstance;
            this.context = context;
            this.ppPlatformMappingService = ppPlatformMappingService;
            this.alert = alert;

        }

        SimpleListMappingPopupController.prototype = {
            init: function () {

            },
            ok: function () {

            },
            cancel: function () {
                this.$uibModalInstance.dismiss('cancel');
            }
        };

        return SimpleListMappingPopupController;

    })());
});