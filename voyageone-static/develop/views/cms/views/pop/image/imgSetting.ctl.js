/**
 * @Name:    popMoveToModelController.js
 * @Date:    2015/08/26
 * @User:    sky
 * @Version: 1.0.0
 */
define([
	'angularAMD',
	'modules/cms/controller/popup.ctl'
], function (angularAMD) {

	angularAMD.controller('popImgSettingCtl', function ($scope,FileUploader, context) {


		$scope.img = {};
		$scope.vm = {};
		var uploader = $scope.uploader = new FileUploader({
			url: '/cms/pop/image_setting/uploadImage'
		});
		$scope.initialize  = function () {

		}
		$scope.upload = function(){
			if(uploader.queue.length != 0) {
				uploader.queue[uploader.queue.length - 1].formData = [{
					"productId": context.product.productId,
					"imageType": context.imageType
				}];
				uploader.queue[uploader.queue.length - 1].upload();
			}
			$scope.vm.messager ="上传中";
		}

		uploader.onProgressItem = function(fileItem, progress) {
			//console.info('onProgressItem', fileItem, progress);
		};

		uploader.onSuccessItem = function(fileItem, response, status, headers) {
			//console.info('onSuccessItem', fileItem, response, status, headers);
			if(response.data){
				response.data.imageType = context.imageType;
				$scope.$close(response.data);
				//$translate({"imageType":"image"+imageType, "base64":response.data.base64, "imageName":response.data.imageName})
				//$scope.img = "data:image/jpg;base64,"+response.data.base64;
			}else{
				$scope.vm.messager = response;
			}

		};
	});
});