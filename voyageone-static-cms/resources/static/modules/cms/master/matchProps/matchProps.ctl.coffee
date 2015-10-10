###
  @Name: MatchPropsController
  @Description: 主数据属性匹配第三方品牌数据，画面控制器
  @Date: 2015-09-06 14:24:50

  @User: Jonas
  @Version: 0.0.1
###

define [
  'modules/cms/cms.module'
  'modules/cms/master/matchProps/matchProps.service'
  'modules/cms/popup/setMatchValue/popSetMatchValue.ctl'
  'css!modules/cms/master/matchProps/matchProps.css'
], (cmsModule)->
  KEYS =
    NAME: 'CMS_TXT_PROMOTION_NAME'
    TYPE: 'CMS_TXT_TH_TYPE'
    VALUE: 'CMS_TXT_TH_VALUE'
    REQUIRED: 'CMS_TXT_TH_REQUIRED'
    IGNORE: 'CMS_TXT_TH_IGNORE'
    OPERATION: 'CMS_TXT_ACTIONS'
    YES: 'CORE_BUTTON_OK'
    NO: 'CORE_BUTTON_CANCEL'
    CHANGE: 'CMS_MSG_CHANGE_IGNORE'
    SUCCESS: 'CMS_MSG_CHANGE_SUCCESS'

  template =
    chkIgnore: "<label class=\"checkbox-inline c-checkbox needsclick\">" +
      "<input type=\"checkbox\" ng-model=\"$row.is_ignore\" ng-change=\"vm.changeIgnore($row)\">" +
      "<span class=\"fa fa-check\"></span></label>"
    btnSetMatch: "<button href=\"index.html#/ims/attribute\" ng-class=\"{'btn-primary-s': $row.is_ignore}\" class=\"btn btn-primary btn-sm fa fa-list-alt\" ng-click=\"vm.matchValue($row)\"> 值匹配</button>"

  angular.module 'cmsModule.controllers.matchProps', ['cmsModule']
  .controller 'MatchPropsController', class
    @$inject: ['$scope', '$compile', '$routeParams', 'vAlert', 'MatchPropsService', 'DTOptionsBuilder',
               'DTColumnBuilder', '$modal', 'cmsPopupPages', 'vConfirm', 'notify', '$translate']
    constructor: (@$scope, @$compile, $routeParams, @alert, @matchPropsService, DTOptionsBuilder, DTColumnBuilder,
                  @$modal, @cmsPopupPages, @confirm, @notify, @$translate) ->
      @category.id = $routeParams.categoryId
      @matchPropsService.getPath @category.id
      .then (res) => @category.path = res.data
      @dtProps =
        options: ( DTOptionsBuilder.newOptions()
        .withOption 'processing', true
        .withOption 'serverSide', true
        .withOption 'ordering', false
        .withOption 'searching', false
        .withOption 'ajax', @dtGetProps
        .withOption 'createdRow', (tr, row) =>
          rowScope = @$scope.$new()
          rowScope.$row = row
          @$compile(
            angular.element(tr)
            .attr('ng-class', '{"prop-require": $row.is_required, "prop-ignore": $row.is_ignore}')
          )(rowScope)
        .withDataProp 'data'
        .withPaginationType 'full_numbers' )
        columns: [
          DTColumnBuilder.newColumn('prop_name', @$translate(KEYS.NAME))
          .withClass('col-sm-5')
          DTColumnBuilder.newColumn('prop_type', @$translate(KEYS.TYPE))
          .withClass('col-sm-1')
          .renderWith (v, t, r) -> r.typeName
          DTColumnBuilder.newColumn('val_count', @$translate(KEYS.VALUE))
          .withClass('col-sm-2')
          .renderWith (val)-> if val > 0 then '已设置' else '无'
          DTColumnBuilder.newColumn('is_required', @$translate(KEYS.REQUIRED))
          .withClass('col-sm-1')
          .renderWith (val) =>
            @$translate.instant(if val is 0 then KEYS.NO else KEYS.YES)
          DTColumnBuilder.newColumn('is_ignore', @$translate(KEYS.IGNORE))
          .withClass('col-sm-1')
          .renderWith -> template.chkIgnore
          DTColumnBuilder.newColumn('', @$translate(KEYS.OPERATION))
          .withClass('col-sm-2 col-md-1')
          .renderWith -> template.btnSetMatch
        ]
        instance: null

      # Windows 上应该没有相应的问题，但是 Mac 中会有中文无法输入的问题，所以此处进行细分处理
      @$scope.$watch 'vm.filter.ignored', (=>
        if @dtProps.instance then @dtProps.instance.reloadData() )
      @$scope.$watch 'vm.filter.required', (=>
        if @dtProps.instance then @dtProps.instance.reloadData() )

    options: [
      {label: 'ALL', value: ''}
      {label: 'YES', value: '1'}
      {label: 'NO', value: '0'}
    ]
    category:
      id: null
      path: null
    props: []
    feedProps: []
    cmsProps: []
    operations: []
    constGot: false
    filter:
      ignored: ''
      required: ''
      propName: ''

    getConst: () ->
      @matchPropsService.getConst().then (res) =>
        @constGot = true
        @cmsProps = res.data.cmsProps
        @feedProps = res.data.feedProps
        @operations = res.data.operations

    dtGetProps: (data, draw) =>
      if !@category.id
        draw data: [], recordsTotal: 0, recordsFiltered: 0
        return;
      # 设定参数
      data.param = @category.id
      data.columns[3].search.value = @filter.required
      data.columns[4].search.value = @filter.ignored
      data.search.value = @filter.propName
      # 设定搜索
      @matchPropsService.getProps data
      .then (res) =>
        draw res.data
        @props = res.data.data

    matchValue: (prop) ->
      if !@constGot
        @getConst().then => @matchValue prop
        return;
      @$modal.open
        templateUrl: @cmsPopupPages.popSetMatchValue.page
        controller: @cmsPopupPages.popSetMatchValue.controller
        controllerAs: 'vm'
        backdrop: 'static'
        size: 'lg'
        resolve:
          category: => @category
          currProp: -> prop
          cmsProps: => @cmsProps
          operations: => @operations
          feedProps: => @feedProps

    changeIgnore: (prop) ->
      @confirm(KEYS.CHANGE).result.then(
        => @matchPropsService.setIgnore(prop).then => @notify.success KEYS.SUCCESS
        => prop.is_ignore = !prop.is_ignore
      )