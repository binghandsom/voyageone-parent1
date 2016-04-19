/**
 * Created by 123 on 2016/4/13.
 */
define([
    'angularAMD',
    'underscore',
    'modules/cms/controller/popup.ctl'
], function (angularAMD, _) {

    angularAMD.controller('popChannelEditCtl', function ($scope, $routeParams, context, usjoiService, $modalInstance) {


        /**
         * 初始化数据.
         */
        $scope.initialize = function () {
            var self = this;
            self.is_add = !context.hasOwnProperty("order_channel_id"); //如果有context那么是新增页面
            self.vm =  _.extend({is_usjoi: "0", cart_ids: ''},context);
            usjoiService.getCompanys().then(function (resp) {
                self.COMPANYS = resp.data;
            });
        };

        $scope.save = function () {
            var self = this;
            var vm = self.vm;
            if (!vm.order_channel_id || !/[0-9]+/.test(vm.order_channel_id) || !vm.company_id || !vm.name
                || !vm.send_name || !vm.screct_key || !vm.session_key) {
                return; //表单错误
            }
            if (self.is_add) {
                usjoiService.save(self.vm).then(function () {
                    $modalInstance.close(self.vm);
                });
            }else{
                 usjoiService.update(self.vm).then(function () {
                    $modalInstance.close(self.vm);
                });
            }

        };

        $scope.genSecretKey = function () {
            var self=this;
            usjoiService.genKey(_.extend({type: "secretkey", time: new Date().getTime()}, self.vm)).then(function (resp) {
                self.vm.screct_key=resp.data;
            });
        };
        $scope.genSessionKey = function () {
            var self=this;
            usjoiService.genKey(_.extend({type: "sessionkey", time: new Date().getTime()}, self.vm)).then(function (resp) {
                self.vm.session_key = resp.data;
            });
        };
    });
});
