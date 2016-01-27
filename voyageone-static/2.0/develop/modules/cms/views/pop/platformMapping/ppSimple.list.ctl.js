define([
    'cms',
    'modules/cms/enums/FieldTypes',
    'modules/cms/models/SimpleMappingBean',
    'modules/cms/models/RuleExpression',
    'modules/cms/enums/WordTypes',
    'modules/cms/models/ruleWords',
    'modules/cms/views/pop/platformMapping/ppPlatformMapping.serv',
    'modules/cms/controller/popup.ctl'
], function (cms, FieldTypes, SimpleMappingBean, RuleExpression, WordTypes, ruleWords) {
    'use strict';
    return cms.controller('simpleListMappingPopupController', (function () {

        function SimpleListMappingPopupController(context, $uibModalInstance, ppPlatformMappingService, alert) {

            this.$uibModalInstance = $uibModalInstance;
            this.context = context;
            this.ppPlatformMappingService = ppPlatformMappingService;
            this.alert = alert;

            this.maindata = {
                category: {
                    id: this.context.mainCategoryId,
                    path: null
                }
            };

            this.platform = {
                category: {
                    id: this.context.platformCategoryId,
                    path: this.context.platformCategoryPath
                },
                /**
                 * @type {WrapField}
                 */
                property: this.context.property
            };

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
             * @type {TextWord[]}
             */
            this.lineEnds = null;
        }

        SimpleListMappingPopupController.prototype = {

            regulars: {
                endWithSpace: /^(&nbsp;| )$/,
                endWithBr: /^(<br ?\/?>)$/
            },

            init: function () {

                var mainCategory = this.maindata.category;
                var property = this.platform.property;

                // 检查 popup 的支持类型
                switch (property.type) {
                    case FieldTypes.complex:
                    case FieldTypes.multiComplex:
                        this.alert('当前属性不是 Simple 属性').result.then(function () {
                            this.cancel();
                        }.bind(this));
                        return;
                }

                // 加载主类目路径
                this.ppPlatformMappingService.getMainCategoryPath(mainCategory.id).then(function (path) {
                    mainCategory.path = path;
                });

                // 加载原有的匹配
                this.ppPlatformMappingService.getPlatformPropertyMapping(
                    property,
                    mainCategory.id,
                    this.platform.category.id,
                    this.context.cartId
                ).then(function (simpleMapping) {

                    // 如果没拿到, 则创建新的 SimpleMapping
                    if (!simpleMapping) {
                        simpleMapping = new SimpleMappingBean();
                        simpleMapping.platformPropId = property.id;
                    }

                    if (!simpleMapping.expression)
                        simpleMapping.expression = new RuleExpression();

                    if (!simpleMapping.expression.ruleWordList)
                        simpleMapping.expression.ruleWordList = [];

                    this.simpleMapping = simpleMapping;
                    this.ruleWords = simpleMapping.expression.ruleWordList;

                    // 加载完数据之后, 进行对 end line 的数据包装
                    this.wrapRuleList();

                }.bind(this));
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
                    next = words[i + 1];
                    if (next.wordType !== WordTypes.TEXT) {
                        // 如果这一行不是 Text, 就跳过, 平行的配置指定为 null ,表示"无"
                        lineEnds[i] = {type: 3, value: null};
                        continue;
                    }

                    // 命中时, 把这个空格行移动到平行配置中

                    if (this.regulars.endWithBr.test(next.value)) {
                        lineEnds[i] = {type: 1, value: words.splice(1, 1)[0]};
                        continue;
                    }

                    if (this.regulars.endWithSpace.test(next.value)) {
                        lineEnds[i] = {type: 2, value: words.splice(1, 1)[0]};
                        continue;
                    }

                    // 是 Text 但未命中的话, 也为空
                    lineEnds[i] = {type: 3, value: null};
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
                        var brWord = new ruleWords.TextWord();
                        brWord.value = '<br/>';
                        lineEndWord.value = brWord;
                        break;
                    case 2:
                        var spaceWord = new ruleWords.TextWord();
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
                ppPlatformMapping.simple.item(this.context).then(function (word) {
                    // 更新 ruleWords 时, 要同步更新平行的 lineEnds
                    this.ruleWords.push(word);
                    this.lineEnds.push({type: 3, value: null});
                }.bind(this));
            },

            edit: function ($index, ppPlatformMapping) {
                this.context.ruleWord = this.ruleWords[$index];
                ppPlatformMapping.simple.item(this.context).then(function (word) {
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

            ok: function () {
                // 最终结束
                // 解除包装的 ruleWords, 更新到各个位置上
                this.mapping.expression.ruleWordList = this.unWrapRuleList();
                this.$uibModalInstance.close(this.mapping);
            },

            cancel: function () {
                this.$uibModalInstance.dismiss('cancel');
            }
        };

        return SimpleListMappingPopupController;

    })());
});