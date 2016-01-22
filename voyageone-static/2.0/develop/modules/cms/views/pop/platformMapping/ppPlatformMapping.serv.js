define([
    'cms',
    'underscore',
    'modules/cms/enums/FieldTypes',
    'modules/cms/enums/MappingTypes'
], function (cms, _, FieldTypes, MappingTypes) {
    'use strict';
    return cms.service('ppPlatformMappingService', (function () {

        function PopupPlatformMappingService(platformMappingService, $q, cookieService) {
            this.platformMappingService = platformMappingService;
            this.$q = $q;
            this.cookieService = cookieService;

            /**
             * 主数据类目缓存 Map.
             * 已查询的会存入.
             * @type {object}
             */
            this.mainCategories = {};
            /**
             * 根据渠道存储的字典数据
             * @type {object}
             */
            this.dictMap = {};
            /**
             * 平台类目匹配的字典数据
             * @type {object}
             */
            this.mappingMap = {};
        }

        PopupPlatformMappingService.prototype = {
            /**
             * 获取主数据类目的属性及 SKU 级属性
             * @param {string} mainCategoryId
             * @returns {Promise.<Field[]>}
             */
            getMainCategoryPropsWithSku: function (mainCategoryId) {
                return this.$getMainCategorySchema(mainCategoryId).then(function (mainCategorySchema) {
                    return mainCategorySchema.fields.concat([mainCategorySchema.sku]);
                });
            },
            /**
             * 获取主数据类目属性的简化格式, 只包含 id 和 name
             * @param {string} mainCategoryId
             * @returns {Promise.<Field[]>}
             */
            getMainCategoryProps: function (mainCategoryId) {
                return this.$getMainCategorySchema(mainCategoryId).then(function (mainCategorySchema) {
                    return mainCategorySchema.fields;
                });
            },
            /**
             * 获取主数据类目的 SKU 级属性
             * @param {string} mainCategoryId
             * @returns {Promise.<Field>}
             */
            getMainCategorySkuProp: function (mainCategoryId) {
                return this.$getMainCategorySchema(mainCategoryId).then(function (mainCategorySchema) {
                    return mainCategorySchema.sku;
                });
            },
            /**
             * 获取主数据类目的类目路径
             * @param {string} mainCategoryId
             * @returns {Promise.<string>}
             */
            getMainCategoryPath: function (mainCategoryId) {
                return this.$getMainCategorySchema(mainCategoryId).then(function (mainCategorySchema) {
                    return mainCategorySchema.catFullPath;
                });
            },
            /**
             * 获取字典数据
             */
            getDictList: function () {

                var defer = this.$q.defer();

                var channelId = this.cookieService.channel();

                var dictList = this.dictMap[channelId];

                if (dictList && dictList.length) {
                    defer.resolve(dictList);
                } else {
                    this.platformMappingService.getDictList().then(function (res) {

                        var dictList = res.data;
                        var first = dictList[0];

                        this.dictMap[first ? first.order_channel_id : channelId] = dictList;

                        // 最终返回的以前台为准
                        defer.resolve(this.dictMap[channelId]);

                    }.bind(this));
                }

                return defer.promise;
            },

            /**
             * 根据 masterWord 查找完整字段路径
             * @param {string} mainCategoryId
             * @param {MasterWord} masterWord
             * @return {Promise.<Field[]>}
             */
            getPropertyPath: function (mainCategoryId, masterWord) {

                return this.$getMainCategorySchema(mainCategoryId)
                    .then(function (mainCategorySchema) {
                        return this.$searchProperty(mainCategorySchema.fields, masterWord.value);
                    }.bind(this));
            },

            /**
             * 搜索主数据属性
             * @param {Field[]} properties 属性集合
             * @param {string} propertyId 属性 ID
             * @returns {Field[]}
             */
            $searchProperty: function (properties, propertyId) {

                var result = null;

                _.each(properties, function (property) {

                    if (property.id === propertyId) {
                        result = [property];
                        // 找到了, 不用继续
                        return false;
                    }

                    // 继续下一个
                    if (!property.fields || !property.fields.length)
                        return true;

                    // 在子属性内查找
                    var childResult = this.$searchProperty(property.fields, propertyId);

                    // 子属性没找到, 继续下一个
                    if (!childResult) return true;

                    // 找到了, 就把当前的父级别追加进去, 并不再继续
                    childResult.push(property);
                    result = childResult;
                    return false;

                }.bind(this));

                return result;
            },

            /**
             * @class
             * @name WrapField
             * @extends {Field}
             * @property {WrapField} parent
             */

            /**
             * 查询平台属性的匹配
             * @param {WrapField} property
             * @param {string} mainCategoryId
             * @param {string} platformCategoryId
             * @param {number} cartId
             * @return {Promise.<MappingBean|MappingBean[]>}
             */
            getPlatformPropertyMapping: function (property, mainCategoryId, platformCategoryId, cartId) {

                return this.$getPlatformMapping(mainCategoryId, platformCategoryId, cartId).then(function (mappings) {

                    // 搜索属性 mapping
                    // 理论上传递的 property 包含 parent 信息
                    // 所以依据 parent 查找
                    // 如果 prop.item.d 中不再为 property 包装 parent 信息, 则此处需要修改

                    if (!property) return null;

                    return this.$searchMappingByParent(property, mappings);

                }.bind(this));
            },
            /**
             * 通过 directive prop.item.d 包装的 parent 信息, 在 mapping 中查找具体属性的 mapping 信息
             * @private
             * @param {WrapField} property 平台类目属性
             * @param {MappingBean[]} mappings
             * @return {MappingBean|MappingBean[]}
             */
            $searchMappingByParent: function (property, mappings) {

                var multiMappings = null;

                if (property.parent) {
                    // 有父级时, 先查找父级的 mapping, 覆盖当前 mappings
                    var parent = property.parent;
                    var parentMapping = this.$searchMappingByParent(parent, mappings);
                    // 不同类型的父级, 需要不同的处理
                    switch (parent.type) {
                        case FieldTypes.complex:
                            mappings = parentMapping ? parentMapping.subMapping : null;
                            break;
                        case FieldTypes.multiComplex:

                            // 如果是多复杂类型, 有可能返回的是数组
                            switch (parentMapping) {
                                case MappingTypes.COMPLEX_MAPPING:
                                    mappings = parentMapping ? parentMapping.subMapping : null;
                                    break;
                                case MappingTypes.MULTI_COMPLEX_MAPPING:
                                    multiMappings = parentMapping;
                                    break;
                                default:
                                    throw 'Unsupported mapping type.';
                            }

                            break;
                        default:
                            throw 'Unsupported parent type.';
                    }
                }

                // 说明找不到
                if (!mappings) return null;

                // 非 Multi 的情况下
                if (!multiMappings) {
                    return _.find(mappings, function (mapping) {
                        return mapping.platformPropId === property.id;
                    });
                }

                // Multi 情况下, 搜索多个
                return _.chain(multiMappings)
                    .map(function (mappings) {
                        return _.find(mappings, function (mapping) {
                            return mapping.platformPropId === property.id;
                        });
                    })
                    .filter(function (mapping) {
                        return !!mapping;
                    })
                    .value();
            },
            /**
             * @private
             * @param {string} mainCategoryId
             * @param {string} platformCategoryId
             * @param {number} cartId
             */
            $getPlatformMapping: function (mainCategoryId, platformCategoryId, cartId) {

                var deferred = this.$q.defer();
                var key = mainCategoryId + '->' + platformCategoryId + '@' + cartId;
                var mapping = this.mappingMap[key];

                if (mapping) {
                    var a = deferred.resolve(mapping);
                    return deferred.promise;
                }

                this.platformMappingService.getPlatformMapping({
                    mainCategoryId: mainCategoryId,
                    platformCategoryId: platformCategoryId,
                    cartId: cartId
                }).then(function (res) {
                    deferred.resolve(this.mappingMap[key] = res.data);
                }.bind(this));

                return deferred.promise;
            },
            /**
             * @private
             * @param {string} mainCategoryId
             * @return {Promise}
             */
            $getMainCategorySchema: function (mainCategoryId) {

                var defer = this.$q.defer();

                var mainCategorySchema = this.mainCategories[mainCategoryId];

                if (mainCategorySchema) {
                    defer.resolve(mainCategorySchema);
                    return defer.promise;
                }

                this.platformMappingService.getMainCategorySchema({
                    mainCategoryId: mainCategoryId
                }).then(function (res) {
                    defer.resolve(this.mainCategories[mainCategoryId] = res.data);
                }.bind(this));

                return defer.promise;
            }
        };

        return PopupPlatformMappingService;

    })());
});