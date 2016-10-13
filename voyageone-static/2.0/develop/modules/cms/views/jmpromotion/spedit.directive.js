define([
    'cms',
    'underscore',
    'modules/cms/controller/popup.ctl'
], function (cms, _) {

    function SpEditDirectiveController() {
    }

    cms.directive('spEdit', [function spEditDirectiveFactory() {
        return {
            restrict: 'E',
            controller: [SpEditDirectiveController],
            templateUrl: '/modules/cms/views/jmpromotion/spedit.directive.html'
        }
    }]);
});