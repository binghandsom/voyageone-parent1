define([
    'cms',
    'underscore',
    'modules/cms/enums/FieldTypes',
    'modules/cms/models/SimpleMappingBean',
    'modules/cms/models/RuleExpression',
    'modules/cms/enums/WordTypes',
    'modules/cms/models/RuleWord',
    'modules/cms/views/pop/platformMapping/ppPlatformMapping.serv',
    'modules/cms/controller/popup.ctl'
], function (cms, _, FieldTypes, SimpleMappingBean, RuleExpression, WordTypes, RuleWord) {
    'use strict';
    return cms.controller('simpleListMappingPopupController', (function () {

        function SimpleListMappingPopupController(context, $uibModalInstance, ppPlatformMappingService, alert, notify) {

            this.$modal = $uibModalInstance;
            this.context = context;
            this.ppService = ppPlatformMappingService;
            this.alert = alert;
            this.notify = notify;

            this.property = context.path[0];

            /**
             * 当前属性的匹配
             * @type {SimpleMappingBean}
             */
            this.simpleMapping = null;
            /**
             * 一组 RuleWord
             * @type {RuleWord[]}
             */
            this.ruleWords = null;
            /**
             * 与 this.ruleWord 平行的行尾配置
             * @type {RuleWord[]}
             */
            this.lineEnds = null;

            this.mainCategory = {
                fields: null,
                skuField: null
            };
        }

        SimpleListMappingPopupController.prototype = {

            regulars: {
                endWithSpace: /^(&nbsp;| )$/,
                endWithBr: /^(<br ?\/?>)$/
            },

            init: function () {

                var self = this;
                var $mainCate = self.context.maindata.category;
                var property = self.property;
                var $pfCate = self.context.platform.category;

                // 检查 popup 的支持类型
                switch (property.type) {
                    case FieldTypes.complex:
                    case FieldTypes.multiComplex:
                        self.alert('当前属性不是 Simple 属性').result.then(function () {
                            self.cancel();
                        });
                        return;
                }

                // 加载主数据类目
                // 页面呈现时, 需要使用其字段信息
                self.ppService.getMainCategoryProps($mainCate.id)

                    .then(function (fields) {
                        self.mainCategory.fields = fields;
                    })

                    // 加载 SKU 级别的字段
                    .then(function () {
                        return self.ppService.getMainCategorySkuProp($mainCate.id);
                    })

                    .then(function (skuField) {
                        self.mainCategory.skuField = skuField;
                    })

                    // 最终加载当前平台类目字段对应的匹配关系
                    .then(function () {
                        return self.ppService.getPlatformPropertyMapping(
                            self.context.path,
                            $mainCate.id,
                            $pfCate.id,
                            self.context.cartId
                        );
                    })

                    .then(function (simpleMapping) {

                        // 如果没拿到, 则创建新的 SimpleMapping
                        if (!simpleMapping) {
                            simpleMapping = new SimpleMappingBean();
                            simpleMapping.platformPropId = property.id;
                        }

                        if (!simpleMapping.expression)
                            simpleMapping.expression = new RuleExpression();

                        if (!simpleMapping.expression.ruleWordList)
                            simpleMapping.expression.ruleWordList = [];

                        self.simpleMapping = simpleMapping;
                        self.ruleWords = simpleMapping.expression.ruleWordList;

                        // 加载完数据之后, 进行对 end line 的数据包装
                        self.wrapRuleList();

                    });
            },
            /**
             * 为 Line End 包装 List
             */
            wrapRuleList: function () {
                // 重置数据
                var lineEnds = this.lineEnds = [];
                // 初始化
                var words = this.ruleWords;
                var i = 0;
                var next = null;
                while (i < words.length) {
                    next = words[++i];
                    if (!next || next.type !== WordTypes.TEXT) {
                        // 如果这一行不是 Text, 就跳过, 平行的配置指定为 null ,表示"无"
                        lineEnds[i - 1] = {type: 3, value: null};
                        continue;
                    }

                    // 命中时, 把这个空格行移动到平行配置中

                    if (this.regulars.endWithBr.test(next.value)) {
                        lineEnds[i - 1] = {type: 1, value: words.splice(i, 1)[0]};
                        continue;
                    }

                    if (this.regulars.endWithSpace.test(next.value)) {
                        lineEnds[i - 1] = {type: 2, value: words.splice(i, 1)[0]};
                        continue;
                    }

                    // 是 Text 但未命中的话, 也为空
                    lineEnds[i - 1] = {type: 3, value: null};
                }
            },
            /**
             * 为 List 解除 Line End 包装
             */
            unWrapRuleList: function () {
                // 根据 end line 合并数据
                var newRuleWordList = [];
                var lineEnds = this.lineEnds;
                _.each(this.ruleWords, function (ruleWord, index) {
                    newRuleWordList.push(ruleWord);
                    var lineEndWord = lineEnds[index];
                    if (lineEndWord.value) newRuleWordList.push(lineEndWord.value);
                });
                return newRuleWordList;
            },
            /**
             * 当 lineEnd 配置变更时, 更新配置的值
             */
            updateLineEnd: function ($index) {

                var lineEndWord = this.lineEnds[$index];

                switch (lineEndWord.type) {
                    case 1:
                        var brWord = new RuleWord(WordTypes.TEXT);
                        brWord.value = '<br/>';
                        lineEndWord.value = brWord;
                        break;
                    case 2:
                        var spaceWord = new RuleWord(WordTypes.TEXT);
                        spaceWord.value = '&nbsp;';
                        lineEndWord.value = spaceWord;
                        break;
                    case 3:
                        lineEndWord.value = null;
                        break;
                }
            },

            add: function (ppPlatformMapping) {
                this.context.ruleWord = null;
                // 增加 RuleWord
                ppPlatformMapping.simpleItem(this.context).then(function (word) {
                    // 更新 ruleWords 时, 要同步更新平行的 lineEnds
                    this.ruleWords.push(word);
                    this.lineEnds.push({type: 3, value: null});
                }.bind(this));
            },

            edit: function ($index, ppPlatformMapping) {
                this.context.ruleWord = this.ruleWords[$index];
                ppPlatformMapping.simpleItem(this.context).then(function (word) {
                    // 用新的结果替换原结果
                    this.ruleWords[$index] = word;
                }.bind(this));
            },

            remove: function ($index) {
                this.ruleWords.splice($index, 1);
            },

            moveUp: function ($index) {
                if ($index < 1) return;
                var temp = this.ruleWords[$index];
                this.ruleWords[$index] = this.ruleWords[$index - 1];
                this.ruleWords[$index - 1] = temp;
            },

            moveDown: function ($index) {
                if ($index + 1 >= this.ruleWords.length) return;
                var temp = this.ruleWords[$index];
                this.ruleWords[$index] = this.ruleWords[$index + 1];
                this.ruleWords[$index + 1] = temp;
            },

            /**
             * 将字段值转换为友好的显示名
             */
            value: function (ruleWord) {
                var val = ruleWord.value;
                var field = this.property;
                var mainCategoryId = this.context.maindata.category.id;
                var mainFields = this.mainCategory;
                var service = this.ppService;
                var selected, selectedVals;

                switch (ruleWord.type) {
                    case WordTypes.TEXT:

                        switch (field.type) {
                            case FieldTypes.singleCheck:
                                selected = field.options.find(function (item) {
                                    return item.value === val;
                                });
                                return selected ? selected.displayName : val;
                            case FieldTypes.multiCheck:
                                selectedVals = val.split(',');
                                selected = field.options.filter(function (item) {
                                    return selectedVals.indexOf(item.value) > -1;
                                });
                                return selected.length ? selected.join(',') : val;
                        }

                        return val;

                    case WordTypes.SKU:

                        selected = mainFields.skuField.fields.find(function(field) {
                            return field.id === val;
                        });

                        return selected ? selected.name : val;

                    case WordTypes.MASTER:

                        selectedVals = service.searchProperty(mainFields.fields, val);

                        return selectedVals.length ? selectedVals[0].name : val;
                }
                return val;
            },

            ok: function () {

                var self = this;
                var simpleMapping = self.simpleMapping;
                var platform = self.context.platform;
                var notify = self.notify;

                simpleMapping.expression.ruleWordList = self.unWrapRuleList();

                this.ppService
                    .saveMapping(
                        self.context.maindata.category.id,
                        platform.category.id,
                        self.context.cartId,
                        simpleMapping,
                        self.context.path)
                    .then(function (updated) {
                        if (updated)
                            notify.success('已更新');
                        else
                            notify.warning('没有更新任何数据');

                        // 维护 Context 中的 Path, 让对应的属性和窗口同时结束生命周期
                        self.context.path.shift();
                        self.$modal.close(updated);
                    });
            },

            cancel: function () {
                var self = this;
                var simpleMapping = self.simpleMapping;

                simpleMapping.expression.ruleWordList = self.unWrapRuleList();
                self.$modal.dismiss();
            }
        };

        return SimpleListMappingPopupController;

    })());
});