/**
 * Created by sofia on 2016/8/10.
 */
define([
    'admin'
], function (admin) {
    admin.controller('AddUserController', (function () {
        function AddUserController(context, adminRoleService, adminOrgService, adminUserService, $uibModalInstance) {
            this.sourceData = context ? context : {};
            this.append = context == 'add' ? true : false;
            this.adminRoleService = adminRoleService;
            this.adminOrgService = adminOrgService;
            this.adminUserService = adminUserService;

            this.popType = '修改用户';
            this.companyId = this.sourceData.companyId;
            this.$uibModalInstance = $uibModalInstance;
        }

        AddUserController.prototype = {
            init: function () {
                var self = this;
                if (self.sourceData == 'add') {
                    self.popType = '添加用户';
                    self.sourceData = {}
                }
                self.adminOrgService.getAllOrg().then(function (res) {
                    self.orgList = res.data;
                });
                self.adminRoleService.getAllRole().then(function (res) {
                    self.allList = res.data;
                    self.roleAllList = [];
                    for (var item in self.allList) {
                        self.roleAllList.push({'roleId': item, 'roleName': self.allList[item]});
                    }
                    return call(self.roleAllList);
                });
                function call(roleAllList) {
                    self.roleList = self.sourceData.roleName.split(',');
                    _.forEach(self.roleList, function (item, index) {
                        _.map(roleAllList, function (role) {
                            if (role.roleName == item) {
                                self.roleList[index] = {'roleId': role.roleId, 'roleName': item}
                            }
                            return self.roleList;
                        })
                    });
                }

            },
            selected: function (item) {
                var self = this;
                self.selectedRoleId = item.roleId;
            },
            move: function (type) {
                var self = this;
                self.cartList = self.cartList ? self.cartList : [];
                self.cartAllList = self.cartAllList ? self.cartAllList : [];
                switch (type) {
                    case 'allInclude':
                        _.extend(self.cartList, self.cartAllList);
                        self.cartAllList = null;
                        break;
                    case 'include':
                        self.data = _.find(self.cartAllList, function (cart) {
                            return cart.cartId == self.selectedCartId;
                        });
                        self.cartList.push(self.data);
                        self.cartAllList.splice(self.cartAllList.indexOf(self.data), 1);
                        break;
                    case 'exclude':
                        self.data = _.find(self.cartList, function (cart) {
                            return cart.cartId == self.selectedCartId;
                        });
                        self.cartAllList.push(self.data);
                        self.cartList.splice(self.cartList.indexOf(self.data), 1);
                        break;
                    case 'allExclude':
                        _.extend(self.cartAllList, self.cartList);
                        self.cartList = null;
                        break;
                }
            },
            cancel: function () {
                this.$uibModalInstance.close();
            },
            save: function () {
                var self = this;
                var result = {};
                if (self.append == true) {
                    self.adminUserService.addUser(self.sourceData).then(function (res) {
                        if (res.data == false) {
                            self.confirm(res.data.message);
                            return;
                        }
                        _.extend(result, {'res': 'success', 'sourceData': self.sourceData});
                        self.$uibModalInstance.close(result);
                    })
                } else {
                    self.adminUserService.updateUser(self.sourceData).then(function (res) {
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
        return AddUserController;
    })())
});