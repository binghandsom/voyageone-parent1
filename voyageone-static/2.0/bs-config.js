const morgan = require('morgan');
const proxy = require('http-proxy-middleware');

module.exports = {
    "server": {
        baseDir: "./develop",
        index: 'login.html'
    },
    "middleware": [
        morgan('dev'),
        proxy(['/cms', '/core'], {
            target: "http://127.0.0.1:8080",
            changeOrigin: true,
            logLevel: 'debug'
        })
    ]

};