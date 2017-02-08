/**
 * Created by sofia on 2016/10/13.
 */
define([
    'cms'
], function (cms) {
    cms.controller('ModifyPassController', (function () {
        function ModifyPassController(context, alert, $uibModalInstance, $modifyPassWordService) {
            this.$modifyPassWordService = $modifyPassWordService;
            this.context = context;
            this.alert = alert;
            this.$uibModalInstance = $uibModalInstance;
            this.newPass = '';
            this.newPassAgain = '';
            this.patrn =/^[a-zA-Z]{1}([a-zA-Z0-9]|[._]){4,19}$/;
        }

        ModifyPassController.prototype = {
            init: function () {
                var self = this;
                self.userAccount = self.context;
            },
            submit: function () {
                var self = this;
                if (self.newPass == self.oldPass) {
                    self.alert('新旧密码不能重复,请重新输入');return;
                }
                if (self.newPass == self.newPassAgain) {
                    self.$modifyPassWordService.save({
                        'oldPassword': self.oldPass,
                        'newPassword': self.newPass
                    }).then(function (res) {
                        res.data == true ? self.alert('密码修改成功，即将重新登录！').then(function () {
                            self.$uibModalInstance.close();
                            window.location.href = '/';
                        }) : self.alert('密码修改失败，请重新输入！')
                    })
                } else {
                    self.alert('两次输入的密码不匹配，请重新输入');
                }
            },
            newPassValidation:function(){
                var self = this;
                if (!self.patrn.exec(self.newPass)) {
                    self.alertMSG1='只能输入6～20个字母、数字、下划线';return;
                }else{
                    self.alertMSG1=''
                }
            },
            newPassAgainValidation:function(){
                var self = this;
                if (!self.patrn.exec(self.newPassAgain)) {
                    self.alertMSG2='只能输入6～20个字母、数字、下划线';return;
                }else{
                    self.alertMSG2=''
                }
            }
        };
        return ModifyPassController;
    })())
});