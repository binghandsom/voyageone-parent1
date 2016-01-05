/**
 * controller FeedPropMappingController
 */

define([
    'cms',
    'modules/cms/enums/FieldTypes',
    'modules/cms/enums/RuleTypes',
    'underscore',
    'modules/cms/controller/popup.ctl'
], function (cms, FieldTypes, RuleTypes, _) {
    'use strict';

    function isRequiredField(field) {

        return _.find(field.rules, function (rule) {
            return rule.name === RuleTypes.REQUIRED_RULE && rule.value === 'true';
        });
    }

    function isSimpleType(field) {

        return field.type !== FieldTypes.complex &&
            field.type !== FieldTypes.multiComplex;
    }

    function getConfig(field) {

        var iconClass = '';

        switch (field.type) {
            case FieldTypes.label:
                iconClass = 'badge badge-initialize';
                break;
            case FieldTypes.input:
                iconClass = 'badge badge-initialize';
                break;
            case FieldTypes.complex:
                iconClass = 'badge badge-refresh';
                break;
            case FieldTypes.singleCheck:
                iconClass = 'badge badge-success';
                break;
            case FieldTypes.multiCheck:
                iconClass = 'badge badge-success';
                break;
            case FieldTypes.multiComplex:
                iconClass = 'badge badge-failure';
                break;
        }

        return {
            iconClass: iconClass,
            isSimple: isSimpleType(field),
            required: isRequiredField(field)
        };
    }

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
                 * 主类目模型,不包含字段信息
                 * @type {object}
                 */
                this.mainCategory = null;
                /**
                 * 类目字段的 Map
                 * @type {object}
                 */
                this.fields = null;
                /**
                 * 共通字段的 Map
                 * @type {object}
                 */
                this.commonFields = null;
                /**
                 * 类目 SKU 级别字段 Map
                 * @type {object}
                 */
                this.skuFields = null;
                /**
                 * 已匹配的主类目属性
                 * @type {string[]}
                 */
                this.matchedMains = null;

                this.saveMapping = this.saveMapping.bind(this);
            }

            FeedPropMappingController.prototype = {

                init: function () {

                    this.feedMappingService.getMainProps({
                        feedCategoryPath: this.feedCategoryPath
                    }).then(function (res) {
                        // 保存主数据类目(完整类目)
                        this.mainCategory = res.data.category;
                        this.fields = res.data.fields;
                        this.skuFields = res.data.sku;
                        this.commonFields = res.data.common;

                        // 根据类目信息继续查询
                        this.feedMappingService.getMatched({
                            feedCategoryPath: this.feedCategoryPath,
                            mainCategoryPath: this.mainCategory.catFullPath
                        }).then(function (res) {
                            // 保存已匹配的属性
                            this.matchedMains = res.data;
                        }.bind(this));
                    }.bind(this));
                },
                popupContext: function (bean) {
                    return {
                        feedCategoryPath: this.feedCategoryPath,
                        mainCategoryPath: this.mainCategory.catFullPath,
                        field: bean.field,
                        bean: bean
                    };
                },
                /**
                 * 上层 popup 返回时的调用
                 * @param {{feedCategoryPath:string,mainCategoryPath:string,field:object,fieldMapping:object}} context
                 */
                saveMapping: function (context) {

                    var path = [];
                    var f = context.field;
                    path.push(f.id);
                    var p = context.bean.parent;
                    while (p) {
                        path.push(p.field.id);
                        p = p.parent;
                    }

                    this.feedMappingService.saveFieldMapping({
                        feedCategoryPath: context.feedCategoryPath,
                        mainCategoryPath: context.mainCategoryPath,
                        fieldPath: path.reverse(),
                        propMapping: context.fieldMapping
                    }).then(function (res) {

                        // 你猜
                    });
                }
            };

            return FeedPropMappingController;

        })())
        .filter('fmExtendField', function () {

            return function (fields) {

                var fieldArray = _.map(fields, function (bean) {

                    bean.x = getConfig(bean.field);

                    if (bean.parentId) bean.x.parent = fields[bean.parentId];

                    return bean;
                });

                return fieldArray.sort(function (a, b) {
                    return a.seq > b.seq ? 1 : -1
                });
            }
        });
});