/**
 * FeedMapping 属性匹配中,对其属性值进行设定画面的 Controller
 */

define(['cms', 'modules/cms/enums/Operations'], function (cms, operations) {

    return cms.controller('feedPropValuePopupController', (function () {

        function FeedPropValuePopupController(context, feedMappingService, $uibModalInstance) {

            this.$uibModalInstance = $uibModalInstance;
            this.feedMappingService = feedMappingService;
            this.operations = operations;

            /**
             * 画面传递的上下文参数
             * @type {{feedCategoryPath:string}}
             */
            this.context = context;
            /**
             * feed 类目的所有属性
             * @type {object[]}
             */
            this.feedAttributes = null;
            /**
             * 关联的设置
             * @type {{type: string, condition: boolean}}
             */
            this.mappingSetting = {
                type: null,
                condition: false
            };
            /**
             * 条件
             * @type {{property: string, operation: Operation, value: string}}
             */
            this.condition = {
                property: null,
                operation: null,
                value: null
            };
            /**
             * 当前编辑的值
             * @type {{feed: string, text: string}}
             */
            this.value = {
                feed: null,
                text: null
            };
        }

        FeedPropValuePopupController.prototype = {
            init: function () {

                this.feedMappingService.getFeedAttrs({
                    feedCategoryPath: this.context.feedCategoryPath
                }).then(function (res) {

                    this.feedAttributes = res.data;
                }.bind(this));
            },
            ok: function () {

                var type = this.mappingSetting.type;

                if (!type) {
                    alert('必须匹配一类值才能保存.');
                    return;
                }

                var value = type === 'feed' ? this.value.feed : this.value.text;

                if (!value) {
                    alert('没有匹配到任何属性或内容上.');
                    return;
                }

                this.$uibModalInstance.close({
                    type: type,
                    value: value,
                    condition: [this.condition]
                });
            },
            cancel: function () {
                this.$uibModalInstance.dismiss('cancel');
            }
        };

        return FeedPropValuePopupController;
    })());
});