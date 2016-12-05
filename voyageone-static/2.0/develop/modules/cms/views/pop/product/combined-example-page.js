define([
    'cms'
], function (cms) {

    cms.controller("combinedExpPopupController", (function () {

        function CombinedExpPopupCtl($element, $scope) {
            this.$element = $element;
            this.$scope = $scope;
        }

        CombinedExpPopupCtl.prototype.destroy = function () {
            var self = this,
                element = self.$element,
                scope = self.$scope;

            element.remove();
            scope.$destroy();
        };

        return CombinedExpPopupCtl;

    })());

});