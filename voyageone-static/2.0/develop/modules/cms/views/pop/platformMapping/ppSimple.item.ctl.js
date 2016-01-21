define([
    'cms',
    'underscore',
    'modules/cms/enums/FieldTypes',
    'modules/cms/models/ruleWords',
    'modules/cms/views/pop/platformMapping/ppPlatformMapping.serv'
], function (cms, _, FieldTypes, ruleWords) {
    'use strict';
    return cms.controller('simpleItemMappingPopupController', (function () {

        /**
         * Simple Mapping 弹出框的 Controller
         * @param {SimpleListMappingPopupContext} context
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
             * 是否需要(显示)选择项的匹配
             * @type {boolean}
             */
            this.needMappingOptions = false;
            /**
             * 平台属性
             * @type {Field}
             */
            this.property = this.context.property;
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
                 * 当前选中的内容
                 * @type {Field|null}
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
                    'MASTER': {desc: 'MASTER ( Product画面->商品详情属性 )'},
                    'FEED_CN': {desc: 'FEED_CN ( Product画面->自定义属性中文部分 )'},
                    'FEED_ORG': {desc: 'FEED_ORG ( Product画面->自定义属性英文部分 )'},
                    'SKU': {desc: 'SKU ( Product画面->SKU属性 )'},
                    'DICT': {desc: 'DICT ( 自定义字典 )'}
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

                // 先重置画面显示
                this.needMappingOptions = false;
                var valueFrom = this.options.valueFrom;

                // 再根据类型加载
                switch (this.selected.valueFrom) {
                    case valueFrom.MASTER:

                        this.ppPlatformMappingService.getMainCategoryProps(this.mainCategoryId).then(function (props) {
                            this.options.values = [
                                {selected: null, props: props}
                            ];
                        }.bind(this));

                        break;
                    case valueFrom.SKU:

                        this.ppPlatformMappingService.getMainCategorySkuProp(this.mainCategoryId).then(function (sku) {
                            this.options.values = [
                                {selected: null, props: sku.fields}
                            ];
                        }.bind(this));

                        break;
                    case valueFrom.DICT:

                        this.ppPlatformMappingService.getDictList().then(function (dictList) {
                            this.options.values = [
                                {selected: null, props: dictList}
                            ];
                        }.bind(this));

                        break;
                    case valueFrom.FEED_CN:
                    case valueFrom.FEED_ORG:
                    case valueFrom.TEXT:
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
            tryLoadNext: function ($index, propGroup) {

                // 先清空后续的下拉绑定
                this.options.values.splice($index + 1);

                var children = propGroup.selected.fields;

                if (!children || !children.length) {
                    this.updateSelectedValue();
                    return;
                }

                // 如果有, 则添加下一级
                this.options.values.push({
                    selected: null,
                    props: children
                });
                this.updateSelectedValue();
            },
            /**
             * 更新当前选中的值, 并触发下一级绑定
             * @private
             */
            updateSelectedValue: function () {
                var values = this.options.values;
                this.selected.value = values[values.length - 1].selected;

                if (this.property.type !== FieldTypes.singleCheck && this.property.type !== FieldTypes.multiCheck)
                    return;

                this.needMappingOptions = (
                    this.selected.value.type === FieldTypes.singleCheck
                    || this.selected.value.type === FieldTypes.multiCheck
                );
            },
            ok: function () {
                // 准备收网~ 根据画面来说
                // 先决定用上半边的数据还是下半边的数据
                this.$uibModalInstance.close(
                    this.selected.fixedValue
                        ? this.getTextWord()
                        : this.getWordByFrom()
                );
            },
            /**
             * 根据画面结果组装结果
             * @returns {TextWord}
             */
            getTextWord: function () {

                // 这里是下半边
                // 固定值,即 TextWord
                var textWord = new ruleWords.TextWord();

                switch (this.property.type) {
                    case FieldTypes.input:
                        textWord.value = this.property.value;
                        break;
                    case FieldTypes.singleCheck:
                        textWord.value = this.property.value.value;
                        break;
                    case FieldTypes.multiCheck:
                        // 多选保存结果
                        var values = _.map(this.property.values, function (value) {
                            return value.value;
                        });
                        textWord.value = values.join(',');
                }

                return textWord;
            },
            /**
             * 根据画面结果组装结果
             * @returns {MasterWord|SkuWord|DictWord}
             */
            getWordByFrom: function () {

                var valueFrom = this.options.valueFrom;
                var word = null;

                // 再根据类型加载
                switch (this.selected.valueFrom) {
                    case valueFrom.MASTER:
                        word = new ruleWords.MasterWord();
                        word.value = this.selected.value.id;

                        var type = this.selected.value.type;

                        if (type === FieldTypes.singleCheck || type === FieldTypes.multiCheck)
                            word.extra = this.getExtra();

                        break;
                    case valueFrom.SKU:
                        word = new ruleWords.SkuWord();
                        word.value = this.selected.value.id;
                        break;
                    case valueFrom.DICT:
                        word = new ruleWords.DictWord();
                        word.name = this.selected.value.name;
                        word.expression = null;
                        break;
                }

                return word;
            },
            /**
             * 组装选项匹配
             */
            getExtra: function () {

                var extra = {};

                _.each(this.selected.value.options, function (option) {
                    extra[option.value] = option.mapping;
                });

                return extra;
            },
            cancel: function () {
                this.$uibModalInstance.dismiss('cancel');
            }
        };

        return SimpleItemMappingPopupController;

    })());
});