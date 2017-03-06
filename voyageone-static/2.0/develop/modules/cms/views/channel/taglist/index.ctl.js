/**
 * @description 自由标签设置
 * @author Piao
 */
define([
    'angularAMD',
    'modules/cms/controller/popup.ctl'
], function () {

    function cmsChannelTagController($scope, channelTagService,confirm) {
        $scope.vm = {
            tagTypeSelectValue:"1",
            tagTree: null,
            id: "",
            parentTagId: "",
            tagTypeList:[]
        };
        $scope.tree = [];
        $scope.key = [];
        $scope.selected = [];
        $scope.newIndex = {
            value: -1
        };

        /**
         *初始化数据,上手调用search拼装tree
         */
        $scope.initialize = function () {
            //默认选中店铺类分类
            channelTagService.init({tagTypeSelectValue: $scope.vm.tagTypeSelectValue}).then(function (res) {
                $scope.source = $scope.vm.tagTree = res.data.tagTree;
                $scope.vm.tagTypeList = res.data.tagTypeList;
                $scope.search(0);
            });
        };

        /**
         *
         * @param arr
         * @param index
         * @returns {*}
         */
        function byTagChildrenName(arr, index){
            var key = $scope.key[index];
            return key ? arr.filter(function (item) {
                return item.tagChildrenName.indexOf($scope.key[index]) > -1;
            }) : arr;
        }

        /**
         * 当用户点击搜索时触发
         * @param index：记录层级
         */
        $scope.search = function (index) {
            var tree = $scope.tree;
            var source = $scope.source;
            var selected = $scope.selected;
            var prev;
            for (; index < 3; index++) {
                if (!index) {
                    tree[index] = byTagChildrenName(source, index);
                } else {
                    prev = selected[index - 1];
                    if (prev)
                        tree[index] = byTagChildrenName(prev.children, index);
                    else {
                        tree[index] = [];
                        continue;
                    }
                }

                if (!selected[index]) {
                    selected[index] = tree[index][0];
                } else if (_.isString(selected[index])) {
                    selected[index] = tree[index].find(function(item) {
                        return item.tagChildrenName === selected[index];
                    });
                } else if (tree[index].indexOf(selected[index]) < 0) {
                    var indexSelected = tree[index].find(function (item) {
                        return item.id === selected[index].id;
                    });
                    if (indexSelected)
                        selected[index] = indexSelected;
                    else
                        selected[index] = tree[index][0];
                }
            }
        };

        /**
         * 新增tag操作
         * @param savedata
         */
        $scope.save = function(savedata) {
            //记录新增记录名称
            $scope.selected[$scope.newIndex.value] = savedata.vm.tagPathName;

            channelTagService.save(savedata.vm).then(function (res) {
                    $scope.source = $scope.vm.tagTree = res.data.tagInfo.tagTree;
                    $scope.search(0);
                    savedata.$close();
                },
                function (err) {
                    if (err.message != null) {
                        savedata.vm.errMsg=err.message;
                    }
                    $scope.search(0);
                })
        }

        /**
         * 删除tag操作
         * @param tag
         */
        $scope.delTag = function(tag) {
            $scope.vm.id = tag.id;
            $scope.vm.parentTagId = tag.parentTagId;
            confirm('TXT_MSG_DO_DELETE').then(function () {
                channelTagService.del($scope.vm).then(function (res) {
                    $scope.source = $scope.vm.tagTree = res.data.tagTree;
                    $scope.search(0);
                });
            })
        }


    }

    cmsChannelTagController.$inject = ['$scope', 'channelTagService','confirm'];

    return cmsChannelTagController;
});
