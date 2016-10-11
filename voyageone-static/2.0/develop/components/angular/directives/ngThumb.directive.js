/**
 * The ng-thumb directive
 * @author: piao wenjie
 * @version: 2.3.0, 2016-7-1
 */
'use strict';
angular.module('voyageone.angular.directives').directive('ngThumb', ['$window', function($window) {

        var helper = {
            support: !!($window.FileReader && $window.CanvasRenderingContext2D),
            isFile: function(item) {
                return angular.isObject(item) && item instanceof $window.File;
            },
            isImage: function(file) {
                var type =  '|' + file.type.slice(file.type.lastIndexOf('/') + 1) + '|';
                return '|jpg|png|jpeg|bmp|gif|'.indexOf(type) !== -1;
            }
        };

        return {
            restrict: 'A',
            link: function(scope, element, attributes) {
                if (!helper.support) return;

                var params = scope.$eval(attributes.ngThumb);

                if (!helper.isImage(params.file)) return;

                var fileReader = new FileReader();

                fileReader.readAsDataURL(params.file);

                fileReader.onload = function (event) {
                    scope.$apply(function () {
                        attributes.$set('src', event.target.result);
                    });
                };


            }
        };
    }]);

