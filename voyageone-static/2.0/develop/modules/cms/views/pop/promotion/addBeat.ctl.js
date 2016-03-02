define([
    'cms'
], function (cms) {
    cms.controller('popAddBeatCtl', (function () {

        function PopAddBeatCtl(context, $uibModalInstance, taskBeatService) {
            this.parent = context;
            this.task_id = context.task_id;
            this.$uibModalInstance = $uibModalInstance;
            this.taskBeatService = taskBeatService;

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
                ttt.taskBeatService.add({
                    num_iid: ttt.selected.num_iid.numIid,
                    code: ttt.selected.code.productCode,
                    task_id: ttt.task_id
                }).then(function (res) {
                    if (res.data) {
                        ttt.$uibModalInstance.close();
                        ttt.parent.getData();
                    }
                })
            },

            cancel: function () {
                this.$uibModalInstance.close();
            }
        };

        return PopAddBeatCtl;
    })());
});