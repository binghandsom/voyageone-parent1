/**
 * @Name:    commonService.js
 * @Date:    2015/3/20
 *
 * @User:    Edward
 * @Version: 1.0.0
 */

define(function(require) {

    var omsApp = require('modules/oms/oms.module');
    //require('components/services/ajaxService');

    omsApp.service('omsCommonService', ['$q', 'omsAction', 'omsType', 'ajaxService',
        function($q, omsAction, omsType, ajaxService) {

            var _ = require('underscore');

            /**
             * get the property data by typeIdList.
             * @param values
             * @returns {r.promise|promise|qFactory.Deferred.promise|x.ready.promise|fd.g.promise}
             */
            this.doGetCodeList = function(values) {
                var defer = $q.defer();
                var data = {};
                data.typeIdList = values;
                ajaxService.ajaxPostWithData(data, omsAction.oms_common_service_doGetCode)
                    .then(function(response) {
                        defer.resolve(response.data);
                    });

                return defer.promise;
            };

            /**
             * get the property data by typeId.
             * @param object
             * @param typeId
             */
            this.doGetOneCodeFromList = function(codeList, typeId) {

                var type = 'type' + typeId;

                if(_.has(codeList, type)) {
                    return _.values(_.pick(codeList, type))[0];
                }
            }

        }]);

    return omsApp;
});