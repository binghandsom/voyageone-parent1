/**
 * Created by linanbin on 15/12/7.
 */

define(['cms'], function(cms) {

  return cms.controller('categoryPopupController', (function() {

    function CategoryPopupController(context, $uibModalInstance) {

      this.$uibModalInstance = $uibModalInstance;

      /**
       * 画面传递的上下文
       * @type {{from:string}}
       */
      this.context = context;
      /**
       * 主类目
       * @type {object[]}
       */
      this.categories = null;
      /**
       * 当前选择的类目
       * @type {object}
       */
      this.selected = null;
    }

    CategoryPopupController.prototype = {
      /**
       * 初始化时,加载必需数据
       */
      init: function() {
        // 加载主类目,如果主类目数据已经缓存则从本地读取

      },
      cancel: function() {
        this.$uibModalInstance.dismiss('cancel');
      }
    };

    return CategoryPopupController;

  })());
});