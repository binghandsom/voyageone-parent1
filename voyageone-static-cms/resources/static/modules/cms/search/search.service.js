/**
 * @Name:    edit.service.js
 * @Date:    2015/6/23
 *
 * @User:    Edward
 * @Version: 1.0.0
 */

define(function (require) {
    var cmsApp = require('modules/cms/cms.module');

    cmsApp.service('searchService', ['$q', 'cmsAction', 'ajaxService', 'userService',
        function ($q, cmsAction, ajaxService, userService) {

            /**
             * 取得当前category的美国信息.
             * @param categoryId
             * @returns {*}
             */
            this.doSearch = function (data, key,type) {
                var defer = $q.defer();
                data.param = {
                		key: key,
                		channelId: userService.getSelChannel(),
                		type: type
                    };
                // TODO 以后根据用户当前的选择
                ajaxService.ajaxPostWithData(data, cmsAction.cms_search_complex_doSearch)
                    .then(function (response) {
                    defer.resolve(response.data);
                });

                return defer.promise;
            };
            this.doAdvanceSearch = function (data,searchOp) {
                var defer = $q.defer();
                //data.typeIdList = values;
                searchOp.channelId = userService.getSelChannel();
                data.param = searchOp;
                // TODO 以后根据用户当前的选择
                ajaxService.ajaxPostWithData(data, cmsAction.cms_search_advance_doSearch)
                    .then(function (response) {
                    defer.resolve(response.data);
                });

                return defer.promise;
            };
            this.doAdvanceExport = function (data) {
                var defer = $q.defer();
                //data.typeIdList = values;
                data.channelId = userService.getSelChannel();
                ajaxService.ajaxPostWithData(data, cmsAction.cms_search_advance_doExport)
                    .then(function (response) {
                    defer.resolve(response.data);
                });

                return defer.promise;
            };


            /**
             * 取得CN Model信息.
             *
             * @param modelId
             * @returns {*}
             */
            this.doGetCNModelInfo = function (modelId) {
                var defer = $q.defer();
                var data = {};
                // data.typeIdList = values;
                data.channelId = userService.getSelChannel();
                data.modelId = modelId;
                // TODO 以后根据用户当前的选择
                ajaxService.ajaxPostWithData(data, cmsAction.cms_edit_model_doGetCNModelInfo)
                    .then(function (response) {
                        defer.resolve(response.data);
                    });

                return defer.promise;
            };
        }
    ]);
});
