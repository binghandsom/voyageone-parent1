/**
 * Created by linanbin on 15/12/7.
 */

define([
    'modules/cms/controller/popup.ctl'
], function () {

    return function ($scope,systemCategoryService,$routeParams,notify) {
        $scope.vm={"category":{},"isEditFlg":false};
        $scope.initialize  = function () {
            $scope.getCategoryDetail();
        };

        $scope.getCategoryDetail = function(){
            systemCategoryService.getCategoryDetail($routeParams.catId).then(function (res) {
                $scope.vm.category = res.data;
                $scope.vm.isEditFlg = false;
            }, function (err) {

            })
        };
        $scope.delNode = function(parent,node){
            var index;
            index=_.indexOf(parent,node);
            if(index >-1 ){
                parent.splice(index,1);
                $scope.vm.isEditFlg = true;
            }
        };

        $scope.update = function(data){
            systemCategoryService.updateCategorySchema(data).then(function(res){
                $scope.vm.category.modified = res.data;
                $scope.vm.isEditFlg = false;
                notify.success("success");
            },function(err){
            })
        }
    };
});
