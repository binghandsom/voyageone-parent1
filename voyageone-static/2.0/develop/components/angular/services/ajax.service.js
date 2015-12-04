/**
 * @Date:    2015-11-16 18:48:29
 * @User:    Jonas
 * @Version: 0.2.0
 */

angular.module('voyageone.angular.services.ajax', [])
  .service('ajaxService', AjaxService);

function AjaxService($http, blockUI) {
  this.$http = $http;
  this.blockUI = blockUI;
}

AjaxService.prototype.post = function(url, data) {
  this.blockUI.start();
  return this.$http.post(url, data).success((function(_this) {
    return function(response) {
      _this.blockUI.stop();
      return response;
    }
  })(this));
};
