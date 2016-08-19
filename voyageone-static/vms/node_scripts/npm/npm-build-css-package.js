var fs = require('fs');
var app = require('./vars').build.common.appCss;
var login = require('./vars').build.common.loginCss;
var css;

css = '@import url(' + app.src.map(i => i.replace(app.dist, '')).join(');\n@import url(') + ');';
fs.writeFile(app.dist + '/' + app.concat, css);

css = '@import url(' + login.src.map(i => i.replace(login.dist, '')).join(');\n@import url(') + ');';
fs.writeFile(login.dist + '/' + login.concat, css);