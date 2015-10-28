/*
  @Name: MatchPropsController
  @Description: 主数据属性匹配第三方品牌数据，画面控制器
  @Date: 2015-09-06 14:24:50

  @User: Jonas
  @Version: 0.0.1
 */

(function() {
  var bind = function(fn, me) {
    return function() {
      return fn.apply(me, arguments);
    };
  };

  define([
    'modules/cms/cms.module',
    'modules/cms/master/matchProps/matchProps.service',
    'modules/cms/popup/setMatchValue/popSetMatchValue.ctl',
    'css!modules/cms/master/matchProps/matchProps.css'
  ], function(cmsModule) {
    var KEYS, template;
    KEYS = {
      NAME: 'CMS_TXT_PROMOTION_NAME',
      TYPE: 'CMS_TXT_TH_TYPE',
      VALUE: 'CMS_TXT_TH_VALUE',
      REQUIRED: 'CMS_TXT_TH_REQUIRED',
      IGNORE: 'CMS_TXT_TH_IGNORE',
      OPERATION: 'CMS_TXT_ACTIONS',
      YES: 'CORE_BUTTON_OK',
      NO: 'CORE_BUTTON_CANCEL',
      CHANGE: 'CMS_MSG_CHANGE_IGNORE',
      SUCCESS: 'CMS_MSG_CHANGE_SUCCESS'
    };
    template = {
      chkIgnore: "<label class=\"checkbox-inline c-checkbox needsclick\">" + "<input type=\"checkbox\" ng-model=\"$row.is_ignore\" ng-change=\"vm.changeIgnore($row)\">" + "<span class=\"fa fa-check\"></span></label>",
      btnSetMatch: "<button href=\"index.html#/ims/attribute\" ng-class=\"{'btn-primary-s': $row.is_ignore}\" class=\"btn btn-primary btn-sm fa fa-list-alt\" ng-click=\"vm.matchValue($row)\"> 值匹配</button>"
    };
    return angular.module('cmsModule.controllers.matchProps', ['cmsModule']).controller('MatchPropsController', (function() {
      _Class.$inject = ['$scope', '$compile', '$routeParams', 'vAlert', 'MatchPropsService', 'DTOptionsBuilder', 'DTColumnBuilder', '$modal', 'cmsPopupPages', 'vConfirm', 'notify', '$translate'];

      function _Class($scope, $compile, $routeParams, alert, matchPropsService, DTOptionsBuilder, DTColumnBuilder, $modal, cmsPopupPages, confirm, notify, $translate) {
        this.$scope = $scope;
        this.$compile = $compile;
        this.alert = alert;
        this.matchPropsService = matchPropsService;
        this.$modal = $modal;
        this.cmsPopupPages = cmsPopupPages;
        this.confirm = confirm;
        this.notify = notify;
        this.$translate = $translate;
        this.dtGetProps = bind(this.dtGetProps, this);
        this.category.id = $routeParams.categoryId;
        this.matchPropsService.getPath(this.category.id).then((function(_this) {
          return function(res) {
            return _this.category.path = res.data;
          };
        })(this));
        this.dtProps = {
          options: DTOptionsBuilder.newOptions().withOption('processing', true).withOption('serverSide', true).withOption('ordering', false).withOption('searching', false).withOption('ajax', this.dtGetProps).withOption('createdRow', (function(_this) {
            return function(tr, row) {
              var rowScope;
              rowScope = _this.$scope.$new();
              rowScope.$row = row;
              return _this.$compile(angular.element(tr).attr('ng-class', '{"prop-require": $row.is_required, "prop-ignore": $row.is_ignore}'))(rowScope);
            };
          })(this)).withDataProp('data').withPaginationType('full_numbers'),
          columns: [
            DTColumnBuilder.newColumn('prop_name', this.$translate(KEYS.NAME)).withClass('col-sm-5'), DTColumnBuilder.newColumn('prop_type', this.$translate(KEYS.TYPE)).withClass('col-sm-1').renderWith(function(v, t, r) {
              return r.typeName;
            }), DTColumnBuilder.newColumn('val_count', this.$translate(KEYS.VALUE)).withClass('col-sm-2').renderWith(function(val) {
              if (val > 0) {
                return '已设置';
              } else {
                return '无';
              }
            }), DTColumnBuilder.newColumn('is_required', this.$translate(KEYS.REQUIRED)).withClass('col-sm-1').renderWith((function(_this) {
              return function(val) {
                return _this.$translate.instant(val === 0 ? KEYS.NO : KEYS.YES);
              };
            })(this)), DTColumnBuilder.newColumn('is_ignore', this.$translate(KEYS.IGNORE)).withClass('col-sm-1').renderWith(function() {
              return template.chkIgnore;
            }), DTColumnBuilder.newColumn('', this.$translate(KEYS.OPERATION)).withClass('col-sm-2 col-md-1').renderWith(function() {
              return template.btnSetMatch;
            })
          ],
          instance: null
        };
        this.$scope.$watch('vm.filter.ignored', ((function(_this) {
          return function() {
            if (_this.dtProps.instance) {
              return _this.dtProps.instance.reloadData();
            }
          };
        })(this)));
        this.$scope.$watch('vm.filter.required', ((function(_this) {
          return function() {
            if (_this.dtProps.instance) {
              return _this.dtProps.instance.reloadData();
            }
          };
        })(this)));
      }

      _Class.prototype.options = [{
        label: 'ALL',
        value: ''
      }, {
        label: 'YES',
        value: '1'
      }, {
        label: 'NO',
        value: '0'
      }];

      _Class.prototype.category = {
        id: null,
        path: null
      };

      _Class.prototype.props = [];

      _Class.prototype.feedProps = [];

      _Class.prototype.cmsProps = [];

      _Class.prototype.operations = [];

      _Class.prototype.constGot = false;

      _Class.prototype.filter = {
        ignored: '',
        required: '',
        propName: ''
      };

      _Class.prototype.getConst = function() {
        return this.matchPropsService.getConst().then((function(_this) {
          return function(res) {
            _this.constGot = true;
            _this.cmsProps = res.data.cmsProps;
            _this.feedProps = res.data.feedProps;
            return _this.operations = res.data.operations;
          };
        })(this));
      };

      _Class.prototype.dtGetProps = function(data, draw) {
        if (!this.category.id) {
          draw({
            data: [],
            recordsTotal: 0,
            recordsFiltered: 0
          });
          return;
        }
        data.param = this.category.id;
        data.columns[3].search.value = this.filter.required;
        data.columns[4].search.value = this.filter.ignored;
        data.search.value = this.filter.propName;
        return this.matchPropsService.getProps(data).then((function(_this) {
          return function(res) {
            draw(res.data);
            return _this.props = res.data.data;
          };
        })(this));
      };

      _Class.prototype.matchValue = function(prop) {
        if (!this.constGot) {
          this.getConst().then((function(_this) {
            return function() {
              return _this.matchValue(prop);
            };
          })(this));
          return;
        }
        return this.$modal.open({
          templateUrl: this.cmsPopupPages.popSetMatchValue.page,
          controller: this.cmsPopupPages.popSetMatchValue.controller,
          controllerAs: 'vm',
          backdrop: 'static',
          size: 'lg',
          resolve: {
            category: (function(_this) {
              return function() {
                return _this.category;
              };
            })(this),
            currProp: function() {
              return prop;
            },
            cmsProps: (function(_this) {
              return function() {
                return _this.cmsProps;
              };
            })(this),
            operations: (function(_this) {
              return function() {
                return _this.operations;
              };
            })(this),
            feedProps: (function(_this) {
              return function() {
                return _this.feedProps;
              };
            })(this)
          }
        });
      };

      _Class.prototype.changeIgnore = function(prop) {
        this.matchPropsService.setIgnore(prop || this.props)
        .then((function(_this) {
          return function() {
            _.forEach(_this.props, function (prop) {
              prop.is_ignore = true;
            });
            return _this.notify.success(KEYS.SUCCESS);
          };
        })(this));
      };

      return _Class;

    })());
  });

}).call(this);
