/**
 * @ngdoc
 * @controller
 * @name platformPropMappingController
 */

/**
 * @typedef {object} PlatformInfo
 * @property {object} category CategorySchema
 * @property {object} properties FieldMap
 * @property {object} mappingModel
 * @property {object} mappingTypes MappingTypeMap
 */

define([
    'cms',
    'underscore',
    'modules/cms/enums/MappingTypes',
    'modules/cms/controller/popup.ctl',
    'modules/cms/views/setting/platform_mapping/prop.item.d'
], function (cms, _, MappingTypes) {
    'use strict';
    return cms.controller('platformPropMappingController', (function () {

        function PlatformPropMappingController(platformPropMappingService, $routeParams, alert) {

            this.dataService = platformPropMappingService;
            this.alert = alert;

            this.mainCategoryId = $routeParams['mainCategoryId'];
            this.cartId = parseInt($routeParams['cartId']);

            /**
             * @type {PlatformInfo}
             */
            this.platform = null;

            this.selected = {
                required: null,
                matched: null
            };
        }

        PlatformPropMappingController.prototype = {
            options: {
                required: {
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
                var $ = this;
                $.dataService.getPlatformData($.mainCategoryId, $.cartId).then(function (data) {
                    $.platform = data;
                });
            },
            filteringData: function () {
                _.each(this.platform.properties, function (property) {
                    this.setHide(property);
                }.bind(this));
            },
            setHide: function (property) {

                // 如果是简单类型
                // 如果强制显示, 则直接显示, 否则计算显示
                if (property.isSimple)
                    return property.hide = (
                        this.selected.required !== null && this.selected.required !== (property.required || property.parentRequired)
                    );

                // 复杂类型计算前, 默认其不显示
                property.hide = true;

                _.each(property.fieldList, function (child) {

                    // 如果子级有需要显示, 则父级跟随显示
                    if (!this.setHide(child))
                        property.hide = false;

                }.bind(this));
            },
            /**
             * 更具属性类型,选择打开 Mapping 弹出框
             * @param property 属性
             * @param ppPlatformMapping 包含不同弹出框的对象
             */
            popup: function (property, ppPlatformMapping) {

                var category = this.platform.category;
                var context = {
                    mainCategoryId: this.mainCategoryId,
                    platformCategoryPath: category.catFullPath,
                    platformCategoryId: category.catId,
                    property: property,
                    cartId: this.cartId
                };

                switch (property.mapping.type) {
                    case MappingTypes.SIMPLE_MAPPING:
                        ppPlatformMapping.simple.list(context);
                        break;
                    case MappingTypes.COMPLEX_MAPPING:
                        ppPlatformMapping.complex(context);
                        break;
                    case MappingTypes.MULTI_COMPLEX_MAPPING:
                        ppPlatformMapping.multiComplex.list(context);
                        break;
                    default:
                        throw 'Unknown mapping type: ' + property.mapping.type;
                }
            }
        };

        return PlatformPropMappingController;

    })()).service('platformPropMappingService', (function () {

        /**
         * 用于封装属性 Mapping 相关的数据操作, 用于在画面和 item directive 间共享
         * @constructor
         */
        function PlatformPropMappingService(platformMappingService, $q) {
            this.$service = platformMappingService;
            this.$q = $q;

            /**
             * @type {PlatformInfo}
             */
            this.platform = null;
        }

        PlatformPropMappingService.prototype = {

            /**
             * 根据主数据目录,通过Mapping,获取完整的平台数据信息
             * @param categoryId MainCategory ID
             * @param cartId
             * @returns {Promise.<PlatformInfo|null>}
             */
            getPlatformData: function (categoryId, cartId) {

                // 因为后续的操作理论上都是在画面加载后进行的.所以后续可能不传递参数
                // 因此需要在此处特殊检查
                if (this.platform || !categoryId || !cartId) {
                    var deferred = this.$q.defer();
                    deferred.resolve(this.platform);
                    return deferred.promise;
                }

                // 木有数据,再去后台拿
                return this.$service.getPlatformCategory({
                    categoryId: categoryId,
                    cartId: cartId
                }).then(function (res) {
                    this.platform = {
                        category: res.data.categorySchema,
                        properties: res.data.properties,
                        mappingModel: res.data.mapping
                    };
                }.bind(this)).then(function () {
                    var platform = this.platform;
                    this.$service.getMappingTypes({
                        cartId: cartId,
                        platformCategoryId: platform.catId
                    }).then(function (res) {
                        platform.mappingTypes = res.data;
                    });
                    return platform;
                }.bind(this));
            },

            /**
             * @param property
             * @returns {Promise.<string|null>}
             */
            getMappingType: function(property) {
                return this.getPlatformData().then(function (platform) {
                    if (!platform) return null;
                    return platform.mappingTypes[property.id];
                });
            }
        };

        return PlatformPropMappingService;

    })());
});