define([
    'cms',
    'modules/cms/enums/Carts',
], function (cms,cart) {
    cms.controller('AddJiagepiluProductController', (function () {

        function AddJiagepiluProductController(context, $uibModalInstance, taskBeatService, taskJiagepiluService, confirm, alert) {
            this.parent = context;
            this.taskId = context.taskId;
            console.log(this.taskId);

            this.$uibModalInstance = $uibModalInstance;
            this.taskBeatService = taskBeatService;
            this.taskJiagepiluService = taskJiagepiluService;
            this.confirm = confirm;
            this.alert = alert;

            this.item = {
                numIid: "",
                code: "",
                price: ""
            };
        }

        AddJiagepiluProductController.prototype = {
            init: function () {
                console.log('name',cart.valueOf(23).desc);
            },

            ok: function () {
                var self = this;
                var param = {
                    taskId: self.taskId,
                    numIid: self.item.numIid,
                    code: self.item.code,
                    price: self.item.price
                };
                console.log(param);
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