/**
 * @Name:    app.js
 * @Date:    2015/1/30
 *
 * @User:    Edward
 * @Version: 1.0.0
 */
define (function (require) {

    require ("angular-animate");
    require ("angular-route");
    require ("angular-cookies");
    require ("angular-sanitize");
    require ("angular-block-ui");
    require ("angular-ui-select");
    require ("angular-xeditable");
    require ("angular-translate");
    require ("angular-datatables");
    require ("ui-bootstrap-tpls");
    require ("ngDialog");
    require ("ngStorage");
    require ("flow");
    require ("dragAndDrop");

    var angularAMD = require ('angularAMD');
    var mainApp = angular.module ("mainModule", [
        "ngRoute"
        , "ngAnimate"
        , "ngStorage"
        , "ngCookies"
        , "ngSanitize"
        , "pascalprecht.translate"
        , "ui.select"
        , "ui.bootstrap"
        , "xeditable"
        , "ngDialog"
        , "blockUI"
        , "localytics.directives"
        , "datatables"
        , "flow"
        , "ang-drag-drop"
        //, "datatables.fixedcolumns"
    ]);

    // define a blockUI to show executing message.
    mainApp.config (function (blockUIConfigProvider) {
        // Change the default overlay message
        blockUIConfigProvider.autoBlock (false);
    });

    return mainApp;
});
