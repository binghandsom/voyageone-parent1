/**
 * Created by linanbin on 15/12/7.
 */
define([
    'angularAMD',
    'modules/cms/controller/popup.ctl',
    'modules/cms/views/promotion/task/stock.ctl'
], function (angularAMD) {

    angularAMD.controller('popFileStockImportCtl', function ($scope,FileUploader,alert,cActions,taskStockService,data,notify) {
            var urls = cActions.cms.task.taskStockService;
            $scope.vm={"messager":""};
            var uploader = $scope.uploader = new FileUploader({
                url: urls.root + "/" + urls.importStockInfo
            });

            var task_id = data.task_id;
            var platformList = JSON.stringify(data.platformList);
            var propertyList = JSON.stringify(data.propertyList);

            $scope.initialize  = function () {
                $scope.import_mode = "2";
            }
            $scope.upload = function(){
                var main = this;
                var uploadQueue = uploader.queue;
                var uploadItem = uploadQueue[uploadQueue.length - 1];
                if (!uploadItem) {
                    return alert('TXT_MSG_NO_UPLOAD');
                }
                this.uploadItem = uploadItem;
                uploadItem.onSuccess = function (res) {
                    if (res.message) {
                        alert(res.message);
                        return;
                    }
                    if (main.import_mode == "2") {
                        notify.success('TXT_MSG_UPDATE_SUCCESS');
                    } else {
                        notify.success('TXT_MSG_INSERT_SUCCESS');
                    }
                    $scope.$close();
                };
                uploadItem.formData = [{"task_id":task_id,"platformList":platformList,"propertyList":propertyList,"import_mode":main.import_mode}];
                uploadItem.upload();
                $scope.vm.messager ="reading...";
            }

            uploader.onProgressItem = function(fileItem, progress) {
                console.info('onProgressItem', fileItem, progress);
            };

            uploader.onSuccessItem = function(fileItem, response, status, headers) {
                console.info('onSuccessItem', fileItem, response, status, headers);
                if(response.data){
                    if(response.data.fail.length == 0){
                        $scope.$close();
                    }
                    $scope.vm.messager ="读入完毕！成功"+response.data.succeed.length +"条  失败"+response.data.fail.length +"条";
                }else{
                    $scope.vm.messager =response.code;
                }

            };
        })
});