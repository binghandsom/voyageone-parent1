var morgan = require('morgan'),
    less = require('less'),
    fs = require('fs'),
    proxy = require('http-proxy-middleware');

var proxyMiddleware = proxy('http://localhost:8080');

module.exports = {
    server: {
        baseDir: "admin",
        index: "login.html"
    },
    open: false,
    files: [
        "admin/app/**/*.{html,css,js}",
        "admin/assets/**/*.css",
        "admin/shared/**/*.js",
        "admin/components/**/*.js",
        {
            match: "admin/app/css/**/*.less",
            fn: () => {
                fs.readFile('admin/app/css/main.less', (err, content) => {
                    if (err) {
                        console.error(err);
                        return;
                    }
                    less.render(String(content), {
                        paths: ['admin/app/css/']
                    }).then(
                        (output) => fs.writeFile('admin/app/app.css', output.css),
                        (e) => console.log(e.message)
                    );
                });
            }
        }
    ],
    browser: "google chrome",
    notify: false,
    middleware: [
        morgan('dev'),
        function (req, res, next) {
            if (req.method !== 'POST')
                return next();
            return proxyMiddleware(req, res, next);
        }
    ]
};
