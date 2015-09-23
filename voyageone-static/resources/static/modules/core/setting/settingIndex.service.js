/**
 * @Name:    omsIndexService.js
 * @Date:    2015/4/20
 *
 * @User:    Eric
 * @Version: 1.0.0
 */

define(function(require) {

	var coreApp = require('modules/core/core.module');
    //require('components/services/ajaxService');

    coreApp.service('coreIndexService', ['$q', 'coreAction', 'ajaxService',
        function($q, omsAction, ajaxService) {

            this.doInit = function (data, scope) {
                var defer = $q.defer();
                ajaxService.ajaxPost(data, coreAction.core_setting_index, scope)
                    .then(function(response) {
                        defer.resolve(response);
                    });
                return defer.promise;
            };

        }]);

    return coreApp;
});