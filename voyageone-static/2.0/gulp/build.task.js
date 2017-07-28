const gulp = require('gulp');
const glob = require('glob');
const babel = require('gulp-babel');
const gulpDebug = require('gulp-debug');
const debug = (title) => gulpDebug({title});
const concat = require('gulp-concat');
const header = require('gulp-header');
const footer = require('gulp-footer');
const ngAnnotate = require('gulp-ng-annotate');
const rename = require('gulp-rename');
const iife = require("gulp-iife");
const environments = require('gulp-environments');
const production = environments.production;
const development = environments.development;
const uglify = require('gulp-uglify');
const cleanCss = require('gulp-clean-css');
const minifyHtml = require('gulp-minify-html');
const replace = require('gulp-replace');
const _ = require('lodash');
const taskName = require('./env/task-name');
const paths = require('./env/path');
const entry = require('./env/entry');
const tryMin = require('./gulp-try-min');

function copy(path) {
    gulp.src(path.src)
        .pipe(debug('copy'))
        .pipe(gulp.dest(path.dist));
}

function buildResources() {

    // 处理图片文件
    copy(paths.img);
    // 处理字体文件
    // 因为样式文件被统一合并到一个 css 文件，所以文字文件必须统一放在一个目录，不能有子目录
    copy({src: glob.sync(paths.fonts.src), dist: paths.fonts.dist});
    // 复制第三方库文件
    copy(paths.libs);
    // 复制 json 文件
    copy({src: entry.modules.json, dist: paths.modules});

    // 处理全局样式
    _.forEach(entry.css, (v, k) => {
        gulp.src(v)
            .pipe(debug('global css'))
            .pipe(concat(`${k}.min.css`))
            .pipe(production(cleanCss()))
            .pipe(gulp.dest(paths.css.dist));
    });

    // 处理页面的样式
    gulp.src(entry.modules.css)
        .pipe(debug('modules css'))
        .pipe(production(cleanCss()))
        .pipe(gulp.dest(paths.modules));
}

function buildAppJS() {

    // 处理顶层页面的脚本
    gulp.src(entry.loginAndChannel.js)
        .pipe(debug('login/channel'))
        .pipe(ngAnnotate())
        .pipe(production(uglify()))
        .pipe(production(rename({suffix: ".min"})))
        .pipe(gulp.dest(paths.loginAndChannel));

    // 处理应用模块的脚本
    gulp.src(entry.modules.js)
        .pipe(debug('module'))
        .pipe(replace('version=', 'v=' + Date.parse(new Date())))
        .pipe(ngAnnotate())
        .pipe(production(tryMin([
            'develop',
            'publish/release/'
        ])))
        .pipe(production(uglify()))
        .pipe(gulp.dest(paths.modules));

    // 处理通用代码
    const definePrefix = 'define(function(){\n';
    const defineSuffix = '});';
    const formatOpt = {
        mangle: false,
        compress: false,
        output: {beautify: true}
    };

    gulp.src(entry.common.src)
        .pipe(debug('common'))
        .pipe(babel())
        .pipe(ngAnnotate())
        // .pipe(iife())
        .pipe(concat(paths.common.output))
        // .pipe(header(definePrefix))
        // .pipe(footer(defineSuffix))
        .pipe(development(uglify(formatOpt)))   // 开发环境: 格式化
        .pipe(production(uglify()))             // 生产环境: 压缩
        .pipe(gulp.dest(paths.common.dist));
}

function buildAppHtml() {

    // 处理顶层页面
    // 压缩，并替换引用的 min 脚本
    gulp.src(entry.loginAndChannel.html)
        .pipe(debug('login/channel html'))
        .pipe(production(replace(/require\(\['(\w+?)'\]\)/g, 'require(["$1.min"])')))
        .pipe(production(replace(/<script src="(libs\/.+?)js"><\/script>/g, '<script src="$1min.js"></script>')))
        .pipe(production(minifyHtml({empty: true})))
        .pipe(gulp.dest(paths.loginAndChannel));

    // 处理模块页面
    gulp.src(entry.modules.html)
        .pipe(debug('module html'))
        .pipe(replace('version=', 'v=' + Date.parse(new Date())))
        .pipe(production(replace(/libs\/require\.js\/(.+?)\/require.js/, 'libs/require.js/$1/require.min.js')))
        .pipe(production(minifyHtml({empty: true})))
        .pipe(gulp.dest(paths.modules));
}

gulp.task(taskName.build, function () {
    buildResources();
    buildAppJS();
    buildAppHtml();
});
