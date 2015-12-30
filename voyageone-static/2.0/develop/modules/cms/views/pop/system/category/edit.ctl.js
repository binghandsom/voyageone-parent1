/**
 * Created by linanbin on 15/12/7.
 */
define([
    'angularAMD',
    'modules/cms/controller/popup.ctl'
], function (angularAMD) {

    angularAMD.controller('popCategorySchemaCtl', function ($scope, systemCategoryService, item, catFullName) {

        $scope.vm = {"schema": {}};
        $scope.newOption = {};

        $scope.initialize = function () {
            if (item) {
                $scope.vm.schema = _.clone(item);
                if (item.options) {
                    $scope.vm.schema.options = _.map(item.options, _.clone);
                }
                $scope.vm.catFullName = catFullName;
            }
        }
        // 添加option
        $scope.addOption = function () {
            $scope.vm.schema.options.push($scope.newOption);
            $scope.newOption = {};
        }

        $scope.delOption = function (option) {
            var index;
            index = _.indexOf($scope.vm.schema.options, option);
            if (index > -1) {
                $scope.vm.schema.options.splice(index, 1);
            }
        }
        $scope.ok = function () {
            for (key in $scope.vm.schema) {
                item[key] = $scope.vm.schema[key];
            }
            switch (item.type) {
                case "INPUT":
                case "LABEL":
                case "MULTICOMPLEX":
                case "COMPLEX":
                    if (item.options) {
                        delete item.options;
                    }
                    break;
            }
            $scope.$close();
        }
    });
});