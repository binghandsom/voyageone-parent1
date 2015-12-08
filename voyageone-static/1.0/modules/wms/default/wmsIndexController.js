/**
 * @User: Jonas
 * @Date: 2015/3/22
 * @Version: 1.0.3
 */

define(["modules/wms/wms.module", 'modules/wms/default/wmsIndexService'], function(wms) {
  return wms.controller("wmsIndexController", [
    '$scope', 'wmsIndexService', function($scope, wmsIndexService) {
      $scope.initialize = function() {
        wmsIndexService.doInit().then(function(res) {
          return $scope.obj = res.data;
        });
      };
    }
  ]);
});