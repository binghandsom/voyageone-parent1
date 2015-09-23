###
  @Name: SetMatchValueService
  @Description: 主数据属性匹配第三方品牌数据，属性值匹配设定弹窗的数据交换
  @Date: 2015-09-06 17:23:47

  @User: Jonas
  @Version: 0.0.1
###

define [
  'modules/cms/cms.module'
], (cmsModule) ->

  cmsModule.service 'SetMatchValueService', class
    @$inject: ['$q', 'cmsAction', 'ajaxService']
    constructor: (@$q, cmsAction, ajaxService)->
      @feedValuesCache = {}
      @actions = cmsAction.match.props
      @post = (url, param) => ajaxService.ajaxPost param, @actions.root + url

    getFeedValues: (feedConfig) ->
      values = @feedValuesCache[feedConfig.cfg_val1]
      if values and !values.length
        return @$q (goon) -> goon values
      @post @actions.getFeedValues, feedConfig
      .then (res) => @feedValuesCache[feedConfig.cfg_val1] = res.data

    getPropOptions: (prop_id) -> @post @actions.getPropOptions, prop_id: prop_id

    addMapping: (mapping) -> @post @actions.addMapping, mapping

    setMapping: (mapping) -> @post @actions.setMapping, mapping

    delMapping: (mapping) -> @post @actions.delMapping, mapping

    getMappings: (prop_id) -> @post @actions.getMappings, prop_id: prop_id

    getDefValue: (prop) -> @post @actions.getDefault, prop