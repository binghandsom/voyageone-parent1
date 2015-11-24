/**
 * @User: sky
 * @Date: 2015-04-09 17:16:50
 * @Version: 0.0.1
 */
define(["modules/wms/wms.module",
        "modules/wms/return/returnService"
        ], function (wms) {
    wms
        .controller("rtnSessionDetailCtrl", [
            "$scope",
            "$routeParams",
            "returnService",
            function ($scope, $routeParams, returnService) {
            	$scope.initialize = function(){
            		returnService.doSessionDetailSearch($routeParams.sessionId, $scope).then(function(response){
            			$scope.returnList = response.data.returnList;
            		});
            	}
            }
        ]);
});
