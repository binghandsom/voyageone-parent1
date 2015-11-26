/**
 * @Name:    omsIndexService.js
 * @Date:    2015/3/2
 *
 * @User:    Tom
 * @Version: 1.0.0
 */

define(function(require) {

    //var defaultApp = require('oms/default/defaultModule');
    var defaultApp = require('modules/oms/oms.module');
    //require('components/services/ajaxService');
    //require('common/config');

    defaultApp.service('omsIndexService', ['$q', 'omsAction', 'ajaxService',
        function($q, omsAction, ajaxService) {

            /**
             * check the login user access.
             * @returns {*}
             */
            //this.doInit = function () {
            //    return ajaxService.ajaxPostOnlyByUrl(action.oms_default_index_doInit)
            //};

            this.doInit = function (data, scope) {
                var defer = $q.defer();
                ajaxService.ajaxPost(data, omsAction.oms_default_index_doInit, scope)
                    .then(function(response) {
                        defer.resolve(response);
                    });
                return defer.promise;
            };

        }]);

    return defaultApp;
});