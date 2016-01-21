define([
    'cms',
    'modules/cms/enums/FieldTypes',
    'modules/cms/models/SimpleMappingBean',
    'modules/cms/models/RuleExpression',
    'modules/cms/views/pop/platformMapping/ppPlatformMapping.serv',
    'modules/cms/controller/popup.ctl'
], function (cms, FieldTypes, SimpleMappingBean, RuleExpression) {
    'use strict';
    return cms.controller('simpleListMappingPopupController', (function () {

        function SimpleListMappingPopupController(context, $uibModalInstance, ppPlatformMappingService, alert) {

            this.$uibModalInstance = $uibModalInstance;
            this.context = context;
            this.ppPlatformMappingService = ppPlatformMappingService;
            this.alert = alert;

            /**
             * 平台类目 ID
             * @type {string}
             */
            this.platformCategoryId = this.context.platformCategoryId;
            /**
             * 主数据类目 ID
             * @type {string}
             */
            this.mainCategoryId = this.context.mainCategoryId;
            /**
             * 平台属性
             * @type {WrapField}
             */
            this.property = this.context.property;
            /**
             * 一组 RuleWord
             * @type {RuleWord[]}
             */
            this.ruleWords = null;
            /**
             * 主数据类目路径
             * @type {string}
             */
            this.mainCategoryPath = null;
            /**
             * 当前属性的匹配
             * @type {SimpleMappingBean}
             */
            this.mapping = null;
        }

        SimpleListMappingPopupController.prototype = {
            init: function () {

                // 检查 popup 的支持类型
                switch (this.property.type) {
                    case FieldTypes.complex:
                    case FieldTypes.multiComplex:
                        this.alert('当前属性不是 Simple 属性').result.then(function () {
                            this.cancel();
                        }.bind(this));
                        return;
                }

                // 加载主类目路径
                this.ppPlatformMappingService.getMainCategoryPath(this.mainCategoryId).then(function (path) {
                    this.mainCategoryPath = path;
                }.bind(this));

                // 加载原有的匹配
                this.ppPlatformMappingService.getPlatformPropertyMapping(
                    this.property, this.mainCategoryId, this.platformCategoryId, this.context.cartId
                ).then(function (mapping) {

                    // 如果没拿到, 则创建新的 SimpleMapping
                    if (!mapping) {
                        mapping = new SimpleMappingBean();
                        mapping.platformPropId = this.property.id;
                    }

                    this.mapping = mapping;

                    if (!mapping.expression) mapping.expression = new RuleExpression();

                    this.ruleWords = (
                        mapping.expression.ruleWordList || (mapping.expression.ruleWordList = [])
                    );

                }.bind(this));
            },
            add: function (ppPlatformMapping) {
                this.context.ruleWord = null;
                // 增加 RuleWord
                ppPlatformMapping.simple.item(this.context).then(function (word) {
                    this.ruleWords.push(word);
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
                this.$uibModalInstance.close(this.mapping);
            },
            cancel: function () {
                this.$uibModalInstance.dismiss('cancel');
            }
        };

        return SimpleListMappingPopupController;

    })());
});