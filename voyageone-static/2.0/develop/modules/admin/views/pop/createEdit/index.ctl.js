/**
 * Created by sofia on 2016/8/10.
 */
define([
    'admin'
], function (admin) {
    admin.controller('CreateEditController', (function () {
        function CreateEditController(context, $uibModalInstance) {
            this.sourceData = context ? context : {};
            this.$uibModalInstance = $uibModalInstance;
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
            }
        };
        return CreateEditController;
    })())
});