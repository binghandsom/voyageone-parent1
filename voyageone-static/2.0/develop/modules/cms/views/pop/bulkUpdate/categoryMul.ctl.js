/**
 * @description 类目多选popup
 * @author piao wenjie
 * @version 2.5.0
 */
define(['cms',
    'underscore'
], function (cms) {

    return cms.controller('popCategoryMulCtl', (function () {

        function PopCategoryMulController(context, $uibModalInstance, notify, $translate) {

            this.$uibModalInstance = $uibModalInstance;
            this.notify = notify;
            this.$translate = $translate;
            this.context = context;
            this.categories = null;
            this.selectedCat = {};
            this.catArrs = [];
            this.selectObj = {};

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
            /**
             * 父节点与子节点之间的区分符,缺省是'>'
             * @type {String}
             */
            this.divType = ">";

        }

        /**
         * 初始化时,加载必需数据
         */
        PopCategoryMulController.prototype.init = function(){
            var self = this;
            self.categories = self.context.categories;
            if (self.context.divType != undefined) {
                self.divType = self.context.divType;
            }
            self.categoryPath = [{level: 1, categories: self.categories}];

            self.defaultCategroy();
        };
        /**
         * 打开一个类目(选定一个类目)
         * 并尝试展示其子类目
         * @param {object} category 类目对象
         */
        PopCategoryMulController.prototype.openCategory = function(category, categoryItem){
            if (categoryItem.selectedCat == undefined) {
                categoryItem.selectedCat = [];
            }
            categoryItem.selectedCat = category.catName;

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
        };

        /**
         * select下拉款的默认选中
         */
        PopCategoryMulController.prototype.defaultCategroy = function(){
            if (!this.context.from)
                return;

            var self = this;

            if (!self.context.from || self.context.from.length == 0)
                return;

            var from = self.catArrs =  angular.copy(self.context.from);
            angular.forEach(from, function (element) {
                var _element = {};
                _element[element.catId] = element.catPath;
                _.extend(self.selectObj, _element);
            });
            self.context.from = from[from.length - 1].catPath;
        };

        /**
         * 选中input【type=checkbox】
         */
        PopCategoryMulController.prototype.selectCat = function(item){
            var self = this,
                _index;

            _index =  _.map(self.catArrs,function(item){
                return item.catPath;
            }).indexOf(item.catPath);

            if (_index < 0){
                self.catArrs.push({catId: item.catId, catPath: item.catPath});
                var _element = {};
                _element[item.catId] = item.catPath;
                _.extend(self.selectObj, _element);
            }
            else
                self.catArrs.splice(_index,1);
        };

        /**
         * 下拉框的值改变时触发
         */
        PopCategoryMulController.prototype.selectChange = function(){
            var self = this;
            var selectedObj = {};
            angular.forEach(self.catArrs,function(element){
                var _element = {};
                _element[element.catId] = element.catPath;
                _.extend(selectedObj,_element);
            });
            self.selectObj = selectedObj;
        };

        /**
         * 保存
         */
        PopCategoryMulController.prototype.ok = function(){

            if (this.catArrs.length == 0) {
                this.notify.danger(this.$translate.instant('TXT_MSG_NO_CATEGORY_SELECT'));
                return;
            }

            this.$uibModalInstance.close(this.catArrs);
        };

        /**
         * 取消
         */
        PopCategoryMulController.prototype.cancel = function(){
            this.$uibModalInstance.dismiss();
        };

        return PopCategoryMulController;

    })());
});