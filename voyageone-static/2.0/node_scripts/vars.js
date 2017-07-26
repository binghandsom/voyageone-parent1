const extend = require('extend');

const versions = {
    // 整个工程版本号
    publish: "2.10.0",
    // voyageone-angular-com.js的版本号
    angularCom: "2.0.0",
    // voyageone-com.js的版本号
    com: "2.0.0",
    // 静态资源的版本号
    statics: "2.0.1"
};

const build = {
    version: versions.statics,
    actions: {
        src: 'develop/modules/*/actions.json'
    },
    common: {
        angular: {
            src: 'develop/components/angular/*/*.js',
            dist: 'develop/components/dist',
            footerFile: 'voyageone.angular.suffix',
            concat: 'voyageone.angular.com.js',
            version: versions.angularCom
        },
        native: {
            src: 'develop/components/js/*/*.js',
            dist: 'develop/components/dist',
            concat: 'voyageone.com.js',
            map: 'voyageone.com.js.map',
            version: versions.com
        },
        appCss: {
            src: [
                'develop/static/css/twitter-bootstrap/local/css/bootstrap.css',
                'develop/static/css/animate.css',
                'develop/static/css/font-awesome/css/font-awesome.css',
                'develop/static/css/simple-line-icons/css/simple-line-icons.css',
                'develop/static/css/glyphicons-regular/css/font.css',
                'develop/static/css/app.reset.css',
                'develop/static/css/app.bootstrap.custom.css',
                'develop/static/css/app.old.css',
                'develop/static/css/app.custom.css',
                'develop/static/css/app.components.css',
                'develop/static/css/app.us.css',
                'develop/static/css/app.pages.css'
            ],
            dist: 'develop/static/css/',
            concat: 'app.min.css'
        },
        loginCss: {
            src: [
                'develop/static/css/login.css',
                'develop/static/css/font-awesome/css/font-awesome.css'
            ],
            dist: 'develop/static/css/',
            concat: 'login.min.css'
        }
    }
};

const publish = {
    version: versions.publish,
    static: {
        path: 'publish/static/' + versions.statics,
        fonts: {
            src: 'develop/static/**/fonts/*',
            dist: 'publish/static/' + versions.statics + '/fonts'
        },
        img: {
            src: 'develop/static/img/**',
            dist: 'publish/static/' + versions.statics + '/img'
        },
        css: {
            src: 'develop/static/css/*.min.css',
            dist: 'publish/static/' + versions.statics + '/css'
        }
    },
    components: {
        angular: {
            dist: 'publish/voaygeone-angular-com/' + versions.angularCom
        },
        native: {
            dist: 'publish/voyageone-com/' + versions.com
        }
    },
    libs: {
        src: ['develop/libs/**/*.js', 'develop/libs/**/*.css', 'develop/libs/**/*.png']
    },
    loginAndChannel: {
        js: 'develop/*.js',
        html: 'develop/*.html'
    },
    modules: {
        js: 'develop/modules/**/*.js',
        html: 'develop/modules/**/*.html',
        css: 'develop/modules/**/*.css',
        json: 'develop/modules/**/*.json'
    },
    release: {
        static: {
            path: 'publish/release/static',
            fonts: 'publish/release/static/fonts',
            img: 'publish/release/static/img',
            css: 'publish/release/static/css'
        },
        components: 'publish/release/components/dist',
        libs: 'publish/release/libs',
        modules: 'publish/release/modules',
        loginAndChannel: 'publish/release/'
    },
    replace_value: {
        voyageone_angular_com: 'components/dist/voyageone.angular.com',
        'voyageone_com': 'components/dist/voyageone.com',
        'angular_animate': 'libs/angular.js/1.5.0-RC.0/angular-animate',
        'angular_route': 'libs/angular.js/1.5.0-RC.0/angular-route',
        'angular_sanitize': 'libs/angular.js/1.5.0-RC.0/angular-sanitize',
        'angular_cookies': 'libs/angular.js/1.5.0-RC.0/angular-cookies',
        'angular': 'libs/angular.js/1.5.0-RC.0/angular',
        'angular_translate': 'libs/angular-translate/2.8.1/angular-translate',
        'angular_block_ui': 'libs/angular-block-ui/0.2.1/angular-block-ui',
        'angular_ui_bootstrap': 'libs/angular-ui-bootstrap/0.14.3/ui-bootstrap-tpls-0.14.3',
        'angular_ngStorage': 'libs/angular-ngStorage/ngStorage',
        'angular_file_upload': 'libs/angular-file-upload/2.2.0/angular-file-upload',
        'angularAMD': 'libs/angularAMD/0.2.1/angularAMD',
        'ngload': 'libs/angularAMD/0.2.1/ngload',
        'jquery': 'libs/jquery/2.1.4/jquery.js',
        'underscore': 'libs/underscore.js/1.8.3/underscore',
        'css': 'libs/require-css/0.1.8/css',
        'json': 'libs/requirejs-plugins/1.0.3/json',
        'text': 'libs/require-text/2.0.12/text',
        'filestyle': 'libs/bootstrap-filestyle/1.2.1/bootstrap-filestyle',
        'notify': 'libs/notify/0.4.0/notify',
        'angular_block_ui_css': 'css!libs/angular-block-ui/0.2.1/angular-block-ui.css'
    }
};

const tasks = {
    publish: {
        all: 'publish',
        statics: 'publish-static',
        angular: 'publish-angular-com',
        com: 'publish-com',
        libs: 'publish-libs',
        modules: 'publish-modules',
        del: 'publish-del'
    },
    beta: {
        all: 'beta',
        statics: 'beta-static',
        angular: 'beta-angular-com',
        com: 'beta-com',
        libs: 'beta-libs',
        modules: 'beta-modules',
        del: 'beta-del'
    }
};

module.exports = {
    build: build,
    publish: publish,
    tasks: tasks,
    versions: versions
};