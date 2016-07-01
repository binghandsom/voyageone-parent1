define([
    'angularAMD',
    'modules/cms/controller/popup.ctl'
], function (angularAMD) {
    angularAMD.controller('popDealExtensionCtl', function ($scope,jmPromotionDetailService,context,$routeParams) {
        $scope.model={};
        $scope.modelPromotion={};
        $scope.selectMinDate=null;
        $scope.initialize = function () {
            $scope.model.promotionId=context.id;
            $scope.modelPromotion=context;
            var myDate = new Date(context.activityEnd);
            myDate.setDate(myDate.getDate() + 1);//日期默认为昨天
            $scope.selectMinDate=myDate;
           // var Year = myDate.getFullYear();   //获取完整的年份(4位,1970-????)
          //  var Month = myDate.getMonth() + 1;      //获取当前月份(0-11,0代表1月)
          //  var Day = myDate.getDate();
            console.log(context);
        };

        $scope.ok=function() {
                updateDealEndTimeAll();
        }

        var updateDealEndTimeAll=function(){
            jmPromotionDetailService.updateDealEndTimeAll( $scope.model).then(function (res) {
                if (!res.data.result) {
                    alert(res.data.msg);
                    return;
                }
                $scope.$close();
            }, function (res) {
            })
        };
        function formatToDate(date){
            return new Date(date) ;//$filter("date")(new Date(date),"yyyy-MM-dd HH:mm:ss");
        };
        function formatToStr(date){
            return $filter("date")(new Date(date),"yyyy-MM-dd HH:mm:ss");
        };
    });
});
