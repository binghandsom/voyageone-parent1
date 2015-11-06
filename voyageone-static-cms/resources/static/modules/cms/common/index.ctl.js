/**
 * @Name:    complex.ctl.js
 * @Date:    2015/8/12
 *
 * @User:    Edward
 * @Version: 1.0.0
 */
define (function (require) {
    var cmsApp = require ('modules/cms/cms.module');
    require ('modules/cms/common/common.service');

    cmsApp.controller ('indexController', ['$scope', '$rootScope', '$q', '$location', '$routeParams', 'cmsCommonService', 'cmsRoute',
      function ($scope, $rootScope, $q, $location, $routeParams, cmsCommonService, cmsRoute ) {

          var _ = require ('underscore');
          var commonUtil = require ('components/util/commonUtil');

          $scope.data=[];
          /**
           * 初始化操作.
           */
          $scope.initialize = function () {
              $scope.doSearch()
          };

          /**
           * 检索
           */
          $scope.doSearch = function () {
              cmsCommonService.doQuickSearch().then (function (data) {
                  $scope.data = data;
              })
          };

          $scope.goSearch = function (mode) {
              switch(mode)
              {
                  case 0:
                      cmsCommonService.setSearchCondition(1);
                      $location.path(cmsRoute.cms_masterCategory_match.hash);
                      break;
                  case 1:
                      cmsCommonService.setSearchCondition(2);
                      $location.path(cmsRoute.cms_masterCategory_match.hash);
                      break;
                  case 2:
                      var condition={};
                      condition.isApprovedDescription = "0";
                      condition.cartId = 23;
                      cmsCommonService.setSearchCondition(condition);
                      $location.path(cmsRoute.cms_search_advance_cn.hash);
                      break;
                  case 3:
                      var condition={};
                      condition.isApprovedDescription = "1";
                      condition.isApproved = "0";
                      condition.cartId = 23;
                      cmsCommonService.setSearchCondition(condition);
                      $location.path(cmsRoute.cms_search_advance_cn.hash);
                      break;
                  case 4:
                      var condition={};
                      condition.isApproved = "1";
                      condition.publishStatus = "0";
                      condition.cartId = 23;
                      cmsCommonService.setSearchCondition(condition);
                      $location.path(cmsRoute.cms_search_advance_cn.hash);
                      break;
                  case 5:
                      var condition={};
                      condition.publishStatus = "3";
                      condition.cartId = 23;
                      cmsCommonService.setSearchCondition(condition);
                      $location.path(cmsRoute.cms_search_advance_cn.hash);
                      break;
                  case 6:
                      var condition={};
                      condition.publishStatus = "2";
                      condition.cartId = 23;
                      cmsCommonService.setSearchCondition(condition);
                      $location.path(cmsRoute.cms_search_advance_cn.hash);
                      break;
              }
          }

      }]);
});