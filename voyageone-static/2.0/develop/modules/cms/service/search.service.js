/**
 * Created by linanbin on 15/12/3.
 */

define([
    'angularAMD'
], function (angularAMD) {
    angularAMD
        .service('searchIndexService', searchIndexService);

    function searchIndexService($q, $searchIndexService, cActions, ajaxService) {

        this.init = init;
        this.search = search;

        function init() {
            var defer = $q.defer();
            $searchIndexService.init().then(function (res) {
                defer.resolve (res);
            });
            return defer.promise;
        }

        function search(data) {
            var defer = $q.defer();
            //ajaxService.post(cActions.cms.search.advance.doSearch, data)
            //    .then(function (res) {
            //        defer.resolve (res);
            //    });
            $searchIndexService.doSearch(data).then(function (res) {
                defer.resolve (res);
            });
            return defer.promise;
        }
    }
});