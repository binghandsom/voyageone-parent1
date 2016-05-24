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
            this.task = null;
            this.$timeout = $timeout;
            this.searchKey = null;
        }

        TaskBeatController.prototype = {
            init: function () {
                this.getData();
            },

            importBeat: function () {
                var ttt = this;
                var uploadQueue = ttt.uploader.queue;
                var uploadItem = uploadQueue[uploadQueue.length - 1];
                var uploadIt = function () {
                    ttt.uploadItem = uploadItem;
                    uploadItem.onSuccess = function (res) {
                        ttt.$timeout(function () {
                            ttt.uploadItem = null;
                        }, 500);
                        if (res.message) {
                            ttt.alert(res.message);
                            ttt.data = [];
                            ttt.pageOption.curr = 1;
                            ttt.pageOption.total = 0;
                            return;
                        }
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
                };
                if (!uploadItem) {
                    return ttt.alert('TXT_MSG_NO_UPLOAD');
                }
                if (!ttt.data.length) {
                    uploadIt();
                    return;
                }
                ttt.confirm('TXT_MSG_REIMPORT_BEAT').result.then(uploadIt);
            },

            getData: function () {
                var self = this;
                var po = self.pageOption;
                var offset;

                // 如果搜索的状态变更了。则需要重置页数到第一页
                if (self.lastFlag !== self.flag) {
                    self.lastFlag = self.flag;
                    po.curr = 1;
                }

                // 计算偏移量
                offset = (po.curr - 1) * po.size;

                self.taskBeatService.page({
                    task_id: self.task_id,
                    flag: self.flag,
                    searchKey: self.searchKey,
                    offset: offset,
                    size: self.pageOption.size
                })
                    .then(function (res) {

                        var map = res.data;

                        self.data = map.list;
                        self.pageOption.total = map.total;
                        self.summary = map.summary;
                        self.task = map.task;
                    })
            },

            download: function () {
                var ttt = this;
                $.download.post(ttt.downloadUrl, {task_id: ttt.task_id});
            },

            controlOne: function (beatInfo, flag) {
                var ttt = this;
                var beat_id = beatInfo.id;
                var changeIt = function () {
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
                ttt.confirm('TXT_MSG_ERROR_BEAT_ITEM').result.then(changeIt);
            },

            controlAll: function (flag) {

                var self = this;

                // 在统计信息中查找错误的统计
                var errorSummary = self.summary.find(function (item) {
                    return item.flag === 'CANT_BEAT';
                });

                // 如果错误统计有数据, 说明是存在错误数据的
                // 就需要人为来确定是否要强制处理这些任务
                if (errorSummary && errorSummary.count) {
                    self.confirm('是否同时处理那些 Promotion 信息不协同的任务?')
                        .result
                        .then(function () {
                            self.$controlAll(true, flag);
                        }, function () {
                            self.$controlAll(false, flag);
                        });
                    return;
                }

                // 否则, 直接处理即可
                self.$controlAll(false, flag);
            },

            $controlAll: function (force, flag) {
                var self = this;

                self.taskBeatService.control({
                    task_id: self.task_id,
                    force: force,
                    flag: flag
                }).then(function (res) {
                    if (!res.data)
                        return self.alert('TXT_MSG_UPDATE_FAIL');
                    self.getData();
                });
            },

            updateTask: function (openNewBeatTask) {
                var self = this;
                openNewBeatTask({task: self.task}).then(function(newTask) {
                    self.task = newTask;
                });
            }
        };

        return TaskBeatController;

    })())
});