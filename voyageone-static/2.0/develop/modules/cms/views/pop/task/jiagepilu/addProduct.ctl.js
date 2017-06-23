define([
    'cms',
    'modules/cms/enums/Carts',
], function (cms,cart) {
    cms.controller('AddJiagepiluProductController', (function () {

        function AddJiagepiluProductController(context, $uibModalInstance, taskBeatService, taskJiagepiluService, confirm, alert) {
            this.parent = context;
            this.taskId = context.taskId;
            this.id = context.id; // id不为空时是编辑，为空时是新增

            this.$uibModalInstance = $uibModalInstance;
            this.taskBeatService = taskBeatService;
            this.taskJiagepiluService = taskJiagepiluService;
            this.confirm = confirm;
            this.alert = alert;

            this.item = {
                id:this.id,
                taskId:this.taskId,
                numIid: "",
                productCode: "",
                price: ""
            };
        }

        AddJiagepiluProductController.prototype = {
            init: function () {
                var self = this;
                if (self.id) {
                    self.taskJiagepiluService.getEditProduct({beat_id:self.id}).then(function (resp) {
                        if (resp.data) {
                            self.item = resp.data;
                        } else {
                            self.alert("未找到目标记录");
                        }
                    });
                }
            },

            ok: function () {
                var self = this;
                var param = angular.copy(self.item);
                self.taskJiagepiluService.addJiagepiluProduct(param).then(function (resp) {
                   if (resp.data) {
                       self.$uibModalInstance.close();
                       self.parent.getData();
                   }
                });

                // self.taskBeatService.addCheck(param).then(function (res) {
                //     var otherBeats = res.data;
                //     if (otherBeats.length)
                //         return self.alert('TXT_MSG_CODE_IN_OTHER');
                //     return true;
                // }).then(function (goon) {
                //     if (!goon) return;
                //     self.taskBeatService.add(param).then(function (res) {
                //         if (res.data !== null) {
                //             self.$uibModalInstance.close();
                //             self.parent.getData();
                //         } else {
                //             self.alert('TXT_MSG_UPDATE_FAIL');
                //         }
                //     });
                // });
            },

            cancel: function () {
                this.$uibModalInstance.dismiss();
            }
        };

        return AddJiagepiluProductController;
    })());
});