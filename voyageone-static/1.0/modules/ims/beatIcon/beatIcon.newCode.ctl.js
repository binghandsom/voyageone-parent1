/**
 * 价格披露里,新增商品的弹出框
 *
 * @User: Jonas
 * @Version: 0.2.3
 */

define([
  'modules/ims/ims.module'
], function (ims) {

  NewCodeController.$inject = ['$modalInstance'];

  function NewCodeController($modalInstance) {
    this.instance = $modalInstance;
  }

  NewCodeController.prototype = {

    code: '',
    price: '',
    message: '',
    isCode: false,

    ok: function () {

      if (!this.code) {
        var msg = this.isCode ? "Code" : "数字 ID";
        return this.message = '请务必填写一个 [ ' + msg + ' ] (*￣０￣)ノ';
      }

      if (!this.price) {
        return this.message = '请务必填写一个 [ 活动价 ] (*￣ω￣) ';
      }

      if (!/^\d+\.?\d*$/.test(this.price)) {
        return this.message = '[ 活动价 ] 必须是数字 (-_-メ)';
      }

      this.instance.close({
        code: this.code,
        price: this.price,
        isCode: this.isCode
      });
    },

    cancel: function () {
      this.instance.dismiss('cancel');
    }
  };

  ims.controller('ims.beat.NewCodeController', NewCodeController);
});