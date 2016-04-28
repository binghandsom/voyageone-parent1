define([
    'cms',
    'underscore',
    'modules/cms/views/pop/platformMapping/ppPlatformMapping.serv'
], function (cms, _) {
    'use strict';
    return cms.controller('multiComplexItemMappingPopupController', (function () {

        /**
         *
         * @param context
         * @param $uibModalInstance
         * @param {PopupPlatformMappingService} ppPlatformMappingService
         * @constructor
         */
        function MultiComplexItemMappingPopupController(context, $uibModalInstance, ppPlatformMappingService) {

            this.context = context;
            this.$modal = $uibModalInstance;
            this.ppService = ppPlatformMappingService;

            this.property = context.path[1];
        }

        MultiComplexItemMappingPopupController.prototype = {

            mapping: function(ppPlatformMapping, property) {
                this.context.path.unshift(property);
                ppPlatformMapping(this.context);
            },

            ok: function () {
                this.cancel();
            },

            cancel: function () {
                this.context.path.shift();
                this.$modal.dismiss();
            }
        };

        return MultiComplexItemMappingPopupController;

    })());
});