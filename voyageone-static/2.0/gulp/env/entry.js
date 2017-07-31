module.exports = {
    css: {
        app: [
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
        login: [
            'develop/static/css/login.css',
            'develop/static/css/font-awesome/css/font-awesome.css'
        ]
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
    common: {
        src: [
            'develop/components/js/*/*.js',
            'develop/components/angular/angular.modules.js',
            'develop/components/angular/*/*.js'
        ]
    }
};