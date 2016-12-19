var morgan = require('morgan'),
    proxy = require('http-proxy-middleware');

var proxyUrl = "http://localhost:8080",
    proxyMiddleware = proxy(proxyUrl);

module.exports = {
    server: {
        baseDir: "develop",
        index: "login.html"
    },
/*    files: [
        'develop/static/!**!/!*.css',
        'develop/modules/!**!/!*.{html,js}',
        'develop/components/!**!/!*.js'
    ],*/
    notify: false,
    port: 3000,
    middleware: [
        morgan('dev'),
        function (req, res, next) {
            if (req.method !== 'POST')
                return next();
            return proxyMiddleware(req, res, next);
        }
    ]
};