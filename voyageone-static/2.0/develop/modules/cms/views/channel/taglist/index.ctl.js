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
            tagTree2:null,
            tagTree3:null,
            tagSelectObject: null,
            id: "",
            parentTagId: "",
            indexThree:0,
            indexTwo: 0,
            tagOneText : "",
            tagTwoText : "",
            tagThreeText:"",
            selectedId1:0,
            selectedId2:0,
            selectedId3:0,
            childs:[]
        };
        /**
         *初始化数据:
         */
        $scope.initialize = initialize;
        function initialize() {
            //默认选中店铺类分类
            channelTagService.init($scope.vm).then(function (res) {
                $scope.vm.tagTree = res.data;
                $scope.vm.tagTree2 = $scope.vm.tagTree[0];
                $scope.vm.tagTree3 = $scope.vm.tagTree[0].children[0];

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

                if($scope.vm.selectedId1 == 0)
                    $scope.vm.tagTree2 = $scope.vm.tagTree[0];
                else
                {
                    $scope.vm.childs = [];
                    dGselect($scope.vm.selectedId1);
                    $scope.vm.tagTree2 = $scope.vm.childs;
                }
                if($scope.vm.selectedId2 == 0)
                    $scope.vm.tagTree3 = $scope.vm.tagTree2.children[0];
                else
                {
                    $scope.vm.childs = [];
                    dGselect($scope.vm.selectedId2);
                    $scope.vm.tagTree3 = $scope.vm.childs;
                }

            });
        }
        /**
         * 添加标签(默认选中第一个标签)
         */
        $scope.save = save;
        function save(savedata) {
            channelTagService.save(savedata.vm).then(function (res) {
                $scope.vm.tagTree = res.data.tagInfo.tagTree;

                if($scope.vm.selectedId1 == 0)
                    $scope.vm.tagTree2 = $scope.vm.tagTree[0];
                else
                {
                    $scope.vm.childs = [];
                    dGselect($scope.vm.selectedId1);
                    $scope.vm.tagTree2 = $scope.vm.childs;
                }
                if($scope.vm.selectedId2 == 0)
                    $scope.vm.tagTree3 = $scope.vm.tagTree2.children[0];
                else
                {
                    $scope.vm.childs = [];
                    dGselect($scope.vm.selectedId2);
                    $scope.vm.tagTree3 = $scope.vm.childs;
                }

                savedata.$close();
            },
            function (err) {
                if (err.message != null) {
                    savedata.vm.errMsg=err.message;
                }
            })
        }
        $scope.tagOneFilter = tagOneFilter;
        $scope.tagTwoFilter = tagTwoFilter;
        $scope.tagThreeFilter = tagThreeFilter;
        $scope.findChilds = findChilds;

        function tagOneFilter(item){
            var searText = $scope.vm.tagOneText;
            if(searText == "" || searText == null)
                return true;
            if(item.tagPathName.indexOf($scope.vm.tagOneText) >= 0)
            {
                $scope.vm.tagTree2 = item;
                $scope.vm.tagTree3 = item.children[0];
                return true;
            }
        }

        function tagTwoFilter(item){
            var searText = $scope.vm.tagTwoText;
            if(searText == "" || searText == null)
                return true;
            if(item.tagPathName.indexOf($scope.vm.tagTwoText) >= 0)
            {
                $scope.vm.tagTree3 = item;
                return true;
            }
        }

        function tagThreeFilter(item){
            var searText = $scope.vm.tagThreeText;
            if(searText == "" || searText == null && $scope.vm.tagOneText == "")
                return true;
            if(item.tagPathName.indexOf($scope.vm.tagThreeText) >= 0)
            {
                return true;
            }
        }

        function findChilds(index,level){

            //首先清空子集合
            $scope.vm.childs = [];
            if(level != 3)
                dGselect(index);
            switch(level){
                case 1:
                    $scope.vm.selectedId1 = index;
                    $scope.vm.tagTree2 = $scope.vm.childs;
                    $scope.vm.tagTree3 = $scope.vm.tagTree2.children[0];
                    break;
                case 2:
                    $scope.vm.selectedId2 = index;
                    $scope.vm.tagTree3 = $scope.vm.childs;
                    break;
                case 3:
                    $scope.vm.selectedId3 = index;
                    break;
            }
        }

        /**
         * 递归查找该index下的子集合
         * @param index 下行index属性
         * @param data 下行集合
         */
        function dGselect(index,data){
            data = data == null ? $scope.vm.tagTree : data;
            //递归访问子属性
            for(var i=0,length=data.length;i<length;i++){
                 if(data[i].id == index)
                 {
                     $scope.vm.childs = data[i];
                 }
                dGselect(index,data[i].children);
            }
        }
    }

    cmsChannelTagController.$inject = ['$scope', 'channelTagService'];
    //返回的数据类型
    return cmsChannelTagController;
});
