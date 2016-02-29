/**
 * @User: linanbin
 * @Date: 15/12/25
 * @Version: 2.0.0
 */

angular.module('voyageone.angular.factories.schema', [])

    // 定义schemaHeaderFactory
    .factory('schemaHeaderFactory', function () {

        function SchemaHeaderFactory(config) {
            this.schemaHearInfo = config || {
                    isRequired: false,
                    //size: null,
                    isMultiComplex: false,
                    isComplex: false,
                    tipMsg: []
                };
        }

        SchemaHeaderFactory.prototype = {

            isRequired: function (value) {
                return value !== undefined ? this.schemaHearInfo.isRequired = value : this.schemaHearInfo.isRequired;
            },

            isComplex: function (value) {
                return value !== undefined ? this.schemaHearInfo.isComplex = value : this.schemaHearInfo.isComplex;
            },

            isMultiComplex: function (value) {
                return value !== undefined ? this.schemaHearInfo.isMultiComplex = value : this.schemaHearInfo.isMultiComplex;
            },

            tipMsg: function (value) {
                return value !== undefined ? this.schemaHearInfo.tipMsg.push(value) : this.schemaHearInfo.tipMsg;
            }

            // size: function (value) {
            //     return value !== undefined ? this.schemaHearInfo.size = value : this.schemaHearInfo.size;
            // }
        };

        return SchemaHeaderFactory;
    })

    // 定义schemaFactory
    .factory('schemaFactory', function () {

        function SchemaFactory(config) {
            this._schemaInfo = config || {
                    type: null,
                    name: null,
                    rowNum: null,
                    isRequired: false,
                    checkValues: [],
                    tipMsg: [],
                    html: [],
                    notShowEdit: true
                };
        }

        SchemaFactory.prototype = {

            type: function (value) {
                return value !== undefined ? this._schemaInfo.type = value : this._schemaInfo.type;
            },
            name: function (value) {
                return value !== undefined ? this._schemaInfo.name = value : this._schemaInfo.name;
            },
            html: function (value) {
                return value !== undefined ? this._schemaInfo.html.push(value) : this._schemaInfo.html.join(" ");
            },
            isRequired: function (value) {
                return value !== undefined ? this._schemaInfo.isRequired = value : this._schemaInfo.isRequired;
            },
            rowNum: function (value) {
                return value !== undefined ? this._schemaInfo.rowNum = value : this._schemaInfo.rowNum;
            },
            tipMsg: function (value) {
                return value !== undefined ? this._schemaInfo.tipMsg.push(value) : this._schemaInfo.tipMsg;
            },
            checkValues: function (value) {
                return value !== undefined ? this._schemaInfo.checkValues.push(value) : this._schemaInfo.checkValues;
            },
            notShowEdit: function (value) {
                return value !== undefined ? this._schemaInfo.notShowEdit = value : this._schemaInfo.notShowEdit;
            },
            schemaInfo: function () {
                return this._schemaInfo;
            }
        };

        return SchemaFactory;
    });