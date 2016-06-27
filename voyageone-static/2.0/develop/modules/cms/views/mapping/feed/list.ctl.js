/**
 * controller FeedMappingController
 */

define([
    'cms',
    'underscore',
    'modules/cms/controller/popup.ctl'
], function (cms, _) {
    "use strict";
    return cms.controller('feedMappingController', (function () {

        /**
         * @param obj
         * @constructor
         * @property seq
         * @property path
         * @property isChild
         * @property level
         * @property {Array.<MappingBean>} mappings
         * @property {MappingBean} defaultMapping
         * @property {MappingBean} selectedMapping
         * @property classes
         */
        function FeedCategoryBean(obj) {
            if (obj) _.extend(this, obj);
            this.classes = {
                background: (this.isChild == 1 ? 'badge-empty' : 'badge-success'),
                icon: (this.isChild == 1 ? 'fa-level-up' : 'fa-level-down')
            };
            if (!this.mappings || !this.mappings.length) return;
            this.mappings = this.mappings.map(function (mapping) {
                return new MappingBean(mapping);
            });
            this.selectedMapping = this.defaultMapping = this.mappings.find(function (mapping) {
                return mapping.defaultMapping == 1;
            });
        }

        FeedCategoryBean.prototype = {
            /**
             * 检查当前类目是否已进行类目匹配
             * @return {boolean}
             */
            isMatched: function () {
                // 只要默认 mapping 存在即已匹配
                return !!this.defaultMapping;
            },

            /**
             * 检查当前类目是否已经完成了属性匹配
             * @return {boolean}
             */
            isPropertyMatched: function () {
                if (!this.isMatched())
                    return false;

                var mapping = this.selectedMapping;
                var mainMapping = mapping.mainMapping;

                // 如果有匹配并且确实匹配完了,才算是属性匹配完成 ==>mainMapping和feedMapping其中有一个完成就算匹配完成
                return !!mapping.matchOver || (mainMapping && !!mainMapping.matchOver);
            },

            /**
             * 更新默认 Mapping
             */
            resetDefaultMapping: function(mapping) {
                var defMappingIndex = 0;
                if (!(mapping instanceof MappingBean)) mapping = new MappingBean(mapping);
                if (!this.mappings) {
                    this.mappings = [];
                } else {
                    defMappingIndex = this.mappings.indexOf(this.defaultMapping);
                }
                this.mappings[defMappingIndex] = mapping;
                this.defaultMapping = this.selectedMapping = mapping;
            }
        };

        /**
         *
         * @param obj
         * @constructor
         * @property _id
         * @property defaultMapping
         * @property defaultMain
         * @property matchOver
         * @property feedPath
         * @property mainPath
         * @property {MappingBean} mainMapping
         */
        function MappingBean(obj) {
            if (!obj) return;
            // 没有 Scope 表示这应该是后端的 MappingBean 数据
            if (!obj.feedCategoryPath && !obj.mainCategoryPath) {
                _.extend(this, obj);
                return;
            }
            // 如果有 Scope 说明是后端的 Model 数据
            this._id = obj._id;
            this.defaultMapping = obj.defaultMapping;
            this.defaultMain = obj.defaultMain;
            this.matchOver = obj.matchOver;
            this.feedPath = obj.feedCategoryPath;
            this.mainPath = obj.mainCategoryPath;
        }

        /**
         * @typedef {{parentPath:string,level:int,model:object,mapping:object}} FeedCategoryBean
         */
        /**
         * @description
         * Feed Mapping 画面的 Controller 类
         * @param $scope
         * @param feedMappingService
         * @param feedMappingListService
         * @param {voNotify} notify
         * @param {function} confirm
         * @param $translate
         * @param blockUI
         * @constructor
         */
        function FeedMappingController($scope, feedMappingService, feedMappingListService, notify, confirm, $translate, blockUI) {

            this.confirm = confirm;
            this.notify = notify;
            this.$translate = $translate;
            this.feedMappingService = feedMappingService;
            this.feedMappingListService = feedMappingListService;
            this.blockUI = blockUI;

            this.currCategoryMap = null;

            this.topCategories = null;
            /**
             * 当前选择的 TOP 类目
             * @type {string}
             */
            this.selectedTop = null;
            /**
             * 表格的数据源
             * @type {object[]}
             */
            this.tableSource = null;
            /**
             * 匹配情况筛选条件
             * @type {{category: boolean, property: boolean}}
             */
            this.matched = {
                category: null,
                property: null,
                keyWord: null
            };

            // 在 Window 上注册回调, 用于子页面通知切换 MatchOver
            var self = this;

            window.feedMappingController = {
                setMatchOver: function (mappingModel, matchOver) {
                    $scope.$apply(function () {
                        var map = self.currCategoryMap;
                        var isCommon = mappingModel.defaultMain === 1;
                        var mainPath = mappingModel.mainCategoryPath;
                        var feedPath = mappingModel.feedCategoryPath;
                        // 更新目标 Feed Mapping
                        map[feedPath].selectedMapping.matchOver = matchOver;
                        if (!isCommon) return;
                        // 更新目标通用 Mapping
                        Object.keys(map).forEach(function (key) {
                            var feedCategoryBean = map[key];
                            if (!feedCategoryBean.selectedMapping) return;
                            var mapping = feedCategoryBean.selectedMapping.mainMapping;
                            if (!mapping) return;
                            if (mapping && mapping.mainPath === mainPath)
                                mapping.matchOver = matchOver;
                        });
                    });
                }
            };
        }

        FeedMappingController.prototype = {
            /**
             * select 的绑定数据源
             */
            options: {
                category: {
                    '类目匹配情况(所有)': null,
                    '类目已匹配': true,
                    '类目未匹配': false
                },
                property: {
                    '匹配属性情况(所有)': null,
                    '属性已匹配完成': true,
                    '属性未匹配完成': false
                }
            },
            /**
             * 画面初始化时
             */
            init: function () {
                var self = this;
                self.feedMappingService.getTopCategories().then(function (res) {
                    self.topCategories = res.data;
                    self.selectedTop = self.topCategories[0].catId;
                    self.refreshTable();
                });
            },

            /**
             * 刷新表格
             */
            refreshTable: function () {

                var self = this;
                var keyWord = self.matched.keyWord;

                self.blockUI.start();

                self.feedMappingListService.getCategoryMap(self.selectedTop).then(function (categoris) {

                    var categoryMap = self.currCategoryMap = {};

                    categoris = _.map(categoris, function (categoryBean) {
                        return (categoryMap[categoryBean.path] = new FeedCategoryBean(categoryBean));
                    }).filter(function (feedCategoryBean) {

                        var result = true;

                        // 如果关键字存在, 先进行关键字过滤
                        if (keyWord && feedCategoryBean.path.indexOf(keyWord) < 0)
                            return false;

                        if (self.matched.category !== null)
                            result = self.matched.category === feedCategoryBean.isMatched();
                        else if (self.matched.property !== null)
                            result = self.matched.property === feedCategoryBean.isPropertyMatched();

                        return result;
                    });

                    // 绑定&显示
                    self.tableSource = categoris;

                    self.blockUI.stop();
                });
            },

            /**
             * 继承其父类目的 Mapping
             * @param {object} feedCategory Feed 类目
             */
            extendsMapping: function (feedCategory) {

                var ttt = this;

                ttt.confirm('TXT_MSG_CONFIRM_FROWARD_PARENT_CATEGORY')
                    .result
                    .then(function () {
                        return ttt.feedMappingService.extendsMapping({
                            from: feedCategory.path
                        });
                    })
                    .then(function (res) {
                        if (!res.data)
                            ttt.notify.warning({id: 'TXT_MSG_NO_FIND_FORWARD_CATEGORY'});
                        else
                            ttt.resetBeanMapping(feedCategory.path, res.data);
                    });
            },
            /**
             * 获取类目的默认 Mapping 类目
             * @param {FeedCategoryBean} feedCategory
             * @returns {string}
             */
            getDefaultMapping: function (feedCategory) {

                if (!feedCategory) {
                    return '?';
                }

                var parent = null;
                var defMapping = feedCategory.defaultMapping;

                if (defMapping)
                    return defMapping.mainPath;

                while (!defMapping && (parent = this.findParent(parent || feedCategory))) {
                    defMapping = parent.defaultMapping;
                }

                return defMapping
                    ? (this.$translate.instant('TXT_FORWARD_WITH_COLON') + defMapping.mainPath)
                    : this.$translate.instant('TXT_UN_SETTING');
            },
            /**
             * 查找父级类目
             * @param {FeedCategoryBean} category
             * @returns {object}
             */
            findParent: function (category) {
                return !category.parentPath ? null : this.findCategory(category.parentPath);
            },
            /**
             * 在树中查找类目
             * @param {string} path 类目路径
             * @return {object|undefined} Feed 类目对象
             */
            findCategory: function (path) {
                return this.currCategoryMap[path];
            },

            /**
             * 在类目 Popup 确定关闭后, 为相关类目进行绑定
             * @param {{from:string, selected:object}} context Popup 返回的结果信息
             */
            bindCategory: function (context) {
                var self = this;
                self.feedMappingService.setMapping({
                    from: context.from,
                    to: context.selected.catPath
                }).then(function (res) {
                    self.resetBeanMapping(context.from, res.data);
                });
            },

            /**
             * 重新设置 bean 对象的 Mapping 和 MainMapping 属性
             * @param feedCategoryPath
             * @param mappingModel
             */
            resetBeanMapping: function (feedCategoryPath, mappingModel) {
                var self = this;
                var feedCategoryBean = self.findCategory(feedCategoryPath);
                feedCategoryBean.resetDefaultMapping(mappingModel);
                // 使用新的查询其默认主 Mapping
                self.feedMappingListService.getMainMapping(feedCategoryBean.defaultMapping.mainPath)
                    .then(function (mainMapping) {
                        feedCategoryBean.defaultMapping.mainMapping = new MappingBean(mainMapping);
                    });
            },

            /**
             * 打开匹配弹出框
             * @param {FeedCategoryBean} feedCategoryBean
             * @param {function} popupNewCategory
             */
            openCategoryMapping: function (feedCategoryBean, popupNewCategory) {

                this.feedMappingService.getMainCategories()
                    .then(function (res) {
                        
                        popupNewCategory({

                            categories: res.data,
                            from: feedCategoryBean.path

                        }).then(this.bindCategory.bind(this));

                    }.bind(this));
            },

            /**
             * 重置搜索条件
             */
            clear: function () {
                this.matched.category = null;
                this.matched.property = null;
                this.matched.keyWord = null;
                this.refreshTable();
            }
        };

        return FeedMappingController;

    })()).service('feedMappingListService', (function () {

        function FeedMappingListService(feedMappingService) {
            this.feedMappingService = feedMappingService;
        }

        FeedMappingListService.prototype = {
            getCategoryMap: function (categoryId) {
                var self = this;
                return self.feedMappingService.getFeedCategoryTree({topCategoryId: categoryId})
                    .then(function (res) {
                        return res.data;
                    });
            },
            getMainMapping: function (mainCategoryPath) {
                var self = this;
                return self.feedMappingService.getMainMapping({to: mainCategoryPath}).then(function (res) {
                    return res.data;
                });
            }
        };

        return FeedMappingListService;

    })());
});