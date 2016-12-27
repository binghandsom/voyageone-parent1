/**
 * Created by sofia on 2016/8/24.
 */
define([
    'admin'
], function (admin) {
    admin.controller('AddCodeController', (function () {
        function AddCodeController(context, channelService, popups, typeService, typeAttrService, codeService, $uibModalInstance) {
            this.sourceData = context ? angular.copy(context) : {};
            this.append = context == 'add' ? true : false;
            this.context = context;
            this.popups = popups;
            this.channelService = channelService;
            this.typeService = typeService;
            this.typeAttrService = typeAttrService;
            this.codeService = codeService;
            this.popType = '编辑';
            this.companyId = this.sourceData.companyId;
            this.$uibModalInstance = $uibModalInstance

        }

        AddCodeController.prototype = {
            init: function () {
                var self = this;
                if (self.sourceData == 'add') {
                    self.popType = '添加';
                    self.sourceData = {}
                }
                self.sourceData.active = self.sourceData.active!=null ? self.sourceData.active ? "1" : "0" : '';
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
                var self = this;
                var result = {};
                self.sourceData.active = self.sourceData.active == '1' ? true : false;
                _.extend(self.context, self.sourceData);
                if (self.append == true) {
                    self.codeService.addCode(self.sourceData).then(function (res) {
                        if (res.data == false) {
                            self.confirm(res.data.message);
                            return;
                        }
                        _.extend(result, {'res': 'success', 'sourceData': self.context});
                        self.$uibModalInstance.close(result);
                    })
                } else {
                    self.codeService.updateCode(self.sourceData).then(function (res) {
                        if (res.data == false) {
                            self.confirm(res.data.message);
                            return;
                        }
                        _.extend(result, {'res': 'success', 'sourceData': self.context});
                        self.$uibModalInstance.close(result);
                    })
                }
            }
        };
        return AddCodeController;
    })())
});