/**
 * Created by linanbin on 15/12/3.
 */

define([
    'angularAMD',
    'underscore'
], function (angularAMD, _) {
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
            $searchIndexService.search(resetSearchInfo(data)).then(function (res) {
                defer.resolve (res);
            });
            return defer.promise;
        }

        /**
         * 检索group
         * @param data
         * @returns {*}
         */
        function getGroupList(data, pagination) {
            var defer = $q.defer();

            $searchIndexService.getGroupList(resetGroupPagination(data, pagination)).then(function (res) {
                defer.resolve (res);
            });
            return defer.promise;
        }

        /**
         * 检索product
         * @param data
         * @returns {*}
         */
        function getProductList(data, pagination) {
            var defer = $q.defer();
            $searchIndexService.getProductList(resetProductPagination(data, pagination)).then(function (res) {
                defer.resolve (res);
            });
            return defer.promise;
        }

        /**
         * 将searchInfo转换成server端使用的bean接口
         * @param data
         * @returns {*}
         */
        function resetSearchInfo (data) {
            var searchInfo = angular.copy (data);
            searchInfo.productStatus = _.allKeys(searchInfo.productStatus);
            searchInfo.publishStatus = _.allKeys(searchInfo.publishStatus);
            searchInfo.labelType = _.allKeys(searchInfo.labelType);
            if (!_.isUndefined(searchInfo.codeList))
                searchInfo.codeList = searchInfo.codeList.split("\n");
            return searchInfo;
        }

        /**
         * 添加group的当前页码和每页显示size到server端使用的bean接口
         * @param data
         * @param pagination
         * @returns {*}
         */
        function resetGroupPagination (data, pagination) {
            var searchInfo = resetSearchInfo(data);
            searchInfo.groupPageNum = pagination.curr;
            searchInfo.groupPageSize = pagination.size;
            return searchInfo
        }

        /**
         * 添加product的当前页码和每页显示size到server端使用的bean接口
         * @param data
         * @param pagination
         * @returns {*}
         */
        function resetProductPagination (data, pagination) {
            var searchInfo = resetSearchInfo(data);
            searchInfo.productPageNum = pagination.curr;
            searchInfo.productPageSize = pagination.size;
            return searchInfo
        }
    }
});