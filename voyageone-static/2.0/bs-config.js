var morgan = require('morgan'),
    proxy = require('http-proxy-middleware');

var proxyMiddleware = proxy('http://localhost:8080');

module.exports = {
    server: {
        baseDir: "develop",
        index: "adminLogin.html"
    },
/*    files: [
        'develop/static/!**!/!*.css',
        'develop/modules/!**!/!*.{html,js}',
        'develop/components/!**!/!*.js'
    ],*/
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