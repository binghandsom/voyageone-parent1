'use strict';

var express = require('express');
var path = require('path');
var logger = require('morgan');
var request = require('request');

var config = require('./config');

var app = express();
var port = config.port;
var proxy = config.http_proxy;

if (!port || !proxy) {
    throw '没有设置正确的配置, 启动前需要设置 port 和 http_proxy 属性到 config.js 文件中, 如果没有这个文件, 请自行创建。';
}

app.use(logger('dev'));

app.get('/', function (req, res) {
    res.redirect('/login.html');
});

app.use(express.static(path.join(__dirname, '../develop')));

app.use(function (req, res) {
    req.pipe(request(proxy + req.path)).pipe(res);
});

app.listen(port, function () {
    console.log('Listening' + port);
    console.log('Started !');
});