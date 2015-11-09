/**
 * @Name:    defaultController.js
 * @Date:    2015/2/3
 *
 * @User:    Edward
 * @Version: 1.1
 */

define([
  'modules/app/app.module',
  'components/util/commonUtil',
  'modules/app/common/common.service',
  'modules/app/services/user.service'
], function(app, common) {
  app.controller('indexController', [
    '$scope', 'commonService', 'userService', '$location',
    indexController
  ]);

  function indexController($scope, commonService, userService, $location) {
    // 如果没登陆
    // 跳至登陆页
    if (!userService.isLogin()) {
      common.goToLoginPage();
      return;
    }
    $location.path('/cms/common/index');
  }
});
