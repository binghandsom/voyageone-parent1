define([
    'cms'
], function (cms) {

    function NewShelvesPopupController(context, $uibModalInstance) {

    }

    NewShelvesPopupController.prototype = {
        ok: function () {
            var self = this;
            self.$uibModalInstance.close();
        },

        cancel: function () {
            this.$uibModalInstance.dismiss();
        }
    };

    cms.controller('NewShelvesPopupController', NewShelvesPopupController);
});