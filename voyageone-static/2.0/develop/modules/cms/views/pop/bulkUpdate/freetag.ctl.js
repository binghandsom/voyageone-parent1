/**
 * Created by sofia on 6/7/2016.
 *
 * updated by piao wenjie
 * @description 添加了自由标签的默认选中和新增的逻辑判断
 * @date 2016-8-5
 */

define([
    'cms'
], function (cms) {

    function flatTrees(categories, parent) {
        return categories.reduce(function (map, curr) {
            curr.parent = parent;
            map[curr.id] = curr;
            if (curr.children && curr.children.length)
                map = angular.extend(map, flatTrees(curr.children, curr));
            return map;
        }, {});
    }

    /**@description 判断选中的值是否改变
     * @param orgChkStsMap 全选的collection  id是全路径要分割
     * @param selectedMap   选中的collection
     */
    function canSave(orgChkStsMap,selectedMap){
        var orgArr,selectedArr;

        orgArr = _.pluck(_.map(orgChkStsMap,function(value,key){
            return {id: _.max(key.split("-")),value:value};
        }).filter(function(item){
            return item.value;
        }),"id");

        selectedArr = _.map(selectedMap,function(item){
            return item.selectedId;
        });

        return compareArr(orgArr,selectedArr);
    }

    /**比较字符串数组是否值相等*/
    function compareArr(arr1,arr2){
        return arr1.length === arr2.length && _.every(arr1,function(element1){
            return _.some(arr2,function(element2){
                return element2 == element1;
            });
        });
    }

    cms.controller('popFreeTagCtl', (function () {
        function popFreeTagCtl(context, channelTagService, $uibModalInstance,alert) {
            this.channelTagService = channelTagService;
            this.$uibModalInstance = $uibModalInstance;
            this.alert = alert;
            this.tagTypeSelectValue = context.tagTypeSel;
            this.cartId = context.cartId;
            this.orgFlg = context.orgFlg; // orgFlg==1:表示从高级检索的检索条件而来；orgFlg==2:表示从高级检索的设置自由标签而来；其它场合不设值
            this.productIds = context.productIds;
            this.selAllFlg = context.selAllFlg;
            this.tagTree = null;
            this.id = "";
            this.parentTagId = "";
            this.tree = [];
            this.key = [];
            this.selected = [];
            this.taglist = {selList: []};
            this.selOrgDispList = [];
            this.orgChkStsMap = {};
            this.orgDispMap = {};
            this._orgChkStsMap = {};
        }

        popFreeTagCtl.prototype = {
            /**
             *初始化数据,上手调用search拼装tree
             */
            init: function () {
                var self = this;
                var params = {
                    tagTypeSelectValue: self.tagTypeSelectValue,
                    'cartId': self.cartId,
                    'orgFlg': self.orgFlg
                };
                if (self.orgFlg == 2) {
                    params.productIds = self.productIds;
                    params.isSelAll = self.selAllFlg;
                }
                self.channelTagService.init(params).then(function (res) {
                    self.source = self.tagTree = res.data.tagTree;

                    /**当是高级检索，设置自由标签时，有初始勾选值*/
                    if (self.orgFlg == 2) {
                        // 返回值：orgChkStsMap和orgDispMap是互斥的,即一个tag不可能在两个Map中同时出现
                        /**checkbox勾选状态*/
                        if (res.data.orgChkStsMap == undefined || res.data.orgChkStsMap == null) {
                            self.orgChkStsMap = {};
                            self._orgChkStsMap = {};
                        } else {
                            self.orgChkStsMap = res.data.orgChkStsMap;
                            self._orgChkStsMap = angular.copy(res.data.orgChkStsMap);
                        }
                        /**checkbox半选状态*/
                        if (res.data.orgDispMap == undefined || res.data.orgDispMap == null) {
                            self.orgDispMap = {};
                            self._orgDispMap = {};
                        } else {
                            self.orgDispMap = res.data.orgDispMap;
                            self._orgDispMap = angular.copy(res.data.orgDispMap);
                        }
                    }
                    self.search(0);
                });
            },

            byTagChildrenName: function (arr, index) {
                var self = this;
                var key = self.key[index];
                return key ? arr.filter(function (item) {
                    return item.tagChildrenName.indexOf(self.key[index]) > -1;
                }) : arr;
            },

            /**
             * 当用户点击搜索时触发
             * @param index：记录层级
             */
            search: function (index) {
                var self = this;
                var tree = self.tree;
                var source = self.source;
                var selected = self.selected;
                var prev;
                for (; index < 3; index++) {
                    if (!index) {
                        tree[index] = self.byTagChildrenName(source, index);
                    } else {
                        prev = selected[index - 1];
                        if (prev)
                            tree[index] = self.byTagChildrenName(prev.children, index);
                        else {
                            tree[index] = [];
                            continue;
                        }
                    }

                    if (!selected[index]) {
                        selected[index] = tree[index][0];
                    } else {
                        var indexSelected = tree[index].find(function (item) {
                            return item.id === selected[index].id;
                        });
                        if (indexSelected && self.key[index] != "")
                            selected[index] = indexSelected;
                        else
                            selected[index] = tree[index][0];
                    }

                }
            },

            /**
             * 点击保存(需要判断是否有改动,没有则不保存)
             */
            save: function () {
                var self = this;
                var map = flatTrees(self.source);
                var selectdTagList = [];
                var selCounts = 0;

                /**因为半角中的id为全路径，要分割到最后一个，也就是数字为最大的*/
                var orgDispArr = _.map(self.orgDispMap,function(value,key){
                    return {id: _.max(key.split("-")), tagPath: value};
                }).filter(function(item){
                    return item.tagPath;
                });

                var selFlagArr = _.map(self.taglist.selFlag, function (value, key) {
                    return {selectedId: key, selected: value};
                }).filter(function (item) {
                    return item.selected;
                });

                /**在选中的当中半角的数量*/
                _.each(selFlagArr,function(sel){
                    if(_.some(orgDispArr,function(orgDis){
                        return orgDis.id == sel.selectedId;
                    })){
                        selCounts++;
                    }
                });

                /**当是高级检索，查询自由标签时，不检查勾选*/
                if (self.orgFlg == 2) {
                    /**判断是否改变*/
                    var dispFlg = false;
                    var isChgFlg = false;
                    // _orgDispMap和orgDispMap相同，而且_orgChkStsMap和orgChkStsMap相同(若orgChkStsMap中的项目比_orgChkStsMap的多，原先的项目值相同，多出来的项目值都是false，则也认为相同)
                    dispFlg = compareArr(self._orgDispMap, self.orgDispMap);
                    if (dispFlg) {
                        // 遍历所有checkbox，检查其状态是否已与原始值不同
                        for (var key in self.orgChkStsMap) {
                            if (self.orgChkStsMap[key] == true && self._orgChkStsMap[key] == undefined) {
                                isChgFlg = true;
                                break;
                            }
                            if (self.orgChkStsMap[key] != self._orgChkStsMap[key]) {
                                isChgFlg = true;
                                break;
                            }
                        }
                    }
                    if (dispFlg && !isChgFlg) {
                        self.alert("未改变任何标签，无需保存！");
                        return;
                    }

                    // 检查是否还有半选的情况
                    dispFlg = false;
                    for (var key in self.orgDispMap) {
                        if (self.orgDispMap[key] == true) {
                            dispFlg = true;
                            break;
                        }
                    }
                    if (dispFlg) {
                        self.alert("存在冲突标签，请确认标签勾选状态！");
                        return;
                    }
                }

                selFlagArr.forEach(function (item) {
                    var selTagList = map[item.selectedId];
                    var self = this;
                    self.list = {
                        "id": selTagList.id,
                        "tagPathName": selTagList.tagPathName,
                        "tagPath": selTagList.tagPath
                    };
                    selectdTagList.push(self.list);
                });

                self.context = {"selectdTagList": selectdTagList, 'orgFlg': self.orgFlg};
                self.$uibModalInstance.close(self.context);
            },

            // 点击tag的checkbox时的操作
            selOrgDisp:function(id,path,event){
                var self = this;

                /**设置checkbox的选择状态*/
                self.orgChkStsMap[path] = self.taglist.selFlag[id];

                /**记录checkbox的半选状态*/
                if(self.orgDispMap[path]){
                    // 如果初始是半选状态
                    if(self.selOrgDispList.indexOf(path) < 0){
                        self.selOrgDispList.push(path);
                    }
                    self.orgDispMap[path] = false;
                }

                /**防止事件冒泡*/
                event.stopPropagation();
            }
        };

        return popFreeTagCtl;
    })())
});