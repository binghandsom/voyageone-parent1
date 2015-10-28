/*
  @Name: MatchPropsService
  @Description: 主数据属性匹配第三方品牌数据，数据交换服务
  @Date: 2015-09-06 15:13:00

  @User: Jonas
  @Version: 0.0.1
 */
define(['modules/cms/cms.module'], function(cmsModule) {
  return cmsModule.service('MatchPropsService', (function() {
    _Class.$inject = ['cmsAction', 'ajaxService'];

    function _Class(cmsAction, ajaxService) {
      this.actions = cmsAction.match.props;
      this.post = (function(_this) {
        return function(url, param) {
          return ajaxService.ajaxPost(param, _this.actions.root + url);
        };
      })(this);
    }

    _Class.prototype.getPath = function(categoryId) {
      if (typeof categoryId === 'string') {
        categoryId = parseInt(categoryId);
      }
      return this.post(this.actions.getPath, {
        categoryId: categoryId
      });
    };

    _Class.prototype.getProps = function(dtParam) {
      return this.post(this.actions.getProps, dtParam);
    };

    _Class.prototype.setIgnore = function(prop) {
      if (prop && !prop.length) {
        prop = [ prop ];
      }
      return this.post(this.actions.setIgnore, prop);
    };

    _Class.prototype.getConst = function() {
      return this.post(this.actions.getConst);
    };

    return _Class;
  })());
});
