/**
 * Created by jeff.duan on 16/6/15.
 *
 * @Author piao
 * @Date 2016-7-7
 * @Description 添加了默认选中和选中背景
 divtype："-"：为feed类目的分隔符， ">"：为主类目的分隔符
 */

define(['cms',
    'underscore'
], function (cms) {

    return cms.controller('popCategoryNewCtl', (function () {

        function PopCategoryNewController(context, $uibModalInstance, notify, $translate) {

            this.$uibModalInstance = $uibModalInstance;
            this.notify = notify;
            this.$translate = $translate;

            /**
             * 画面传递的上下文
             * @type {{categories: object[], from: string, divType: string}}
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
            this.selectedCat = {};
            /**
             * 选择目录的路径
             * @type {Array}
             */
            this.categoryPath = [];
            /**
             * 父节点与子节点之间的区分符,缺省是'>'
             * @type {String}
             */
            this.divType = ">";
        }

        PopCategoryNewController.prototype = {
            /**
             * 初始化时,加载必需数据
             */
            init: function () {
                this.categories = this.context.categories;
                if (this.context.divType != undefined) {
                    this.divType = this.context.divType;
                }

                // 每次加载,都初始化 TOP 为第一级
                this.categoryPath = [{level: 1, categories: this.categories}];

                //默认选中
                this.defaultCategroy();
            },
            /**
             * 打开一个类目(选定一个类目)
             * 并尝试展示其子类目
             */
            openCategory: function (category, categoryItem) {
                // 标记选中
                this.selected = category;
                categoryItem.selectedCat = category.catName;

                // 查询当前选中的是第几级
                var level = 0;
                if (this.divType == null) {
                    level = category.catPath.split('>').length;
                } else {
                    level = category.catPath.split(this.divType).length;
                }
                // 获取这一级别的数据
                var pathItem = this.categoryPath[level];

                if (pathItem) {
                    // 如果有数据,那么当前级别和后续级别都需要清空
                    this.categoryPath.splice(level);
                }

                if (!category.children || !category.children.length) return;

                this.categoryPath.push({level: level + 1, categories: category.children});
            },
            defaultCategroy: function () {
                // 默认选中
                if (!this.context.from)
                    return;

                var self = this, str = this.context.from + "";
                var arrayCat = str.split(self.divType);
                angular.forEach(arrayCat, function (item1, index) {
                    _.filter(self.categoryPath[index].categories, function (item2) {
                        if (item2.catName == item1) {
                            self.categoryPath[index].selectedCat = item1;
                            if (item2.children.length != 0) {
                                self.categoryPath.push({level: index + 2, categories: item2.children});
                            }
                            else
                                self.selected = item2;
                            return true;
                        }
                    });
                });
            },
            getSkuSplitDesc: function (category) {
                return category.skuSplit.id == 1 ? '该类目下的商品，将按照一个SKU为一个商品进行拆分。' : '该类目下的商品，将按照一个SKU为一个商品占一个平台页面进行拆分。';
            },
            getSkuSplitClass: function (category) {
                return category.skuSplit.id == 1 ? {'label-info': true} : {'label-warning': true};
            },
            ok: function () {

                if (!this.selected) {
                    this.notify.danger(this.$translate.instant('TXT_MSG_NO_SELECT_CATEGORY'));
                    return;
                }

                this.context.selected = this.selected;
                this.$uibModalInstance.close(this.context);
            },
            cancel: function () {
                this.$uibModalInstance.dismiss();
            }
        };

        return PopCategoryNewController;

    })());
});