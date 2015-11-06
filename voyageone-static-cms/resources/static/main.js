/**
 * @Name:    main.js
 * @Date:    2015/6/15
 *
 * @User:    Edward
 * @Version: 1.0.0
 */

require.config({
    paths: {
        'jQuery': ['//cdn.bootcss.com/jquery/2.1.3/jquery.min', 'libs/jquery/jquery/dist/jquery'],
        'angular': ['//cdn.bootcss.com/angular.js/1.3.17/angular.min', 'libs/angular/angular/angular'],
        'angularAMD': 'libs/angular/angular-amd/angular-amd.min',
        'angular-animate': ['//cdnjs.cloudflare.com/ajax/libs/angular.js/1.3.17/angular-animate.min', 'libs/angular/angular-animate/angular-animate'],
        'angular-route': ['//cdnjs.cloudflare.com/ajax/libs/angular.js/1.3.17/angular-route.min', 'libs/angular/angular-route/angular-route.min'],
        'angular-cookies': ['//cdnjs.cloudflare.com/ajax/libs/angular.js/1.3.17/angular-cookies.min', 'libs/angular/angular-cookies/angular-cookies'],
        'angular-sanitize': ['//cdnjs.cloudflare.com/ajax/libs/angular.js/1.3.17/angular-sanitize.min', 'libs/angular/angular-sanitize/angular-sanitize'],
        'ngStorage': 'libs/angular/ngstorage/ngStorage',
        'ui-bootstrap-tpls': ['//cdn.bootcss.com/angular-ui-bootstrap/0.12.1/ui-bootstrap-tpls.min', 'libs/angular/angular-bootstrap/ui-bootstrap-tpls'],
        'angular-block-ui': 'libs/angular/angular-block-ui/angular-block-ui.min',
        'angular-translate': ['//cdn.bootcss.com/angular-translate/2.5.2/angular-translate.min', 'libs/angular/angular-translate/angular-translate.min'],
        'angular-xeditable': ['//cdn.bootcss.com/angular-xeditable/0.1.9/js/xeditable.min', 'libs/angular/angular-xeditable/xeditable.min'],
        'ngDialog': ['//cdn.bootcss.com/ng-dialog/0.5.4/js/ngDialog.min', 'libs/angular/ngDialog/ngDialog.min'],
        'dragAndDrop': 'libs/angular/angular-drag-and-drop/dragAndDrop',
        'underscore': ['//cdn.bootcss.com/underscore.js/1.8.2/underscore-min', 'libs/jquery/underscore/underscore-min'],
        'chosen': ['//cdn.bootcss.com/chosen/1.4.2/chosen.jquery.min', "libs/angular/chosen/chosen.jquery.min"],
        'angular-chosen': "libs/angular/chosen/chosen",
        'angular-ui-select': ['//cdn.bootcss.com/angular-ui-select/0.13.2/select.min', 'libs/angular/angular-ui-select/dist/select.min'],
        'datatables': ['//cdn.bootcss.com/datatables/1.10.9/js/jquery.dataTables.min', 'libs/jquery/datatables/media/js/jquery.dataTables.min'],
        'datatables-extend': ['//cdn.bootcss.com/datatables/1.10.9/js/dataTables.bootstrap.min', 'libs/jquery/plugins/integration/bootstrap/3/dataTables.bootstrap.min'],
        'datatables-fixedcolumns': 'libs/angular/angular-datatables/fixedcolumns/angular-datatables.fixedcolumns.min',
        'angular-datatables': 'libs/angular/angular-datatables/angular-datatables.min',
        'bootstrap': 'components/bootstrap',
        'app': 'components/app',
        'flow': ['//cdn.bootcss.com/ng-flow/2.6.1/ng-flow-standalone.min', 'libs/angular/ng-flow/ng-flow-standalone.min'],
        'css': '//cdn.bootcss.com/require-css/0.1.8/css.min'
    },

    map: {
        '*': {
            'css': 'css'
        }
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
        'flow': {deps: ['angular']},
    },

    deps: ['bootstrap']
});
