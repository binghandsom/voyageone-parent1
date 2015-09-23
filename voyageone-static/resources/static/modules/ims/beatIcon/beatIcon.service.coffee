###
  @Name: beat-icon service
  @Date: 2015/06/30

  @User: Jonas
  @Version: 0.2.1
###

define ['modules/ims/ims.module'], (ims) ->
  ims.service 'ImsBeatService', [
    'ajaxService', 'imsAction'
    class
      constructor: (ajaxService, imsAction) ->
        @url = imsAction.beatIcon;
        @post = (url, param) => ajaxService.ajaxPost param, @url.root + url
      getCarts: (channel_id) ->
        @post @url.getCarts,
          order_channel_id: channel_id
        .then (res) -> res.data
      create: (beat)->
        @post @url.create, beat
        .then (res) -> res.data
      dtGetBeats: (dtData) ->
        @post @url.dtGetBeats, dtData
        .then (res) -> res.data
      dtGetItems: (dtData) ->
        @post @url.dtGetItems, dtData
        .then (res) -> res.data
      beatControl: (beat_id, item_id, action) ->
        @post @url.control,
          beat_id: beat_id, item_id: item_id, action: action
        .then (res) -> res.data
      setItemPrice: (beat_id, item_id, price) ->
        @post @url.setItemPrice,
          beat_id: beat_id, item_id: item_id, price: price
        .then (res) -> res.data
  ]





