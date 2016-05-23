/**
 * @ngdoc
 * @controller
 * @name simpleItemMappingPopupController
 */

/**
 * @typedef {object} PropGroup
 * @property {object} selected 选中值
 * @property {object[]} props 可选值, 必须包含 id 和 name
 */

define([
    'cms',
    'underscore',
    'modules/cms/enums/FieldTypes',
    'modules/cms/models/RuleWord',
    'modules/cms/enums/WordTypes',
    'modules/cms/views/pop/platformMapping/ppPlatformMapping.serv'
], function (cms, _, FieldTypes, RuleWord, WordTypes) {
    'use strict';
    return cms.controller('simpleItemMappingPopupController', (function () {

        /**
         * Simple Mapping 弹出框的 Controller
         * @param context
         * @param $uibModalInstance
         * @param {PopupPlatformMappingService} ppPlatformMappingService
         * @param alert
         * @constructor
         */
        function SimpleItemMappingPopupController(context, $uibModalInstance, ppPlatformMappingService, alert) {

            this.$modal = $uibModalInstance;
            this.context = context;
            this.ppService = ppPlatformMappingService;
            this.alert = alert;

            this.field = angular.copy(context.path[0]);
            /**
             * 是否需要(显示)选择项的匹配
             * @type {boolean}
             */
            this.needMappingOptions = false;
            /**
             * 将编辑的 Word 实例
             * @type {RuleWord}
             */
            this.ruleWord = this.context.ruleWord;

            this.selected = {
                /**
                 * 选中的值来源
                 * @type {object}
                 */
                valueFrom: this.valueFromOptions.MASTER,
                /**
                 * 当前选中的内容
                 */
                value: null,
                /**
                 * 是否是固定值,画面上作为切换的 flag
                 * @type {boolean}
                 */
                fixedValue: false
            };

            /**
             * 一组 PropGroup 数组. 因为需要处理多级属性
             * @type {PropGroup[]}
             */
            this.values = null;
        }

        SimpleItemMappingPopupController.prototype = {

            valueFromOptions: {
                'MASTER': {desc: 'MASTER ( Product画面->商品详情属性 )'},
                'FEED_CN': {desc: 'FEED_CN ( Product画面->自定义属性中文部分 )'},
                'FEED_ORG': {desc: 'FEED_ORG ( Product画面->自定义属性英文部分 )'},
                'SKU': {desc: 'SKU ( Product画面->SKU属性 )'},
                'DICT': {desc: 'DICT ( 自定义字典 )'}
            },

            init: function () {

                var me = this;
                var valueFrom = null;
                var property = this.field;

                switch (property.type) {
                    case FieldTypes.complex:
                    case FieldTypes.multiComplex:
                        me.alert('当前属性不是 Simple 属性').result.then(function () {
                            me.cancel();
                        });
                        return;
                }

                if (!me.ruleWord) {
                    me.loadValue();
                    return;
                }

                switch (me.ruleWord.type) {
                    case WordTypes.MASTER:
                        break;
                    case WordTypes.FEED_CN:
                        valueFrom = me.valueFromOptions.FEED_CN;
                        break;
                    case WordTypes.FEED_ORG:
                        valueFrom = me.valueFromOptions.FEED_ORG;
                        break;
                    case WordTypes.SKU:
                        valueFrom = me.valueFromOptions.SKU;
                        break;
                    case WordTypes.DICT:
                        valueFrom = me.valueFromOptions.DICT;
                        break;
                    case WordTypes.TEXT:
                        // 反向加载值...
                        switch (property.type) {
                            case FieldTypes.input:
                                property.value = me.ruleWord.value;
                                break;
                            case FieldTypes.singleCheck:
                                property.value = {value: me.ruleWord.value};
                                break;
                            case FieldTypes.multiCheck:
                                var values = me.ruleWord.value.split(',');
                                values = _.map(values, function (value) {
                                    return {value: value};
                                });
                                property.values = values;
                        }
                        me.selected.fixedValue = true;
                        break;
                    default:
                        throw 'Unsupported word type.';
                }

                if (valueFrom) me.selected.valueFrom = valueFrom;

                me.loadValue();
            },

            /**
             * 根据 valueFrom 加载指定的值数据
             */
            loadValue: function () {

                var me = this;
                var options = me.valueFromOptions;
                var mainCate = me.context.maindata.category;

                // 先重置画面显示
                me.needMappingOptions = false;

                // 再根据类型加载
                switch (me.selected.valueFrom) {
                    case options.MASTER:

                        return me.ppService.getMainCategoryProps(mainCate.id, false, true).then(function (props) {

                            // 如果是编辑, 则搜索选中字段的完整字段路径
                            // 编辑情况下, 有可能默认选中固定值, 但同时也会 load Master 属性, 所以这里需要特殊处理

                            var values = me.values = [];

                            if (!me.ruleWord || me.selected.fixedValue) {
                                values.push({selected: null, props: props});
                                return;
                            }

                            // 使用选中的叶子属性, 搜索完整的属性树枝干
                            var properties = me.ppService.searchProperty(props, me.ruleWord.value);

                            // 如果搜索不到枝干(路径), 就默认不选中数据
                            if (!properties) {
                                values.push({selected: null, props: props});
                                return;
                            }

                            // 如果有, 则按倒序, 为每个下拉指定数据源和默认的选中项
                            _.each(properties.reverse(), function (property) {
                                values.push({selected: property, props: props});
                                props = property.fields;
                                me.selected.value = property;
                            });

                        });

                    case options.SKU:

                        return me.ppService.getMainCategorySkuProp(mainCate.id).then(function (sku) {
                            var selectedId = me.ruleWord ? me.ruleWord.value : null;
                            var selectedField = _.find(sku.fields, function (field) {
                                return field.id === selectedId;
                            });
                            me.values = [
                                {selected: selectedField, props: sku.fields}
                            ];
                        });

                    case options.DICT:

                        return me.ppService.getDictList(me.context.cartId).then(function (dictList) {
                            var selectedName = me.ruleWord ? me.ruleWord.value : null;
                            var selectedDict = _.find(dictList, function (dict) {
                                return dict.name === selectedName;
                            });
                            me.values = [
                                {selected: selectedDict, props: dictList}
                            ];
                        });

                    case options.FEED_CN:
                    case options.FEED_ORG:
                    case options.TEXT:
                        me.alert('当前暂时不支持该类型');
                        me.values = [];
                        return null;
                }

            },

            /**
             * 尝试加载下一级属性
             * @param $index 当前更改的 Index
             * @param propGroup 当前更改的属性信息
             */
            tryLoadNext: function ($index, propGroup) {

                // 先清空后续的下拉绑定
                this.values.splice($index + 1);

                var children = propGroup.selected.fields;

                if (!children || !children.length) {
                    this.updateSelectedValue();
                    return;
                }

                // 如果有, 则添加下一级
                this.values.push({
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
                var values = this.values;
                var property = this.field;

                this.selected.value = values[values.length - 1].selected;

                if (property.type !== FieldTypes.singleCheck && property.type !== FieldTypes.multiCheck)
                    return;

                this.needMappingOptions = (
                    this.selected.value.type === FieldTypes.singleCheck
                    || this.selected.value.type === FieldTypes.multiCheck
                );
            },

            /**
             * 根据画面结果组装结果
             * @returns {RuleWord}
             */
            getTextWord: function () {

                // 这里是下半边
                // 固定值,即 TextWord
                var textWord = new RuleWord(WordTypes.TEXT);
                var pfProp = this.field;

                switch (pfProp.type) {
                    case FieldTypes.input:
                        textWord.value = pfProp.value;
                        break;
                    case FieldTypes.singleCheck:
                        textWord.value = pfProp.value.value;
                        break;
                    case FieldTypes.multiCheck:
                        // 多选保存结果
                        var values = _.map(pfProp.values, function (value) {
                            return value.value;
                        });
                        textWord.value = values.join(',');
                }

                return textWord;
            },

            /**
             * 根据画面结果组装结果
             * @returns {RuleWord}
             */
            getWordByFrom: function () {

                var froms = this.valueFromOptions;
                var word = null;
                var value = this.selected.value;

                // 再根据类型加载
                switch (this.selected.valueFrom) {
                    case froms.MASTER:
                        word = new RuleWord(WordTypes.MASTER);
                        word.value = value.id;

                        var type = value.type;

                        if (type === FieldTypes.singleCheck || type === FieldTypes.multiCheck)
                            word.extra = this.getExtra();

                        break;
                    case froms.SKU:
                        word = new RuleWord(WordTypes.SKU);
                        word.value = value.id;
                        break;
                    case froms.DICT:
                        word = new RuleWord(WordTypes.DICT);
                        word.value = value.name;
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

            ok: function () {
                this.$modal.close(
                    this.selected.fixedValue
                        ? this.getTextWord()
                        : this.getWordByFrom()
                );
            },

            cancel: function () {
                this.$modal.dismiss();
            }
        };

        return SimpleItemMappingPopupController;

    })());
});