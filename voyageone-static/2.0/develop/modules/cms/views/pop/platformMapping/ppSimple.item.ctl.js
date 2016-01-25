define([
    'cms',
    'underscore',
    'modules/cms/enums/FieldTypes',
    'modules/cms/models/ruleWords',
    'modules/cms/enums/WordTypes',
    'modules/cms/enums/MappingTypes',
    'modules/cms/views/pop/platformMapping/ppPlatformMapping.serv'
], function (cms, _, FieldTypes, ruleWords, WordTypes, MappingTypes) {
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
            /**
             * 将编辑的 Word 实例
             * @type {RuleWord}
             */
            this.editingWord = this.context.ruleWord;

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

            this.loadEditing();
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
                }.bind(this));

                // 检查父级, 及父级的 Mapping 类型
                // 如果父级 Mapping 类型是 ComplexMapping 则需要依据父级 loadValue
                var parent = this.property.parent;

                if (!parent) {
                    this.loadValue();
                }

                this.ppPlatformMappingService.getPlatformPropertyMapping(
                    parent, this.mainCategoryId, this.context.platformCategoryId, this.cartId
                ).then(function (parentMapping) {

                    if (!parentMapping || parentMapping.mappingType !== MappingTypes.complex) {
                        this.loadValue();
                        return;
                    }

                    // TODO 未测试, 需要等待可以保存
                    // TODO Complex 特殊处理后续继续

                    /*
                     * 2016-01-22 19:29:03
                     * 当前情况是不支持 Feed Cn/Org 的. 所以 ComplexMapping 只有 Master.
                     * 所以子属性只能匹配到 ComplexMapping 对应属性的子属性
                     * 后续修改时, 请视情况修改这段注释
                     */

                    this.selected.valueFrom = this.options.valueFrom.MASTER;
                    this.readonly.valueFrom = true;

                    // 获取主数据属性的 Path
                    this.ppPlatformMappingService.getPropertyPath(this.mainCategoryId, parentMapping.value)
                        .then(function (properties) {

                            // 找到 ComplexMapping 指定的属性
                            // 并设定默认选中
                            var parentProperty = properties[0];

                            this.loadValue().then(function () {
                                this.options.values[0].selected = parentProperty;
                            }.bind(this));

                        }.bind(this));

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

                        return this.ppPlatformMappingService.getMainCategoryProps(this.mainCategoryId).then(function (props) {

                            if (!this.editingWord) {
                                this.options.values = [
                                    {selected: null, props: props}
                                ];
                                return;
                            }

                            var values = this.options.values = [];

                            // 如果是编辑, 则搜索选中字段的完整字段路径
                            this.ppPlatformMappingService.getPropertyPath(this.mainCategoryId, this.editingWord.value)
                                .then(function (properties) {

                                    _.each(properties.reverse(), function (property) {
                                        values.push({selected: property, props: props});
                                        props = property.fields;
                                        this.selected.value = property;
                                    }.bind(this));

                                }.bind(this));

                        }.bind(this));

                    case valueFrom.SKU:

                        return this.ppPlatformMappingService.getMainCategorySkuProp(this.mainCategoryId).then(function (sku) {
                            var selectedId = this.editingWord ? this.editingWord.value : null;
                            var selectedField = _.find(sku.fields, function (field) {
                                return field.id === selectedId;
                            });
                            this.options.values = [
                                {selected: selectedField, props: sku.fields}
                            ];
                        }.bind(this));

                    case valueFrom.DICT:

                        return this.ppPlatformMappingService.getDictList().then(function (dictList) {
                            var selectedName = this.editingWord ? this.editingWord.value : null;
                            var selectedDict = _.find(dictList, function (dict) {
                                return dict.name === selectedName;
                            });
                            this.options.values = [
                                {selected: selectedDict, props: dictList}
                            ];
                        }.bind(this));

                    case valueFrom.FEED_CN:
                    case valueFrom.FEED_ORG:
                    case valueFrom.TEXT:
                        this.alert('当前暂时不支持该类型');
                        this.options.values = [];
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
                        word.value = this.selected.value.name;
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
            loadEditing: function () {

                // 不存在,就使用默认
                if (!this.editingWord) return;

                var valueFrom = null;

                // 除了 Text 其他的都需要等待 loadValue, 所以在 loadValue 加载值

                switch (this.editingWord.wordType) {
                    case WordTypes.MASTER:
                        break;
                    case WordTypes.FEED_CN:
                        valueFrom = this.options.valueFrom.FEED_CN;
                        break;
                    case WordTypes.FEED_ORG:
                        valueFrom = this.options.valueFrom.FEED_ORG;
                        break;
                    case WordTypes.SKU:
                        valueFrom = this.options.valueFrom.SKU;
                        break;
                    case WordTypes.DICT:
                        valueFrom = this.options.valueFrom.DICT;
                        break;
                    case WordTypes.TEXT:
                        // 反向加载值...
                        switch (this.property.type) {
                            case FieldTypes.input:
                                this.property.value = this.editingWord.value;
                                break;
                            case FieldTypes.singleCheck:
                                this.property.value = {value: this.editingWord.value};
                                break;
                            case FieldTypes.multiCheck:
                                var values = this.editingWord.value.split(',');
                                values = _.map(values, function (value) {
                                    return {value: value};
                                });
                                this.property.values = values;
                        }
                        this.selected.fixedValue = true;
                        break;
                    default:
                        throw 'Unsupported word type.';
                }

                if (valueFrom) this.selected.valueFrom = valueFrom;
            },
            cancel: function () {
                this.$uibModalInstance.dismiss('cancel');
            }
        };

        return SimpleItemMappingPopupController;

    })());
});