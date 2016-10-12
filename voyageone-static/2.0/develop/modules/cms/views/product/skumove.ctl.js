/**
 * Created by sofia on 2016/10/10.
 */
define([
    'cms',
    'modules/cms/controller/popup.ctl'
], function (cms) {
    cms.controller('SkuMoveController', (function () {
        function SkuMoveController() {
            this.show = false;
            self.showView = false;
        }

        SkuMoveController.prototype = {
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
                        item.value == 2 ? self.show = true : self.show = false;
                        break;
                    case 'buildView':
                        self.showView = true;
                }

            }

        };
        return SkuMoveController;
    })())
});