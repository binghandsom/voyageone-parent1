define([
    'cms'
], function (cms) {
    'use strict';
    return cms.controller('propertySettingController', (function () {

        function PropertySettingController(context,$uibModalInstance,platformMappingService) {
            this.context = context;
            this.uibModalInstance = $uibModalInstance;
            this.platformMappingService = platformMappingService;
        }

        PropertySettingController.prototype = {
            init:function(){
                var self = this;
                self.cat = 'MASTER';

                self.platformMappingService.getCommonSchema().then(function(res){
                    self.options = self.mastOpts = res.data;

                });
                self.platformMappingService.getFeedCustomProps().then(function(res){
                    self.feedOpts = res.data;
                });

            },
            changeCat:function(){
                var self = this;
                switch (self.cat){
                    case "MASTER":
                            self.isFixValue = false;
                            self.options = self.mastOpts;
                            break;
                    case "FEED_ORG":
                    case "FEED_CN":
                            self.isFixValue = false;
                            self.options = self.feedOpts;
                            break;
                    case "FIXED":
                            self.isFixValue = true;
                            break
                }
            },
            save:function(){
                var self = this;
                var newValue = {
                    $id:Math.random().toString().substr(2,6),
                    type:self.cat,
                    append:"",
                    value:self.value
                };

                self.uibModalInstance.close(newValue);
            }
        };

        return PropertySettingController;

    })());
});