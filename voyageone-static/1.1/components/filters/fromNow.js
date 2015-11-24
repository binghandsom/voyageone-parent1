'use strict';

/* Filters */
// need load the moment.js to use this filter. 
//angular.module ('mainModule')
//    .filter ('fromNow', function () {
//        return function (date) {
//            return moment (date).fromNow ();
//        }
//    });
define (function (require) {

    var mainApp = require ('components/app');

    mainApp.filter ('fromNow', function () {
        return function (date) {
            return moment (date).fromNow ();
        }
    });
});