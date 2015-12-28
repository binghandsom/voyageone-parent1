/**
 * FeedMapping 属性匹配画面,对属性进行具体匹配的弹出框 Controller
 */

define(['cms'], function(cms) {

    return cms.controller('feedPropMappingPopupController', (function() {

        function FeedPropMappingPopupController(context, feedMappingService, $uibModalInstance) {

            this.$uibModalInstance = $uibModalInstance;
            this.feedMappingService = feedMappingService;

            this.context = context;
            /**
             * 具体到字段的 Mapping 设定
             * @typedef {{property:string,operation:string,value:string}} Condition
             * @typedef {{condition:Condition[],type:string,val:string}} Mapping
             * @type {{prop:string,mapping:Mapping[]}}
             */
            this.fieldMapping = null;
            /**
             * 当前编辑的主类目字段
             */
            this.field = this.context.field;
        }

        FeedPropMappingPopupController.prototype = {
            init: function() {

                this.feedMappingService.getFieldMapping({
                    feedCategoryPath: this.context.feedCategoryPath,
                    mainCategoryPath: this.context.mainCategoryPath,
                    field: this.context.field.id
                }).then(function (res) {

                    this.fieldMapping = res.data;
                }.bind(this));
            },
            cancel: function () {
                this.$uibModalInstance.dismiss('cancel');
            }
        };

        return FeedPropMappingPopupController;
    })());
});