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

gulp.task('stylus-watch', ['stylus'], function () {
    gulp.watch('./src/stylus/*.styl', ['stylus']);
});

gulp.task('cljsbuild', shell.task(['lein cljsbuild auto tasks']))

gulp.task('serve', function () {
    browserSync({
        server: {
            baseDir: "./"
        }
    });
});


gulp.task('default', ['cljsbuild', 'serve', 'stylus-watch', 'cljsbuild']);



