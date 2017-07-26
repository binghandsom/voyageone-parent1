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
            link: function(scope, element, attrs) {

                let $domObj = element;

                $domObj.on('mouseenter',function () {

                    $domObj.animate({right:"-7px"},1000,"linear",function () {

                    });

                }).on('mouseleave',function () {

                    $domObj.animate({right:"-170px"},1000,"linear",function () {
                        console.log('mouseleave');
                    });

                })

            }
        }
    })

});