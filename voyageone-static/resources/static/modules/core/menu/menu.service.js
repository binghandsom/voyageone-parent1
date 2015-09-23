/**
 * @Name:    menuService.js
 * @Date:    2015/2/3
 *
 * @User:    Edward
 * @Version: 1.0.0
 */

define(function(require) {
    var coreApp = require('modules/core/core.module');
    //require('components/services/ajaxService');

    coreApp.service('homeService',['$q', 'ajaxService', 'coreAction',
        function($q, ajaxService, coreAction) {

            /**
             * get all announcement.
             * @returns {*}
             */
            this.doGetAnnouncement = function() {
                var defer = $q.defer();
                ajaxService.ajaxPostOnlyByUrl(coreAction.core_menu_home_doGetAnnouncement)
                    .then(function(response) {
                        defer.resolve(response.data);
                    })
                return defer.promise;
            };

    }]);
});