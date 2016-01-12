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
            /**
             * 主数据类目
             * @type {object[]}
             */
            this.mainCategories = null;
            /**
             * 映射路径
             * @type {object}
             */
            this.mappings = null;
        }

        PlatformMappingController.prototype = {

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

                    this.mappings = res.data.mappings;

                }.bind(this));
            },

            popupMapping: function (category, popupNewCategory) {

                this.platformMappingService.getPlatformCategory({
                    cartId: this.selected.cart
                }).then(function (res) {

                    if (!res.data || !res.data.length) {
                        this.alert('没有取到任何平台类目... 尝试换一个平台试试 ?');
                        return;
                    }

                    popupNewCategory({
                        from: category.catPath,
                        categories: res.data
                    }).then(function (context) {

                        this.platformMappingService.setPlatformMapping({
                            from: category.catId,
                            to: context.selected.catId,
                            cartId: this.selected.cart
                        }).then(function() {

                            this.mappings[category.catId] = context.selected.catPath;

                        }.bind(this));
                    }.bind(this))
                }.bind(this));
            }
        };

        return PlatformMappingController;
    })());
});