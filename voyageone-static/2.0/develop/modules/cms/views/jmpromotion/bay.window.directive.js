define(['cms'], function (cms) {
    function BayWindowComponentController() {

    }

    cms.directive('bayWindow', function bayWindowDirectiveFactory() {
        return {
            templateUrl: '/modules/cms/views/jmpromotion/bay.window.directive.html',
            controller: [BayWindowComponentController]
        };
    });
});