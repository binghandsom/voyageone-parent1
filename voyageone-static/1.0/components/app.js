/**
 * @Name:    app.js
 * @Date:    2015/1/30
 *
 * @User:    Edward
 * @Version: 1.0.0
 */
define (function (require) {

    require ("angular-block-ui");
    require ("angular-bootstrap-nav-tree");
    require ("angular-file-upload");
    require ("angular-ui-select");
    require ("angular-xeditable");
    require ("angular-translate");
    require ("ngDialog");
    require ("ngGrid");
    require ("flow");
    require ("angular-datatables");

    var angularAMD = require ('angularAMD');
    var mainApp = angular.module ("mainModule", [
        "ngRoute"
        , "ngAnimate"
        , "ngStorage"
        , "ngCookies"
        , "ngSanitize"
        , "ngResource"
        , "pascalprecht.translate"
        , "angularBootstrapNavTree"
        , "angularFileUpload"
        , "ui.select"
        , "ui.bootstrap"
        , "xeditable"
        , "ngDialog"
        , "ngGrid"
        , "flow"
        , "blockUI"
        , "localytics.directives"
        , "datatables"]);

    // define a blockUI to show executing message.
    mainApp.config (function (blockUIConfigProvider) {
        // Change the default overlay message
        //blockUIConfigProvider.message("<img src='styles/images/loading.gif'>");
        blockUIConfigProvider.autoBlock (false);
    })
        .config (['flowFactoryProvider', function (flowFactoryProvider) {
            flowFactoryProvider.defaults = {
                target: '',
                permanentErrors: [404, 500, 501],
                maxChunkRetries: 1,
                chunkRetryInterval: 5000,
                simultaneousUploads: 4,
                singleFile: true
            };
            flowFactoryProvider.on ('catchAll', function (event) {
                //console.log ('catchAll', arguments);
            });
        }]);

    return mainApp;
});