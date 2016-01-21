define([
    'cms',
    'underscore'
], function (cms, _) {
    'use strict';
    return cms.service('ppPlatformMappingService', (function () {

        function PopupPlatformMappingService(platformMappingService, $q, cookieService) {
            this.platformMappingService = platformMappingService;
            this.$q = $q;
            this.cookieService = cookieService;

            /**
             * 主数据类目缓存 Map.
             * 已查询的会存入.
             * @type {Map}
             */
            this.mainCategories = {};
            /**
             * 根据渠道存储的字典数据
             * @type {Map}
             */
            this.dictMap = {};
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

                if (dictList) {
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