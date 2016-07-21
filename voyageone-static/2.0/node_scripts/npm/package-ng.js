var glob = require('glob');
var fs = require('fs');

var common = require('../vars').build.common;

process.chdir('develop');

glob('components/angular/*/*.js', function (err, files) {

    files.unshift('components/angular/angular.modules.js');

    var content = '';

    files.forEach((file) => {
        content += '\n\n/*****************************/\n\n';
        content += fs.readFileSync(file);
    });

    fs.writeFile('components/dist/' + common.angular.concat, content);
});