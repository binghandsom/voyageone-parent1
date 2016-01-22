define([
    'cms',
    'modules/cms/views/pop/platformMapping/ppPlatformMapping.serv',
    'modules/cms/controller/popup.ctl'
], function (cms) {
    'use strict';
    return cms.controller('multiComplexMappingPopupController', (function () {

        function MultiComplexMappingPopupController(context, $uibModalInstance, ppPlatformMappingService) {

            this.context = context;
            this.$uibModalInstance = $uibModalInstance;
            this.ppPlatformMappingService = ppPlatformMappingService;

            this.mainCategoryPath = null;
            this.mainCategoryId = this.context.mainCategoryId;
        }

        MultiComplexMappingPopupController.prototype = {

            init: function() {

                this.ppPlatformMappingService.getMainCategoryPath(this.mainCategoryId).then(function (path) {
                    this.mainCategoryPath = path;
                }.bind(this));
            },

            ok: function () {
                this.cancel();
            },
            cancel: function () {
                this.$uibModalInstance.dismiss('cancel');
            }
        };

        return MultiComplexMappingPopupController;

    })());
});