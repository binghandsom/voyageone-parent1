define([
    'cms',
    'underscore',
    'modules/cms/controller/popup.ctl'
], function (cms, _) {
    'use strict';
    /**
     * @ngdoc object
     * @name platformMappingController
     * @type {PlatformMappingController}
     * @description
     *
     * 平台类目到主数据类目的匹配
     */
    return cms.controller('platformMappingController', (function () {

        /**
         * @description
         * 创建平台到主数据类目匹配的画面 Controller 类
         * @param $rootScope
         * @param platformMappingService
         * @param {function} alert
         * @param $translate
         * @constructor
         */
        function PlatformMappingController($rootScope, platformMappingService, alert, $translate) {

            this.alert = alert;
            this.platformMappingService = platformMappingService;
            this.carts = [];
            this.$translate = $translate;

            /**
             * 下拉选中
             * @type {{cart: number}}
             */
            this.selected = {
                cart: null
            };

            this.matched = {
                category: null,
                property: null,
                keyWord: null
            };
            /**
             * 主数据类目
             * @type {object[]}
             */
            this.mainCategories = null;
        }

        PlatformMappingController.prototype = {

            options: {
                category: {
                    '类目匹配情况(ALL)': null,
                    '类目已匹配': true,
                    '类目未匹配': false
                },
                property: {
                    '类目属性匹配情况(ALL)': null,
                    '类目属性已匹配完成': true,
                    '类目属性未匹配完成': false
                }
            },

            init: function () {
                var self = this;
                self.platformMappingService.getCarts().then(function(res){
                    self.carts = res.data;
                    self.selected.cart = self.carts[0].value;
                    self.loadCategories();
                });
            },

            loadCategories: function () {
                var self = this;
                var selectedCart = self.selected.cart;

                if (!selectedCart)
                    return;

                self.platformMappingService.getMainCategory({
                    cartId: selectedCart
                }).then(function (res) {

                    self.mainCategories = res.data.categories;
                    var mappings = res.data.mappings;

                    // 追加附加属性
                    _.each(self.mainCategories, function (category) {

                        var map = mappings[category.catId];

                        category.matched = !!(map && map.path);

                        category.mapping = category.matched ? map.path : self.$translate.instant('TXT_INCOMPLETE');

                        category.propertyMatched = category.matched && !!map.matched;
                    });

                });
            },

            popupMapping: function (category, popupNewCategory) {

                this.platformMappingService

                    .getPlatformCategories({cartId: this.selected.cart})

                    .then(function (res) {
                        if (!res.data || !res.data.length) {
                            this.alert(this.$translate.instant('TXT_MSG_NO_PLATFORM_CATEGORY'));
                            return null;
                        }

                        return popupNewCategory({
                            from: category.catPath,
                            categories: res.data
                        });
                    }.bind(this))

                    .then(function (context) {

                        if (!context) return null;

                        this.platformMappingService.setPlatformMapping({
                            from: category.catId,
                            to: context.selected.catId,
                            cartId: this.selected.cart
                        }).then(function (res) {
                            // 更新附加属性
                            category.mapping = context.selected.catPath;
                            category.matched = true;
                            category.propertyMatched = res.data;
                        });

                    }.bind(this));
            },

            shown: function (category) {

                var cateMatched = this.matched.category;
                var propMatched = this.matched.property;
                var keyWord = this.matched.keyWord;

                if (keyWord && category.catPath.indexOf(keyWord) < 0)
                    return false;

                return (cateMatched === null || cateMatched === category.matched)
                    && (propMatched === null || propMatched === category.propertyMatched);
            },

            clear: function () {
                this.matched = {
                    category: null,
                    property: null,
                    keyWord: null
                };
            }
        };

        return PlatformMappingController;
    })());
});