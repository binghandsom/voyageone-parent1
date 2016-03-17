var fs = require('fs');
var common = require('./vars').build.common;
var glob = require('glob');
var angularFiles = common.angular.src;
var angularPackage = common.angular.dist + '/' + common.angular.concat;
var commonFiles = common.native.src;
var commonPackage = common.native.dist + '/' + common.native.concat;

var build = function (src, dist) {

    console.log("target path -> " + src);

    glob(src, function (err, files) {
        if (err) {
            console.error(err);
            return;
        }

        if (!files || !files.length) {
            console.warn('cant find any files !!!');
            return;
        }

        fs.writeFile(dist, 'define([\n  \'../' + files.join("',\n  '../") + '\'\n]);');
    });

    console.log("write to    -> " + dist + "\n");

};

console.log("\nWill build package for develop.\n");
console.log("cwd         -> " + process.cwd() + "\n");

build(angularFiles, angularPackage);
build(commonFiles, commonPackage);

console.log("completed !\n");