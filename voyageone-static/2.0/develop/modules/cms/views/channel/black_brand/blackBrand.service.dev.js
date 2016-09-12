/**
 * Created by 123 on 2016/9/9.
 */

define(['cms',
    './list.json'
], function (cms,listData) {

    return cms.service('blackBrandService', function ($q) {
        this.list = list;

        function list() {
            var defer = $q.defer();
            defer.resolve(listData);
            return defer.promise;
        }
    });

});