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

            this.maindata = {
                category: {
                    id: this.context.mainCategoryId,
                    path: null
                }
            };

            this.platform = {
                category: {
                    id: this.context.platformCategoryId,
                    path: this.context.platformCategoryPath
                },
                property: this.context.property
            };

            /**
             * @type {number}
             */
            this.valueIndex = this.context.valueIndex;
        }

        MultiComplexItemMappingPopupController.prototype = {

            init: function () {

                var mainCate = this.maindata.category;

                this.ppPlatformMappingService.getMainCategoryPath(mainCate.id).then(function (path) {
                    mainCate.path = path;
                });
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