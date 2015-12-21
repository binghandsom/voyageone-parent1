/**
 * Created by linanbin on 15/12/7.
 */

define(['cms'], function(cms) {

  return cms.controller('categoryPopupController', (function() {

    function CategoryPopupController(context, feedMappingService, $uibModalInstance) {

      this.$uibModalInstance = $uibModalInstance;
      this.feedMappingService = feedMappingService;

      /**
       * 画面传递的上下文
       * @type {{from:object}}
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
      /**
       * 选择目录的路径
       * @type {Array}
       */
      this.categoryPath = [];
    }

    CategoryPopupController.prototype = {
      /**
       * 初始化时,加载必需数据
       */
      init: function() {
        // 加载主类目,如果主类目数据已经缓存则从本地读取
        this.feedMappingService.getMainCategories().then(function(res) {
          this.categories = res.data;
          // 每次加载,都初始化 TOP 为第一级
          this.categoryPath = [this.categories];
        }.bind(this));
      },
      cancel: function() {
        this.$uibModalInstance.dismiss('cancel');
      }
    };

    return CategoryPopupController;

  })());
});