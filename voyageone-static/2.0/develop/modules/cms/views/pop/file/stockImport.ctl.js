/**
 * Created by linanbin on 15/12/7.
 */
define([
    'angularAMD',
    'modules/cms/controller/popup.ctl',
    'modules/cms/views/promotion/task/stock.ctl'
], function (angularAMD) {

    angularAMD.controller('popFileStockImportCtl', function ($scope,FileUploader,confirm,alert,cActions,data,notify,blockUI) {
            var task_id = data.task_id;
            var subTaskId = data.subTaskId;
            var parent_id = data.parent_id;
            var platformList = JSON.stringify(data.platformList);
            var propertyList = JSON.stringify(data.propertyList);
            var blockUI = blockUI;

            var urls;
            if (parent_id == '1') {
                // 隔离导入
                urls = cActions.cms.task.taskStockService;
            }
            if (parent_id == '2') {
                // 增量导入
                urls = cActions.cms.task.taskStockIncrementDetailService;
            }
            $scope.vm={"messager":""};
            var uploader = $scope.uploader = new FileUploader({
                url: urls.root + "/" + urls.importStockInfo
            });

            $scope.initialize  = function () {
                $scope.import_mode = "2";
                $scope.parent_id = parent_id;
            }
            $scope.upload = function() {
                var main = this;
                var uploadQueue = uploader.queue;
                var uploadItem = uploadQueue[uploadQueue.length - 1];
                if (!uploadItem) {
                    return alert('TXT_MSG_NO_UPLOAD');
                }
                var uploadIt = function () {
                    this.uploadItem = uploadItem;
                    uploadItem.onSuccess = function (res) {
                        blockUI.stop();
                        if (res.message) {
                            $scope.vm.messager = res.message;
                            alert(res.message);
                            return;
                        }
                        if (main.import_mode == "1") {
                            notify.success('TXT_MSG_INSERT_SUCCESS');
                        } else {
                            notify.success('TXT_MSG_UPDATE_SUCCESS');
                        }
                        if (res.data.hasExecutingData) {
                            alert('TXT_MSG_IMPORT_STATUS_ERROR');
                        }
                        $scope.$close();
                    };
                    uploadItem.formData = [{
                        "task_id": task_id,
                        "subTaskId": subTaskId,
                        "platformList": platformList,
                        "propertyList": propertyList,
                        "import_mode": main.import_mode
                    }];
                    uploadItem.upload();
                    $scope.vm.messager = "reading...";
                    blockUI.start();
                };
                if (main.import_mode == "3") {
                    confirm('TXT_MSG_IMPORT_DELETE_UPDATE_MSGBOX').result.then(uploadIt);
                } else {
                    uploadIt();
                }
            }

            uploader.onProgressItem = function(fileItem, progress) {
                console.info('onProgressItem', fileItem, progress);
            };

            //uploader.onSuccessItem = function(fileItem, response, status, headers) {
            //    console.info('onSuccessItem', fileItem, response, status, headers);
            //    if(response.data){
            //        if(response.data.fail.length == 0){
            //            $scope.$close();
            //        }
            //        $scope.vm.messager ="读入完毕！成功"+response.data.succeed.length +"条  失败"+response.data.fail.length +"条";
            //    }else{
            //        $scope.vm.messager =response.code;
            //    }
            //
            //};
        })
});