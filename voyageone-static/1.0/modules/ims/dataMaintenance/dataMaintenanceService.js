/**
 * @Name:    dataMaintenanceService.js
 * @Date:    2015/04/15
 *
 * @User:    Bob.Chen
 * @Version: 1.0.0
 */

define(function(require) {

    var omsApp = require('modules/ims/module');
    require('components/services/ajaxService');

    omsApp.service('modifyMainPicService',['$q', 'imsAction', 'ajaxService',
        function ($q, imsAction, ajaxService) {

            /**
             * search
             * @param data
             * @param scope
             * @returns {*}
             */
            this.doSearch = function (data, scope) {
                var defer = $q.defer();
                ajaxService.ajaxPost(data, imsAction.ims_datamaintenance_mainpic_doSearchMainPic, scope)
                    .then(function(response) {
                        // TODO
                        defer.resolve(response);
                    });
                return defer.promise;
            }

        }]);

    return omsApp;
});