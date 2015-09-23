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
  cmsModule.controller 'DictCustomController', class
    @$inject: ['$scope', 'vAlert', '$translate', 'DictService']
    constructor: ($scope, @alert, @$translate, @DictService) ->
      @openValue = (word, callback) -> $scope.$parent.vm.openValue word, callback
      # 页面初始化时加载必要信息
      @DictService.customs().then (res) => @customs = res.data
      # 接收新增信号
      $scope.$on 'custom.add', @onAddCustom
      # 接收编辑信号
      $scope.$on 'custom.edit', @onEditCustom
      # 定义信号发回
      @emit = (name, msg) -> $scope.$emit name, msg

    onAddCustom: =>
      @editing = false
      @customValue =
        type: 'CUSTOM'
        value:
          moduleName: ''
          userParam: {}
      @customBody = @customValue.value

    onEditCustom: (event, editingCustom) =>
      @editing = true
      @customValue = editingCustom
      if !@customValue or !@customValue.value
        @alert 'CMS_MSG_DICT_UN_VALID_CUS'
        @cancel()
        return
      @customBody = @customValue.value
      @custom = _.find @customs, (obj) => obj.word_name is @customBody.moduleName

    editing: false
    # 选中的自定义项的结构声明
    custom: null
    # 完整的 custom 值
    #   type: 'CUSTOM'
    #   value: customBody
    customValue: null
    # 当前创建或编辑的，自定义项的主体
    #   moduleName: String
    #   userParam: {}
    customBody: null

    cancel: -> @emit 'custom.cancel'

    setParam: (param) ->
      @openValue null, (val) =>
        @customBody.userParam[param.param_name] =
          ruleWordList: [val]

    saveCustom: ->
      if !@custom
        @alert 'CMS_MSG_DICT_NO_CUS'
        return
      unValidParamName = @checkParam()
      if unValidParamName
        @alert id: 'CMS_MSG_DICT_CUS_NO_VALUE', values: unValidParamName: unValidParamName
        return
      @customBody.moduleName = @custom.word_name
      @emit ('custom.save'), editing: @editing, custom: @customValue

    checkParam: ->
      params = @customBody.userParam
      for p in @custom.params
        val = params[p.param_name]
        if !val or !val.ruleWordList or !val.ruleWordList.length
          return p.param_name
      return null
