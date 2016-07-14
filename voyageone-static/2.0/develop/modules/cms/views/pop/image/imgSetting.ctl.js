/**@Description 支持图片预览（html5支持下）多个图片上传
 * 				图片大小不许超过5M，最多10张
 * @Date:    2016-07-12
 * @User:    piao wenjie
 * @Version: 2.3.0
 */
define([
	'angularAMD',
	'modules/cms/controller/popup.ctl'
], function (angularAMD) {

	angularAMD.controller('popImgSettingCtl', (function(){

		function PopImgSettingCtl($uibModalInstance,FileUploader,context,blockUI){
			this.$uibModalInstance = $uibModalInstance;
			this.FileUploader = FileUploader;
			this.blockUI = blockUI.instances.get('imgUpload');
			this.context = context;
			this.vm = {};
			this.uploader = null;
			this.files = [];
		}

		PopImgSettingCtl.prototype = {
			init:function(){
				var self = this;
				var upLoader = new this.FileUploader({
					url: '/cms/pop/image_setting/uploadImage'
				});

				upLoader.onSuccessItem = function(fileItem, response) {

					if(response.data){

						response.data.imageType = self.context.imageType;
						self.files.push(response.data);

					}else{
						fileItem.message =  response;
					}

					if(self.files.length == self.vm.total)
						self.blockUI.stop();

				};

				upLoader.onBeforeUploadItem = function(fileItem){
					console.log(fileItem);
					var _idx =  fileItem._idx;
					if(_idx > 0){
						if(self.uploader.queue[_idx-1].message != null){
							self.blockUI.stop();
							throw "上一张图片上传错误!";
						}
					}
				};

				this.uploader = upLoader;

			},
			upload:function(){
				var self = this;

				//记录要上传的总数
				self.vm.total = self.uploader.queue.length;

				if(self.uploader.queue.length != 0) {

					self.blockUI.start("图片上传中。。。请耐心等待！");

					var data = [{
						"productId": self.context.productId,
						"imageType": self.context.imageType
					}];

					angular.forEach(self.uploader.queue , function(item,index){
						item._idx = index;
						item.formData = data;
						item.upload();

					});

				}
			}
		};

		return PopImgSettingCtl;
	})());
});