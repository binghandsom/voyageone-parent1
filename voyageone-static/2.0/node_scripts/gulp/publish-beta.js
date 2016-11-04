var fs = require('fs');

var gulp = require('gulp'),
    debug = require('gulp-debug'),
    ngAnnotate = require('gulp-ng-annotate'),
    uglify = require('gulp-uglify'),
    replace = require('gulp-replace'),
    rename = require('gulp-rename'),
    concat = require('gulp-concat'),
    header = require('gulp-header'),
    footer = require('gulp-footer'),
    rjs = require('requirejs'),
    replaceRequirePathMain = require('./require-min');

var definePrefix = 'define(function(){\n',
    defineSuffix = '});';
   // rjsBuildConfig = require('../build');

// 拷贝所有的静态资源到发布目录
gulp.task('copy-beta', function () {

    // 对图片, 样式, 媒体文件
    // 全数复制
    gulp.src('./develop/static/{css,img}/**')
        .pipe(gulp.dest('./dest/beta/static'));

    // 对 js 部分, 只复制压缩后的
    // 和其附带的 css 文件
    gulp.src('./develop/libs/**/*.{min.js,css}')
        .pipe(gulp.dest('./dest/beta/libs'));

    // 对应用下的业务逻辑页面和js进行原样拷贝
    gulp.src('./develop/modules/!(core)/**/*.{js,html}')
        .pipe(replace(/\/require.js"/, '/require.min.js"')) // 对页面的引用进行 min 替换
        .pipe(gulp.dest('./dest/beta/modules/'));

    // 对登录页进行复制
    gulp.src('./develop/*.{js,css,html}')
       // .pipe(replaceRequirePathMain(['./src/'])) // 对登陆的配置进行 min 替换
       // .pipe(replace(/\/require.js"/, '/require.min.js"')) // 对页面的引用进行 min 替换
       // .pipe(replace(/shared\/components\.ng/, 'shared/components')) // 因为 components 都合并了, 所以这里要替换掉
        .pipe(gulp.dest('./dest/beta/'));
});

// 对 js 代码进行打包
/*gulp.task('pkg-beta', ['build.vms.module'], function () {

    // 对 shared 代码进行打包
    gulp.src([
        './src/shared/ng/modules.js',
        './src/shared/ng/!*!/!*.js',
        './src/shared/js/!**!/!*.js'
    ])
        .pipe(debug())
        // 追加依赖注入语法
        .pipe(ngAnnotate())
        // 合并到一个文件
        .pipe(concat('components.js'))
        // 包裹整个内容
        .pipe(header(definePrefix))
        .pipe(footer(defineSuffix))
        .pipe(uglify({
            mangle: false,
            compress: false,
            output: {beautify: true}
        }))
        .pipe(gulp.dest('./dest/beta/shared/'));

    // 对 app main.js 进行处理
    gulp.src('./src/app/main.js')
        .pipe(replaceRequirePathMain(['./src/assets/'])) // 对配置进行 min 替换
        .pipe(replace(/'.+?':\s?'.+?components\.ng',/, '')) // 因为上面的 components 都合并了, 所以这里要删除掉
        .pipe(replace(/'vo-libs-angular': \['angular'\],|'vo-libs-angular',/g, '')) // 同上, 需要清理
        .pipe(replace(/'vo-libs':\s?\['jquery'\],/, '\'vo-libs\': [\'jquery\', \'angular\'],')) // 同上, 需要追加 require 配置里的依赖
        .pipe(uglify())
        .pipe(gulp.dest('./dest/beta/app/'));
});*/

gulp.task('beta', ['copy-beta']);