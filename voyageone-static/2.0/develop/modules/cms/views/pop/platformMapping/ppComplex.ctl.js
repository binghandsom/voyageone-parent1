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
         * @param {ComplexMappingPopupContext} context
         * @param $uibModalInstance
         * @param {PopupPlatformMappingService} ppPlatformMappingService
         * @param alert
         * @constructor
         */
        function ComplexMappingPopupController(context, $uibModalInstance, ppPlatformMappingService, alert) {

            this.$uibModalInstance = $uibModalInstance;
            // context 中的平台类目路径将直接绑定到画面,这里没有显式操作
            this.context = context;
            this.ppPlatformMappingService = ppPlatformMappingService;
            this.alert = alert;

            // 主类目路径
            this.mainCategoryPath = null;
            // 主类目 ID
            this.mainCategoryId = this.context.mainCategoryId;
            // 当前选中的属性 ID
            this.selected = {
                /**
                 * @type {Field|null}
                 */
                value: null
            };
            // 当前可选的所有属性
            this.options = {
                /**
                 * @type {Field[]}
                 */
                values: null
            };
        }

        ComplexMappingPopupController.prototype = {
            init: function () {

                if (this.context.property.type !== FieldTypes.complex) {
                    this.alert('当前属性不是 Complex 属性').result.then(function() {
                        this.cancel();
                    }.bind(this));
                    return;
                }

                // 先加载主数据类目的路径
                this.ppPlatformMappingService.getMainCategoryPath(this.mainCategoryId).then(function (path) {
                    this.mainCategoryPath = path;
                    // 之后加载所有属性
                    this.loadValue();
                }.bind(this));
            },
            loadValue: function () {
                this.ppPlatformMappingService.getMainCategoryPropsWithSku(this.mainCategoryId).then(function (props) {
                    // 绑定所有属性
                    this.options.values = props;
                    // 同时指定第一个为默认值
                    this.selected.value = props[0];
                }.bind(this));
            },
            ok: function () {
                var complexMapping = new ComplexMappingBean(this.context.property.id, this.selected.value.id);
                // 返回新创建的 complex mapping 实例
                this.$uibModalInstance.close(complexMapping);
            },
            cancel: function () {
                this.$uibModalInstance.dismiss('cancel');
            }
        };

        return ComplexMappingPopupController;

    })());
});