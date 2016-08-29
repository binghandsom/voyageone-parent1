/**
 * Created by sofia on 2016/8/10.
 */
define([
    'admin'
], function (admin) {
    admin.controller('AddOrgController', (function () {
        function AddOrgController(context, adminRoleService, adminOrgService, $uibModalInstance) {
            this.sourceData = context ? context : {};
            this.append = context == 'add' ? true : false;
            this.adminOrgService = adminOrgService;

            this.popType = '修改组织';
            this.$uibModalInstance = $uibModalInstance;
        }

        AddOrgController.prototype = {
            init: function () {
                var self = this;
                self.sourceData.active = self.sourceData.active ?  self.sourceData.active ? "1" : "0":'';
                if (self.sourceData == 'add') {
                    self.popType = '添加组织';
                    self.sourceData = {}
                }
                self.adminOrgService.getAllOrg().then(function (res) {
                    self.orgList = res.data;
                });

            },
            cancel: function () {
                this.$uibModalInstance.close();
            },
            save: function () {
                var self = this;
                var result = {};
                self.sourceData.active = self.sourceData.active == '1' ? true : false;
                if (self.append == true) {
                    self.adminOrgService.addOrg(self.sourceData).then(function (res) {
                        if (res.data == false) {
                            self.confirm(res.data.message);
                            return;
                        }
                        _.extend(result, {'res': 'success', 'sourceData': self.sourceData});
                        self.$uibModalInstance.close(result);
                    })
                } else {
                    self.adminOrgService.updateOrg(self.sourceData).then(function (res) {
                        if (res.data == false) {
                            self.confirm(res.data.message);
                            return;
                        }
                        _.extend(result, {'res': 'success', 'sourceData': self.sourceData});
                        self.$uibModalInstance.close(result);
                    })
                }
            }
        };
        return AddOrgController;
    })())
});