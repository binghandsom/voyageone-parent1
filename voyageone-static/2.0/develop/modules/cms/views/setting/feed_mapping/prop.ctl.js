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

    function getIconClass(field) {

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

        return iconClass;
    }

    function hasRequired(bean, beans, required) {

        if (!bean.children || !bean.children.length) {
            return (!!bean.required) === required;
        }
        var child = _.find(bean.children, function(id) {
            return hasRequired(beans[id], beans, required);
        });

        return !!child;
    }

    function toArray(beans) {

        // 转换为包装后的数组
        var beanArray = _.map(beans, function (bean) {

            if (bean.parentId) {
                var parent
                    = bean.parent
                    = beans[bean.parentId];

                if (!parent.children) {
                    parent.children = [];
                }

                parent.children.push(bean.field.id);
            }

            bean.iconClass = getIconClass(bean.field);
            bean.isSimple = isSimpleType(bean.field);
            bean.required = isRequiredField(bean.field);
            bean.matched = _.contains(toArray.matchedMains, bean.field.id);

            return bean;
        });

        // 设定字段,是否其子字段为必填
        // 用于过滤
        _.each(beanArray, function(bean) {
            bean.hasRequired = hasRequired(bean, beans, true);
            bean.hasOptional = hasRequired(bean, beans, false);
        });

        // 整理展示顺序
        return beanArray.sort(function (a, b) {
            return a.seq > b.seq ? 1 : -1
        });
    }

    /**
     * 已匹配的主类目属性
     * @type {string[]}
     */
    toArray.matchedMains = null;

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
                 * 上述字段的真实绑定对象
                 * @type {{fields: object[], common: object[], sku: object[]}}
                 */
                this.dataSources = {
                    fields: null,
                    common: null,
                    sku: null
                };
                /**
                 * 显示的过滤条件
                 * @type {{hasRequired: boolean|null, matched: boolean|null}}
                 */
                this.show = {
                    hasRequired: null,
                    matched: null
                };

                this.saveMapping = this.saveMapping.bind(this);
            }

            FeedPropMappingController.prototype = {

                options: {
                    hasRequired: {
                        '必填情况(ALL)': null,
                        '非必填': false,
                        '必填': true
                    },
                    matched: {
                        '设定情况(ALL)': null,
                        '已设定': true,
                        '未设定': false
                    }
                },

                init: function () {

                    this.feedMappingService.getMainProps({
                        feedCategoryPath: this.feedCategoryPath
                    }).then(function (res) {

                        // 保存主数据类目(完整类目)
                        this.mainCategory = res.data.category;

                        // 保存字段 Map 数据
                        this.fields = res.data.fields;
                        this.skuFields = res.data.sku;
                        this.commonFields = res.data.common;

                        // 根据类目信息继续查询
                        this.feedMappingService.getMatched({
                            feedCategoryPath: this.feedCategoryPath,
                            mainCategoryPath: this.mainCategory.catFullPath
                        }).then(function (res) {

                            // 保存已匹配的属性
                            toArray.matchedMains = res.data;

                            // 包装数据源
                            this.dataSources.fields = toArray(this.fields);
                            this.dataSources.common = toArray(this.commonFields);
                            this.dataSources.sku = toArray(this.skuFields);

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
                },
                /**
                 * 确定某字段是否要显示
                 * @param {object} bean
                 * @return {boolean}
                 */
                shown: function(bean) {
                    var result = true;
                    if (this.show.hasRequired !== null) {
                        result = (this.show.hasRequired ? bean.hasRequired : bean.hasOptional);
                    } else if (this.show.matched !== null) {
                        result = this.show.matched === bean.matched;
                    }
                    return result;
                }
            };

            return FeedPropMappingController;

        })());
});