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
    // 对系统特殊内容进行处理
    if (sysMessage(res)) {
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

// 和 JAVA 同步,系统通知前端自动跳转的特殊代码
var CODE_SYS_REDIRECT = "SYS_REDIRECT";
// 和 JAVA 同步,回话过期的信息
var MSG_TIMEOUT = "300001";
// 同步,DisplayType 枚举
var DISPLAY_TYPES = {
  /**
   * 弹出提示
   */
  ALERT: 1,
  /**
   * 顶端弹出自动关闭
   */
  NOTIFY: 2,
  /**
   * 右下弹出自动关闭
   */
  POP: 3,
  /**
   * 用户自定义处理
   */
  CUSTOM: 4
};

/**
 * 对系统自动跳转的响应,执行跳转
 * @param {{code:string,redirectTo:string}} res
 * @returns {boolean}
 */
function autoRedirect(res) {
  if (res.code != CODE_SYS_REDIRECT) {
    return false;
  }
  // 如果跳转数据异常,则默认跳转登陆页
  location.href = res.redirectTo || '/login.html';
  return true;
}

/**
 * 对会话超时和未登录进行特殊处理
 * @param {{code:string}} res
 * @returns {boolean}
 */
function sessionTimeout(res) {
  if (res.code != MSG_TIMEOUT) {
    return false;
  }
  // 会话超时,默认跳转到登陆页
  location.href = '/login.html';
  return true;
}

/**
 *
 * @param res
 */
function autoDisplayMessage(res) {
  // todo
  return true;
}

/**
 * 对特定类型(依据 Code 判断)的信息进行处理
 * @param {{message:string,code:string,redirectTo:string,displayType:DISPLAY_TYPES}} res
 * @returns {boolean}
 */
function sysMessage(res) {
  return autoRedirect(res) || sessionTimeout(res) || autoDisplayMessage(res);
}