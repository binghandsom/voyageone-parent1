define([
    'cms',
    'underscore',
    'modules/cms/enums/FieldTypes',
    'modules/cms/enums/RuleTypes',
    'modules/cms/controller/popup.ctl'
], function (cms, _, FieldTypes, RuleTypes) {
    'use strict';

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

    return cms.directive('platformProp', function () {
        return {
            restrict: 'A',
            // 地址基于地址栏定位, 启动的画面变动, 这里也要跟随变动
            templateUrl: 'views/setting/platform_mapping/prop.item.d.html',
            scope: {
                property: '=platformProp',
                parent: '=platformPropParent',
                popup: '&platformPropPopup'
            },
            link: function ($scope) {

                var property = $scope.property;
                var parent = property.parent = $scope.parent;

                // 计算每个属性的属性
                property.iconClass = getIconClass(property);
                property.isSimple = isSimpleType(property);
                property.required = isRequiredField(property);
                property.parentRequired = !parent ? false : (parent.required || parent.parentRequired);
                property.headClass = property.isSimple ? 'fa-minus' : 'fa-plus';
            }
        }
    });
});