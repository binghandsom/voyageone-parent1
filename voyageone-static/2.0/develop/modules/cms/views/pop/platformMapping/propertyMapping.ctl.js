define([
    'cms',
    'modules/cms/enums/Carts'
], function (cms, carts) {
    'use strict';
    return cms.controller('propertyMappingController', (function () {

        function PropertyMappingController(context, $uibModalInstance, popups, platformMappingService) {
            var self = this;

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
                    expressionListJson = self.context.value,
                    expressionList,
                    platformMappingService = self.platformMappingService;

                if (self.context.cartId)
                    self.context.cartName = carts.valueOf(+self.context.cartId).desc;

                if (expressionListJson)
                    expressionList = angular.fromJson(self.context.value);
                else
                    expressionList = [];

                self.valueArr = expressionList;
            },
            openPpPropertySetting: function () {
                var self = this;
                self.popups.openPropertySetting({
                    cartPath: self.context.cartPath,
                    cartName: self.context.cartName,
                    name: self.context.name
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
                var self = this;
                _.extend(item, {
                    cartPath: self.context.cartPath,
                    cartName: self.context.cartName,
                    name: self.context.name
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
            }
        };

        return PropertyMappingController;

    })());
});