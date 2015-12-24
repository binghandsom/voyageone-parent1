/**
 * controller FeedPropMappingController
 */

define([
    'cms',
    'modules/cms/controller/popup.ctl'
], function (cms) {
    "use strict";
    return cms.controller('feedPropMappingController', (function () {

        function FeedPropMappingController($routeParams) {

            this.feedCategoryPath = $routeParams['feedCategoryPath'];
        }

        FeedPropMappingController.prototype = {

            init: function() {

                window.alert(this.feedCategoryPath);
            }
        };

        return FeedPropMappingController;

    })());
});