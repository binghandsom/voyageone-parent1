/**
 * Created by sofia on 2016/8/29.
 */
define([
    'admin',
    'underscore',
    'modules/admin/enums/MappingTypes'
], function (admin, _, MappingTypes) {
    admin.controller('treeTable', ['$scope', '$filter',
        function ($scope, $filter) {
            $scope.initCheckbox = function (item, parentItem, flatList) {
                if (!item.opened) item.opened = true;
                if (item.children.length < 1) item.showArrow = false;
                flatList.push(item);
                return item.selected = parentItem && parentItem.selected || item.selected || false;
            };
            $scope.toggleCheckbox = function (item, list) {
                if (item.selected===true) {
                    list.push(item);
                }
            };
        }
    ]);
    admin.filter('selected', [
        '$filter',
        function ($filter) {
            return function (files) {
                return $filter('filter')(files, {selected: true});
            };
        }
    ]);
});