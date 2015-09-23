/**
 * @Name:    omsIndexService.js
 * @Date:    2015/3/2
 *
 * @User:    Tom
 * @Version: 1.0.0
 */

define(function(require) {

    var defaultApp = require('modules/core/core.module');
    //require('components/services/ajaxService');
    //require('common/config');

    defaultApp.service('coreIndexService', ['$q', 'coreAction', 'ajaxService',
        function($q, coreAction, ajaxService) {

            this.doInit = function (data, scope) {
                var defer = $q.defer();
                ajaxService.ajaxPost(data, coreAction.core_default_index, scope)
                    .then(function(response) {
                        defer.resolve(response);
                    });
                return defer.promise;
            };

        }]);

    return defaultApp;
});