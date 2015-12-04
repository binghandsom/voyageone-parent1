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
    // 对后端通知的跳转进行自动处理
    if (autoRedirect(res) || sessionTimeout(res)) {
      return;
    }
    if (res.message || res.code) {
      defer.reject(res);
      return;
    }
    defer.resolve(res.result);
  }, function (response) {
    defer.reject(null, response);
  });
  return defer.promise;
};

// 和 JAVA 同步,系统通知前端自动跳转的特殊代码
var CODE_SYS_REDIRECT = "SYS_REDIRECT";
// 和 JAVA 同步,回话过期的信息
var MSG_TIMEOUT = "300001";

function autoRedirect(res) {
  if (res.code != CODE_SYS_REDIRECT) {
    return false;
  }
  // 如果跳转数据异常,则默认跳转登陆页
  location.href = (!res.data || !res.data.redirectTo)
    ? '/login.html'
    : res.data.redirectTo;
  return true;
}

function sessionTimeout(res) {
  if (res.code != MSG_TIMEOUT) {
    return false;
  }
  // 会话超时,默认跳转到登陆页
  location.href = '/login.html';
  return true;
}