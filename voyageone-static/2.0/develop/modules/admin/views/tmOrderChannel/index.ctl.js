/**
 * @Date:    2016-08-10 11:16:14
 * @User:    sofia
 * @Version: 1.0.0
 */
define([
    'admin',
    'modules/admin/controller/popup.ctl'
], function (admin) {
    admin.controller('TmOrderChannelController', (function () {
        function TmOrderChannelController(popups, confirm) {
            this.popups = popups;
            this.confirm = confirm;
        }

        TmOrderChannelController.prototype = {
            delete: function () {
                var self = this;
                self.confirm('TXT_CONFIRM_DELETE_MSG');
            }
        };
        return TmOrderChannelController;
    })())
});
