/**
 * Created by sofia on 2016/9/1.
 */
define([
    'admin',
    'modules/admin/controller/popup.ctl'
], function (admin) {
    admin.controller('GuideBatchJobController', (function () {
        function GuideBatchJobController(popups, alert, confirm, selectRowsFactory) {
            this.context = JSON.parse(window.sessionStorage.getItem('cartInfo'));
            this.popups = popups;
            this.alert = alert;
            this.confirm = confirm;
            this.selectRowsFactory = selectRowsFactory;
            this.tempTaskSelect = null;
            this.taskSelList = {selList: []};

        }

        GuideBatchJobController.prototype = {
            init: function () {
                var self = this;
                self.taskList = self.context.task;

                // 设置勾选框
                if (self.tempTaskSelect == null) {
                    self.tempTaskSelect = new self.selectRowsFactory();
                } else {
                    self.tempTaskSelect.clearCurrPageRows();
                    self.tempTaskSelect.clearSelectedList();
                }
                _.forEach(self.taskList, function (Info) {
                    if (Info.updFlg != 8) {
                        self.tempTaskSelect.currPageRows({
                            "id": Info.taskId
                        });
                    }
                });
                self.taskSelList = self.tempTaskSelect.selectRowsInfo;
                // End 设置勾选框
            }
        };
        return GuideBatchJobController;
    })())
});