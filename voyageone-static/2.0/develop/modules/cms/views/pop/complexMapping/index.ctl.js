/**
 * complexMappingPopupController
 */

define([
    'cms'
], function (cms) {
    return cms.controller('complexMappingPopupController', (function(){

        /**
         * Simple / Complex Mapping 弹出框的 Controller
         * @param {ComplexMappingPopupContext} context
         * @param $uibModalInstance
         * @constructor
         */
        function ComplexMappingPopupController(context, $uibModalInstance) {
            this.$uibModalInstance = $uibModalInstance;
            this.context = context;
        }

        ComplexMappingPopupController.prototype = {
            ok: function () {
                this.cancel();
            },
            cancel: function () {
                this.$uibModalInstance.dismiss('cancel');
            }
        };

        return ComplexMappingPopupController;

    })());
});