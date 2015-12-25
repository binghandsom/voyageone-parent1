/**
 * FeedMapping 属性匹配画面,对属性进行具体匹配的弹出框 Controller
 */

define(['cms'], function(cms) {

    return cms.controller('feedPropMappingPopupController', (function() {

        function FeedPropMappingPopupController(context, feedMappingService, $uibModalInstance) {

            this.$uibModalInstance = $uibModalInstance;
            this.feedMappingService = feedMappingService;

            this.context = context;
        }

        FeedPropMappingPopupController.prototype = {
            cancel: function () {
                this.$uibModalInstance.dismiss('cancel');
            }
        };

        return FeedPropMappingPopupController;
    })());
});