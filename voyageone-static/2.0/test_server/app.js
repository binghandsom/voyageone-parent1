var express = require('express');
var fs = require("fs");
var extend = require("extend");
var app = express();

var commonError = {
  'result': 'NG',
  'messageType': 1,
  'message': 'Some error',
  'formValidateList': [],
  'permissions': [],
  'resultInfo': {},
  'forward': '',
  'token': '2015020600000001'
};

var mappings = {};

// 按尾部的设定,进行目录注册
(function (routes) {
  return routes.forEach(function (name) {
    var phyPath, routePath;
    routePath = "./routes/" + name;
    phyPath = __dirname + "\/routes\/" + name + ".js";
    return fs.exists(phyPath, function (isExists) {
      if (isExists) {
        return extend(mappings, require(routePath));
      } else {
        return console.log("未找到：" + routePath + "，物理路径：" + phyPath);
      }
    });
  });
})(["core"]);

// 设定 express 渲染根目录
app.set("views", "./test_server/data");
// 设定对 json 的渲染方式
app.engine("json", function (filePath, options, callback) {
  return fs.readFile(filePath, function (err, content) {
    if (err) {
      return callback(new Error(err));
    }
    return callback(null, content);
  });
});
// 设定捕捉全部请求
app.post("*", function (req, res, next) {
  var url, view;
  url = req.url;
  view = mappings[url];
  console.log(url + " -> " + view);
  if (view) {
    if (typeof view === "function") {
      return view(req, res, next);
    } else {
      return res.render(view + ".json");
    }
  } else {
    var json = require(url);
    if (json) {
      return res.json(json);
    }
    return res.json(commonError);
  }
});
// 启动监听
app.listen(8080);

module.exports = app;