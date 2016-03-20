var fs = require('fs');
var gulp = require('gulp');
var debug = require('gulp-debug');
var ngAnnotate = require('gulp-ng-annotate');
var minifyCss = require('gulp-minify-css');
var minifyHtml = require('gulp-minify-html');
var uglify = require('gulp-uglify');
var sourceMaps = require('gulp-sourcemaps');
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

// 压缩之前需要把 angular.com 追加 .min
function fixCommonRef() {

    return replace(
        build.common.angular.concat.replace('.js', ''),
        build.common.angular.concat.replace('.js', '.min')
    );
}

// 图片, 字体, 样式, 资源发布
gulp.task(tasks.publish.statics, function () {

    gulp.src(publish.static.img.src)
        .pipe(gulp.dest(publish.static.img.dist))
        .pipe(gulp.dest(publish.release.static.img));

    var fonts = glob.sync(publish.static.fonts.src);
    gulp.src(fonts)
        .pipe(gulp.dest(publish.static.fonts.dist))
        .pipe(gulp.dest(publish.release.static.fonts));

    gulp.src(build.common.appCss.src)
        .pipe(concat(build.common.appCss.concat))
        .pipe(minifyCss())
        .pipe(gulp.dest(publish.static.css.dist))
        .pipe(gulp.dest(publish.release.static.css));

    gulp.src(build.common.loginCss.src)
        .pipe(concat(build.common.loginCss.concat))
        .pipe(minifyCss())
        .pipe(gulp.dest(publish.static.css.dist))
        .pipe(gulp.dest(publish.release.static.css));
});

// 压缩打包 schema directive 需要使用的 HTML 模板
gulp.task('packaging-templates', function () {
    return gulp.src("develop/components/angular/factories/templates/*/*.html")
        .pipe(debug())
        .pipe(minifyHtml({empty: true}))
        .pipe(ngHtml2Js({
            moduleName: "voyageone.angular.templates",
            prefix: "/components/angular/factories/templates/"
        }))
        .pipe(concat("templates.html.js"))
        .pipe(uglify({
            mangle: false,
            compress: false,
            output: {beautify: true, indent_level: 2}
        }))
        .pipe(gulp.dest("publish/temp/factories/"));
});

// 发布未压缩版本的 angular 工具包
gulp.task(tasks.publish.angular, ['packaging-templates', tasks.build.angular_suff], function () {

    var parentDeclare = fs.readFileSync((build.common.angular.dist + '/' + build.common.angular.footerFile), encode);

    gulp.src([
            build.common.angular.src,
            "publish/temp/factories/templates.html.js"
        ])
        .pipe(debug())
        // 追加依赖注入语法
        .pipe(ngAnnotate())
        // 合并到一个文件
        .pipe(concat(build.common.angular.concat))
        // 追加尾部声明
        .pipe(footer(parentDeclare))
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
gulp.task(tasks.publish.com, function () {

    gulp.src(build.common.native.src)
        .pipe(header(headerSingle))
        .pipe(footer(footerSingle))
        .pipe(concat(build.common.native.concat))
        .pipe(header(definePrefix))
        .pipe(footer(defineSuffix))
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
        .pipe(minifyHtml({empty: true}))
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
        .pipe(minifyHtml({empty: true}))
        .pipe(gulp.dest(publish.release.modules));

    // copy json文件
    gulp.src(publish.modules.json)
        .pipe(gulp.dest(publish.release.modules));

});

gulp.task(tasks.publish.all, [tasks.publish.statics, tasks.publish.angular, tasks.publish.com, tasks.publish.libs, tasks.publish.modules]);