'use strict';

var gulp = require('gulp');
var stylus = require('gulp-stylus');
var browserSync = require('browser-sync');
var reload = browserSync.reload;
var shell = require('gulp-shell');
gulp.task('stylus', function () {
    gulp.src('./src/stylus/main.styl')
        .pipe(stylus())
        .pipe(gulp.dest('./out/css'))
        .pipe(reload({stream: true}));
});
gulp.task('static', function () {
    gulp.src('./src/static/**/*')
        .pipe(gulp.dest('./out'))
        .pipe(reload({stream: true}));
});

gulp.task('stylus-watch', ['stylus'], function () {
    gulp.watch('./src/stylus/*.styl', ['stylus']);
});

gulp.task('static-watch', ['static'], function () {
    gulp.watch('./src/static/**/*', ['static']);
});

gulp.task('cljsbuild', shell.task(['lein cljsbuild auto tasks']));

gulp.task('serve', function () {
    browserSync({
        server: {
            baseDir: "./out"
        }
    });
});


gulp.task('default', ['cljsbuild', 'serve', 'static-watch','stylus-watch', 'cljsbuild']);



