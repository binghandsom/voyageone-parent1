define([
    'cms',
    'underscore',
    'modules/cms/enums/FieldTypes',
    'modules/cms/enums/RuleTypes',
    'modules/cms/enums/MappingTypes'
], function (cms, _, FieldTypes, RuleTypes, MappingTypes) {

    function switchMappingType(field, type) {
        switch (field.type) {
            case FieldTypes.complex:
                return MappingTypes.COMPLEX_MAPPING;
            case FieldTypes.multiComplex:
                return type || MappingTypes.COMPLEX_MAPPING;
            default:
                return MappingTypes.SIMPLE_MAPPING;
        }
    }

    function isRequiredField(field) {

        return !!_.find(field.rules, function (rule) {
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
            })
                .then(function (res) {
                    return that.platform = {
                        category: res.data.categorySchema,
                        properties: res.data.properties,
                        mappingInfo: res.data.mapping,
                        matchOver: res.data.matchOver
                    };
                })

                .then(function (platform) {

                    // 从后台获取平台的完整关联关系数据
                    // 包括属性的
                    that.getPlatformMapping(categoryId, platform.category.catId, cartId);

                    // 使用底层 service 从后台获取, 每个 field 所对应使用的 mapping type
                    return that.$service.getMappingTypes({
                        cartId: cartId,
                        platformCategoryId: platform.catId
                    });
                })

                .then(function (res) {
                    // 保存请求获得的 field 对应 mapping type 的数据
                    that.platform.mappingTypes = res.data;
                    // 包装之前获取的 field
                    _.each(that.platform.properties, function(field) {
                        that.makeField(field);
                    });
                    // 并返回之前所组装的, 完整平台类目相关数据
                    return that.platform;
                });
        },

        /**
         * 为 field 追加附加的属性, 如样式名, 必填, 是否匹配等
         */
        makeField: function(field, parent) {

            var self = this;
            var mappingTypes = self.platform.mappingTypes;
            var type = mappingTypes[field.id];

            field.iconClass = getIconClass(field);
            field.isSimple = isSimpleType(field);
            field.headClass = field.isSimple ? 'fa-minus' : 'fa-plus';
            field.required = isRequiredField(field);
            field.matched = self.$isMatched(field);

            field.mapping = {
                type: switchMappingType(field, type),
                isMulti: type === MappingTypes.MULTI_COMPLEX_MAPPING,
                isChildOfMulti: function() {
                    if (!parent)
                        return false;
                    if (parent.mapping.isMulti)
                        return true;
                    return parent.mapping.isChildOfMulti();
                }
            };

            if (field.fieldList) {
                field.fieldList.forEach(function (child) {
                    child.parent = field;
                    self.makeField(child, field);
                });
            }
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
            }).then(function (res) {
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

        /**
         * 从平台的匹配数据中, 查找指定字段是否已经被匹配过
         * @private
         */
        $isMatched: function(field) {

            var that = this;
            var path = [field];
            var parent = field.parent;
            var mappingInfo = that.platform.mappingInfo;
            var result = false;

            while (parent) {
                path.unshift(parent);
                parent = parent.parent;
            }

            path.find(function (_field) {
                
                result = mappingInfo[_field.id];
                
                if (_.isBoolean(result))
                    return true;
                
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

            return !!result;
        },

        /**
         * 从平台的匹配数据中, 查找指定字段是否已经被匹配过
         */
        isMatched: function (field) {
            var self = this;
            return self.getPlatformData().then(function() {
                return self.$isMatched(field);
            });
        }
    };

    cms.service('platformPropMappingService', PlatformPropMappingService);

    return PlatformPropMappingService;

});