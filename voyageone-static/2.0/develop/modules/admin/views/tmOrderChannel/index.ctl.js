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
        function TmOrderChannelController(popups) {
            this.popups = popups;
        }

        TmOrderChannelController.prototype = {
         
        };
        return TmOrderChannelController;
    })())
});
