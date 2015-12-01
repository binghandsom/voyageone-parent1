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
    'voyageone-angular-com': 'components/dist/voyageone.angular.com.min',
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

  fnAppService.$inject = ['$q', 'ajaxService', 'actions'];
  function fnAppService($q, ajaxService, actions) {

    this.getSystemInfo = fnGetSystemInfo;
    this.search = fnSearch;

    /**
     * get the system info.
     * includes: systemMenu, languages,
     * @returns {*}
       */
    function fnGetSystemInfo () {
      var defer = $q.defer ();
      ajaxService.post(actions.app.common.getSystemInfo)
          .then(function (response) {
            defer.resolve (response.data);
          });
      return defer.promise;
    }

    /**
     * search by input value.
     * @returns {*}
       */
    function fnSearch () {
      var defer = $q.defer ();

      ajaxService.post(actions.app.getSystemMenus)
          .then(function (response) {
            defer.resolve (response.data);
          });
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

  fnHeaderCtrl.$inject = ['$scope', '$window', 'appService', 'cookieService'];
  function fnHeaderCtrl($scope, $window, appService, cookieService) {
    var vm = this;
    vm.menuList = {};
    vm.languageList = {};
    vm.currentSystem = "Sears";//cookieService.channel();
    vm.currentMenu = "CMS";//cookieService.menu();
    vm.currentLanguage = "中文";//cookieService.language();
    vm.currentUserName = "Edward.lin";//cookieService.name();

    $scope.initialize = fnInitial;
    $scope.selectMenu = fnSelectMenu;
    $scope.search = fnSearch;
    $scope.selectLanguage = fnSelectlanguage;
    $scope.logout = fnLogout;

    function fnInitial () {

      appService.getSystemInfo().then(function (data) {
        vm.menuList = data.menus;
        vm.languageList = data.languages;
      });
    }

    /**
     * change selected menu.
     * @param menu
       */
    function fnSelectMenu(menu) {
      cookieService.menu (menu.menuName);
      $window.location = menu.menuUrl;
    }

    /**
     * search by input value.
     * @param value
       */
    function fnSearch (value) {
      appService.search (value);
    }

    /**
     * change selected language.
     * @param language
       */
    function fnSelectlanguage (language) {
      cookieService.language(language.name);
    }

      /**
       * logout.
       */
    function fnLogout () {
      // todo
    }
  }

  return angularAMD.bootstrap(mainApp);
});