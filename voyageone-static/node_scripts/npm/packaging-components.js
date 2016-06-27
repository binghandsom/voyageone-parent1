var fs = require('fs');
var glob = require('glob');

glob('src/shared/ng/*/*.js', function (err, files) {
    if (err) {
        console.error(err);
        return;
    }

    if (!files || !files.length) {
        console.warn('cant find any files !!!');
        return;
    }

    let code = '';

    code += 'define(function (require) {\n';

    code += '  require(\'shared/ng/modules\');\n';

    files.map(file => file.replace('src/', '').replace('.js', '')).forEach(file => {
        code += '  require(\'' + file + '\');\n';
    });
    
    code += '});';

    fs.writeFile('src/shared/components.ng.js', code);
});

glob('src/shared/js/*/*.js', function (err, files) {
    if (err) {
        console.error(err);
        return;
    }

    if (!files || !files.length) {
        console.warn('cant find any files !!!');
        return;
    }

    let code = '';

    code += 'define(function (require) {\n';

    files.map(file => file.replace('src/', '').replace('.js', '')).forEach(file => {
        code += '  require(\'' + file + '\');\n';
    });
    
    code += '});';

    fs.writeFile('src/shared/components.js', code);
});