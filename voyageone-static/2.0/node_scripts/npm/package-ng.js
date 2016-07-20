var UglifyJS = require('uglify-js');
var glob = require('glob');
var fs = require('fs');

var common = require('../vars').build.common;

process.chdir('develop');

glob('components/angular/*/*.js', function (err, files) {

    files.unshift('components/angular/angular.modules.js');

    var result = UglifyJS.minify(files, {
        sourceRoot: '../../',
        mangle: false,
        outSourceMap: common.angular.concat + '.map'
    });

    fs.writeFile('components/dist/' + common.angular.concat, result.code);
    fs.writeFile('components/dist/' + common.angular.concat + '.map', result.map);
});