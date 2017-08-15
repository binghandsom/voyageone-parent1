/**
 * @User: Jonas
 * @Date: 2015-3-31 14:39:26
 * @Version: 2.0.0
 */
angular.module("voyageone.angular.factories").factory("$dialogs", function ($uibModal, $filter, $templateCache) {

    var templateName = "voyageone.angular.factories.dialogs.tpl.html";

    var template = '<div class="vo_modal vo-dialogs"><div class="modal-header"><button type="button" class="close" data-dismiss="modal" aria-label="Close" ng-click="close()"><span aria-hidden="true"><i ng-click="close()" class="fa fa-close"></i></span></button><h5 class="modal-title"><i class="fa fa-exclamation-triangle"></i>&nbsp;<span ng-bind-html="title"></span></h5></div><div class="modal-body"><div class="text-left text-wrap" ng-bind-html="content"></div></div><div class="modal-footer"><button class="btn btn-default btn-sm" ng-if="!isAlert" ng-click="close()" translate="BTN_CANCEL"></button><button class="btn btn-sm {{::isAlert?\'btn-default\':\'btn-vo\'}}" ng-click="ok()" translate="BTN_OK"></button></div></div>';

    $templateCache.put(templateName, template);

    function tran(translationId, values) {
        return $filter("translate")(translationId, values);
    }

    return function (options) {
        if (!_.isObject(options)) throw "arg type must be object";
        var values;
        if (_.isObject(options.content)) {
            values = options.content.values;
            options.content = options.content.id;
        }
        options.title = tran(options.title);
        options.content = tran(options.content, values);
        var modalInstance = $uibModal.open({
            templateUrl: templateName,
            controller: ["$scope", function (scope) {
                _.extend(scope, options);
            }],
            size: "md",
            windowClass: 'zindex'
        });
        options.close = function () {
            modalInstance.dismiss("close");
        };
        options.ok = function () {
            modalInstance.close("");
        };
        return modalInstance;
    };

}).factory("alert", function ($dialogs) {
    return function (content, title) {
        return $dialogs({
            title: title || "TXT_ALERT",
            content: content,
            isAlert: true
        }).result;
    };
}).factory("confirm", function vConfirm($dialogs) {
    return function (content, title) {
        return $dialogs({
            title: title || "TXT_CONFIRM",
            content: content,
            isAlert: false
        }).result;
    };
});
