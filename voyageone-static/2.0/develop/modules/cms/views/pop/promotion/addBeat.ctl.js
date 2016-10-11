define([
    'cms'
], function (cms) {
    cms.controller('popAddBeatCtl', (function () {

        function PopAddBeatCtl(context, $uibModalInstance, taskBeatService, confirm, alert) {
            this.parent = context;
            this.task_id = context.task_id;
            this.$uibModalInstance = $uibModalInstance;
            this.taskBeatService = taskBeatService;
            this.confirm = confirm;
            this.alert = alert;

            this.numiids = [];
            this.codes = [];
            this.selected = {
                num_iid: '',
                code: {}
            }
        }

        PopAddBeatCtl.prototype = {
            init: function () {
                var self = this;
                self.taskBeatService.addNumiid({
                    task_id: self.task_id
                }).then(function (res) {
                    self.numiids = res.data;
                    // 加载数据后, 重置初始化的 Chsoen 宽度
                    $("#numiid-chosen-box").find(".chosen-container").width("100%");
                });
            },

            loadCode: function () {
                var self = this;
                var model = self.selected.num_iid;
                self.taskBeatService.addCode(model).then(function (res) {
                    self.codes = res.data;
                });
            },

            ok: function () {
                var self = this;
                var param = {
                    num_iid: self.selected.num_iid.numIid,
                    code: self.selected.code.productCode,
                    task_id: self.task_id
                };
                self.taskBeatService.addCheck(param).then(function (res) {
                    var otherBeats = res.data;
                    if (otherBeats.length)
                        return self.alert('TXT_MSG_CODE_IN_OTHER');
                    return true;
                }).then(function (goon) {
                    if (!goon) return;
                    self.taskBeatService.add(param).then(function (res) {
                        if (res.data !== null) {
                            self.$uibModalInstance.close();
                            self.parent.getData();
                        } else {
                            self.alert('TXT_MSG_UPDATE_FAIL');
                        }
                    });
                });
            },

            cancel: function () {
                this.$uibModalInstance.dismiss();
            }
        };

        return PopAddBeatCtl;
    })());
});