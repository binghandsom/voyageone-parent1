/**
 * FeedMapping 属性匹配画面,对属性进行具体匹配的弹出框 Controller
 */

define(['cms'], function (cms) {

    return cms.controller('propFeedMappingAttributeController', (function () {

        function PropFeedMappingAttributeController(context, feedMappingService, $uibModalInstance) {

            this.$uibModalInstance = $uibModalInstance;
            this.feedMappingService = feedMappingService;

            this.context = context;

            this.field = context.field;

            this.mapping = context.mapping;

            /**
             * 具体到字段的 Mapping 设定
             * @typedef {{property:string,operation:string,value:string}} Condition
             * @typedef {{condition:Condition[],type:string,val:string}} Mapping
             * @type {{prop:string,mappings:Mapping[]}}
             */
            this.fieldMapping = null;

            this.addValueMapping = this.addValueMapping.bind(this);
        }

        PropFeedMappingAttributeController.prototype = {

            init: function () {

                var ttt = this;

                ttt.feedMappingService.getFieldMapping({
                        mappingId: ttt.mapping._id,
                        fieldId: ttt.field.id,
                        fieldType: ttt.context.bean.type
                    })
                    .then(function (res) {
                        ttt.fieldMapping = res.data;
                    });
            },

            /**
             * 增加值匹配到属性匹配中
             * @param mapping 由上层 Popup 返回的值
             */
            addValueMapping: function (mapping) {
                if (!this.fieldMapping) {
                    this.fieldMapping = {};
                }
                if (!this.fieldMapping.mappings || !this.fieldMapping.mappings.length) {
                    this.fieldMapping.mappings = [];
                }
                this.fieldMapping.mappings.push(mapping);
            },
            
            /**
             * 移除一个mapping
             * @param {number} index
             */
            remove: function (index) {
                this.fieldMapping.mappings.splice(index, 1);
            },
            
            ok: function () {
                this.context.fieldMapping = this.fieldMapping;
                this.$uibModalInstance.close(this.context);
            },
            
            cancel: function () {
                this.$uibModalInstance.dismiss('cancel');
            }
        };

        return PropFeedMappingAttributeController;
    })());
});