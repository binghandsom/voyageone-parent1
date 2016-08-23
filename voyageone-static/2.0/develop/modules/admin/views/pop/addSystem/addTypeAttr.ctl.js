/**
 * Created by sofia on 2016/8/18.
 */
define([
    'admin'
], function (admin) {
    admin.controller('AddTypeAttrController', (function () {
        function AddTypeAttrController(context, channelService, popups, typeService, typeAttrService, $uibModalInstance) {
            this.sourceData = context ? context : {};
            this.append = context == 'add' ? true : false;
            this.popups = popups;
            this.channelService = channelService;
            this.typeService = typeService;
            this.typeAttrService = typeAttrService;
            this.popType = '编辑';
            this.companyId = this.sourceData.companyId;
            this.$uibModalInstance = $uibModalInstance

        }

        AddTypeAttrController.prototype = {
            init: function () {
                var self = this;
                if (self.sourceData == 'add') {
                    self.popType = '添加';
                    self.sourceData = {}
                }
                self.sourceData.active = self.sourceData.active ? "0" : "1";
                self.typeService.getAllType().then(function (res) {
                    self.typeList = res.data;
                });
            },
            add: function (type) {
                var self = this;
                self.popups.openTypeAdd(type).then(function (res) {
                    self.typeService.getAllType().then(function (res) {
                        self.typeList = res.data;
                    });
                    self.sourceData.typeId = res.res.id;
                })
            },
            cancel: function () {
                this.$uibModalInstance.close();
            },
            save: function () {
                var self = this;
                var result = {};
                self.sourceData.active = self.sourceData.active == '0' ? true : false;
                if (self.append == true) {
                    self.typeAttrService.addTypeAttribute(self.sourceData).then(function (res) {
                        _.extend(result, {'res': 'success', 'sourceData': self.sourceData});
                        self.$uibModalInstance.close(result);
                    })
                } else {
                    self.typeAttrService.updateTypeAttribute(self.sourceData).then(function (res) {
                        _.extend(result, {'res': 'success', 'sourceData': self.sourceData});
                        self.$uibModalInstance.close(result);
                    })
                }
            }
        };
        return AddTypeAttrController;
    })())
});