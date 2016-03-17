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

function clearPaths(pathArray) {
    return pathArray.map(i => i.replace('.js', '').replace(staticRoot, ''));
}

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

    var code = def.replace('%deps%', deps).replace('%body%', '');

    console.log("will write to -> " + angularPackage);

    fs.writeFile(commonPackage, code);
});



