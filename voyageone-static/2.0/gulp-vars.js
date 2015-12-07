var extend = require('extend');

var versions = {
  // 整个工程版本号
  publish: "2.0.0",
  // voyageone-angular-com.js的版本号
  angularCom: "2.0.0",
  // voyageone-com.js的版本号
  com: "2.0.0",
  // 静态资源的版本号
  statics: "2.0.1"
};
var build = {
  version: versions.statics,
  common: {
    angular: {
      src: 'develop/components/angular/*/*.js',
      dist: 'develop/components/dist',
      footerFile: 'develop/components/angular/voyageone.angular.com.suffix',
      concat: 'voyageone.angular.com.js',
      version: versions.angularCom
    },
    native: {
      src: 'develop/components/js/*.js',
      dist: 'develop/components/dist',
      concat: 'voyageone.com.min.js',
      map: 'voyageone.com.min.js.map',
      version: versions.com
    },
    appCss: {
      src: [
        'develop/static/css/twitter-bootstrap/local/bootstrap.css',
        'develop/static/css/assets/animate.css',
        'develop/static/css/assets/font-awesome.css',
        'develop/static/css/assets/simple-line-icons.css',
        'develop/static/css/font.css',
        'develop/static/css/app.css'
      ],
      dist: 'develop/static/',
      concat: 'app.min.css'
    },
    loginCss: {
      src: [
        'develop/static/css/login.css',
        'develop/static/css/font-awesome.css'
      ],
      dist: 'develop/static/',
      concat: 'login.min.css'
    }
  }
};
var publish = {
  version: versions.publish,
  static: {
    fonts: {
      src: 'develop/static/fonts/**',
      dist: 'publish/static/' + versions.statics + '/fonts'
    },
    img: {
      src: 'develop/static/img/**',
      dist: 'publish/static/' + versions.statics + '/img'
    },
    css: {
      src: 'develop/static/*.css',
      dist: 'publish/static/' + versions.statics
    }
  },
  components: {
    angular: {
      dist: 'publish/voaygeone-angular-com/' + versions.angularCom
    },
    native: {
      dist: 'publish/voyageone-com/' + versions.com
    }
  },
  libs: {
    src: 'develop/libs/**/*min.js'
  },
  loginAndChannel: {
    js: 'develop/*.js',
    html: 'develop/*.html'
  },
  views: {
    js: 'develop/views/**/*.js',
    html: 'develop/views/**/*.html',
    json: 'develop/views/**/*.json'
  },
  release: {
    static: {
      fonts: 'publish/release/' + versions.publish + '/static/fonts',
      img: 'publish/release/' + versions.publish + '/static/img',
      css: 'publish/release/' + versions.publish + '/static'
    },
    components: 'publish/release/' + versions.publish + '/components/dist',
    libs: 'publish/release/' + versions.publish + '/libs',
    views: 'publish/release/' + versions.publish + '/views',
    loginAndChannel: 'publish/release/' + versions.publish
  }
};
var tasks = {
  build: {
    angular: 'build-angular-com',
    com: 'build-com',
    css: {
      all: 'build-css',
      app : 'build-css-app',
      login : 'build-css-login'
    }
  },
  publish: {
    all: 'publish',
    statics: 'publish-static',
    angular: 'publish-angular-com',
    com: 'publish-com',
    libs: 'publish-libs',
    views: 'publish-views'
  }
};

module.exports = {
  build: build,
  publish: publish,
  tasks: tasks
};