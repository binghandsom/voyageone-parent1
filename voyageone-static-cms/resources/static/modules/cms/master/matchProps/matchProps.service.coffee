###
  @Name: MatchPropsService
  @Description: 主数据属性匹配第三方品牌数据，数据交换服务
  @Date: 2015-09-06 15:13:00

  @User: Jonas
  @Version: 0.0.1
###

define ['modules/cms/cms.module'], (cmsModule)->
  cmsModule.service 'MatchPropsService', class
    @$inject: ['cmsAction', 'ajaxService']
    constructor: (cmsAction, ajaxService)->
      @actions = cmsAction.match.props
      @post = (url, param) => ajaxService.ajaxPost param, @actions.root + url

    getPath: (categoryId) ->
      if typeof categoryId is 'string'
        categoryId = parseInt categoryId
      @post @actions.getPath, categoryId: categoryId

    getProps: (dtParam) -> @post @actions.getProps, dtParam

    setIgnore: (prop) -> @post @actions.setIgnore, prop

    getConst: () -> @post @actions.getConst
