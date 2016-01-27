/**
 * complexMappingPopupController
 */

define([
    'cms',
    'modules/cms/models/ComplexMappingBean',
    'modules/cms/enums/FieldTypes',
    'modules/cms/views/pop/platformMapping/ppPlatformMapping.serv'
], function (cms, ComplexMappingBean, FieldTypes) {
    'use strict';

    return cms.controller('complexMappingPopupController', (function () {

        /**
         * Complex Mapping 弹出框的 Controller
         * @param {SimpleListMappingPopupContext} context
         * @param $uibModalInstance
         * @param {PopupPlatformMappingService} ppPlatformMappingService
         * @param notify 参见 Components/Angular/Factory/Notify
         * @constructor
         */
        function ComplexMappingPopupController(context, $uibModalInstance, ppPlatformMappingService, notify) {

            this.$uibModalInstance = $uibModalInstance;
            this.context = context;
            this.ppPlatformMappingService = ppPlatformMappingService;
            this.notify = notify;

            this.maindata = {
                category: {
                    id: this.context.mainCategoryId,
                    path: null
                }
            };

            this.platform = {
                property: this.context.property,
                category: {
                    id: this.context.platformCategoryId,
                    path: this.context.platformCategoryPath
                }
            };

            // 当前可选的所有属性
            this.options = {
                /**
                 * @type {Field[]}
                 */
                values: null
            };

            /**
             * @type {ComplexMappingBean}
             */
            this.complexMapping = null;
        }

        ComplexMappingPopupController.prototype = {
            init: function () {

                var mainCategory = this.maindata.category;

                // 加载类目路径
                this.ppPlatformMappingService.getMainCategoryPath(mainCategory.id).then(function (path) {
                    mainCategory.path = path;
                });

                // 尝试加载原有数据
                this.ppPlatformMappingService.getPlatformPropertyMapping(
                    this.platform.property, mainCategory.id, this.platform.category.id, this.context.cartId
                ).then(function (complexMapping) {

                    if (!complexMapping)
                    // 新建默认
                        complexMapping = new ComplexMappingBean(
                            this.platform.property.id,
                            null,
                            []
                        );

                    this.complexMapping = complexMapping;

                }.bind(this));

                this.loadValue();
            },
            loadValue: function () {
                this.ppPlatformMappingService
                    .getMainCategoryPropsWithSku(this.maindata.category.id)
                    .then(function (props) {
                        // 绑定所有属性
                        this.options.values = props;
                    }.bind(this));
            },
            ok: function () {

                var modal = this.$uibModalInstance;
                var platform = this.platform;
                var notify = this.notify;

                this.ppPlatformMappingService
                    .saveMapping(
                        this.maindata.category.id,
                        platform.category.id,
                        this.context.cartId,
                        this.complexMapping,
                        platform.property)
                    .then(function (updated) {
                        if (updated)
                            notify.success('已更新');
                        else
                            notify.warning('没有更新任何数据');
                        modal.close(updated);
                    });
            },
            cancel: function () {
                this.$uibModalInstance.dismiss('cancel');
            }
        };

        return ComplexMappingPopupController;

    })());
});