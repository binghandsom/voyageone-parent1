// Generated by CoffeeScript 1.9.3

/*
  @Name: DictCustomController
  @Date: 2015-09-15 16:06:34

  @User: Jonas
  @Version: 0.0.1
 */
define(['modules/cms/cms.module', 'modules/cms/master/dict/dict.service'], function(cmsModule) {
  return cmsModule.controller('DictValueController', (function() {
    _Class.$inject = ['$scope', '$translate', 'DictService', 'masterProps', 'cmsValues', 'dictList', 'word', 'notify'];

    function _Class($scope, $translate, DictService, masterProps, cmsValues, dictList, word, notify) {
      this.$scope = $scope;
      this.$translate = $translate;
      this.DictService = DictService;
      this.masterProps = masterProps;
      this.cmsValues = cmsValues;
      this.dictList = dictList;
      this.word = word;
      this.notify = notify;
      this.selected.text = "";
      if (!this.word) {
        this.selected.valueType = null;
        return;
      }
      switch (this.selected.valueType = this.word.type) {
        case this.valueTypes.text:
          this.selected.txt = this.word.value;
          break;
        case this.valueTypes.cms:
          this.selected.cmsValue = this.word.value;
          break;
        case this.valueTypes.dict:
          this.selected.dict = this.word.value;
          break;
        case this.valueTypes.master:
          this.selected.masterProp = this.word.value;
          break;
        case this.valueTypes.custom:
          this.selected.text = JSON.stringify(this.word.value);
          break;
        default :
          this.selected.text = JSON.stringify(this.word.value);
      }
    }

    _Class.prototype.valueTypes = {
      text: 'TEXT',
      cms: 'CMS',
      dict: 'DICT',
      master: 'MASTER',
      custom: 'CUSTOM'
    };

    _Class.prototype.selected = {
      txt: null,
      cmsValue: null,
      masterProp: null,
      dict: null,
      valueType: null
    };

    _Class.prototype.save = function() {
      var val;
      //if (!this.selected.valueType) {
      //  this.notify.warning('CMS_MSG_DICT_VAL_NO_TYPE');
      //  return;
      //}
      val = (function() {
        switch (this.selected.valueType) {
          case this.valueTypes.text:
            return this.selected.txt;
          case this.valueTypes.cms:
            return this.selected.cmsValue;
          case this.valueTypes.dict:
            return this.selected.dict;
          case this.valueTypes.master:
            return this.selected.masterProp;
          case this.valueTypes.custom:
            return  JSON.parse(this.selected.text);
          default:
            return  JSON.parse(this.selected.text);
        }
      }).call(this);
      if (!val) {
        this.notify.warning('CMS_MSG_DICT_VAL_NO_VALUE');
        return;
      }
      return this.$scope.$close({
        type: this.selected.valueType,
        value: val
      });
    };

    return _Class;

  })());
});
