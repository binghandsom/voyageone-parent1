const glob = require('glob');
const fs = require('fs');
const babel = require("babel-core");

const common = require('../vars').build.common;

process.chdir('develop');

function readFile(file) {
    return new Promise(r => {
        fs.readFile(file, (_, fileContent) => r(fileContent))
    })
}

function babelTransform(c) {
    return babel.transform(c).code
}

glob('components/angular/*/*.js', function (err, files) {

    files.unshift('components/angular/angular.modules.js');

    const readingPromises = files.map(readFile);

    Promise.all(readingPromises)
        .then(contents => contents.join('\n'))
        .then(babelTransform)
        .then(content => fs.writeFile('components/dist/' + common.angular.concat, content,
            () => console.log('ng package succeed')));
});

glob('components/js/**/*.js', function (err, files) {

    const readingPromises = files.map(readFile);

    Promise.all(readingPromises)
        .then(contents => contents.join('\n'))
        .then(babelTransform)
        .then(content => fs.writeFile('components/dist/' + common.native.concat, content,
            () => console.log('common package succeed')));
});