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
            this.$uibModalInstance = $uibModalInstance;
            this.ppPlatformMappingService = ppPlatformMappingService;

            this.mainCategoryPath = null;
            this.mainCategoryId = this.context.mainCategoryId;

            this.platformProperty = this.context.property;
            /**
             * com.voyageone.cms.service.bean.MultiComplexCustomMappingValue
             */
            this.value = this.context.value;
        }

        MultiComplexItemMappingPopupController.prototype = {

            init: function () {

                this.ppPlatformMappingService.getMainCategoryPath(this.mainCategoryId).then(function (path) {
                    this.mainCategoryPath = path;
                }.bind(this));
            },

            mapping: function(ppPlatformMapping, property) {

                var context = _.clone(this.context);

                context.property = property;

                ppPlatformMapping.simple.list(context);
            },

            ok: function () {
                this.cancel();
            },
            cancel: function () {
                this.$uibModalInstance.dismiss('cancel');
            }
        };

        return MultiComplexItemMappingPopupController;

    })());
});