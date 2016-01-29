/**
 * Created by linanbin on 15/12/7.
 */

define([
  'modules/cms/controller/popup.ctl'
], function () {

  function dictionaryItem($scope, $location, cRoutes, $routeParams, $translate, $dictionaryService, notify) {

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
        }
      };

      if (!_.isUndefined($routeParams.id)) {
        $dictionaryService.getDict({id: $routeParams.id})
            .then(function (res) {
              $scope.vm.dictionary = JSON.parse(res.data.value);
              _.forEach($scope.vm.dictionary.expression.ruleWordList, function (object) {
                if (_.isEqual(object.type, 'CUSTOM')) {
                  object.value = angular.toJson(object.value);
                }
              })
            })
      }
    }

    /**
     * 清空画面上显示的数据,并返回到字典列表页面
     */
    function cancel () {
      $location.path(cRoutes.system_dict_list.hash);
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
        $dictionaryService.addDict(data)
            .then(function () {
              $location.path(cRoutes.system_dict_list.hash);
            })
      } else {
        notify.danger($translate.instant('TXT_COM_MSG_NO_DATA_WITH_SAVE'))
      }
    }

    /**
     * 删除dictionary数据
     * @param dictionaryInfo
     */
    function delDictItem (index) {
      $scope.vm.dictionary.expression.ruleWordList.splice(index, 1);
      notify.success ($translate.instant('TXT_COM_DELETE_SUCCESS'));
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

  dictionaryItem.$inject = ['$scope', '$location', 'cRoutes', '$routeParams', '$translate', '$dictionaryService', 'notify'];
  return dictionaryItem;
});