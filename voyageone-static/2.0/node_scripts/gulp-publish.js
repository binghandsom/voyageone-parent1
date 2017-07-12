require('./gulp-publish-beta');
//static  server for es6
require('./server');

const fs = require('fs');
const glob = require('glob');
const gulp = require('gulp');
const debug = require('gulp-debug');
const ngAnnotate = require('gulp-ng-annotate');
const minifyCss = require('gulp-minify-css');
const minifyHtml = require('gulp-minify-html');
const uglify = require('gulp-uglify');
const replace = require('gulp-replace');
const rename = require('gulp-rename');
const concat = require('gulp-concat');
const header = require('gulp-header');
const babel = require('gulp-babel');

const vars = require('./vars');
const requireMin = require('./gulp-require-min');
const publish = vars.publish;
const build = vars.build;
const tasks = vars.tasks;

const searchMin = [
    'develop',
    'publish/release/' + vars.versions.publish
];

// release static
gulp.task(tasks.publish.statics, function () {

    gulp.src(publish.static.img.src)
        .pipe(gulp.dest(publish.release.static.img));

    let fonts = glob.sync(publish.static.fonts.src);
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
        .pipe(babel())
        .pipe(uglify())
        .pipe(rename({suffix: '.min'}))
        .pipe(gulp.dest(publish.release.components));
});

// release voyageone.com.js.
gulp.task(tasks.publish.com, [tasks.beta.com], function () {
    gulp.src(publish.release.components + '/' + build.common.native.concat)
        .pipe(babel())
        .pipe(uglify())
        .pipe(rename({suffix: '.min'}))

        .pipe(gulp.dest(publish.release.components));
});

// release modules.
gulp.task(tasks.publish.modules, function () {

    // build login.app and channel.app
    gulp.src(publish.loginAndChannel.js)
        .pipe(ngAnnotate())
        .pipe(babel())
        .pipe(uglify())
        .pipe(rename({suffix: ".min"}))
        .pipe(gulp.dest(publish.release.loginAndChannel));

    gulp.src(publish.loginAndChannel.html)
        .pipe(replace(/require\(\['(\w+?)'\]\)/g, 'require(["$1.min"])'))
        .pipe(replace(/<script src="(libs\/.+?)js"><\/script>/g, '<script src="$1min.js"></script>'))
        .pipe(minifyHtml({empty: true}))
        .pipe(gulp.dest(publish.release.loginAndChannel));

    gulp.src(publish.modules.js)
        .pipe(replace('version=', 'v='+ Date.parse(new Date())))
        .pipe(requireMin(searchMin))
        .pipe(ngAnnotate())
        .pipe(babel())
        .pipe(uglify())
        .pipe(gulp.dest(publish.release.modules));

    // 压缩html文件
    gulp.src(publish.modules.html)
        .pipe(replace('version=', 'v='+ Date.parse(new Date())))
        .pipe(replace('libs/require.js/2.1.21/require.js', 'libs/require.js/2.1.21/require.min.js'))
        .pipe(minifyHtml({empty: true}))
        .pipe(gulp.dest(publish.release.modules));

    // 压缩css文件
    gulp.src(publish.modules.css)
        .pipe(minifyCss())
        .pipe(gulp.dest(publish.release.modules));

    // copy json文件
    gulp.src(publish.modules.json)
        .pipe(gulp.dest(publish.release.modules));

});

gulp.task(tasks.publish.libs, [tasks.beta.libs]);

gulp.task(tasks.publish.all, [tasks.publish.statics, tasks.publish.angular, tasks.publish.com, tasks.publish.libs, tasks.publish.modules]);