/*
  @Name: beat-icon service
  @Date: 2015/06/30

  @User: Jonas
  @Version: 0.2.2
 */
define(['modules/ims/ims.module'], function(ims) {
  return ims.service('ImsBeatService', [
    'ajaxService', 'imsAction', (function() {

      function ImsBeatService(ajaxService, imsAction) {
        this.url = imsAction.beatIcon;
        this.post = (function(_this) {
          return function(url, param) {
            return ajaxService.ajaxPost(param, _this.url.root + url);
          };
        })(this);
      }

      ImsBeatService.prototype.getCarts = function(channel_id) {
        return this.post(this.url.getCarts, {
          order_channel_id: channel_id
        }).then(function(res) {
          return res.data;
        });
      };

      ImsBeatService.prototype.create = function(beat) {
        return this.post(this.url.create, beat).then(function(res) {
          return res.data;
        });
      };

      ImsBeatService.prototype.dtGetBeats = function(dtData) {
        return this.post(this.url.dtGetBeats, dtData).then(function(res) {
          return res.data;
        });
      };

      ImsBeatService.prototype.dtGetItems = function(dtData) {
        return this.post(this.url.dtGetItems, dtData).then(function(res) {
          return res.data;
        });
      };

      ImsBeatService.prototype.beatControl = function(beat_id, item_id, action) {
        return this.post(this.url.control, {
          beat_id: beat_id,
          item_id: item_id,
          action: action
        }).then(function(res) {
          return res.data;
        });
      };

      ImsBeatService.prototype.setItemPrice = function(beat_id, item_id, price) {
        return this.post(this.url.setItemPrice, {
          beat_id: beat_id,
          item_id: item_id,
          price: price
        }).then(function(res) {
          return res.data;
        });
      };

      return ImsBeatService;

    })()
  ]);
});
