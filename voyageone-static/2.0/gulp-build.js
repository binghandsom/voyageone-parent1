var gulp = require('gulp');
var ngAnnotate = require('gulp-ng-annotate');
var concat = require('gulp-concat');
var debug = require('gulp-debug');
var header = require('gulp-header');
var footer = require('gulp-footer');
var minifyCss = require('gulp-minify-css');
var uglify = require('gulp-uglify');
var sourceMaps = require('gulp-sourcemaps');
var fs = require('fs');
var suffBuilder = require('./gulp-build-suff');

var build = require('./gulp-vars').build;
var tasks = require('./gulp-vars').tasks;

var definePrefix = 'define(function(){\n';
var defineSuffix = '});';
var headerSingle = '(function(){\n';
var footerSingle = '})();';
var encode = 'utf-8';

gulp.task(tasks.build.angular_suff, function () {
  return gulp.src(build.common.angular.src)
    .pipe(debug())
    .pipe(suffBuilder(build.common.angular.footerFile))
    .pipe(gulp.dest(build.common.angular.dist));
});

// build voyageone.angular.com.js
gulp.task(tasks.build.angular, [tasks.build.angular_suff], function () {
  return gulp.src(build.common.angular.src)
    .pipe(debug())
    .pipe(sourceMaps.init())
    // 追加依赖注入语法
    .pipe(ngAnnotate())
    // 合并到一个文件
    .pipe(concat(build.common.angular.concat))
    // 追加尾部声明
    .pipe(footer(fs.readFileSync(( build.common.angular.dist + '/' + build.common.angular.footerFile), encode)))
    // 包裹整个内容
    .pipe(header(definePrefix))
    .pipe(footer(defineSuffix))
    // 此处不进行压缩,只对合并后的内容进行格式化
    .pipe(uglify({
      mangle: false,
      compress: false,
      output: {beautify: true, indent_level: 2}
    }))
    .pipe(sourceMaps.write('./', {
      sourceRoot: 'components/angular'
    }))
    .pipe(gulp.dest(build.common.angular.dist));
});

// build voyageone.com.js
gulp.task(tasks.build.com, function () {
  return gulp.src(build.common.native.src)
    .pipe(debug())
    .pipe(sourceMaps.init())
    .pipe(header(headerSingle))
    .pipe(footer(footerSingle))
    .pipe(concat(build.common.native.concat))
    .pipe(header(definePrefix))
    .pipe(footer(defineSuffix))
    .pipe(sourceMaps.write('./'))
    .pipe(gulp.dest(build.common.native.dist));
});

// build app.css
gulp.task(tasks.build.css.app, function () {
  return gulp.src(build.common.appCss.src)
    .pipe(debug())
    .pipe(concat(build.common.appCss.concat))
    .pipe(minifyCss())
    .pipe(gulp.dest(build.common.appCss.dist));
});

// build login.css
gulp.task(tasks.build.css.login, function () {
  return gulp.src(build.common.loginCss.src)
    .pipe(debug())
    .pipe(concat(build.common.loginCss.concat))
    .pipe(minifyCss())
    .pipe(gulp.dest(build.common.loginCss.dist));
});

// build all css files
gulp.task(tasks.build.css.all, [tasks.build.css.app, tasks.build.css.login]);
