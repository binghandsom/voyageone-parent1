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
            tagSelectObject: null,
            id: "",
            parentTagId: "",
            indexThree:0,
            indexTwo: 0,
        };
        /**
         *初始化数据:
         */
        $scope.initialize = initialize;
        function initialize() {
            //默认选中店铺类分类
            channelTagService.init($scope.vm).then(function (res) {
                $scope.vm.tagTree = res.data;
            });
        };

        /**
         * 删除标签(默认选中第一个标签)
         */
        $scope.delTag = delTag;
        function delTag(tag) {
            $scope.vm.id = tag.id;
            $scope.vm.parentTagId = tag.parentTagId;
            channelTagService.del($scope.vm).then(function (res) {
                $scope.vm = res.data;

            });
        }
        /**
         * 添加标签(默认选中第一个标签)
         */
        $scope.save = save;
        function save(savedata) {
            channelTagService.save(savedata.vm).then(function (res) {
                $scope.vm.tagTree = res.data.tagInfo.tagTree;
                savedata.$close();
            },
            function (err) {
                if (err.message != null) {
                    savedata.vm.errMsg=err.message;
                }
            })
        }
    }
    cmsChannelTagController.$inject = ['$scope', 'channelTagService'];
    //返回的数据类型
    return cmsChannelTagController;
});
