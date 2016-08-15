require('./publish-beta');

var fs = require('fs'),
    _ngAnnotate = require("ng-annotate");

var gulp = require('gulp'),
    debug = require('gulp-debug'),
    ngAnnotate = require('gulp-ng-annotate'),
    minifyCss = require('gulp-minify-css'),
    uglify = require('gulp-uglify'),
    replace = require('gulp-replace'),
    rename = require('gulp-rename'),
    concat = require('gulp-concat'),
    header = require('gulp-header'),
    footer = require('gulp-footer'),
    rjs = require('requirejs'),
    replaceRequirePathMain = require('./require-min');

var definePrefix = 'define(function(){\n',
    defineSuffix = '});',
    rjsBuildConfig = require('../build.release');

// 拷贝所有的静态资源到发布目录
gulp.task('copy-release', function () {

    // 对图片, 样式, 媒体文件
    // 全数复制
    gulp.src('./src/assets/{css,img,media}/**')
        .pipe(gulp.dest('./dest/release/assets'));

    // 对 js 部分, 只复制压缩后的
    // 和其附带的 css 文件
    gulp.src('./src/assets/js/**/*.{min.js,css}')
        .pipe(gulp.dest('./dest/release/assets/js/'));

    // 对应用下的文件进行原样拷贝
    gulp.src([
        //'./src/*.html',
        './src/app/!(css|translate)/**/*.html',
        './src/app/*.html'
    ])
        .pipe(replace(/\/require.js"/, '/require.min.js"')) // 对页面的引用进行 min 替换
        .pipe(gulp.dest('./dest/release/app/'));

    // 对登录页进行复制
    gulp.src('./src/*.html')
        .pipe(replace(/\/require.js"/, '/require.min.js"')) // 对页面的引用进行 min 替换
        .pipe(gulp.dest('./dest/release/'));
});

// 对 js, css 代码进行打包
gulp.task('pkg-release', ['release.vms.module'], function () {

    // 对 shared 代码进行打包
    gulp.src([
        './src/shared/ng/modules.js',
        './src/shared/ng/*/*.js',
        './src/shared/js/**/*.js'
    ])
        // 追加依赖注入语法
        .pipe(ngAnnotate())
        // 合并到一个文件
        .pipe(concat('components.js'))
        // 包裹整个内容
        .pipe(header(definePrefix))
        .pipe(footer(defineSuffix))
        .pipe(uglify())
        .pipe(gulp.dest('./dest/release/shared/'));

    // 对应用下的 JS 进行压缩
    gulp.src('./src/app/!(css|translate)/**/*.js')
        .pipe(ngAnnotate())
        .pipe(uglify())
        .pipe(gulp.dest('./dest/release/app/'));

    // 对 app main.js 进行处理
    gulp.src('./src/app/main.js')
        .pipe(replaceRequirePathMain(['./src/assets/'])) // 对配置进行 min 替换
        .pipe(replace(/'.+?':\s?'.+?components\.ng',/, '')) // 因为上面的 components 都合并了, 所以这里要删除掉
        .pipe(replace(/'vo-libs-angular': \['angular'\],|'vo-libs-angular',/g, '')) // 同上, 需要清理
        .pipe(replace(/'vo-libs':\s?\['jquery'\],/, '\'vo-libs\': [\'jquery\', \'angular\'],')) // 同上, 需要追加 require 配置里的依赖
        .pipe(uglify())
        .pipe(gulp.dest('./dest/release/app/'));

    // 对登陆进行替换和压缩
    gulp.src('./src/login.js')
        .pipe(replaceRequirePathMain(['./src/'])) // 对登陆的配置进行 min 替换
        .pipe(ngAnnotate())
        .pipe(replace(/shared\/components\.ng/, 'shared/components')) // 因为 components 都合并了, 所以这里要替换掉
        .pipe(uglify())
        .pipe(gulp.dest('./dest/release/'));

    gulp.src('./src/app/app.css')
        .pipe(minifyCss())
        .pipe(gulp.dest('./dest/release/app/'));

    gulp.src('./src/login.css')
        .pipe(minifyCss())
        .pipe(gulp.dest('./dest/release/'));
});

//release.vms.module
gulp.task('release.vms.module', function(cb){
    fs.readFile('./src/app/vms.js', (err, content) => {
        fs.writeFile('./src/app/vms.js.bak', content, () => {
            var res = _ngAnnotate(content.toString(), {add: true});
            var newContent = new Buffer(res.src);
            fs.writeFile('./src/app/vms.js', newContent, () => {
                rjs.optimize(rjsBuildConfig, function(buildResponse){
                    console.log('buildResponse', buildResponse);
                    fs.rename('./src/app/vms.js.bak', './src/app/vms.js');
                    cb();
                }, cb);
            });
        });
    });
});

gulp.task('release', ['copy-release', 'pkg-release']);


// var ngAnnotate = require("ng-annotate");
// var applySourceMap = require("vinyl-sourcemaps-apply");
// var merge = require("merge");
// var BufferStreams = require("bufferstreams");
//
// var PLUGIN_NAME = "gulp-ng-annotate";
//
// // Function which handle logic for both stream and buffer modes.
// function transform(file, input, opts) {
//     var res = ngAnnotate(input.toString(), opts);
//     if (res.errors) {
//         var filename = "";
//         if (file.path) {
//             filename = file.relative + ": ";
//         }
//         throw new gutil.PluginError(PLUGIN_NAME, filename + res.errors.join("\n"));
//     }
//
//     if (opts.map && file.sourceMap) {
//         var sourceMap = JSON.parse(res.map);
//         sourceMap.file = file.relative;
//         applySourceMap(file, sourceMap);
//     }
//
//     return new Buffer(res.src);
// }