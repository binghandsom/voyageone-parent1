/**
 * @Name:    main.js
 * @Date:    2015/6/15
 *
 * @User:    Edward
 * @Version: 1.0.0
 */

require.config({

    map: {
        '*': {
            'css': 'libs/jquery/require-css/css'
        }
    },

    paths: {
        'jQuery': 'libs/jquery/jquery/dist/jquery',
        'angular': 'libs/angular/angular/angular',
        'angularAMD': 'libs/angular/angular-amd/angular-amd',
        'angular-animate': 'libs/angular/angular-animate/angular-animate',
        'angular-route': 'libs/angular/angular-route/angular-route',
        'angular-cookies': 'libs/angular/angular-cookies/angular-cookies',
        'angular-sanitize': 'libs/angular/angular-sanitize/angular-sanitize',
        'ngStorage': 'libs/angular/ngstorage/ngStorage',
        'ui-bootstrap-tpls': 'libs/angular/angular-bootstrap/ui-bootstrap-tpls',
        'ui-load': 'libs/angular/ui-load/ui-load',
        'ui-jq': 'libs/angular/ui-jq/ui-jq',
        'angular-block-ui': 'libs/angular/angular-block-ui/angular-block-ui.min',
        'angular-translate': 'libs/angular/angular-translate/angular-translate.min',
        'angular-xeditable': 'libs/angular/angular-xeditable/xeditable.min',
        'ngDialog': 'libs/angular/ngDialog/ngDialog',
        'dragAndDrop': 'libs/angular/angular-drag-and-drop/dragAndDrop',
        'underscore': 'libs/jquery/underscore/underscore-min',
        'chosen': "libs/angular/chosen/chosen.jquery",
        'angular-chosen': "libs/angular/chosen/chosen",
        'angular-ui-select': 'libs/angular/angular-ui-select/dist/select.min',
        'datatables': 'libs/jquery/datatables/media/js/jquery.dataTables.min',
        'datatables-extend': 'libs/jquery/plugins/integration/bootstrap/3/dataTables.bootstrap',
        'datatables-fixedcolumns': 'libs/angular/angular-datatables/fixedcolumns/angular-datatables.fixedcolumns',
        'angular-datatables': 'libs/angular/angular-datatables/angular-datatables',
        'bootstrap': 'components/bootstrap',
        'app': 'components/app',
        'flow': 'libs/angular/ng-flow/ng-flow-standalone.min'
    },

    shim: {
        'angular': {exports: 'angular'},
        'angularAMD': {deps: ['angular']},
        'angular-animate': {deps: ['angular']},
        'angular-route': {deps: ['angular']},
        'angular-cookies': {deps: ['angular']},
        'angular-sanitize': {deps: ['angular']},
        'angular-translate': {deps: ['angular']},
        'angular-block-ui': {deps: ['angular', 'css!libs/angular/angular-block-ui/angular-block-ui.min.css']},
        'angular-ui-select': {deps: ['angular', 'jQuery', 'css!libs/angular/angular-ui-select/dist/select.min.css']},
        'angular-xeditable': {deps: ['angular']},
        'ngDialog': {deps: ['angular', 'css!libs/angular/ngDialog/ngDialog.min.css']},
        'dragAndDrop': {deps: ['angular']},
        'underscore': {deps: ['angular']},
        'bootstrap': {deps: ['angular', 'angular-chosen']},
        'chosen':{exports: 'jQuery.fn.chosen', deps:['angular', 'jQuery']},
        'angular-chosen':{deps:['chosen']},
        'datatables':{deps:['jQuery']},
        'datatables-extend': {deps: ['datatables', 'css!libs/jquery/plugins/integration/bootstrap/3/dataTables.bootstrap.css']},
        'angular-datatables': {deps: ['angular', 'datatables', 'datatables-extend', 'datatables-fixedcolumns']},
        'ui-load': {deps: ['angular']},
        'ui-jq': {deps: ['angular', 'ui-load']},
        'flow': {deps: ['angular']},
    },

    deps: ['bootstrap']
});
