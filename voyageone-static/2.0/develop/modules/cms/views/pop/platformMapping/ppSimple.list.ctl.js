define([
    'cms',
    'modules/cms/enums/FieldTypes',
    'modules/cms/views/pop/platformMapping/ppPlatformMapping.serv',
    'modules/cms/controller/popup.ctl'
], function (cms, FieldTypes) {
    'use strict';
    return cms.controller('simpleListMappingPopupController', (function () {

        function SimpleListMappingPopupController(context, $uibModalInstance, ppPlatformMappingService, alert) {

            this.$uibModalInstance = $uibModalInstance;
            this.context = context;
            this.ppPlatformMappingService = ppPlatformMappingService;
            this.alert = alert;

            /**
             * 主数据类目 ID
             * @type {string}
             */
            this.mainCategoryId = this.context.mainCategoryId;
            /**
             * 平台属性
             * @type {Field}
             */
            this.property = this.context.property;
            /**
             * 一组 RuleWord
             * @type {RuleWord[]}
             */
            this.ruleWords = null;
            /**
             * 主数据类目路径
             * @type {string}
             */
            this.mainCategoryPath = null;
        }

        SimpleListMappingPopupController.prototype = {
            init: function () {

                switch (this.context.property.type) {
                    case FieldTypes.complex:
                    case FieldTypes.multiComplex:
                        this.alert('当前属性不是 Simple 属性').result.then(function () {
                            this.cancel();
                        }.bind(this));
                        return;
                }

                this.ppPlatformMappingService.getMainCategoryPath(this.mainCategoryId).then(function (path) {
                    this.mainCategoryPath = path;
                }.bind(this));

                // TODO 加载原有的匹配
                this.ruleWords = [];
            },
            popup: function(ppPlatformMapping){
                // 增加 RuleWord
                ppPlatformMapping.simple.item(this.context).then(function(word){
                    this.ruleWords.push(word);
                }.bind(this));
            },
            ok: function () {

            },
            cancel: function () {
                this.$uibModalInstance.dismiss('cancel');
            }
        };

        return SimpleListMappingPopupController;

    })());
});