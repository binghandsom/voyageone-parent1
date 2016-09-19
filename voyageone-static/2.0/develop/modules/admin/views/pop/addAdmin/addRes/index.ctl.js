/**
 * Created by sofia on 2016/8/10.
 */
define([
    'admin'
], function (admin) {
    admin.controller('AddResController', (function () {
        function AddResController(context, adminUserService, adminResService, $uibModalInstance) {
            this.sourceData = context ? context : {};
            this.append = context == 'add' ? true : false;
            this.adminUserService = adminUserService;
            this.adminResService = adminResService;
            this.popType = '修改菜单资源';
            this.$uibModalInstance = $uibModalInstance;
            this.saveInfo = {
                resName: this.sourceData.resName,
                resKey: this.sourceData.resKey,
                resType: this.sourceData.resType + '',
                parentId: this.sourceData.parentId + '',
                weight: this.sourceData.weight,
                resUrl: this.sourceData.resUrl,
                icon: this.sourceData.icon,
                active: this.sourceData.active,
                description: this.sourceData.description,
                application: this.sourceData.application
            }
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
                self.adminResService.getMenu({'application': 'admin'}).then(function (res) {
                    self.menuList = res.data;
                    _.forEach(self.menuList, function (item) {
                        item.id = item.id + '';
                    });
                })
            },
            cancel: function () {
                this.$uibModalInstance.close();
            },
            save: function () {
                var self = this;
                var result = {};
                _.extend(self.sourceData, self.saveInfo);
                self.saveInfo.resType = self.saveInfo.resType - 0;
                self.saveInfo.parentId = self.saveInfo.parentId - 0;
                if (self.append == true) {
                    self.adminResService.addRes(self.saveInfo).then(function (res) {
                        if (res.data == false) {
                            self.confirm(res.data.message);
                            return;
                        }
                        _.extend(result, {'res': 'success', 'sourceData': self.sourceData});
                        self.$uibModalInstance.close(result);
                    })
                } else {
                    _.extend(self.saveInfo, {'id': self.sourceData.id});
                    self.adminResService.updateRes(self.saveInfo).then(function (res) {
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