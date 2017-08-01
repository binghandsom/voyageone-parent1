const path = require('path');
const gulp = require('gulp');
const concat = require('gulp-concat');
const debug = require('gulp-debug');
const babel = require('gulp-babel');
const cache = require('gulp-cached');
const remember = require('gulp-remember');
const browserSync = require('browser-sync');
const _ = require('lodash');
const through = require('through2');
const paths = require('./env/path');

const WORK_DIR = process.cwd();
const scripts = [
    'develop/modules/**/*.js',
    'develop/*.js'
];
const common = [
    'develop/components/angular/**/*.js',
    'develop/components/js/**/*.js'
];

const beforeBabel = 'scripts-before-babel';
const afterBabel = 'scripts-after-babel';

const reload = _.debounce(browserSync.reload, 500);

gulp.task('dev:compile', () => gulp.src(scripts)
    .pipe(cache(beforeBabel))
    .pipe(debug({title: 'dev:compile'}))
    .pipe(babel())
    .pipe(cache(afterBabel))
    .pipe(through.obj((file, enc, cb) => {
        // 单个文件不处理
        cb(null, file);
    }, (cb) => {
        // 在本次所有文件处理结束后调用刷新
        reload();
        cb();
    }))
);

// 通过下面的代码
// .pipe(concat({path: `${WORK_DIR}/develop/components/dist/${paths.common.output}`}))
// 在内存中模拟 common.js 文件
gulp.task('dev:common:compile', () => gulp.src(common)
    .pipe(cache(beforeBabel))
    .pipe(debug({title: 'dev:common:compile'}))
    .pipe(babel())
    .pipe(remember('common'))
    .pipe(concat({path: `${WORK_DIR}/develop/components/dist/${paths.common.output}`}))
    .pipe(cache(afterBabel))
    .pipe(through.obj((file, enc, cb) => {
        // 单个文件不处理
        cb(null, file);
    }, (cb) => {
        // 在本次所有文件处理结束后调用刷新
        reload();
        cb();
    }))
);

gulp.task('default', ['dev:common:compile', 'dev:compile'], function () {

    const bsOption = require('../config/browser-sync');

    // 给 bs 配置增加从内存获取代码的中间件
    bsOption.middleware.push(function (req, res, next) {
        // 从编译后的缓存里直接获取代码
        const absPath = path.join(WORK_DIR, 'develop', req.url.replace(/\?.+$/, ''));
        const script = cache.caches[afterBabel][absPath];
        if (script) {
            return res.end(script);
        }
        next();
    });

    browserSync(bsOption);

    // 启动结束后，开始监视 js 文件
    // 即时编译并存于缓存
    const watcher = gulp.watch(scripts, ['dev:compile']);
    watcher.on('change', function (event) {
        if (event.type === 'deleted') {
            delete cache.caches[beforeBabel][event.path];
            delete cache.caches[afterBabel][event.path];
        }
    });

    gulp.watch(common, ['dev:common:compile']);
});
