/**
 * @Date:    2015-11-16 18:48:29
 * @User:    Jonas
 * @Version: 0.2.1
 */
angular.module("voyageone.angular.services").service("$ajax", $Ajax).service("ajaxService", AjaxService).config(['$httpProvider', function ($httpProvider) {
    $httpProvider.defaults.headers.common = {
        'X-Requested-With': 'XMLHttpRequest12'
    };
}]);

function $Ajax($http, $q, blockUI, $timeout) {
    this.$http = $http;
    this.$q = $q;
    this.blockUI = blockUI;
    this.$timeout = $timeout;
}

$Ajax.prototype.post = function (url, data, option) {
    var defer = this.$q.defer(),
        blockUI = this.blockUI,
        $timeout = this.$timeout,
        cancelBlock = null;

    option = option || {
            autoBlock: true,
            blockDelay: 1000
        };

    var autoBlock = option.autoBlock,
        blockDelay = option.blockDelay;

    if (autoBlock) {
        cancelBlock = (function (blockPromise) {
            return function () {
                $timeout.cancel(blockPromise);
                blockUI.stop();
            };
        })($timeout(function () {
            blockUI.start();
        }, blockDelay));
    }

    if (data === undefined) {
        data = {};
    }

    this.$http.post(url, data).then(function (response) {
        var res = response.data;

        if (cancelBlock) cancelBlock();

        if (!res) {
            alert("相应结果不存在?????");
            defer.reject(null);
            return;
        }
        if (res.message || res.code) {
            defer.reject(res);
            return;
        }
        defer.resolve(res);
    }, function (response) {
        if (cancelBlock) cancelBlock();

        defer.reject(null, response);
    });

    return defer.promise;
};

function AjaxService($q, $ajax, messageService) {
    this.$q = $q;
    this.$ajax = $ajax;
    this.messageService = messageService;
}

AjaxService.prototype.post = function (url, data, option) {
    var defer = this.$q.defer();

    this.$ajax.post(url, data, option).then(function (res) {
        // 成功
        defer.resolve(res);
        return res;
    }, function (_this) {
        // 失败
        return function (res) {
            _this.messageService.show(res);
            defer.reject(res);
            return res;
        };
    }(this));

    return defer.promise;
};

