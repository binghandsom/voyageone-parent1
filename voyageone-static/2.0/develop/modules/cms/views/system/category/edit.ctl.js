/**
 * Created by linanbin on 15/12/7.
 */

define([
    'modules/cms/controller/popup.ctl'
], function () {

    return function ($scope,systemCategoryService,$routeParams) {
        $scope.vm={"category":{}};

        $scope.initialize  = function () {
            systemCategoryService.getCategoryDetail($routeParams.catId.replace("2fff","/")).then(function (res) {
                $scope.vm.category = res.data;
            }, function (err) {

            })
        }

        $scope.delNode = function(parent,node){
            var index;
            index=_.indexOf(parent,node);
            if(index >-1 ){
                parent.splice(index,1);
            }
        }

        $scope.update = function(data){
            systemCategoryService.updateCategorySchema(data).then(function(res){
                alert("OK");
            },function(err){
                alert("NG");
            })
        }
    };
});
