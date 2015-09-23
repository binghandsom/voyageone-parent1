/**
 * @Name: categorySearchController
 * @Date: 2015/06/12
 * 
 * @User: Lewis.liu
 * @Version: 1.0.0
 */

define([ "modules/cms/cms.module",
		"modules/cms/masterPropValueSetting/setMtPropValuePopUpService"], 
		function(cmsApp) {
	
			cmsApp.controller('setMtPropValuePopUpController', ['$scope', "setMtPropValuePopUpService",'$filter', '$http', 'editableOptions', 'editableThemes','$q',
			  function($scope, service, $filter, $http, editableOptions, editableThemes, $q) {

				 $scope.words = [];
				 
				 $scope.complexValues = [];
				 			 
	 			 $scope.types = [
	 			    {value: 1, text: 'TEXT'},
	 			    {value: 2, text: 'CMS'},
	 			    {value: 3, text: 'DICT'},
	 			    {value: 4, text: 'MASTER'},
	 			    {value: 5, text: 'CUSTOM'}
	 			 ];
	 			  
				
				$scope.init = function() {
					
					var path = $scope.objPath;
					
					var unDecodeValue ="";
					
					if (path.indexOf(".")>-1) {
						var names = path.split(".");
					
						var leafObj = $scope;
						for (var i = 0; i < names.length-1; i++) {
							leafObj=leafObj[names[i]];
						}
						unDecodeValue = leafObj[names[names.length-1]];
					}else {
						unDecodeValue = scope[path];
					}
					if (unDecodeValue==undefined||unDecodeValue==null||unDecodeValue.trim()=="") {
						var word = {type:1,value:undefined};
						$scope.words.push(word);
					}else {
						var formData = {txtValue:unDecodeValue,channelId:$scope.hiddenInfo.channelId};
						service.deserialize(formData).then(function(response){
							var decodeValues = response.data.decodeValues;
							for (var i = 0; i < decodeValues.length; i++) {
								var word={};
								word.type=decodeValues[i].wordType;
								switch (word.type) {
								case 1:
									word.value=decodeValues[i].wordValue;
									break;
								case 2:
									word.complexValues=decodeValues[i].complexValues;
									word.value = decodeValues[i].wordValue;
									break;
								case 3:
									word.complexValues=decodeValues[i].complexValues;
									word.value = decodeValues[i].wordValue;
									break;
								case 4:
									
									break;
								case 5:
									
									break;

								default:
									break;
								}
								
								
								$scope.words.push(word);
							}
						});
					}
					
				};
				
			  
			  $scope.onChange = function(curObject){
				  
				  var word = curObject.word;
				  
				  var formData = {wordType:word.type};
				  word.value=null;
				  
				  //取得complexValues
				  word.complexValues = getComplexValues(word.type,formData);
			  };
			  
			  function getComplexValues(wordType,formData) {
				  var complexValues = [];
				//取得complexValues
				  switch (wordType) {
				case 2:
					service.getComplexValues(formData).then(function(response) {
						var resDatas = response.data.complexValues;
						for (var i = 0; i < resDatas.length; i++) {
							complexValues.push(resDatas[i]);
						}
						
					  });
					break;
				case 3:
					formData.channelId = $scope.hiddenInfo.channelId;
					service.getComplexValues(formData).then(function(response) {
						var resDatas = response.data.complexValues;
						for (var i = 0; i < resDatas.length; i++) {
							complexValues.push(resDatas[i]);
						}
						
					  });
					break;
				case 4:
//					$scope.complexValues=
					break;
				case 5:
//					$scope.complexValues=
					break;

				default:
					break;
				}
				  return complexValues;
				  
			};
			  
			  $scope.confirm = function () {
				  
				  var path = $scope.objPath;
				  
				  var words = [];
				  
				  for (var i = 0; i < $scope.words.length; i++) {
					  
					  var word = {};
					  
					  if ($scope.words[i].type==1) {
						  word = {
							  wordType:$scope.words[i].type,
							  wordValue:$scope.words[i].value
						  }
						}else {
							word = {
								wordType:$scope.words[i].type,
								wordValue:$scope.words[i].value
							}
						}
					
					  words.push(word);
				  };
				  
				  if (words.length>0) {
					  service.serialize({words:words}).then(function(response) {
						  	
							setValue(path,response.data.encodeValue);
							
						  });
				}else {
					setValue(path,null);
				}
				  
				  
				  $scope.closeThisDialog ();
				
			};
			
			function setValue(path,encodeValue) {
				if (path.indexOf(".")>-1) {
					var names = path.split(".");
				
					var leafObj = $scope;
					for (var i = 0; i < names.length-1; i++) {
						leafObj = leafObj[names[i]];
					}
					leafObj[names[names.length-1]] = encodeValue;
				}else {
					 scope[path] = encodeValue ;
				}
			};


			}]);

		
});

