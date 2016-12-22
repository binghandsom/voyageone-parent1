/**
 * Created by sofia on 2016/8/18.
 */
define([
    'admin'
], function (admin) {
    admin.controller('AddTypeAttrController', (function () {
        function AddTypeAttrController(context, channelService, popups, typeService, typeAttrService, $uibModalInstance) {
            this.context = context;
            this.sourceData = context ? angular.copy(context) : {};
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
                self.sourceData.active = self.append == false ? (self.sourceData.active == true ? "0" : "1") : '';
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
                var result = {res: 'failure'};
                this.$uibModalInstance.close(result);
            },
            save: function () {
                var self = this, result = {};
                self.sourceData.active = self.sourceData.active == '0' ? true : false;
                _.extend(self.context, self.sourceData);
                if (self.append == true) {
                    self.typeAttrService.addTypeAttribute(self.sourceData).then(function (res) {
                        if (res.data == false) {
                            self.confirm(res.data.message);
                            return;
                        }
                    });
                } else {
                    self.typeAttrService.updateTypeAttribute(self.sourceData).then(function (res) {
                        if (res.data == false) {
                            self.confirm(res.data.message);
                            return;
                        }
                    });
                }
                self.sourceData.active = self.sourceData.active ? '0' : '1';
                _.extend(result, {'res': 'success', 'sourceData': self.context});
                self.$uibModalInstance.close(result);
            }
        };
        return AddTypeAttrController;
    })())
});