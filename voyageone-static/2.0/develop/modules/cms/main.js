/**
 * @Description
 * Bootstrap Main App
 * @Date:    2015-11-19 20:31:14
 * @User:    Jonas
 * @Version: 2.0.0
 */

require.config({
    baseUrl: '../../',
    paths: {
        'voyageone-angular-com': 'components/dist/voyageone.angular.com',
        'voyageone-com': 'components/dist/voyageone.com.min',
        'angular-animate': 'libs/angular.js/1.5.0/angular-animate.min',
        'angular-route': 'libs/angular.js/1.5.0/angular-route.min',
        'angular-sanitize': 'libs/angular.js/1.5.0/angular-sanitize.min',
        'angular-cookies': 'libs/angular.js/1.5.0/angular-cookies.min',
        'angular': 'libs/angular.js/1.5.0/angular.min',
        'angular-translate': 'libs/angular-translate/2.8.1/angular-translate.min',
        'angular-block-ui': 'libs/angular-block-ui/0.2.1/angular-block-ui.min',
        'angular-ui-bootstrap': 'libs/angular-ui-bootstrap/0.14.3/ui-bootstrap-tpls-0.14.3.min',
        'angular-ngStorage': 'libs/angular-ngStorage/ngStorage.min',
        'angular-file-upload': 'libs/angular-file-upload/2.2.0/angular-file-upload.min',
        'angularAMD': 'libs/angularAMD/0.2.1/angularAMD.min',
        'ngload': 'libs/angularAMD/0.2.1/ngload.min',
        'jquery': 'libs/jquery/2.1.4/jquery.min',
        'underscore': 'libs/underscore.js/1.8.3/underscore.min',
        'css': 'libs/require-css/0.1.8/css.min',
        'json': 'libs/requirejs-plugins/1.0.3/json.min',
        'text': 'libs/require-text/2.0.12/text.min',
        'filestyle': 'libs/bootstrap-filestyle/1.2.1/bootstrap-filestyle.min',
        'notify': 'libs/notify/0.4.0/notify.min',
        'cms': 'modules/cms/app'
    },
    shim: {
        'voyageone-com': ['jquery'],
        'voyageone-angular-com': ['angularAMD'],
        'angular-sanitize': ['angular'],
        'angular-route': ['angular'],
        'angular-animate': ['angular'],
        'angular-cookies': ['angular'],
        'angular-translate': ['angular'],
        'angular-block-ui': ['angular', 'css!libs/angular-block-ui/0.2.1/angular-block-ui.min.css'],
        'angular-ui-bootstrap': ['angular'],
        'angular-ngStorage': ['angular'],
        'angular-file-upload': ['angular'],
        'angular': {exports: 'angular', deps: ['jquery']},
        'jquery': {exports: 'jQuery'},
        'filestyle': ['jquery'],
        'json': ['text'],
        'angularAMD': ['angular', 'ngload']
    },
    deps: ['cms']
});