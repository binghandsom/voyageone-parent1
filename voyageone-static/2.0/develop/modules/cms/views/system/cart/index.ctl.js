/**
 * Created by 123 on 2016/4/13.
 */
define([
    'underscore',
    'modules/cms/controller/popup.ctl'
], function (_) {



    function indexController($scope,cartService,platformService) {

        var initVals = {cart_id: "", name: "", status: "1", cart_type: ''}; //表单初始化值注意顺序


        var CART_TYPE_DICT=[['','ALL'],['1','1'],['2','2'],['3','3']];

        //页面model
        $scope.vm = {data: [], search : _.clone(initVals),CART_TYPE_DICT:CART_TYPE_DICT,PLATFORM_DICT:{}};


        $scope.initialize  = function(){
            platformService.list().then(function (resp) {
                _.forEach(resp.data, function (el) {
                    $scope.vm.PLATFORM_DICT[el.platform_id] = el.platform;
                });
            })

        };

        $scope.clear = function () {
            $scope.vm.search = _.clone(initVals);
        };

        $scope.search= function () {

            cartService.search($scope.vm.search).then(function (resp) {
                $scope.vm.data = resp.data;
            });
        };

        $scope.edit = function (el, popCtrl) {
            popCtrl.openCartEdit({el:el,PLATFORM_DICT:$scope.vm.PLATFORM_DICT}).then(function (editedEl) {
                var index=_.findIndex($scope.vm.data,function(r){return r.cart_id==editedEl.cart_id});
                if(index!=-1 && editedEl){
                    $scope.vm.data[index]=editedEl
                }
                //_.filter($scope.vm.data, function (r) {r.cart_id===editedEl.cart_id})[0]=editedEl;
            });
        };

        $scope.add = function (popCtrl) {
            popCtrl.openCartEdit({PLATFORM_DICT:$scope.vm.PLATFORM_DICT}).then(function (addedEl) {
                if(addedEl) {
                    $scope.vm.data.push(addedEl);
                }
                //_.filter($scope.vm.data, function (r) {r.cart_id===editedEl.cart_id})[0]=editedEl;
            });
        };

        $scope.delete=function(index,cart_id) {
            cartService.delete({cart_id: cart_id}).then(function () {
                $scope.vm.data.splice(index, 1);
            });
        };



    }

    indexController.$inject = ['$scope',"systemCartService",'platformService'];
    return indexController;
});
