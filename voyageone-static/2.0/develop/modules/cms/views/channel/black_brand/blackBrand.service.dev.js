/**
 * Created by 123 on 2016/9/9.
 */

define(['cms',
    './list.json'
], function (cms,listData) {

    return cms.service('blackBrandService', function ($q) {
        this.list = list;
        this.update = update;
        this.batchUpdate = batchUpdate;

        function list() {
            var defer = $q.defer();
            defer.resolve(listData);
            return defer.promise;
        }

        function update(){
            var defer = $q.defer();
            defer.resolve(true);
            return defer.promise;
        }

        function batchUpdate(){
            var defer = $q.defer();
            defer.resolve(true);
            return defer.promise;
        }
    });

});