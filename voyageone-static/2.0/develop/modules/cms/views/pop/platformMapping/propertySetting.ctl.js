define([
    'cms'
], function (cms) {
    'use strict';
    return cms.controller('propertySettingController', (function () {

        function PropertySettingController(context, $uibModalInstance, platformMappingService, $q) {
            this.context = context;
            this.uibModalInstance = $uibModalInstance;
            this.platformMappingService = platformMappingService;
            this.$q = $q;
        }

        PropertySettingController.prototype = {
            init: function () {
                var self = this,
                    platformMappingService = self.platformMappingService;

                self.cat = 'MASTER';

                self.$q.all(platformMappingService.getCommonSchema().then(function (res) {
                        self.mastOpts = res.data;
                        if (self.optionSource === "mastOpts")
                            self.options = self.mastOpts;
                    }),
                    platformMappingService.getFeedCustomProps().then(function (res) {
                        self.feedOpts = res.data;
                        if (self.optionSource === "feedOpts")
                            self.options = self.feedOpts;
                    })
                ).then(function () {
                    //判断是否是编辑入口进来
                    if (self.context.type)
                        self.cat = self.context.type;
                    self.value = self.context.value;
                    self.changeCat();
                });

            },
            changeCat: function () {
                var self = this,
                    optionSource;

                switch (self.cat) {
                    case "MASTER":
                        self.isFixValue = false;
                        self.optionSource = "mastOpts";
                        break;
                    case "FEED_ORG":


                    case "FEED_CN":
                        self.isFixValue = false;
                        self.optionSource = "feedOpts";
                        break;
                    case "FIXED":
                        self.optionSource = null;
                        self.isFixValue = true;
                        break
                }

                if (!self.optionSource)
                    return;

                optionSource = self[self.optionSource];

                if (!optionSource)
                    return;

                self.options = optionSource;
            },
            save: function () {
                var self = this;
                var newValue = {
                    $id: Math.random().toString().substr(2, 6),
                    type: self.cat,
                    append: "",
                    value: self.value
                };

                self.uibModalInstance.close(newValue);
            }
        };

        return PropertySettingController;

    })());
});