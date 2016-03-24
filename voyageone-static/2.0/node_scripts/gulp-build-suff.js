var through = require('through2');
var gutil = require('gulp-util');
var File = gutil.File;

module.exports = function (path) {
    var nameMatcher = /angular.module\((.+), ?\[\]\)/;

    var skipMatcher = /\/\/@Skip/;

    var pref = 'voyageone.angular';

    var modules = [];

    return through.obj(function (file, enc, cb) {

        if (!file.isBuffer()) {
            gutil.log('Not Support');
            return cb();
        }

        var shortPath = file.path.replace(file.cwd, '');
        var content = String(file.contents);

        // 对特殊目标进行跳过检测,如果符合就跳过这个文件
        if (skipMatcher.test(content)) {
            gutil.log('skip this file: ' + shortPath);
            return cb();
        }

        var match = content.match(nameMatcher);

        // 如果匹配模块名失败,则跳过
        if (match.length < 2) {
            gutil.log('cant match this file: ' + shortPath);
            return cb();
        }

        var moduleName = match[1];
        // 去除两边的引号
        moduleName = moduleName.replace(/'|"/g, '');

        // 简单检查格式
        if (moduleName.indexOf(pref) !== 0) {
            gutil.log(`cant support this module name (must start with "${pref}"): ${moduleName}`);
            return cb();
        }

        var lastPointIndex = moduleName.lastIndexOf('.');
        if (lastPointIndex < 1) {
            gutil.log(`cant support this module name: ${moduleName}`);
            return cb();
        }

        var contain = modules.some(function (module) {
            return module === moduleName;
        });

        if (!contain) modules.push(moduleName);

        return cb();
    }, function (cb) {
        // 生成最终文件的文件名
        var suff = new File({path: path});
        suff.contents = new Buffer(`return angular.module("${pref}",[\n"${modules.join('",\n"')}"\n]);`);
        this.push(suff);
        return cb();
    });
};