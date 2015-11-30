var gulp = require('gulp');
var ngAnnotate = require('gulp-ng-annotate');
var header = require('gulp-header');
var minifyHtml = require('gulp-minify-html');
var uglify = require('gulp-uglify');
var replace = require('gulp-replace');
var rename = require('gulp-rename');

var vars = require('./gulp-vars');
var publish = vars.publish;
var build = vars.build;
var tasks = vars.tasks;

// release static
gulp.task(tasks.publish.statics, [tasks.build.css.all], function () {

  gulp.src(publish.static.fonts.src)
    .pipe(gulp.dest(publish.static.fonts.dist))
    .pipe(gulp.dest(publish.release.static.fonts));
  gulp.src(publish.static.img.src)
    .pipe(gulp.dest(publish.static.img.dist))
    .pipe(gulp.dest(publish.release.static.img));
  gulp.src(publish.static.css.src)
    .pipe(gulp.dest(publish.static.css.dist))
    .pipe(gulp.dest(publish.release.static.css));
});

// release voyageone.angular.com.js
gulp.task(tasks.publish.angular, [tasks.build.angular], function () {
  gulp.src([build.common.angular.dist + '/' + build.common.angular.concat,
      build.common.angular.dist + '/' + build.common.angular.map])
    .pipe(gulp.dest(publish.components.angular.dist))
    .pipe(gulp.dest(publish.release.components));
});

// release voyageone.com.js.
gulp.task(tasks.publish.com, [tasks.build.com], function () {
  gulp.src([build.common.native.dist + '/' + build.common.native.concat,
      build.common.native.dist + '/' + build.common.native.map])
    .pipe(gulp.dest(publish.components.native.dist))
    .pipe(gulp.dest(publish.release.components));
});

// release libs.
gulp.task(tasks.publish.libs, function () {
  gulp.src(publish.libs.src)
    .pipe(gulp.dest(publish.release.libs));
});

// release views.
gulp.task(tasks.publish.views, function () {

  // build login.app and channel.app
  gulp.src(publish.loginAndChannel.js)
    .pipe(ngAnnotate())
    .pipe(uglify())
    .pipe(rename({suffix:".min"}))
    .pipe(gulp.dest(publish.release.loginAndChannel));

  gulp.src(publish.loginAndChannel.html)
    .pipe(replace(/data-main=["'](.+?)["']/g, 'data-main="$1.min"'))
    .pipe(minifyHtml())
    .pipe(gulp.dest(publish.release.loginAndChannel));

  // 压缩js文件
  gulp.src(publish.views.js)
    .pipe(uglify())
    .pipe(gulp.dest(publish.release.views));

  // 压缩html文件
  gulp.src(publish.views.html)
    .pipe(minifyHtml())
    .pipe(gulp.dest(publish.release.views));

  // copy json文件
  gulp.src(publish.views.json)
    .pipe(gulp.dest(publish.release.views));

});

gulp.task(tasks.publish.all, [tasks.publish.statics, tasks.publish.angular, tasks.publish.com, tasks.publish.libs, tasks.publish.views]);