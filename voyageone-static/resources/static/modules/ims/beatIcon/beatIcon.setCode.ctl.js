/**
 * 价格披露里,修改商品的弹出框
 *
 * @User: Jonas
 * @Version: 0.2.3
 */

define([
  'modules/ims/ims.module'
], function (ims) {

  SetCodeController.$inject = ['$modalInstance', 'lastCode'];

  function SetCodeController($modalInstance, lastCode) {
    this.instance = $modalInstance;
    this.code = lastCode;
  }

  SetCodeController.prototype = {

    message: '',

    ok: function () {
      if (this.code) {
        this.instance.close(this.code);
      } else {
        this.message = '请务必填写一个 Code 或数字 ID (Num_iid)';
      }
    },

    cancel: function () {
      this.instance.dismiss('cancel');
    }
  };

  ims.controller('ims.beat.SetCodeController', SetCodeController);
});