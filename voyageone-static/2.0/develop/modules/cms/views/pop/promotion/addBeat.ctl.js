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
                var ttt = this;
                ttt.taskBeatService.addNumiid({task_id: ttt.task_id}).then(function (res) {
                    ttt.numiids = res.data;
                    // 加载数据后, 重置初始化的 Chsoen 宽度
                    $("#numiid-chosen-box").find(".chosen-container").width("100%");
                });
            },

            loadCode: function () {
                var ttt = this;
                var model = ttt.selected.num_iid;
                ttt.taskBeatService.addCode(model).then(function (res) {
                    ttt.codes = res.data;
                });
            },

            ok: function () {
                var ttt = this;
                var param = {
                    num_iid: ttt.selected.num_iid.numIid,
                    code: ttt.selected.code.productCode,
                    task_id: ttt.task_id
                };
                ttt.taskBeatService.addCheck(param).then(function (res) {
                    var otherBeats = res.data;
                    if (otherBeats.length)
                        return ttt.confirm('TXT_MSG_CODE_IN_OTHER').result;
                    return true;
                }).then(function (goon) {
                    if (!goon) return;
                    ttt.taskBeatService.add(param).then(function (res) {
                        if (res.data !== null) {
                            ttt.$uibModalInstance.close();
                            ttt.parent.getData();
                        } else {
                            ttt.alert('TXT_MSG_UPDATE_FAIL');
                        }
                    });
                });
            },

            cancel: function () {
                this.$uibModalInstance.close();
            }
        };

        return PopAddBeatCtl;
    })());
});