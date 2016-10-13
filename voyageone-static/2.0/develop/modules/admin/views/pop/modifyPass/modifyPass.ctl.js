/**
 * Created by sofia on 2016/10/13.
 */
define([
    'admin'
], function (admin) {
    admin.controller('ModifyPassController', (function () {
        function ModifyPassController(context, adminUserService, alert, $uibModalInstance) {
            this.context = context;
            this.adminUserService = adminUserService;
            this.alert = alert;
            this.$uibModalInstance = $uibModalInstance;
            this.newPass = '';
            this.newPassAgain = '';
        }

        ModifyPassController.prototype = {
            init: function () {
                var self = this;
                self.userAccount = self.context;
            },
            submit: function () {
                var self = this;
                if (self.newPass == self.newPassAgain) {
                    self.adminUserService.modifyPass({'password': self.newPass}).then(function (res) {
                        res.data == true ? self.alert('密码修改成功，即将重新登录！').then(function () {
                            self.$uibModalInstance.close();
                            window.location.href = '/';
                        }) : self.alert('密码修改失败，请重新输入！')
                    })
                } else {
                    self.alert('两次输入的密码不匹配，请重新输入');
                }
            }

        };
        return ModifyPassController;
    })())
});