/**
 * 配合 vresource 自动声明 datasource service.
 * 该插件自动生成用于描述 datasource service 的描述文件.
 */

var through = require('through2');
var gutil = require('gulp-util');
var File = gutil.File;

module.exports = function () {

  var headDesc =
      "/**\n" +
      " * @description\n" +
      " * 该文件用于描述 actions.json 的内容, 不需要被任何模块引用!\n" +
      " * 注意: 该文件由 gulp 生成! 请不要手动修改\n" +
      " */\n\n";

  var methodTemplate =
      "  /**\n" +
      "   * @param {object} [data] 远程请求的参数\n" +
      "   * @return {Promise} Promise\n" +
      "   */\n" +
      "  ${method} (data) {\n" +
      "    /* auto imp by vresource */\n" +
      "  }\n";

  function gentDoc(name, actions) {

    var members = "";

    for (var action in actions) {
      // 不对 root 进行创建
      if (action === "root") continue;
      // 额外的检查
      if (actions.hasOwnProperty(action)) {
        members += methodTemplate.replace('${method}', action);
      }
    }

    return `class ${name.charAt(0).toUpperCase() + name.slice(1)} {\n${members}}\n\n`;
  }

  function register(name, actions) {

    if (!actions) return null;

    if (typeof actions !== 'object') return null;

    if (actions.root) {
      return gentDoc(name, actions);
    }

    var doc = "";

    for (var childName in actions) {
      // 额外的检查
      if (actions.hasOwnProperty(childName)) {

        var part = register(childName, actions[childName]);
        if (part) doc += part;
      }
    }

    return doc;
  }

  return through.obj(function (file, enc, cb) {

    if (!file.isBuffer()) {
      gutil.log('Not Support');
      return cb();
    }

    var contents = String(file.contents);

    if (!contents) {
      return cb();
    }

    var actions = JSON.parse(contents);

    var desc = headDesc + register(null, actions);

    var docFile = new File({
      path: file.path.replace('.json', '.doc.js'),
      contents: new Buffer(desc)
    });

    return cb(null, docFile);
  });
};