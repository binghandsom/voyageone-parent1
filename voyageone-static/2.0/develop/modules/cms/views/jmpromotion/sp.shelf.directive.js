define([
    'cms',
    'underscore',
    'modules/cms/controller/popup.ctl'
], function (cms, _) {

    function SpModelDirectiveController() {

    }

    cms.directive('spShelf', [function spModelDirectivefactory() {
        return {
            restrict: 'E',
            controller: [SpModelDirectiveController],
            templateUrl: '/modules/cms/views/jmpromotion/sp.shelf.directive.html'
        }
    }]);
});