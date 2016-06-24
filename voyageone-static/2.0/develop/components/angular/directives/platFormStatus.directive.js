/**
 * @Description: 解析平台状态
 * @example:
 * @User:    tony-piao
 * @Version: 2.1.0, 2016-6-24
 */
angular.module('voyageone.angular.directives').directive('platformStatus', function () {

        function StatusController($scope, $attrs) {

            var controller = this;
            controller.$attrs = $attrs;
            controller.$scope = $scope;
        }

        StatusController.prototype.init = function(){
             // console.log(this.$attrs.data);
        };

        return {
            restrict: 'E',
            scope: {"data":"=data"},
            controller: StatusController,
            controllerAs: 'statusController',
            link: function ($scope, $element) {
             //   console.log($scope.data);
                $scope.statusController.init();
            }
        }
    });
