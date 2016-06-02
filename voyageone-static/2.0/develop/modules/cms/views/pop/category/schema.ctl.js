/**
 * Created by linanbin on 15/12/7.
 *
 * 目前，参数context中包含5项，示例并解释如下
 * { 'field': $scope.vm.selPField,  // 要编辑的field对象
 *   'vm': $scope.vm,               // $scope中存储的临时数据
 *   'addOrEditFlg': 3,             // 操作标识符  0:添加  1:编辑  2:查看  3:从平台类目属性添加到主数据类目属性
 *   'isRoot': isRoot,              // 是否添加到所选类目的根属性（addOrEditFlg==3时才有效）
 *   'targetField': paraField       // 目标field对象（addOrEditFlg==3时才有效）
 * }
 */
define([
    'angularAMD',
    'modules/cms/controller/popup.ctl'
], function (angularAMD) {

    angularAMD.controller('popCategorySchemaCtl', function ($scope, systemCategoryService, context, confirm, alert, $translate) {

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
            if ($scope.addOrEditFlg == 1) {
                if (context.field) {
                    $scope.vm.schema = _.clone(context.field);
                    if (context.field.options) {
                        $scope.vm.schema.options = _.map(context.field.options, _.clone);
                    }
                }
            } else if ($scope.addOrEditFlg == 0) {
                if (!$scope.vm.schema.rules) $scope.vm.schema.rules = [{"name": "requiredRule", "value": "false"}]
            } else if ($scope.addOrEditFlg == 2) {
                // 查看平台类目属性（不可编辑）
                if (context.field) {
                    $scope.vm.schema = _.clone(context.field);
                    if (context.field.options) {
                        $scope.vm.schema.options = _.map(context.field.options, _.clone);
                    }
                }
            } else if ($scope.addOrEditFlg == 3) {
                // 添加到主数据类目属性时，编辑属性
                if (context.field) {
                    $scope.vm.schema = _.clone(context.field);
                    if (context.field.options) {
                        $scope.vm.schema.options = _.map(context.field.options, _.clone);
                    }
                }
            }
        };
        // 添加option
        $scope.addOption = function () {
            if(!$scope.vm.schema.options) $scope.vm.schema.options=[];
            $scope.vm.schema.options.push({displayName: "", value: $scope.vm.schema.options.length});
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
            if (!$scope.schemaFrom.$valid) {
                return;
            }
            if ($scope.vm.schema.type != "SINGLECHECK" && $scope.vm.schema.type != "MULTICHECK") {
                if ($scope.vm.schema.options) {
                    delete $scope.vm.schema.options;
                }
            }
            if ($scope.vm.schema.type == "MULTICOMPLEX" || $scope.vm.schema.type == "COMPLEX") {
                if (!$scope.vm.schema.fields) {
                    $scope.vm.schema.fields = [];
                }
            }

            if ($scope.addOrEditFlg == 1) {
                for (key in $scope.vm.schema) {
                    context.field[key] = $scope.vm.schema[key];
                }
            } else if ($scope.addOrEditFlg == 0) {
                if(context.field.fields[context.field.fields.length-1].name == "SKU" || context.field.fields[context.field.fields.length-1].name == "sku"){
                    context.field.fields.splice(context.field.fields.length-2,0,$scope.vm.schema);
                }else{
                    context.field.fields.push($scope.vm.schema);
                }
            } else if ($scope.addOrEditFlg == 3) {
                // 检查重复属性(id重复，$$hashKey重复)
                var attrId = $scope.vm.schema.id;
                var _hashKey = $scope.vm.schema.$$hashKey;
                if (context.targetField.fields == null || context.targetField.fields == undefined) {
                    context.targetField.fields = [];
                }
                var fieldList = context.targetField.fields;
                for (idx in fieldList) {
                    if (attrId == fieldList[idx].id) {
                        alert("与现有属性'" + fieldList[idx].name + "'的id重复，请修改后再保存");
                        return false;
                    }
                    if (_hashKey == fieldList[idx].$$hashKey) {
                        alert("已添加过属性'" + $scope.vm.schema.name + "'，不能重复添加");
                        return false;
                    }
                }

                if (context.isRoot) {
                    // fields的最后一个元素可能是sku,需要过滤
                    var leng = fieldList.length;
                    if (leng > 0 && (fieldList[leng - 1].name == "SKU" || fieldList[leng - 1].name == "sku")) {
                        fieldList.push(fieldList[leng - 1]);
                        fieldList[leng - 1] = $scope.vm.schema;
                    } else {
                        fieldList.push($scope.vm.schema);
                    }
                } else {
                    fieldList.push($scope.vm.schema);
                }
            }
            context.vm.isEditFlg = true;
            $scope.$close();
        }
    });
});