/**
 * @Date:    2015-11-16 18:48:29
 * @User:    Jonas
 * @Version: 0.2.0
 */

angular.module('voyageone.angular.services.ajax', [])
  .service('ajaxService', AjaxService);

function AjaxService($http, blockUI, $q) {
  this.$http = $http;
  this.blockUI = blockUI;
  this.$q = $q;
}
AjaxService.prototype.post = function (url, data) {
  var defer = this.$q.defer();
  this.$http.post(url, data).then(function (response) {
    var res = response.data;
    if (!res) {
      alert('相应结果不存在?????');
      defer.reject(null);
      return;
    }
    if (res.message || res.code) {
      defer.reject(res);
      return;
    }
    defer.resolve(res);
  }, function (response) {
    defer.reject(null, response);
  });
  return defer.promise;
};