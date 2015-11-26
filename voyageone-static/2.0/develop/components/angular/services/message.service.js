/**
 * @Date:    2015-11-19 14:47:23
 * @User:    Jonas
 * @Version: 0.2.0
 */

angular.module('voyageone.angular.services.message', [])
  .factory("$messageHelper", $messageHelper)
  .service('messageService', MessageService);

function $messageHelper($modal, $filter, $templateCache) {

  var templateUrl = "voyageone/cms/angular/service/messageHelper.tpl.html";

  $templateCache.put(templateUrl, '<div class="vo_modal">' +
    '<div class="modal-header">' +
    '<button type="button" class="close" data-dismiss="modal" aria-label="Close" ng-click="close()"><span' +
    'aria-hidden="true"><i ng-click="close()" class="fa fa-close"></i></span></button>' +
    '<h4 class="modal-title" ng-bind-html="title"></h4>' +
    '</div>' +
    '<div class="modal-body wrapper-lg">' +
    '<div class="row">' +
    '<p ng-bind-html="content"></p>' +
    '</div>' +
    '</div>' +
    '<div class="modal-footer">' +
    '<button class="btn btn-default btn-sm" ng-if="!isAlert" ng-click="close()" translate="CORE_BUTTON_CANCEL"></button>' +
    '<button class="btn btn-vo btn-sm" ng-click="ok()" translate="CORE_BUTTON_OK"></button>' +
    '</div>' +
    '</div>');

  function tran(translationId, values) {
    return $filter('translate')(translationId, values);
  }

  return function(options) {
    if (!_.isObject(options)) throw "arg type must be object";
    var values;
    if (_.isObject(options.content)) {
      values = options.content.values;
      options.content = options.content.id;
    }
    options.title = tran(options.title);
    options.content = tran(options.content, values);
    var modalInstance = $modal.open({
      templateUrl: templateUrl,
      controller: ["$scope", function(scope) {
        _.extend(scope, options);
      }],
      size: 'md'
    });
    options.close = function() {
      modalInstance.dismiss('close')
    };
    options.ok = function() {
      modalInstance.close('');
    };
    return modalInstance;
  };
}

function MessageService($messageHelper) {
  this.$messageHelper = $messageHelper;
}

MessageService.prototype = {
  alert: function(content, title) {
    return this.$messageHelper({
      title: title || 'CORE_TXT_ALERT',
      content: content,
      isAlert: true
    });
  },
  confirm: function(content, title) {
    return this.$messageHelper({
      title: title || 'CORE_TXT_CONFIRM',
      content: content,
      isAlert: false
    });
  }
};
