/**
 * Created by sofia on 2016/8/10.
 */
define([
    'admin'
], function (admin) {
    admin.controller('AddOrgController', (function () {
        function AddOrgController(context, adminOrgService, $uibModalInstance) {
            this.sourceData = context ? context : {};
            this.append = context == 'add' ? true : false;
            this.adminOrgService = adminOrgService;

            this.popType = '修改组织';
            this.$uibModalInstance = $uibModalInstance;
            this.saveInfo = {
                orgName: this.sourceData !== 'add' ? this.sourceData.orgName : '',
                parentId:this.sourceData !== 'add' ? this.sourceData.parentId:'',
                weight: this.sourceData !== 'add' ? this.sourceData.weight : '',
                active: this.sourceData !== 'add' ? this.sourceData.active + '' : '1',
                id: this.sourceData.id !== 'add' ? this.sourceData.id : ''
            }
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
                    var data = angular.copy(res.data);
                    var name = [];
                    _.forEach(data, function (item) {
                        if (item) {
                            name = item.orgName.split('-');
                            if (name.length > 1 ? name[length + 1] : name[length] == self.sourceData.orgName) {
                                data.splice(data.indexOf(item), 1);
                            }
                        }
                    });
                    self.orgList = data;
                });

            },
            cancel: function () {
                var result = {res: 'failure'};
                this.$uibModalInstance.close(result);
            },
            save: function () {
                var self = this, result = {};
                self.saveInfo.parentId = self.saveInfo.parentId - 0;
                self.saveInfo.active = self.saveInfo.active - 0;
                _.extend(self.sourceData, self.saveInfo);
                if (self.append == true) {
                    self.adminOrgService.addOrg(self.saveInfo).then(function (res) {
                        if (res.data == false) {
                            self.confirm(res.data.message);
                            return;
                        }
                        _.extend(result, {'res': 'success', 'sourceData': self.sourceData});
                        self.$uibModalInstance.close(result);
                    })
                } else {
                    self.adminOrgService.updateOrg(self.saveInfo).then(function (res) {
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