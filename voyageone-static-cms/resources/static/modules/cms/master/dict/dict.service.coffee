###
  @Name: DictService
  @Date: 2015-09-15 11:48:52

  @User: Jonas
  @Version: 0.0.1
###

define ['modules/cms/cms.module'], (cmsModule)->
  cmsModule.service 'DictService', class
    @$inject: ['cmsAction', 'ajaxService']
    constructor: (cmsAction, ajaxService)->
      @actions = cmsAction.dict.manage
      @post = (url, param) => ajaxService.ajaxPost param, @actions.ROOT + url

    dtGetDict: (data) -> @post @actions.DT_GET_DICT, data

    getConst: -> @post @actions.GET_CONST

    getCustoms: -> @post @actions.GET_CUSTOMS

    getDictList: -> @post @actions.GET_DICT_LIST

    addDict: (dict) -> @post @actions.ADD_DICT, dict

    setDict: (dict) -> @post @actions.SET_DICT, dict

    delDict: (dict) -> @post @actions.DEL_DICT, dict

    customs: () -> @post @actions.GET_CUSTOMS
