/**
 * Created by linanbin on 15/12/7.
 */

define([], function () {

    return function ($scope,productDetailService,$routeParams) {

        $scope.FieldTypeEnum  = {
            "input":"INPUT",
            "multiInput":"MULTIINPUT",
            "singleCheck":"SINGLECHECK",
            "multiCheck":"MULTICHECK",
            "complex":"COMPLEX",
            "multiComplex":"MULTICOMPLEX",
            "label":"LABEL"
        };

        $scope.RuleTypeEnum = {
            "MAX_LENGTH_RULE":"maxLengthRule",
            "MIN_LENGTH_RULE":"minLengthRule",
            "MAX_VALUE_RULE":"maxValueRule",
            "MIN_VALUE_RULE":"minValueRule",
            "MAX_INPUT_NUM_RULE":"maxInputNumRule",
            "MIN_INPUT_NUM_RULE":"minInputNumRule",
            "VALUE_TYPE_RULE":"valueTypeRule",
            "REQUIRED_RULE":"requiredRule",
            "DISABLE_RULE":"disableRule",
            "MAX_DECIMAL_DIGITS_RULE":"maxDecimalDigitsRule",
            "MIN_DECIMAL_DIGITS_RULE":"minDecimalDigitsRule",
            "REGEX_RULE":"regexRule",
            "SET_RULE":"setRule",
            "TIP_RULE":"tipRule",
            "DEV_TIP_RULE":"devTipRule",
            "READ_ONLY_RULE":"readOnlyRule",
            "MAX_TARGET_SIZE_RULE":"maxTargetSizeRule",
            "MIN_TARGET_SIZE_RULE":"minTargetSizeRule",
            "MAX_IMAGE_SIZE_RULE":"maxImageSizeRule",
            "MIN_IMAGE_SIZE_RULE":"minImageSizeRule"
        };

        $scope.ValueTypeEnum = {
            "TEXT":"text",
            "DECIMAL":"decimal",
            "INTEGER":"integer",
            "LONG":"long",
            "DATE":"date",
            "TIME":"time",
            "URL":"url",
            "TEXTAREA":"textarea",
            "HTML":"html"
        };

        this.productDetailService = productDetailService;

        productDetailService.getProductInfo({"productId":$routeParams.productId}).then(
            function (response){
                $scope.categorySchema = response.data.categorySchema;
                var fields = response.data.categorySchema.fields;
                $scope.tarFields = [];
                var isComplex = true;
                var isMultiComplex = true;

                $scope.tarFields.push($scope.categorySchema.sku);

                fields.forEach(function(item){
                    console.log(item);
                    if (isComplex && $scope.FieldTypeEnum.complex == item.type){
                        $scope.tarFields.push(item);
                        isComplex = false;
                    }

                    //if (isMultiComplex && $scope.FieldTypeEnum.multiComplex == item.type){
                    //    $scope.tarFields.push(item);
                    //    isMultiComplex = false;
                    //}
                });
                //console.log();
                //console.log($scope.tarFields);
            }
        )

    };
});
