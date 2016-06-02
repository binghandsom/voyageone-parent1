/**
 * Created by 123 on 2016/4/13.
 */
define([
    'underscore',
    'modules/cms/controller/popup.ctl'
], function (_) {


    function indexController($scope, cartService, platformService, $translate, confirm) {

        var initVals = {cart_id: "", name: "", active: "1", cart_type: ''}; //表单初始化值注意顺序


        //var CART_TYPE_DICT = [['', 'ALL'], ['1', '1'], ['2', '2'], ['3', '3']];

        var CART_TYPE_DICT = [
            ['', 'ALL'],
            ['1', '中国店铺'],
            ['2', '国外店铺'],
            ['3', 'MiniMall']
        ];

        $scope.page = {
            curr: 1, total: 0, fetch: function () {
                $scope.refreshPage();
            }
        };
        //页面model
        $scope.vm = {data: [], search: _.clone(initVals), CART_TYPE_DICT: CART_TYPE_DICT, PLATFORM_DICT: {}};

        $scope.initialize = function () {
            platformService.list().then(function (resp) {
                _.forEach(resp.data, function (el) {
                    $scope.vm.PLATFORM_DICT[el.platform_id] = el.platform;
                });
                $scope.search();
            });


        };

        $scope.clear = function () {
            $scope.vm.search = _.clone(initVals);
        };

        $scope.search = function () {
            $scope.page.curr = 1;
            $scope.refreshPage();
        }

        $scope.refreshPage = function () {

            cartService.search(_.extend($scope.page,$scope.vm.search)).then(function (resp) {
                $scope.vm.data = resp.data.data;
                $scope.page.total=resp.data.total;
            });
        };

        $scope.edit = function (el, popCtrl) {
            popCtrl.openCartEdit({el: el, PLATFORM_DICT: $scope.vm.PLATFORM_DICT}).then(function (editedEl) {
                if (!editedEl) return;
                var index = _.findIndex($scope.vm.data, function (r) {
                    return r.cart_id == editedEl.cart_id
                });
                if (index != -1 && editedEl) {
                    $scope.vm.data[index] = editedEl
                }
            });
        };

        $scope.add = function (popCtrl) {
            popCtrl.openCartEdit({PLATFORM_DICT: $scope.vm.PLATFORM_DICT}).then(function (addedEl) {
                if (addedEl) {
                    //$scope.vm.data.push(addedEl);
                    //$scope.page.curr = 1;
                    $scope.search();
                }
            });
        };

        $scope.delete = function (index, cart) {
            confirm($translate.instant('TXT_MSG_DO_DELETE') + cart.name).result.then(function () {
                cartService.delete({cart_id: cart.cart_id}).then(function () {
                    //$scope.vm.data.splice(index, 1);
                    //$scope.page.curr = 1;
                    $scope.search();
                });
            })
        };


    }

    indexController.$inject = ['$scope', "systemCartService", 'platformService', '$translate', 'confirm'];
    return indexController;
});
