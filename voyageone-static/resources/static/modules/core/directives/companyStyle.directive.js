/**
 * @Name:    companyStyle.js
 * @Date:    2015/2/16
 *
 * @User:    Edward
 * @Version: 1.0.0
 */

define(function(require) {
    var coreApp = require('modules/core/core.module');

    // define a directive on mainApp for the permission on page.
    coreApp.directive('companyStyle', function(){
        return {
            restrict: 'A',
            link : function(scope, iElement) {
                if (scope.$last) {
                    var parentItem = iElement.parent();
                    var liLen = parentItem.find('li').length;
                    var width = parentItem.find('li').width() * liLen  + liLen * 30;
                    parentItem.css({"width": width, "margin": "20px auto"});
                    if (liLen > 5)
                        parentItem.css({"width": "720px", "margin": "20px auto"});
                }
            }
        }
    });

    return coreApp;
});