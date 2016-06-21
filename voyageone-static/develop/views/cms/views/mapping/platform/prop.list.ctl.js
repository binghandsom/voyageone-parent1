/**
 * @ngdoc
 * @controller
 * @name platformPropMappingController
 */

define([
    'cms',
    'underscore',
    'modules/cms/controller/popup.ctl',
    './prop.item.d',
    './PlatformPropMapping.service'
], function (cms, _) {
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
                required: true,
                matched: false,
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
                    $.filteringData();
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
                this.filteringData();
            },

            setHide: function (field) {

                var self = this;
                var keyWord = self.selected.keyWord;
                var matched = self.selected.matched;
                var required = self.selected.required;
                var hide = false;

                // 如果是简单类型
                // 如果强制显示, 则直接显示, 否则计算显示
                if (field.isSimple) {

                    if (required !== null)
                        hide = field.required !== required;

                    if (!hide && matched !== null)
                        hide = field.matched !== matched;

                    if (!hide && keyWord)
                        hide = field.name.indexOf(keyWord) < 0;

                    return field.hide = hide;
                }

                // 复杂类型计算前, 默认其不显示
                hide = true;

                field.fieldList.forEach(function (child) {

                    // 如果子级有需要显示, 则父级跟随显示
                    if (!self.setHide(child))
                        hide = false;

                });

                return field.hide = hide;
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

    })());
});