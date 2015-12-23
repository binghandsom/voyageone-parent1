/**
 * @User: Jonas
 * @Date: 2015-3-31 14:39:26
 * @Version: 2.0.0
 */

angular.module('voyageone.angular.factories.dialogs', [])
  .factory("$dialogs", function ($modal, $filter, $templateCache) {

    var templateName = 'voyageone.angular.factories.dialogs.tpl.html';
    $templateCache.put(templateName, '<div class="vo_modal"><div class="modal-header"><button type="button" class="close" data-dismiss="modal" aria-label="Close" ng-click="close()"><span aria-hidden="true"><i ng-click="close()" class="fa fa-close"></i></span></button><h4 class="modal-title" ng-bind-html="title"></h4></div><div class="modal-body wrapper-lg"><div class="row"><p ng-bind-html="content"></p></div></div><div class="modal-footer"><button class="btn btn-default btn-sm" ng-if="!isAlert" ng-click="close()" translate="BTN_COM_CANCEL"></button><button class="btn btn-vo btn-sm" ng-click="ok()" translate="BTN_COM_OK"></button></div></div>');

    function tran(translationId, values) {
      return $filter('translate')(translationId, values);
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

      var modalInstance = $modal.open({
        templateUrl: templateName,
        controller: ["$scope", function (scope) {
          _.extend(scope, options);
        }],
        size: 'md'
      });

      options.close = function () {
        modalInstance.dismiss('close')
      };

      options.ok = function () {
        modalInstance.close('');
      };

      return modalInstance;
    };
  })

  .factory("alert", function ($dialogs) {
    return function (content, title) {
      return $dialogs({
        title: title || 'TXT_COM_ALERT',
        content: content,
        isAlert: true
      });
    };
  })

  .factory("confirm", function vConfirm($dialogs) {
    return function (content, title) {
      return $dialogs({
        title: title || 'TXT_COM_CONFIRM',
        content: content,
        isAlert: false
      });
    };
  });