define([
    'cms',
    'modules/cms/models/MultiComplexMappingBean',
    'modules/cms/views/pop/platformMapping/ppPlatformMapping.serv',
    'modules/cms/controller/popup.ctl'
], function (cms, MultiComplexMappingBean) {
    'use strict';
    return cms.controller('multiComplexMappingPopupController', (function () {

        /**
         *
         * @param {SimpleListMappingPopupContext} context
         * @param $uibModalInstance
         * @param {PopupPlatformMappingService} ppPlatformMappingService
         * @constructor
         */
        function MultiComplexMappingPopupController(context, $uibModalInstance, ppPlatformMappingService) {

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
             * 当前平台属性所对应的 Mapping
             * @type {MultiComplexMappingBean}
             */
            this.multiComplexMapping = null;
        }

        MultiComplexMappingPopupController.prototype = {

            init: function () {

                var mainCate = this.maindata.category;
                var platform = this.platform;
                var cartId = this.context.cartId;

                this.ppPlatformMappingService.getMainCategoryPath(mainCate.id).then(function (path) {
                    mainCate.path = path;
                });

                this.ppPlatformMappingService.getPlatformPropertyMapping(
                    platform.property, mainCate.id, platform.category.id, cartId
                ).then(function (multiComplexMapping) {
                    this.multiComplexMapping = multiComplexMapping || this.$create();
                }.bind(this));
            },

            /**
             * @private
             * 创建一个新的 MultiComplexMappingBean
             */
            $create: function() {
                var mapping = new MultiComplexMappingBean();
                mapping.values = [];
                mapping.platformPropId = this.platform.property.id;
                return mapping;
            },

            add: function (ppPlatformMapping) {
                // 打开子窗
                this.context.value = null;
                ppPlatformMapping.multiComplex.item(this.context);
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