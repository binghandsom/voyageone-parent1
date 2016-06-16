/**
 * Created by linanbin on 15/12/7.
 */

define([
  'modules/cms/controller/popup.ctl'
], function () {

  function dictionaryItem($scope, $location, cRoutes, $routeParams, $translate, $dictionaryService, notify, confirm) {

    $scope.initialize = initialize;
    $scope.cancel = cancel;
    $scope.save = save;
    $scope.delDictItem = delDictItem;
    $scope.sortUp = sortUp;
    $scope.sortDown = sortDown;
    $scope.addNewDictionary = addNewDictionary;
    $scope.editDictionary = editDictionary;

    /**
     * 设置初始化数据
     */
    function initialize () {
      $scope.vm = {
        dictionary: {
          type: "DICT",
          value: "",
          expression: {
            ruleWordList: []
          },
          isUrl: false
        },
        idEdit: false,
        modified: ""
      };


      $dictionaryService.init()
          .then(function (res) {
            $scope.vm.masterData = res.data;
            if (!_.isUndefined($routeParams.id)) {
              $scope.vm.idEdit = true;
              $dictionaryService.getDict({id: $routeParams.id})
                  .then(function (res) {
                    $scope.vm.dictionary = JSON.parse(res.data.value);
                    $scope.vm.cart_id = res.data.cart_id;
                    $scope.vm.modified = res.data.modified;
                  })
            }
          });
    }

    /**
     * 清空画面上显示的数据,并返回到字典列表页面
     */
    function cancel () {
      $location.path(cRoutes.image_dictionary_list.hash);
    }

    /**
     * 检索
     */
    function save () {

      if ($scope.dictionaryForm.$valid
        && $scope.vm.dictionary.expression.ruleWordList.length > 0) {
        var data = {
          name: $scope.vm.dictionary.value,
          value: angular.toJson ($scope.vm.dictionary)
        };
        data.cart_id = $scope.vm.cart_id;
        if ($scope.vm.idEdit) {
          data.id = $routeParams.id;
          data.modified = $scope.vm.modified;
          $dictionaryService.setDict(data)
              .then(function () {
                notify.success ($translate.instant('TXT_MSG_UPDATE_SUCCESS'));
                $location.path(cRoutes.image_dictionary_list.hash);
              })
        } else {
          $dictionaryService.addDict(data)
              .then(function () {
                notify.success ($translate.instant('TXT_MSG_UPDATE_SUCCESS'));
                $location.path(cRoutes.image_dictionary_list.hash);
              })
        }
      } else {
        notify.danger($translate.instant('TXT_MSG_NO_DATA_WITH_SAVE'))
      }
    }

    /**
     * 删除dictionary数据
     * @param dictionaryInfo
     */
    function delDictItem (index) {
      confirm($translate.instant('TXT_MSG_DELETE_ITEM')).result
          .then(function () {
            $scope.vm.dictionary.expression.ruleWordList.splice(index, 1);
            notify.success ($translate.instant('TXT_MSG_DELETE_SUCCESS'));
          });
    }

    /**
     * 升序
     * @param index
     */
    function sortUp (index) {
      var temp =  angular.copy ($scope.vm.dictionary.expression.ruleWordList[index]);
      $scope.vm.dictionary.expression.ruleWordList.splice(index, 1);
      $scope.vm.dictionary.expression.ruleWordList.splice(index - 1, 0,temp);

    }

    /**
     * 降序
     * @param index
     */
    function sortDown (index) {
      var temp =  angular.copy ($scope.vm.dictionary.expression.ruleWordList[index]);
      $scope.vm.dictionary.expression.ruleWordList.splice(index, 1);
      $scope.vm.dictionary.expression.ruleWordList.splice(index + 1 , 0,temp);
    }

    /**
     * 添加一个新的字典项
     * @param info
     */
    function addNewDictionary (info) {
      if (!_.isUndefined(info))
        $scope.vm.dictionary.expression.ruleWordList.push(info);
    }

    /**
     * 修改一个原有的字典项
     * @param info
     * @param index
     */
    function editDictionary (info, index) {
      if (!_.isUndefined(info))
        $scope.vm.dictionary.expression.ruleWordList[index] = info;
    }
  }

  dictionaryItem.$inject = ['$scope', '$location', 'cRoutes', '$routeParams', '$translate', '$dictionaryService', 'notify', 'confirm'];
  return dictionaryItem;
});