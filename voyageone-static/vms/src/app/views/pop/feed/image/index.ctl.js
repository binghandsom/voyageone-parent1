/**
 * Created by sofia on 7/1/2016.
 */
define([
    'vms'
], function (vms) {
    vms.controller('ImagePreviewController', (function () {

        function ImagePreviewController(context) {
            this.context = context;
            this.imgSrc="";
        }

        ImagePreviewController.prototype = {
            init: function () {
                var self = this;
                self.imgSrc = self.context;
            }
        };

        return ImagePreviewController;
    })());
});
