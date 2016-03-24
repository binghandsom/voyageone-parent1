/**
 * Created by linanbin on 15/12/7.
 */

define([
    'modules/cms/controller/popup.ctl'
], function () {

    function attributeController($scope, $routeParams, attributeService, $location, confirm, $translate, notify, cRoutes) {
        $scope.vm = {
            cat_path: $routeParams.catPath,
            sameAttr: "",
            unvalList: [],
            valList: []
        };

        $scope.initialize = initialize;
        $scope.save = save;
        $scope.openCategoryMapping = openCategoryMapping;
        $scope.goAttributePage = goAttributePage;
        $scope.openAddAttributeValue = openAddAttributeValue;
        $scope.remove = remove;
        $scope.addVal = addVal;
        $scope.goValuePage = goValuePage;

        /**
         * 初始化数据:
         * unsplitFlg默认值缺省，接收两个List
         * 初始化属性已翻译及未翻译列表
         */
        function initialize () {
            attributeService.init({cat_path:$routeParams.catPath})
                .then(function (res) {
                    $scope.vm.sameAttr = res.data.sameAttr;
                    $scope.vm.unvalList = res.data.unvalList;
                    $scope.vm.valList = res.data.valList;
                });
        }

        /**
         * 移除已翻译列表中的一条数据，并插入到未翻译列表中
         * @param item
         */
        function remove (item, inx){
            confirm($translate.instant('TXT_MSG_DELETE_ITEM')).result
                .then(function () {
                    // 删除被选中的一条数据
                    $scope.vm.valList.splice(inx,1);
                    // 将该数据移植到未翻译列表
                    $scope.vm.unvalList.push(item);
                })
        }

        /**
         * 移除未翻译列表中的一条数据，并插入到已翻译列表中
         * 且为其添加一个key:prop_translation,值为空
         * @param inx
         */
        function addVal (item, inx){
            $scope.vm.unvalList.splice(inx,1);
            // 清空以前翻译的值
            item.prop_translation = "";
            $scope.vm.valList.push(item);
        }

        /**
         * 保存已翻译和未翻译数据
         */
        function save(){
            var nData = {
                valList: $scope.vm.valList,
                unvalList: $scope.vm.unvalList,
                cat_path: $scope.vm.cat_path
            };
            attributeService.save(nData).then(function () {
                notify.success($translate.instant('TXT_MSG_UPDATE_SUCCESS'));
                $scope.initialize();
            })
        }

        /**
         * 跳转到自定义属性页面
         */
        function goValuePage (){
            $location.path(cRoutes.channel_custom_value.url + $scope.vm.cat_path);
        }

        /**
         * 打开类目选择popup
         * @param popupNewCategory
         */
        function openCategoryMapping (popupNewCategory) {

            attributeService.getCatTree()
                .then(function (res) {

                    popupNewCategory({
                        categories: res.data.categoryTree,
                        from: $scope.vm.cat_path != '0' ? $scope.vm.cat_path : "",
                        divType: '-'
                    }).then( function (res) {
                            // TODO 如果类目名字太长有问题
                            goAttributePage (res.selected.catPath)
                        }
                    );
                });
        }

        /**
         * 类目选择跳转至类目页面
         * @param catPath
         */
        function goAttributePage (catPath) {
            $location.path(cRoutes.channel_custom_attribute.url + catPath);
        }

        /**
         * 打开添加自定义属性popup -- newAttribute
         * @param openAddAttribute
         */
        function openAddAttributeValue (openAddAttribute) {

            var attributeList = angular.copy($scope.vm.unvalList);
            openAddAttribute({
                from: attributeList.concat($scope.vm.valList)
            }).then( function (res) {
                    addNewAttribute (res)
                }
            );
        }

        /**
         * 添加新追加的属性
         * @param nData
         */
        function addNewAttribute (nData) {
            // 未翻译
            if (_.isEmpty(nData.prop_translation)) {
                $scope.vm.unvalList.push({"prop_original":nData.prop_original, "cat_path":"0"})
            }
            // 已翻译
            else {
                $scope.vm.valList.push({"prop_original":nData.prop_original, "prop_translation": nData.prop_translation, "cat_path":"0"})
            }
            $scope.save();
        }
    }

    attributeController.$inject = ['$scope', '$routeParams', 'attributeService', '$location', 'confirm', '$translate', 'notify', 'cRoutes'];
    return attributeController;
});
