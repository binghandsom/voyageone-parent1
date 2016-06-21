/**
 * Created by 123 on 2016/4/18.
 */
define([
    'angularAMD',
    'modules/cms/controller/popup.ctl'
], function (angularAMD) {

    angularAMD.controller('popAddTagListCtl', function ($scope,channelTagService,context,alert) {
        //定义常量
        $scope.vm = {
            tagInfo:null,
            tagPath:null,
            tagPathName:null,
            tagSelectObject:null,
            first:null
        };
        //页面初始化
        $scope.initialize =initialize;
        function initialize(){
            $scope.vm.tagInfo=context.tagInfo;
            $scope.vm.first=context.first;

            if(context.tagSelectObject){
                //页面有数据的情况
                if(context.first==true){
                    $scope.vm.tagPath="";
                }else{
                    $scope.vm.tagPath=context.tagSelectObject.tagPathName;
                }
            }else{
                //页面没有数据的情况
                if(context.first==true){
                    $scope.vm.tagPath="";
                }else{
                    $scope.$dismiss();
                    alert('TXT_MSG_TAG');
                }
            }
        }
        //Save保存按钮
        $scope.save =save;
        function save(){
            //取得当前管理标签的的数据
            $scope.vm.tagSelectObject=context.tagSelectObject;
            context.save($scope);
        }
    });
});