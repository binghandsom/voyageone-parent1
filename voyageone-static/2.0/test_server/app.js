'use strict';

var express = require('express');
var path = require('path');
var logger = require('morgan');
var request = require('request');

var app = express();
var port = 9090;
var proxy = 'http://10.0.1.156:8080';

app.use(logger('dev'));

app.get('/', function(req, res) {
  res.redirect('/login.html');
});

app.use(express.static(path.join(__dirname, '../develop')));

app.use(function (req, res) {
  req.pipe(request(proxy + req.path)).pipe(res);
});

app.listen(port, function() {
  console.log('Listening' + port);
  console.log('Started !');
});