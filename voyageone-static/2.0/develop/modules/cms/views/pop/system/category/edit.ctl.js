/**
 * Created by linanbin on 15/12/7.
 */
define([
    'angularAMD',
    'modules/cms/controller/popup.ctl'
], function (angularAMD) {

    angularAMD.controller('popCategorySchemaCtl', function ($scope, systemCategoryService, item, catFullName,addOrEditFlg) {

        $scope.vm = {"schema": {}};
        $scope.newOption = {};
        $scope.addOrEditFlg = addOrEditFlg;
        $scope.initialize = function () {
            $scope.vm = {"schema": {}};
            $scope.newOption = {};
            $scope.addOrEditFlg = addOrEditFlg;
            // 编辑的场合
            if(addOrEditFlg == 1){
                if (item) {
                    $scope.vm.schema = _.clone(item);
                    if (item.options) {
                        $scope.vm.schema.options = _.map(item.options, _.clone);
                    }
                    $scope.vm.catFullName = catFullName;
                }
            }else{

            }
        }
        // 添加option
        $scope.addOption = function () {
            if(!$scope.vm.schema.options) $scope.vm.schema.options=[];
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


            if($scope.vm.schema.type != "SINGLECHECK" && $scope.vm.schema.type != "MULTICHECK"){
                if ($scope.vm.schema.options) {
                    delete $scope.vm.schema.options;
                }
            }
            if($scope.vm.schema.type == "MULTICOMPLEX" || $scope.vm.schema.type == "COMPLEX") {
                if (!$scope.vm.schema.fields) {
                    $scope.vm.schema.fields = [];
                }
            }

            if($scope.addOrEditFlg == 1){
                for (key in $scope.vm.schema) {
                    item[key] = $scope.vm.schema[key];
                }
            }else{
                item.fields.push($scope.vm.schema);
            }
            $scope.$close();
        }
    });
});