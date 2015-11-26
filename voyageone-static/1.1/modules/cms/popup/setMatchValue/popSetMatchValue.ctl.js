/*
  @Name: SetMatchValueController
  @Description: 主数据属性匹配第三方品牌数据，属性值匹配设定弹窗的控制器
  @Date: 2015-09-06 17:21:33

  @User: Jonas
  @Version: 0.1.
 */
var bind = function(fn, me) {
  return function() {
    return fn.apply(me, arguments);
  };
};

define(['modules/cms/cms.module', 'modules/cms/popup/setMatchValue/popSetMatchValue.service', 'css!modules/cms/popup/setMatchValue/popSetMatchValue.css'], function(cmsModule) {
  return cmsModule.controller('SetMatchValueController', (function() {
    _Class.$inject = ['category', 'currProp', 'cmsProps', 'operations', 'feedProps', 'SetMatchValueService', 'notify'];

    function _Class(category, currProp, cmsProps, operations, feedProps, SetMatchValueService, notify) {
      this.category = category;
      this.currProp = currProp;
      this.cmsProps = cmsProps;
      this.operations = operations;
      this.feedProps = feedProps;
      this.SetMatchValueService = SetMatchValueService;
      this.notify = notify;
      this.findFeedProp = bind(this.findFeedProp, this);
      this.SetMatchValueService.getMappings(this.currProp.prop_id).then((function(_this) {
        return function(res) {
          return _this.mappings = res.data;
        };
      })(this));
      this.SetMatchValueService.getPropOptions(this.currProp.prop_id).then((function(_this) {
        return function(res) {
          return _this.options = res.data;
        };
      })(this));
      this.SetMatchValueService.getDefValue(this.currProp).then((function(_this) {
        return function(res) {
          return _this.selected["default"] = res.data;
        };
      })(this));
    }

    _Class.prototype.editing = false;

    _Class.prototype.fromAdd = false;

    _Class.prototype.editIndex = 0;

    _Class.prototype.mappings = [];

    _Class.prototype.options = [];

    _Class.prototype.feedValues = [];

    _Class.prototype.masterPropType = {
      input: 1,
      singleCheck: 2,
      multiCheck: 3,
      label: 4,
      complex: 5,
      multiComplex: 6,
      multiInput: 7
    };

    _Class.prototype.feedPropMappingType = {
      feed: 1,
      "default": 2,
      options: 3,
      cms: 4,
      value: 5
    };

    _Class.prototype.selected = {
      feed: null,
      "default": null,
      options: null,
      cms: null,
      value: null
    };

    _Class.prototype.condition = {
      property: null,
      operation: null,
      value: null
    };

    _Class.prototype.curr = null;

    _Class.prototype.conditionChecked = false;

    _Class.prototype.findFeedProp = function(name, val2) {
      var i, j, len, len1, o, ref, ref1;
      if (name) {
        ref = this.feedProps;
        for (i = 0, len = ref.length; i < len; i++) {
          o = ref[i];
          if (o.cfg_name === name) {
            return o;
          }
        }
      }
      if (val2) {
        ref1 = this.feedProps;
        for (j = 0, len1 = ref1.length; j < len1; j++) {
          o = ref1[j];
          if (o.cfg_val2 === val2) {
            return o;
          }
        }
      }
      return null;
    };

    _Class.prototype.formatCondition = function(condition) {
      var desc, operation, property;
      if (!condition) {
        return "";
      }
      condition = angular.fromJson(condition);
      operation = ((function(_this) {
        return function() {
          var i, len, o, ref;
          ref = _this.operations;
          for (i = 0, len = ref.length; i < len; i++) {
            o = ref[i];
            if (o.name === condition.operation) {
              return o;
            }
          }
        };
      })(this))();
      property = this.findFeedProp(condition.property);
      desc = "判断属性 [ " + (property ? property.cfg_val2 : condition.property) + " ] " + operation.desc;
      if (!operation.single) {
        desc += " “ " + condition.value + " ”";
      }
      return desc += "时";
    };

    _Class.prototype.formatValue = function(mapping) {
      var property;
      if (mapping.type !== this.feedPropMappingType.feed) {
        return mapping.value;
      }
      property = this.findFeedProp(mapping.value);
      if (property) {
        return property.cfg_val2;
      } else {
        return mapping.value;
      }
    };

    _Class.prototype.add = function() {
      this.fromAdd = true;
      this.selected = {
        feed: null,
        "default": this.selected["default"],
        options: null,
        cms: null,
        value: null
      };
      this.curr = {
        prop_id: this.currProp.prop_id,
        main_category_id: this.currProp.category_id
      };
      this.conditionChecked = false;
      this.condition = {};
      return this.editing = true;
    };

    _Class.prototype.save = function() {
      if (!this.curr.type) {
        this.notify.warning('CMS_MSG_NO_MATCH_TYPE');
        return;
      }
      this.curr.value = (function() {
        switch (this.curr.type) {
          case this.feedPropMappingType.feed:
            return this.selected.feed;
          case this.feedPropMappingType.cms:
            return this.selected.cms;
          case this.feedPropMappingType.options:
            if (this.isSingle()) {
              return this.selected.options;
            } else {
              return this.selected.options.join(',');
            }
            break;
          case this.feedPropMappingType.value:
            return this.selected.value;
          default:
            return null;
        }
      }).call(this);
      if (!this.curr.value) {
        this.notify.warning('CMS_MSG_NO_MATCH_VALUE');
        return;
      }
      if (this.conditionChecked) {
        if (!this.condition.property) {
          this.notify.warning('CMS_MSG_NO_CONDITION_PROP');
          return;
        }
        if (!this.condition.operation) {
          this.notify.warning('CMS_MSG_NO_CONDITION_OP');
          return;
        } else if (!this.condition.operation.single && !this.condition.value) {
          this.notify.warning('CMS_MSG_NO_CONDITION_VALUE');
          return;
        }
        this.curr.conditions = angular.toJson({
          property: this.condition.property,
          operation: this.condition.operation.name,
          value: this.condition.value || ''
        });
      } else {
        this.curr.conditions = '';
      }
      if (this.fromAdd) {
        return this.SetMatchValueService.addMapping(this.curr).then((function(_this) {
          return function(res) {
            _this.mappings.push(res.data);
            return _this.editing = false;
          };
        })(this));
      } else {
        return this.SetMatchValueService.setMapping(this.curr).then((function(_this) {
          return function(res) {
            _this.mappings[_this.editIndex] = res.data;
            return _this.editing = false;
          };
        })(this));
      }
    };

    _Class.prototype.edit = function(index) {
      this.fromAdd = false;
      this.editIndex = index;
      this.curr = this.mappings[index];
      switch (this.curr.type) {
        case this.feedPropMappingType.feed:
          this.selected.feed = this.curr.value;
          break;
        case this.feedPropMappingType.cms:
          this.selected.cms = this.curr.value;
          break;
        case this.feedPropMappingType.options:
          this.selected.options = this.isSingle() ? this.curr.value : this.curr.value.split(',');
          break;
        case this.feedPropMappingType.value:
          this.selected.value = this.curr.value;
      }
      if (this.conditionChecked = !!this.curr.conditions) {
        // 格式化出条件
        var cond = angular.fromJson(this.curr.conditions);
        // 查找出条件具体的操作
        cond.operation =
          _.find(this.operations, (function(_this){
            return function(op) {
              return op.name === cond.operation
            };
          })(this));
        // 如果条件操作不是单值比较
        if (!cond.operation.single) {
          var item = _.find(this.feedProps, function(p) {
            return p.cfg_name == cond.property;
          });
          // 需要为下拉填充值
          this.getFeedValues(item);
        }

        this.condition = cond;
      } else {
        this.condition = {};
      }
      return this.editing = true;
    };

    _Class.prototype.cancel = function() {
      return this.editing = false;
    };

    _Class.prototype.getFeedValues = function(item) {
      this.feedValues = [];
      this.condition.value = null;
      return this.SetMatchValueService.getFeedValues(item).then((function(_this) {
        return function(values) {
          return _this.feedValues = values;
        };
      })(this));
    };

    _Class.prototype.del = function(index) {
      this.curr = this.mappings[index];
      return this.SetMatchValueService.delMapping(this.curr).then((function(_this) {
        return function() {
          return _this.mappings.splice(index, 1);
        };
      })(this));
    };

    _Class.prototype.isSingle = function() {
      return this.currProp.prop_type === this.masterPropType.singleCheck;
    };

    _Class.prototype.isCheckProp = function() {
      return this.currProp.prop_type === this.masterPropType.singleCheck || this.currProp.prop_type === this.masterPropType.multiCheck;
    };

    _Class.prototype.isTextProp = function() {
      return this.currProp.prop_type === this.masterPropType.input || this.currProp.prop_type === this.masterPropType.label;
    };

    return _Class;

  })());
});
