(function() {
    /**
     * @Description:
     *
     * @User: linanbin
     * @Version: 2.0.0, 16/1/11
     */
    angular.module("voyageone.angular.factories.selectRows", []).factory("selectRowsFactory", function() {
        return function(config) {
            var _selectRowsInfo = config ? config : {
                selAllFlag: false,
                currPageRows: [],
                // [{id: "12345"}], [{id: "123456", name: "test1"}]
                selFlag: [],
                // [{"12345": true}, {"12346": false}]
                selList: []
            };
            this.selAllFlag = function(value) {
                return value !== undefined ? _selectRowsInfo.selAllFlag = value : _selectRowsInfo.selAllFlag;
            };
            this.clearCurrPageRows = function() {
                _selectRowsInfo.currPageRows = [];
            };
            this.currPageRows = function(value) {
                return value !== undefined ? _selectRowsInfo.currPageRows.push(value) : _selectRowsInfo.currPageRows;
            };
            this.selFlag = function(value) {
                return value !== undefined ? _selectRowsInfo.selFlag.push(value) : _selectRowsInfo.selFlag;
            };
            this.selList = function(value) {
                return value !== undefined ? _selectRowsInfo.selList.push(value) : _selectRowsInfo.selList;
            };
            this.selectRowsInfo = _selectRowsInfo;
        };
    });
})();