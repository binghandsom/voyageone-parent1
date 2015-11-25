/**
 * @Name:    show-big-image.directive.js
 * @Date:    2015/5/8
 *
 * @User:    Edward
 * @Version: 1.0.0
 */

define (function (require) {

    var mainApp = require ('components/app');

    var _ = require ('underscore');

    mainApp.directive ('showImage', ['ngDialog', 'appPopupPages',
        function (ngDialog, appPopupPages) {

            return {
                restrict: 'A',
                scope: {
                    showImage: '='
                },
                link: function (scope, element) {

                    $ (element).addClass ("carousel");
                    var showImgSrc = "";

                    // 点击图片，弹出放大图片效果
                    element[0].onclick = function () {
                        if (!_.isEmpty (showImgSrc)) {

                            scope.imgUrl = showImgSrc;

                            ngDialog.open ({
                                template: appPopupPages.popBigImage.page,
                                //controller: appPopupPages.popBigImage.controller,
                                scope: scope
                            });
                        }
                    };

                    // 当图片初始化，或者发生变化时调用.
                    scope.$watch ("showImage", function () {
                        changeImg ();
                    });

                    function changeImg () {
                    	if (_.isEmpty(scope.showImage)) {
                    		showImgSrc = "styles/images/no_image.gif";
                    	} else {
                            if (scope.showImage.startWith ("/")) {
                                    showImgSrc = scope.showImage.substr (1, scope.showImage.length);
                                } else {
                                    showImgSrc = scope.showImage
                                }
                    	}
                        $ (element).attr ("src", showImgSrc);
                    }
                }
            };
        }])
});

