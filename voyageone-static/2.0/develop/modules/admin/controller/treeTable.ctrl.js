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
            $scope.toggleAllCheckboxes = function ($event, list, selectedList) {
                var i, item, len, ref, results, selected;
                selected = $event.target.checked;
                ref = list;
                results = [];
                for (i = 0, len = ref.length; i < len; i++) {
                    item = ref[i];
                    item.selected = selected;
                    if (item.children != null && item.children.length > 0) {
                        $scope.toggleCheckbox(item, selectedList);
                        results.push($scope.$broadcast('changeChildren', item, selectedList));
                    } else {
                        results.push(void 0);
                    }
                }
                return results;
            };
            $scope.initCheckbox = function (item, parentItem, flatList) {
                if (!item.opened) item.opened = true;
                if (item.children.length < 1) item.showArrow = false;
                flatList.push(item);
                return item.selected = item.selected==1 || item.selected || false;
            };
            $scope.toggleCheckbox = function (item, list) {
                if (item.selected === true && list != undefined) {
                    list.push(item);
                }
                if (item.children != null) {
                    $scope.$broadcast('changeChildren', item);
                }
            };
            $scope.$on('changeChildren', function (event, parentItem, selectedList) {
                var child, i, len, ref, results;
                ref = parentItem.children;
                results = [];
                for (i = 0, len = ref.length; i < len; i++) {
                    child = ref[i];
                    child.selected = parentItem.selected;
                    $scope.toggleCheckbox(child, selectedList);
                    if (child.children != null) {
                        results.push($scope.$broadcast('changeChildren', child, selectedList));
                    } else {
                        results.push(void 0);
                    }
                }
                return results;
            });
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