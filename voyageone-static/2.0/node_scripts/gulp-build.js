var gulp = require('gulp');
var debug = require('gulp-debug');
var suffBuilder = require('./gulp-build-suff');
var actionsDesc = require('./gulp-build-actions');

var build = require('./vars').build;
var tasks = require('./vars').tasks;

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