var proxy = require('http-proxy-middleware');
var morgan = require('morgan');
var proxyMiddleware = proxy('http://localhost:8080');

module.exports = {
    server: {
        baseDir: "src",
        index: "login.html"
    },
    browser: "google chrome",
    notify: false,
    middleware: [
        morgan('dev'),
        function (req, res, next) {
            if (req.method !== 'POST') {
                next();
                return;
            }
            return proxyMiddleware(req, res, next);
        }
    ]
};