var request = require('request');

var config = require('./config');

var proxy = config.java_proxy;

module.exports = {
    server: {
        baseDir: "develop",
        index: "/login.html",
        middleware: {
            /**
             * 对 post 请求进行 http 代理转发
             */
            2: function (req, res, next) {

                if (req.method !== 'POST') {
                    next();
                    return;
                }

                req.pipe(request(proxy + req.url)).pipe(res);
            },
            /**
             * 如果 index 设置无效, 最终会通过该中间件跳转
             */
            3: function (req, res, next) {

                if (req.url !== '/index.html') {
                    next();
                    return;
                }

                res.writeHead(302, {
                    'Location': 'login.html'
                });

                res.end();
            }
        }
    }
};