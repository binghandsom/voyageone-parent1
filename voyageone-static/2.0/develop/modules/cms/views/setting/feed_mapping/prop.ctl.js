/**
 * controller FeedPropMappingController
 */

define([
    'cms',
    'modules/cms/controller/popup.ctl'
], function (cms) {
    "use strict";
    return cms.controller('feedPropMappingController', (function () {
        /**
         * @description
         * Feed Mapping 属性匹配画面的 Controller 类
         * @param $routeParams
         * @param {FeedMappingService} feedMappingService
         * @constructor
         */
        function FeedPropMappingController($routeParams, feedMappingService) {

            this.feedCategoryPath = $routeParams['feedCategoryPath'];
            this.feedMappingService = feedMappingService;

            this.mainCategory = null;
        }

        FeedPropMappingController.prototype = {

            init: function () {

                this.feedMappingService.getMainProps({
                    feedCategoryPath: this.feedCategoryPath
                }).then(function(res) {

                    this.mainCategory = res.data;
                }.bind(this));
            }
        };

        return FeedPropMappingController;

    })());
});