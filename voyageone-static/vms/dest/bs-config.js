var morgan = require('morgan'),
    less = require('less'),
    fs = require('fs'),
    proxy = require('http-proxy-middleware');

var proxyMiddleware = proxy('http://10.0.1.38:8080');

module.exports = {
    server: {
        baseDir: "release",
        index: "login.html"
    },
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
