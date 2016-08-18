/**
 * NodeJs 辅助执行文件
 */

var fs = require('fs');
var glob = require('glob');

glob('../src/components/angular/*/*.js', function (err, files) {

    if (err) {
        console.error(err);
        return;
    }

    let once = true;

    files.forEach(file => fs.readFile(file, (err, contents) => {

        var strings = String(contents);

        strings = strings.replace(/^\(function\(\)\s?\{/, "").replace(/\}\)\(\);$/, "");

        if (once) {

            var result = UglifyJS.minify(strings, {
                beautify: true,
                fromString: true,
                compress: null,
                mangle: false
            });

            console.log(result.code);
            once = false;
        }

    }))

});