/**
 * Created by sofia on 5/8/2016.
 */
define([
    'cms',
    'underscore',
    'modules/cms/enums/MappingTypes',
    'modules/cms/controller/popup.ctl'
], function (cms, _, MappingTypes) {

    function catUnionController($scope, platformMappingService, systemCategoryService, $translate, cookieService, notify, confirm, alert) {

        $scope.vm = {
            curMCatPath: '',
            curPCatPath: '',
            "category": {},
            "isEditFlg":false
        };

        // 获取初始化数据
        $scope.initialize = function() {
            // 上个画面选择的类目信息保存在cookie中(只保存key，不保存所有值)，返回到上个画面时，根据key信息重新从数据库查询
            var catvmdata = cookieService.get("_catvmdata");
            $scope.vm.curMCatPath = catvmdata.curMCatPath;
            $scope.vm.curPCatPath = catvmdata.curPCatPath;

            // 取得所选主数据类目的属性数据
            systemCategoryService.getCategoryDetail({"id": catvmdata.curMCatId}).then(function (res) {
                if (res.data == null || res.data == undefined) {
                    return;
                }
                $scope.vm.category = res.data;
                $scope.vm.category.fields.push($scope.vm.category.sku);
                $scope.vm.isEditFlg = false;

                // 往fields中添加索引，以确定查找路径
                addNsetIndex($scope.vm.category);
            })
            $scope.vm.isEditFlg = false;

            // 取得所选平台类目的属性数据
            platformMappingService.getPlatformCategorySchema({
                categoryId: catvmdata.curPCatId,
                cartId: parseInt(catvmdata.selCart)
            }).then(function (res) {
                if (res.data == null || res.data == undefined) {
                    return;
                }
                $scope.vm.platform = res.data;

                // 往fields中添加索引，以确定查找路径
                addNsetIndex($scope.vm.platform);
            });
        };

        // 返回到类目选择画面
        $scope.backToSel = function() {
            window.location.href = "#/system/categorysetting/1";
        };

        $scope.delNode = function(parent,node) {
            confirm($translate.instant('TXT_MSG_DELETE_ITEM')).result
                .then(function () {
                    var index;
                    index=_.indexOf(parent,node);
                    if(index >-1 ){
                        parent.splice(index,1);
                        $scope.vm.isEditFlg = true;
                    }
                });
        };

        $scope.update = function(data) {
            var temp = angular.copy(data)
            if(temp.fields[temp.fields.length-1].name == "SKU" || temp.fields[temp.fields.length-1].name == "sku"){
                temp.fields.pop();
            }
            systemCategoryService.updateCategorySchema(temp).then(function(res){
                $scope.vm.category.modified = res.data;
                $scope.vm.isEditFlg = false;
                //$scope.vm.category.fields.push($scope.vm.category.sku);
                notify.success($translate.instant('TXT_MSG_UPDATE_SUCCESS'));
            })
        };

        $scope.isRequiredField = function(field) {
            return field.rules.some(function (rule) {
                return rule.name === "requiredRule" && rule.value === 'true';
            });
        };

        $scope.isOptionsField = function(field) {
            if(field.options && field.options.length > 0) return true;
        };

        $scope.showOptions = function(field) {
            var str = "";
            _.map(field.options, function(item) {
                str += item.displayName + "\t" +item.value + "<br>";
            })
            return str;
        }

        // 选择主数据属性
        $scope.selMasterField = function(fieldObj) {
            $scope.vm.selMField = fieldObj;
            $scope.vm.curMField = fieldObj._connName_path;
        };
        // 选择平台数据属性
        $scope.selPlatformField = function(fieldObj) {
            $scope.vm.selPField = fieldObj;
            $scope.vm.curPField = fieldObj._connName_path;
        };

        // 加入到指定主数据类目的属性
        $scope.addToField = function(isRoot) {
            if (isRoot) {
                $scope.vm.selMField = null;
                $scope.vm.curMField = '';
            } else {
                if ($scope.vm.curMField == undefined || $scope.vm.curMField == '') {
                    alert("请选择主数据类目的属性");
                    return;
                }
            }
            if ($scope.vm.curPField == undefined || $scope.vm.curPField == '') {
                alert("请选择平台类目的属性");
                return;
            }

            var paraField = null;
            if (isRoot) {
                paraField = $scope.vm.category;
            } else {
                paraField = $scope.vm.selMField;
                // 检查目标属性类型
                if (paraField.type != 'MULTICOMPLEX' && paraField.type != 'COMPLEX') {
                    alert("目标平台类目的属性必须是复杂类型的(COMPLEX和MULTICOMPLEX)，请重新选择");
                    return;
                }
            }
            this.openSystemCategory({'field':$scope.vm.selPField,'vm':$scope.vm,'addOrEditFlg':3,'isRoot':isRoot,'targetField':paraField});
        };

        function addNsetIndex(catObj, lastItemIdx, connName) {
            var fields = catObj.fields;
            if (fields == undefined || !_.isArray(fields) || fields.length == 0) {
                return;
            }
            var itemIdx = [];
            if (lastItemIdx != null && lastItemIdx != undefined) {
                itemIdx = itemIdx.concat(lastItemIdx);
            }
            if (connName == undefined) {
                connName = '';
            }
            if (connName != '') {
                connName += ' -> ';
            }

            for (idx in fields) {
                var nowIdx = [];
                nowIdx = nowIdx.concat(itemIdx);
                nowIdx = nowIdx.concat(idx);
                fields[idx]._index_path = nowIdx;
                fields[idx]._connName_path = connName + fields[idx].name;
                addNsetIndex(fields[idx], nowIdx, fields[idx]._connName_path);
            }
        }

    };

    catUnionController.$inject = ['$scope', 'platformMappingService', 'systemCategoryService', '$translate', 'cookieService', 'notify', 'confirm', 'alert' ];
    return catUnionController;
});