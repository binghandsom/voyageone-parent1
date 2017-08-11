/**
 * @description 固定在右侧的功能栏
 *              template是navBar.directive.html手动压缩后黏贴进来的
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
            template:'<nav class="nav-bar nav-container"><div class="nav-item cur-p"style="background: #ededed;"ng-click="blink()"><div class="nav-slide"><span><i class="glyphicon glyphicon-chevron-{{direction}}"></i></span></div></div><div class="nav-item"ng-transclude></div></nav>',
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