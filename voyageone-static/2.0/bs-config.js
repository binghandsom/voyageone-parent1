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
            target: "http://10.0.1.108:8080",
            changeOrigin: true,
            logLevel: 'debug'
        })
    ]

};