define([
    'cms',
    'underscore',
    'modules/cms/models/MultiComplexMappingBean',
    'modules/cms/views/pop/platformMapping/ppPlatformMapping.serv',
    'modules/cms/controller/popup.ctl'
], function (cms, _, MultiComplexMappingBean) {
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
            this.$modal = $uibModalInstance;
            this.ppService = ppPlatformMappingService;

            _.extend(this, context);

            this.platform.property = context.path[0];

            /**
             * 当前平台属性所对应的 Mapping
             * @type {MultiComplexMappingBean}
             */
            this.multiComplexMapping = null;
        }

        MultiComplexMappingPopupController.prototype = {

            init: function () {

                var $mainCate = this.maindata.category;
                var $platform = this.platform;
                var $cartId = this.context.cartId;
                var $service = this.ppService;

                $service.getMainCategoryPath($mainCate.id).then(function (path) {
                    $mainCate.path = path;
                });

                $service.getPlatformPropertyMapping(
                    $platform.property, $mainCate.id, $platform.category.id, $cartId
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
                var newIndex = this.multiComplexMapping.values.length;
                this.context.path.unshift(newIndex);
                ppPlatformMapping(this.context);
            },

            edit: function($index, ppPlatformMapping) {
                this.context.path.unshift($index);
                ppPlatformMapping(this.context);
            },

            ok: function () {
                this.cancel();
            },
            cancel: function () {
                this.$modal.dismiss('cancel');
            }
        };

        return MultiComplexMappingPopupController;

    })());
});