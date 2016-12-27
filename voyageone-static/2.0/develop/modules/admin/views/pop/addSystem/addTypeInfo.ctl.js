/**
 * Created by sofia on 2016/8/10.
 */
define([
    'admin'
], function (admin) {
    admin.controller('AddTypeInfoController', (function () {
        function AddTypeInfoController(context, typeService, $uibModalInstance) {
            this.sourceData = context ? angular.copy(context) : {};
            this.append = context == 'add' ? true : false;
            this.context = context;
            this.typeService = typeService;
            this.popType = '编辑';
            this.companyId = this.sourceData.companyId;
            this.$uibModalInstance = $uibModalInstance;
        }

        AddTypeInfoController.prototype = {
            init: function () {
                var self = this;
                if (self.sourceData == 'add') {
                    self.popType = '添加';
                    self.sourceData = {}
                }
                self.sourceData.active = self.sourceData.active!=null ? self.sourceData.active ? "1" : "0" : '';
            },
            cancel: function () {
                var result = {res: 'failure'};
                this.$uibModalInstance.close(result);
            },
            save: function () {
                var self = this;
                var result = {};
                self.sourceData.active = self.sourceData.active == '1' ? true : false;
                _.extend(self.context, self.sourceData);
                if (self.append == true) {
                    self.typeService.addType(self.sourceData).then(function (res) {
                        if (res.data == false) {
                            self.confirm(res.data.message);
                            return;
                        }
                        _.extend(result, {'res': res.data, 'sourceData': self.context});
                        self.$uibModalInstance.close(result);
                    })
                } else {
                    self.typeService.updateType(self.sourceData).then(function (res) {
                        if (res.data == false) {
                            self.confirm(res.data.message);
                            return;
                        }
                        _.extend(result, {'res': res.data, 'sourceData': self.context});
                        self.$uibModalInstance.close(result);
                    })
                }
            }
        };
        return AddTypeInfoController;
    })())
});