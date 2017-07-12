const through = require('through2');
const fs = require('fs');

function exists(path) {
    try {
        fs.accessSync(path);
        return true;
    } catch (e) {
        if (e.message.indexOf('no such') < 0)
            console.error(e);
        return false;
    }
}

module.exports = function (dirs) {
    
    return through.obj(function (file, enc, cb) {

        let content = String(file.contents);

        if (!(/require\.config/.test(content)))
            return cb(null, file);

        content = content.replace(/paths:(.|\n)+?\}/, function(match) {
            return match.replace(/['"].+?['"].+?['"](.+?)['"],?/g, function (row, g1) {
                let min = dirs.some(function(dir) {
                    return exists(dir + '/' + g1 + '.min.js');
                });
                return min ? row.replace(g1, g1 + '.min') : row;
            });
        });

        file.contents = new Buffer(content);
        
        return cb(null, file);
        
    });
};