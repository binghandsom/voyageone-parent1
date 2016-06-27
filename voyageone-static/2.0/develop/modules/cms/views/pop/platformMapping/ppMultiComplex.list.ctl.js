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

            this.property = context.path[0];

            /**
             * 当前平台属性所对应的 Mapping
             * @type {MultiComplexMappingBean}
             */
            this.multiComplexMapping = null;
        }

        MultiComplexMappingPopupController.prototype = {

            init: function () {

                var $ = this;
                var $mainCate = $.context.maindata.category;
                var $platform = $.context.platform;
                var $cartId = $.context.cartId;
                var $service = $.ppService;

                $service.getPlatformPropertyMapping(
                    $.context.path, $mainCate.id, $platform.category.id, $cartId
                ).then(function (multiComplexMapping) {
                    $.multiComplexMapping = multiComplexMapping || $.$create();
                });
            },

            /**
             * @private
             * 创建一个新的 MultiComplexMappingBean
             */
            $create: function() {
                var mapping = new MultiComplexMappingBean();
                mapping.values = [];
                mapping.platformPropId = this.context.platform.property.id;
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
                this.$modal.dismiss();
            }
        };

        return MultiComplexMappingPopupController;

    })());
});