/**
 * Created by sofia on 2016/8/29.
 */
define([
    'admin',
    'underscore',
    'modules/admin/enums/MappingTypes'
], function (admin, _, MappingTypes) {
    var list;
    list = [];
    admin.controller('treeTable', ['$scope', '$filter',
        function ($scope, $filter) {
            $scope.list = list;
            $scope.toggleAllCheckboxes = function ($event) {
                var i, item, len, ref, results, selected;
                selected = $event.target.checked;
                ref = $scope.list;
                results = [];
                for (i = 0, len = ref.length; i < len; i++) {
                    if (window.CP.shouldStopExecution(1)) {
                        break;
                    }
                    item = ref[i];
                    item.selected = selected;
                    if (item.children != null) {
                        results.push($scope.$broadcast('changeChildren', item));
                    } else {
                        results.push(void 0);
                    }
                }
                window.CP.exitedLoop(1);
                return results;
            };
            $scope.initCheckbox = function (item, parentItem) {
                if (!item.opened) item.opened = true;
                if (item.children.length < 1) item.showArrow = false;
                return item.selected = parentItem && parentItem.selected || item.selected || false;
            };
            $scope.toggleCheckbox = function (item, parentScope) {
                if (item.children != null) {
                    $scope.$broadcast('changeChildren', item);
                }
                if (parentScope.item != null) {
                    return $scope.$emit('changeParent', parentScope);
                }
            };
            $scope.$on('changeChildren', function (event, parentItem) {
                var child, i, len, ref, results;
                ref = parentItem.children;
                results = [];
                for (i = 0, len = ref.length; i < len; i++) {
                    if (window.CP.shouldStopExecution(2)) {
                        break;
                    }
                    child = ref[i];
                    child.selected = parentItem.selected;
                    if (child.children != null) {
                        results.push($scope.$broadcast('changeChildren', child));
                    } else {
                        results.push(void 0);
                    }
                }
                window.CP.exitedLoop(2);
                return results;
            });
            return $scope.$on('changeParent', function (event, parentScope) {
                var children;
                children = parentScope.item.children;
                parentScope.item.selected = $filter('selected')(children).length === children.length;
                parentScope = parentScope.$parent.$parent;
                if (parentScope.item != null) {
                    return $scope.$broadcast('changeParent', parentScope);
                }
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