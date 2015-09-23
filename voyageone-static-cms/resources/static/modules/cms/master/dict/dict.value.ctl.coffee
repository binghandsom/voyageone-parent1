###
  @Name: DictCustomController
  @Date: 2015-09-15 16:06:34

  @User: Jonas
  @Version: 0.0.1
###

define [
  'modules/cms/cms.module'
  'modules/cms/master/dict/dict.service'
], (cmsModule)->
  cmsModule.controller 'DictValueController', class
    @$inject: ['$scope', '$translate', 'DictService', 'masterProps', 'cmsValues', 'dictList', 'word', 'notify']
    constructor: (@$scope, @$translate, @DictService, @masterProps, @cmsValues, @dictList, @word, @notify) ->
      if !@word
        @selected.valueType = null
        return

      switch @selected.valueType = @word.type
        when @valueTypes.text then @selected.txt = @word.value
        when @valueTypes.cms then @selected.cmsValue = @word.value
        when @valueTypes.dict then @selected.dict = @word.value
        when @valueTypes.master then @selected.masterProp = @word.value

    # valueTypes 对应 java 的 WordType 枚举
    valueTypes:
      text : 'TEXT'
      cms : 'CMS'
      dict : 'DICT'
      master : 'MASTER'
      custom : 'CUSTOM'

    selected:
      txt: null
      cmsValue: null
      masterProp: null
      dict: null
      valueType: null

    save: ->
      # 先检查类型
      if !@selected.valueType
        @notify.warning 'CMS_MSG_DICT_VAL_NO_TYPE'
        return

      val = switch @selected.valueType
        when @valueTypes.text then @selected.txt
        when @valueTypes.cms then @selected.cmsValue
        when @valueTypes.dict then @selected.dict
        when @valueTypes.master then @selected.masterProp
        else
          null

      # 再检查获得的值
      if !val
        @notify.warning 'CMS_MSG_DICT_VAL_NO_VALUE'
        return

      @$scope.$close type: @selected.valueType, value: val
