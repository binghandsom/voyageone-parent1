/**
 * FeedMapping 属性匹配中,对其属性值进行设定画面的 Controller
 */

define(['cms'], function(cms) {

    return cms.controller('feedPropValuePopupController', (function() {

        function FeedPropValuePopupController(context, feedMappingService, $uibModalInstance) {

            this.$uibModalInstance = $uibModalInstance;
            this.feedMappingService = feedMappingService;

            this.context = context;
        }

        FeedPropValuePopupController.prototype = {
            cancel: function () {
                this.$uibModalInstance.dismiss('cancel');
            }
        };

        return FeedPropValuePopupController;
    })());
});