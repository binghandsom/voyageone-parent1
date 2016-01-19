define([
    'cms',
    'modules/cms/enums/FieldTypes',
    'modules/cms/views/pop/platformMapping/ppPlatformMapping.serv'
], function (cms, FieldTypes) {
    'use strict';
    return cms.controller('simpleItemMappingPopupController', (function () {

        /**
         * Simple Mapping 弹出框的 Controller
         * @param {SimpleItemMappingPopupContext} context
         * @param $uibModalInstance
         * @param {PopupPlatformMappingService} ppPlatformMappingService
         * @param alert
         * @constructor
         */
        function SimpleItemMappingPopupController(context, $uibModalInstance, ppPlatformMappingService, alert) {
            this.$uibModalInstance = $uibModalInstance;
            this.context = context;
            this.ppPlatformMappingService = ppPlatformMappingService;
            this.alert = alert;

            /**
             * 主数据类目 ID
             * @type {string}
             */
            this.mainCategoryId = this.context.mainCategoryId;
            /**
             * 主数据类目路径
             * @type {string}
             */
            this.mainCategoryPath = null;
            this.selected = {
                /**
                 * 选中的值来源
                 * @type {object}
                 */
                valueFrom: this.options.valueFrom.MASTER,
                /**
                 * 选中的值,有可能是属性也有可能是文本,也有可能是具体的属性选项
                 * @type {Field|object|null}
                 */
                value: null,
                /**
                 * 是否是固定值,画面上作为切换的 flag
                 * @type {boolean}
                 */
                fixedValue: false
            };
        }

        SimpleItemMappingPopupController.prototype = {
            options: {
                valueFrom: {
                    'MASTER': {desc: 'MASTER（Product画面->商品详情属性）'},
                    'FEED_CN': {desc: 'FEED_CN（Product画面->自定义属性中文部分）'},
                    'FEED_ORG': {desc: 'FEED_ORG（Product画面->自定义属性英文部分）'},
                    'SKU': {desc: 'SKU（Product画面->SKU属性）'},
                    'DICT': {desc: 'DICT'},
                    'TEXT': {desc: 'TEXT'}
                },
                values: null
            },
            init: function () {

                switch (this.context.property.type) {
                    case FieldTypes.complex:
                    case FieldTypes.multiComplex:
                        this.alert('当前属性不是 Simple 属性').result.then(function () {
                            this.cancel();
                        }.bind(this));
                        return;
                }

                this.ppPlatformMappingService.getMainCategoryPath(this.mainCategoryId).then(function (path) {
                    this.mainCategoryPath = path;
                    this.loadValue();
                }.bind(this));
            },
            /**
             * 根据 valueFrom 加载指定的值数据
             */
            loadValue: function () {

                switch (this.selected.valueFrom) {
                    case this.options.valueFrom.MASTER:

                }

            },
            ok: function () {
                console.log(this.selected.fixedValue);
            },
            cancel: function () {
                this.$uibModalInstance.dismiss('cancel');
            }
        };

        return SimpleItemMappingPopupController;

    })());
});