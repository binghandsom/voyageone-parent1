###
  @Name: DictListController
  @Date: 2015-09-15 13:29:15

  @User: Jonas
  @Version: 0.0.1
###

define [
  'modules/cms/cms.module'
  'modules/cms/master/dict/dict.service'
], (cmsModule)->
  cmsModule.controller 'DictListController', class
    @$inject: ['$scope', '$compile', '$translate', 'DTOptionsBuilder', 'DTColumnBuilder', 'DictService', '$location', 'notify']
    constructor: ($scope, $compile, @$translate, DTOptionsBuilder, DTColumnBuilder, @DictService, @$location, @notify) ->
      @dtDict =
        options: ( DTOptionsBuilder.newOptions()
        .withOption 'processing', true
        .withOption 'serverSide', true
        .withOption 'ordering', false
        .withOption 'ajax', @dtGetDict
        .withOption 'createdRow', (tr, row) =>
          rowScope = $scope.$new()
          rowScope.$row = row
          $compile(
            angular.element(tr)
            .attr('ng-class', '{"prop-require": $row.is_required, "prop-ignore": $row.is_ignore}')
          )(rowScope)
        .withDataProp 'data'
        .withPaginationType 'full_numbers' )
        columns: [
          DTColumnBuilder.newColumn('name', @$translate 'CMS_TXT_PROMOTION_NAME')
          .withClass('col-sm-2')
          DTColumnBuilder.newColumn('value', @$translate 'CMS_TXT_TH_VALUE')
          .withClass('col-sm-8')
          .renderWith -> '{{$row.value}}' # 借 angular 的 bind 进行 html 的 encode
          DTColumnBuilder.newColumn('', @$translate('CMS_TXT_ACTIONS'))
          .withClass('col-sm-2')
          .renderWith ->
            '<button ng-click="vm.editItem($row)" class="btn btn-success btn-sm"><i class="fa fa-pencil"></i></button>&nbsp;'+
            '<button ng-click="vm.delItem($row)" class="btn btn-danger btn-sm"><i class="fa fa-trash-o"></i></button>'
        ]
        instance: null

    dtGetDict: (data, callback) =>
      @DictService.dtGetDict data
      .then (res) -> callback res.data

    addItem: ->
      @editItem {}

    editItem: (item) ->
      # session 传递新对象
      sessionStorage.dict = angular.toJson item: item
      # 跳转
      @$location.path '/cms/dict/item'

    delItem: (item) ->
      @DictService.delDict item
      .then (res) =>
        if res.data
          @notify.success 'CMS_TXT_MSG_DELETE_SUCCESS'
          @dtDict.instance.reloadData()
        else
          @notify.warning 'CMS_TXT_MSG_DELETE_FAILED'