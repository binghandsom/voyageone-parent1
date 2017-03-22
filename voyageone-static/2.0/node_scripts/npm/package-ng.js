const glob = require('glob');
const fs = require('fs');

const common = require('../vars').build.common;

process.chdir('develop');

glob('components/angular/*/*.js', function (err, files) {

    files.unshift('components/angular/angular.modules.js');

    let content = '';

    files.forEach((file) => {
        content += '\n\n/*****************************/\n\n';
        content += fs.readFileSync(file);
    });

    fs.writeFile('components/dist/' + common.angular.concat, content);
});

glob('components/js/**/*.js', function (err, files) {

    let content = '';

    files.forEach((file) => {
        content += '\n\n/*****************************/\n\n';
        content += fs.readFileSync(file);
    });

    fs.writeFile('components/dist/' + common.native.concat, content);
});