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
                 * 是否是固定值,画面上作为切换的 flag
                 * @type {boolean}
                 */
                fixedValue: false
            };
        }

        SimpleItemMappingPopupController.prototype = {
            options: {
                valueFrom: {
                    'MASTER': {desc: 'MASTER ( Product画面->商品详情属性 )'},
                    'FEED_CN': {desc: 'FEED_CN ( Product画面->自定义属性中文部分 )'},
                    'FEED_ORG': {desc: 'FEED_ORG ( Product画面->自定义属性英文部分 )'},
                    'SKU': {desc: 'SKU ( Product画面->SKU属性 )'},
                    'DICT': {desc: 'DICT'}
                },

                /**
                 * @typedef {object} SimpleProps
                 * @property {object|Field} selected 选中值
                 * @property {object[]|Field[]} props 可选值, 必须包含 id 和 name
                 *
                 */

                /**
                 * 一组 SimpleProps 数组. 因为需要处理多级属性
                 * @type {SimpleProps[]}
                 */
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
                    // 第一次加载默认数据
                    this.loadValue();
                }.bind(this));
            },
            /**
             * 根据 valueFrom 加载指定的值数据
             */
            loadValue: function () {

                switch (this.selected.valueFrom) {
                    case this.options.valueFrom.MASTER:

                        this.ppPlatformMappingService.getMainCategoryProps(this.mainCategoryId).then(function (props) {
                            this.options.values = [
                                {selected: null, props: props}
                            ];
                        }.bind(this));

                        break;
                    case this.options.valueFrom.SKU:
                        break;
                    case this.options.valueFrom.DICT:
                        break;
                    case this.options.valueFrom.FEED_CN:
                    case this.options.valueFrom.FEED_ORG:
                    case this.options.valueFrom.TEXT:
                        this.alert('当前暂时不支持该类型');
                        this.options.values = [];
                        break;
                }

            },
            /**
             * 尝试加载下一级属性
             * @param $index 当前更改的 Index
             * @param propGroup 当前更改的属性信息
             */
            tryLoadNext: function($index, propGroup) {

                // 先清空后续的下拉绑定
                this.options.values.splice($index + 1);

                var children = propGroup.selected.fields;

                if (!children || !children.length) return;

                // 如果有, 则添加下一级
                this.options.values.push({
                    selected: null,
                    props: children
                });
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