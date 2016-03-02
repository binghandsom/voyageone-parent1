/**
 * Created by linanbin on 15/12/7.
 */
define([
    'cms',
    'modules/cms/controller/popup.ctl'
], function (cms) {
    cms.controller("taskBeatController", (function () {

        function TaskBeatController($routeParams, taskBeatService, cActions, FileUploader, alert, notify, $location) {
            var urls = cActions.cms.task.taskBeatService;
            var task_id = $routeParams['task_id'];
            if (!task_id)
                alert('地址错误').result.then(function() {
                    $location.path('/promotion/task');
                });
            task_id = parseInt(task_id);

            this.taskBeatService = taskBeatService;
            this.alert = alert;
            this.notify = notify;

            this.task_id = task_id;
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
            this.downloadUrl = urls.root + "/" + urls.download;
            this.summary = {};
        }

        TaskBeatController.prototype = {
            init: function () {
                this.getData();
            },

            importBeat: function () {
                var ttt = this;
                var uploadQueue = ttt.uploader.queue;
                var uploadItem = uploadQueue[uploadQueue.length - 1];
                if (!uploadItem) {
                    return ttt.alert('没选择文件');
                }
                uploadItem.onSuccess = function (res) {
                    if (res.message)
                        return ttt.alert(res.message);
                    ttt.notify.success('上传成功');
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

            getData: function (flag) {
                var ttt = this;
                var po = ttt.pageOption;
                if (flag) po.curr = 1;
                var offset = (po.curr - 1) * po.size;
                ttt.taskBeatService.page({
                    task_id: ttt.task_id,
                    flag: flag,
                    offset: offset,
                    size: ttt.pageOption.size
                }).then(function (res) {
                    ttt.data = res.data.list;
                    ttt.pageOption.total = res.data.total;
                    ttt.summary = res.data.summary;
                })
            },

            download: function () {
                var ttt = this;
                $.download.post(ttt.downloadUrl, {task_id: ttt.task_id});
            },

            controlOne: function(beat_id, flag) {
                var ttt = this;
                ttt.taskBeatService.control({
                    beat_id: beat_id,
                    flag: flag
                }).then(function(res) {
                    if (!res.data)
                        return ttt.alert('失败了');
                    ttt.getData();
                });
            },

            controlAll: function(flag) {
                var ttt = this;
                ttt.taskBeatService.control({
                    task_id: ttt.task_id,
                    flag: flag
                }).then(function(res) {
                    if (!res.data)
                        return ttt.alert('失败了');
                    ttt.getData();
                });
            }
        };

        return TaskBeatController;

    })())
});