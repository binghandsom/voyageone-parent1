define [
  'components/app'
  'components/services/language.service'
], (app) ->
  app.config [
    '$translateProvider'
    'languageType'
    ($translate, languageType) ->
      $translate.translations languageType.zh,
        NO_SELECTED_CART: '没有找到店铺。请先选择渠道。'
        NO_SELECTED_BEAT: '先选择任务。'
        NO_UPLOAD_FILE: '提交前请选择要提交的文件'
        NO_SELECTED_ITEM: '没找到子任务'
        PRICE_ERR: '不能不填写价格或价格不能小于 0'
        NO_ROW_MODIFIED: '修改完成了，但是服务器并没有更新任何行'
        SUCCESS: '成功处理'
  ]