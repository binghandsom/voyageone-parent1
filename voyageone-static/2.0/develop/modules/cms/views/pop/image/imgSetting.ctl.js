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

		function PopImgSettingCtl($uibModalInstance,FileUploader,context,blockUI,alert){
			this.uibModalInstance = $uibModalInstance;
			this.FileUploader = FileUploader;
			this.blockUI = blockUI.instances.get('imgUpload');
			this.alert = alert;
			this.context = context;
			this.vm = {
				total:null,
				reUpload:false
			};
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
						response.data.imageType = self.context.imageType.replace("image","images");
						fileItem.uploaded = true;
						fileItem.message = null;
						self.files.push(response.data);
					}else{
						fileItem.message =  "可能由于网络原因上传异常，请再试";
						self.blockUI.stop();
						//fileItem.uploaded = true;
					}

					if(self.files.length == self.vm.total){
						self.blockUI.stop();
						self.uibModalInstance.close(self.files);
					}


				};

				upLoader.onBeforeUploadItem = function(fileItem){

					if(fileItem._file.size > 5*1024*1024){
						self.alert(fileItem._file.name + "的图片大小大于5M");
						fileItem.message = fileItem._file.name + "的图片大小大于5M";
						self.vm.reUpload = true;
						self.blockUI.stop();
						throw "图片过大错误!";
					}

					if(fileItem.uploaded)
						throw "已上传!";

					var _idx =  fileItem._idx;
					if(_idx > 0){
						var preItem = self.uploader.queue[_idx-1];
						if(preItem.message != null){
							self.blockUI.stop();
							self.vm.reUpload = true;
							fileItem.message = "等待上传！";
							throw "上一张图片上传错误!";
						}
					}
				};

				upLoader.filters.push({name:'filterName', fn:function(fileItem) {

					return !_.any(upLoader.queue, function (addedFileItem) {
						return addedFileItem._file.name === fileItem.name;
					});

				}});

				this.uploader = upLoader;

			},
			upload:function(){
				var self = this;

				//记录要上传的总数
				self.vm.total = self.uploader.queue.length;

				if(self.uploader.queue.length != 0) {

					self.blockUI.start("图片上传中。。。请耐心等待！");

					//平台级产品图片上传才会有cartId
					var data = [{
						productId: self.context.productId,
						imageType: self.context.imageType,
                        cartId:self.context.cartId ? self.context.cartId : 0
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