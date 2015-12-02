/**
 * @Description
 * Bootstrap Main App
 * @Date:    2015-11-19 20:31:14
 * @User:    Jonas
 * @Version: 0.2.0
 */

require.config({
  baseUrl: '../../',
  paths: {
    'voyageone-angular-com': 'components/dist/voyageone.angular.com',
    'voyageone-com': 'components/dist/voyageone.com.min',
    'angular-animate': 'libs/angular.js/1.4.7/angular-animate',
    'angular-route': 'libs/angular.js/1.4.7/angular-route',
    'angular-sanitize': 'libs/angular.js/1.4.7/angular-sanitize',
    'angular-cookies': 'libs/angular.js/1.4.7/angular-cookies',
    'angular': 'libs/angular.js/1.4.7/angular',
    'angular-translate': 'libs/angular-translate/2.8.1/angular-translate',
    'angular-block-ui': 'libs/angular-block-ui/0.2.1/angular-block-ui',
    'angular-ui-bootstrap': 'libs/angular-ui-bootstrap/0.14.3/ui-bootstrap-tpls-0.14.3.min',
    'angularAMD': 'libs/angularAMD/0.2.1/angularAMD.min',
    'ngload': 'libs/angularAMD/0.2.1/ngload.min',
    'jquery': 'libs/jquery/2.1.4/jquery',
    'underscore': 'libs/underscore.js/1.8.3/underscore',
    'css': 'libs/require-css/0.1.8/css',
    'json': 'libs/requirejs-plugins/1.0.3/json',
    'text': 'libs/require-text/2.0.12/text'
  },
  shim: {
    'voyageone-angular-com': ['angularAMD'],
    'angular-sanitize': ['angular'],
    'angular-route': ['angular'],
    'angular-animate': ['angular'],
    'angular-cookies': ['angular'],
    'angular-translate': ['angular'],
    'angular-block-ui': ['angular', 'css!libs/angular-block-ui/0.2.1/angular-block-ui.css'],
    'angular-ui-bootstrap': ['angular'],
    'angular': {exports: 'angular', deps: ['jquery']},
    'jquery': {exports: 'jQuery'},
    'json': ['text'],
    'angularAMD': [
      'angular',
      'angular-route',
      'angular-sanitize',
      'angular-animate',
      'angular-translate',
      'angular-cookies',
      'ngload'
    ]
  }
});

// Bootstrap App !!
requirejs([
  'angularAMD',
  'angular',
  'underscore',
  'json!views/cms/routes.json',
  'json!views/cms/actions.json',
  'json!views/cms/translate/en.json',
  'json!views/cms/translate/zh.json',
  'voyageone-angular-com',
  'voyageone-com',
  'angular-block-ui',
  'angular-ui-bootstrap'
], function (angularAMD, angular, _, routes, actions, enTranslate, zhTranslate) {
  var mainApp = angular.module('voyageone.cms', [
        'ngRoute',
        'ngAnimate',
        'ngCookies',
        'ngSanitize',
        'pascalprecht.translate',
        'blockUI',
        'voyageone.angular',
        'ui.bootstrap'
      ])

      // define
      .constant('actions', actions)
      .constant('routes', routes)
      .constant('languageType', {
        en: {
          name: "en",
          value: enTranslate
        },
        zh: {
          name: "zh",
          value: zhTranslate
        }
      })

      .config(function (blockUIConfig) {
        blockUIConfig.autoBlock = false;
      })

      // router config.
      .config(function ($routeProvider) {
        return _.each(routes, function (module) {
          return $routeProvider.when(module.hash, angularAMD.route ({
            templateUrl: module.templateUrl,
            controllerUrl: module.controllerUrl
          }));
        });
      })

      // translate config.
      .config(function ($translateProvider, languageType) {

        _.forEach(languageType, function (type) {
          $translateProvider.translations (type.name, type.value);
        });
      })

      // main service.
      .service('appService', fnAppService)

      // main controller.
      .controller('headerCtrl', fnHeaderCtrl)
      .controller('appCtrl', fnAppCtrl);

  fnAppService.$inject = ['$q', 'ajaxService', 'cookieService', 'translateService', 'actions'];
  function fnAppService($q, ajaxService, cookieService, translateService, actions) {

    this.getMenuHeaderInfo = fnGetMenuHeaderInfo;
    this.setMenu = fnSetMenu;
    this.clearChannel = fnClearChannel;
    this.setLanguage = fnSetLanguage;
    this.logout = fnLogout;

    /**
     * get the system info.
     * includes: systemMenu, languages,
     * @returns {*}
       */
    function fnGetMenuHeaderInfo () {
      var defer = $q.defer ();
      ajaxService.post(actions.core.home.menu.getMenuHeaderInfo)
          .then(function (data) {
            //var data = response.data;
            var languageType = _.isEmpty(cookieService.language()) ? translateService.getBrowserLanguage() : cookieService.language();
            _.forEach(data.languageList, function (language) {

              if (_.isEqual(languageType, language.add_name2)) {
                data.userInfo.language = language.add_name1;
              }
            });
            // TODO
            data.userInfo.application = 'CMS';//cookieService.application();
            defer.resolve (data);
          });
      return defer.promise;
    }

    /**
     * set menu.
     * @param menu
     * @returns {*}
       */
    function fnSetMenu (menuTitle) {
      var defer = $q.defer ();
      cookieService.application (menuTitle);
      defer.resolve(menuTitle.toLocaleLowerCase());
      return defer.promise;
    }

    /**
     * clear selected channel
     * @returns {*}
     */
    function fnClearChannel () {
      var defer = $q.defer ();
      cookieService.channel ("");
      defer.resolve();
      return defer.promise;
    }

    /**
     * set language.
     * @param language
     * @returns {*}
       */
    function fnSetLanguage (language) {
      var defer = $q.defer ();
      cookieService.language(language.add_name1);
      translateService.setLanguage(language.add_name2);
      defer.resolve(language.add_name1);
      return defer.promise;
    }

    /**
     * clear all cookie.
     * @returns {*}
       */
    function fnLogout() {
      var defer = $q.defer ();
      cookieService.removeAll();
      defer.resolve();
      return defer.promise;
    }
  }

  fnAppCtrl.$inject = ['$scope', '$window', 'translateService'];
  function fnAppCtrl ($scope, $window, translateService) {

    var isIE = !!navigator.userAgent.match(/MSIE/i);
    isIE && angular.element($window.document.body).addClass('ie');
    isSmartDevice( $window ) && angular.element($window.document.body).addClass('smart');

    // config
    $scope.app = {
      name: 'VoyageOne',
      version: 'Beta 0.0.2',
      copyRight: '&copy; 2015 Copyright',
      // for chart colors
      color: {
        primary: '#7266ba',
        info:    '#23b7e5',
        success: '#27c24c',
        warning: '#fad733',
        danger:  '#f05050',
        light:   '#e8eff0',
        dark:    '#3a3f51',
        black:   '#1c2b36',
        vo:'#e67e22'
      },
      settings: {
        themeID: 1,
        navbarHeaderColor: 'bg-black',
        navbarCollapseColor: 'bg-black',
        asideColor: 'bg-dark',
        headerFixed: true,
        asideFixed: false,
        asideFolded: false,
        asideDock: false,
        container: false
      }
    };

    $scope.$watch('app.settings', function(){
      if( $scope.app.settings.asideDock  &&  $scope.app.settings.asideFixed ){
        // aside dock and fixed must set the header fixed.
        $scope.app.settings.headerFixed = true;
      }
      // for box layout, add background image
      $scope.app.settings.container ? angular.element('html').addClass('bg') : angular.element('html').removeClass('bg');
    }, true);

    $scope.$watch('$viewContentLoaded', function(){
      $(window).load(function() {});

      $(window).scroll(function() {
        var scroll = $(window).scrollTop();
        if (scroll > 56) {
          $(".app-header").addClass("addShadow");
        }
        else {
          $(".app-header").removeClass("addShadow");
        }
      });

    });

    // set de default language
    translateService.setLanguage("");

    function isSmartDevice( $window )
    {
      // Adapted from http://www.detectmobilebrowsers.com
      var ua = $window['navigator']['userAgent'] || $window['navigator']['vendor'] || $window['opera'];
      // Checks for iOs, Android, Blackberry, Opera Mini, and Windows mobile devices
      return (/iPhone|iPod|iPad|Silk|Android|BlackBerry|Opera Mini|IEMobile/).test(ua);
    }
  }

  fnHeaderCtrl.$inject = ['$scope', '$window', '$location', 'appService'];
  function fnHeaderCtrl($scope, $window, $location, appService) {
    var vm = this;
    vm.menuList = {};
    vm.languageList = {};
    vm.userInfo = {};
    vm.searchValue = "";

    $scope.initialize = fnInitial;
    $scope.selectChannel = fnSelectChannel;
    $scope.selectMenu = fnSelectMenu;
    $scope.selectLanguage = fnSelectLanguage;
    $scope.search = fnSearch;
    $scope.logout = fnLogout;

    function fnInitial () {
      appService.getMenuHeaderInfo().then(function (data) {
        vm.menuList = data.menuList;
        vm.languageList = data.languageList;
        vm.userInfo = data.userInfo;
      });
    }

    /**
     * go to channel selected page.
     */
    function fnSelectChannel () {
      appService.clearChannel().then(function () {
        $window.location = routes.channel.templateUrl;
      });
    }

    /**
     * change to selected menu, and go to new system.
     * @param menu
       */
    function fnSelectMenu(menu) {
      appService.setMenu(menu.menuTitle).then(function (application) {
        $window.location = routes.views.url + application + routes.views.templateUrl;
      });
    }

    /**
     * search by input value.
     * @param value
       */
    function fnSearch () {
      $location.path(routes.search.complex.url + vm.searchValue);
    }

    /**
     * change to selected language.
     * @param language
       */
    function fnSelectLanguage (language) {
      appService.setLanguage(language).then(function (data) {
        vm.userInfo.language = data;
      })
    }

    /**
     * logout.
     */
    function fnLogout () {
      appService.logout().then(function () {
        $window.location = routes.login.templateUrl;
      })
    }
  }

  return angularAMD.bootstrap(mainApp);
});