/**
 * controller FeedPropMappingController
 */

define([
    'cms',
    'json!modules/cms/enums/FieldTypes.json',
    'json!modules/cms/enums/RuleTypes.json',
    'modules/cms/controller/popup.ctl'
], function (cms, FieldTypes, RuleTypes) {
    'use strict';
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

            /**
             * 主类目模型
             * @type {object}
             */
            this.mainCategory = null;
            /**
             * 已匹配的主类目属性
             * @type {string[]}
             */
            this.matchedMains = null;
            /**
             * 匹配关系对象
             * @type {object}
             */
            this.mapping = null;
        }

        FeedPropMappingController.prototype = {

            init: function () {

                this.feedMappingService.getMainProps({
                    feedCategoryPath: this.feedCategoryPath
                }).then(function (res) {

                    this.mainCategory = res.data;

                    this.feedMappingService.getMatched({
                        feedCategoryPath: this.feedCategoryPath,
                        mainCategoryPath: this.mainCategory.catFullPath
                    }).then(function (res) {

                        this.matchedMains = res.data;
                    }.bind(this));

                    this.feedMappingService.getMapping({
                        feedCategoryPath: this.feedCategoryPath,
                        mainCategoryPath: this.mainCategory.catFullPath
                    }).then(function (res) {

                        this.mapping = res.data;
                    }.bind(this));
                }.bind(this));
            }
        };


        return FeedPropMappingController;

    })()).directive('feedMappingFields', function () {
        return {
            restrict: 'A',
            templateUrl: 'feedMapping.fields.tpl.html',
            scope: {
                fields: '=feedMappingFields'
            },
            controllerAs: 'ctrl',
            controller: (function () {
                function FeedMappingFieldsController() {

                }

                FeedMappingFieldsController.prototype = {
                    isRequiredField: function (field) {

                        return _.find(field.rules, function (rule) {
                            return rule.name === RuleTypes.REQUIRED_RULE && rule.value === 'true';
                        });
                    },

                    isSimpleType: function (field) {

                        return field.type !== FieldTypes.complex &&
                            field.type !== FieldTypes.multiComplex;
                    },

                    getConfig: function (field) {
                        return {
                            isSimple: this.isSimpleType(field),
                            required: this.isRequiredField(field)
                        };
                    }
                };

                return FeedMappingFieldsController;
            })()
        };
    });
});