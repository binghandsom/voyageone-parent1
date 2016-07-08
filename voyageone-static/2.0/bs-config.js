var morgan = require('morgan'),
    proxy = require('http-proxy-middleware');

var proxyUrl = require('./config').java_proxy,
    proxyMiddleware = proxy(proxyUrl);

module.exports = {
    server: {
        baseDir: "develop",
        index: "login.html"
    },
    files: [
        'develop/static/**/*.css',
        'develop/modules/**/*.{html,js}',
        'develop/components/**/*.js'
    ],
    notify: false,
    browser: 'google chrome',
    middleware: [
        morgan('dev'),
        function (req, res, next) {
            if (req.method !== 'POST')
                return next();
            return proxyMiddleware(req, res, next);
        }
    ]
};