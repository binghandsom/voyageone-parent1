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
                if (self.sourceData == 'add') {
                    self.popType = '添加组织';
                    self.sourceData = {};
                    self.sourceData.active = '1';
                }
                self.adminOrgService.getAllOrg().then(function (res) {
                    self.orgList = [];
                    var name = [];
                    _.forEach(res.data, function (item) {
                        name = item.orgName.split('-');
                        if (name.length > 1) {
                            var tt = name[length + 1];
                        } else {
                            var tt = name[length];
                        }
                        if (tt == self.sourceData.orgName) {
                            res.data.slice(res.data.indexOf(item), 1);
                        }
                    });
                    self.orgList.push(res.data);
                });

            },
            cancel: function () {
                this.$uibModalInstance.close();
            },
            save: function () {
                var self = this;
                var result = {};
                var para = {
                    'id': self.sourceData.id,
                    'orgName': self.sourceData.orgName,
                    'parentId': self.sourceData.parentId,
                    'weight': self.sourceData.weight,
                    'active': self.sourceData.active
                };
                if (self.append == true) {
                    self.adminOrgService.addOrg(para).then(function (res) {
                        if (res.data == false) {
                            self.confirm(res.data.message);
                            return;
                        }
                        _.extend(result, {'res': 'success', 'sourceData': self.sourceData});
                        self.$uibModalInstance.close(result);
                    })
                } else {
                    self.adminOrgService.updateOrg(para).then(function (res) {
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