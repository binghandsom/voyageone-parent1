/**
 * Created by linanbin on 15/12/3.
 */

define([
    'angularAMD'
], function (angularAMD) {
    angularAMD
        .service('searchService', searchService);

    function searchService($q, cActions, ajaxService) {

        this.search = search;

        function search(data) {
            var defer = $q.defer();
            ajaxService.post(cActions.cms.search.advance.doSearch, data)
                .then(function (res) {
                    defer.resolve (res);
                });
            return defer.promise;
        }
    }
});