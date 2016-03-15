/**
 * Created by linanbin on 15/12/7.
 */

define([
    'modules/cms/controller/popup.ctl'
], function () {

    function editController($scope,systemCategoryService,$routeParams,notify,$translate,confirm) {
        $scope.vm={"category":{},"isEditFlg":false};
        $scope.initialize  = function () {
            $scope.getCategoryDetail();
            $scope.vm.isEditFlg = false;
        };

        $scope.getCategoryDetail = function(){
            systemCategoryService.getCategoryDetail($routeParams.catId).then(function (res) {
                $scope.vm.category = res.data;
                $scope.vm.category.fields.push($scope.vm.category.sku);
                $scope.vm.isEditFlg = false;
            }, function (err) {

            })
        };
        $scope.delNode = function(parent,node){
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

        $scope.update = function(data){
            var temp = angular.copy(data)
            if(temp.fields[temp.fields.length-1].name == "SKU" || temp.fields[temp.fields.length-1].name == "sku"){
                temp.fields.pop();
            }
            systemCategoryService.updateCategorySchema(temp).then(function(res){
                $scope.vm.category.modified = res.data;
                $scope.vm.isEditFlg = false;
                //$scope.vm.category.fields.push($scope.vm.category.sku);
                notify.success($translate.instant('TXT_MSG_UPDATE_SUCCESS'));
            },function(err){
            })
        }
    };

    editController.$inject = ['$scope', 'systemCategoryService', '$routeParams', 'notify', '$translate', 'confirm'];
    return editController;
});
