/**
 * @Name:    popMoveToModelController.js
 * @Date:    2015/08/26
 * @User:    sky
 * @Version: 1.0.0
 */
define(function (require) {
    var cmsApp = require('modules/cms/cms.module');

    require ('modules/cms/popup/imgSetting/imgSetting.service');
    require ("components/directives/dialogs/dialogs");

    cmsApp.controller('imgSettingController', ['$scope', 'imgSettingService', 'userService', '$modalInstance', 'notify', 'productId', 'productImages',
        function ($scope, imgSettingService, userService, $modalInstance, notify, productId, productImages) {

        	var _ = require('underscore');
        	$scope.uploader={};
        	$scope.uploader.flow_item=[];
        	$scope.channelId = userService.getSelChannel();
        	//$scope.productId = productId;
        	$scope.productImages = productImages;
        	
        	$scope.data = {};
            $scope.data.channelId= userService.getSelChannel();
            $scope.data.productId= productId;
            $scope.data.delImgList = [];

			/**
			 * 关闭当前窗口
			 */
        	$scope.closeDialog = function() {
        		$modalInstance.dismiss("close");
            };

			// TODO 改成临时删除，然后点save时才执行数据库删除.
        	/**
        	 * 删除已选择的图片
        	 */
        	$scope.delItem = function(files,item,isTallServer){
        		var index = _.indexOf(files,item);
        		if(index > -1){
        			files.splice(index,1);
        		}
        		if(isTallServer == true){
        			$scope.data.delImgList.push(item);
        		}
        	};

			/**
			 *
			 */
        	$scope.save = function () {

        		$scope.data.addImgList=[];
                _.each($scope.uploader.flow_item.files,function (item,i){
                	o={};
                	o.fileBase64 = item.imgData;
                	o.filename = item.name;
                	o.imageTypeId = 1;
                	$scope.data.addImgList.push(o);
                });
                _.each($scope.uploader.flow_box.files,function (item,i){
                	o={};
                	o.fileBase64 = item.imgData;
                	o.filename = item.name;
                	o.imageTypeId = 2;
                	$scope.data.addImgList.push(o);
                });
                _.each($scope.uploader.flow_angel.files,function (item,i){
                	o={};
                	o.fileBase64 = item.imgData;
                	o.filename = item.name;
                	o.imageTypeId = 3;
                	$scope.data.addImgList.push(o);
                });
                _.each($scope.uploader.flow_design.files,function (item,i){
                	o={};
                	o.fileBase64 = item.imgData;
                	o.filename = item.name;
                	o.imageTypeId = 4;
                	$scope.data.addImgList.push(o);
                });                
                // 上传图片
                imgSettingService.doUpdateProductImg ($scope.data)
                    .then (function (data) {
                    	$modalInstance.close("");
                });
            };

        }]);
});