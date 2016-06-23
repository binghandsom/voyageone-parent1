
var morgan = require('morgan');

module.exports = {
    server: {
        baseDir: "src",
        index: "login.html"
    },
    files: ["src/**/*.css", "src/**/*.html"],
    browser: "google chrome",
    notify: false,
    middleware: [
        morgan('dev')
    ]
};