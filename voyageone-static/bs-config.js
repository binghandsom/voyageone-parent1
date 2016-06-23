
var morgan = require('morgan');

module.exports = {
    server: {
        baseDir: "src",
        index: "login.html"
    },
    browser: "google chrome",
    notify: false,
    middleware: [
        morgan('dev')
    ]
};