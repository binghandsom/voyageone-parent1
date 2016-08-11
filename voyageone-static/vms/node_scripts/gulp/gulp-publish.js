require('./gulp-publish-beta');

var fs = require('fs'),
    glob = require('glob');

var gulp = require('gulp'),
    debug = require('gulp-debug'),
    ngAnnotate = require('gulp-ng-annotate'),
    minifyCss = require('gulp-minify-css'),
    minifyHtml = require('gulp-minify-html'),
    uglify = require('gulp-uglify'),
    replace = require('gulp-replace'),
    rename = require('gulp-rename'),
    concat = require('gulp-concat'),
    header = require('gulp-header');

var requireMin = require('./gulp-require-min');

var SEARCH_MIN = ['src'];

// release static
gulp.task(tasks.publish.statics, function () {

    gulp.src(publish.static.img.src)
        .pipe(gulp.dest(publish.release.static.img));

    var fonts = glob.sync(publish.static.fonts.src);
    gulp.src(fonts)
        .pipe(gulp.dest(publish.release.static.fonts));

    gulp.src(build.common.appCss.src)
        .pipe(concat(build.common.appCss.concat))
        .pipe(minifyCss())
        .pipe(gulp.dest(publish.release.static.css));

    gulp.src(build.common.loginCss.src)
        .pipe(concat(build.common.loginCss.concat))
        .pipe(minifyCss())
        .pipe(gulp.dest(publish.release.static.css));
});

// release voyageone.angular.com.js
gulp.task(tasks.publish.angular, [tasks.beta.angular], function () {
    // 压缩 js, 并重新追加 min 后缀
    gulp.src(publish.release.components + '/' + build.common.angular.concat)
        .pipe(uglify())
        .pipe(rename({suffix: '.min'}))
        .pipe(gulp.dest(publish.release.components));
});

// release voyageone.com.js.
gulp.task(tasks.publish.com, [tasks.beta.com], function () {
    gulp.src(publish.release.components + '/' + build.common.native.concat)
        .pipe(uglify())
        .pipe(rename({suffix: '.min'}))
        .pipe(gulp.dest(publish.release.components));
});

// release modules.
gulp.task(tasks.publish.modules, function () {

    // build login.app and channel.app
    gulp.src(publish.loginAndChannel.js)
        .pipe(requireMin(SEARCH_MIN))
        .pipe(ngAnnotate())
        .pipe(uglify())
        .pipe(rename({suffix: ".min"}))
        .pipe(gulp.dest(publish.release.loginAndChannel));

    gulp.src(publish.loginAndChannel.html)
        .pipe(replace(/data-main=["'](.+?)["']/g, 'data-main="$1.min"'))
        .pipe(replace('libs/require.js/2.1.21/require.js', 'libs/require.js/2.1.21/require.min.js'))
        .pipe(minifyHtml({empty: true}))
        .pipe(gulp.dest(publish.release.loginAndChannel));

    gulp.src([publish.modules.js, '!src/modules/**/*.doc.js'])
        .pipe(requireMin(SEARCH_MIN))
        .pipe(ngAnnotate())
        .pipe(uglify())
        .pipe(gulp.dest(publish.release.modules));

    // 压缩html文件
    gulp.src(publish.modules.html)
        .pipe(replace('libs/require.js/2.1.21/require.js', 'libs/require.js/2.1.21/require.min.js'))
        .pipe(minifyHtml({empty: true}))
        .pipe(gulp.dest(publish.release.modules));

    // copy json文件
    gulp.src(publish.modules.json)
        .pipe(gulp.dest(publish.release.modules));

});

gulp.task(tasks.publish.libs, [tasks.beta.libs]);

gulp.task(tasks.publish.all, [tasks.publish.statics, tasks.publish.angular, tasks.publish.com, tasks.publish.libs, tasks.publish.modules]);