###
  @Name: DictItemController
  @Date: 2015-09-15 13:48:01

  @User: Jonas
  @Version: 0.0.1
###

define [
  'modules/cms/cms.module'
  'modules/cms/master/dict/dict.service'
  'modules/cms/master/dict/dict.custom.ctl'
  'modules/cms/master/dict/dict.value.ctl'
], (cmsModule)->
  cmsModule.controller 'DictItemController', class
    @$inject: ['$scope', '$translate', 'DictService', '$location', '$modal', 'vAlert', 'notify']
    constructor: ($scope, @$translate, @DictService, @$location, @$modal, @alert, @notify) ->
      # 定义信号收发
      @broadcast = (name, msg) -> $scope.$broadcast name, msg
      $scope.$on 'custom.save', @saveCustom
      $scope.$on 'custom.cancel', => @showCustom = false

      dict = sessionStorage.dict
      # 先尝试转换
      if dict
        dict = angular.fromJson dict
      # 如果不存在，则不会转换，所以如下进行判断
      if !dict or !dict.item or !dict.item.id
        # 如果当前没有传递内容，则默认认为是创建行为
        # 将新的空对象放置到各个变量中
        sessionStorage.dict = angular.toJson(dict =
          item: {isUrl: false, type: 'DICT'})
        @isEdit = false
      else
        @isEdit = true
        # 如果有，则为编辑行为
        dict = angular.fromJson dict
      # 如果解析出来的内容，不包含 item，则自己创建。防止报错。
      @item = dict.item or dict.item = {}
      # 转换保存当前字典的所有值
      if @item.value then @value = angular.fromJson @item.value
      # 并尝试将实际值保存的数组取出
      if !@value.expression then @value.expression = {}
      if !@value.expression.ruleWordList then @value.expression.ruleWordList = []
      @wordList = @value.expression.ruleWordList

      # 加载值窗体需要的常量
      @DictService.getConst().then (res) => @constData = res.data
      @DictService.getDictList().then (res) =>
        # 找到自己
        selfIndex = _.findIndex res.data, (i) => i.id is @item.id
        # 删除自己
        res.data.splice(selfIndex, 1)
        # 放回
        @dictList = res.data

    # 标记当前画面行为
    isEdit: false
    item: null
    # value 的定义
    #   value: string,
    #   isUrl: bool,
    #   type: 'DICT'
    #   expression: json
    value: {}
    wordList: []
    showCustom: false
    constData: null
    dictList: null

    cancel: ->
      # 清空当前选择
      sessionStorage.dict = null
      # 跳回
      @$location.path '/cms/dict'

    addCustom: ->
      # 发送新增信号
      @broadcast 'custom.add'
      # 切换画面
      @showCustom = true

    addValue: ->
      @openValue null, (res) =>
        # 将模态窗确定后返回的值对象，放到当前对象的值集合中
        @wordList.push res

    delValue: (index) ->
      @wordList.splice index, 1

    editValue: (word) ->
      if word.type is 'CUSTOM'
        @broadcast 'custom.edit', word
        @showCustom = true
        return
      @openValue word, (res) ->
        # 将返回的新值，赋值回去
        word.type = res.type
        word.value = res.value

    openValue: (word, callback) ->
      @$modal.open
        templateUrl: 'modules/cms/master/dict/dict.value.tpl.html'
        controller: 'DictValueController'
        controllerAs: 'vm'
        resolve:
          masterProps: => @constData.masterProps
          cmsValues: => @constData.cmsValues
          dictList: => @dictList
          word: -> word
      .result.then callback

    save: ->
      # 简单检查必填
      if !@value.value
        @notify.warning 'CMS_MSG_DICT_NO_NAME'
        return
      if !@wordList.length
        @notify.warning 'CMS_MSG_DICT_NO_VALUE'
        return

      # 对象的 value 是 json 字符串，所以保存前，将编辑好的值都转回 json 字符串保存
      @item.value = angular.toJson @value
      # 另外字典的名称是既保存在 item 中，同时也存在于 value 的 json 中的。所以此处需要同步保存名称
      @item.name = @value.value
      # 当前的 js 对象应该是完整映射 java 对象的
      # 所以保存是直接将对象返回给 java
      @DictService[if !@isEdit then 'addDict' else 'setDict'](@item)
      # 如果成功了，则在 alert 之后，跳回 list
      .then (res) => if res then @alert('CMS_TXT_MSG_SAVE_SUCCESS').result.then => @cancel()

    saveCustom: (event, msg) =>
      if !msg.editing
        @wordList.push msg.custom
      @showCustom = false
