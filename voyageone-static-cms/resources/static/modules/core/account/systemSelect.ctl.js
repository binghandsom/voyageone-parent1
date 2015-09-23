/**
 * Created by edward-pc1 on 2015/8/10.
 */

define (function (require) {

    var coreApp = require ('modules/core/core.module');
    require ('modules/core/account/account.service');
    require ('modules/app/common/common.service');
    require ('modules/app/services/user.service');

    coreApp.controller ('systemSelectController', ['$scope', '$location', 'systemSelectService', 'commonService', 'userService',
        function ($scope, $location, systemSelectService, commonService, userService) {

            var commonUtil = require ('components/util/commonUtil');

            $scope.currentCompanyName = '';

            $scope.initialize = function () {
                systemSelectService.doGetCompanyList ()
                    .then (function (data) {
                    $scope.companyList = data.newCompanyList;
                    $scope.currentCompanyId = data.selCompanyId;
                });
            };

            /**
             * 重新显示被选中company下面的channel list信息.
             * @param companyId
             */
            $scope.doSelectCompany = function (companyId) {
                $scope.currentCompanyId = companyId;
            };

            /**
             * 提交选中的system结果.
             * @param channelId
             * @param systemName
             */
            $scope.doSelectSystem = function (channelId, systemName) {
                var data = {};
                data.id = $scope.currentCompanyId;
                data.channelId = channelId;
                data.systemName = systemName;
                commonService.doSelectCompany(data)
                    .then(function(response) {
                        userService.setSelCompany($scope.currentCompanyId);
                        userService.setSelChannel(channelId);
                        userService.setSelMenu(systemName);

                        commonService.doGetMasterInfo(channelId, systemName, true);
                        commonUtil.goToSystemPage(response.next);
                    });
            };

            /**
             * do logout,then return to login page.
             */
            $scope.doLogout = function () {
                commonService.doLogout();
            };
        }]);
});
