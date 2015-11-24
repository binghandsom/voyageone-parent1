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

var definePrefix = 'define(function(){\n';
var defineSuffix = '});';
var headerSingle = '(function(){\n';
var footerSingle = '})();';

var encode = 'utf8';

gulp.task('build-angular-com', function () {
  var srcFiles = 'develop/components/angular/*/*.js';
  var destDir = 'develop/components/dist';
  var footerFile = 'develop/components/angular/voyageone.angular.com.suffix';
  var concatFile = 'voyageone.angular.com.min.js';
  return gulp.src(srcFiles)
    .pipe(debug())
    .pipe(sourceMaps.init())
    // 追加依赖注入语法
    .pipe(ngAnnotate())
    // 包裹每个单个组件
    .pipe(header(headerSingle))
    .pipe(footer(footerSingle))
    // 合并到一个文件
    .pipe(concat(concatFile))
    // 追加尾部声明
    .pipe(footer(fs.readFileSync(footerFile, encode)))
    // 包裹整个内容
    .pipe(header(definePrefix))
    .pipe(footer(defineSuffix))
    // 压缩
    .pipe(uglify())
    .pipe(sourceMaps.write('./'))
    .pipe(gulp.dest(destDir));
});

gulp.task('build-com', function () {
  var srcFiles = 'develop/components/js/*.js';
  var destDir = 'develop/components/dist';
  var concatFile = 'voyageone.com.js';
  return gulp.src(srcFiles)
    .pipe(debug())
    .pipe(sourceMaps.init())
    .pipe(header(headerSingle))
    .pipe(footer(footerSingle))
    .pipe(concat(concatFile))
    .pipe(header(definePrefix))
    .pipe(footer(defineSuffix))
    .pipe(sourceMaps.write('./'))
    .pipe(gulp.dest(destDir));
});

gulp.task('build-css-app', function () {
  return gulp.src([
      'develop/static/css/twitter-bootstrap/3.3.5/bootstrap.css',
      'develop/static/css/font-awesome.css',
      'develop/static/css/app.css'
    ])
    .pipe(concat('app.min.css'))
    .pipe(minifyCss())
    .pipe(gulp.dest('develop/static/'));
});

gulp.task('build-login-css', function () {
  return gulp.src([
      'develop/static/css/login.css',
      'develop/static/css/font-awesome.css'
    ])
    .pipe(concat('login.min.css'))
    .pipe(minifyCss())
    .pipe(gulp.dest('develop/static/'));
});

gulp.task('build-login', function () {
  return gulp.src([
      'develop/static/css/login.css',
      'develop/static/css/font-awesome.css'
    ])
    .pipe(concat('login.min.css'))
    .pipe(minifyCss())
    .pipe(gulp.dest('develop/static/'));
});
