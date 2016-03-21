/**
 * NodeJs 辅助执行文件
 */

var fs = require('fs');
var glob = require('glob');

// base -> develop/components/angualr
glob('**/*.js', function(err, files) {
    b(files);
});

function a(files) {
    files.forEach(function (file) {
        console.log(file);
        fs.readFile(file, function (err, buffer) {
            var content = buffer.toString("utf-8").replace(/^\(function \(\) \{/, '').replace(/\}\)\(\);$/, '');
            fs.writeFile(file, content);
        });
    });
}

function b(files) {
    files.forEach(function (file) {
        console.log(file);
        fs.readFile(file, function (err, buffer) {
            var content = buffer.toString("utf-8").replace(/\n( +?)\/\*\*(.|\n)+?\*\//g, function(match, g1) {
                return match.replace(/\n +?\*/g, function() {
                    return '\n ' + g1 + '*';
                });
            });
            fs.writeFile(file, content);
        });
    });
}