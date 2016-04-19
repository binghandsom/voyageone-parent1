/**
 * Created by 123 on 2016/4/11.
 */
define([
    'angularAMD',
    'underscore',
    'modules/cms/controller/popup.ctl'
], function (angularAMD, _) {

    angularAMD.controller('popChannelSettingCtl', ['$scope', '$routeParams', 'context', 'usjoiService','$modalInstance',
        function ($scope, $routeParams, context, usjoiService,$modalInstance) {

            /**
             * 初始化数据.
             */
            $scope.initialize = function () {

                $scope.vm=_.extend({}, context);
                var selected = ($scope.vm.cart_ids||"").split(","); //页面选中的cart_id arr

                usjoiService.getCarts().then(function (resp) {
                    $scope.vm.carts = _.chain(resp.data)
                        .filter(function (el) {

                            //如果是usjoi 那么返回所有的cart,不然只返回非usjoi的cart
                            return $scope.vm.is_usjoi ? true : (el.cart_type == 1 || el.cart_type == 2);
                        }).map(function (el) {
                            el.selected = selected.indexOf(el.cart_id) != -1;
                            return el;
                        }).value();
                });
            };

            $scope.save=function() {
                var cart_ids= _.chain($scope.vm.carts)
                    .filter(function (el) { return el.selected; })
                    .map(function(el){return el.cart_id}).value().join(",");

                var param = {order_channel_id: $scope.vm.order_channel_id, cart_ids: cart_ids};

                usjoiService.updateCartIds(param).then(function () {
                    $modalInstance.close(param);
                });
            };

        }]);
});
