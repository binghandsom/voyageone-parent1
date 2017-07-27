const morgan = require('morgan');
const proxy = require('http-proxy-middleware');

// 开发时，文件只监视不用编译的 html 和 css
// js 的监视交给 gulp
module.exports = {
    server: {
        baseDir: "develop",
        index: "login.html"
    },
    files: [
        'develop/**/*.html',
        'develop/**/*.css',
    ],
    port: 3000,
    middleware: [
        morgan('dev'),
        proxy(['/cms', '/core'], {
            target: "http://localhost:8080",
            changeOrigin: true,
            logLevel: 'debug'
        })
    ]
};
