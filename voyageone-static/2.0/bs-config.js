const morgan = require('morgan'),
    proxy = require('http-proxy-middleware');

const proxyUrl = "http://10.0.1.108:8080";
const proxyMiddleware = proxy(proxyUrl);

const jsonPlaceholderProxy = proxy(['/cms','/core'], {
    target: proxyUrl,
    changeOrigin: true,             // for vhosted sites, changes host header to match to target's host
    logLevel: 'debug'
});
module.exports = {
    server: {
        baseDir: "develop",
        index: "login.html"
    },
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