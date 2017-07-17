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
            template:"<nav class=\"nav-bar\">\n" +
                    " <div ng-transclude></div>\n" +
                    "</nav>\n" +
                    "",
            scope: {
                heading: '@',
                isOpen: '=?',
                isDisabled: '=?'
            },
            link: function(scope, element, attrs) {

                let $domObj = element;

                $domObj.on('mouseenter',function (event) {

                    $domObj.animate({right:"10px"},1000,"linear",function () {
                        console.log('mouseenter');
                    });

                }).on('mouseleave',function (event) {

                    $domObj.animate({right:"-170px"},1000,"linear",function () {
                        console.log('mouseleave');
                    });

                })

            }
        }
    })

});