/**
 * Created by sofia on 2016/8/10.
 */
define([
    'admin'
], function (admin) {
    admin.controller('AddResController', (function () {
        function AddResController(context, adminUserService, $uibModalInstance) {
            this.sourceData = context ? context : {};
            this.append = context == 'add' ? true : false;
            this.adminUserService = adminUserService;
            this.popType = '修改菜单资源';
            this.$uibModalInstance = $uibModalInstance;
        }

        AddResController.prototype = {
            init: function () {
                var self = this;
                if (self.sourceData == 'add') {
                    self.popType = '添加菜单资源';
                    self.sourceData = {};
                    self.sourceData.active = '1';
                }
                self.adminUserService.getAllApp().then(function (res) {
                    self.appList = res.data;
                });
            },
            cancel: function () {
                this.$uibModalInstance.close();
            },
            save: function () {
                var self = this;
                var result = {};
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
            },
            getResType: function (type) {
                switch (type) {
                    case 0:
                        return '系统';
                        break;
                    case 1:
                        return '菜单';
                        break;
                    case 2:
                        return 'Action';
                        break;
                }
            }
        };
        return AddResController;
    })())
});