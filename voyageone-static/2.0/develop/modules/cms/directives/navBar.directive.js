/**
 * @description 固定在右侧的功能栏
 * @author piao
 */
define([
    'cms'
],function (cms) {

    cms.directive('navBar',function () {
        return {
            restrict: 'E',
            transclude: true,
            replace: true,
            templateUrl:"directives/navBar.directive.html",
            scope: {
                heading: '@',
                isOpen: '=?',
                isDisabled: '=?'
            },
            link: function(scope, element) {

                let $domObj = element;

                scope.direction = 'right';

                scope.blink = function(){

                    if(scope.direction === 'right'){
                        scope.direction = 'left';
                        $domObj.animate({right:"-150px"},800,"linear");
                    }else{
                        scope.direction = 'right';
                        $domObj.animate({right:"0px"},800,"linear");
                    }

                };


            }
        }
    })

});