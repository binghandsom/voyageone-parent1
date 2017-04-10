/**
 * Created by sofia on 5/19/2016.
 * @author tony-piao
 * @description 不会触发保存接口，把信息返回给调用者
 * @version 2.1.0
 */
define([
    'cms'
], function (cms) {

    /**将树递归拍平*/
    function flatCategories(categories, parent) {
        return categories.reduce(function (map, curr) {
            curr.parent = parent;
            map[curr.catId] = curr;
            if (curr.children && curr.children.length)
                map = angular.extend(map, flatCategories(curr.children, curr));
            return map;
        }, {});
    }

    /**判断是否可以模糊匹配上*/
    function canMatchNode(treeNode, catName) {

        if (treeNode.catName.indexOf(catName) >= 0)
            return true;
        else {
            if (!treeNode.children && !treeNode.children.length)
                return false;

            return _.some(treeNode.children, function (item) {
                return canMatchNode(item, catName);
            });
        }

    }

    cms.controller('popAddChannelCategoryCtrl', (function () {

        function PopAddChannelCategoryCtrl(context, $rootScope, $addChannelCategoryService, notify, alert, $uibModalInstance, $translate) {
            this.cartName = '';
            this.channelCategoryList = null;
            this.orgCategoryList = null;
            this.isSelectCid = [];
            this.orgChkStsMap = {};
            this.orgDispMap = {};
            this._orgChkStsMap = {};
            this._orgDispMap = {};
            this.cartId = context.cartId == null ? $rootScope.platformType.cartId.toString() : context.cartId;
            this.cnt = "";
            this.addChannelCategoryService = $addChannelCategoryService;
            this.$uibModalInstance = $uibModalInstance;
            this.notify = notify;
            this.checkedCountValid = false;
            this.cartIdValid = false;
            this._context = context;
            this.needSave = false;
            this.translate = $translate;
            this.alert = alert;
        }

        PopAddChannelCategoryCtrl.prototype.init = function () {
            var self = this;
            if (self.cartId == null) {
                self.cartId = 0;
            }
            self.addChannelCategoryService.init({
                "code": self._context.productIds,
                "cartId": self.cartId,
                "isSelAll": self._context.isSelAll,
                'isQuery': self._context.isQuery,
                'searchInfo': self._context.searchInfo
            }).then(function (res) {
                //默认对打钩的数目和店铺渠道选择的验证处于隐藏状态
                self.checkedCountValid = false;
                self.cartIdValid = false;
                self.cartName = res.data.cartName;
                self.cnt = res.data.cnt;
                // 如果店铺渠道选择master或feed，不显示分类列
                if (self.cartId == 0 || self.cartId == 1) {
                    self.isSelectCid = [];
                    self.channelCategoryList = null;
                    return;
                }
                self.orgChkStsMap = self._context.plateSchema ? self._context.selectedIds : res.data.orgChkStsMap;
                self.orgDispMap = res.data.orgDispMap;
                self._orgChkStsMap = angular.copy(res.data.orgChkStsMap);
                self._orgDispMap = angular.copy(res.data.orgDispMap);
                //self.isSelectCid = self._context.plateSchema ? self._context.selectedIds:res.data.isSelectCid;
                self.channelCategoryList = self.orgCategoryList =  res.data.channelCategoryList;
            });
        };

        PopAddChannelCategoryCtrl.prototype.save = function () {
            var self = this;

            if (!self._context.isQuery && !self.needSave) {
                self.alert("店铺内分类没有改变，不需要保存");
                return;
            }

            //save保存时，如果店铺渠道选择的是master或feed，则显示警告：操作无效
            if (self.cartId == 1 || self.cartId == 0) {
                self.cartIdValid = true;
                return;
            }
            var cIds = [], cNames = [], fullCNames = [], fullCIds = [];
            var map = flatCategories(self.channelCategoryList);

            _.map(self.orgChkStsMap, function (value, key) {
                return {categoryId: key, selected: value};
            }).filter(function (item) {
                return item.selected;
            }).forEach(function (item) {
                var category = map[item.categoryId];
                //查询叶子节点
                if (category && category.isParent == 0) {
                    if (fullCNames.indexOf(category.catPath) < 0) fullCNames.push(category.catPath);
                    if (fullCIds.indexOf(category.fullCatId) < 0)  fullCIds.push(category.fullCatId);
                }
                //查询叶子节点和父节点
                while (category) {
                    if (cIds.indexOf(category.catId) < 0) cIds.push(category.catId);
                    if (cNames.indexOf(category.catName) < 0) cNames.push(category.catName);
                    category = category.parent;
                }
            });
            //save保存时，如果类目打钩数目超过cnt的值，则显示警告：超过最大值了     可设置数应该小于等于10
            self.cnt = 10;
            if (fullCIds.length > self.cnt) {
                self.alert(self.translate.instant("MAX_SELLER_CAT_CNT") + self.cnt);
                return;
            }

            var sellerCats = [];
            angular.forEach(fullCIds, function (item, index) {
                var cids = item.split("-");
                var cid = cids[cids.length - 1];
                var cNames = fullCNames[index].split(">");
                var cName = cNames[cNames.length - 1];
                sellerCats.push({cId: cid, cIds: cids, cName: fullCNames[index], cNames: cNames});
            });

            var returnObj = {sellerCats: sellerCats, cartId: self.cartId};
            // 处于半选状态的分类ID
            returnObj._orgDispList = _.map(self.orgDispMap, function (value, key) {
                if (value) {
                    return key;
                }
            }).filter(function (item) {
                return item != undefined && item != null && item != '';
            });
            self.$uibModalInstance.close(returnObj);
        };

        PopAddChannelCategoryCtrl.prototype.chkSave = function (cId, obj) {
            var self = this;
            var boxObj = obj.target;
            var isUpd = false;
            // 先判断该checkbox的状态是否与原始值不同
            if (boxObj.indeterminate != self._orgDispMap[cId]) {
                // 这里肯定是原始值为半选的状态
                self.orgDispMap[cId] = false;
                isUpd = true;
            } else {
                if (boxObj.checked != self._orgChkStsMap[cId]) {
                    isUpd = true;
                }
            }
            if (isUpd) {
                if (self.needSave) {
                    return;
                } else {
                    self.needSave = true;
                    return;
                }
            } else {
                // 遍历所有checkbox，检查其状态是否已与原始值不同
                for (var key in self.orgDispMap) {
                    if (self.orgDispMap[key] != self._orgDispMap[key]) {
                        isUpd = true;
                        break;
                    }
                }
                if (isUpd) {
                    self.needSave = true;
                    return;
                }
                for (var key in self.orgChkStsMap) {
                    if (self.orgChkStsMap[key] != self._orgChkStsMap[key]) {
                        isUpd = true;
                        break;
                    }
                }
                self.needSave = isUpd;
            }
        };

        PopAddChannelCategoryCtrl.prototype.searchByName = function () {
            var self = this,
                filterArr = [];

            if(!self.searchName){
                self.channelCategoryList = self.orgCategoryList;
                return;
            }

            angular.forEach(self.orgCategoryList,function(element){
                if(canMatchNode(element,self.searchName))
                    filterArr.push(element);
            });

            self.channelCategoryList = filterArr;
        };

        return PopAddChannelCategoryCtrl;

    })());
});