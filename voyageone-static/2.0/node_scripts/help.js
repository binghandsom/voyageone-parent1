/**
 * NodeJs 辅助执行文件
 */

const fs = require('fs');
const glob = require('glob');

glob('../develop/components/angular/*/*.js', function (err, files) {

    if (err) {
        console.error(err);
        return;
    }

    let once = true;

    files.forEach(file => fs.readFile(file, (err, contents) => {

        let strings = String(contents);

        strings = strings.replace(/^\(function\(\)\s?\{/, "").replace(/\}\)\(\);$/, "");

        if (once) {

            let result = UglifyJS.minify(strings, {
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