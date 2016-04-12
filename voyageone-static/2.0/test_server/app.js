var express = require('express');
var fs = require("fs");
var extend = require("extend");
var app = express();

var mappings = {};

// 按尾部的设定,进行目录注册
(function (routes) {
  return routes.forEach(function (name) {
    var phyPath, routePath;
    routePath = "./modules/" + name + "\/" + name;
    phyPath = __dirname + "\/modules\/" + name + "\/" + name + ".js";
    return fs.exists(phyPath, function (isExists) {
      if (isExists) {
        return extend(mappings, require(routePath));
      } else {
        return console.log("未找到：" + routePath + "，物理路径：" + phyPath);
      }
    });
  });
})(["core","cms"]);

// 设定 express 渲染根目录
app.set("views", "./test_server/modules");
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
      req.on('data', function (data) {
        return view(JSON.parse(data), res, next);
      })
    } else {
      return res.render(view + ".json");
    }
  } else {
    if (view === undefined) {
      url = url.substring(1, url.length);
      return res.render(url + ".json");
    }
  }
});

console.log(process.cwd());

var staticRouter =  express["static"](process.cwd() + "\/develop");

app.use(function (req, res, next) {
  return staticRouter(req, res, next);
});

app.listen(8084);

module.exports = app;