/**
 * FeedMapping 属性匹配中,对其属性值进行设定画面的 Controller
 */

define([
    'cms',
    'modules/cms/enums/Operations',
    'underscore',
    'modules/cms/enums/FieldTypes'
], function (cms, operations, _, FieldTypes) {

    return cms.controller('propFeedMappingValueController', (function () {

        function PropFeedMappingValueController(context, feedMappingService, $uibModalInstance, alert, $translate) {

            this.alert = alert;
            this.$uibModalInstance = $uibModalInstance;
            this.feedMappingService = feedMappingService;
            this.operations = operations;
            this.$translate = $translate;

            this.context = context;

            this.feedPath = context.mapping.feedCategoryPath;
            /**
             * 当前处理的主类目属性
             * @type {Field}
             */
            this.field = angular.copy(context.field);
            /**
             * feed 类目的所有属性
             * @type {object[]}
             */
            this.feedAttributes = null;
            /**
             * 关联的设置
             * @type {{type: string}}
             */
            this.mappingSetting = {
                type: null
            };
            /**
             * 条件
             * @typedef {{property: string, operation: Operation, value: string}} Condition
             * @type {Condition[]}
             */
            this.conditions = [];
        }

        PropFeedMappingValueController.prototype = {
            
            init: function () {
                var ttt = this;
                ttt.feedMappingService.getFeedAttrs({
                    feedCategoryPath: ttt.feedPath
                }).then(function (res) {
                    ttt.feedAttributes = res.data;
                });
            },
            
            removeCondition: function (index) {
                (this.conditions || (this.conditions = [])).splice(index, 1);
            },

            addCondition: function () {
                (this.conditions || (this.conditions = [])).push({
                    property: null,
                    operation: null,
                    value: null
                });
            },

            getValue: function (field) {
                switch (field.type) {
                    case FieldTypes.input:
                        // 文本框即为字符串
                        return field.value;
                    case FieldTypes.singleCheck:
                        // 单选为 Value 对象
                        // 其具体值在其 value 属性上
                        return field.value
                            ? field.value.value
                            : null;
                    case FieldTypes.multiCheck:
                        // 多选为 Values, 是一组 Value 对象
                        // 其具体值在各 Value 的 value 属性上, 需要拼接
                        if (!field.values || !field.values.length)
                            return null;
                        return _.map(field.values, function (v) {
                            return v.value;
                        }).join(',');
                    default:
                        this.alert(this.$translate.instant('TXT_MSG_CANNOT_GET') + field.type + this.$translate.instant('TXT_MSG_TYPE_VALUE'));
                        return null;
                }
            },
            ok: function () {

                var ttt = this;
                var type = this.mappingSetting.type;

                if (!type) {
                    ttt.alert('TXT_MSG_MUST_GET_ONE_VALUE');
                    return;
                }

                var value;

                if (type === 'propFeed') {
                    value = ttt.field.value;
                } else if (!ttt.field.$valid) {
                    ttt.alert('TXT_MSG_FIELD_VAL_INVALID');
                    return;
                } else {
                    value = ttt.getValue(ttt.field);
                }

                if (!value) {
                    ttt.alert('TXT_MSG_NO_VALUE_IS_ON_THE_ATTRIBUTE');
                    return;
                }

                // 需要转换 Condition 中 Operation 的存储类型
                // 否则服务端无法转换
                var isBreak = ttt.conditions.some(function(condition) {
                    if (!condition.property || !condition.operation) return true;
                    if (!condition.operation.isSingle && !condition.value) return true;
                    condition.operation = condition.operation.name;
                });
                
                if (isBreak) {
                    ttt.alert('TXT_MSG_UNVALID_CONDITION');
                    return;
                }

                ttt.$uibModalInstance.close({
                    type: type,
                    val: value,
                    condition: ttt.conditions
                });
            },
            cancel: function () {
                this.$uibModalInstance.dismiss();
            }
        };

        return PropFeedMappingValueController;
    })());
});