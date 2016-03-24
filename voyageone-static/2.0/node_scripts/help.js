/**
 * NodeJs 辅助执行文件
 */

var fs = require('fs');

fs.readFile('develop/login.js', function (err, buffer) {
    var content = buffer.toString("utf-8").replace(/paths:(.|\n)+?\}/, function(match, g1) {
        return match.replace(/['"].+?['"].+?['"](.+?)['"],?/g, function (row, g1) {
            var jsMinFile = 'develop/' + g1 + '.min.js';
            try {
                fs.accessSync(jsMinFile);
                return row.replace(g1, g1 + '.min');
            } catch (e) {
                return row;
            }
        });
    });
});
