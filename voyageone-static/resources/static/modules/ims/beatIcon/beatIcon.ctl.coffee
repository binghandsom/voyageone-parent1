###
  @Name: beat-icon controller
  @Date: 2015/06/29

  @User: Jonas
  @Version: 0.2.1
###

define ['modules/ims/ims.module', 'modules/ims/beatIcon/beatIcon.service'
        'filestyle'], (ims)->
  deepClone = (obj) -> JSON.parse JSON.stringify(obj)

  dateFormat = (date, type, filter) ->
    if typeof date is 'string'
      iMilliseconds = Date.parse date
      if not isNaN iMilliseconds
        date = new Date(iMilliseconds)
    type = switch type
      when 0 then 'yyyy-MM-dd HH:mm:00'
      when 1 then 'yyyy-MM-dd'
      when 2 then 'HH:mm:00'
      else
        type
    filter(date, type)

  class CreatorController
    @$inject: ['$modalInstance', '$filter', 'carts']
    constructor: ($modalInstance, $filter, carts) ->
      @instance = $modalInstance
      @date = $filter 'date'
      @carts = carts
      @beat = deepClone @beatDef
    ok: ->
      part1 = dateFormat @beat.end, 1, @date
      part2 = dateFormat @time.end, 2, @date
      @beat.end = part1 + ' ' + part2
      @instance.close @beat
    cancel: ->
      @instance.dismiss 'cancel'
    carts: []
    beat: {}
    beatDef:
      description: ''
      channel_id: ''
      cart_id: ''
      end: new Date()
      template_url: ''
    time:
      end: new Date()

  class SetPriceController
    @$inject: ['$modalInstance', 'lastPrice']
    constructor: (@instance, lastPrice) ->
      @price = lastPrice
    ok: ->
      if /^\d+\.?\d*$/.test @price
        @instance.close parseFloat @price
      else
        @message = '不是数字'
    cancel: ->
      @instance.dismiss 'cancel'
    price: 0
    message: ''

  class BeatController
    @$inject: ['ImsBeatService', 'userService', '$filter', 'FileUploader', 'imsAction', 'DTOptionsBuilder'
               'DTColumnBuilder', 'vConfirm', 'vAlert', '$modal', '$compile', '$scope', 'notify']
    constructor: (imsBeatService, user, filter, FileUploader, imsAction, DTOptionsBuilder,
                  DTColumnBuilder, vConfirm, vAlert, $modal, @$compile, @$scope, @notify) ->
      @confirm = vConfirm
      @alert = vAlert
      @beatService = imsBeatService

      @$modal = $modal

      url = imsAction.beatIcon
      root = '/VoyageOne' + url.root

      @downItemsAction = root + url.downItems
      @downErrAction = root + url.downErr
      @itemUploader = new FileUploader(url: root + url.addItems)
      @priceUploader = new FileUploader(url: root + url.saveItems)

      @dateFilter = filter('date')

      @dtBeats =
        options: (DTOptionsBuilder.newOptions()
        .withOption('processing', true)
        .withOption('serverSide', true)
        .withOption('ajax', @onDtGetBeats)
        .withOption('createdRow', (row) => @$compile(angular.element(row).contents())(@$scope))
        .withDataProp('data')
        .withPaginationType 'full_numbers')
        columns: [
          DTColumnBuilder.newColumn('cart_id', 'Cart').renderWith @formatCart
          DTColumnBuilder.newColumn('description', 'Description')
          DTColumnBuilder.newColumn('end', 'End')
          DTColumnBuilder.newColumn('template_url', 'Template')
          DTColumnBuilder.newColumn('modifier', 'Modifier')
          DTColumnBuilder.newColumn('modified', 'Modified')
          DTColumnBuilder.newColumn('', '').renderWith (val, type, row, cell) ->
            "<button class=\"btn btn-primary btn-xs\" ng-click=\"ctrl.openBeat(#{cell.row})\">查看</button>"

        ]
        instance: null

      @dtItems =
        options: (DTOptionsBuilder.newOptions()
        .withOption('processing', true)
        .withOption('serverSide', true)
        .withOption('ajax', @onDtGetItems)
        .withOption('createdRow', (row) => @$compile(angular.element(row).contents())(@$scope))
        .withDataProp('data')
        .withPaginationType 'full_numbers')
        columns: [
          DTColumnBuilder.newColumn('code', 'Code')
          DTColumnBuilder.newColumn('num_iid', 'Num iid').renderWith (col) -> "<a target=\"_blank\" href=\"https://detail.tmall.hk/hk/item.htm?id=#{col}\">#{col}</a>"
          DTColumnBuilder.newColumn('key', 'Image Name').renderWith (col, type, row) -> row.url_key or row.image_name
          DTColumnBuilder.newColumn('price', 'Price')
          DTColumnBuilder.newColumn('beat_flg', 'Status').renderWith (col) => @status[col.toString()]
          DTColumnBuilder.newColumn('comment', 'Comment')
          DTColumnBuilder.newColumn('modifier', 'Modifier')
          DTColumnBuilder.newColumn('modified', 'Modified')
          DTColumnBuilder.newColumn('', '').renderWith (val, type, row, cell) ->
            "<button class=\"btn btn-success btn-xs\" ng-click=\"ctrl.beatControl('startup',#{row.beat_item_id || null})\">刷图</button>" +
              "<button class=\"btn btn-warning btn-xs\" ng-click=\"ctrl.beatControl('cancel',#{row.beat_item_id || null})\">取消</button>" +
              "<button class=\"btn btn-danger btn-xs\" ng-click=\"ctrl.beatControl('stop',#{row.beat_item_id || null})\">暂停</button>" +
              "<button class=\"btn btn-danger btn-xs\" ng-click=\"ctrl.setPrice(#{cell.row})\">修改价格</button>"
        ]
        instance: null

      @channels = user.getChannels()

      # 如果渠道只有一个。则默认选中第一个渠道，并调用事件处理
      if @channels.length is 1
        @selected.channel = @channels[0].id
        @changeChannel()

    # 状态翻译
    status:
      '0':'未执行'
      '1':'等待执行'
      '2':'刷图成功'
      '3':'刷图失败'
      '4':'还原成功'
      '5':'还原失败'
      '10':'取消'
      '11':'取消成功'
      '12':'取消失败'
    # 当前渠道的所有店铺
    carts: []
    # 上传 item 时类型
    itemsType: false
    # dtBeats 的表格数据
    beats: []
    # dtItems 的表格数据
    items: []
    # 当前选中的 beat 任务
    beat: {}
    # 当前选中的内容
    selected:
      channel: null
      flg: null
      lastChannel: null
    # 汇总信息
    beatSummary: {}

    $formatCart: (col) ->
      return "[ #{cart.comment} ] #{cart.shop_name}" for cart in @carts when parseInt(cart.cart_id) == col

    formatCart: (col) =>
      if @carts.length then @$formatCart col else col

    changeChannel: ->
      # 还原数据
      @selected.beat = null
      # 请求新的店铺数据
      @beatService.getCarts(@selected.channel).then (carts) =>
        @carts = carts
      # 记录用于下一次切换的判断
      @selected.lastChannel = @selected.channel
      # 让 Beats DataTable 刷新数据
      @dtBeats.instance?.reloadData()

    reloadData: (index) ->
      (if !index then @dtBeats else @dtItems).instance.reloadData()

    createBeat: ->
      if !@carts.length
        @alert 'NO_SELECTED_CART'
        return
      # 打开 modal 创建
      @$modal.open
        templateUrl: 'createBeat.tpl.html'
        controller: CreatorController
        controllerAs: 'ctrl'
        resolve:
          carts: => @carts
      .result.then (beat) =>
        beat.channel_id = @selected.channel
        @beatService.create beat
        .then () => @dtBeats.instance.reloadData()

    openBeat: (index) ->
      beat = @beats[index]
      if beat is @beat then return;
      @beat = beat
      @dtItems.instance.reloadData();

    onDtGetItems: (data, draw) =>
      if data.draw is 1 or !@beat
        draw data: [], recordsTotal: 0, recordsFiltered: 0
        return;
      data.param =
        beat: @beat
        flg: @selected.flg
      @beatService.dtGetItems data
      .then (data) =>
        draw data.dtResponse
        @items = data.dtResponse.data
        @beatSummary = data.beatSummary

    onDtGetBeats: (data, draw) =>
      # 第一次的请求，直接忽略
      if !@selected.channel
        draw data: [], recordsTotal: 0, recordsFiltered: 0
        return
      data.param =
        channel_id: @selected.channel
      # 获取活动下的具体内容
      @beatService.dtGetBeats data
      .then (data) =>
        @beats = data.data
        draw data

    $uploadItems: =>
      if !@beat or !@beat.beat_id
        @alert 'NO_SELECTED_BEAT'
        return
      [..., fileItem] = @itemUploader.queue
      if !fileItem
        @alert 'NO_UPLOAD_FILE'
        return
      progress = @showProgress fileItem
      # 注册成功事件
      fileItem.onSuccess = (res) =>
        progress.dismiss 'cancel'
        if res.result isnt 'OK'
          @alert res.message
          return
        # 成功后，刷新表格
        @dtItems.instance.reloadData()
      fileItem.formData = [
        beat_id: @beat.beat_id
        isCode: @itemsType
      ]
      fileItem.upload()

    uploadItems: ->
      if !@items or !@items.length
        @$uploadItems()
        return
      @confirm '提交新的商品会删除原来提交的所有内容。确定提交么？'
      .then @$uploadItems

    $downloadHelper: null

    $downFile: (action) ->
      @downFileAction = action
      if !@$downloadHelper
        @$downloadHelper = $('#beat-downloadHelper')[0]
        @$downloadHelper.onload = =>
          res = JSON.parse $(@$downloadHelper.contentDocument.body).text()
          if res.result is 'NG'
            @alert(res.message)
      $('#beat-downloadForm').attr('action', action).submit()

    downBeatIconExcel: ->
      if !@beat or !@beat.beat_id
        @alert 'NO_SELECTED_BEAT'
        return
      @$downFile @downItemsAction
      return false

    uploadPrice: ->
      @confirm '确定提交价格表么？'
      .then =>
        if !@beat or !@beat.beat_id
          @alert 'NO_SELECTED_BEAT'
          return
        [..., fileItem] = @priceUploader.queue
        if !fileItem
          @alert 'NO_UPLOAD_FILE'
          return
        progress = @showProgress fileItem
        # 注册成功事件
        fileItem.onSuccess = (res) =>
          progress.dismiss 'cancel'
          if res.result isnt 'OK'
            @alert res.message
            return
          @alert 'SUCCESS'

        fileItem.upload()

    downErrExcel: (lev) ->
      if !@beat or !@beat.beat_id
        @alert 'NO_SELECTED_BEAT'
        return
      $('#errLev').val(lev)
      @$downFile @downErrAction
      return false
    beatControl: (action, item_id) ->
      if !@beat or !@beat.beat_id
        @alert 'NO_SELECTED_BEAT。'
        return
      @beatService.beatControl @beat.beat_id, item_id, action
      .then (count) =>
        @notify.success "[ #{item_id || 'all'} ] #{action} complete. return count: [ #{count} ]"
        if count
          @dtItems.instance.reloadData()
    setPrice: (rowIndex) ->
      item = @items[rowIndex];
      if !item
        @alert 'NO_SELECTED_ITEM'
        return;
      @$modal.open
        templateUrl: 'setPrice.tpl.html'
        controller: SetPriceController
        controllerAs: 'ctrl'
        resolve:
          lastPrice: () => item.price
      .result.then (price) =>
        if !price
          @alert 'PRICE_ERR'
          return
        @beatService.setItemPrice @beat.beat_id, item.beat_item_id, price
        .then (count) =>
          if !count
            @notify.warning 'NO_ROW_MODIFIED'
          @dtItems.instance.reloadData()

    showProgress: (uploader) ->
      @$modal.open
        templateUrl: 'progress.tpl.html'
        backdrop: 'static'
        controller: ['uploader', (@uploader) ->]
        controllerAs: 'ctrl'
        resolve: uploader: () -> uploader

  ims.controller "BeatController", BeatController
