const fs = require('fs');
const glob = require('glob');

glob('admin/shared/ng/*/*.js', function (err, files) {
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

    code += '  require(\'/shared/ng/modules.js\');\n';

    files.map(file => file.replace('admin/', '/')).forEach(file => {
        code += '  require(\'' + file + '\');\n';
    });

    code += '});';

    fs.writeFile('admin/shared/components.ng.js', code);
});

glob('admin/shared/js/*/*.js', function (err, files) {
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

    files.map(file => file.replace('admin/', '/')).forEach(file => {
        code += '  require(\'' + file + '\');\n';
    });

    code += '});';

    fs.writeFile('admin/shared/components.js', code);
});