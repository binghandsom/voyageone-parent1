define(function (require) {
    'use strict';
    var cms = require('cms'),
        _ = require('underscore'),
        ruleTypes = require('modules/cms/enums/RuleTypes'),
        valueTypes = require('modules/cms/enums/ValueTypes'),
        carts = require('modules/cms/enums/Carts');
    return cms.controller('propertyMappingController', (function () {

        function PropertyMappingController(context, $uibModalInstance, popups, platformMappingService) {
            var self = this;

            var fieldValueType = context.fieldValueType,
                isMultiLine = false;

            switch (fieldValueType) {
                case valueTypes.TEXTAREA:
                case valueTypes.HTML:
                    isMultiLine = true;
                    break;
            }

            self.isMultiLine = isMultiLine;
            self.context = context;
            self.uibModalInstance = $uibModalInstance;
            self.popups = popups;
            self.valueArr = [];

            platformMappingService.getCommonSchema().then(function (res) {
                var mastOpts = self.mastOpts = {};
                _.each(res.data, function (item) {
                    mastOpts[item.value] = item.label;
                });
            });

            platformMappingService.getFeedCustomProps().then(function (res) {
                var feedCnOpts = self.feedCnOpts = {};
                _.each(res.data, function (item) {
                    if (!item.cnLabel)
                        return;
                    feedCnOpts[item.value] = item.cnLabel;
                });
            });
        }

        PropertyMappingController.prototype = {
            TYPE: {
                MASTER: "Master详情",
                FEED_ORG: "Feed属性",
                FEED_CN: "自定义属性",
                FIXED: "固定值"
            },
            init: function () {
                var self = this,
                    context = self.context,
                    expressionListJson = context.value,
                    expressionList;

                if (context.cartId)
                    context.cartName = carts.valueOf(+context.cartId).desc;

                if (expressionListJson)
                    expressionList = angular.fromJson(context.value);
                else
                    expressionList = [];

                self.valueArr = expressionList;
            },
            openPpPropertySetting: function () {
                var self = this,
                    context = self.context;

                self.popups.openPropertySetting({
                    cartPath: context.cartPath,
                    cartName: context.cartName,
                    name: context.name
                }).then(function (context) {
                    self.valueArr.push(context);
                });
            },
            order: function (arrow, index) {

                var curr = this.valueArr[index];
                var repIndex = arrow == "up" ? index - 1 : index + 1;

                if (repIndex < 0 || repIndex > this.valueArr.length - 1)
                    return;

                var tmp = this.valueArr.splice(repIndex, 1, curr);
                this.valueArr.splice(index, 1, tmp[0]);
            },
            update: function (item) {
                var self = this,
                    context = self.context;
                _.extend(item, {
                    cartPath: context.cartPath,
                    cartName: context.cartName,
                    name: context.name
                });
                self.popups.openPropertySetting(item).then(function (context) {
                    item.type = context.type;
                    item.value = context.value;
                });
            },
            remove: function (index) {
                this.valueArr.splice(index, 1)
            },
            confirm: function () {
                var valueList = _.map(this.valueArr, function (item) {
                    return {type: item.type, append: item.append, value: item.value};
                });

                if (valueList.length != 0)
                    this.context.value = JSON.stringify(valueList);
                else
                    this.context.value = null;

                this.uibModalInstance.close();
            },
            formatValue: function (item) {
                var self = this;
                switch (self.TYPE[item.type]) {
                    case self.TYPE.FEED_CN:
                        return self.feedCnOpts[item.value];
                    case self.TYPE.MASTER:
                        return self.mastOpts[item.value];
                }
                return item.value;
            },
            getValueType: function () {
                var valueTypeRule = _.find(this.context.rules, function (rule) {
                    return rule.name === ruleTypes.VALUE_TYPE_RULE;
                });
                return valueTypeRule ? valueTypeRule.value : null;
            }
        };

        return PropertyMappingController;
    })());
});