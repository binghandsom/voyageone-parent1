/**
 * Created by 123 on 2016/4/18.
 */
define([
    'angularAMD',
    'modules/cms/controller/popup.ctl'
], function () {

    function cmsChannelTagController($scope, channelTagService) {
        $scope.vm = {
            tagTypeSelectValue:"1",
            tagTree: null,
            id: "",
            parentTagId: ""
        };

        $scope.tree = [];
        $scope.key = [];

        /**
         *初始化数据:
         */
        $scope.initialize = function () {
            //默认选中店铺类分类
            channelTagService.init({tagTypeSelectValue: $scope.vm.tagTypeSelectValue}).then(function (res) {
                $scope.source = $scope.vm.tagTree = res.data;
                $scope.search(0);
            });
        };

        function byTagChildrenName(arr, index){
            var key = $scope.key[index];
            return key ? arr.filter(function (item) {
                return item.tagChildrenName.indexOf($scope.key[index]) > -1;
            }) : arr;
        }

        $scope.search = function (index) {
            var tree = $scope.tree;
            var source = $scope.source;
            var prev;
            for (; index < 3; index++) {
                if (!index) {
                    tree[index] = byTagChildrenName(source, index);
                } else {
                    prev = tree[index - 1].selected;
                    if (prev)
                        tree[index] = byTagChildrenName(prev.children, index);
                    else
                        return;
                }
                tree[index].selected = tree[index][0];
            }
        };

        $scope.save = save;
        function save(savedata) {
            channelTagService.save(savedata.vm).then(function (res) {
                    $scope.source = $scope.vm.tagTree = res.data.tagInfo.tagTree;
                    //$scope.search(0);
                    savedata.$close();
                },
                function (err) {
                    if (err.message != null) {
                        savedata.vm.errMsg=err.message;
                    }
                })
        }

        $scope.delTag = delTag;
        function delTag(tag) {
            $scope.vm.id = tag.id;
            $scope.vm.parentTagId = tag.parentTagId;
            channelTagService.del($scope.vm).then(function (res) {
                $scope.source = $scope.vm.tagTree = res.data.tagTree;
                //$scope.search(0);
            });
        }
    }

    cmsChannelTagController.$inject = ['$scope', 'channelTagService'];

    return cmsChannelTagController;
});
