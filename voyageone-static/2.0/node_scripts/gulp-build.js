var gulp = require('gulp');
var ngAnnotate = require('gulp-ng-annotate');
var concat = require('gulp-concat');
var debug = require('gulp-debug');
var header = require('gulp-header');
var footer = require('gulp-footer');
var minifyCss = require('gulp-minify-css');
var uglify = require('gulp-uglify');
var fs = require('fs');
var suffBuilder = require('./gulp-build-suff');
var actionsDesc = require('./gulp-build-actions');

var build = require('./vars').build;
var tasks = require('./vars').tasks;

var definePrefix = 'define(function(){\n';
var defineSuffix = '});';
var headerSingle = '(function(){\n';
var footerSingle = '})();';
var encode = 'utf-8';

gulp.task(tasks.build.actions, function() {
  return gulp.src(build.actions.src)
    .pipe(debug())
    .pipe(actionsDesc())
    .pipe(gulp.dest('./'));
});

gulp.task(tasks.build.angular_suff, function () {
  return gulp.src(build.common.angular.src)
    .pipe(debug())
    .pipe(suffBuilder(build.common.angular.footerFile))
    .pipe(gulp.dest(build.common.angular.dist));
});