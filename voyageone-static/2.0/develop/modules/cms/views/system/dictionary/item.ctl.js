/**
 * Created by linanbin on 15/12/7.
 */

define([
  'modules/cms/controller/popup.ctl'
], function () {

  function dictionaryItem($scope, $location, cRoutes, $routeParams, $translate, $dictionaryService, notify) {

    $scope.initialize = initialize;
    $scope.cancel = cancel;
    $scope.save = save;
    $scope.delDictItem = delDictItem;
    $scope.sortUp = sortUp;
    $scope.sortDown = sortDown;
    $scope.addNewDictionary = addNewDictionary;

    /**
     * 设置初始化数据
     */
    function initialize () {
      $scope.vm = {
        dictionary: {
          type: "DICT",
          value: "",
          expression: {
            ruleWordList: []
          },
          isUrl: false
        }
      };
    }

    /**
     * 清空画面上显示的数据,并返回到字典列表页面
     */
    function cancel () {
      $location.path(cRoutes.system_dict_list.hash);
    }

    /**
     * 检索
     */
    function save () {

      if ($scope.dictionaryForm.$valid
        && $scope.vm.dictionary.expression.ruleWordList.length > 0) {
        var data = {
          name: $scope.vm.dictionary.value,
          value: angular.toJson ($scope.vm.dictionary)
        };
        $dictionaryService.addDict(data)
            .then(function () {
              $location.path(cRoutes.system_dict_list.hash);
            })
      } else {
        notify.danger($translate.instant('TXT_COM_MSG_NO_DATA_WITH_SAVE'))
      }
    }

    /**
     * 删除dictionary数据
     * @param dictionaryInfo
     */
    function delDictItem (index) {
      $scope.vm.dictionary.expression.ruleWordList.splice(index, 1);
      notify.success ($translate.instant('TXT_COM_DELETE_SUCCESS'));
    }

    /**
     * 升序
     * @param index
     */
    function sortUp (index) {
      var temp =  angular.copy ($scope.vm.dictionary.expression.ruleWordList[index]);
      $scope.vm.dictionary.expression.ruleWordList.splice(index, 1);
      $scope.vm.dictionary.expression.ruleWordList.splice(index - 1, 0,temp);

    }

    /**
     * 降序
     * @param index
     */
    function sortDown (index) {
      var temp =  angular.copy ($scope.vm.dictionary.expression.ruleWordList[index]);
      $scope.vm.dictionary.expression.ruleWordList.splice(index, 1);
      $scope.vm.dictionary.expression.ruleWordList.splice(index + 1 , 0,temp);
    }

    /**
     * 添加一个新的字典项
     * @param info
     */
    function addNewDictionary (info) {
      if (!_.isUndefined(info))
        $scope.vm.dictionary.expression.ruleWordList.push(info);
    }
  }

  dictionaryItem.$inject = ['$scope', '$location', 'cRoutes', '$routeParams', '$translate', '$dictionaryService', 'notify'];
  return dictionaryItem;
});
/*
 var bind = function(fn, me){
 return function(){
 return fn.apply(me, arguments);
 };
 };

 define([],

 function(cmsModule) {

 return cmsModule.controller('DictCustomController', (function() {

 _Class.$inject = ['$scope', 'alert', '$translate', 'DictService'];

 function _Class($scope, alert,$translate, DictService) {
 this.alert = alert;
 this.$translate = $translate;
 this.DictService = DictService;
 this.onEditCustom = bind(this.onEditCustom, this);
 this.onAddCustom = bind(this.onAddCustom, this);
 this.openValue = function(word, callback) {
 return $scope.$parent.vm.openValue(word, callback);
 };
 this.DictService.customs().then((function(_this) {
 return function(res) {
 return _this.customs = res.data;
 };
 })(this));
 $scope.$on('custom.add', this.onAddCustom);
 $scope.$on('custom.edit', this.onEditCustom);
 this.emit = function(name, msg) {
 return $scope.$emit(name, msg);
 };
 }

 _Class.prototype.onAddCustom = function() {
 this.editing = false;
 this.customValue = {
 type: 'CUSTOM',
 value: {
 moduleName: '',
 userParam: {}
 }
 };
 return this.customBody = this.customValue.value;
 };

 _Class.prototype.onEditCustom = function(event, editingCustom) {
 this.editing = true;
 this.customValue = editingCustom;
 if (!this.customValue || !this.customValue.value) {
 this.alert('TXT_MSG_DICT_UN_VALID_CUS');
 this.cancel();
 return;
 }
 this.customBody = this.customValue.value;
 return this.custom = _.find(this.customs, (function(_this) {
 return function(obj) {
 return obj.word_name === _this.customBody.moduleName;
 };
 })(this));
 };

 _Class.prototype.editing = false;

 _Class.prototype.custom = null;

 _Class.prototype.customValue = null;

 _Class.prototype.customBody = null;

 _Class.prototype.cancel = function() {
 return this.emit('custom.cancel');
 };

 _Class.prototype.setParam = function(param) {
 return this.openValue(null, (function(_this) {
 return function(val) {
 return _this.customBody.userParam[param.param_name] = {
 ruleWordList: [val]
 };
 };
 })(this));
 };

 _Class.prototype.saveCustom = function() {
 var unValidParamName;
 if (!this.custom) {
 this.alert('TXT_MSG_DICT_NO_CUS');
 return;
 }
 unValidParamName = this.checkParam();
 if (unValidParamName) {
 this.alert({
 id: 'TXT_MSG_DICT_CUS_NO_VALUE',
 values: {
 unValidParamName: unValidParamName
 }
 });
 return;
 }
 this.customBody.moduleName = this.custom.word_name;
 return this.emit('custom.save', {
 editing: this.editing,
 custom: this.customValue
 });
 };

 _Class.prototype.checkParam = function() {
 var i, len, p, params, ref, val;
 params = this.customBody.userParam;
 ref = this.custom.params;
 for (i = 0, len = ref.length; i < len; i++) {
 p = ref[i];
 val = params[p.param_name];
 if (!val || !val.ruleWordList || !val.ruleWordList.length) {
 return p.param_name;
 }
 }
 return null;
 };

 return _Class;

 })());
 });
 */