/**
 * platformPropMappingController
 */
define([
    'cms',
    'underscore',
    'modules/cms/enums/FieldTypes',
    'modules/cms/controller/popup.ctl',
    'modules/cms/views/setting/platform_mapping/prop.item.d'
], function (cms, _, FieldTypes) {
    'use strict';
    return cms.controller('platformPropMappingController', (function () {

        function PlatformMappingController(platformMappingService, $routeParams) {

            this.platformMappingService = platformMappingService;

            this.mainCategoryId = $routeParams['mainCategoryId'];

            this.cartId = parseInt($routeParams['cartId']);

            /**
             * 平台类目
             * @type {object}
             */
            this.category = null;
            /**
             * 平台类目属性的 Map, Key 为属性名, 值为 Field
             * @type {object}
             */
            this.properties = null;
            /**
             * 包含 Mapping Matched 信息的 Map
             * @type {object}
             */
            this.mappings = null;
            /**
             * 平台属性的 Map 备份
             * @type {object}
             */
            this.backupProperties = null;

            this.selected = {
                required: null,
                matched: null
            };
        }

        PlatformMappingController.prototype = {
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

                this.platformMappingService.getPlatformCategory({
                    categoryId: this.mainCategoryId,
                    cartId: this.cartId
                }).then(function (res) {

                    this.category = res.data.categorySchema;
                    this.properties = res.data.properties;
                    this.mappings = res.data.mapping;

                }.bind(this));
            },
            filteringData: function () {

                _.each(this.properties, function (property) {
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

                var context = {
                    mainCategoryId: this.mainCategoryId,
                    platformCategoryPath: this.category.catFullPath,
                    property: property
                };

                switch (property.type) {
                    case FieldTypes.complex:
                        ppPlatformMapping.complex(context);
                        break;
                    case FieldTypes.multiComplex:
                        break;
                    default: // simple ~
                        ppPlatformMapping.simple(context);
                        break;
                }
            }
        };

        return PlatformMappingController;

    })());
});