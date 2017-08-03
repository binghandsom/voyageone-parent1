var morgan = require('morgan'),
    proxy = require('http-proxy-middleware');

var proxyUrl = "http://10.0.1.143:8080";
var proxyMiddleware = proxy(proxyUrl);

var jsonPlaceholderProxy = proxy(['/cms','/core'], {
    target: proxyUrl,
    changeOrigin: true,             // for vhosted sites, changes host header to match to target's host
    logLevel: 'debug'
});
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
        morgan('dev'),jsonPlaceholderProxy
        ,function (req, res, next) {
            if (req.method !== 'POST')
                return next();
            return proxyMiddleware(req, res, next);
        },
    ]
};