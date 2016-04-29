define([
    'cms',
    'underscore',
    'modules/cms/enums/FieldTypes',
    'modules/cms/enums/MappingTypes'
], function (cms, _, FieldTypes, MappingTypes) {
    'use strict';
    return cms.service('ppPlatformMappingService', (function () {

        function $WrapFieldId(id) {
            return id.replace(/\./g, '->');
        }

        function PopupPlatformMappingService(platformMappingService, platformPropMappingService, $q, cookieService) {

            this.pmService = platformMappingService;
            this.ppService = platformPropMappingService;
            this.$q = $q;
            this.cookieService = cookieService;

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
             * 获取主数据类目属性的简化格式, 只包含 id 和 name
             * @param {string} mainCategoryId
             * @returns {Promise.<Field[]>}
             */
            getMainCategoryProps: function (mainCategoryId, withSku) {
                return this.$getMainCategorySchema(mainCategoryId).then(function (mainCategorySchema) {
                    var fields = _.clone(mainCategorySchema.fields);
                    if (withSku) fields.push(mainCategorySchema.sku);
                    return fields;
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
             * 保存一个 Mapping
             * @param {string} mainCategoryId 主数据类目 ID
             * @param {string} platformCategoryId 平台类目 ID
             * @param {number} cartId 平台 ID
             * @param {MappingBean} mapping 将保存的 Mapping
             * @param {Array.<WrapField|number>} path Mapping 存储路径
             * @returns {Promise.<boolean>}
             */
            saveMapping: function (mainCategoryId, platformCategoryId, cartId, mapping, path) {

                path = _.map(_.clone(path).reverse(), function (item) {
                    if (_.isNumber(item)) return item;
                    return item.id;
                });

                return this.pmService.$saveMapping({
                    mainCategoryId: mainCategoryId,
                    platformCategoryId: platformCategoryId,
                    cartId: cartId,
                    mappingBean: mapping,
                    mappingPath: path
                }).then(function (res) {
                    // java return top MappingBean
                    var mappingBean = res.data;

                    if (!mappingBean) return false;

                    return this.$getPlatformMapping(mainCategoryId, platformCategoryId, cartId).then(function (mappingModel) {

                        var index = _.findIndex(mappingModel.props, function (propertyMapping) {
                            return propertyMapping.platformPropId === mappingBean.platformPropId;
                        });

                        if (index > -1) {
                            mappingModel.props[index] = mappingBean;
                        } else {
                            mappingModel.props.push(mappingBean);
                        }

                        return true;
                    });
                }.bind(this));
            },

            /**
             * 获取字典数据
             */
            getDictList: function (cartId) {

                var defer = this.$q.defer();

                var channelId = this.cookieService.channel();

                var dictList = this.dictMap[channelId];

                if (dictList && dictList.length) {
                    defer.resolve(dictList);
                } else {
                    this.pmService.getDictList({
                        cartId: cartId
                    }).then(function (res) {

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
             * @param {string} propertyId
             * @param withSku
             * @return {Promise.<Field[]>}
             */
            getPropertyPath: function (mainCategoryId, propertyId, withSku) {

                return this.$getMainCategorySchema(mainCategoryId)
                    .then(function (mainCategorySchema) {
                        var fields = _.clone(mainCategorySchema.fields);
                        if (withSku) fields.push(mainCategorySchema.sku);
                        return this.$searchProperty(fields, propertyId);
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
             * 查询平台属性的匹配
             *
             * @param {object[]} path Mapping 路径, 顺序为倒序
             * @param {string} mainCategoryId
             * @param {string} platformCategoryId
             * @param {number} cartId
             * @return {Promise.<MappingBean|MappingBean[]>}
             */
            getPlatformPropertyMapping: function (path, mainCategoryId, platformCategoryId, cartId) {

                return this.$getPlatformMapping(mainCategoryId, platformCategoryId, cartId).then(function (model) {

                    if (!path || !path.length) return null;

                    var mappings = model.props;
                    var mapping = null;
                    var values;

                    _.each(_.clone(path).reverse(), function (item) {

                        if (_.isNumber(item)) {
                            mappings = values[item].subMappings;
                            return;
                        }

                        mapping = _.find(mappings, function (mapping) {
                            return mapping.platformPropId === $WrapFieldId(item.id);
                        });

                        if (!mapping) return false;

                        switch (mapping.mappingType) {
                            case MappingTypes.COMPLEX_MAPPING:
                                mappings = mapping.subMappings;
                                values = null;
                                break;
                            case MappingTypes.MULTI_COMPLEX_MAPPING:
                                values = mapping.values;
                                mappings = null;
                                break;
                            default:
                                mappings = null;
                                values = null;
                                break;
                        }
                    });

                    return mapping;
                }.bind(this));
            },

            /**
             * @private
             * @param {string} mainCategoryId
             * @param {string} platformCategoryId
             * @param {number} cartId
             */
            $getPlatformMapping: function (mainCategoryId, platformCategoryId, cartId) {
                return this.ppService.getPlatformMapping(mainCategoryId, platformCategoryId, cartId);
            },

            /**
             * @private
             */
            $getMainCategorySchema: function (mainCategoryId) {
                return this.ppService.getMainCategorySchema(mainCategoryId);
            }
        };

        return PopupPlatformMappingService;

    })());
});