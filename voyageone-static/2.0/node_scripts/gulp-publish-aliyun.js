var gulp = require('gulp');
var ngAnnotate = require('gulp-ng-annotate');
var header = require('gulp-header');
var minifyHtml = require('gulp-minify-html');
var uglify = require('gulp-uglify');
var sourceMaps = require('gulp-sourcemaps');
var replace = require('gulp-replace');
var rename = require('gulp-rename');
//var clean = require('gulp-clean')

var vars = require('./vars');
var publish = vars.publish;
var build = vars.build;
var tasks = vars.tasks;

// 压缩之前需要把 angular.com 追加 .min
function fixCommonRef() {

  return replace(
      build.common.angular.concat.replace('.js', ''),
      build.common.angular.concat.replace('.js', '.min')
  );
}

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
  gulp.src(build.common.angular.dist + '/' + build.common.angular.concat)
      .pipe(sourceMaps.init())
      .pipe(uglify())
      .pipe(rename({suffix: '.min'}))
      .pipe(sourceMaps.write('./'))
      .pipe(gulp.dest(publish.components.angular.dist))
      .pipe(gulp.dest(publish.release.components));
});

// release voyageone.com.js.
gulp.task(tasks.publish.com, [tasks.build.com], function () {
  gulp.src(build.common.native.dist + '/' + build.common.native.concat)
      .pipe(sourceMaps.init())
      .pipe(uglify())
      .pipe(rename({suffix: '.min'}))
      .pipe(sourceMaps.write('./'))
      .pipe(gulp.dest(publish.components.native.dist))
      .pipe(gulp.dest(publish.release.components));
});

// release libs.
gulp.task(tasks.publish.libs, function () {

  gulp.src(publish.libs.src)
      .pipe(gulp.dest(publish.release.libs));
});

// release modules.
gulp.task(tasks.publish.modules, function () {

  // build login.app and channel.app
  gulp.src(publish.loginAndChannel.js)
      .pipe(fixCommonRef())
      .pipe(ngAnnotate())
      .pipe(uglify())
      .pipe(rename({suffix: ".min"}))
      .pipe(gulp.dest(publish.release.loginAndChannel));

  gulp.src(publish.loginAndChannel.html)
      .pipe(replace(/data-main=["'](.+?)["']/g, 'data-main="$1.min"'))
      .pipe(replace('libs/require.js/2.1.21/require.js', 'libs/require.js/2.1.21/require.min.js'))
      .pipe(minifyHtml({ empty: true }))
      .pipe(gulp.dest(publish.release.loginAndChannel));

  // 压缩js文件
  gulp.src(publish.modules.js)
      .pipe(fixCommonRef())
      //.pipe(sourceMaps.init())
      // 追加依赖注入语法
      //.pipe(ngAnnotate())
      //.pipe(uglify())
      //.pipe(sourceMaps.write('./'))
      .pipe(gulp.dest(publish.release.modules));

  // 压缩html文件
  gulp.src(publish.modules.html)
      .pipe(replace('libs/require.js/2.1.21/require.js', 'libs/require.js/2.1.21/require.min.js'))
      .pipe(minifyHtml({ empty: true }))
      .pipe(gulp.dest(publish.release.modules));

  // copy json文件
  gulp.src(publish.modules.json)
      .pipe(gulp.dest(publish.release.modules));

});

gulp.task(tasks.publish.all, [tasks.publish.statics, tasks.publish.angular, tasks.publish.com, tasks.publish.libs, tasks.publish.modules]);