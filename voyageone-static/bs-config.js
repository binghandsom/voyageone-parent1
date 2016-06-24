var morgan = require('morgan');
var less = require('less');
var fs = require('fs');

module.exports = {
    server: {
        baseDir: "src",
        index: "login.html"
    },
    open: false,
    files: [
        "src/app/**/*.{html,css,js}",
        "src/components/**/*.js",
        {
            match: "src/app/css/**/*.less",
            fn: () => {
                fs.readFile('src/app/css/main.less', (err, content) => {
                    if (err) {
                        console.error(err);
                        return;
                    }
                    less.render(String(content), {
                        paths: ['src/app/css/']
                    }).then(
                        (output) => fs.writeFile('src/app/app.css', output.css),
                        (e) => console.log(e.message)
                    );
                });
            }
        }
    ],
    browser: "google chrome",
    notify: false,
    middleware: [
        morgan('dev')
    ]
};