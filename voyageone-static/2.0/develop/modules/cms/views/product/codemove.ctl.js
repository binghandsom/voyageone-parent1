/**
 * Created by sofia on 2016/10/13.
 */
define([
    'cms',
    'modules/cms/controller/popup.ctl'
], function (cms) {
    cms.controller('CodeMoveController', (function () {
        function CodeMoveController(popups) {
            this.show = false;
            this.popups = popups;
            this.showView = false;
        }

        CodeMoveController.prototype = {
            init: function () {
                var self = this;
            },
            search: function () {
                var self = this;
                console.log(self.type);
            },
            ifShow: function (item) {
                var self = this;
                switch (item.type) {
                    case 'selectGroup':
                        item.value == 1 ? self.show = true : self.show = false;
                        break;
                    case 'buildView':
                        self.showView = true;
                }

            },
            move: function (type) {
                var self = this;
                self.popups.openSKUMoveConfirm(type);
            }

        };
        return CodeMoveController;
    })())
});