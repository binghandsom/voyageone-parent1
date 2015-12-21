/**
 * Created by linanbin on 15/12/7.
 */

define(['cms'], function (cms) {

    return cms.controller('categoryPopupController', (function () {

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
            init: function () {
                // 加载主类目,如果主类目数据已经缓存则从本地读取
                this.feedMappingService.getMainCategories().then(function (res) {
                    this.categories = res.data;
                    // 每次加载,都初始化 TOP 为第一级
                    this.categoryPath = [{level:1, categories: this.categories}];
                }.bind(this));
            },
            /**
             * 打开一个类目(选定一个类目)
             * 并尝试展示其子类目
             * @param {object} category 类目对象
             */
            openCategory: function (category) {
                // 标记选中
                this.selected = category;

                // 查询当前选中的是第几级
                var level = category.catPath.split('>').length;
                // 获取这一级别的数据
                var pathItem = this.categoryPath[level];

                if (pathItem) {
                    // 如果有数据,那么当前级别和后续级别都需要清空
                    this.categoryPath.splice(level);
                }

                if (!category.children || !category.children.length) return;

                this.categoryPath.push({level:level + 1, categories: category.children});
            },
            cancel: function () {
                this.$uibModalInstance.dismiss('cancel');
            }
        };

        return CategoryPopupController;

    })());
});