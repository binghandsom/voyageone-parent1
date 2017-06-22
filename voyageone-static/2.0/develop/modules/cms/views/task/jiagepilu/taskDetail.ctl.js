/**
 * 价格披露Task详情页面 create By rex.wu at 2017-06-21 11:22:00
 */
define([
    'cms',
    'underscore',
    'modules/cms/controller/popup.ctl'
], function (cms, _) {
    cms.controller("jiagepiluController", (function () {

        function JiagepiluController($routeParams, taskJiagepiluService, $translate, taskBeatService, cActions, FileUploader, alert, confirm, notify, $location, $timeout) {

            var urls = cActions.cms.task.taskJiagepiluService;
            this.urls = urls;
            var taskId = parseInt($routeParams['taskId']);
            taskId = 460;
            console.log(taskId);
            if (_.isNaN(taskId)) {
                this.init = null;
                alert('TXT_MSG_UNVALID_URL').then(function () {
                    $location.path('/promotion/task');
                });
            }

            this.config = {
                open:true
            };

            this.taskBeatService = taskBeatService;
            this.alert = alert;
            this.notify = notify;
            this.confirm = confirm;


            this.taskJiagepiluService = taskJiagepiluService;
            this.$translate = $translate;

            this.taskId = taskId; // 价格披露任务ID
            this.task = {};       // 价格披露Task
            this.searchBean = {
                taskId:this.taskId
            };     // 价格披露商品搜索条件
            this.productList = [];// 价格披露Task内商品
            this.pageOption = {
                curr: 1,
                total: 0,
                size: 20,
                fetch: this.getData.bind(this)
            };
            this.importInfoList = [];
            this.uploader = new FileUploader({
                url: urls.root + "/" + urls.import
            });
            this.uploadItem = null;
            this.downloadUrl = urls.root + "/" + urls.download;

            this.beatFlags = [];
            this.imageStatuses = [];

            this.summary = {};
            this.$timeout = $timeout;
            this.searchKey = null;
        }

        JiagepiluController.prototype = {
            init: function () {
                this.getData();
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

                // 获取价格披露任务Model
                self.taskJiagepiluService.getTaskModel({taskId:self.taskId}).then(function (resp) {
                    if (resp.data && resp.data.task) {
                        self.task = resp.data;

                        var beatFlagArray = _.map(resp.data.beatFlags,function (element) {
                            var _obj = {key:element,value: self.$translate.instant(element)};
                            // _obj[element] = self.$translate.instant(element);
                            return _obj;
                        });
                        self.beatFlags = beatFlagArray;

                        var imageStatusArray = _.map(resp.data.imageStatuses,function (element) {
                            var _obj = {key:element,value: self.$translate.instant(element)};
                            return _obj;
                        });
                        self.imageStatuses = imageStatusArray;

                    }
                });


                var searchBean = angular.copy(self.searchBean);
                if (searchBean.beatFlags && _.size(searchBean.beatFlags) > 0) {
                    var beatFlagObj = _.pick(searchBean.beatFlags, function (value, key, object) {
                        return value;
                    });
                    var beatFlags = _.keys(beatFlagObj);
                    _.extend(searchBean, {"beatFlags":beatFlags});
                }

                if (searchBean.imageStatuses && _.size(searchBean.imageStatuses) > 0) {
                    var imageStatusObj = _.pick(searchBean.imageStatuses, function (value, key, object) {
                        return value;
                    });
                    var imageStatuses = _.keys(imageStatusObj);
                    _.extend(searchBean, {"imageStatuses":imageStatuses});
                }

                // 获取价格披露任务产品列表
                self.taskJiagepiluService.search(searchBean).then(function (resp) {
                    if (resp.data) {
                        self.productList = resp.data.products;
                        self.pageOption.total = resp.data.total;
                    }
                });

                // 获取当前任务导入信息列表
                self.refreshImportInfoList();
            },

            importProduct: function () {
                var self = this;
                var uploadQueue = self.uploader.queue;
                var uploadItem = uploadQueue[uploadQueue.length - 1];
                var uploadIt = function () {
                    self.uploadItem = uploadItem;
                    uploadItem.onSuccess = function (res) {
                        self.$timeout(function () {
                            self.uploadItem = null;
                        }, 500);
                        if (res.message) {
                            self.alert(res.message);
                            return;
                        }
                        self.notify.success('TXT_MSG_UPDATE_SUCCESS');
                    };
                    uploadItem.formData = [{
                        taskId: self.taskId
                    }];
                    uploadItem.upload();
                };
                if (!uploadItem) {
                    return self.alert('TXT_MSG_NO_UPLOAD');
                }
                // if (!self.data.length) {
                //     uploadIt();
                //     return;
                // }
                self.confirm('TXT_JIAGEPILU__REIMPORT_CONFIRM').then(uploadIt);
            },

            // 导入模板下载
            downloadTemplate:function () {
                var urls = this.urls;
                var path = urls.root + "/" + urls.downloadImportTemplate;
                $.download.post(path);
            },

            // 导入信息列表刷新
            refreshImportInfoList: function () {
                var self = this;
                self.taskJiagepiluService.getImportInfoList({taskId:self.taskId}).then(function (resp) {
                    if (resp.data) {
                        self.importInfoList = resp.data;
                    }
                });
            },

            // 导入错误文件下载
            downloadError: function (fileName) {
                var urls = this.urls;
                var path = urls.root + "/" + urls.downloadImportError;
                $.download.post(path, {fileName: fileName});
            },

            // 弹出添加商品框
            popAddBeat: function () {
                popups.popNewCombinedProduct(_.extend({"carts": $scope.vm.carts},
                                                      {"startSupplyChain": $scope.vm.config.startSupplyChain}))
                    .then(function () {
                        getProductList();
                    })
            },

            // 清除搜索条件
            clear: function () {
                this.searchBean = {taskId:this.taskId};
            },

            // 搜索结果
            search: function () {
                var self = this;
                // 检索
                self.getData();
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
                ttt.confirm('TXT_MSG_ERROR_BEAT_ITEM').then(changeIt);
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

        return JiagepiluController;

    })())
});