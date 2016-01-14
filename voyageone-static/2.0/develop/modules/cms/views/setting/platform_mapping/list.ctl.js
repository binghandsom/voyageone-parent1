define([
    'cms',
    'underscore',
    'modules/cms/enums/Carts',
    'modules/cms/controller/popup.ctl'
], function (cms, _, Carts) {
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
         * @param {PlatformMappingService} platformMappingService
         * @param {function} alert
         * @constructor
         */
        function PlatformMappingController(platformMappingService, alert) {

            this.alert = alert;
            this.platformMappingService = platformMappingService;
            this.carts = Carts;

            /**
             * 下拉选中
             * @type {{cart: number}}
             */
            this.selected = {
                cart: this.carts.TM.id
            };

            this.matched = {
                category: null,
                property: null
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

                this.loadCategories();
            },

            loadCategories: function () {

                if (!this.selected.cart)
                    return;

                this.platformMappingService.getMainCategory({

                    cartId: this.selected.cart.toString()

                }).then(function (res) {

                    this.mainCategories = res.data.categories;
                    var mappings = res.data.mappings;

                    // 追加附加属性
                    _.each(this.mainCategories, function (category) {

                        var map = mappings[category.catId];

                        category.matched = !!(map && map.path);

                        category.mapping = category.matched ? map.path : '未匹配';

                        category.propertyMatched = category.matched && map.matched === 1;
                    });

                }.bind(this));
            },

            popupMapping: function (category, popupNewCategory) {

                this.platformMappingService

                    .getPlatformCategory({cartId: this.selected.cart})

                    .then(function (res) {
                        if (!res.data || !res.data.length) {
                            this.alert('没有取到任何平台类目... 尝试换一个平台试试 ?');
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

                return (this.matched.category === null || this.matched.category === category.matched)
                    && (this.matched.property === null || this.matched.property === category.propertyMatched);
            }
        };

        return PlatformMappingController;
    })());
});