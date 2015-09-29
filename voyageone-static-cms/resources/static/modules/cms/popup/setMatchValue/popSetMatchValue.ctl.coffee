###
  @Name: SetMatchValueController
  @Description: 主数据属性匹配第三方品牌数据，属性值匹配设定弹窗的控制器
  @Date: 2015-09-06 17:21:33

  @User: Jonas
  @Version: 0.0.1
###

define [
  'modules/cms/cms.module'
  'modules/cms/popup/setMatchValue/popSetMatchValue.service'
  'css!modules/cms/popup/setMatchValue/popSetMatchValue.css'
], (cmsModule) ->
  cmsModule.controller 'SetMatchValueController', class
    @$inject: ['category', 'currProp', 'cmsProps', 'operations', 'feedProps', 'SetMatchValueService', 'notify']
    constructor: (@category, @currProp, @cmsProps, @operations, @feedProps, @SetMatchValueService, @notify) ->
      @SetMatchValueService.getMappings @currProp.prop_id
      .then (res) => @mappings = res.data
      @SetMatchValueService.getPropOptions @currProp.prop_id
      .then (res) => @options = res.data
      @SetMatchValueService.getDefValue @currProp
      .then (res) =>
        @selected.default = res.data
    editing: false
    fromAdd: false
    editIndex: 0
    mappings: []
    options: []
    feedValues: []
    masterPropType: # 对应 Java 端的 MasterPropTypeEnum 枚举
      input: 1
      singleCheck: 2
      multiCheck: 3
      label: 4
      # 这以下的三个类型，在查询时即被排除，暂不支持
      complex: 5
      multiComplex: 6
      multiInput: 7
    feedPropMappingType: # 对应 Java 端的 FeedPropMappingType 枚举
      feed: 1
      default: 2
      options: 3
      cms: 4
      value: 5
    selected:
      feed: null
      default: null
      options: null
      cms: null
      value: null
    condition: # 当前条件
      property: null
      operation: null # 当前条件的判断操作，此处不是操作对应的 Value，而是 Object
      value: null
    curr: null # 当前编辑的 mapping
    conditionChecked: false # 画面上条件多选是否选中
    findFeedProp: (val1, val2) =>
      if (val1)
        return o for o in @feedProps when o.cfg_val1 is val1
      if (val2)
        return o for o in @feedProps when o.cfg_val2 is val2
      null
    formatCondition: (condition) ->
      if !condition then return ""
      condition = angular.fromJson condition
      operation = (=> return o for o in @operations when o.name is condition.operation)()
      property = @findFeedProp condition.property
      desc = "判断属性 [ #{property.cfg_val2} ] #{operation.desc}"
      if !operation.single then desc += " “ #{condition.value} ”"
      desc += "时"
    formatValue: (mapping) ->
      if (mapping.type != @feedPropMappingType.feed)
        return mapping.value
      property = @findFeedProp mapping.value
      property.cfg_val2
    add: ->
      # 标为增加
      @fromAdd = true
      # 重置值输入
      @selected =
        feed: null
        default: @selected.default
        options: null
        cms: null
        value: null
      # 创建新的编辑对象
      @curr =
        prop_id: @currProp.prop_id
        main_category_id: @currProp.category_id
      # 重置条件编辑
      @conditionChecked = false
      @condition = {}
      # 切换界面
      @editing = true
    save: ->
      if !@curr.type
        @notify.warning 'CMS_MSG_NO_MATCH_TYPE'
        return
      # 手动绑定
      @curr.value = switch @curr.type
        when @feedPropMappingType.feed then @selected.feed
        when @feedPropMappingType.cms then @selected.cms
        when @feedPropMappingType.options
          if @isSingle() then @selected.options else @selected.options.join(',')
        when @feedPropMappingType.value then @selected.value
        else
          null
      if !@curr.value
        @notify.warning 'CMS_MSG_NO_MATCH_VALUE'
        return
      # 手动处理条件
      if @conditionChecked
        if !@condition.property
          @notify.warning 'CMS_MSG_NO_CONDITION_PROP'
          return
        if !@condition.operation
          @notify.warning 'CMS_MSG_NO_CONDITION_OP'
          return
        else if !@condition.operation.single and !@condition.value
          @notify.warning 'CMS_MSG_NO_CONDITION_VALUE'
          return
        # 最终成功转化
        @curr.conditions = angular.toJson
          property: @condition.property
          operation: @condition.operation.name
          value: @condition.value || ''
      else
        @curr.conditions = ''
      if @fromAdd
        @SetMatchValueService.addMapping @curr
        .then (res) =>
          @mappings.push res.data
          @editing = false
      else
        @SetMatchValueService.setMapping @curr
        .then (res) =>
          @mappings.splice @editIndex, 1
          @mappings.push res.data
          @editing = false
    edit: (index) ->
      # 标为更改
      @fromAdd = false
      @editIndex = index
      @curr = @mappings[index]
      # 手动绑定
      switch @curr.type
        when @feedPropMappingType.feed then @selected.feed = @curr.value
        when @feedPropMappingType.cms then @selected.cms = @curr.value
        when @feedPropMappingType.options
          @selected.options = if @isSingle() then @curr.value else @curr.value.split(',')
        when @feedPropMappingType.value then @selected.value = @curr.value
      # 设定条件编辑界面
      if @conditionChecked = !!@curr.conditions
        @condition = angular.fromJson @curr.conditions
        # 因为 @condition 的 operation 时 Object 所以需要手动转换
        @condition.operation = (=> return o for o in @operations when o.name is @condition.operation)()
      else
        @condition = {}
      # 切换界面
      @editing = true
    cancel: ->
      # 切换界面
      @editing = false
    getFeedValues: (item) ->
      # 每次重选时，都要重置，否则无值可选时，ui-select 不会更改原先的选中值
      @feedValues = []
      @condition.value = null
      @SetMatchValueService.getFeedValues item
      .then (values) => @feedValues = values
    del: (index) ->
      @curr = @mappings[index]
      @SetMatchValueService.delMapping @curr
      .then => @mappings.splice index, 1
    isSingle: ->
      @currProp.prop_type is @masterPropType.singleCheck
    isCheckProp: ->
      @currProp.prop_type is @masterPropType.singleCheck or @currProp.prop_type is @masterPropType.multiCheck
    isTextProp: ->
      @currProp.prop_type is @masterPropType.input or @currProp.prop_type is @masterPropType.label
