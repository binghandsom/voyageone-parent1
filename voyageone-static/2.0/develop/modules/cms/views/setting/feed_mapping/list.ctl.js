/**
 * class FeedMappingController
 */

define([
  'cms',
  'modules/cms/controller/popup.ctl'
], function (cms) {

  return cms.controller('feedMappingController', (function() {

    function FeedMappingController(feedMappingService) {
      this.feedMappingService = feedMappingService;
      console.log(this.feedMappingService);
    }

    FeedMappingController.prototype = {};

    return FeedMappingController
  })());
});