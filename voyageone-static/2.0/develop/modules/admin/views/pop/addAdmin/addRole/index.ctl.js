/**
 * Created by sofia on 2016/8/10.
 */
define([
    'admin'
], function (admin) {
    admin.controller('AddRoleController', (function () {
        function AddRoleController(context, alert, adminRoleService, adminOrgService, adminUserService, channelService, storeService, $uibModalInstance) {
            this.sourceData = context ? context : {};
            this.append = context == 'add' ? true : false;
            this.alert = alert;
            this.adminRoleService = adminRoleService;
            this.adminOrgService = adminOrgService;
            this.adminUserService = adminUserService;
            this.channelService = channelService;
            this.storeService = storeService;
            this.popType = '修改角色';
            this.$uibModalInstance = $uibModalInstance;
            this.applicationList = [
                {'id': 1, 'application': 'Admin', 'valid': false},
                {'id': 2, 'application': 'CMS', 'valid': false},
                {'id': 3, 'application': 'OMS', 'valid': false},
                {'id': 4, 'application': 'WMS', 'valid': false},
                {'id': 5, 'application': 'VMS', 'valid': false}
            ];
            this.saveInfo = {
                roleName: this.sourceData !== 'add' ? this.sourceData.roleName : '',
                roleType: this.sourceData !== 'add' ? this.sourceData.roleType + '' : '',
                description: this.sourceData !== 'add' ? this.sourceData.description : '',
                active: this.sourceData !== 'add' ? this.sourceData.active : '1',
                allChannel: this.sourceData !== 'add' ? (this.sourceData.channelId != null ? this.sourceData.channelId.indexOf('ALL') > -1 ? '1' : '0' : '') : '',
                allStore: this.sourceData !== 'add' ? (this.sourceData.storeId != null ? this.sourceData.storeId.indexOf('ALL') > -1 ? '1' : '0' : '') : '',
                applications: this.sourceData !== 'add' ? this.sourceData.application : [],
                channelIds: this.sourceData !== 'add' ? this.sourceData.channelId : [],
                storeIds: this.sourceData !== 'add' ? this.sourceData.storeId : []
            }
        }

        AddRoleController.prototype = {
            init: function () {
                var self = this;
                self.adminRoleService.getAllRoleType().then(function (res) {
                    self.roleTypeList = res.data;
                });
                if (self.sourceData == 'add') {
                    self.popType = '添加角色';
                    self.sourceData = {};
                    self.sourceData.active = '1';
                }
                if (self.sourceData.isCopyRole == true) {
                    self.popType = '拷贝角色';
                    self.sourceData.active = '1';
                }
                self.adminOrgService.getAllOrg().then(function (res) {
                    self.orgList = res.data;
                });
                self.adminUserService.getAllApp().then(function (res) {
                    self.appList = res.data;
                });

                self.channelService.getAllChannel(null).then(function (res) {
                    _.forEach(res.data, function (channel) {
                        channel.name = '(' + channel.orderChannelId + ')' + channel.name;
                    });
                    self.channelAllListCopy = res.data;
                    self.channelAllList = res.data;
                    if (self.popType == '添加角色') return;
                    return call(self.channelAllList);
                });
                function call(channelAllList) {
                    var channelAllListCopy = angular.copy(channelAllList);
                    self.channelList = self.sourceData.channelId != null ? self.sourceData.channelId.split(',') : [];
                    _.forEach(self.channelList, function (item, index) {
                        _.map(channelAllList, function (channel) {
                            if (channel.orderChannelId == item) {
                                self.channelList[index] = {
                                    'orderChannelId': item,
                                    'name': channel.name
                                }
                            }
                            return self.channelList;
                        });
                    });
                    channelCallback(channelAllListCopy);
                }

                function channelCallback(channelAllListCopy) {
                    self.channelAllList = [];
                    if (self.channelList.length == 0) {
                        self.channelAllList = channelAllListCopy;
                        return;
                    } else {
                        self.channelAllList = channelAllListCopy;
                        _.forEach(self.channelList, function (item) {
                            self.data = _.find(self.channelAllList, function (channel) {
                                return channel.orderChannelId == item.orderChannelId;
                            });
                            self.channelAllList.splice(self.channelAllList.indexOf(self.data), 1);
                        });
                    }
                }

                self.storeService.getAllStore(null).then(function (res) {
                    self.storeAllListCopy = res.data;
                    self.storeAllList = res.data;
                    _.forEach(self.storeAllList, function (item) {
                        item.storeName = '(' + item.channelId + ')' + item.storeName;
                    });
                    if (self.popType == '添加角色') return;
                    return storeCall(self.storeAllList);
                });
                function storeCall(storeAllList) {
                    var storeAllListCopy = angular.copy(storeAllList);
                    self.storeList = self.sourceData.storeId != null ? self.sourceData.storeId.split(',') : [];
                    _.forEach(self.storeList, function (item, index) {
                        _.map(storeAllList, function (store) {
                            if (store.storeId == item) {
                                self.storeList[index] = {
                                    'storeId': item,
                                    'storeName': store.storeName
                                }
                            }
                            return self.storeList;
                        });
                    });
                    storeCallback(storeAllListCopy);
                }

                function storeCallback(storeAllListCopy) {
                    self.storeAllList = [];
                    if (self.storeList.length == 0) {
                        self.storeAllList = storeAllListCopy;
                    } else {
                        self.storeAllList = storeAllListCopy;
                        _.forEach(self.storeList, function (item) {
                            self.data = _.find(self.storeAllList, function (store) {
                                return store.storeId == item.storeId;
                            });
                            self.storeAllList.splice(self.storeAllList.indexOf(self.data), 1);
                        });
                    }
                }

                if (!self.sourceData.application)return;
                var appList = self.sourceData.application.split(',');
                _.forEach(appList, function (item) {
                    _.forEach(self.applicationList, function (i) {
                        if (i.application.toLocaleLowerCase() == item) {
                            i.valid = true;
                        }
                    })
                })
            },
            selected: function (item) {
                var self = this;
                if (item.value) {
                    self.selectedChannelId = item.value.orderChannelId;
                    self.selectedStoreId = item.value.storeId;
                    item.direction == 'left' ? self.leftSelectedFlg = true : self.rightSelectedFlg = true;
                } else {
                    item.direction == 'left' ? self.leftSelectedFlg = false : self.rightSelectedFlg = false;
                }
            },
            search: function (item) {
                var self = this;
                self.channelTempAllList = [];
                self.storeTempAllList = [];
                self.channelIds = [];
                if (item.isFilter == true) {
                    _.forEach(self.channelList, function (channel) {
                        self.channelIds.push(channel.orderChannelId);
                    });
                    self.storeService.getStoreByChannelIds(self.channelIds).then(function (res) {
                        self.storeAllListCopy = res.data;
                    });
                    // self.storeService.getAllStore(null).then(function (res) {
                    //     self.storeAllListCopy = res.data;
                    // });
                }
                switch (item.type) {
                    case'channel':
                        // 从AllChannelList过滤出已经选择的channel，组成新的可以选择的list
                        self.channelAllList = self.channelAllListCopy;
                        _.forEach(self.channelList, function (item) {
                            var index = -1;
                            _.forEach(self.channelAllList, function (allItem, i) {
                                if (allItem.orderChannelId == item.orderChannelId) {
                                    index = i;
                                }
                            });
                            if (index > -1) self.channelAllList.splice(index, 1);
                        });
                        _.filter(self.channelAllList, function (data) {
                            if (data.name.toUpperCase().indexOf(item.value.toUpperCase()) > -1) {
                                self.channelTempAllList.push(data)
                            }
                        });
                        self.channelAllList = self.channelTempAllList;
                        break;
                    case'store':
                        if (item.isFilter == true) {
                            _.filter(self.storeAllList.length < self.storeAllListCopy.length ? self.storeAllListCopy : self.storeAllList, function (data) {
                                if (data.channelId == item.value) {
                                    data.storeName = data.storeName.indexOf('(') < 0 ? '(' + data.channelId + ')' + data.storeName : data.storeName;
                                    self.storeTempAllList.push(data)
                                }
                            });
                            return self.storeTempAllList;
                        } else {
                            _.filter(self.storeAllListCopy, function (data) {
                                if (data.storeName.toUpperCase().indexOf(item.value.toUpperCase()) > -1) {
                                    self.storeTempAllList.push(data)
                                }
                            });
                            self.storeAllList = self.storeTempAllList;
                        }
                        break;
                }
            },
            channelMove: function (type) {
                var self = this;
                self.channelList = self.channelList ? self.channelList : [];
                self.channelAllList = self.channelAllList ? self.channelAllList : [];
                switch (type) {
                    case '':
                        self.channelAllList = self.channelAllListCopy;
                        _.forEach(self.channelList, function (item) {
                            var index = -1;
                            _.forEach(self.channelAllList, function (allItem, i) {
                                if (allItem.orderChannelId == item.orderChannelId) {
                                    index = i;
                                }
                            });
                            if (index > -1) self.channelAllList.splice(index, 1);
                        });
                        break;
                    case 'allInclude':
                        if (self.channelTempAllList) {
                            self.channelAllList = self.channelTempAllList;
                            _.forEach(self.channelAllList, function (item) {
                                self.channelList.push(item);
                            });
                            self.channelAllList = [];
                            break;
                        } else {
                            _.forEach(self.channelAllList, function (item) {
                                self.channelList.push(item);
                            });
                            self.channelAllList = [];
                            break;
                        }
                    case 'include':
                        if (self.rightSelectedFlg == true) {
                            self.data = _.find(self.channelAllList, function (channel) {
                                return channel.orderChannelId == self.selectedChannelId;
                            });
                            if (self.data == undefined) {
                                self.alert('请在可选择渠道区 选择渠道后再点此按钮!');
                                return;
                            } else {
                                self.channelList.push(self.data);
                                self.channelAllList.splice(self.channelAllList.indexOf(self.data), 1);
                                self.selectedChannelId = '';
                                var data = self.search({
                                    'type': 'store',
                                    'value': self.data.orderChannelId,
                                    'isFilter': true
                                });
                                if (self.storeAllList.length < self.storeAllListCopy.length) {
                                    _.forEach(data, function (item) {
                                        self.storeAllList.push(item);
                                    })
                                } else {
                                    self.storeAllList = data;
                                }
                                break;
                            }
                        } else {
                            self.alert('请在可选择渠道区 选择渠道后再点此按钮!');
                            return;
                        }
                    case 'exclude':
                        if (self.leftSelectedFlg == true) {
                            self.data = _.find(self.channelList, function (channel) {
                                return channel.orderChannelId == self.selectedChannelId;
                            });
                            if (self.data == undefined) {
                                self.alert('请在已选择渠道区 选择渠道后再点此按钮!');
                                return;
                            } else {
                                self.channelAllList.push(self.data);
                                self.channelList.splice(self.channelList.indexOf(self.data), 1);
                                self.selectedChannelId = '';
                                break;
                            }
                        } else {
                            self.alert('请在已选择渠道区 选择渠道后再点此按钮!');
                            return;
                        }
                    case 'allExclude':
                        _.forEach(self.channelList, function (item) {
                            self.channelAllList.push(item);
                        });
                        self.channelList = [];
                        break;
                }
            },
            storeMove: function (type) {
                var self = this;
                self.storeList = self.storeList ? self.storeList : [];
                self.storeAllList = self.storeAllList ? self.storeAllList : [];
                switch (type) {
                    case '':
                        self.storeAllList = self.storeAllListCopy;
                        _.forEach(self.storeAllList, function (item) {
                            item.storeName = item.storeName.indexOf('(') < 0 ? '(' + item.channelId + ')' + item.storeName : item.storeName;
                        });
                        _.forEach(self.storeList, function (item) {
                            var index = -1;
                            _.forEach(self.storeAllList, function (allItem, i) {
                                if (allItem.storeId == item.storeId) {
                                    index = i;
                                }
                            });
                            if (index > -1) self.storeAllList.splice(index, 1);
                        });
                        break;
                    case 'allInclude':
                        if (self.storeTempAllList) {
                            self.storeAllList = self.storeTempAllList;
                            _.forEach(self.storeAllList, function (item) {
                                self.storeList.push(item);
                            });
                            self.storeAllList = [];
                            break;
                        } else {
                            _.forEach(self.storeAllList, function (item) {
                                self.storeList.push(item);
                            });
                            self.storeAllList = [];
                            break;
                        }
                    case 'include':
                        if (self.rightSelectedFlg == true) {
                            self.data = _.find(self.storeAllList, function (store) {
                                return store.storeId == self.selectedStoreId;
                            });
                            if (self.data == undefined) {
                                self.alert('请在可选择仓库区 选择仓库后再点此按钮!');
                                return;
                            } else {
                                self.storeList.push(self.data);
                                self.storeAllList.splice(self.storeAllList.indexOf(self.data), 1);
                                self.selectedStoreId = '';
                                break;
                            }
                        } else {
                            self.alert('请在可选择仓库区 选择仓库后再点此按钮!');
                            return;
                        }
                    case 'exclude':
                        if (self.leftSelectedFlg == true) {
                            self.data = _.find(self.storeList, function (store) {
                                return store.storeId == self.selectedStoreId;
                            });
                            if (self.data == undefined) {
                                self.alert('请在已选择仓库区 选择仓库后再点此按钮!');
                                return;
                            }
                            self.storeAllList.push(self.data);
                            self.storeList.splice(self.storeList.indexOf(self.data), 1);
                            self.selectedStoreId = '';
                            break;
                        } else {
                            self.alert('请在已选择仓库区 选择仓库后再点此按钮!');
                            return;
                        }
                    case 'allExclude':
                        _.forEach(self.storeList, function (item) {
                            self.storeAllList.push(item);
                        });
                        self.storeList = [];
                        break;
                }
            },
            cancel: function () {
                this.$uibModalInstance.close();
            },
            save: function () {
                var self = this, selApp = [], result = {};
                self.saveInfo.active = self.saveInfo.active - 0;
                var selectedAppList = _.filter(self.applicationList, function (selectedApp) {
                    return selectedApp.valid;
                });
                _.forEach(selectedAppList, function (app) {
                    selApp.push(app.application.toLowerCase());
                });
                self.saveInfo.applications = selApp;

                self.saveInfo.roleType = self.saveInfo.roleType - 0;

                self.saveInfo.channelIds = [];
                self.saveInfo.storeIds = [];
                if (self.saveInfo.allChannel == '0') {
                    _.forEach(self.channelList, function (item) {
                        self.saveInfo.channelIds.push(item.orderChannelId);
                    });
                }
                if (self.saveInfo.allStore == '0') {
                    /**
                     * 授权仓库只有在选择了WMS系统才显示
                     * 未选择WMS系统时，清空授权仓库的信息
                     */
                    if (self.saveInfo.applications.indexOf('wms') < 0) {
                        self.saveInfo.allStore = '0';
                        self.saveInfo.storeIds = [];
                    } else {
                        _.forEach(self.storeList, function (item) {
                            self.saveInfo.storeIds.push(item.storeId - 0);
                        });
                    }
                }

                if (self.append == true) {
                    self.adminRoleService.addRole(self.saveInfo).then(function (res) {
                        if (res.data == false) {
                            self.confirm(res.data.message);
                            return;
                        }
                        var channelName = [], storeName = [];
                        _.forEach(self.channelList, function (item) {
                            channelName.push(item.name);
                        });
                        _.forEach(self.storeList, function (item) {
                            storeName.push(item.storeName);
                        });
                        self.saveInfo.channelName = channelName.join(',');
                        self.saveInfo.storeName = storeName.join(',');
                        _.extend(result, {'res': 'success', 'sourceData': self.sourceData});
                        self.$uibModalInstance.close(result);
                    })
                } else {
                    _.extend(self.saveInfo, {id: self.sourceData.id});
                    if (self.sourceData.isCopyRole == true) {
                        _.extend(self.saveInfo, {action: 'copy'});
                    }
                    self.adminRoleService.updateRole(self.saveInfo).then(function (res) {
                        if (res.data == false) {
                            self.confirm(res.data.message);
                            return;
                        }
                        _.extend(result, {'res': 'success', 'sourceData': self.sourceData});
                        self.$uibModalInstance.close(result);
                    })
                }
            }
        };
        return AddRoleController;
    })())
});