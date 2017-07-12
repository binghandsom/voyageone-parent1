const fs = require('fs');
const app = require('./vars').build.common.appCss;
const login = require('./vars').build.common.loginCss;
let css;

css = '@import url(' + app.src.map(i => i.replace(app.dist, '')).join(');\n@import url(') + ');';
fs.writeFile(app.dist + '/' + app.concat, css);

css = '@import url(' + login.src.map(i => i.replace(login.dist, '')).join(');\n@import url(') + ');';
fs.writeFile(login.dist + '/' + login.concat, css);