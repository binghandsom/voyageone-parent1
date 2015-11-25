define [
  'components/app'
  'components/services/language.service'
], (app) ->
  app.config [
    '$translateProvider'
    'languageType'
    ($translate, languageType) ->
      $translate.translations languageType.en,
        NO_SELECTED_CART: 'No stores were found. Please select a channel.'
        NO_SELECTED_BEAT: 'Select a task first.'
        NO_UPLOAD_FILE: 'Before submitting, please select the files you want to submit'
        NO_SELECTED_ITEM: 'Couldn\'t find subtasks'
        PRICE_ERR: 'Must fill in the price or price cannot be less than 0'
        NO_ROW_MODIFIED: 'Modifications are complete, but the server does not update any rows'
        SUCCESS: 'Success'
  ]