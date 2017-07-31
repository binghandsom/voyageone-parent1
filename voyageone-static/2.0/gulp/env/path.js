module.exports = {
    fonts: {
        src: 'develop/static/**/fonts/*',
        dist: 'publish/release/static/fonts'
    },
    img: {
        src: 'develop/static/img/**',
        dist: 'publish/release/static/img'
    },
    css: {
        dist: 'publish/release/static/css'
    },
    loginAndChannel: 'publish/release/',
    modules: 'publish/release/modules',
    libs: {
        src: ['develop/libs/**/*.js', 'develop/libs/**/*.css', 'develop/libs/**/*.png'],
        dist: 'publish/release/libs'
    },
    common: {
        output: 'common.js',
        dist: 'publish/release/components/dist'
    }
};
