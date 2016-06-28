/**
 * Created by linanbin on 15/12/7.
 * @author sofia
 * @description 新增层级判断
 */

define(['cms',
    'underscore'
], function (cms) {

    return cms.controller('popCategoryCtl', (function () {

        function PopCategoryController(context, $uibModalInstance, notify, $translate) {

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
            this.selectedCat = [];
            /**
             * 选择目录的路径
             * @type {Array}
             */
            this.categoryPath = [];
            /**
             * 父节点与子节点之间的区分符,缺省是'>'
             * @type {String}
             */
            this.divType = null;
        }

        PopCategoryController.prototype = {
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

                /**产品详情页平台schema默认选中*/
                if (this.context.plateSchema)
                    this.defaultCategroy();
            },
            /**
             * 打开一个类目(选定一个类目)
             * 并尝试展示其子类目
             * @param {object} category 类目对象
             */
            openCategory: function (category, categoryItem) {
                if (categoryItem.selectedCat == undefined) {
                    categoryItem.selectedCat = [];
                }
                categoryItem.selectedCat[0] = category;

                // 标记选中
                this.selected = category;

                // 查询当前选中的是第几级
                var levelIdx = categoryItem.level - 1;

                // 获取这一级别的数据
                var pathItem = this.categoryPath[levelIdx + 1];
                if (pathItem) {
                    // 如果有数据,那么当前级别和后续级别都需要清空
                    this.categoryPath.splice(levelIdx + 1);
                }
                if (!category.children || !category.children.length) return;
                this.categoryPath.push({level: levelIdx + 2, categories: category.children});
            },
            defaultCategroy: function () {
                // 默认选中
                var self = this;
                var arrayCat = this.context.from.split(">");
                angular.forEach(arrayCat, function (item1, index) {
                    _.filter(self.categoryPath[index].categories, function (item2) {
                        if (item2.catName == item1) {
                            if (item2.children.length != 0)
                                self.categoryPath.push({level: index + 2, categories: item2.children});
                            else
                                self.selected = item2;
                            return true;
                        }
                    });
                });
            },
            ok: function () {

                if (!this.selected) {
                    this.notify.danger(this.$translate.instant('TXT_MSG_NO_CATEGORY_SELECT'));
                    return;
                }

                if (this.selected.isParent === 1) {
                    this.notify.danger(this.$translate.instant('TXT_MSG_IS_NOT_CHILE_CATEGORY'));
                    return;
                }

                this.context.selected = this.selected;
                this.$uibModalInstance.close(this.context);
            },
            cancel: function () {
                this.$uibModalInstance.dismiss();
            }
        };

        return PopCategoryController;

    })());
});