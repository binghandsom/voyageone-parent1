/**
 * @Name:    popup.service.js
 * @Date:    2015/5/14
 *
 * @User:    Edward
 * @Version: 1.0.0
 */

define (function (require) {
    var omsApp = require('modules/core/core.module');

    omsApp.service ('popAnnouncementService', ['$q', 'coreAction', 'ajaxService',
        function ($q, coreAction, ajaxService) {

            /**
             * get the one announcement info by id.
             * @param id
             * @returns {r.promise|promise|qFactory.Deferred.promise|x.ready.promise|fd.g.promise}
             */
            this.doGetAnnouncementContent = function(id) {
                var defer = $q.defer();

                ajaxService.ajaxPostByIdWithoutValidate(id,coreAction.core_menu_home_doGetAnnouncementContent)
                    .then(function(response) {
                        // TODO 处理数据用来画面可显示的数据
                        defer.$$resolve(response.data)
                    });
                return defer.promise;
            };

        }]);
});
