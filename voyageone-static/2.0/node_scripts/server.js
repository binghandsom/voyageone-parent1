/**
 * 静态服务器实时编译es6
 */
const gulp = require('gulp');
const babel = require('gulp-babel');
const browserSync = require('browser-sync');
const vars = require('./vars');
const publish = vars.publish;
const build = vars.build;
const morgan = require('morgan');
const proxy = require('http-proxy-middleware');
const cached = require('gulp-cached');
const remember = require('gulp-remember');
const moment = require('moment');

let proxyUrl = "http://localhost:8080";
let proxyMiddleware = proxy(proxyUrl);
let httpProxy = proxy(['/cms', '/core'], {
    target: proxyUrl,
    changeOrigin: true,
    logLevel: 'debug'
});

//compile es6 to es5
gulp.task('es6',()=>{

    let _currentTime = moment().format('HH:mm:ss');

    console.log(`[${_currentTime}]  start compiling...`);

    gulp.src(publish.loginAndChannel.js)
        .pipe(cached(publish.loginAndChannel.js))
        .pipe(babel())
        .pipe(remember(publish.loginAndChannel.js))
        .pipe(gulp.dest(publish.release.loginAndChannel));

    gulp.src(publish.modules.js)
        .pipe(cached(publish.modules.js))
        .pipe(babel())
        .pipe(remember(publish.modules.js))
        .pipe(gulp.dest(publish.release.modules));

});

// The static server
gulp.task('serve', ['es6'], () => {

    browserSync.init({
        server: {
            baseDir: ["publish/release","develop"],
            index: "login.html"
        },
        middleware: [
            morgan('dev'), httpProxy
            , function (req, res, next) {
                if (req.method !== 'POST')
                    return next();
                return proxyMiddleware(req, res, next);
            },
        ]
    });

    gulp.watch([publish.modules.js,
        build.common.native.src,
        publish.loginAndChannel.js], ['es6']);

});
