/**
 * @ngdoc
 * @controller
 * @name platformPropMappingController
 */

/**
 * @typedef {object} PlatformInfo
 * @property {object} category CategorySchema
 * @property {object} properties FieldMap
 * @property {object} mappingInfo
 * @property {object} mappingTypes MappingTypeMap
 */

define([
    'cms',
    'underscore',
    'modules/cms/enums/MappingTypes',
    'modules/cms/controller/popup.ctl',
    'modules/cms/views/mapping/platform/prop.item.d'
], function (cms, _, MappingTypes) {
    'use strict';
    return cms.controller('platformPropMappingController', (function () {

        function PlatformPropMappingController(platformPropMappingService, $routeParams, alert, notify, $translate) {

            this.dataService = platformPropMappingService;
            this.alert = alert;
            this.notify = notify;
            this.$translate = $translate;

            this.cartId = parseInt($routeParams['cartId']);

            this.maindata = {
                category: {
                    id: $routeParams['mainCategoryId']
                }
            };

            /**
             * @type {PlatformInfo}
             */
            this.platform = null;

            this.selected = {
                required: null,
                matched: null,
                keyWord: null
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
                    '已匹配': true,
                    '未匹配': false
                }
            },
            init: function () {
                var $ = this;
                var $service = this.dataService;
                var $mainCate = this.maindata.category;

                $service.getPlatformData($mainCate.id, $.cartId).then(function (data) {
                    $.platform = data;
                });

                $service.getMainCategorySchema($mainCate.id).then(function (mainCategory) {
                    $.maindata.category.schema = mainCategory;
                });
            },
            filteringData: function () {
                _.each(this.platform.properties, function (property) {
                    this.setHide(property);
                }.bind(this));
            },
            clear: function () {
                this.selected = {
                    required: null,
                    matched: null,
                    keyWord: null
                };
            },
            setHide: function (property) {

                var keyWord = this.selected.keyWord;

                // 如果是简单类型
                // 如果强制显示, 则直接显示, 否则计算显示
                if (property.isSimple) {

                    if (keyWord && property.name.indexOf(keyWord) < 0) {
                        return property.hide = true;
                    }

                    property.hide = (
                        this.selected.required !== null && this.selected.required !== (property.required || property.parentRequired)
                    );

                    return property.hide;
                }


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
                var mainCate = this.maindata.category;
                var path = [property];
                var parent = property.parent;

                while (parent) {
                    path.push(parent);
                    parent = parent.parent;
                }

                var context = {
                    maindata: {
                        category: {
                            id: mainCate.id,
                            path: mainCate.schema.catFullPath
                        }
                    },
                    platform: {
                        category: {
                            id: category.catId,
                            path: category.catFullPath,
                            model: category
                        }
                    },
                    cartId: this.cartId,
                    path: path
                };

                ppPlatformMapping(context).then(function(topPropMapping) {
                    // 窗口处理结束后, Complex 和 Simple 都会返回顶层的属性 Mapping
                    // 因为 Match 标识是计算好的. 这里不再重复计算 Top 属性
                    // 所以只简单处理
                    // 如果返回了 Mapping, 说明保存正常, 则切换状态即可
                    property.matched = topPropMapping && !property.matched;
                });
            },

            saveMatchOver: function () {
                var that = this;
                that.dataService.saveMatchOver(that.maindata.category.id, that.platform.matchOver,
                    that.cartId).then(function (matchOver) {
                    that.platform.matchOver = matchOver;
                    that.notify.success(that.$translate.instant('TXT_MSG_UPDATE_SUCCESS'));
                });
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

            /**
             * 主数据类目缓存 Map.
             * 已查询的会存入.
             * @type {object}
             */
            this.mainCategories = {};

            this.mappingMap = {};
        }

        PlatformPropMappingService.prototype = {

            /**
             * 根据主数据目录,通过Mapping,获取完整的平台数据信息
             * @param categoryId MainCategory ID
             * @param cartId
             * @returns {Promise.<PlatformInfo|null>}
             */
            getPlatformData: function (categoryId, cartId) {
                var that = this;

                // 因为后续的操作理论上都是在画面加载后进行的.所以后续可能不传递参数
                // 因此需要在此处特殊检查
                if (that.platform || !categoryId || !cartId) {
                    var deferred = that.$q.defer();
                    deferred.resolve(that.platform);
                    return deferred.promise;
                }

                // 木有数据,再去后台拿
                return that.$service.getPlatformCategory({
                    categoryId: categoryId,
                    cartId: cartId
                }).then(function (res) {
                    that.platform = {
                        category: res.data.categorySchema,
                        properties: res.data.properties,
                        mappingInfo: res.data.mapping,
                        matchOver: res.data.matchOver
                    };
                    that.getPlatformMapping(categoryId, that.platform.category.catId, cartId);
                }).then(function () {
                    var platform = that.platform;
                    that.$service.getMappingTypes({
                        cartId: cartId,
                        platformCategoryId: platform.catId
                    }).then(function (res) {
                        platform.mappingTypes = res.data;
                    });
                    return platform;
                });
            },

            /**
             * @param property
             * @returns {Promise.<string|null>}
             */
            getMappingType: function (property) {
                return this.getPlatformData().then(function (platform) {
                    if (!platform) return null;
                    return platform.mappingTypes[property.id];
                });
            },

            /**
             * @param {string} mainCategoryId
             * @return {Promise}
             */
            getMainCategorySchema: function (mainCategoryId) {

                var deferred = this.$q.defer();
                var mainCategorySchema = this.mainCategories[mainCategoryId];

                if (mainCategorySchema) {
                    deferred.resolve(mainCategorySchema);
                    return deferred.promise;
                }

                this.$service.getMainCategorySchema({
                    mainCategoryId: mainCategoryId
                }).then(function (res) {
                    deferred.resolve(this.mainCategories[mainCategoryId] = res.data);
                }.bind(this));

                return deferred.promise;
            },

            saveMatchOver: function (mainCategoryId, matchOver, cartId) {
                return this.$service.$saveMatchOverByMainCategory({
                    mainCategoryId: mainCategoryId, matchOver: matchOver, cartId: cartId
                }).then(function(res) {
                    return res.data;
                });
            },

            /**
             * @param {string} mainCategoryId
             * @param {string} platformCategoryId
             * @param {number} cartId
             */
            getPlatformMapping: function (mainCategoryId, platformCategoryId, cartId) {

                var deferred = this.$q.defer();
                var key = mainCategoryId + '->' + platformCategoryId + '@' + cartId;
                var mapping = this.mappingMap[key];

                if (mapping) {
                    deferred.resolve(mapping);
                    return deferred.promise;
                }

                this.$service.getPlatformMapping({
                    mainCategoryId: mainCategoryId,
                    platformCategoryId: platformCategoryId,
                    cartId: cartId
                }).then(function (res) {
                    deferred.resolve(this.mappingMap[key] = res.data);
                }.bind(this));

                return deferred.promise;
            },

            isMatched: function(property) {

                var that = this;
                var path = [property];
                var parent = property.parent;
                while (parent) {
                    path.unshift(parent);
                    parent = parent.parent;
                }

                return that.getPlatformData().then(function(platform) {
                    var mappingInfo = platform.mappingInfo;
                    var result = false;
                    _.find(path, function(p) {
                        result = mappingInfo[p.id];
                        if (_.isBoolean(result)) return true;
                        if (_.isArray(result)) {
                            result = !!result.length;
                            return true;
                        }
                        if (_.isObject(result)) {
                            mappingInfo = result;
                            result = !!_.keys(result).length;
                        }
                        return false;
                    });
                    return result;
                });
            }

        };

        return PlatformPropMappingService;

    })());
});