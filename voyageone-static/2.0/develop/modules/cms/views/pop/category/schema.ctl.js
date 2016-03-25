/**
 * Created by linanbin on 15/12/7.
 */
define([
    'angularAMD',
    'modules/cms/controller/popup.ctl'
], function (angularAMD) {

    angularAMD.controller('popCategorySchemaCtl', function ($scope, systemCategoryService,context, confirm, $translate) {

        $scope.vm = {"schema": {}};
        $scope.newOption = {};
        $scope.addOrEditFlg = context.addOrEditFlg;
        $scope.fieldName = context.field.name;
        $scope.initialize = function () {
            $scope.vm = {"schema": {}};
            $scope.newOption = {};
            $scope.addOrEditFlg = context.addOrEditFlg;
            $scope.vm.catFullName = context.vm.category.catFullPath;
            $scope.fieldName = context.field.name;
            // 编辑的场合
            if($scope.addOrEditFlg == 1){
                if (context.field) {
                    $scope.vm.schema = _.clone(context.field);
                    if (context.field.options) {
                        $scope.vm.schema.options = _.map(context.field.options, _.clone);
                    }
                }
            }else{
                if(!$scope.vm.schema.rules) $scope.vm.schema.rules=[{"name":"requiredRule","value":"false"}]
            }
        };
        // 添加option
        $scope.addOption = function () {
            if(!$scope.vm.schema.options) $scope.vm.schema.options=[];
            $scope.vm.schema.options.push($scope.newOption);
            $scope.newOption = {};
        };

        $scope.delOption = function (option) {
            confirm($translate.instant('TXT_MSG_DELETE_ITEM')).result
                .then(function () {
                    var index;
                    index = _.indexOf($scope.vm.schema.options, option);
                    if (index > -1) {
                        $scope.vm.schema.options.splice(index, 1);
                    }
                });
        };
        $scope.onChecked = function(data){
            if(data.value == "false"){
                data.value = "true"
            }else{
                data.value = "false"
            }
        };
        $scope.ok = function () {

            if(!$scope.schemaFrom.$valid){
                return;
            }
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
                    context.field[key] = $scope.vm.schema[key];
                }
            }else{
                context.field.fields.push($scope.vm.schema);
            }
            context.vm.isEditFlg = true;
            $scope.$close();
        }
    });
});