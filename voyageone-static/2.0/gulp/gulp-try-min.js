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

        // 如果文件内容不包含 require.config 字样
        // 就跳过进入下一步
        if (!(/require\.config/.test(content)))
            return cb(null, file);

        content = content.replace(/paths:(.|\n|\r)+?}/, function(match) {

            // 匹配 require.config paths 配置里的路径
            // 检查这个路径下相同文件是否有已压缩的版本
            // 搜索的位置由用户传入的 dirs 决定
            // 如果有, 就替换当前 paths 里的配置
            return match.replace(/['"].+?['"].+?['"](.+?)['"],?/g, function (row, g1) {
                const min = dirs.some(function(dir) {
                    return exists(dir + '/' + g1 + '.min.js');
                });
                return min ? row.replace(g1, g1 + '.min') : row;
            });
        });

        // 写入修改后的内容
        file.contents = new Buffer(content);
        
        return cb(null, file);
        
    });
};