/**
 * @description 由于angular service为单例模式
 *              该service产品详情页统一ajax入口
 */
define([
    'cms'
],function (cms) {

    cms.service('detailDataService',class DetailDataService{
        constructor(){

        }

        test(){
            console.log('a');
        }
    });

});
