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
        this.getGroupList = getGroupList;
        this.getProductList = getProductList;

        /**
         * 初始化数据
         * @returns {*}
         */
        function init() {
            var defer = $q.defer();
            $searchIndexService.init().then(function (res) {
                defer.resolve (res);
            });
            return defer.promise;
        }

        /**
         * 检索group和product
         * @param data
         * @returns {*}
         */
        function search(data) {
            var defer = $q.defer();
            $searchIndexService.search(data).then(function (res) {
                defer.resolve (res);
            });
            return defer.promise;
        }

        /**
         * 检索group
         * @param data
         * @returns {*}
         */
        function getGroupList(data) {
            var defer = $q.defer();
            $searchIndexService.getGroupList(data).then(function (res) {
                defer.resolve (res);
            });
            return defer.promise;
        }

        /**
         * 检索product
         * @param data
         * @returns {*}
         */
        function getProductList(data) {
            var defer = $q.defer();
            $searchIndexService.getGroupList(data).then(function (res) {
                defer.resolve (res);
            });
            return defer.promise;
        }
    }
});