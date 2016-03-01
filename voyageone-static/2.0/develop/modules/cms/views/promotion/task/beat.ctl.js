/**
 * Created by linanbin on 15/12/7.
 */
define([
    'cms',
    'modules/cms/controller/popup.ctl'
], function (cms) {
    cms.controller("taskBeatController", (function () {

        function TaskBeatController($routeParams, taskBeatService, cActions, FileUploader) {
            var urls = cActions.cms.task.taskBeatService;
            
            this.taskBeatService = taskBeatService;

            this.task_id = $routeParams['task_id'];
            this.data = [];
            this.pageOption = {
                curr: 1,
                total: 0,
                size: 20,
                fetch: this.getData.bind(this)
            };
            this.uploader = new FileUploader({
                url: urls.root + "/" + urls.import
            });
        }

        TaskBeatController.prototype = {
            init: function() {
                this.getData();
            },

            importBeat: function() {
                var ttt = this;
                var uploadQueue = ttt.uploader.queue;
                var uploadItem = uploadQueue[uploadQueue.length - 1];
                if (!uploadItem) {
                    alert('没选择文件');
                    return;
                }
                uploadItem.onSuccess = function(res) {
                    alert(res.message || '上传成功');
                    if (res.data) {
                        ttt.data = res.data.list;
                        ttt.pageOption.curr = 1;
                        ttt.pageOption.total = res.data.total;
                    }
                };
                uploadItem.formData = [{
                    task_id: ttt.task_id,
                    size: ttt.pageOption.size
                }];
                uploadItem.upload();
            },

            getData: function () {
                var ttt = this;
                var po = ttt.pageOption;
                var offset = (po.curr - 1) * po.size;
                ttt.taskBeatService.page({
                    task_id: ttt.task_id,
                    offset: offset,
                    size: ttt.pageOption.size
                }).then(function (res) {
                    ttt.data = res.data.list;
                    ttt.pageOption.total = res.data.total;
                })
            }
        };

        return TaskBeatController;

    })())
});