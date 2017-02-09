define([
    'cms'
], function (cms) {

    cms.controller("approvedExpPopupController", (function () {

        function ApprovedExpPopupCtl($element, $scope) {
            this.$element = $element;
            this.$scope = $scope;
        }

        ApprovedExpPopupCtl.prototype.init = function () {
            var self = this;

            angular.element('body').on("keyup", function (e) {
                if (e.keyCode === 27) {
                    if(self.destroy)
                        self.destroy();
                }

            });
        };

        ApprovedExpPopupCtl.prototype.destroy = function () {
            var self = this,
                element = self.$element,
                scope = self.$scope;

            element.remove();
            scope.$destroy();
        };

        return ApprovedExpPopupCtl;

    })());

});
