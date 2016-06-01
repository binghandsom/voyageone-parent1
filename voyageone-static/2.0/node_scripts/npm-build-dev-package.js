var fs = require('fs');
var common = require('./vars').build.common;
var glob = require('glob');
var def = 'define(%deps%%body%);';
var body = ', function () {\n\n%code%\n\n}';
var angularFiles = common.angular.src;
var angularPackage = common.angular.dist + '/' + common.angular.concat;
var commonFiles = common.native.src;
var commonPackage = common.native.dist + '/' + common.native.concat;
var suffPath = common.native.dist + '/' + common.angular.footerFile;
var staticRoot = 'develop/';

/**
 * 转换指定路径到相对路径, 并删除后缀名
 */
function clearPaths(pathArray) {
    return pathArray.map(i => i.replace('.js', '').replace(staticRoot, ''));
}

/**
 * 获取指定目录下的所有文件的相对路径
 */
function build(src, callback) {

    console.log("target path   -> " + src);

    glob(src, function (err, files) {
        if (err) {
            console.error(err);
            return;
        }

        if (!files || !files.length) {
            console.warn('cant find any files !!! -> ' + src);
            return;
        }

        var deps = '[\n  \'' + clearPaths(files).join("',\n  '") + '\'\n]';

        callback(deps);
    });
}

console.log("suff file     -> " + suffPath);

build(angularFiles, function (deps) {

    // 读取后缀文件内容
    // 并和所有相对路径拼接
    // 组合成完整的 angular component 声明文件
    fs.readFile(suffPath, function (err, data) {

        if (err) {
            console.error(err);
            return;
        }

        var angular = def.replace('%deps%', deps).replace('%body%', body.replace('%code%', data));

        console.log("will write to -> " + angularPackage);

        fs.writeFile(angularPackage, angular);
    });
});

build(commonFiles, function (deps) {

    // 类似 angular component 的编译过程
    // 普通 component 不需要组合代码, 所以没有后缀
    var code = def.replace('%deps%', deps).replace('%body%', '');

    console.log("will write to -> " + angularPackage);

    fs.writeFile(commonPackage, code);
});



