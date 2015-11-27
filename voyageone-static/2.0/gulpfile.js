var gulp = require('gulp');
var ngAnnotate = require('gulp-ng-annotate');
var concat = require('gulp-concat');
var debug = require('gulp-debug');
var header = require('gulp-header');
var footer = require('gulp-footer');
var minifyCss = require('gulp-minify-css');
var minifyHtml = require('gulp-minify-html');
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
var build = {
  common: {
    angular: {
      src: 'develop/components/angular/*/*.js',
      dist: 'develop/components/dist',
      footerFile: 'develop/components/angular/voyageone.angular.com.suffix',
      concat: 'voyageone.angular.com.min.js',
      map: 'voyageone.angular.com.min.js.map'
    },
    native: {
      src: 'develop/components/js/*.js',
      dist: 'develop/components/dist',
      concat: 'voyageone.com.min.js',
      map: 'voyageone.com.min.js.map'
    },
    appCss: {
      src:
          [
            'develop/static/css/twitter-bootstrap/3.3.5/bootstrap.css',
            'develop/static/css/animate.css',
            'develop/static/css/simple-line-icons.css',
            'develop/static/css/font.css',
            'develop/static/css/font-awesome.css',
            'develop/static/css/app.css'
          ],
      dist: 'develop/static/',
      concat: 'app.min.css'
    },
    loginCss: {
      src:
          [
            'develop/static/css/login.css',
            'develop/static/css/font-awesome.css'
          ],
      dist: 'develop/static/',
      concat: 'login.min.css'
    }
  }
};

// build voyageone.angular.com.js
gulp.task('build-angular-com', function () {
  return gulp.src(build.common.angular.src)
      .pipe(debug())
      .pipe(sourceMaps.init())
      // 追加依赖注入语法
      .pipe(ngAnnotate())
      // 包裹每个单个组件
      .pipe(header(headerSingle))
      .pipe(footer(footerSingle))
      // 合并到一个文件
      .pipe(concat(build.common.angular.concat))
      // 追加尾部声明
      .pipe(footer(fs.readFileSync(build.common.angular.footerFile, encode)))
      // 包裹整个内容
      .pipe(header(definePrefix))
      .pipe(footer(defineSuffix))
      // 压缩
      .pipe(uglify())
      .pipe(sourceMaps.write('./'))
      .pipe(gulp.dest(build.common.angular.dist));
});

// build voyageone.com.js
gulp.task('build-com', function () {
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
gulp.task('build-css-app', function () {
  return gulp.src(build.common.appCss.src)
      .pipe(concat(build.common.appCss.concat))
      .pipe(minifyCss())
      .pipe(gulp.dest(build.common.appCss.dist));
});

// build login.css
gulp.task('build-css-login', function () {
  return gulp.src(build.common.loginCss.src)
      .pipe(concat(build.common.loginCss.concat))
      .pipe(minifyCss())
      .pipe(gulp.dest(build.common.loginCss.dist));
});

// build all css files
gulp.task('build-css', ['build-css-app', 'build-css-login']);

// watch the login.css and app.css has been changed.
gulp.task('watch', function () {
  gulp.watch(build.common.appCss.src, ['build-css-login']);
  gulp.watch(build.common.loginCss.src, ['build-css-app']);
});

/** release start **/
// 整个工程版本号
var publishVersion = "2.0.0";
// voyageone-angular-com.js的版本号
var voyageoneAngularComVersion = "2.0.0";
// voyageone-com.js的版本号
var voyageoneComVersion = "2.0.0";
// 静态资源的版本号
var staticVersion = "2.0.1";

// release define.
var publish = {
  static: {
    fonts: {
      src: 'develop/static/fonts/**',
      dist: 'publish/static/' + staticVersion + '/fonts'
    },
    img: {
      src: 'develop/static/img/**',
      dist: 'publish/static/' + staticVersion + '/img'
    },
    css: {
      src: 'develop/static/*.css',
      dist: 'publish/static/' + staticVersion
    }
  },
  components: {
    angular: {
      dist:'publish/voaygeone-angular-com/' + voyageoneAngularComVersion
    },
    native: {
      dist: 'publish/voyageone-com/' + voyageoneComVersion
    }
  },
  libs: {
    src: 'develop/libs/**'
  },
  loginAndChannel: {
    js: 'develop/*.js',
    html: 'develop/*.html'
  },
  views: {
    js: 'develop/views/**/*.js',
    html: 'develop/views/**/*.html',
    json: 'develop/views/**/*.json'
  },
  release: {
    static: {
      fonts: 'publish/release/' + publishVersion + '/static/fonts',
      img: 'publish/release/' + publishVersion + '/static/img',
      css: 'publish/release/' + publishVersion + '/static'
    },
    components: 'publish/release/' + publishVersion + '/components/dist',
    libs: 'publish/release/' + publishVersion + '/libs',
    views: 'publish/release/' + publishVersion + '/views',
    loginAndChannel: 'publish/release/' + publishVersion
  }
};

// release static
gulp.task('publish-static', ['build-css'], function () {

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
gulp.task('publish-angular-com', ['build-angular-com'], function () {
  gulp.src([build.common.angular.dist + '/' + build.common.angular.concat,
        build.common.angular.dist + '/' + build.common.angular.map])
      .pipe(gulp.dest(publish.components.angular.dist))
      .pipe(gulp.dest(publish.release.components));
});

// release voyageone.com.js.
gulp.task('publish-com', ['build-com'], function () {
  gulp.src([build.common.native.dist + '/' + build.common.native.concat,
        build.common.native.dist + '/' + build.common.native.map])
      .pipe(gulp.dest(publish.components.native.dist))
      .pipe(gulp.dest(publish.release.components));
});

// release libs.
gulp.task('publish-libs', function () {
  gulp.src(publish.libs.src)
      .pipe(gulp.dest(publish.release.libs));
});

// release views.
gulp.task('publish-views', function () {

  // build login.app and channel.app
  gulp.src(publish.loginAndChannel.js)
      .pipe(ngAnnotate())
      .pipe(sourceMaps.init())
      .pipe(uglify())
      .pipe(sourceMaps.write('./'))
      .pipe(gulp.dest(publish.release.loginAndChannel));

  gulp.src(publish.loginAndChannel.html)
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

gulp.task('publish', ['publish-static', 'publish-angular-com', 'publish-com', 'publish-libs', 'publish-views']);

/** release end **/