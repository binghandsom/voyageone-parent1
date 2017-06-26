/**
 * 价格披露Task详情页面 create By rex.wu at 2017-06-21 11:22:00
 */
define([
    'cms',
    'underscore',
    'modules/cms/enums/Carts',
    'modules/cms/controller/popup.ctl'
], function (cms, _, cart) {
    cms.controller("jiagepiluController", (function () {

        function JiagepiluController($routeParams, taskJiagepiluService, $translate, taskBeatService, cActions, popups, FileUploader, alert, confirm, notify, $location, $timeout) {

            var urls = cActions.cms.task.taskJiagepiluService;
            this.urls = urls;
            var taskId = parseInt($routeParams['taskId']);
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
            this.popups = popups;


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

            this.imageTemplateList = [
                {
                    channelId:"001",
                    cartId:20,
                    url:"http://s7d5.scene7.com/is/image/sneakerhead/SN20161223_1200x1200?$1200X1200$&$img=%s"
                },
                {
                    channelId:"001",
                    cartId:23,
                    url:"http://image.sneakerhead.com/is/image/sneakerhead/1200templateblack?$1200x1200$&$img=%s"
                },
                {
                    channelId:"007",
                    cartId:23,
                    url:"http://s7d5.scene7.com/is/image/sneakerhead/champion_zhutu_moban?$900x900$&$layer_1_src=%s"
                },
                {
                    channelId:"010",
                    cartId:23,
                    url:"http://s7d5.scene7.com/is/image/sneakerhead/Jewelry%%5F20170425%%5Fx1200%%5F1200x?$1200x1200$&$products=%s"
                },
                {
                    channelId:"012",
                    cartId:23,
                    url:"http://s7d5.scene7.com/is/image/sneakerhead/bcbg_1200_1200?$1200x1200$&$big=%s"
                },
                {
                    channelId:"014",
                    cartId:23,
                    url:"http://s7d5.scene7.com/is/image/sneakerhead/zhutu-1?$1200x1200$&$layer_1_src=%s"
                },
                {
                    channelId:"017",
                    cartId:30,
                    url:"http://s7d5.scene7.com/is/image/sneakerhead/1200-logo-zt?$VTM-TM-1200-1200$&$img=%s"
                },
                {
                    channelId:"018",
                    cartId:23,
                    url:"http://s7d5.scene7.com/is/image/sneakerhead/Target_20161212_x1200_1200x-1?$1200x1200$&$1200x1200$&$product=%s"
                },
                {
                    channelId:"024",
                    cartId:30,
                    url:"http://s7d5.scene7.com/is/image/sneakerhead/OSzt?$800x800$&$layer_2_src=%s"
                },
                {
                    channelId:"030",
                    cartId:20,
                    url:"http://s7d5.scene7.com/is/image/sneakerhead/Target_20160527_x1200_1200x?$1200x1200$&$product=%s"
                },
                {
                    channelId:"033",
                    cartId:30,
                    url:"http://s7d5.scene7.com/is/image/sneakerhead/KITBAG201704241000x1000zhutu?$KITBAG-1000X10000$&$PRODUCT=%s&update=1"
                },
                {
                    channelId:"034",
                    cartId:23,
                    url:"http://s7d5.scene7.com/is/image/sneakerhead/coty_zhutu_800?$800x800$&$layer_1_src=%s&aa="
                },
                {
                    channelId:"928",
                    cartId:31,
                    url:"http://s7d5.scene7.com/is/image/sneakerhead/liking_414_1200X1200zhutu?$1200x1200$&$image=%s"
                }
                // 001 20 http://s7d5.scene7.com/is/image/sneakerhead/SN20161223_1200x1200?$1200X1200$&$img=%s
                // 001 23 http://image.sneakerhead.com/is/image/sneakerhead/1200templateblack?$1200x1200$&$img=%s
                // 007 23 http://s7d5.scene7.com/is/image/sneakerhead/champion_zhutu_moban?$900x900$&$layer_1_src=%s
                // 010 23 http://s7d5.scene7.com/is/image/sneakerhead/Jewelry%%5F20170425%%5Fx1200%%5F1200x?$1200x1200$&$products=%s
                // 012 23 http://s7d5.scene7.com/is/image/sneakerhead/bcbg_1200_1200?$1200x1200$&$big=%s
                // 014 23 http://s7d5.scene7.com/is/image/sneakerhead/zhutu-1?$1200x1200$&$layer_1_src=%s
                // 017 30 http://s7d5.scene7.com/is/image/sneakerhead/1200-logo-zt?$VTM-TM-1200-1200$&$img=%s
                // 018 23 http://s7d5.scene7.com/is/image/sneakerhead/Target_20161212_x1200_1200x-1?$1200x1200$&$1200x1200$&$product=%s
                // 024 30 http://s7d5.scene7.com/is/image/sneakerhead/OSzt?$800x800$&$layer_2_src=%s
                // 030 20 http://s7d5.scene7.com/is/image/sneakerhead/Target_20160527_x1200_1200x?$1200x1200$&$product=%s
                // 033 30 http://s7d5.scene7.com/is/image/sneakerhead/KITBAG201704241000x1000zhutu?$KITBAG-1000X10000$&$PRODUCT=%s&update=1
                // 034 23 http://s7d5.scene7.com/is/image/sneakerhead/coty_zhutu_800?$800x800$&$layer_1_src=%s&aa=
                // 928 31 http://s7d5.scene7.com/is/image/sneakerhead/liking_414_1200X1200zhutu?$1200x1200$&$image=%s
            ];
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
                        self.task = resp.data.task;
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

                        var cartName = cart.valueOf(self.task.cartId).desc;
                        _.extend(self.task, {cartName:cartName});

                    }
                });

                self.search();

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
                        // 刷新列表
                        self.getData();
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

            download: function () {
                var ttt = this;
                $.download.post(ttt.downloadUrl, {taskId: ttt.taskId});
            },

            // 清除搜索条件
            clear: function () {
                this.searchBean = {taskId:this.taskId};
            },

            // 搜索结果
            search: function () {
                var self = this;
                // 检索
                var searchBean = angular.copy(self.searchBean);

                // 处理多个code
                if (searchBean.numIidOrCodes) {
                    var numIidOrCodes = searchBean.numIidOrCodes.split("\n");
                    searchBean.numIidOrCodes = numIidOrCodes;
                }
                // 处理勾选的单品状态和图片状态
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

                // 分页参数处理
                _.extend(searchBean, self.pageOption);

                // 获取价格披露任务产品列表
                self.taskJiagepiluService.search(searchBean).then(function (resp) {
                    if (resp.data) {
                        self.productList = resp.data.products;
                        self.pageOption.total = resp.data.total;
                        self.summary = resp.data.summary;

                        if (_.size(self.productList) > 0) {
                            _.forEach(self.productList, function (product) {
                                // 查询图片模板
                                var imageTemplate = _.find(self.imageTemplateList, function (templdate) {
                                    return self.task.channelId == templdate.channelId && self.task.cartId == templdate.cartId;
                                });
                                if (imageTemplate) {
                                    var imageUrl = imageTemplate.url.replace(/%s/g, product.imageName);
                                    _.extend(product, {imageUrl:imageUrl});
                                }
                            });
                        }
                    }
                });
            },

            // 启动/停止/还原单个商品
            controlOne: function (beatInfo, flag) {
                var ttt = this;
                var beat_id = beatInfo.id;
                var changeIt = function () {
                    ttt.taskJiagepiluService.operateProduct({
                        beat_id: beat_id,
                        flag: flag
                    }).then(function (res) {
                        if (!res.data)
                            return ttt.alert('TXT_MSG_UPDATE_FAIL');
                        ttt.search();
                    });
                };
                if (beatInfo.synFlagEnum !== 'CANT_BEAT') {
                    changeIt();
                    return;
                }
                ttt.confirm('TXT_MSG_ERROR_BEAT_ITEM').then(changeIt);
            },

            // 启动/停止/还原所有
            controlAll: function (flag) {

                var self = this;

                // 在统计信息中查找错误的统计
                var errorSummary = self.summary.find(function (item) {
                    return item.flag === 'CANT_BEAT';
                });

                // 如果错误统计有数据, 说明是存在错误数据的
                // 就需要人为来确定是否要强制处理这些任务
                if (errorSummary && errorSummary.count) {
                    self.confirm("有状态为 <" +　self.$translate.instant('CANT_BEAT')　+ "> 的商品，确定要操作所有吗?")
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

            // 重启(启动失败/还原失败的重启)
            restartAll: function (flag) {
                var self = this;

                var flagSummary = self.summary.find(function (item) {
                    return item.flag === flag;
                });
                if (!flagSummary) {
                    self.alert("当前任务没有状态为 <" +　self.$translate.instant(flag)　+ "> 的商品");
                    return;
                }
                self.confirm("确定要重启状态为 <" +　self.$translate.instant(flag)　+ "> 的商品吗?").then(function () {
                    self.taskJiagepiluService.reBeating({task_id:self.taskId,flag:flag}).then(function (resp) {
                        if (resp.data) {
                            self.search();
                        }
                    })
                });
            },

            $controlAll: function (force, flag) {
                var self = this;

                self.taskJiagepiluService.operateProduct({
                    task_id: self.taskId,
                    force: force,
                    flag: flag
                }).then(function (res) {
                    if (!res.data)
                        return self.alert('TXT_MSG_UPDATE_FAIL');
                    self.search();
                });
            },

            updateTask: function () {
                var self = this;
                self.popups.openNewBeatTask({task: self.task}).then(function(newTask) {
                    self.task = newTask;
                });
            },

            bigImg:function (url) {
                window.open(url);
            }
        };

        return JiagepiluController;

    })())
});