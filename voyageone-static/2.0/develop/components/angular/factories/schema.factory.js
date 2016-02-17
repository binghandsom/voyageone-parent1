/**
 * @Description:
 *
 * @User: linanbin
 * @Version: 2.0.0, 15/12/25
 */

angular.module('voyageone.angular.factories.schema', [])

    // 定义schemaHeaderFactory
    .factory('schemaHeaderFactory', function () {
        return function (config) {
            var _schemaHeaderInfo = config ? config : {
                isRequired: false,
                //size: null,
                isMultiComplex: false,
                isComplex: false,
                tipMsg: []
            };

            this.isRequired = function (value) {
                return value !== undefined ? _schemaHeaderInfo.isRequired = value : _schemaHeaderInfo.isRequired;
            };

            //this.size = function (value) {
            //    return value !== undefined ? _schemaHeaderInfo.size = value : _schemaHeaderInfo.size;
            //};

            this.isComplex = function (value) {
                return value !== undefined ? _schemaHeaderInfo.isComplex = value : _schemaHeaderInfo.isComplex;
            };

            this.isMultiComplex = function (value) {
                return value !== undefined ? _schemaHeaderInfo.isMultiComplex = value : _schemaHeaderInfo.isMultiComplex;
            };

            this.tipMsg = function (value) {
                return value !== undefined ? _schemaHeaderInfo.tipMsg.push(value) : _schemaHeaderInfo.tipMsg;
            };

            this.schemaHearInfo = _schemaHeaderInfo;
        }
    })

    // 定义schemaFactory
    .factory('schemaFactory', function () {

        return function (config) {

            var _schemaInfo = config ? config : {
                type: null,
                name: null,
                rowNum: null,
                isRequired: false,
                checkValues: [],
                tipMsg: [],
                html: [],
                notShowEdit: true
            };

            this.type = function (value) {
                return value !== undefined ? _schemaInfo.type = value : _schemaInfo.type;
            };

            this.name = function (value) {
                return value !== undefined ? _schemaInfo.name = value : _schemaInfo.name;
            };

            this.html = function (value) {
                return value !== undefined ? _schemaInfo.html.push(value) : htmlToString(_schemaInfo.html);
            };

            this.isRequired = function (value) {
                return value !== undefined ? _schemaInfo.isRequired = value : _schemaInfo.isRequired;
            };

            this.rowNum = function (value) {
                return value !== undefined ? _schemaInfo.rowNum = value : _schemaInfo.rowNum;
            };

            this.tipMsg = function (value) {
                return value !== undefined ? _schemaInfo.tipMsg.push(value) : _schemaInfo.tipMsg;
            };

            this.checkValues = function (value) {
                return value !== undefined ? _schemaInfo.checkValues.push(value) : _schemaInfo.checkValues;
            };

            this.notShowEdit = function (value) {
                return value !== undefined ? _schemaInfo.notShowEdit = value : _schemaInfo.notShowEdit;
            };

            this.schemaInfo = function () {
                return _schemaInfo;
            };

            function htmlToString (htmls) {
                var result = "";
                angular.forEach(htmls, function (html) {
                    result += " " + html + " ";
                });
                return result;
            }
        };
    });