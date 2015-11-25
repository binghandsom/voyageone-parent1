var gulp = require('gulp');
var ngAnnotate = require('gulp-ng-annotate');
var concat = require('gulp-concat');
var debug = require('gulp-debug');
var header = require('gulp-header');
var footer = require('gulp-footer');
var minifyCss = require('gulp-minify-css');
var uglify = require('gulp-uglify');
var sourceMaps = require('gulp-sourcemaps');
var rename = require('gulp-rename');
var fs = require('fs');

var definePrefix = 'define(function(){\n';
var defineSuffix = '});';
var headerSingle = '(function(){\n';
var footerSingle = '})();';

var encode = 'utf8';

// 全部任务的地址或字符串预定义
var cms = {
  common: {
    angular: {
      src: 'develop/components/angular/*/*.js',
      dist: 'develop/components/dist',
      footerFile: 'develop/components/angular/voyageone.angular.com.suffix',
      concat: 'voyageone.angular.com.min.js'
    },
    native: {
      srcFiles: 'develop/components/js/*.js',
      destDir: 'develop/components/dist',
      concatFile: 'voyageone.com.js'
    }
  },
  appCss: '',
  channel: {
    css: [
      'develop/static/css/login.css',
      'develop/static/css/font-awesome.css'
    ],
    js: 'develop/channel.js',
    concat: ''
  },
  login: {
    css: {
      src: [
        'develop/static/css/login.css',
        'develop/static/css/font-awesome.css'
      ],
      concat: 'login.min.css',
      dist: 'develop/static/'
    },
    js: {
      src: 'develop/login.js',
      dist: 'develop/'
    }
  }
};

gulp.task('build-angular-com', function () {
  return gulp.src(cms.common.angular.src)
    .pipe(debug())
    .pipe(sourceMaps.init())
    // 追加依赖注入语法
    .pipe(ngAnnotate())
    // 包裹每个单个组件
    .pipe(header(headerSingle))
    .pipe(footer(footerSingle))
    // 合并到一个文件
    .pipe(concat(cms.common.angular.concat))
    // 追加尾部声明
    .pipe(footer(fs.readFileSync(cms.common.angular.footerFile, encode)))
    // 包裹整个内容
    .pipe(header(definePrefix))
    .pipe(footer(defineSuffix))
    // 压缩
    .pipe(uglify())
    .pipe(sourceMaps.write('./'))
    .pipe(gulp.dest(cms.common.angular.dist));
});

gulp.task('build-com', function () {
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
  return gulp.src(cms.login.css.src)
    .pipe(concat(cms.login.css.concat))
    .pipe(minifyCss())
    .pipe(gulp.dest(cms.login.css.dist));
});

gulp.task('build-login-app', function () {
  return gulp.src(cms.login.js.src)
    .pipe(ngAnnotate())
    .pipe(sourceMaps.init())
    .pipe(uglify())
    .pipe(rename({extname: '.min.js'}))
    .pipe(sourceMaps.write('./'))
    .pipe(gulp.dest(cms.login.js.dist));
});

gulp.task('build-login', ['build-login-css', 'build-login-app']);

gulp.task('watch', function () {
  gulp.watch(cms.login.css.src, ['build-login-css']);
  gulp.watch(cms.login.js.src, ['build-login-app']);
});
