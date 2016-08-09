var fs = require('fs');
var gulp = require('gulp');
var debug = require('gulp-debug');
var ngAnnotate = require('gulp-ng-annotate');
var minifyHtml = require('gulp-minify-html');
var uglify = require('gulp-uglify');
var replace = require('gulp-replace');
var rename = require('gulp-rename');
var concat = require('gulp-concat');
var ngHtml2Js = require('gulp-ng-html2js');
var header = require('gulp-header');
var footer = require('gulp-footer');
var glob = require('glob');

var vars = require('./vars');
var publish = vars.publish;
var build = vars.build;
var tasks = vars.tasks;
var definePrefix = 'define(function(){\n';
var defineSuffix = '});';
var headerSingle = '(function(){\n';
var footerSingle = '})();';
var encode = 'utf-8';

// 图片, 字体, 样式, 资源发布
gulp.task(tasks.beta.statics, function () {

    gulp.src(publish.static.img.src)
        .pipe(gulp.dest(publish.static.img.dist))
        .pipe(gulp.dest(publish.release.static.img));

    var fonts = glob.sync(publish.static.fonts.src);
    gulp.src(fonts)
        .pipe(gulp.dest(publish.static.fonts.dist))
        .pipe(gulp.dest(publish.release.static.fonts));

    gulp.src(build.common.appCss.src)
        .pipe(concat(build.common.appCss.concat))
        .pipe(gulp.dest(publish.static.css.dist))
        .pipe(gulp.dest(publish.release.static.css));

    gulp.src(build.common.loginCss.src)
        .pipe(concat(build.common.loginCss.concat))
        .pipe(gulp.dest(publish.static.css.dist))
        .pipe(gulp.dest(publish.release.static.css));
});

// 发布未压缩版本的 angular 工具包
gulp.task(tasks.beta.angular, function () {

    gulp.src([
            "develop/components/angular/angular.modules.js",
            build.common.angular.src
        ])
        .pipe(debug())
        // 追加依赖注入语法
        .pipe(ngAnnotate())
        // 合并到一个文件
        .pipe(concat(build.common.angular.concat))
        // 包裹整个内容
        .pipe(header(definePrefix))
        .pipe(footer(defineSuffix))
        .pipe(uglify({
            mangle: false,
            compress: false,
            output: {beautify: true}
        }))
        .pipe(gulp.dest(publish.components.angular.dist))
        .pipe(gulp.dest(publish.release.components));
});

// publish-com
gulp.task(tasks.beta.com, function () {

    gulp.src(build.common.native.src)
        .pipe(header(headerSingle))
        .pipe(footer(footerSingle))
        .pipe(concat(build.common.native.concat))
        .pipe(header(definePrefix))
        .pipe(footer(defineSuffix))
        .pipe(uglify({
            mangle: false,
            compress: false,
            output: {beautify: true}
        }))
        .pipe(gulp.dest(publish.components.native.dist))
        .pipe(gulp.dest(publish.release.components));
});

// release libs.
gulp.task(tasks.beta.libs, function () {

    gulp.src(publish.libs.src)
        .pipe(gulp.dest(publish.release.libs));
});

// release modules.
gulp.task(tasks.beta.modules, function () {

    // build login.app and channel.app
    gulp.src(publish.loginAndChannel.js)
        .pipe(ngAnnotate())
        .pipe(gulp.dest(publish.release.loginAndChannel));

    gulp.src(publish.loginAndChannel.html)
        .pipe(gulp.dest(publish.release.loginAndChannel));

    // 压缩js文件
    gulp.src(publish.modules.js)
        .pipe(gulp.dest(publish.release.modules));

    // 压缩html文件
    gulp.src(publish.modules.html)
        .pipe(gulp.dest(publish.release.modules));

    // copy json文件
    gulp.src(publish.modules.json)
        .pipe(gulp.dest(publish.release.modules));

});

gulp.task(tasks.beta.all, [
    tasks.beta.statics, 
    tasks.beta.angular, 
    tasks.beta.com, 
    tasks.beta.libs, 
    tasks.beta.modules
]);