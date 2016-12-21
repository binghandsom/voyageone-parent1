/**
 * Created by sofia on 2016/8/10.
 */
define([
    'admin'
], function (admin) {
    admin.controller('AddTaskController', (function () {
        function AddTaskController(context, taskService, $uibModalInstance) {
            this.sourceData = context ? angular.copy(context) : {};
            this.append = context == 'add' || context.kind == 'add' ? true : false;
            this.readOnly = context.isReadOnly == true ? true : false;
            this.context = context;
            this.taskService = taskService;
            this.popType = '编辑';
            this.companyId = this.sourceData.companyId;
            this.$uibModalInstance = $uibModalInstance;
        }

        AddTaskController.prototype = {
            init: function () {
                var self = this;
                if (self.sourceData == 'add' || self.sourceData.kind == 'add') {
                    self.popType = '添加';
                    if (self.sourceData.isReadOnly !== true) {
                        self.sourceData = {};
                    } else {
                        self.sourceData = self.sourceData;
                    }
                }
                self.taskService.getAllTaskType().then(function (res) {
                    self.taskTypeList = res.data;
                });
            },
            cancel: function () {
                var result = {res: 'failure'};
                this.$uibModalInstance.close(result);
            },
            save: function () {
                var self = this;
                var result = {};
                if (self.readOnly == true) {
                    self.$uibModalInstance.close(self.sourceData);
                    return;
                }
                _.extend(self.context, self.sourceData);
                if (self.append == true) {
                    self.taskService.addTask(self.sourceData).then(function (res) {
                        if (res.data == false) {
                            self.confirm(res.data.message);
                            return;
                        }
                        _.extend(result, {'res': res.data, 'sourceData': self.context});
                        self.$uibModalInstance.close(result);
                    })
                } else {
                    self.taskService.updateTask(self.sourceData).then(function (res) {
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
        return AddTaskController;
    })())
});