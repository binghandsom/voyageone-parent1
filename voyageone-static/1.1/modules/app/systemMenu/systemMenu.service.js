/**
 * Created by edward-pc1 on 2015/8/24.
 */

define (function (require) {

    var mainApp = require ("modules/app/app.module");
    require ('modules/app/services/user.service');
    require ('components/services/ajax.service');

    mainApp.service ('cmsMenuService', ['$q', 'ajaxService', 'userService', 'commonAction',
        function ($q, ajaxService, userService, commonAction) {

            var commonUtil = require ('components/util/commonUtil');
            var _ = require ('underscore');

            /**
             * 取得CMS左边菜单的显示数据.
             * @returns {*}
             */
            this.doGetCMSMasterInfo = function () {
                var defer = $q.defer ();

                var data = {};
                data.channelId = userService.getSelChannel();
                ajaxService.ajaxPostWithData (data, commonAction.cms_common_master_doGetMasterDataList)
                    .then (function (response) {
                    defer.resolve (response.data);
                });

                return defer.promise;
            }
        }]);
});