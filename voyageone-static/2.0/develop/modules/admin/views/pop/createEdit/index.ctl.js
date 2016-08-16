/**
 * Created by sofia on 2016/8/10.
 */
define([
    'admin'
], function (admin) {
    admin.controller('CreateEditController', (function () {
        function CreateEditController(context, $uibModalInstance, AdminChannelService) {
            this.sourceData = context ? context : {};
            this.$uibModalInstance = $uibModalInstance;
            this.AdminChannelService = AdminChannelService;
            this.append = context == 'add' ? true : false;
            this.popType = '修改';
        }

        CreateEditController.prototype = {
            init: function () {
                var self = this;
                if (self.sourceData == 'add') {
                    self.popType = '添加';
                    self.sourceData = {}
                }
            },
            cancel: function () {
                this.$uibModalInstance.dismiss();
            },
            save: function () {
                var self = this;
                console.log(self.sourceData);
                _.extend(self.sourceData, {'cartList': self.cartList});
                if (self.append == true) {
                    self.AdminChannelService.addConfig().then(function () {
                    })
                }else{
                    self.AdminChannelService.updateConfig().then(function () {
                    })
                }
            }
        };
        return CreateEditController;
    })())
});