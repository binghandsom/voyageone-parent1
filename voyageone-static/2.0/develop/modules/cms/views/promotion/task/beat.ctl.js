/**
 * Created by linanbin on 15/12/7.
 */
define([
    'cms',
    'underscore',
    'modules/cms/controller/popup.ctl'
], function (cms, _) {
    cms.controller("taskBeatController", (function () {

        function TaskBeatController($routeParams, taskBeatService, cActions, FileUploader, alert, confirm, notify, $location, $timeout) {
            var urls = cActions.cms.task.taskBeatService;
            var task_id = parseInt($routeParams['task_id']);
            if (_.isNaN(task_id)) {
                this.init = null;
                alert('TXT_MSG_UNVALID_URL').result.then(function () {
                    $location.path('/promotion/task');
                });
            }

            this.taskBeatService = taskBeatService;
            this.alert = alert;
            this.notify = notify;
            this.confirm = confirm;

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
            this.uploadItem = null;
            this.downloadUrl = urls.root + "/" + urls.download;
            this.summary = {};
            this.$timeout = $timeout;
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
                    return ttt.alert('TXT_MSG_NO_UPLOAD');
                }
                ttt.uploadItem = uploadItem;
                uploadItem.onSuccess = function (res) {
                    ttt.$timeout(function () {
                        ttt.uploadItem = null;
                    }, 500);
                    if (res.message)
                        return ttt.alert(res.message);
                    ttt.notify.success('TXT_MSG_UPDATE_SUCCESS');
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

            controlOne: function (beatInfo, flag) {
                var ttt = this;
                var beat_id = beatInfo.id;
                var changeIt = function() {
                    ttt.taskBeatService.control({
                        beat_id: beat_id,
                        flag: flag
                    }).then(function (res) {
                        if (!res.data)
                            return ttt.alert('TXT_MSG_UPDATE_FAIL');
                        ttt.getData();
                    });
                };
                if (beatInfo.beatFlag !== 'CANT_BEAT') {
                    changeIt();
                    return;
                }
                ttt.confirm('这个任务的商品信息好像有问题? 你确定要修改它的状态?')
                    .result
                    .then(changeIt);
            },

            controlAll: function (flag) {
                var ttt = this;
                ttt.taskBeatService.control({
                    task_id: ttt.task_id,
                    flag: flag
                }).then(function (res) {
                    if (!res.data)
                        return ttt.alert('TXT_MSG_UPDATE_FAIL');
                    ttt.getData();
                });
            }
        };

        return TaskBeatController;

    })())
});