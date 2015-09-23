/**
 * @Name:    ajaxService.js
 * @Date:    2015/2/3
 *
 * @User:    Edward
 * @Version: 1.0.0
 */

define(function (require) {

    var mainApp = require("components/app");
    require("components/services/alert.service");

    // define ajaxService for getting json data from server.
    mainApp.service("ajaxService", ["$q", "$http", "blockUI", "alertService", "messageService", "statusCodes", ajaxService]);

    function ajaxService($q, $http, blockUI, alertService, messageService, statusCodes) {

        var _ = require("underscore");
        var PROJECT_NAME = "/VoyageOne";

        /**
         * get the server"s response by ajax.
         * @param data
         * @param url
         * @constructor
         */
        this.ajaxPost = function (data, url) {
            blockUI.start();

            var defer = $q.defer();

            url = PROJECT_NAME + url;

            // 有可能出现更底层的 error，暂时没有进行处理
            $http.post(url, data)

                // 统一处理服务器的响应内容
                .success(function (response) {

                    blockUI.stop();

                    // 检查响应内容中携带的 message type，根据 type 返回自定义的伪 http code。
                    var code = messageService.checkMessageType(response);

                    switch (code) {
                        // 服务器未返回任何非法内容，返回给用户精简后的数据
                        case statusCodes.success:

                            defer.resolve({
                                data : response.resultInfo,
                                next : response.forward
                            });
                            break;

                        // 服务器返回验证错误
                        case statusCodes.validError:

                            alertService.setValidationErrors(response.formValidateList);
                            defer.reject(response.formValidateList);

                            break;

                        // 服务器返回错误或其他未知内容
                        default:

                            defer.reject(response);

                            break;
                    }
                });

            return defer.promise;
        };

        /**
         * get the server"s response by id.
         * @param id
         * @param url
         * @returns {promise.promise|jQuery.promise|d.promise|promise|r.promise|jQuery.ready.promise|*}
         * @constructor
         */
        this.ajaxPostByIdWithoutValidate = function (id, url) {
            var params = {"id": id};
            return this.ajaxPost(params, url);
        };

        /**
         * get the server"s reponse without data and scope.
         * @param url
         */
        this.ajaxPostOnlyByUrl = function (url) {
            return this.ajaxPost(null, url);
        };

        /**
         * get the server"s response without data by ajax.
         * @param url
         * @constructor
         */
        this.ajaxPostWithValidate = function (url) {
            return this.ajaxPost(null, url);
        };

        /**
         * get the server"s response without scope by ajax.
         * @param data
         * @param url
         * @constructor
         */
        this.ajaxPostWithData = function (data, url) {
            return this.ajaxPost(data, url);
        };
    }
});