var fs = require('fs');
var glob = require('glob');

var common = require('./vars').build.common;
var angularPackage = common.angular.dist + '/' + common.angular.concat;

var commonFiles = common.native.src;
var commonPackage = common.native.dist + '/' + common.native.concat;

glob('src/components/angular/*/*.js', function (err, files) {
    if (err) {
        console.error(err);
        return;
    }

    if (!files || !files.length) {
        console.warn('cant find any files !!! -> ' + src);
        return;
    }

    let code = '';

    code += 'define(function (require) {\n';

    code += '  require(\'components/angular/angular.modules\');\n';

    files.map(file => file.replace('src/', '').replace('.js', '')).forEach(file => {
        code += '  require(\'' + file + '\');\n';
    });
    
    code += '});';

    fs.writeFile(angularPackage, code);
});

glob(commonFiles, function (err, files) {
    if (err) {
        console.error(err);
        return;
    }

    if (!files || !files.length) {
        console.warn('cant find any files !!! -> ' + src);
        return;
    }

    let code = '';

    code += 'define(function (require) {\n';

    files.map(file => file.replace('src/', '').replace('.js', '')).forEach(file => {
        code += '  require(\'' + file + '\');\n';
    });
    
    code += '});';

    fs.writeFile(commonPackage, code);
});