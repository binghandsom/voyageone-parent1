/**
 * Created by sofia on 2016/8/10.
 */
define([
    'admin'
], function (admin) {
    admin.controller('CreateEditController', (function () {
        function CreateEditController(context) {
            this.sourceData = context ? context : {};
            this.append = context == 'add' ? true : false;
            this.popType = '修改';
            console.log(context);
        }

        CreateEditController.prototype = {
            init: function () {
                var self = this;
                if (self.sourceData == 'add') {
                    self.popType = '添加';
                    self.sourceData = {}
                }
            }
        };
        return CreateEditController;
    })())
});