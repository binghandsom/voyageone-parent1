/**
 * complexMappingPopupController
 */

define([
    'cms',
    'underscore',
    'modules/cms/enums/FieldTypes',
    'modules/cms/models/ComplexMappingBean',
    'modules/cms/views/pop/platformMapping/ppPlatformMapping.serv'
], function (cms, _, FieldTypes, ComplexMappingBean) {
    'use strict';
    return cms.controller('complexMappingPopupController', (function () {

        /**
         * Complex Mapping 弹出框的 Controller
         * @param {SimpleListMappingPopupContext} context
         * @param $uibModalInstance
         * @param {PopupPlatformMappingService} ppPlatformMappingService
         * @param notify 参见 Components/Angular/Factory/Notify
         * @constructor
         */
        function ComplexMappingPopupController(context, $uibModalInstance, ppPlatformMappingService, notify) {

            // 保存依赖
            this.$modal = $uibModalInstance;
            this.context = context;
            this.ppService = ppPlatformMappingService;
            this.notify = notify;

            // 保存当前属性
            this.property = context.path[0];

            // 当前可选的所有属性
            this.options = {
                /**
                 * @type {PropGroup[]}
                 */
                propGroups: null
            };

            /**
             * @type {ComplexMappingBean}
             */
            this.complexMapping = null;

            this.selectedValue = null;
        }

        ComplexMappingPopupController.prototype = {
            init: function () {
                var $ = this;
                var $mainCate = this.context.maindata.category;
                var $service = this.ppService;
                var $platform = this.context.platform;

                // 尝试加载原有数据
                $service.getPlatformPropertyMapping(
                    $.context.path, $mainCate.id, $platform.category.id, $.context.cartId
                ).then(function (complexMapping) {

                    if (!complexMapping)
                    // 新建默认
                        complexMapping = new ComplexMappingBean(
                            $.property.id,
                            null,
                            []
                        );

                    $.complexMapping = complexMapping;
                    $.selectedValue = complexMapping.masterPropId;
                    $.loadValue();
                });
            },
            loadValue: function () {
                var me = this;
                var $service = this.ppService;
                var $mainCate = this.context.maindata.category;
                var $mapping = this.complexMapping;
                var $options = this.options;

                $service.getMainCategoryProps($mainCate.id, true).then(function (props) {

                    $options.propGroups = [];
                    props = me.filterCurrent(props);

                    // 加载第一级下拉菜单
                    // 同时根据 Mapping 设定默认选中值
                    if (!$mapping.masterPropId) {
                        // 加载的 Mapping 无默认选中内容
                        $options.propGroups.push({
                            selected: null,
                            props: props
                        });
                        return;
                    }

                    // 获取默认选中值,所在的属性路径
                    $service.getPropertyPath($mainCate.id, $mapping.masterPropId, true).then(function (properties) {
                        if (!properties) {
                            $options.propGroups.push({selected: null, props: props});
                            return;
                        }

                        _.each(_.clone(properties).reverse(), function (property) {
                            $options.propGroups.push({selected: property, props: props});
                            props = me.filterCurrent(property.fields);
                        });
                    });
                });
            },

            loadNext: function (propGroup, $index) {
                var propGroups = this.options.propGroups;
                var prop = propGroup.selected;
                var children = this.filterCurrent(prop.fields);
                // 清空指定级别以下的所有数据
                propGroups.splice($index + 1);
                if (children.length)
                    propGroups.push({selected: null, props: children});
                this.setSelectedValue();
            },

            setSelectedValue: function () {

                var $propGroups = this.options.propGroups;
                var index = $propGroups.length - 1;
                var group = $propGroups[index];

                while (!group.selected && index >= 0) {
                    group = $propGroups[index--];
                }

                this.selectedValue = group.selected ? group.selected.id : '';
            },

            filterCurrent: function (fieldArr) {
                var me = this;
                var filter;
                switch (me.property.type) {
                    case FieldTypes.complex:
                        filter = function (f) {
                            return f.type === FieldTypes.complex;
                        };
                        break;
                    case FieldTypes.multiComplex:
                        filter = function (f) {
                            return f.type === FieldTypes.complex || f.type === FieldTypes.multiComplex;
                        };
                        break;
                }
                return _.filter(fieldArr, filter);
            },

            ok: function () {

                var me = this;
                var $modal = me.$modal;
                var $platform = me.context.platform;
                var $notify = me.notify;

                this.complexMapping.masterPropId = this.selectedValue;

                this.ppService.saveMapping(
                    me.context.maindata.category.id,
                    $platform.category.id,
                    me.context.cartId,
                    me.complexMapping,
                    me.context.path)
                    .then(function (updated) {
                        if (updated)
                            $notify.success('已更新');
                        else
                            $notify.warning('没有更新任何数据');

                        // 维护 Context 中的 Path, 让对应的属性和窗口同时结束生命周期
                        me.context.path.shift();
                        $modal.close(updated);
                    });
            },
            cancel: function () {
                this.$modal.dismiss();
            }
        };

        return ComplexMappingPopupController;

    })());
});