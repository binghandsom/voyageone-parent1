/**
 * @Description
 * Bootstrap Main App
 * @Date:    2015-11-19 20:31:14
 * @User:    Jonas
 * @Version: 2.0.0
 */

require.config({
    baseUrl: '../../',
    urlArgs: "version=",
    paths: {
        'voyageone-angular-com': 'components/dist/voyageone.angular.com',
        'voyageone-com': 'components/dist/voyageone.com',
        'angular-animate': 'libs/angular.js/1.5.6/angular-animate',
        'angular-route': 'libs/angular.js/1.5.6/angular-route',
        'angular-sanitize': 'libs/angular.js/1.5.6/angular-sanitize',
        'angular-cookies': 'libs/angular.js/1.5.6/angular-cookies',
        'angular': 'libs/angular.js/1.5.6/angular',
        'angular-translate': 'libs/angular-translate/2.8.1/angular-translate',
        'angular-block-ui': 'libs/angular-block-ui/0.2.1/angular-block-ui',
        'angular-ui-bootstrap': 'libs/angular-ui-bootstrap/0.14.3/ui-bootstrap-tpls-0.14.3',
        'angular-ngStorage': 'libs/angular-ngStorage/ngStorage',
        'angular-file-upload': 'libs/angular-file-upload/2.2.0/angular-file-upload',
        'angular-ui-utils': 'libs/angular-ui-utils/0.1.1/angular-ui-utils.min',
        'angularAMD': 'libs/angularAMD/0.2.1/angularAMD.min',
        'ngload': 'libs/angularAMD/0.2.1/ngload.min',
        'jquery': 'libs/jquery/2.2.4/jquery',
        'underscore': 'libs/underscore.js/1.8.3/underscore',
        'filestyle': 'libs/bootstrap-filestyle/1.2.1/bootstrap-filestyle',
        'notify': 'libs/notify/0.4.0/notify',
        'cms': 'modules/cms-us/app',
        'chosen': 'libs/chosen/1.4.2/chosen.jquery',
        'angular-chosen': 'libs/angular-chosen/1.5.0/angular-chosen',
        'md5': 'libs/angular-md5/0.1.8/angular-md5',
        'angular-drag': 'libs/angular-drag/angular-drag',
        'angular-sortable-view': 'libs/angular-sortable-view/0.0.15/angular-sortable-view'
    },
    waitSeconds: 0,
    shim: {
        'voyageone-com': ['jquery'],
        'voyageone-angular-com': ['angular'],
        'angular-sanitize': ['angular'],
        'angular-route': ['angular'],
        'angular-animate': ['angular'],
        'angular-cookies': ['angular'],
        'angular-translate': ['angular'],
        'angular-block-ui': ['angular'],
        'angular-ui-bootstrap': ['angular'],
        'angular-ngStorage': ['angular'],
        'angular-file-upload': ['angular'],
        'angular': {exports: 'angular', deps: ['jquery']},
        'jquery': {exports: 'jQuery'},
        'filestyle': ['jquery'],
        'json': ['text'],
        'angularAMD': ['angular', 'ngload'],
        'chosen': ['jquery'],
        'angular-chosen': ['angular', 'chosen'],
        'angular-ui-utils': ['angular'],
        'angular-drag': ['angular'],
        'md5': ['angular'],
        'angular-sortable-view': ['angular'],
        'cms': [
            'underscore',
            'voyageone-angular-com',
            'voyageone-com',
            'angular-block-ui',
            'angular-ui-bootstrap',
            'angular-ngStorage',
            'angular-route',
            'angular-sanitize',
            'angular-animate',
            'angular-translate',
            'angular-cookies',
            'angular-file-upload',
            'filestyle',
            'notify',
            'angular-chosen',
            'angular-ui-utils',
            'md5',
            'angular-drag',
            'angular-sortable-view'
        ]
    },
    deps: ['cms']
});