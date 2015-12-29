/**
 * Created by linanbin on 15/12/7.
 */

define([
    'modules/cms/controller/popup.ctl'
], function () {

    return function ($scope) {
        $scope.vm={};

        $scope.delNode = function(parent,node){
            var index;
            index=_.indexOf(parent,node);
            if(index >-1 ){
                parent=parent.splice(index,1);
            }
        }
        $scope.vm.category={
            "_id" : "5680fa6b62b84722eabeb29d",
            "created" : "2015-12-28 17:01:31",
            "creater" : "buildMasterSchemaFromPlatformTask",
            "modified" : "2015-12-28 17:01:31.276",
            "modifier" : "buildMasterSchemaFromPlatformTask",
            "catId" : "5aWz6KOFL+Wls+Wjq+eyvuWTgT7og4zlv4PlkIrluKY=",
            "catFullPath" : "女装/女士精品>背心吊带",
            "fields" : [{
                "id" : "prop_13021751",
                "name" : "货号",
                "type" : "INPUT",
                "rules" : [{
                    "name" : "requiredRule",
                    "value" : "true",
                    "valueIntervalInclude" : false
                }],
                "properties" : [],
                "inputLevel" : 1,
                "isDisplay" : 1
            }, {
                "id" : "material_prop_149422948",
                "name" : "材质成分",
                "type" : "MULTICOMPLEX",
                "rules" : [{
                    "name" : "maxInputNumRule",
                    "value" : "5",
                    "exProperty" : "include",
                    "valueIntervalInclude" : true
                }],
                "properties" : [],
                "inputLevel" : 1,
                "isDisplay" : 1,
                "values" : [],
                "fields" : [{
                    "id" : "material_prop_name",
                    "name" : "材质",
                    "type" : "MULTICOMPLEX",
                    "rules" : [],
                    "properties" : [],
                    "inputLevel" : 0,
                    "isDisplay" : 1,
                    "fields" : [{
                        "name" : "三醋酯纤维(三醋纤)",
                        "type" : "INPUT"
                    }, {
                        "name" : "亚麻",
                        "type" : "INPUT"
                    }],
                    "value" : { }
                }, {
                    "id" : "material_prop_content",
                    "name" : "含量(%)",
                    "type" : "INPUT",
                    "rules" : [{
                        "name" : "requiredRule",
                        "value" : "true",
                        "valueIntervalInclude" : false
                    }, {
                        "name" : "disableRule",
                        "value" : "true",
                        "dependGroup" : {
                            "dependExpressList" : [{
                                "fieldId" : "material_prop_name",
                                "value" : "头层牛皮",
                                "symbol" : "=="
                            }, {
                                "fieldId" : "material_prop_name",
                                "value" : "山羊皮",
                                "symbol" : "=="
                            }, {
                                "fieldId" : "material_prop_name",
                                "value" : "牛二层皮",
                                "symbol" : "=="
                            }, {
                                "fieldId" : "material_prop_name",
                                "value" : "猪皮",
                                "symbol" : "=="
                            }, {
                                "fieldId" : "material_prop_name",
                                "value" : "绵羊皮",
                                "symbol" : "=="
                            }, {
                                "fieldId" : "material_prop_name",
                                "value" : "鹿皮",
                                "symbol" : "=="
                            }],
                            "operator" : "or",
                            "dependGroupList" : [],
                            "empty" : false
                        },
                        "valueIntervalInclude" : false
                    }, {
                        "name" : "valueTypeRule",
                        "value" : "decimal",
                        "valueIntervalInclude" : false
                    }, {
                        "name" : "regexRule",
                        "value" : "^\\d+(\\.\\d{1,2})?$",
                        "valueIntervalInclude" : false
                    }, {
                        "name" : "minValueRule",
                        "value" : "0",
                        "exProperty" : "not include",
                        "valueIntervalInclude" : false
                    }, {
                        "name" : "maxValueRule",
                        "value" : "100",
                        "exProperty" : "include",
                        "valueIntervalInclude" : true
                    }],
                    "properties" : [],
                    "inputLevel" : 0,
                    "isDisplay" : 1
                }]
            }, {
                "id" : "prop_148380063",
                "name" : "销售渠道类型",
                "type" : "SINGLECHECK",
                "rules" : [],
                "properties" : [],
                "inputLevel" : 1,
                "isDisplay" : 1,
                "options" : [{
                    "displayName" : "纯电商(只在线上销售)",
                    "value" : "852538341"
                }],
                "value" : { }
            }, {
                "id" : "prop_122216586",
                "name" : "服装版型",
                "type" : "SINGLECHECK",
                "rules" : [],
                "properties" : [],
                "inputLevel" : 1,
                "isDisplay" : 1,
                "options" : [{
                    "displayName" : "宽松",
                    "value" : "4043538"
                }, {
                    "displayName" : "直筒",
                    "value" : "29947"
                }, {
                    "displayName" : "修身",
                    "value" : "130137"
                }],
                "value" : { }
            }, {
                "id" : "prop_122276315",
                "name" : "款式",
                "type" : "SINGLECHECK",
                "rules" : [],
                "properties" : [],
                "inputLevel" : 1,
                "isDisplay" : 1,
                "options" : [{
                    "displayName" : "挂脖式",
                    "value" : "3226839"
                }, {
                    "displayName" : "其他",
                    "value" : "20213"
                }, {
                    "displayName" : "斜肩",
                    "value" : "20858859"
                }, {
                    "displayName" : "背带",
                    "value" : "100343"
                }, {
                    "displayName" : "吊带",
                    "value" : "20495"
                }, {
                    "displayName" : "工字型",
                    "value" : "130208"
                }],
                "value" : { }
            }, {
                "id" : "prop_10142888",
                "name" : "组合形式",
                "type" : "SINGLECHECK",
                "rules" : [],
                "properties" : [],
                "inputLevel" : 1,
                "isDisplay" : 1,
                "options" : [{
                    "displayName" : "三件套",
                    "value" : "44632"
                }, {
                    "displayName" : "单件",
                    "value" : "3386071"
                }, {
                    "displayName" : "两件套",
                    "value" : "31605"
                }, {
                    "displayName" : "假两件",
                    "value" : "130567"
                }],
                "value" : { }
            }, {
                "id" : "prop_122216562",
                "name" : "衣长",
                "type" : "SINGLECHECK",
                "rules" : [],
                "properties" : [],
                "inputLevel" : 1,
                "isDisplay" : 1,
                "options" : [{
                    "displayName" : "中长款",
                    "value" : "44597"
                }, {
                    "displayName" : "短款",
                    "value" : "47502"
                }, {
                    "displayName" : "常规",
                    "value" : "3226292"
                }],
                "value" : { }
            }, {
                "id" : "prop_20603",
                "name" : "图案",
                "type" : "SINGLECHECK",
                "rules" : [],
                "properties" : [],
                "inputLevel" : 1,
                "isDisplay" : 1,
                "options" : [{
                    "displayName" : "条纹",
                    "value" : "29452"
                }, {
                    "displayName" : "格子",
                    "value" : "29453"
                }, {
                    "displayName" : "纯色",
                    "value" : "29454"
                }, {
                    "displayName" : "手绘",
                    "value" : "29455"
                }, {
                    "displayName" : "其他",
                    "value" : "20213"
                }],
                "value" : { }
            }, {
                "id" : "prop_8366967",
                "name" : "克重",
                "type" : "SINGLECHECK",
                "rules" : [],
                "properties" : [],
                "inputLevel" : 1,
                "isDisplay" : 1,
                "options" : [{
                    "displayName" : "401g/m^2 (含)-500g/m^2 (含)",
                    "value" : "494072243"
                }, {
                    "displayName" : "201g/m^2 (含)-250g/m^2 (含)",
                    "value" : "494072244"
                }, {
                    "displayName" : "101g/m^2 (含)-120g/m^2 (含)",
                    "value" : "494072245"
                }, {
                    "displayName" : "161g/m^2 (含)-180g/m^2 (含)",
                    "value" : "494072246"
                }, {
                    "displayName" : "121g/m^2 (含)-140g/m^2 (含)",
                    "value" : "494072247"
                }, {
                    "displayName" : "61g/m^2 (含)-80g/m^2 (含)",
                    "value" : "494072248"
                }, {
                    "displayName" : "351g/m^2 (含)-400g/m^2 (含)",
                    "value" : "494072249"
                }, {
                    "displayName" : "81g/m^2 (含)-100g/m^2 (含)",
                    "value" : "494072250"
                }, {
                    "displayName" : "141g/m^2 (含)-160g/m^2 (含)",
                    "value" : "494072251"
                }, {
                    "displayName" : "181g/m^2 (含)-200g/m^2 (含)",
                    "value" : "494072252"
                }, {
                    "displayName" : "501g/m^2 (含)-600g/m^2 (含)",
                    "value" : "494072253"
                }, {
                    "displayName" : "40g/m^2 及以下",
                    "value" : "494072254"
                }, {
                    "displayName" : "251g/m^2 (含)-300g/m^2 (含)",
                    "value" : "494072255"
                }, {
                    "displayName" : "41g/m^2 (含)-60g/m^2 (含)",
                    "value" : "494072256"
                }, {
                    "displayName" : "301g/m^2 (含)-350g/m^2 (含)",
                    "value" : "494072257"
                }, {
                    "displayName" : "601g/m^2 及以上",
                    "value" : "494072258"
                }],
                "value" : { }
            }, {
                "id" : "prop_20017",
                "name" : "适用年龄",
                "type" : "SINGLECHECK",
                "rules" : [],
                "properties" : [],
                "inputLevel" : 1,
                "isDisplay" : 1,
                "options" : [{
                    "displayName" : "18-24周岁",
                    "value" : "494072158"
                }, {
                    "displayName" : "25-29周岁",
                    "value" : "494072160"
                }, {
                    "displayName" : "30-34周岁",
                    "value" : "494072162"
                }, {
                    "displayName" : "35-39周岁",
                    "value" : "494072164"
                }, {
                    "displayName" : "40-49周岁",
                    "value" : "494072166"
                }, {
                    "displayName" : "17周岁以下",
                    "value" : "136515180"
                }],
                "value" : { }
            }, {
                "id" : "prop_122216347",
                "name" : "年份季节",
                "type" : "SINGLECHECK",
                "rules" : [{
                    "name" : "requiredRule",
                    "value" : "true",
                    "valueIntervalInclude" : false
                }],
                "properties" : [],
                "inputLevel" : 1,
                "isDisplay" : 1,
                "options" : [{
                    "displayName" : "2016年夏季",
                    "value" : "828914351"
                }, {
                    "displayName" : "2016年春季",
                    "value" : "854168429"
                }, {
                    "displayName" : "2014年冬季",
                    "value" : "379886796"
                }, {
                    "displayName" : "2014年夏季",
                    "value" : "379818839"
                }, {
                    "displayName" : "2014年春季",
                    "value" : "379930774"
                }, {
                    "displayName" : "2014年秋季",
                    "value" : "380120406"
                }, {
                    "displayName" : "2015年冬季",
                    "value" : "740132938"
                }, {
                    "displayName" : "2015年夏季",
                    "value" : "647672577"
                }, {
                    "displayName" : "2015年春季",
                    "value" : "379874864"
                }, {
                    "displayName" : "2015年秋季",
                    "value" : "715192583"
                }, {
                    "displayName" : "2011年秋季",
                    "value" : "96618833"
                }, {
                    "displayName" : "2012年夏季",
                    "value" : "132721297"
                }, {
                    "displayName" : "2012年冬季",
                    "value" : "132721335"
                }, {
                    "displayName" : "2012年春季",
                    "value" : "132721270"
                }, {
                    "displayName" : "2011年夏季",
                    "value" : "96618834"
                }, {
                    "displayName" : "2011年春季",
                    "value" : "94386424"
                }, {
                    "displayName" : "2011年冬季",
                    "value" : "96618832"
                }, {
                    "displayName" : "2012年秋季",
                    "value" : "132721317"
                }, {
                    "displayName" : "2013年夏季",
                    "value" : "186026840"
                }, {
                    "displayName" : "2013年春季",
                    "value" : "199870733"
                }, {
                    "displayName" : "2013年冬季",
                    "value" : "209928863"
                }, {
                    "displayName" : "2013年秋季",
                    "value" : "209928864"
                }],
                "value" : { }
            }, {
                "id" : "market_price",
                "name" : "市场价格",
                "type" : "INPUT",
                "rules" : [{
                    "name" : "valueTypeRule",
                    "value" : "decimal",
                    "valueIntervalInclude" : false
                }, {
                    "name" : "minValueRule",
                    "value" : "0.00",
                    "exProperty" : "not include",
                    "valueIntervalInclude" : false
                }, {
                    "name" : "maxValueRule",
                    "value" : "100000000.00",
                    "exProperty" : "not include",
                    "valueIntervalInclude" : false
                }],
                "properties" : [],
                "inputLevel" : 1,
                "isDisplay" : 1
            }, {
                "id" : "longTitle",
                "name" : "长标题",
                "type" : "INPUT",
                "rules" : [{
                    "name" : "valueTypeRule",
                    "value" : "text",
                    "valueIntervalInclude" : false
                }, {
                    "name" : "requiredRule",
                    "value" : "true",
                    "valueIntervalInclude" : false
                }, {
                    "name" : "minLengthRule",
                    "value" : "1",
                    "exProperty" : "include",
                    "unit" : "byte",
                    "valueIntervalInclude" : true
                }, {
                    "name" : "maxLengthRule",
                    "value" : "60",
                    "exProperty" : "include",
                    "unit" : "byte",
                    "valueIntervalInclude" : true
                }, {
                    "name" : "requiredRule",
                    "value" : "true",
                    "valueIntervalInclude" : false
                }],
                "properties" : [],
                "inputLevel" : 2,
                "inputOrgId" : "title",
                "isDisplay" : 1
            }, {
                "id" : "short_title",
                "name" : "无线短标题",
                "type" : "INPUT",
                "rules" : [{
                    "name" : "valueTypeRule",
                    "value" : "text",
                    "valueIntervalInclude" : false
                }, {
                    "name" : "requiredRule",
                    "value" : "true",
                    "valueIntervalInclude" : false
                }, {
                    "name" : "minLengthRule",
                    "value" : "1",
                    "exProperty" : "include",
                    "unit" : "byte",
                    "valueIntervalInclude" : true
                }, {
                    "name" : "maxLengthRule",
                    "value" : "20",
                    "exProperty" : "include",
                    "unit" : "byte",
                    "valueIntervalInclude" : true
                }, {
                    "name" : "tipRule",
                    "value" : "品牌+商品名称+卖点",
                    "url" : "https://img.alicdn.com/tps/i1/TB1jmZSFVXXXXbTXpXXyaz1LpXX-500-350.png",
                    "valueIntervalInclude" : false
                }],
                "properties" : [],
                "inputLevel" : 2,
                "isDisplay" : 1
            }, {
                "id" : "sell_points",
                "name" : "商品卖点",
                "type" : "COMPLEX",
                "rules" : [{
                    "name" : "minLengthRule",
                    "value" : "0",
                    "exProperty" : "include",
                    "unit" : "character",
                    "valueIntervalInclude" : true
                }, {
                    "name" : "maxLengthRule",
                    "value" : "20",
                    "exProperty" : "include",
                    "unit" : "character",
                    "valueIntervalInclude" : true
                }, {
                    "name" : "tipRule",
                    "value" : "最多5个卖点短语,每个卖点短语最多6个字,总字数不超过20个字",
                    "valueIntervalInclude" : false
                }],
                "properties" : [],
                "inputLevel" : 2,
                "isDisplay" : 1,
                "complexValue" : {
                    "fieldMap" : { }
                },
                "fields" : [{
                    "id" : "sell_point_0",
                    "name" : "商品卖点",
                    "type" : "INPUT",
                    "rules" : [{
                        "name" : "valueTypeRule",
                        "value" : "text",
                        "valueIntervalInclude" : false
                    }, {
                        "name" : "minLengthRule",
                        "value" : "0",
                        "exProperty" : "include",
                        "unit" : "character",
                        "valueIntervalInclude" : true
                    }, {
                        "name" : "maxLengthRule",
                        "value" : "6",
                        "exProperty" : "include",
                        "unit" : "character",
                        "valueIntervalInclude" : true
                    }],
                    "properties" : [],
                    "inputLevel" : 0,
                    "isDisplay" : 1
                }, {
                    "id" : "sell_point_1",
                    "name" : "商品卖点",
                    "type" : "INPUT",
                    "rules" : [{
                        "name" : "valueTypeRule",
                        "value" : "text",
                        "valueIntervalInclude" : false
                    }, {
                        "name" : "minLengthRule",
                        "value" : "0",
                        "exProperty" : "include",
                        "unit" : "character",
                        "valueIntervalInclude" : true
                    }, {
                        "name" : "maxLengthRule",
                        "value" : "6",
                        "exProperty" : "include",
                        "unit" : "character",
                        "valueIntervalInclude" : true
                    }],
                    "properties" : [],
                    "inputLevel" : 0,
                    "isDisplay" : 1
                }, {
                    "id" : "sell_point_2",
                    "name" : "商品卖点",
                    "type" : "INPUT",
                    "rules" : [{
                        "name" : "valueTypeRule",
                        "value" : "text",
                        "valueIntervalInclude" : false
                    }, {
                        "name" : "minLengthRule",
                        "value" : "0",
                        "exProperty" : "include",
                        "unit" : "character",
                        "valueIntervalInclude" : true
                    }, {
                        "name" : "maxLengthRule",
                        "value" : "6",
                        "exProperty" : "include",
                        "unit" : "character",
                        "valueIntervalInclude" : true
                    }],
                    "properties" : [],
                    "inputLevel" : 0,
                    "isDisplay" : 1
                }, {
                    "id" : "sell_point_3",
                    "name" : "商品卖点",
                    "type" : "INPUT",
                    "rules" : [{
                        "name" : "valueTypeRule",
                        "value" : "text",
                        "valueIntervalInclude" : false
                    }, {
                        "name" : "minLengthRule",
                        "value" : "0",
                        "exProperty" : "include",
                        "unit" : "character",
                        "valueIntervalInclude" : true
                    }, {
                        "name" : "maxLengthRule",
                        "value" : "6",
                        "exProperty" : "include",
                        "unit" : "character",
                        "valueIntervalInclude" : true
                    }],
                    "properties" : [],
                    "inputLevel" : 0,
                    "isDisplay" : 1
                }, {
                    "id" : "sell_point_4",
                    "name" : "商品卖点",
                    "type" : "INPUT",
                    "rules" : [{
                        "name" : "valueTypeRule",
                        "value" : "text",
                        "valueIntervalInclude" : false
                    }, {
                        "name" : "minLengthRule",
                        "value" : "0",
                        "exProperty" : "include",
                        "unit" : "character",
                        "valueIntervalInclude" : true
                    }, {
                        "name" : "maxLengthRule",
                        "value" : "6",
                        "exProperty" : "include",
                        "unit" : "character",
                        "valueIntervalInclude" : true
                    }],
                    "properties" : [],
                    "inputLevel" : 0,
                    "isDisplay" : 1
                }]
            }, {
                "id" : "start_time",
                "name" : "开始时间",
                "type" : "INPUT",
                "rules" : [{
                    "name" : "valueTypeRule",
                    "value" : "time",
                    "valueIntervalInclude" : false
                }, {
                    "name" : "disableRule",
                    "value" : "true",
                    "dependGroup" : {
                        "dependExpressList" : [{
                            "fieldId" : "item_status",
                            "value" : "1",
                            "symbol" : "!="
                        }],
                        "operator" : "and",
                        "dependGroupList" : [],
                        "empty" : false
                    },
                    "valueIntervalInclude" : false
                }],
                "properties" : [],
                "inputLevel" : 2,
                "isDisplay" : 1,
                "defaultValueField" : {
                    "type" : "INPUT",
                    "rules" : [],
                    "properties" : [],
                    "inputLevel" : 0,
                    "isDisplay" : 1,
                    "value" : "2015-12-28 16:50:05"
                }
            }, {
                "id" : "item_type",
                "name" : "发布类型",
                "type" : "SINGLECHECK",
                "rules" : [{
                    "name" : "requiredRule",
                    "value" : "true",
                    "valueIntervalInclude" : false
                }],
                "properties" : [],
                "inputLevel" : 2,
                "isDisplay" : 1,
                "defaultValueField" : {
                    "type" : "SINGLECHECK",
                    "rules" : [],
                    "properties" : [],
                    "inputLevel" : 0,
                    "isDisplay" : 1,
                    "options" : [],
                    "value" : {
                        "value" : "b"
                    }
                },
                "options" : [{
                    "displayName" : "一口价",
                    "value" : "b"
                }],
                "value" : { }
            }, {
                "id" : "stuff_status",
                "name" : "宝贝类型",
                "type" : "SINGLECHECK",
                "rules" : [{
                    "name" : "requiredRule",
                    "value" : "true",
                    "valueIntervalInclude" : false
                }],
                "properties" : [],
                "inputLevel" : 2,
                "isDisplay" : 1,
                "defaultValueField" : {
                    "type" : "SINGLECHECK",
                    "rules" : [],
                    "properties" : [],
                    "inputLevel" : 0,
                    "isDisplay" : 1,
                    "options" : [],
                    "value" : {
                        "value" : "5"
                    }
                },
                "options" : [{
                    "displayName" : "全新",
                    "value" : "5"
                }],
                "value" : { }
            }, {
                "id" : "sub_stock",
                "name" : "拍下减库存",
                "type" : "SINGLECHECK",
                "rules" : [{
                    "name" : "tipRule",
                    "value" : "秒杀商品或超低价抢购促销商品或热卖商品请选择拍下减库存，可以防止超卖情况！",
                    "url" : "https://service.taobao.com/support/knowledge-1110945.htm",
                    "valueIntervalInclude" : false
                }],
                "properties" : [],
                "inputLevel" : 2,
                "isDisplay" : 1,
                "defaultValueField" : {
                    "type" : "SINGLECHECK",
                    "rules" : [],
                    "properties" : [],
                    "inputLevel" : 0,
                    "isDisplay" : 1,
                    "options" : [],
                    "value" : {
                        "value" : "false"
                    }
                },
                "options" : [{
                    "displayName" : "是",
                    "value" : "true"
                }, {
                    "displayName" : "否",
                    "value" : "false"
                }],
                "value" : { }
            }, {
                "id" : "second_kill",
                "name" : "商品秒杀",
                "type" : "MULTICHECK",
                "rules" : [{
                    "name" : "tipRule",
                    "value" : "若此商品参加秒杀活动，在此期间内必须设为秒杀商品，以防止作弊",
                    "valueIntervalInclude" : false
                }, {
                    "name" : "tipRule",
                    "value" : "秒杀商品的商品详情页面将不出现“加入购物车”按钮",
                    "valueIntervalInclude" : false
                }],
                "properties" : [],
                "inputLevel" : 2,
                "isDisplay" : 1,
                "options" : [{
                    "displayName" : "电脑用户",
                    "value" : "web"
                }, {
                    "displayName" : "手机用户",
                    "value" : "wap"
                }],
                "values" : []
            }, {
                "id" : "prop_34272",
                "name" : "流行元素",
                "type" : "MULTICHECK",
                "rules" : [],
                "properties" : [],
                "inputLevel" : 2,
                "isDisplay" : 1,
                "options" : [{
                    "displayName" : "3D",
                    "value" : "3235817"
                }, {
                    "displayName" : "不对称",
                    "value" : "7642045"
                }, {
                    "displayName" : "亮丝",
                    "value" : "7573005"
                }, {
                    "displayName" : "亮片",
                    "value" : "29959"
                }, {
                    "displayName" : "做旧",
                    "value" : "112597"
                }, {
                    "displayName" : "刺绣/绣花",
                    "value" : "58828099"
                }, {
                    "displayName" : "印花",
                    "value" : "129555"
                }, {
                    "displayName" : "口袋",
                    "value" : "3243112"
                }, {
                    "displayName" : "扎染",
                    "value" : "5145675"
                }, {
                    "displayName" : "扎花",
                    "value" : "17665110"
                }, {
                    "displayName" : "抽褶",
                    "value" : "41118856"
                }, {
                    "displayName" : "拉链",
                    "value" : "115481"
                }, {
                    "displayName" : "拼接",
                    "value" : "9142620"
                }, {
                    "displayName" : "明线装饰",
                    "value" : "27436000"
                }, {
                    "displayName" : "木耳",
                    "value" : "42536"
                }, {
                    "displayName" : "植绒",
                    "value" : "3267928"
                }, {
                    "displayName" : "波浪",
                    "value" : "3424792"
                }, {
                    "displayName" : "流苏",
                    "value" : "115777"
                }, {
                    "displayName" : "燕尾",
                    "value" : "6061030"
                }, {
                    "displayName" : "破洞",
                    "value" : "3267932"
                }, {
                    "displayName" : "立体装饰",
                    "value" : "32971735"
                }, {
                    "displayName" : "系带",
                    "value" : "28102"
                }, {
                    "displayName" : "纱网",
                    "value" : "26325697"
                }, {
                    "displayName" : "纽扣",
                    "value" : "3693451"
                }, {
                    "displayName" : "绑带",
                    "value" : "3705386"
                }, {
                    "displayName" : "肩章",
                    "value" : "130138"
                }, {
                    "displayName" : "背带",
                    "value" : "100343"
                }, {
                    "displayName" : "荷叶边",
                    "value" : "130316"
                }, {
                    "displayName" : "蕾丝",
                    "value" : "28386"
                }, {
                    "displayName" : "蝴蝶结",
                    "value" : "115772"
                }, {
                    "displayName" : "螺纹",
                    "value" : "8611558"
                }, {
                    "displayName" : "褶皱",
                    "value" : "112602"
                }, {
                    "displayName" : "贴布",
                    "value" : "130845"
                }, {
                    "displayName" : "钉珠",
                    "value" : "29958"
                }, {
                    "displayName" : "铆钉",
                    "value" : "115776"
                }, {
                    "displayName" : "链条",
                    "value" : "4015931"
                }, {
                    "displayName" : "镂空",
                    "value" : "115771"
                }, {
                    "displayName" : "镶钻",
                    "value" : "3332415"
                }, {
                    "displayName" : "露背",
                    "value" : "7600710"
                }, {
                    "displayName" : "其它",
                    "value" : "-1"
                }],
                "values" : []
            }, {
                "id" : "in_prop_34272",
                "name" : "流行元素",
                "type" : "INPUT",
                "rules" : [{
                    "name" : "disableRule",
                    "value" : "true",
                    "dependGroup" : {
                        "dependExpressList" : [{
                            "fieldId" : "prop_34272",
                            "value" : "-1",
                            "symbol" : "not contains"
                        }],
                        "operator" : "and",
                        "dependGroupList" : [],
                        "empty" : false
                    },
                    "valueIntervalInclude" : false
                }],
                "properties" : [],
                "inputLevel" : 2,
                "isDisplay" : 1
            }, {
                "id" : "prop_20608",
                "name" : "风格",
                "type" : "SINGLECHECK",
                "rules" : [],
                "properties" : [],
                "inputLevel" : 2,
                "isDisplay" : 1,
                "options" : [{
                    "displayName" : "其他",
                    "value" : "20213"
                }, {
                    "displayName" : "原创设计",
                    "value" : "3223746"
                }, {
                    "displayName" : "瑞丽",
                    "value" : "29920"
                }, {
                    "displayName" : "甜美",
                    "value" : "3267776"
                }, {
                    "displayName" : "百搭",
                    "value" : "29921"
                }, {
                    "displayName" : "街头",
                    "value" : "29934"
                }, {
                    "displayName" : "通勤",
                    "value" : "6384766"
                }],
                "value" : { }
            }, {
                "id" : "std_size_group",
                "name" : "尺码分组",
                "type" : "SINGLECHECK",
                "rules" : [],
                "properties" : [],
                "inputLevel" : 2,
                "isDisplay" : 1,
                "defaultValueField" : {
                    "type" : "SINGLECHECK",
                    "rules" : [],
                    "properties" : [],
                    "inputLevel" : 0,
                    "isDisplay" : 1,
                    "options" : [],
                    "value" : {
                        "value" : "-1:自定义:-1"
                    }
                },
                "options" : [{
                    "displayName" : "自定义",
                    "value" : "-1:自定义:-1"
                }],
                "value" : { }
            }, {
                "id" : "std_size_extends_20509",
                "name" : "尺码扩展",
                "type" : "MULTICOMPLEX",
                "rules" : [{
                    "name" : "tipRule",
                    "value" : "尺码表中的字段至少两个维度必填。",
                    "valueIntervalInclude" : false
                }, {
                    "name" : "tipRule",
                    "value" : "尺码表中的字段若选择填写某个字段，则所有尺码对应的该字段信息均需填写。",
                    "valueIntervalInclude" : false
                }, {
                    "name" : "tipRule",
                    "value" : "因尺码表结构调整，您填写的数据项可能被调整为自定义项，不影响信息展示。",
                    "valueIntervalInclude" : false
                }],
                "properties" : [],
                "inputLevel" : 2,
                "isDisplay" : 1,
                "values" : [],
                "fields" : [{
                    "id" : "std_size_prop_20509_-1",
                    "name" : "“自定义”尺码",
                    "type" : "INPUT",
                    "rules" : [{
                        "name" : "tipRule",
                        "value" : "自定义尺码值只能输入以下格式：【数字/字母/数字；字母/字母，字母+数字 字母/数字；数字/字母；数字+字母；数字/数字；数字/数字/字母；字母；数字/数字+字母/字母；数字/数字+字母；数字】，并支持在上述格式前添加性别如“男/女/男童/女童”。若无支持的格式，可最多新增一个不在上述格式范围内的尺码值",
                        "valueIntervalInclude" : false
                    }, {
                        "name" : "disableRule",
                        "value" : "true",
                        "dependGroup" : {
                            "dependExpressList" : [{
                                "fieldId" : "std_size_group",
                                "value" : "-1:自定义:-1",
                                "symbol" : "!="
                            }],
                            "operator" : "and",
                            "dependGroupList" : [],
                            "empty" : false
                        },
                        "valueIntervalInclude" : false
                    }],
                    "properties" : [],
                    "inputLevel" : 0,
                    "isDisplay" : 1
                }, {
                    "id" : "size_tip",
                    "name" : "尺码备注",
                    "type" : "INPUT",
                    "rules" : [{
                        "name" : "minLengthRule",
                        "value" : "1",
                        "exProperty" : "include",
                        "unit" : "byte",
                        "valueIntervalInclude" : true
                    }, {
                        "name" : "maxLengthRule",
                        "value" : "30",
                        "exProperty" : "include",
                        "unit" : "byte",
                        "valueIntervalInclude" : true
                    }],
                    "properties" : [],
                    "inputLevel" : 0,
                    "isDisplay" : 1
                }, {
                    "id" : "size_mapping_shengao",
                    "name" : "身高（cm）",
                    "type" : "INPUT",
                    "rules" : [{
                        "name" : "disableRule",
                        "value" : "true",
                        "dependGroup" : {
                            "dependExpressList" : [{
                                "fieldId" : "std_size_prop_20509_-1",
                                "value" : "均码",
                                "symbol" : "=="
                            }],
                            "operator" : "or",
                            "dependGroupList" : [],
                            "empty" : false
                        },
                        "valueIntervalInclude" : false
                    }],
                    "properties" : [],
                    "inputLevel" : 0,
                    "isDisplay" : 1
                }, {
                    "id" : "size_mapping_shengao_range",
                    "name" : "身高（cm）",
                    "type" : "COMPLEX",
                    "rules" : [{
                        "name" : "disableRule",
                        "value" : "true",
                        "dependGroup" : {
                            "dependExpressList" : [{
                                "fieldId" : "std_size_prop_20509_-1",
                                "value" : "均码",
                                "symbol" : "!="
                            }],
                            "operator" : "and",
                            "dependGroupList" : [],
                            "empty" : false
                        },
                        "valueIntervalInclude" : false
                    }],
                    "properties" : [],
                    "inputLevel" : 0,
                    "isDisplay" : 1,
                    "complexValue" : {
                        "fieldMap" : { }
                    },
                    "fields" : [{
                        "id" : "size_mapping_shengao_from",
                        "name" : "最小值",
                        "type" : "INPUT",
                        "rules" : [],
                        "properties" : [],
                        "inputLevel" : 0,
                        "isDisplay" : 1
                    }, {
                        "id" : "size_mapping_shengao_to",
                        "name" : "最大值",
                        "type" : "INPUT",
                        "rules" : [],
                        "properties" : [],
                        "inputLevel" : 0,
                        "isDisplay" : 1
                    }]
                }, {
                    "id" : "size_mapping_tizhong",
                    "name" : "体重（公斤）",
                    "type" : "INPUT",
                    "rules" : [{
                        "name" : "disableRule",
                        "value" : "true",
                        "dependGroup" : {
                            "dependExpressList" : [{
                                "fieldId" : "std_size_prop_20509_-1",
                                "value" : "均码",
                                "symbol" : "=="
                            }],
                            "operator" : "or",
                            "dependGroupList" : [],
                            "empty" : false
                        },
                        "valueIntervalInclude" : false
                    }],
                    "properties" : [],
                    "inputLevel" : 0,
                    "isDisplay" : 1
                }, {
                    "id" : "size_mapping_tizhong_range",
                    "name" : "体重（公斤）",
                    "type" : "COMPLEX",
                    "rules" : [{
                        "name" : "disableRule",
                        "value" : "true",
                        "dependGroup" : {
                            "dependExpressList" : [{
                                "fieldId" : "std_size_prop_20509_-1",
                                "value" : "均码",
                                "symbol" : "!="
                            }],
                            "operator" : "and",
                            "dependGroupList" : [],
                            "empty" : false
                        },
                        "valueIntervalInclude" : false
                    }],
                    "properties" : [],
                    "inputLevel" : 0,
                    "isDisplay" : 1,
                    "complexValue" : {
                        "fieldMap" : { }
                    },
                    "fields" : [{
                        "id" : "size_mapping_tizhong_from",
                        "name" : "最小值",
                        "type" : "INPUT",
                        "rules" : [],
                        "properties" : [],
                        "inputLevel" : 0,
                        "isDisplay" : 1
                    }, {
                        "id" : "size_mapping_tizhong_to",
                        "name" : "最大值",
                        "type" : "INPUT",
                        "rules" : [],
                        "properties" : [],
                        "inputLevel" : 0,
                        "isDisplay" : 1
                    }]
                }, {
                    "id" : "size_mapping_jiankuan",
                    "name" : "肩宽（cm）",
                    "type" : "INPUT",
                    "rules" : [{
                        "name" : "disableRule",
                        "value" : "true",
                        "dependGroup" : {
                            "dependExpressList" : [{
                                "fieldId" : "std_size_prop_20509_-1",
                                "value" : "均码",
                                "symbol" : "=="
                            }],
                            "operator" : "or",
                            "dependGroupList" : [],
                            "empty" : false
                        },
                        "valueIntervalInclude" : false
                    }],
                    "properties" : [],
                    "inputLevel" : 0,
                    "isDisplay" : 1
                }, {
                    "id" : "size_mapping_jiankuan_range",
                    "name" : "肩宽（cm）",
                    "type" : "COMPLEX",
                    "rules" : [{
                        "name" : "disableRule",
                        "value" : "true",
                        "dependGroup" : {
                            "dependExpressList" : [{
                                "fieldId" : "std_size_prop_20509_-1",
                                "value" : "均码",
                                "symbol" : "!="
                            }],
                            "operator" : "and",
                            "dependGroupList" : [],
                            "empty" : false
                        },
                        "valueIntervalInclude" : false
                    }],
                    "properties" : [],
                    "inputLevel" : 0,
                    "isDisplay" : 1,
                    "complexValue" : {
                        "fieldMap" : { }
                    },
                    "fields" : [{
                        "id" : "size_mapping_jiankuan_from",
                        "name" : "最小值",
                        "type" : "INPUT",
                        "rules" : [],
                        "properties" : [],
                        "inputLevel" : 0,
                        "isDisplay" : 1
                    }, {
                        "id" : "size_mapping_jiankuan_to",
                        "name" : "最大值",
                        "type" : "INPUT",
                        "rules" : [],
                        "properties" : [],
                        "inputLevel" : 0,
                        "isDisplay" : 1
                    }]
                }, {
                    "id" : "size_mapping_xiongwei",
                    "name" : "胸围（cm）",
                    "type" : "INPUT",
                    "rules" : [{
                        "name" : "disableRule",
                        "value" : "true",
                        "dependGroup" : {
                            "dependExpressList" : [{
                                "fieldId" : "std_size_prop_20509_-1",
                                "value" : "均码",
                                "symbol" : "=="
                            }],
                            "operator" : "or",
                            "dependGroupList" : [],
                            "empty" : false
                        },
                        "valueIntervalInclude" : false
                    }],
                    "properties" : [],
                    "inputLevel" : 0,
                    "isDisplay" : 1
                }, {
                    "id" : "size_mapping_xiongwei_range",
                    "name" : "胸围（cm）",
                    "type" : "COMPLEX",
                    "rules" : [{
                        "name" : "disableRule",
                        "value" : "true",
                        "dependGroup" : {
                            "dependExpressList" : [{
                                "fieldId" : "std_size_prop_20509_-1",
                                "value" : "均码",
                                "symbol" : "!="
                            }],
                            "operator" : "and",
                            "dependGroupList" : [],
                            "empty" : false
                        },
                        "valueIntervalInclude" : false
                    }],
                    "properties" : [],
                    "inputLevel" : 0,
                    "isDisplay" : 1,
                    "complexValue" : {
                        "fieldMap" : { }
                    },
                    "fields" : [{
                        "id" : "size_mapping_xiongwei_from",
                        "name" : "最小值",
                        "type" : "INPUT",
                        "rules" : [],
                        "properties" : [],
                        "inputLevel" : 0,
                        "isDisplay" : 1
                    }, {
                        "id" : "size_mapping_xiongwei_to",
                        "name" : "最大值",
                        "type" : "INPUT",
                        "rules" : [],
                        "properties" : [],
                        "inputLevel" : 0,
                        "isDisplay" : 1
                    }]
                }, {
                    "id" : "size_mapping_yaowei",
                    "name" : "腰围（cm）",
                    "type" : "INPUT",
                    "rules" : [{
                        "name" : "disableRule",
                        "value" : "true",
                        "dependGroup" : {
                            "dependExpressList" : [{
                                "fieldId" : "std_size_prop_20509_-1",
                                "value" : "均码",
                                "symbol" : "=="
                            }],
                            "operator" : "or",
                            "dependGroupList" : [],
                            "empty" : false
                        },
                        "valueIntervalInclude" : false
                    }],
                    "properties" : [],
                    "inputLevel" : 0,
                    "isDisplay" : 1
                }, {
                    "id" : "size_mapping_yaowei_range",
                    "name" : "腰围（cm）",
                    "type" : "COMPLEX",
                    "rules" : [{
                        "name" : "disableRule",
                        "value" : "true",
                        "dependGroup" : {
                            "dependExpressList" : [{
                                "fieldId" : "std_size_prop_20509_-1",
                                "value" : "均码",
                                "symbol" : "!="
                            }],
                            "operator" : "and",
                            "dependGroupList" : [],
                            "empty" : false
                        },
                        "valueIntervalInclude" : false
                    }],
                    "properties" : [],
                    "inputLevel" : 0,
                    "isDisplay" : 1,
                    "complexValue" : {
                        "fieldMap" : { }
                    },
                    "fields" : [{
                        "id" : "size_mapping_yaowei_from",
                        "name" : "最小值",
                        "type" : "INPUT",
                        "rules" : [],
                        "properties" : [],
                        "inputLevel" : 0,
                        "isDisplay" : 1
                    }, {
                        "id" : "size_mapping_yaowei_to",
                        "name" : "最大值",
                        "type" : "INPUT",
                        "rules" : [],
                        "properties" : [],
                        "inputLevel" : 0,
                        "isDisplay" : 1
                    }]
                }, {
                    "id" : "size_mapping_xiuchang",
                    "name" : "袖长（cm）",
                    "type" : "INPUT",
                    "rules" : [{
                        "name" : "disableRule",
                        "value" : "true",
                        "dependGroup" : {
                            "dependExpressList" : [{
                                "fieldId" : "std_size_prop_20509_-1",
                                "value" : "均码",
                                "symbol" : "=="
                            }],
                            "operator" : "or",
                            "dependGroupList" : [],
                            "empty" : false
                        },
                        "valueIntervalInclude" : false
                    }],
                    "properties" : [],
                    "inputLevel" : 0,
                    "isDisplay" : 1
                }, {
                    "id" : "size_mapping_xiuchang_range",
                    "name" : "袖长（cm）",
                    "type" : "COMPLEX",
                    "rules" : [{
                        "name" : "disableRule",
                        "value" : "true",
                        "dependGroup" : {
                            "dependExpressList" : [{
                                "fieldId" : "std_size_prop_20509_-1",
                                "value" : "均码",
                                "symbol" : "!="
                            }],
                            "operator" : "and",
                            "dependGroupList" : [],
                            "empty" : false
                        },
                        "valueIntervalInclude" : false
                    }],
                    "properties" : [],
                    "inputLevel" : 0,
                    "isDisplay" : 1,
                    "complexValue" : {
                        "fieldMap" : { }
                    },
                    "fields" : [{
                        "id" : "size_mapping_xiuchang_from",
                        "name" : "最小值",
                        "type" : "INPUT",
                        "rules" : [],
                        "properties" : [],
                        "inputLevel" : 0,
                        "isDisplay" : 1
                    }, {
                        "id" : "size_mapping_xiuchang_to",
                        "name" : "最大值",
                        "type" : "INPUT",
                        "rules" : [],
                        "properties" : [],
                        "inputLevel" : 0,
                        "isDisplay" : 1
                    }]
                }, {
                    "id" : "size_mapping_yichang",
                    "name" : "衣长（cm）",
                    "type" : "INPUT",
                    "rules" : [{
                        "name" : "disableRule",
                        "value" : "true",
                        "dependGroup" : {
                            "dependExpressList" : [{
                                "fieldId" : "std_size_prop_20509_-1",
                                "value" : "均码",
                                "symbol" : "=="
                            }],
                            "operator" : "or",
                            "dependGroupList" : [],
                            "empty" : false
                        },
                        "valueIntervalInclude" : false
                    }],
                    "properties" : [],
                    "inputLevel" : 0,
                    "isDisplay" : 1
                }, {
                    "id" : "size_mapping_yichang_range",
                    "name" : "衣长（cm）",
                    "type" : "COMPLEX",
                    "rules" : [{
                        "name" : "disableRule",
                        "value" : "true",
                        "dependGroup" : {
                            "dependExpressList" : [{
                                "fieldId" : "std_size_prop_20509_-1",
                                "value" : "均码",
                                "symbol" : "!="
                            }],
                            "operator" : "and",
                            "dependGroupList" : [],
                            "empty" : false
                        },
                        "valueIntervalInclude" : false
                    }],
                    "properties" : [],
                    "inputLevel" : 0,
                    "isDisplay" : 1,
                    "complexValue" : {
                        "fieldMap" : { }
                    },
                    "fields" : [{
                        "id" : "size_mapping_yichang_from",
                        "name" : "最小值",
                        "type" : "INPUT",
                        "rules" : [],
                        "properties" : [],
                        "inputLevel" : 0,
                        "isDisplay" : 1
                    }, {
                        "id" : "size_mapping_yichang_to",
                        "name" : "最大值",
                        "type" : "INPUT",
                        "rules" : [],
                        "properties" : [],
                        "inputLevel" : 0,
                        "isDisplay" : 1
                    }]
                }, {
                    "id" : "size_mapping_beikuan",
                    "name" : "背宽（cm）",
                    "type" : "INPUT",
                    "rules" : [{
                        "name" : "disableRule",
                        "value" : "true",
                        "dependGroup" : {
                            "dependExpressList" : [{
                                "fieldId" : "std_size_prop_20509_-1",
                                "value" : "均码",
                                "symbol" : "=="
                            }],
                            "operator" : "or",
                            "dependGroupList" : [],
                            "empty" : false
                        },
                        "valueIntervalInclude" : false
                    }],
                    "properties" : [],
                    "inputLevel" : 0,
                    "isDisplay" : 1
                }, {
                    "id" : "size_mapping_beikuan_range",
                    "name" : "背宽（cm）",
                    "type" : "COMPLEX",
                    "rules" : [{
                        "name" : "disableRule",
                        "value" : "true",
                        "dependGroup" : {
                            "dependExpressList" : [{
                                "fieldId" : "std_size_prop_20509_-1",
                                "value" : "均码",
                                "symbol" : "!="
                            }],
                            "operator" : "and",
                            "dependGroupList" : [],
                            "empty" : false
                        },
                        "valueIntervalInclude" : false
                    }],
                    "properties" : [],
                    "inputLevel" : 0,
                    "isDisplay" : 1,
                    "complexValue" : {
                        "fieldMap" : { }
                    },
                    "fields" : [{
                        "id" : "size_mapping_beikuan_from",
                        "name" : "最小值",
                        "type" : "INPUT",
                        "rules" : [],
                        "properties" : [],
                        "inputLevel" : 0,
                        "isDisplay" : 1
                    }, {
                        "id" : "size_mapping_beikuan_to",
                        "name" : "最大值",
                        "type" : "INPUT",
                        "rules" : [],
                        "properties" : [],
                        "inputLevel" : 0,
                        "isDisplay" : 1
                    }]
                }, {
                    "id" : "size_mapping_qianchang",
                    "name" : "前长（cm）",
                    "type" : "INPUT",
                    "rules" : [{
                        "name" : "disableRule",
                        "value" : "true",
                        "dependGroup" : {
                            "dependExpressList" : [{
                                "fieldId" : "std_size_prop_20509_-1",
                                "value" : "均码",
                                "symbol" : "=="
                            }],
                            "operator" : "or",
                            "dependGroupList" : [],
                            "empty" : false
                        },
                        "valueIntervalInclude" : false
                    }],
                    "properties" : [],
                    "inputLevel" : 0,
                    "isDisplay" : 1
                }, {
                    "id" : "size_mapping_qianchang_range",
                    "name" : "前长（cm）",
                    "type" : "COMPLEX",
                    "rules" : [{
                        "name" : "disableRule",
                        "value" : "true",
                        "dependGroup" : {
                            "dependExpressList" : [{
                                "fieldId" : "std_size_prop_20509_-1",
                                "value" : "均码",
                                "symbol" : "!="
                            }],
                            "operator" : "and",
                            "dependGroupList" : [],
                            "empty" : false
                        },
                        "valueIntervalInclude" : false
                    }],
                    "properties" : [],
                    "inputLevel" : 0,
                    "isDisplay" : 1,
                    "complexValue" : {
                        "fieldMap" : { }
                    },
                    "fields" : [{
                        "id" : "size_mapping_qianchang_from",
                        "name" : "最小值",
                        "type" : "INPUT",
                        "rules" : [],
                        "properties" : [],
                        "inputLevel" : 0,
                        "isDisplay" : 1
                    }, {
                        "id" : "size_mapping_qianchang_to",
                        "name" : "最大值",
                        "type" : "INPUT",
                        "rules" : [],
                        "properties" : [],
                        "inputLevel" : 0,
                        "isDisplay" : 1
                    }]
                }, {
                    "id" : "size_mapping_baiwei",
                    "name" : "摆围（cm）",
                    "type" : "INPUT",
                    "rules" : [{
                        "name" : "disableRule",
                        "value" : "true",
                        "dependGroup" : {
                            "dependExpressList" : [{
                                "fieldId" : "std_size_prop_20509_-1",
                                "value" : "均码",
                                "symbol" : "=="
                            }],
                            "operator" : "or",
                            "dependGroupList" : [],
                            "empty" : false
                        },
                        "valueIntervalInclude" : false
                    }],
                    "properties" : [],
                    "inputLevel" : 0,
                    "isDisplay" : 1
                }, {
                    "id" : "size_mapping_baiwei_range",
                    "name" : "摆围（cm）",
                    "type" : "COMPLEX",
                    "rules" : [{
                        "name" : "disableRule",
                        "value" : "true",
                        "dependGroup" : {
                            "dependExpressList" : [{
                                "fieldId" : "std_size_prop_20509_-1",
                                "value" : "均码",
                                "symbol" : "!="
                            }],
                            "operator" : "and",
                            "dependGroupList" : [],
                            "empty" : false
                        },
                        "valueIntervalInclude" : false
                    }],
                    "properties" : [],
                    "inputLevel" : 0,
                    "isDisplay" : 1,
                    "complexValue" : {
                        "fieldMap" : { }
                    },
                    "fields" : [{
                        "id" : "size_mapping_baiwei_from",
                        "name" : "最小值",
                        "type" : "INPUT",
                        "rules" : [],
                        "properties" : [],
                        "inputLevel" : 0,
                        "isDisplay" : 1
                    }, {
                        "id" : "size_mapping_baiwei_to",
                        "name" : "最大值",
                        "type" : "INPUT",
                        "rules" : [],
                        "properties" : [],
                        "inputLevel" : 0,
                        "isDisplay" : 1
                    }]
                }, {
                    "id" : "size_mapping_xiabaiwei",
                    "name" : "下摆围（cm）",
                    "type" : "INPUT",
                    "rules" : [{
                        "name" : "disableRule",
                        "value" : "true",
                        "dependGroup" : {
                            "dependExpressList" : [{
                                "fieldId" : "std_size_prop_20509_-1",
                                "value" : "均码",
                                "symbol" : "=="
                            }],
                            "operator" : "or",
                            "dependGroupList" : [],
                            "empty" : false
                        },
                        "valueIntervalInclude" : false
                    }],
                    "properties" : [],
                    "inputLevel" : 0,
                    "isDisplay" : 1
                }, {
                    "id" : "size_mapping_xiabaiwei_range",
                    "name" : "下摆围（cm）",
                    "type" : "COMPLEX",
                    "rules" : [{
                        "name" : "disableRule",
                        "value" : "true",
                        "dependGroup" : {
                            "dependExpressList" : [{
                                "fieldId" : "std_size_prop_20509_-1",
                                "value" : "均码",
                                "symbol" : "!="
                            }],
                            "operator" : "and",
                            "dependGroupList" : [],
                            "empty" : false
                        },
                        "valueIntervalInclude" : false
                    }],
                    "properties" : [],
                    "inputLevel" : 0,
                    "isDisplay" : 1,
                    "complexValue" : {
                        "fieldMap" : { }
                    },
                    "fields" : [{
                        "id" : "size_mapping_xiabaiwei_from",
                        "name" : "最小值",
                        "type" : "INPUT",
                        "rules" : [],
                        "properties" : [],
                        "inputLevel" : 0,
                        "isDisplay" : 1
                    }, {
                        "id" : "size_mapping_xiabaiwei_to",
                        "name" : "最大值",
                        "type" : "INPUT",
                        "rules" : [],
                        "properties" : [],
                        "inputLevel" : 0,
                        "isDisplay" : 1
                    }]
                }, {
                    "id" : "size_mapping_xiukou",
                    "name" : "袖口（cm）",
                    "type" : "INPUT",
                    "rules" : [{
                        "name" : "disableRule",
                        "value" : "true",
                        "dependGroup" : {
                            "dependExpressList" : [{
                                "fieldId" : "std_size_prop_20509_-1",
                                "value" : "均码",
                                "symbol" : "=="
                            }],
                            "operator" : "or",
                            "dependGroupList" : [],
                            "empty" : false
                        },
                        "valueIntervalInclude" : false
                    }],
                    "properties" : [],
                    "inputLevel" : 0,
                    "isDisplay" : 1
                }, {
                    "id" : "size_mapping_xiukou_range",
                    "name" : "袖口（cm）",
                    "type" : "COMPLEX",
                    "rules" : [{
                        "name" : "disableRule",
                        "value" : "true",
                        "dependGroup" : {
                            "dependExpressList" : [{
                                "fieldId" : "std_size_prop_20509_-1",
                                "value" : "均码",
                                "symbol" : "!="
                            }],
                            "operator" : "and",
                            "dependGroupList" : [],
                            "empty" : false
                        },
                        "valueIntervalInclude" : false
                    }],
                    "properties" : [],
                    "inputLevel" : 0,
                    "isDisplay" : 1,
                    "complexValue" : {
                        "fieldMap" : { }
                    },
                    "fields" : [{
                        "id" : "size_mapping_xiukou_from",
                        "name" : "最小值",
                        "type" : "INPUT",
                        "rules" : [],
                        "properties" : [],
                        "inputLevel" : 0,
                        "isDisplay" : 1
                    }, {
                        "id" : "size_mapping_xiukou_to",
                        "name" : "最大值",
                        "type" : "INPUT",
                        "rules" : [],
                        "properties" : [],
                        "inputLevel" : 0,
                        "isDisplay" : 1
                    }]
                }, {
                    "id" : "size_mapping_xiufei",
                    "name" : "袖肥（cm）",
                    "type" : "INPUT",
                    "rules" : [{
                        "name" : "disableRule",
                        "value" : "true",
                        "dependGroup" : {
                            "dependExpressList" : [{
                                "fieldId" : "std_size_prop_20509_-1",
                                "value" : "均码",
                                "symbol" : "=="
                            }],
                            "operator" : "or",
                            "dependGroupList" : [],
                            "empty" : false
                        },
                        "valueIntervalInclude" : false
                    }],
                    "properties" : [],
                    "inputLevel" : 0,
                    "isDisplay" : 1
                }, {
                    "id" : "size_mapping_xiufei_range",
                    "name" : "袖肥（cm）",
                    "type" : "COMPLEX",
                    "rules" : [{
                        "name" : "disableRule",
                        "value" : "true",
                        "dependGroup" : {
                            "dependExpressList" : [{
                                "fieldId" : "std_size_prop_20509_-1",
                                "value" : "均码",
                                "symbol" : "!="
                            }],
                            "operator" : "and",
                            "dependGroupList" : [],
                            "empty" : false
                        },
                        "valueIntervalInclude" : false
                    }],
                    "properties" : [],
                    "inputLevel" : 0,
                    "isDisplay" : 1,
                    "complexValue" : {
                        "fieldMap" : { }
                    },
                    "fields" : [{
                        "id" : "size_mapping_xiufei_from",
                        "name" : "最小值",
                        "type" : "INPUT",
                        "rules" : [],
                        "properties" : [],
                        "inputLevel" : 0,
                        "isDisplay" : 1
                    }, {
                        "id" : "size_mapping_xiufei_to",
                        "name" : "最大值",
                        "type" : "INPUT",
                        "rules" : [],
                        "properties" : [],
                        "inputLevel" : 0,
                        "isDisplay" : 1
                    }]
                }, {
                    "id" : "size_mapping_zhongyao",
                    "name" : "中腰（cm）",
                    "type" : "INPUT",
                    "rules" : [{
                        "name" : "disableRule",
                        "value" : "true",
                        "dependGroup" : {
                            "dependExpressList" : [{
                                "fieldId" : "std_size_prop_20509_-1",
                                "value" : "均码",
                                "symbol" : "=="
                            }],
                            "operator" : "or",
                            "dependGroupList" : [],
                            "empty" : false
                        },
                        "valueIntervalInclude" : false
                    }],
                    "properties" : [],
                    "inputLevel" : 0,
                    "isDisplay" : 1
                }, {
                    "id" : "size_mapping_zhongyao_range",
                    "name" : "中腰（cm）",
                    "type" : "COMPLEX",
                    "rules" : [{
                        "name" : "disableRule",
                        "value" : "true",
                        "dependGroup" : {
                            "dependExpressList" : [{
                                "fieldId" : "std_size_prop_20509_-1",
                                "value" : "均码",
                                "symbol" : "!="
                            }],
                            "operator" : "and",
                            "dependGroupList" : [],
                            "empty" : false
                        },
                        "valueIntervalInclude" : false
                    }],
                    "properties" : [],
                    "inputLevel" : 0,
                    "isDisplay" : 1,
                    "complexValue" : {
                        "fieldMap" : { }
                    },
                    "fields" : [{
                        "id" : "size_mapping_zhongyao_from",
                        "name" : "最小值",
                        "type" : "INPUT",
                        "rules" : [],
                        "properties" : [],
                        "inputLevel" : 0,
                        "isDisplay" : 1
                    }, {
                        "id" : "size_mapping_zhongyao_to",
                        "name" : "最大值",
                        "type" : "INPUT",
                        "rules" : [],
                        "properties" : [],
                        "inputLevel" : 0,
                        "isDisplay" : 1
                    }]
                }, {
                    "id" : "size_mapping_lingshen",
                    "name" : "领深（cm）",
                    "type" : "INPUT",
                    "rules" : [{
                        "name" : "disableRule",
                        "value" : "true",
                        "dependGroup" : {
                            "dependExpressList" : [{
                                "fieldId" : "std_size_prop_20509_-1",
                                "value" : "均码",
                                "symbol" : "=="
                            }],
                            "operator" : "or",
                            "dependGroupList" : [],
                            "empty" : false
                        },
                        "valueIntervalInclude" : false
                    }],
                    "properties" : [],
                    "inputLevel" : 0,
                    "isDisplay" : 1
                }, {
                    "id" : "size_mapping_lingshen_range",
                    "name" : "领深（cm）",
                    "type" : "COMPLEX",
                    "rules" : [{
                        "name" : "disableRule",
                        "value" : "true",
                        "dependGroup" : {
                            "dependExpressList" : [{
                                "fieldId" : "std_size_prop_20509_-1",
                                "value" : "均码",
                                "symbol" : "!="
                            }],
                            "operator" : "and",
                            "dependGroupList" : [],
                            "empty" : false
                        },
                        "valueIntervalInclude" : false
                    }],
                    "properties" : [],
                    "inputLevel" : 0,
                    "isDisplay" : 1,
                    "complexValue" : {
                        "fieldMap" : { }
                    },
                    "fields" : [{
                        "id" : "size_mapping_lingshen_from",
                        "name" : "最小值",
                        "type" : "INPUT",
                        "rules" : [],
                        "properties" : [],
                        "inputLevel" : 0,
                        "isDisplay" : 1
                    }, {
                        "id" : "size_mapping_lingshen_to",
                        "name" : "最大值",
                        "type" : "INPUT",
                        "rules" : [],
                        "properties" : [],
                        "inputLevel" : 0,
                        "isDisplay" : 1
                    }]
                }, {
                    "id" : "size_mapping_linggao",
                    "name" : "领高（cm）",
                    "type" : "INPUT",
                    "rules" : [{
                        "name" : "disableRule",
                        "value" : "true",
                        "dependGroup" : {
                            "dependExpressList" : [{
                                "fieldId" : "std_size_prop_20509_-1",
                                "value" : "均码",
                                "symbol" : "=="
                            }],
                            "operator" : "or",
                            "dependGroupList" : [],
                            "empty" : false
                        },
                        "valueIntervalInclude" : false
                    }],
                    "properties" : [],
                    "inputLevel" : 0,
                    "isDisplay" : 1
                }, {
                    "id" : "size_mapping_linggao_range",
                    "name" : "领高（cm）",
                    "type" : "COMPLEX",
                    "rules" : [{
                        "name" : "disableRule",
                        "value" : "true",
                        "dependGroup" : {
                            "dependExpressList" : [{
                                "fieldId" : "std_size_prop_20509_-1",
                                "value" : "均码",
                                "symbol" : "!="
                            }],
                            "operator" : "and",
                            "dependGroupList" : [],
                            "empty" : false
                        },
                        "valueIntervalInclude" : false
                    }],
                    "properties" : [],
                    "inputLevel" : 0,
                    "isDisplay" : 1,
                    "complexValue" : {
                        "fieldMap" : { }
                    },
                    "fields" : [{
                        "id" : "size_mapping_linggao_from",
                        "name" : "最小值",
                        "type" : "INPUT",
                        "rules" : [],
                        "properties" : [],
                        "inputLevel" : 0,
                        "isDisplay" : 1
                    }, {
                        "id" : "size_mapping_linggao_to",
                        "name" : "最大值",
                        "type" : "INPUT",
                        "rules" : [],
                        "properties" : [],
                        "inputLevel" : 0,
                        "isDisplay" : 1
                    }]
                }, {
                    "id" : "size_mapping_lingkuan",
                    "name" : "领宽（cm）",
                    "type" : "INPUT",
                    "rules" : [{
                        "name" : "disableRule",
                        "value" : "true",
                        "dependGroup" : {
                            "dependExpressList" : [{
                                "fieldId" : "std_size_prop_20509_-1",
                                "value" : "均码",
                                "symbol" : "=="
                            }],
                            "operator" : "or",
                            "dependGroupList" : [],
                            "empty" : false
                        },
                        "valueIntervalInclude" : false
                    }],
                    "properties" : [],
                    "inputLevel" : 0,
                    "isDisplay" : 1
                }, {
                    "id" : "size_mapping_lingkuan_range",
                    "name" : "领宽（cm）",
                    "type" : "COMPLEX",
                    "rules" : [{
                        "name" : "disableRule",
                        "value" : "true",
                        "dependGroup" : {
                            "dependExpressList" : [{
                                "fieldId" : "std_size_prop_20509_-1",
                                "value" : "均码",
                                "symbol" : "!="
                            }],
                            "operator" : "and",
                            "dependGroupList" : [],
                            "empty" : false
                        },
                        "valueIntervalInclude" : false
                    }],
                    "properties" : [],
                    "inputLevel" : 0,
                    "isDisplay" : 1,
                    "complexValue" : {
                        "fieldMap" : { }
                    },
                    "fields" : [{
                        "id" : "size_mapping_lingkuan_from",
                        "name" : "最小值",
                        "type" : "INPUT",
                        "rules" : [],
                        "properties" : [],
                        "inputLevel" : 0,
                        "isDisplay" : 1
                    }, {
                        "id" : "size_mapping_lingkuan_to",
                        "name" : "最大值",
                        "type" : "INPUT",
                        "rules" : [],
                        "properties" : [],
                        "inputLevel" : 0,
                        "isDisplay" : 1
                    }]
                }, {
                    "id" : "size_mapping_lingwei",
                    "name" : "领围（cm）",
                    "type" : "INPUT",
                    "rules" : [{
                        "name" : "disableRule",
                        "value" : "true",
                        "dependGroup" : {
                            "dependExpressList" : [{
                                "fieldId" : "std_size_prop_20509_-1",
                                "value" : "均码",
                                "symbol" : "=="
                            }],
                            "operator" : "or",
                            "dependGroupList" : [],
                            "empty" : false
                        },
                        "valueIntervalInclude" : false
                    }],
                    "properties" : [],
                    "inputLevel" : 0,
                    "isDisplay" : 1
                }, {
                    "id" : "size_mapping_lingwei_range",
                    "name" : "领围（cm）",
                    "type" : "COMPLEX",
                    "rules" : [{
                        "name" : "disableRule",
                        "value" : "true",
                        "dependGroup" : {
                            "dependExpressList" : [{
                                "fieldId" : "std_size_prop_20509_-1",
                                "value" : "均码",
                                "symbol" : "!="
                            }],
                            "operator" : "and",
                            "dependGroupList" : [],
                            "empty" : false
                        },
                        "valueIntervalInclude" : false
                    }],
                    "properties" : [],
                    "inputLevel" : 0,
                    "isDisplay" : 1,
                    "complexValue" : {
                        "fieldMap" : { }
                    },
                    "fields" : [{
                        "id" : "size_mapping_lingwei_from",
                        "name" : "最小值",
                        "type" : "INPUT",
                        "rules" : [],
                        "properties" : [],
                        "inputLevel" : 0,
                        "isDisplay" : 1
                    }, {
                        "id" : "size_mapping_lingwei_to",
                        "name" : "最大值",
                        "type" : "INPUT",
                        "rules" : [],
                        "properties" : [],
                        "inputLevel" : 0,
                        "isDisplay" : 1
                    }]
                }, {
                    "id" : "size_mapping_yuanbaihou",
                    "name" : "圆摆后中长（cm）",
                    "type" : "INPUT",
                    "rules" : [{
                        "name" : "disableRule",
                        "value" : "true",
                        "dependGroup" : {
                            "dependExpressList" : [{
                                "fieldId" : "std_size_prop_20509_-1",
                                "value" : "均码",
                                "symbol" : "=="
                            }],
                            "operator" : "or",
                            "dependGroupList" : [],
                            "empty" : false
                        },
                        "valueIntervalInclude" : false
                    }],
                    "properties" : [],
                    "inputLevel" : 0,
                    "isDisplay" : 1
                }, {
                    "id" : "size_mapping_yuanbaihou_range",
                    "name" : "圆摆后中长（cm）",
                    "type" : "COMPLEX",
                    "rules" : [{
                        "name" : "disableRule",
                        "value" : "true",
                        "dependGroup" : {
                            "dependExpressList" : [{
                                "fieldId" : "std_size_prop_20509_-1",
                                "value" : "均码",
                                "symbol" : "!="
                            }],
                            "operator" : "and",
                            "dependGroupList" : [],
                            "empty" : false
                        },
                        "valueIntervalInclude" : false
                    }],
                    "properties" : [],
                    "inputLevel" : 0,
                    "isDisplay" : 1,
                    "complexValue" : {
                        "fieldMap" : { }
                    },
                    "fields" : [{
                        "id" : "size_mapping_yuanbaihou_from",
                        "name" : "最小值",
                        "type" : "INPUT",
                        "rules" : [],
                        "properties" : [],
                        "inputLevel" : 0,
                        "isDisplay" : 1
                    }, {
                        "id" : "size_mapping_yuanbaihou_to",
                        "name" : "最大值",
                        "type" : "INPUT",
                        "rules" : [],
                        "properties" : [],
                        "inputLevel" : 0,
                        "isDisplay" : 1
                    }]
                }, {
                    "id" : "size_mapping_pingbai",
                    "name" : "平摆衣长（cm）",
                    "type" : "INPUT",
                    "rules" : [{
                        "name" : "disableRule",
                        "value" : "true",
                        "dependGroup" : {
                            "dependExpressList" : [{
                                "fieldId" : "std_size_prop_20509_-1",
                                "value" : "均码",
                                "symbol" : "=="
                            }],
                            "operator" : "or",
                            "dependGroupList" : [],
                            "empty" : false
                        },
                        "valueIntervalInclude" : false
                    }],
                    "properties" : [],
                    "inputLevel" : 0,
                    "isDisplay" : 1
                }, {
                    "id" : "size_mapping_pingbai_range",
                    "name" : "平摆衣长（cm）",
                    "type" : "COMPLEX",
                    "rules" : [{
                        "name" : "disableRule",
                        "value" : "true",
                        "dependGroup" : {
                            "dependExpressList" : [{
                                "fieldId" : "std_size_prop_20509_-1",
                                "value" : "均码",
                                "symbol" : "!="
                            }],
                            "operator" : "and",
                            "dependGroupList" : [],
                            "empty" : false
                        },
                        "valueIntervalInclude" : false
                    }],
                    "properties" : [],
                    "inputLevel" : 0,
                    "isDisplay" : 1,
                    "complexValue" : {
                        "fieldMap" : { }
                    },
                    "fields" : [{
                        "id" : "size_mapping_pingbai_from",
                        "name" : "最小值",
                        "type" : "INPUT",
                        "rules" : [],
                        "properties" : [],
                        "inputLevel" : 0,
                        "isDisplay" : 1
                    }, {
                        "id" : "size_mapping_pingbai_to",
                        "name" : "最大值",
                        "type" : "INPUT",
                        "rules" : [],
                        "properties" : [],
                        "inputLevel" : 0,
                        "isDisplay" : 1
                    }]
                }, {
                    "id" : "size_mapping_yuanbai",
                    "name" : "圆摆衣长（cm）",
                    "type" : "INPUT",
                    "rules" : [{
                        "name" : "disableRule",
                        "value" : "true",
                        "dependGroup" : {
                            "dependExpressList" : [{
                                "fieldId" : "std_size_prop_20509_-1",
                                "value" : "均码",
                                "symbol" : "=="
                            }],
                            "operator" : "or",
                            "dependGroupList" : [],
                            "empty" : false
                        },
                        "valueIntervalInclude" : false
                    }],
                    "properties" : [],
                    "inputLevel" : 0,
                    "isDisplay" : 1
                }, {
                    "id" : "size_mapping_yuanbai_range",
                    "name" : "圆摆衣长（cm）",
                    "type" : "COMPLEX",
                    "rules" : [{
                        "name" : "disableRule",
                        "value" : "true",
                        "dependGroup" : {
                            "dependExpressList" : [{
                                "fieldId" : "std_size_prop_20509_-1",
                                "value" : "均码",
                                "symbol" : "!="
                            }],
                            "operator" : "and",
                            "dependGroupList" : [],
                            "empty" : false
                        },
                        "valueIntervalInclude" : false
                    }],
                    "properties" : [],
                    "inputLevel" : 0,
                    "isDisplay" : 1,
                    "complexValue" : {
                        "fieldMap" : { }
                    },
                    "fields" : [{
                        "id" : "size_mapping_yuanbai_from",
                        "name" : "最小值",
                        "type" : "INPUT",
                        "rules" : [],
                        "properties" : [],
                        "inputLevel" : 0,
                        "isDisplay" : 1
                    }, {
                        "id" : "size_mapping_yuanbai_to",
                        "name" : "最大值",
                        "type" : "INPUT",
                        "rules" : [],
                        "properties" : [],
                        "inputLevel" : 0,
                        "isDisplay" : 1
                    }]
                }, {
                    "id" : "size_mapping_-1",
                    "name" : "尺码表自定义字段-1",
                    "type" : "COMPLEX",
                    "rules" : [],
                    "properties" : [],
                    "inputLevel" : 0,
                    "isDisplay" : 1,
                    "complexValue" : {
                        "fieldMap" : { }
                    },
                    "fields" : [{
                        "id" : "size_mapping_-1_name",
                        "name" : "尺码表自定义字段-1名称",
                        "type" : "INPUT",
                        "rules" : [{
                            "name" : "146134104_1",
                            "value" : "尺码表自定义字段名称不能包含数字",
                            "valueIntervalInclude" : false
                        }, {
                            "name" : "1502077407_1",
                            "value" : "尺码表自定义字段名称不能包含逗号、冒号、分号、星号、短横线等特殊符号",
                            "valueIntervalInclude" : false
                        }],
                        "properties" : [],
                        "inputLevel" : 0,
                        "isDisplay" : 1
                    }, {
                        "id" : "size_mapping_-1_value",
                        "name" : "尺码表自定义字段-1值",
                        "type" : "INPUT",
                        "rules" : [{
                            "name" : "disableRule",
                            "value" : "true",
                            "dependGroup" : {
                                "dependExpressList" : [{
                                    "fieldId" : "std_size_prop_20509_-1",
                                    "value" : "均码",
                                    "symbol" : "=="
                                }],
                                "operator" : "or",
                                "dependGroupList" : [],
                                "empty" : false
                            },
                            "valueIntervalInclude" : false
                        }],
                        "properties" : [],
                        "inputLevel" : 0,
                        "isDisplay" : 1
                    }, {
                        "id" : "size_mapping_-1_value_range",
                        "name" : "尺码表自定义字段-1值",
                        "type" : "COMPLEX",
                        "rules" : [{
                            "name" : "disableRule",
                            "value" : "true",
                            "dependGroup" : {
                                "dependExpressList" : [{
                                    "fieldId" : "std_size_prop_20509_-1",
                                    "value" : "均码",
                                    "symbol" : "!="
                                }],
                                "operator" : "and",
                                "dependGroupList" : [],
                                "empty" : false
                            },
                            "valueIntervalInclude" : false
                        }],
                        "properties" : [],
                        "inputLevel" : 0,
                        "isDisplay" : 1,
                        "complexValue" : {
                            "fieldMap" : { }
                        },
                        "fields" : [{
                            "id" : "size_mapping_-1_value_from",
                            "name" : "最小值",
                            "type" : "INPUT",
                            "rules" : [],
                            "properties" : [],
                            "inputLevel" : 0,
                            "isDisplay" : 1
                        }, {
                            "id" : "size_mapping_-1_value_to",
                            "name" : "最大值",
                            "type" : "INPUT",
                            "rules" : [],
                            "properties" : [],
                            "inputLevel" : 0,
                            "isDisplay" : 1
                        }]
                    }]
                }, {
                    "id" : "size_mapping_-2",
                    "name" : "尺码表自定义字段-2",
                    "type" : "COMPLEX",
                    "rules" : [],
                    "properties" : [],
                    "inputLevel" : 0,
                    "isDisplay" : 1,
                    "complexValue" : {
                        "fieldMap" : { }
                    },
                    "fields" : [{
                        "id" : "size_mapping_-2_name",
                        "name" : "尺码表自定义字段-2名称",
                        "type" : "INPUT",
                        "rules" : [{
                            "name" : "146134104_1",
                            "value" : "尺码表自定义字段名称不能包含数字",
                            "valueIntervalInclude" : false
                        }, {
                            "name" : "1502077407_1",
                            "value" : "尺码表自定义字段名称不能包含逗号、冒号、分号、星号、短横线等特殊符号",
                            "valueIntervalInclude" : false
                        }],
                        "properties" : [],
                        "inputLevel" : 0,
                        "isDisplay" : 1
                    }, {
                        "id" : "size_mapping_-2_value",
                        "name" : "尺码表自定义字段-2值",
                        "type" : "INPUT",
                        "rules" : [{
                            "name" : "disableRule",
                            "value" : "true",
                            "dependGroup" : {
                                "dependExpressList" : [{
                                    "fieldId" : "std_size_prop_20509_-1",
                                    "value" : "均码",
                                    "symbol" : "=="
                                }],
                                "operator" : "or",
                                "dependGroupList" : [],
                                "empty" : false
                            },
                            "valueIntervalInclude" : false
                        }],
                        "properties" : [],
                        "inputLevel" : 0,
                        "isDisplay" : 1
                    }, {
                        "id" : "size_mapping_-2_value_range",
                        "name" : "尺码表自定义字段-2值",
                        "type" : "COMPLEX",
                        "rules" : [{
                            "name" : "disableRule",
                            "value" : "true",
                            "dependGroup" : {
                                "dependExpressList" : [{
                                    "fieldId" : "std_size_prop_20509_-1",
                                    "value" : "均码",
                                    "symbol" : "!="
                                }],
                                "operator" : "and",
                                "dependGroupList" : [],
                                "empty" : false
                            },
                            "valueIntervalInclude" : false
                        }],
                        "properties" : [],
                        "inputLevel" : 0,
                        "isDisplay" : 1,
                        "complexValue" : {
                            "fieldMap" : { }
                        },
                        "fields" : [{
                            "id" : "size_mapping_-2_value_from",
                            "name" : "最小值",
                            "type" : "INPUT",
                            "rules" : [],
                            "properties" : [],
                            "inputLevel" : 0,
                            "isDisplay" : 1
                        }, {
                            "id" : "size_mapping_-2_value_to",
                            "name" : "最大值",
                            "type" : "INPUT",
                            "rules" : [],
                            "properties" : [],
                            "inputLevel" : 0,
                            "isDisplay" : 1
                        }]
                    }]
                }, {
                    "id" : "size_mapping_-3",
                    "name" : "尺码表自定义字段-3",
                    "type" : "COMPLEX",
                    "rules" : [],
                    "properties" : [],
                    "inputLevel" : 0,
                    "isDisplay" : 1,
                    "complexValue" : {
                        "fieldMap" : { }
                    },
                    "fields" : [{
                        "id" : "size_mapping_-3_name",
                        "name" : "尺码表自定义字段-3名称",
                        "type" : "INPUT",
                        "rules" : [{
                            "name" : "146134104_1",
                            "value" : "尺码表自定义字段名称不能包含数字",
                            "valueIntervalInclude" : false
                        }, {
                            "name" : "1502077407_1",
                            "value" : "尺码表自定义字段名称不能包含逗号、冒号、分号、星号、短横线等特殊符号",
                            "valueIntervalInclude" : false
                        }],
                        "properties" : [],
                        "inputLevel" : 0,
                        "isDisplay" : 1
                    }, {
                        "id" : "size_mapping_-3_value",
                        "name" : "尺码表自定义字段-3值",
                        "type" : "INPUT",
                        "rules" : [{
                            "name" : "disableRule",
                            "value" : "true",
                            "dependGroup" : {
                                "dependExpressList" : [{
                                    "fieldId" : "std_size_prop_20509_-1",
                                    "value" : "均码",
                                    "symbol" : "=="
                                }],
                                "operator" : "or",
                                "dependGroupList" : [],
                                "empty" : false
                            },
                            "valueIntervalInclude" : false
                        }],
                        "properties" : [],
                        "inputLevel" : 0,
                        "isDisplay" : 1
                    }, {
                        "id" : "size_mapping_-3_value_range",
                        "name" : "尺码表自定义字段-3值",
                        "type" : "COMPLEX",
                        "rules" : [{
                            "name" : "disableRule",
                            "value" : "true",
                            "dependGroup" : {
                                "dependExpressList" : [{
                                    "fieldId" : "std_size_prop_20509_-1",
                                    "value" : "均码",
                                    "symbol" : "!="
                                }],
                                "operator" : "and",
                                "dependGroupList" : [],
                                "empty" : false
                            },
                            "valueIntervalInclude" : false
                        }],
                        "properties" : [],
                        "inputLevel" : 0,
                        "isDisplay" : 1,
                        "complexValue" : {
                            "fieldMap" : { }
                        },
                        "fields" : [{
                            "id" : "size_mapping_-3_value_from",
                            "name" : "最小值",
                            "type" : "INPUT",
                            "rules" : [],
                            "properties" : [],
                            "inputLevel" : 0,
                            "isDisplay" : 1
                        }, {
                            "id" : "size_mapping_-3_value_to",
                            "name" : "最大值",
                            "type" : "INPUT",
                            "rules" : [],
                            "properties" : [],
                            "inputLevel" : 0,
                            "isDisplay" : 1
                        }]
                    }]
                }, {
                    "id" : "size_mapping_-4",
                    "name" : "尺码表自定义字段-4",
                    "type" : "COMPLEX",
                    "rules" : [],
                    "properties" : [],
                    "inputLevel" : 0,
                    "isDisplay" : 1,
                    "complexValue" : {
                        "fieldMap" : { }
                    },
                    "fields" : [{
                        "id" : "size_mapping_-4_name",
                        "name" : "尺码表自定义字段-4名称",
                        "type" : "INPUT",
                        "rules" : [{
                            "name" : "146134104_1",
                            "value" : "尺码表自定义字段名称不能包含数字",
                            "valueIntervalInclude" : false
                        }, {
                            "name" : "1502077407_1",
                            "value" : "尺码表自定义字段名称不能包含逗号、冒号、分号、星号、短横线等特殊符号",
                            "valueIntervalInclude" : false
                        }],
                        "properties" : [],
                        "inputLevel" : 0,
                        "isDisplay" : 1
                    }, {
                        "id" : "size_mapping_-4_value",
                        "name" : "尺码表自定义字段-4值",
                        "type" : "INPUT",
                        "rules" : [{
                            "name" : "disableRule",
                            "value" : "true",
                            "dependGroup" : {
                                "dependExpressList" : [{
                                    "fieldId" : "std_size_prop_20509_-1",
                                    "value" : "均码",
                                    "symbol" : "=="
                                }],
                                "operator" : "or",
                                "dependGroupList" : [],
                                "empty" : false
                            },
                            "valueIntervalInclude" : false
                        }],
                        "properties" : [],
                        "inputLevel" : 0,
                        "isDisplay" : 1
                    }, {
                        "id" : "size_mapping_-4_value_range",
                        "name" : "尺码表自定义字段-4值",
                        "type" : "COMPLEX",
                        "rules" : [{
                            "name" : "disableRule",
                            "value" : "true",
                            "dependGroup" : {
                                "dependExpressList" : [{
                                    "fieldId" : "std_size_prop_20509_-1",
                                    "value" : "均码",
                                    "symbol" : "!="
                                }],
                                "operator" : "and",
                                "dependGroupList" : [],
                                "empty" : false
                            },
                            "valueIntervalInclude" : false
                        }],
                        "properties" : [],
                        "inputLevel" : 0,
                        "isDisplay" : 1,
                        "complexValue" : {
                            "fieldMap" : { }
                        },
                        "fields" : [{
                            "id" : "size_mapping_-4_value_from",
                            "name" : "最小值",
                            "type" : "INPUT",
                            "rules" : [],
                            "properties" : [],
                            "inputLevel" : 0,
                            "isDisplay" : 1
                        }, {
                            "id" : "size_mapping_-4_value_to",
                            "name" : "最大值",
                            "type" : "INPUT",
                            "rules" : [],
                            "properties" : [],
                            "inputLevel" : 0,
                            "isDisplay" : 1
                        }]
                    }]
                }, {
                    "id" : "size_mapping_-5",
                    "name" : "尺码表自定义字段-5",
                    "type" : "COMPLEX",
                    "rules" : [],
                    "properties" : [],
                    "inputLevel" : 0,
                    "isDisplay" : 1,
                    "complexValue" : {
                        "fieldMap" : { }
                    },
                    "fields" : [{
                        "id" : "size_mapping_-5_name",
                        "name" : "尺码表自定义字段-5名称",
                        "type" : "INPUT",
                        "rules" : [{
                            "name" : "146134104_1",
                            "value" : "尺码表自定义字段名称不能包含数字",
                            "valueIntervalInclude" : false
                        }, {
                            "name" : "1502077407_1",
                            "value" : "尺码表自定义字段名称不能包含逗号、冒号、分号、星号、短横线等特殊符号",
                            "valueIntervalInclude" : false
                        }],
                        "properties" : [],
                        "inputLevel" : 0,
                        "isDisplay" : 1
                    }, {
                        "id" : "size_mapping_-5_value",
                        "name" : "尺码表自定义字段-5值",
                        "type" : "INPUT",
                        "rules" : [{
                            "name" : "disableRule",
                            "value" : "true",
                            "dependGroup" : {
                                "dependExpressList" : [{
                                    "fieldId" : "std_size_prop_20509_-1",
                                    "value" : "均码",
                                    "symbol" : "=="
                                }],
                                "operator" : "or",
                                "dependGroupList" : [],
                                "empty" : false
                            },
                            "valueIntervalInclude" : false
                        }],
                        "properties" : [],
                        "inputLevel" : 0,
                        "isDisplay" : 1
                    }, {
                        "id" : "size_mapping_-5_value_range",
                        "name" : "尺码表自定义字段-5值",
                        "type" : "COMPLEX",
                        "rules" : [{
                            "name" : "disableRule",
                            "value" : "true",
                            "dependGroup" : {
                                "dependExpressList" : [{
                                    "fieldId" : "std_size_prop_20509_-1",
                                    "value" : "均码",
                                    "symbol" : "!="
                                }],
                                "operator" : "and",
                                "dependGroupList" : [],
                                "empty" : false
                            },
                            "valueIntervalInclude" : false
                        }],
                        "properties" : [],
                        "inputLevel" : 0,
                        "isDisplay" : 1,
                        "complexValue" : {
                            "fieldMap" : { }
                        },
                        "fields" : [{
                            "id" : "size_mapping_-5_value_from",
                            "name" : "最小值",
                            "type" : "INPUT",
                            "rules" : [],
                            "properties" : [],
                            "inputLevel" : 0,
                            "isDisplay" : 1
                        }, {
                            "id" : "size_mapping_-5_value_to",
                            "name" : "最大值",
                            "type" : "INPUT",
                            "rules" : [],
                            "properties" : [],
                            "inputLevel" : 0,
                            "isDisplay" : 1
                        }]
                    }]
                }]
            }, {
                "id" : "size_mapping_template_id",
                "name" : "尺码表填充模板ID",
                "type" : "INPUT",
                "rules" : [{
                    "name" : "valueTypeRule",
                    "value" : "long",
                    "valueIntervalInclude" : false
                }, {
                    "name" : "tipRule",
                    "value" : "尺码表填充模板ID和尺码属性值尺码表数据，二选一，尺码表填充模板ID对应的数据优先",
                    "valueIntervalInclude" : false
                }, {
                    "name" : "tipRule",
                    "value" : "尺码表填充模板只做为发布或编辑商品时填充尺码属性的尺码表的辅助数据，商品与该尺码表填充模板ID不进行强关联，更新尺码表填充模板数据不会影响商品上的尺码表数据",
                    "valueIntervalInclude" : false
                }],
                "properties" : [],
                "inputLevel" : 2,
                "isDisplay" : 1
            }, {
                "id" : "lang",
                "name" : "商品文字的字符集",
                "type" : "SINGLECHECK",
                "rules" : [],
                "properties" : [],
                "inputLevel" : 2,
                "isDisplay" : 1,
                "options" : [{
                    "displayName" : "zh_CN",
                    "value" : "zh_CN"
                }, {
                    "displayName" : "zh_HK",
                    "value" : "zh_HK"
                }],
                "value" : { }
            }, {
                "id" : "quantity",
                "name" : "商品数量",
                "type" : "INPUT",
                "rules" : [{
                    "name" : "valueTypeRule",
                    "value" : "integer",
                    "valueIntervalInclude" : false
                }, {
                    "name" : "requiredRule",
                    "value" : "true",
                    "valueIntervalInclude" : false
                }, {
                    "name" : "regexRule",
                    "value" : "^[0-9]*$",
                    "valueIntervalInclude" : false
                }, {
                    "name" : "minValueRule",
                    "value" : "0",
                    "exProperty" : "include",
                    "valueIntervalInclude" : true
                }, {
                    "name" : "maxValueRule",
                    "value" : "1000000",
                    "exProperty" : "not include",
                    "valueIntervalInclude" : false
                }, {
                    "name" : "tipRule",
                    "value" : "请认真填写。无货空挂，可能引起投诉与退款",
                    "url" : "https://bangpai.taobao.com/group/thread/145336-9509231.htm",
                    "valueIntervalInclude" : false
                }],
                "properties" : [],
                "inputLevel" : 2,
                "isDisplay" : 1
            }, {
                "id" : "price",
                "name" : "商品价格",
                "type" : "INPUT",
                "rules" : [{
                    "name" : "valueTypeRule",
                    "value" : "decimal",
                    "valueIntervalInclude" : false
                }, {
                    "name" : "requiredRule",
                    "value" : "true",
                    "valueIntervalInclude" : false
                }, {
                    "name" : "regexRule",
                    "value" : "^([0-9]+|[0-9]{1,3}(,[0-9]{3})*)(\\.[0-9]{0,2})?$",
                    "valueIntervalInclude" : false
                }, {
                    "name" : "tipRule",
                    "value" : "商品价格 应在 销售属性表中所填 最高与最低价格 范围区间内。",
                    "valueIntervalInclude" : false
                }, {
                    "name" : "minValueRule",
                    "value" : "1.00",
                    "exProperty" : "not include",
                    "valueIntervalInclude" : false
                }, {
                    "name" : "maxValueRule",
                    "value" : "100000000.00",
                    "exProperty" : "not include",
                    "valueIntervalInclude" : false
                }, {
                    "name" : "293701623_1",
                    "value" : "商品价格必须在销售属性表中所填最高与最低价格范围区间内",
                    "valueIntervalInclude" : false
                }, {
                    "name" : "tipRule",
                    "value" : "为避免价格变动引发的违规，请谨慎输入价格。",
                    "url" : "https://rule.tmall.com/tdetail-2695.htm",
                    "valueIntervalInclude" : false
                }],
                "properties" : [],
                "inputLevel" : 2,
                "isDisplay" : 1
            }, {
                "id" : "barcode",
                "name" : "条形码",
                "type" : "INPUT",
                "rules" : [{
                    "name" : "valueTypeRule",
                    "value" : "text",
                    "valueIntervalInclude" : false
                }, {
                    "name" : "tipRule",
                    "value" : "如果规格（如颜色，容量，尺码等）区域已填写条形码，则此处不用填写。",
                    "valueIntervalInclude" : false
                }, {
                    "name" : "tipRule",
                    "value" : "请严格按照外包装填写条形码信息。",
                    "valueIntervalInclude" : false
                }],
                "properties" : [],
                "inputLevel" : 2,
                "isDisplay" : 1
            }, {
                "id" : "has_discount",
                "name" : "是否支持会员折扣",
                "type" : "SINGLECHECK",
                "rules" : [],
                "properties" : [],
                "inputLevel" : 2,
                "isDisplay" : 1,
                "defaultValueField" : {
                    "type" : "SINGLECHECK",
                    "rules" : [],
                    "properties" : [],
                    "inputLevel" : 0,
                    "isDisplay" : 1,
                    "options" : [],
                    "value" : {
                        "value" : "false"
                    }
                },
                "options" : [{
                    "displayName" : "支持会员打折",
                    "value" : "true"
                }, {
                    "displayName" : "不支持会员打折",
                    "value" : "false"
                }],
                "value" : { }
            }, {
                "id" : "inner_shop",
                "name" : "页面模板-淘宝店铺",
                "type" : "SINGLECHECK",
                "rules" : [],
                "properties" : [],
                "inputLevel" : 2,
                "isDisplay" : 1,
                "options" : [{
                    "displayName" : "默认宝贝详情页",
                    "value" : "843770147"
                }, {
                    "displayName" : "Dr.martens详情页",
                    "value" : "900382318"
                }],
                "value" : { }
            }, {
                "id" : "outer_shop",
                "name" : "页面模板-官方网店",
                "type" : "SINGLECHECK",
                "rules" : [],
                "properties" : [],
                "inputLevel" : 2,
                "isDisplay" : 1,
                "options" : [{
                    "displayName" : "宝贝详情页",
                    "value" : "685770596"
                }],
                "value" : { }
            }, {
                "id" : "delivery_way",
                "name" : "提取方式",
                "type" : "MULTICHECK",
                "rules" : [{
                    "name" : "requiredRule",
                    "value" : "true",
                    "valueIntervalInclude" : false
                }],
                "properties" : [],
                "inputLevel" : 2,
                "isDisplay" : 1,
                "options" : [{
                    "displayName" : "邮寄",
                    "value" : "2"
                }],
                "values" : []
            }, {
                "id" : "locality_life->expirydate",
                "name" : "有效期",
                "type" : "SINGLECHECK",
                "rules" : [{
                    "name" : "disableRule",
                    "value" : "true",
                    "dependGroup" : {
                        "dependExpressList" : [{
                            "fieldId" : "delivery_way",
                            "value" : "1",
                            "symbol" : "not contains"
                        }],
                        "operator" : "and",
                        "dependGroupList" : [],
                        "empty" : false
                    },
                    "valueIntervalInclude" : false
                }],
                "properties" : [],
                "inputLevel" : 2,
                "isDisplay" : 1,
                "options" : [{
                    "displayName" : "按时间区间",
                    "value" : "1"
                }, {
                    "displayName" : "按购买日起时间区间",
                    "value" : "2"
                }, {
                    "displayName" : "按购买日起有效天数区间",
                    "value" : "3"
                }],
                "value" : { }
            }, {
                "id" : "locality_life->expirydate->startend",
                "name" : "有效期按时间区间",
                "type" : "COMPLEX",
                "rules" : [{
                    "name" : "disableRule",
                    "value" : "true",
                    "dependGroup" : {
                        "dependExpressList" : [{
                            "fieldId" : "locality_life.expirydate",
                            "value" : "1",
                            "symbol" : "!="
                        }],
                        "operator" : "and",
                        "dependGroupList" : [],
                        "empty" : false
                    },
                    "valueIntervalInclude" : false
                }],
                "properties" : [],
                "inputLevel" : 2,
                "isDisplay" : 1,
                "complexValue" : {
                    "fieldMap" : { }
                },
                "fields" : [{
                    "id" : "locality_life->expirydate->startend->start",
                    "name" : "开始时间",
                    "type" : "INPUT",
                    "rules" : [{
                        "name" : "valueTypeRule",
                        "value" : "date",
                        "valueIntervalInclude" : false
                    }],
                    "properties" : [],
                    "inputLevel" : 0,
                    "isDisplay" : 1
                }, {
                    "id" : "locality_life->expirydate->startend->end",
                    "name" : "结束时间",
                    "type" : "INPUT",
                    "rules" : [{
                        "name" : "valueTypeRule",
                        "value" : "date",
                        "valueIntervalInclude" : false
                    }],
                    "properties" : [],
                    "inputLevel" : 0,
                    "isDisplay" : 1
                }]
            }, {
                "id" : "locality_life->expirydate->end",
                "name" : "有效期按购买日起时间区间",
                "type" : "INPUT",
                "rules" : [{
                    "name" : "valueTypeRule",
                    "value" : "date",
                    "valueIntervalInclude" : false
                }, {
                    "name" : "disableRule",
                    "value" : "true",
                    "dependGroup" : {
                        "dependExpressList" : [{
                            "fieldId" : "locality_life.expirydate",
                            "value" : "2",
                            "symbol" : "!="
                        }],
                        "operator" : "and",
                        "dependGroupList" : [],
                        "empty" : false
                    },
                    "valueIntervalInclude" : false
                }],
                "properties" : [],
                "inputLevel" : 2,
                "isDisplay" : 1
            }, {
                "id" : "locality_life->expirydate->severalDays",
                "name" : "有效期按购买日起有效天数区间",
                "type" : "INPUT",
                "rules" : [{
                    "name" : "valueTypeRule",
                    "value" : "integer",
                    "valueIntervalInclude" : false
                }, {
                    "name" : "disableRule",
                    "value" : "true",
                    "dependGroup" : {
                        "dependExpressList" : [{
                            "fieldId" : "locality_life.expirydate",
                            "value" : "3",
                            "symbol" : "!="
                        }],
                        "operator" : "and",
                        "dependGroupList" : [],
                        "empty" : false
                    },
                    "valueIntervalInclude" : false
                }],
                "properties" : [],
                "inputLevel" : 2,
                "isDisplay" : 1
            }, {
                "id" : "locality_life->network_id",
                "name" : "网点id",
                "type" : "INPUT",
                "rules" : [{
                    "name" : "valueTypeRule",
                    "value" : "text",
                    "valueIntervalInclude" : false
                }, {
                    "name" : "disableRule",
                    "value" : "true",
                    "dependGroup" : {
                        "dependExpressList" : [{
                            "fieldId" : "delivery_way",
                            "value" : "1",
                            "symbol" : "not contains"
                        }],
                        "operator" : "and",
                        "dependGroupList" : [],
                        "empty" : false
                    },
                    "valueIntervalInclude" : false
                }],
                "properties" : [],
                "inputLevel" : 2,
                "isDisplay" : 1
            }, {
                "id" : "locality_life->merchant",
                "name" : "码商",
                "type" : "COMPLEX",
                "rules" : [{
                    "name" : "requiredRule",
                    "value" : "true",
                    "valueIntervalInclude" : false
                }, {
                    "name" : "disableRule",
                    "value" : "true",
                    "dependGroup" : {
                        "dependExpressList" : [{
                            "fieldId" : "delivery_way",
                            "value" : "1",
                            "symbol" : "not contains"
                        }],
                        "operator" : "and",
                        "dependGroupList" : [],
                        "empty" : false
                    },
                    "valueIntervalInclude" : false
                }],
                "properties" : [],
                "inputLevel" : 2,
                "isDisplay" : 1,
                "complexValue" : {
                    "fieldMap" : { }
                },
                "fields" : [{
                    "id" : "locality_life->merchant->id",
                    "name" : "码商ID",
                    "type" : "INPUT",
                    "rules" : [{
                        "name" : "valueTypeRule",
                        "value" : "long",
                        "valueIntervalInclude" : false
                    }],
                    "properties" : [],
                    "inputLevel" : 0,
                    "isDisplay" : 1
                }, {
                    "id" : "locality_life->merchant->name",
                    "name" : "码商名称",
                    "type" : "INPUT",
                    "rules" : [{
                        "name" : "valueTypeRule",
                        "value" : "text",
                        "valueIntervalInclude" : false
                    }],
                    "properties" : [],
                    "inputLevel" : 0,
                    "isDisplay" : 1
                }]
            }, {
                "id" : "expired_refund",
                "name" : "过期退款",
                "type" : "COMPLEX",
                "rules" : [{
                    "name" : "requiredRule",
                    "value" : "true",
                    "valueIntervalInclude" : false
                }, {
                    "name" : "disableRule",
                    "value" : "true",
                    "dependGroup" : {
                        "dependExpressList" : [{
                            "fieldId" : "delivery_way",
                            "value" : "1",
                            "symbol" : "not contains"
                        }],
                        "operator" : "and",
                        "dependGroupList" : [],
                        "empty" : false
                    },
                    "valueIntervalInclude" : false
                }],
                "properties" : [],
                "inputLevel" : 2,
                "isDisplay" : 1,
                "complexValue" : {
                    "fieldMap" : { }
                },
                "fields" : [{
                    "id" : "support_expired_refund_rate",
                    "name" : "支持过期退款",
                    "type" : "SINGLECHECK",
                    "rules" : [],
                    "properties" : [],
                    "inputLevel" : 0,
                    "isDisplay" : 1,
                    "options" : [{
                        "displayName" : "是",
                        "value" : "1"
                    }],
                    "value" : { }
                }, {
                    "id" : "expired_refund_rate",
                    "name" : "过期退款比例",
                    "type" : "INPUT",
                    "rules" : [{
                        "name" : "valueTypeRule",
                        "value" : "integer",
                        "valueIntervalInclude" : false
                    }, {
                        "name" : "minValueRule",
                        "value" : "1",
                        "exProperty" : "include",
                        "valueIntervalInclude" : true
                    }, {
                        "name" : "maxValueRule",
                        "value" : "100",
                        "exProperty" : "include",
                        "valueIntervalInclude" : true
                    }, {
                        "name" : "requiredRule",
                        "value" : "true",
                        "valueIntervalInclude" : false
                    }, {
                        "name" : "tipRule",
                        "value" : "退款比例请输入1-100之间的整数",
                        "valueIntervalInclude" : false
                    }, {
                        "name" : "disableRule",
                        "value" : "true",
                        "dependGroup" : {
                            "dependExpressList" : [{
                                "fieldId" : "support_expired_refund_rate",
                                "value" : "1",
                                "symbol" : "!="
                            }],
                            "operator" : "and",
                            "dependGroupList" : [],
                            "empty" : false
                        },
                        "valueIntervalInclude" : false
                    }],
                    "properties" : [],
                    "inputLevel" : 0,
                    "isDisplay" : 1
                }]
            }, {
                "id" : "locality_life->verification",
                "name" : "核销打款",
                "type" : "SINGLECHECK",
                "rules" : [{
                    "name" : "disableRule",
                    "value" : "true",
                    "dependGroup" : {
                        "dependExpressList" : [{
                            "fieldId" : "delivery_way",
                            "value" : "1",
                            "symbol" : "not contains"
                        }],
                        "operator" : "and",
                        "dependGroupList" : [],
                        "empty" : false
                    },
                    "valueIntervalInclude" : false
                }],
                "properties" : [],
                "inputLevel" : 2,
                "isDisplay" : 1,
                "options" : [{
                    "displayName" : "是",
                    "value" : "1"
                }],
                "value" : { }
            }, {
                "id" : "auto_refund",
                "name" : "自动退款",
                "type" : "COMPLEX",
                "rules" : [{
                    "name" : "disableRule",
                    "value" : "true",
                    "dependGroup" : {
                        "dependExpressList" : [{
                            "fieldId" : "delivery_way",
                            "value" : "1",
                            "symbol" : "not contains"
                        }],
                        "operator" : "and",
                        "dependGroupList" : [],
                        "empty" : false
                    },
                    "valueIntervalInclude" : false
                }],
                "properties" : [],
                "inputLevel" : 2,
                "isDisplay" : 1,
                "complexValue" : {
                    "fieldMap" : { }
                },
                "fields" : [{
                    "id" : "support_auto_refund_rate",
                    "name" : "支持自动退款",
                    "type" : "SINGLECHECK",
                    "rules" : [],
                    "properties" : [],
                    "inputLevel" : 0,
                    "isDisplay" : 1,
                    "options" : [{
                        "displayName" : "是",
                        "value" : "1"
                    }],
                    "value" : { }
                }, {
                    "id" : "auto_refund_rate",
                    "name" : "过期自动比例",
                    "type" : "INPUT",
                    "rules" : [{
                        "name" : "valueTypeRule",
                        "value" : "integer",
                        "valueIntervalInclude" : false
                    }, {
                        "name" : "minValueRule",
                        "value" : "1",
                        "exProperty" : "include",
                        "valueIntervalInclude" : true
                    }, {
                        "name" : "maxValueRule",
                        "value" : "100",
                        "exProperty" : "include",
                        "valueIntervalInclude" : true
                    }, {
                        "name" : "tipRule",
                        "value" : "售中自动退款比例请输入1-100之间的整数",
                        "valueIntervalInclude" : false
                    }, {
                        "name" : "disableRule",
                        "value" : "true",
                        "dependGroup" : {
                            "dependExpressList" : [{
                                "fieldId" : "support_auto_refund_rate",
                                "value" : "1",
                                "symbol" : "!="
                            }],
                            "operator" : "and",
                            "dependGroupList" : [],
                            "empty" : false
                        },
                        "valueIntervalInclude" : false
                    }],
                    "properties" : [],
                    "inputLevel" : 0,
                    "isDisplay" : 1
                }]
            }, {
                "id" : "location",
                "name" : "所在地",
                "type" : "COMPLEX",
                "rules" : [],
                "properties" : [],
                "inputLevel" : 2,
                "isDisplay" : 1,
                "complexValue" : {
                    "fieldMap" : { }
                },
                "fields" : [{
                    "id" : "prov",
                    "name" : "省份",
                    "type" : "INPUT",
                    "rules" : [{
                        "name" : "valueTypeRule",
                        "value" : "text",
                        "valueIntervalInclude" : false
                    }, {
                        "name" : "requiredRule",
                        "value" : "true",
                        "valueIntervalInclude" : false
                    }],
                    "properties" : [],
                    "inputLevel" : 0,
                    "isDisplay" : 1
                }, {
                    "id" : "city",
                    "name" : "城市",
                    "type" : "INPUT",
                    "rules" : [{
                        "name" : "valueTypeRule",
                        "value" : "text",
                        "valueIntervalInclude" : false
                    }, {
                        "name" : "requiredRule",
                        "value" : "true",
                        "valueIntervalInclude" : false
                    }],
                    "properties" : [],
                    "inputLevel" : 0,
                    "isDisplay" : 1
                }]
            }, {
                "id" : "freight_payer",
                "name" : "运费承担方式",
                "type" : "SINGLECHECK",
                "rules" : [{
                    "name" : "disableRule",
                    "value" : "true",
                    "dependGroup" : {
                        "dependExpressList" : [{
                            "fieldId" : "delivery_way",
                            "value" : "2",
                            "symbol" : "not contains"
                        }],
                        "operator" : "and",
                        "dependGroupList" : [],
                        "empty" : false
                    },
                    "valueIntervalInclude" : false
                }],
                "properties" : [],
                "inputLevel" : 2,
                "isDisplay" : 1,
                "defaultValueField" : {
                    "type" : "SINGLECHECK",
                    "rules" : [],
                    "properties" : [],
                    "inputLevel" : 0,
                    "isDisplay" : 1,
                    "options" : [],
                    "value" : {
                        "value" : "1"
                    }
                },
                "options" : [{
                    "displayName" : "卖家承担运费",
                    "value" : "2"
                }, {
                    "displayName" : "买家承担运费",
                    "value" : "1"
                }],
                "value" : { }
            }, {
                "id" : "freight_by_buyer",
                "name" : "买家承担运费",
                "type" : "SINGLECHECK",
                "rules" : [{
                    "name" : "disableRule",
                    "value" : "true",
                    "dependGroup" : {
                        "dependExpressList" : [{
                            "fieldId" : "freight_payer",
                            "value" : "1",
                            "symbol" : "!="
                        }],
                        "operator" : "and",
                        "dependGroupList" : [],
                        "empty" : false
                    },
                    "valueIntervalInclude" : false
                }],
                "properties" : [],
                "inputLevel" : 2,
                "isDisplay" : 1,
                "options" : [{
                    "displayName" : "使用运费模板",
                    "value" : "postage"
                }],
                "value" : { }
            }, {
                "id" : "postage_id",
                "name" : "运费模板ID",
                "type" : "INPUT",
                "rules" : [{
                    "name" : "valueTypeRule",
                    "value" : "long",
                    "valueIntervalInclude" : false
                }, {
                    "name" : "disableRule",
                    "value" : "true",
                    "dependGroup" : {
                        "dependExpressList" : [{
                            "fieldId" : "freight_by_buyer",
                            "value" : "postage",
                            "symbol" : "!="
                        }],
                        "operator" : "and",
                        "dependGroupList" : [],
                        "empty" : false
                    },
                    "valueIntervalInclude" : false
                }],
                "properties" : [],
                "inputLevel" : 2,
                "isDisplay" : 1
            }, {
                "id" : "seller_cids",
                "name" : "商品所属的店铺类目列表",
                "type" : "MULTICHECK",
                "rules" : [],
                "properties" : [],
                "inputLevel" : 2,
                "isDisplay" : 1,
                "options" : [{
                    "displayName" : "New Arrival >> 4月新品 new",
                    "value" : "1039474062"
                }, {
                    "displayName" : "New Arrival >> 3月新品 new",
                    "value" : "1020298978"
                }, {
                    "displayName" : "New Arrival >> 2月新品 new",
                    "value" : "1003666171"
                }, {
                    "displayName" : "New Arrival >> 1月新品 new",
                    "value" : "961820946"
                }, {
                    "displayName" : "New Arrival >> 12月新品 new",
                    "value" : "961820945"
                }, {
                    "displayName" : "New Arrival >> 11月新品 new",
                    "value" : "961820944"
                }, {
                    "displayName" : "男装Mens >> American Apperal",
                    "value" : "979143762"
                }, {
                    "displayName" : "男装Mens >> Asphalt",
                    "value" : "971477213"
                }, {
                    "displayName" : "男装Mens >> Boy London",
                    "value" : "971477215"
                }, {
                    "displayName" : "男装Mens >> Canada Goose",
                    "value" : "971477216"
                }, {
                    "displayName" : "男装Mens >> DL1961",
                    "value" : "971477217"
                }, {
                    "displayName" : "男装Mens >> Levi's李维斯",
                    "value" : "971477223"
                }, {
                    "displayName" : "男装Mens >> Prps Goods &amp; Co",
                    "value" : "971477224"
                }, {
                    "displayName" : "男装Mens >> Raven Denim",
                    "value" : "971477225"
                }, {
                    "displayName" : "男装Mens >> Superdry极度干燥",
                    "value" : "971477228"
                }, {
                    "displayName" : "男装Mens >> Sneakerhead",
                    "value" : "971464084"
                }, {
                    "displayName" : "男装Mens >> Timberland添柏岚",
                    "value" : "1015506739"
                }, {
                    "displayName" : "男装Mens >> True Religion",
                    "value" : "971476831"
                }, {
                    "displayName" : "女装Womens >> 7 For All Mankind",
                    "value" : "1022280805"
                }, {
                    "displayName" : "女装Womens >> American Apperal",
                    "value" : "1022280806"
                }, {
                    "displayName" : "女装Womens >> Asphalt",
                    "value" : "1022280807"
                }, {
                    "displayName" : "女装Womens >> Boy London",
                    "value" : "1022280809"
                }, {
                    "displayName" : "女装Womens >> Canada Goose",
                    "value" : "1022280810"
                }, {
                    "displayName" : "女装Womens >> DL1961",
                    "value" : "1037851337"
                }, {
                    "displayName" : "女装Womens >> Free People",
                    "value" : "1022280814"
                }, {
                    "displayName" : "女装Womens >> Iijin艾今",
                    "value" : "1029854150"
                }, {
                    "displayName" : "女装Womens >> Lauren Moshi",
                    "value" : "1022280816"
                }, {
                    "displayName" : "女装Womens >> Levi's李维斯",
                    "value" : "1022280817"
                }, {
                    "displayName" : "女装Womens >> Raven Denim",
                    "value" : "1022280819"
                }, {
                    "displayName" : "女装Womens >> Siwy",
                    "value" : "1022280821"
                }, {
                    "displayName" : "女装Womens >> Superdry极度干燥",
                    "value" : "1022280822"
                }, {
                    "displayName" : "女装Womens >> Sneakerhead",
                    "value" : "1022280823"
                }, {
                    "displayName" : "女装Womens >> Tokidoki",
                    "value" : "1022280825"
                }, {
                    "displayName" : "女装Womens >> True Religion",
                    "value" : "1022280826"
                }, {
                    "displayName" : "女装Womens >> Wildfox",
                    "value" : "1022280827"
                }, {
                    "displayName" : "时尚包包 >> ASH / 艾熙",
                    "value" : "980835346"
                }, {
                    "displayName" : "时尚包包 >> Michael Kors",
                    "value" : "971460884"
                }, {
                    "displayName" : "时尚包包 >> Rebecca Minkoff",
                    "value" : "971460885"
                }, {
                    "displayName" : "时尚包包 >> Herschel Supply",
                    "value" : "971460886"
                }, {
                    "displayName" : "时尚包包 >> Tumi / 途明",
                    "value" : "971476829"
                }, {
                    "displayName" : "英伦鞋履 >> Clarks 其乐",
                    "value" : "971464086"
                }, {
                    "displayName" : "英伦鞋履 >> Dr.Martens 马丁靴",
                    "value" : "979143663"
                }, {
                    "displayName" : "英伦鞋履 >> Levis/李维斯",
                    "value" : "1002632578"
                }, {
                    "displayName" : "英伦鞋履 >> Polo Ralph Lauren",
                    "value" : "1002633657"
                }, {
                    "displayName" : "英伦鞋履 >> Sebago仕品高",
                    "value" : "971464088"
                }, {
                    "displayName" : "英伦鞋履 >> SWIMS",
                    "value" : "971464090"
                }, {
                    "displayName" : "英伦鞋履 >> Thorocraft",
                    "value" : "971464089"
                }, {
                    "displayName" : "英伦鞋履 >> TIMBERLAND添柏岚",
                    "value" : "971477229"
                }, {
                    "displayName" : "英伦鞋履 >> Wolverine 渥弗林",
                    "value" : "978583045"
                }, {
                    "displayName" : "精美配饰 >> Prada/普拉达",
                    "value" : "1020499140"
                }],
                "values" : []
            }, {
                "id" : "vertical_image",
                "name" : "商品竖图",
                "type" : "INPUT",
                "rules" : [{
                    "name" : "valueTypeRule",
                    "value" : "url",
                    "valueIntervalInclude" : false
                }, {
                    "name" : "requiredRule",
                    "value" : "true",
                    "valueIntervalInclude" : false
                }],
                "properties" : [],
                "inputLevel" : 2,
                "isDisplay" : 1
            }, {
                "id" : "after_sale_id",
                "name" : "售后说明模板ID",
                "type" : "INPUT",
                "rules" : [{
                    "name" : "valueTypeRule",
                    "value" : "long",
                    "valueIntervalInclude" : false
                }, {
                    "name" : "devTipRule",
                    "value" : "请使用taobao.aftersale.get接口获取售后说明模板信息",
                    "url" : "http://api.taobao.com/apidoc/api.htm?path=cid:4-apiId:10448",
                    "valueIntervalInclude" : false
                }],
                "properties" : [],
                "inputLevel" : 2,
                "isDisplay" : 1
            }, {
                "id" : "has_warranty",
                "name" : "保修",
                "type" : "SINGLECHECK",
                "rules" : [{
                    "name" : "requiredRule",
                    "value" : "true",
                    "valueIntervalInclude" : false
                }],
                "properties" : [],
                "inputLevel" : 2,
                "isDisplay" : 1,
                "defaultValueField" : {
                    "type" : "SINGLECHECK",
                    "rules" : [],
                    "properties" : [],
                    "inputLevel" : 0,
                    "isDisplay" : 1,
                    "options" : [],
                    "value" : {
                        "value" : "false"
                    }
                },
                "options" : [{
                    "displayName" : "有",
                    "value" : "true"
                }, {
                    "displayName" : "无",
                    "value" : "false"
                }],
                "value" : { }
            }, {
                "id" : "sell_promise",
                "name" : "退换货服务",
                "type" : "SINGLECHECK",
                "rules" : [],
                "properties" : [],
                "inputLevel" : 2,
                "isDisplay" : 1,
                "defaultValueField" : {
                    "type" : "SINGLECHECK",
                    "rules" : [],
                    "properties" : [],
                    "inputLevel" : 0,
                    "isDisplay" : 1,
                    "options" : [],
                    "value" : {
                        "value" : "true"
                    }
                },
                "options" : [{
                    "displayName" : "是",
                    "value" : "true"
                }, {
                    "displayName" : "否",
                    "value" : "false"
                }],
                "value" : { }
            }, {
                "id" : "has_showcase",
                "name" : "橱窗推荐",
                "type" : "SINGLECHECK",
                "rules" : [],
                "properties" : [],
                "inputLevel" : 2,
                "isDisplay" : 1,
                "defaultValueField" : {
                    "type" : "SINGLECHECK",
                    "rules" : [],
                    "properties" : [],
                    "inputLevel" : 0,
                    "isDisplay" : 1,
                    "options" : [],
                    "value" : {
                        "value" : "false"
                    }
                },
                "options" : [{
                    "displayName" : "橱窗推荐",
                    "value" : "true"
                }, {
                    "displayName" : "橱窗不推荐",
                    "value" : "false"
                }],
                "value" : { }
            }, {
                "id" : "outer_id",
                "name" : "商家外部编码",
                "type" : "INPUT",
                "rules" : [{
                    "name" : "valueTypeRule",
                    "value" : "text",
                    "valueIntervalInclude" : false
                }, {
                    "name" : "maxLengthRule",
                    "value" : "64",
                    "exProperty" : "include",
                    "unit" : "byte",
                    "valueIntervalInclude" : true
                }],
                "properties" : [],
                "inputLevel" : 2,
                "isDisplay" : 1
            }, {
                "id" : "is_xinpin",
                "name" : "是否新品",
                "type" : "SINGLECHECK",
                "rules" : [{
                    "name" : "tipRule",
                    "value" : "新品查询工具：//sell.tmall.com/auction/tools/xinpin.htm",
                    "valueIntervalInclude" : false
                }],
                "properties" : [],
                "inputLevel" : 2,
                "isDisplay" : 1,
                "defaultValueField" : {
                    "type" : "SINGLECHECK",
                    "rules" : [],
                    "properties" : [],
                    "inputLevel" : 0,
                    "isDisplay" : 1,
                    "options" : [],
                    "value" : {
                        "value" : "false"
                    }
                },
                "options" : [{
                    "displayName" : "是",
                    "value" : "true"
                }, {
                    "displayName" : "否",
                    "value" : "false"
                }],
                "value" : { }
            }, {
                "id" : "description",
                "name" : "商品描述",
                "type" : "INPUT",
                "rules" : [{
                    "name" : "valueTypeRule",
                    "value" : "html",
                    "valueIntervalInclude" : false
                }, {
                    "name" : "minLengthRule",
                    "value" : "5",
                    "exProperty" : "include",
                    "unit" : "character",
                    "valueIntervalInclude" : true
                }, {
                    "name" : "maxLengthRule",
                    "value" : "25000",
                    "exProperty" : "include",
                    "unit" : "character",
                    "valueIntervalInclude" : true
                }, {
                    "name" : "requiredRule",
                    "value" : "true",
                    "valueIntervalInclude" : false
                }],
                "properties" : [],
                "inputLevel" : 2,
                "isDisplay" : 1
            }, {
                "id" : "wap_desc",
                "name" : "无线商品描述",
                "type" : "COMPLEX",
                "rules" : [{
                    "name" : "maxLengthRule",
                    "value" : "5000",
                    "exProperty" : "include",
                    "unit" : "character",
                    "valueIntervalInclude" : true
                }],
                "properties" : [],
                "inputLevel" : 2,
                "isDisplay" : 1,
                "complexValue" : {
                    "fieldMap" : { }
                },
                "fields" : [{
                    "id" : "wap_desc_summary",
                    "name" : "无线商品描述摘要",
                    "type" : "INPUT",
                    "rules" : [{
                        "name" : "valueTypeRule",
                        "value" : "textarea",
                        "valueIntervalInclude" : false
                    }, {
                        "name" : "maxLengthRule",
                        "value" : "140",
                        "exProperty" : "include",
                        "unit" : "character",
                        "valueIntervalInclude" : true
                    }],
                    "properties" : [],
                    "inputLevel" : 0,
                    "isDisplay" : 1
                }, {
                    "id" : "wap_desc_audio",
                    "name" : "无线商品音频描述",
                    "type" : "COMPLEX",
                    "rules" : [],
                    "properties" : [],
                    "inputLevel" : 0,
                    "isDisplay" : 1,
                    "complexValue" : {
                        "fieldMap" : { }
                    },
                    "fields" : [{
                        "id" : "wap_desc_audio_title",
                        "name" : "无线商品描述音频标题",
                        "type" : "INPUT",
                        "rules" : [{
                            "name" : "valueTypeRule",
                            "value" : "text",
                            "valueIntervalInclude" : false
                        }],
                        "properties" : [],
                        "inputLevel" : 0,
                        "isDisplay" : 1
                    }, {
                        "id" : "wap_desc_audio_url",
                        "name" : "无线商品描述音频文件地址",
                        "type" : "INPUT",
                        "rules" : [{
                            "name" : "valueTypeRule",
                            "value" : "url",
                            "valueIntervalInclude" : false
                        }, {
                            "name" : "regexRule",
                            "value" : "^.*\\.mp3$",
                            "valueIntervalInclude" : false
                        }, {
                            "name" : "maxInputNumRule",
                            "value" : "1",
                            "exProperty" : "include",
                            "valueIntervalInclude" : true
                        }, {
                            "name" : "maxTargetSizeRule",
                            "value" : "204800",
                            "exProperty" : "include",
                            "unit" : "byte",
                            "valueIntervalInclude" : true
                        }],
                        "properties" : [],
                        "inputLevel" : 0,
                        "isDisplay" : 1
                    }]
                }, {
                    "id" : "wap_desc_content",
                    "name" : "无线商品描述内容（文本或图片）",
                    "type" : "MULTICOMPLEX",
                    "rules" : [],
                    "properties" : [],
                    "inputLevel" : 0,
                    "isDisplay" : 1,
                    "values" : [],
                    "fields" : [{
                        "id" : "wap_desc_content_type",
                        "name" : "无线商品描述类型",
                        "type" : "SINGLECHECK",
                        "rules" : [],
                        "properties" : [],
                        "inputLevel" : 0,
                        "isDisplay" : 1,
                        "options" : [{
                            "displayName" : "图片",
                            "value" : "image"
                        }, {
                            "displayName" : "文本",
                            "value" : "text"
                        }],
                        "value" : { }
                    }, {
                        "id" : "wap_desc_content_content",
                        "name" : "无线商品描述内容",
                        "type" : "INPUT",
                        "rules" : [{
                            "name" : "valueTypeRule",
                            "value" : "textarea",
                            "dependGroup" : {
                                "dependExpressList" : [{
                                    "fieldId" : "wap_desc_content_type",
                                    "value" : "text",
                                    "symbol" : "=="
                                }],
                                "operator" : "and",
                                "dependGroupList" : [],
                                "empty" : false
                            },
                            "valueIntervalInclude" : false
                        }, {
                            "name" : "valueTypeRule",
                            "value" : "url",
                            "dependGroup" : {
                                "dependExpressList" : [{
                                    "fieldId" : "wap_desc_content_type",
                                    "value" : "image",
                                    "symbol" : "=="
                                }],
                                "operator" : "and",
                                "dependGroupList" : [],
                                "empty" : false
                            },
                            "valueIntervalInclude" : false
                        }, {
                            "name" : "maxLengthRule",
                            "value" : "500",
                            "exProperty" : "include",
                            "dependGroup" : {
                                "dependExpressList" : [{
                                    "fieldId" : "wap_desc_content_type",
                                    "value" : "text",
                                    "symbol" : "=="
                                }],
                                "operator" : "and",
                                "dependGroupList" : [],
                                "empty" : false
                            },
                            "unit" : "character",
                            "valueIntervalInclude" : true
                        }, {
                            "name" : "minImageSizeRule",
                            "value" : "0x480",
                            "exProperty" : "include",
                            "dependGroup" : {
                                "dependExpressList" : [{
                                    "fieldId" : "wap_desc_content_type",
                                    "value" : "image",
                                    "symbol" : "=="
                                }],
                                "operator" : "and",
                                "dependGroupList" : [],
                                "empty" : false
                            },
                            "valueIntervalInclude" : true
                        }, {
                            "name" : "maxImageSizeRule",
                            "value" : "960x620",
                            "exProperty" : "include",
                            "dependGroup" : {
                                "dependExpressList" : [{
                                    "fieldId" : "wap_desc_content_type",
                                    "value" : "image",
                                    "symbol" : "=="
                                }],
                                "operator" : "and",
                                "dependGroupList" : [],
                                "empty" : false
                            },
                            "valueIntervalInclude" : true
                        }],
                        "properties" : [],
                        "inputLevel" : 0,
                        "isDisplay" : 1
                    }]
                }]
            }, {
                "id" : "item_size_weight",
                "name" : "商品物流重量体积",
                "type" : "COMPLEX",
                "rules" : [{
                    "name" : "requiredRule",
                    "value" : "true",
                    "valueIntervalInclude" : false
                }],
                "properties" : [],
                "inputLevel" : 2,
                "isDisplay" : 1,
                "complexValue" : {
                    "fieldMap" : { }
                },
                "fields" : [{
                    "id" : "item_size",
                    "name" : "商品物流体积(立方米)",
                    "type" : "INPUT",
                    "rules" : [{
                        "name" : "valueTypeRule",
                        "value" : "text",
                        "valueIntervalInclude" : false
                    }],
                    "properties" : [],
                    "inputLevel" : 0,
                    "isDisplay" : 1
                }, {
                    "id" : "item_weight",
                    "name" : "商品物流重量(千克)",
                    "type" : "INPUT",
                    "rules" : [{
                        "name" : "valueTypeRule",
                        "value" : "text",
                        "valueIntervalInclude" : false
                    }, {
                        "name" : "regexRule",
                        "value" : "^\\d+(\\.\\d+)?$",
                        "valueIntervalInclude" : false
                    }],
                    "properties" : [],
                    "inputLevel" : 0,
                    "isDisplay" : 1
                }]
            }, {
                "id" : "valid_thru",
                "name" : "有效期",
                "type" : "SINGLECHECK",
                "rules" : [{
                    "name" : "requiredRule",
                    "value" : "true",
                    "valueIntervalInclude" : false
                }],
                "properties" : [],
                "inputLevel" : 2,
                "isDisplay" : 1,
                "options" : [{
                    "displayName" : "7天",
                    "value" : "7"
                }, {
                    "displayName" : "14天",
                    "value" : "14"
                }],
                "value" : { }
            }, {
                "id" : "is_taobao",
                "name" : "是否在淘宝和天猫显示",
                "type" : "SINGLECHECK",
                "rules" : [{
                    "name" : "tipRule",
                    "value" : "商品如果设置为在淘宝和天猫不显示，在淘宝、天猫的主站和后台均不显示该商品。（不设置默认为显示）",
                    "valueIntervalInclude" : false
                }],
                "properties" : [],
                "inputLevel" : 2,
                "isDisplay" : 1,
                "options" : [{
                    "displayName" : "是",
                    "value" : "true"
                }, {
                    "displayName" : "否",
                    "value" : "false"
                }],
                "value" : { }
            }, {
                "id" : "is_ex",
                "name" : "是否在外店显示",
                "type" : "SINGLECHECK",
                "rules" : [],
                "properties" : [],
                "inputLevel" : 2,
                "isDisplay" : 1,
                "options" : [{
                    "displayName" : "是",
                    "value" : "true"
                }, {
                    "displayName" : "否",
                    "value" : "false"
                }],
                "value" : { }
            }, {
                "id" : "is_3D",
                "name" : "是否是3D",
                "type" : "SINGLECHECK",
                "rules" : [],
                "properties" : [],
                "inputLevel" : 2,
                "isDisplay" : 1,
                "options" : [{
                    "displayName" : "是",
                    "value" : "true"
                }, {
                    "displayName" : "否",
                    "value" : "false"
                }],
                "value" : { }
            }, {
                "id" : "viewschema_separateDelivery",
                "name" : "单独发货",
                "type" : "MULTICHECK",
                "rules" : [],
                "properties" : [],
                "inputLevel" : 2,
                "isDisplay" : 1,
                "options" : [{
                    "displayName" : "不能和其它商品一起发货，加入购物车下单后将单独拆单",
                    "value" : "83202"
                }],
                "values" : []
            }, {
                "id" : "size_measure_image",
                "name" : "尺码测量示意图",
                "type" : "SINGLECHECK",
                "rules" : [{
                    "name" : "requiredRule",
                    "value" : "true",
                    "valueIntervalInclude" : false
                }],
                "properties" : [],
                "inputLevel" : 2,
                "isDisplay" : 1,
                "options" : [{
                    "displayName" : "背心小吊带",
                    "value" : "https://img.alicdn.com/bao/uploaded/i4/TB1NuqWHXXXXXbXXXXXXXXXXXXX_!!0-item_pic.jpg"
                }],
                "value" : { }
            }, {
                "id" : "size_model_try",
                "name" : "尺码模特试穿表",
                "type" : "MULTICOMPLEX",
                "rules" : [],
                "properties" : [],
                "inputLevel" : 2,
                "isDisplay" : 1,
                "defaultValueField" : {
                    "type" : "MULTICOMPLEX",
                    "rules" : [],
                    "properties" : [],
                    "inputLevel" : 0,
                    "isDisplay" : 1,
                    "values" : [{
                        "fieldMap" : {
                            "sizeModelTry_tunwei" : {
                                "id" : "sizeModelTry_tunwei",
                                "type" : "INPUT",
                                "rules" : [],
                                "properties" : [],
                                "inputLevel" : 0,
                                "isDisplay" : 1
                            },
                            "sizeModelTry_size" : {
                                "id" : "sizeModelTry_size",
                                "type" : "INPUT",
                                "rules" : [],
                                "properties" : [],
                                "inputLevel" : 0,
                                "isDisplay" : 1
                            },
                            "sizeModelTry_shengao" : {
                                "id" : "sizeModelTry_shengao",
                                "type" : "INPUT",
                                "rules" : [],
                                "properties" : [],
                                "inputLevel" : 0,
                                "isDisplay" : 1
                            },
                            "sizeModelTry_modelName" : {
                                "id" : "sizeModelTry_modelName",
                                "type" : "INPUT",
                                "rules" : [],
                                "properties" : [],
                                "inputLevel" : 0,
                                "isDisplay" : 1
                            },
                            "sizeModelTry_yaowei" : {
                                "id" : "sizeModelTry_yaowei",
                                "type" : "INPUT",
                                "rules" : [],
                                "properties" : [],
                                "inputLevel" : 0,
                                "isDisplay" : 1
                            },
                            "sizeModelTry_xiongwei" : {
                                "id" : "sizeModelTry_xiongwei",
                                "type" : "INPUT",
                                "rules" : [],
                                "properties" : [],
                                "inputLevel" : 0,
                                "isDisplay" : 1
                            },
                            "sizeModelTry_result" : {
                                "id" : "sizeModelTry_result",
                                "type" : "INPUT",
                                "rules" : [],
                                "properties" : [],
                                "inputLevel" : 0,
                                "isDisplay" : 1
                            },
                            "sizeModelTry_tizhong" : {
                                "id" : "sizeModelTry_tizhong",
                                "type" : "INPUT",
                                "rules" : [],
                                "properties" : [],
                                "inputLevel" : 0,
                                "isDisplay" : 1
                            }
                        }
                    }, {
                        "fieldMap" : {
                            "sizeModelTry_tunwei" : {
                                "id" : "sizeModelTry_tunwei",
                                "type" : "INPUT",
                                "rules" : [],
                                "properties" : [],
                                "inputLevel" : 0,
                                "isDisplay" : 1
                            },
                            "sizeModelTry_size" : {
                                "id" : "sizeModelTry_size",
                                "type" : "INPUT",
                                "rules" : [],
                                "properties" : [],
                                "inputLevel" : 0,
                                "isDisplay" : 1
                            },
                            "sizeModelTry_shengao" : {
                                "id" : "sizeModelTry_shengao",
                                "type" : "INPUT",
                                "rules" : [],
                                "properties" : [],
                                "inputLevel" : 0,
                                "isDisplay" : 1
                            },
                            "sizeModelTry_modelName" : {
                                "id" : "sizeModelTry_modelName",
                                "type" : "INPUT",
                                "rules" : [],
                                "properties" : [],
                                "inputLevel" : 0,
                                "isDisplay" : 1
                            },
                            "sizeModelTry_yaowei" : {
                                "id" : "sizeModelTry_yaowei",
                                "type" : "INPUT",
                                "rules" : [],
                                "properties" : [],
                                "inputLevel" : 0,
                                "isDisplay" : 1
                            },
                            "sizeModelTry_xiongwei" : {
                                "id" : "sizeModelTry_xiongwei",
                                "type" : "INPUT",
                                "rules" : [],
                                "properties" : [],
                                "inputLevel" : 0,
                                "isDisplay" : 1
                            },
                            "sizeModelTry_result" : {
                                "id" : "sizeModelTry_result",
                                "type" : "INPUT",
                                "rules" : [],
                                "properties" : [],
                                "inputLevel" : 0,
                                "isDisplay" : 1
                            },
                            "sizeModelTry_tizhong" : {
                                "id" : "sizeModelTry_tizhong",
                                "type" : "INPUT",
                                "rules" : [],
                                "properties" : [],
                                "inputLevel" : 0,
                                "isDisplay" : 1
                            }
                        }
                    }, {
                        "fieldMap" : {
                            "sizeModelTry_tunwei" : {
                                "id" : "sizeModelTry_tunwei",
                                "type" : "INPUT",
                                "rules" : [],
                                "properties" : [],
                                "inputLevel" : 0,
                                "isDisplay" : 1
                            },
                            "sizeModelTry_size" : {
                                "id" : "sizeModelTry_size",
                                "type" : "INPUT",
                                "rules" : [],
                                "properties" : [],
                                "inputLevel" : 0,
                                "isDisplay" : 1
                            },
                            "sizeModelTry_shengao" : {
                                "id" : "sizeModelTry_shengao",
                                "type" : "INPUT",
                                "rules" : [],
                                "properties" : [],
                                "inputLevel" : 0,
                                "isDisplay" : 1
                            },
                            "sizeModelTry_modelName" : {
                                "id" : "sizeModelTry_modelName",
                                "type" : "INPUT",
                                "rules" : [],
                                "properties" : [],
                                "inputLevel" : 0,
                                "isDisplay" : 1
                            },
                            "sizeModelTry_yaowei" : {
                                "id" : "sizeModelTry_yaowei",
                                "type" : "INPUT",
                                "rules" : [],
                                "properties" : [],
                                "inputLevel" : 0,
                                "isDisplay" : 1
                            },
                            "sizeModelTry_xiongwei" : {
                                "id" : "sizeModelTry_xiongwei",
                                "type" : "INPUT",
                                "rules" : [],
                                "properties" : [],
                                "inputLevel" : 0,
                                "isDisplay" : 1
                            },
                            "sizeModelTry_result" : {
                                "id" : "sizeModelTry_result",
                                "type" : "INPUT",
                                "rules" : [],
                                "properties" : [],
                                "inputLevel" : 0,
                                "isDisplay" : 1
                            },
                            "sizeModelTry_tizhong" : {
                                "id" : "sizeModelTry_tizhong",
                                "type" : "INPUT",
                                "rules" : [],
                                "properties" : [],
                                "inputLevel" : 0,
                                "isDisplay" : 1
                            }
                        }
                    }],
                    "fields" : []
                },
                "values" : [],
                "fields" : [{
                    "id" : "sizeModelTry_size",
                    "name" : "试穿尺码",
                    "type" : "INPUT",
                    "rules" : [{
                        "name" : "requiredRule",
                        "value" : "true",
                        "valueIntervalInclude" : false
                    }],
                    "properties" : [],
                    "inputLevel" : 0,
                    "isDisplay" : 1
                }, {
                    "id" : "sizeModelTry_modelName",
                    "name" : "模特名称",
                    "type" : "INPUT",
                    "rules" : [{
                        "name" : "requiredRule",
                        "value" : "true",
                        "valueIntervalInclude" : false
                    }],
                    "properties" : [],
                    "inputLevel" : 0,
                    "isDisplay" : 1
                }, {
                    "id" : "sizeModelTry_shengao",
                    "name" : "身高（cm）",
                    "type" : "INPUT",
                    "rules" : [{
                        "name" : "requiredRule",
                        "value" : "true",
                        "valueIntervalInclude" : false
                    }],
                    "properties" : [],
                    "inputLevel" : 0,
                    "isDisplay" : 1
                }, {
                    "id" : "sizeModelTry_tizhong",
                    "name" : "体重（kg）",
                    "type" : "INPUT",
                    "rules" : [],
                    "properties" : [],
                    "inputLevel" : 0,
                    "isDisplay" : 1
                }, {
                    "id" : "sizeModelTry_result",
                    "name" : "试穿心得",
                    "type" : "INPUT",
                    "rules" : [],
                    "properties" : [],
                    "inputLevel" : 0,
                    "isDisplay" : 1
                }, {
                    "id" : "sizeModelTry_xiongwei",
                    "name" : "胸围（cm）",
                    "type" : "INPUT",
                    "rules" : [],
                    "properties" : [],
                    "inputLevel" : 0,
                    "isDisplay" : 1
                }, {
                    "id" : "sizeModelTry_yaowei",
                    "name" : "腰围（cm）",
                    "type" : "INPUT",
                    "rules" : [],
                    "properties" : [],
                    "inputLevel" : 0,
                    "isDisplay" : 1
                }, {
                    "id" : "sizeModelTry_tunwei",
                    "name" : "臀围（cm）",
                    "type" : "INPUT",
                    "rules" : [],
                    "properties" : [],
                    "inputLevel" : 0,
                    "isDisplay" : 1
                }]
            }, {
                "id" : "modulized_option",
                "name" : "分阶段发布商品",
                "type" : "SINGLECHECK",
                "rules" : [{
                    "name" : "tipRule",
                    "value" : "选择‘分阶段发布商品’模式，商品状态必须选择‘下架’。",
                    "valueIntervalInclude" : false
                }],
                "properties" : [],
                "inputLevel" : 2,
                "isDisplay" : 1,
                "defaultValueField" : {
                    "type" : "SINGLECHECK",
                    "rules" : [],
                    "properties" : [],
                    "inputLevel" : 0,
                    "isDisplay" : 1,
                    "options" : [],
                    "value" : {
                        "value" : "false"
                    }
                },
                "options" : [{
                    "displayName" : "是",
                    "value" : "true"
                }, {
                    "displayName" : "否",
                    "value" : "false"
                }],
                "value" : { }
            }, {
                "id" : "item_attach_images",
                "name" : "商品资质图片",
                "type" : "COMPLEX",
                "rules" : [],
                "properties" : [],
                "inputLevel" : 2,
                "isDisplay" : 1,
                "complexValue" : {
                    "fieldMap" : { }
                },
                "fields" : [{
                    "id" : "attach_51",
                    "name" : "吊牌图",
                    "type" : "INPUT",
                    "rules" : [{
                        "name" : "valueTypeRule",
                        "value" : "url",
                        "valueIntervalInclude" : false
                    }, {
                        "name" : "requiredRule",
                        "value" : "true",
                        "valueIntervalInclude" : false
                    }],
                    "properties" : [],
                    "inputLevel" : 0,
                    "isDisplay" : 1
                }, {
                    "id" : "attach_52",
                    "name" : "耐久性标签",
                    "type" : "INPUT",
                    "rules" : [{
                        "name" : "valueTypeRule",
                        "value" : "url",
                        "valueIntervalInclude" : false
                    }, {
                        "name" : "requiredRule",
                        "value" : "true",
                        "valueIntervalInclude" : false
                    }],
                    "properties" : [],
                    "inputLevel" : 0,
                    "isDisplay" : 1
                }, {
                    "id" : "attach_55",
                    "name" : "质检报告",
                    "type" : "INPUT",
                    "rules" : [{
                        "name" : "valueTypeRule",
                        "value" : "url",
                        "valueIntervalInclude" : false
                    }],
                    "properties" : [],
                    "inputLevel" : 0,
                    "isDisplay" : 1
                }, {
                    "id" : "attach_56",
                    "name" : "合格证",
                    "type" : "INPUT",
                    "rules" : [{
                        "name" : "valueTypeRule",
                        "value" : "url",
                        "valueIntervalInclude" : false
                    }],
                    "properties" : [],
                    "inputLevel" : 0,
                    "isDisplay" : 1
                }]
            }, {
                "id" : "images1",
                "name" : "商品图片",
                "type" : "MULTICOMPLEX",
                "rules" : [{
                    "name" : "requiredRule",
                    "value" : "true",
                    "valueIntervalInclude" : false
                }],
                "properties" : [],
                "inputLevel" : 0,
                "isDisplay" : 1,
                "values" : [],
                "fields" : [{
                    "id" : "image1",
                    "name" : "商品图片1",
                    "type" : "INPUT",
                    "rules" : [],
                    "properties" : [],
                    "inputLevel" : 0,
                    "isDisplay" : 1
                }]
            }, {
                "id" : "code",
                "name" : "产品code",
                "type" : "INPUT",
                "rules" : [{
                    "name" : "requiredRule",
                    "value" : "true",
                    "valueIntervalInclude" : false
                }],
                "properties" : [],
                "inputLevel" : 0,
                "isDisplay" : 1
            }, {
                "id" : "brand",
                "name" : "品牌",
                "type" : "LABEL",
                "rules" : [{
                    "name" : "requiredRule",
                    "value" : "true",
                    "valueIntervalInclude" : false
                }],
                "properties" : [],
                "inputLevel" : 0,
                "isDisplay" : 1,
                "labelGroup" : {
                    "labelGroupList" : [],
                    "labelList" : []
                }
            }, {
                "id" : "productNameCn",
                "name" : "产品名称（中文）",
                "type" : "INPUT",
                "rules" : [{
                    "name" : "requiredRule",
                    "value" : "true",
                    "valueIntervalInclude" : false
                }],
                "properties" : [],
                "inputLevel" : 0,
                "isDisplay" : 1
            }, {
                "id" : "productNameEn",
                "name" : "产品名称（英文）",
                "type" : "INPUT",
                "rules" : [{
                    "name" : "requiredRule",
                    "value" : "true",
                    "valueIntervalInclude" : false
                }],
                "properties" : [],
                "inputLevel" : 0,
                "isDisplay" : 1
            }, {
                "id" : "middleTitle",
                "name" : "中标题",
                "type" : "INPUT",
                "rules" : [{
                    "name" : "requiredRule",
                    "value" : "true",
                    "valueIntervalInclude" : false
                }],
                "properties" : [],
                "inputLevel" : 0,
                "isDisplay" : 1
            }, {
                "id" : "shortTitle",
                "name" : "短标题",
                "type" : "INPUT",
                "rules" : [{
                    "name" : "requiredRule",
                    "value" : "true",
                    "valueIntervalInclude" : false
                }],
                "properties" : [],
                "inputLevel" : 0,
                "isDisplay" : 1
            }, {
                "id" : "model",
                "name" : "款号",
                "type" : "INPUT",
                "rules" : [{
                    "name" : "requiredRule",
                    "value" : "true",
                    "valueIntervalInclude" : false
                }],
                "properties" : [],
                "inputLevel" : 0,
                "isDisplay" : 1
            }, {
                "id" : "color",
                "name" : "颜色",
                "type" : "INPUT",
                "rules" : [{
                    "name" : "requiredRule",
                    "value" : "true",
                    "valueIntervalInclude" : false
                }],
                "properties" : [],
                "inputLevel" : 0,
                "isDisplay" : 1
            }, {
                "id" : "origin",
                "name" : "产地",
                "type" : "INPUT",
                "rules" : [{
                    "name" : "requiredRule",
                    "value" : "true",
                    "valueIntervalInclude" : false
                }],
                "properties" : [],
                "inputLevel" : 0,
                "isDisplay" : 1
            }, {
                "id" : "shortDesCn",
                "name" : "简短描述中文",
                "type" : "INPUT",
                "rules" : [{
                    "name" : "requiredRule",
                    "value" : "true",
                    "valueIntervalInclude" : false
                }],
                "properties" : [],
                "inputLevel" : 0,
                "isDisplay" : 1
            }, {
                "id" : "longDesCn",
                "name" : "详情描述中文",
                "type" : "INPUT",
                "rules" : [{
                    "name" : "requiredRule",
                    "value" : "true",
                    "valueIntervalInclude" : false
                }],
                "properties" : [],
                "inputLevel" : 0,
                "isDisplay" : 1
            }, {
                "id" : "shortDesEn",
                "name" : "简短描述英语",
                "type" : "INPUT",
                "rules" : [{
                    "name" : "requiredRule",
                    "value" : "true",
                    "valueIntervalInclude" : false
                }],
                "properties" : [],
                "inputLevel" : 0,
                "isDisplay" : 1
            }, {
                "id" : "longDesEn",
                "name" : "详情描述英语",
                "type" : "INPUT",
                "rules" : [{
                    "name" : "requiredRule",
                    "value" : "true",
                    "valueIntervalInclude" : false
                }],
                "properties" : [],
                "inputLevel" : 0,
                "isDisplay" : 1
            }, {
                "id" : "hsCodeCrop",
                "name" : "税号集货",
                "type" : "INPUT",
                "rules" : [{
                    "name" : "requiredRule",
                    "value" : "true",
                    "valueIntervalInclude" : false
                }],
                "properties" : [],
                "inputLevel" : 0,
                "dataSource" : "optConfig",
                "isDisplay" : 1
            }, {
                "id" : "hsCodePrivate",
                "name" : "税号个人",
                "type" : "INPUT",
                "rules" : [{
                    "name" : "requiredRule",
                    "value" : "true",
                    "valueIntervalInclude" : false
                }],
                "properties" : [],
                "inputLevel" : 0,
                "dataSource" : "optConfig",
                "isDisplay" : 1
            }, {
                "id" : "priceMsrpSt",
                "name" : "官方建议售价",
                "type" : "LABEL",
                "rules" : [],
                "properties" : [],
                "inputLevel" : 0,
                "isDisplay" : 0,
                "labelGroup" : {
                    "labelGroupList" : [],
                    "labelList" : []
                }
            }, {
                "id" : "priceMsrpEd",
                "name" : "官方建议售价",
                "type" : "LABEL",
                "rules" : [],
                "properties" : [],
                "inputLevel" : 0,
                "isDisplay" : 0,
                "labelGroup" : {
                    "labelGroupList" : [],
                    "labelList" : []
                }
            }, {
                "id" : "priceRetailSt",
                "name" : "销售价格（未审批）",
                "type" : "LABEL",
                "rules" : [],
                "properties" : [],
                "inputLevel" : 0,
                "isDisplay" : 0,
                "labelGroup" : {
                    "labelGroupList" : [],
                    "labelList" : []
                }
            }, {
                "id" : "priceRetailEd",
                "name" : "销售价格（未审批）",
                "type" : "LABEL",
                "rules" : [],
                "properties" : [],
                "inputLevel" : 0,
                "isDisplay" : 0,
                "labelGroup" : {
                    "labelGroupList" : [],
                    "labelList" : []
                }
            }, {
                "id" : "priceSaleSt",
                "name" : "销售价格（已审批）",
                "type" : "LABEL",
                "rules" : [],
                "properties" : [],
                "inputLevel" : 0,
                "isDisplay" : 0,
                "labelGroup" : {
                    "labelGroupList" : [],
                    "labelList" : []
                }
            }, {
                "id" : "priceSaleEd",
                "name" : "销售价格（已审批）",
                "type" : "LABEL",
                "rules" : [],
                "properties" : [],
                "inputLevel" : 0,
                "isDisplay" : 0,
                "labelGroup" : {
                    "labelGroupList" : [],
                    "labelList" : []
                }
            }, {
                "id" : "status",
                "name" : "商品状态",
                "type" : "LABEL",
                "rules" : [],
                "properties" : [],
                "inputLevel" : 0,
                "isDisplay" : 0,
                "labelGroup" : {
                    "labelGroupList" : [],
                    "labelList" : []
                }
            }, {
                "id" : "priceChange",
                "name" : "价格审批flg",
                "type" : "LABEL",
                "rules" : [{
                    "name" : "requiredRule",
                    "value" : "true",
                    "valueIntervalInclude" : false
                }],
                "properties" : [],
                "inputLevel" : 0,
                "isDisplay" : 0,
                "labelGroup" : {
                    "labelGroupList" : [],
                    "labelList" : []
                }
            }, {
                "id" : "images2",
                "name" : "包装图片",
                "type" : "MULTICOMPLEX",
                "rules" : [],
                "properties" : [],
                "inputLevel" : 0,
                "isDisplay" : 1,
                "values" : [],
                "fields" : [{
                    "id" : "image2",
                    "name" : "包装图片2",
                    "type" : "INPUT",
                    "rules" : [],
                    "properties" : [],
                    "inputLevel" : 0,
                    "isDisplay" : 1
                }]
            }, {
                "id" : "images3",
                "name" : "带角度图片",
                "type" : "MULTICOMPLEX",
                "rules" : [],
                "properties" : [],
                "inputLevel" : 0,
                "isDisplay" : 1,
                "values" : [],
                "fields" : [{
                    "id" : "image3",
                    "name" : "带角度图片3",
                    "type" : "INPUT",
                    "rules" : [],
                    "properties" : [],
                    "inputLevel" : 0,
                    "isDisplay" : 1
                }]
            }, {
                "id" : "images4",
                "name" : "自定义图片",
                "type" : "MULTICOMPLEX",
                "rules" : [],
                "properties" : [],
                "inputLevel" : 0,
                "isDisplay" : 1,
                "values" : [],
                "fields" : [{
                    "id" : "image4",
                    "name" : "自定义图片4",
                    "type" : "INPUT",
                    "rules" : [],
                    "properties" : [],
                    "inputLevel" : 0,
                    "isDisplay" : 1
                }]
            }, {
                "id" : "lock",
                "name" : "lock商品",
                "type" : "SINGLECHECK",
                "rules" : [{
                    "name" : "requiredRule",
                    "value" : "true",
                    "valueIntervalInclude" : false
                }],
                "properties" : [],
                "inputLevel" : 0,
                "isDisplay" : 1,
                "options" : [],
                "value" : { }
            }],
            "sku" : {
                "id" : "sku",
                "name" : "SKU",
                "type" : "MULTICOMPLEX",
                "rules" : [{
                    "name" : "1256266624_1",
                    "value" : "SKU与类目销售属性必须匹配",
                    "valueIntervalInclude" : false
                }, {
                    "name" : "13932264_1",
                    "value" : "如果销售属性存在套餐且存在官方标配，官方标配必选",
                    "valueIntervalInclude" : false
                }],
                "properties" : [],
                "inputLevel" : 2,
                "isDisplay" : 1,
                "values" : [],
                "fields" : [{
                    "id" : "std_size_prop_20509_-1",
                    "name" : "“自定义”尺码",
                    "type" : "INPUT",
                    "rules" : [{
                        "name" : "tipRule",
                        "value" : "自定义尺码值只能输入以下格式：【数字/字母/数字；字母/字母，字母+数字 字母/数字；数字/字母；数字+字母；数字/数字；数字/数字/字母；字母；数字/数字+字母/字母；数字/数字+字母；数字】，并支持在上述格式前添加性别如“男/女/男童/女童”。若无支持的格式，可最多新增一个不在上述格式范围内的尺码值",
                        "valueIntervalInclude" : false
                    }, {
                        "name" : "disableRule",
                        "value" : "true",
                        "dependGroup" : {
                            "dependExpressList" : [{
                                "fieldId" : "std_size_group",
                                "value" : "-1:自定义:-1",
                                "symbol" : "!="
                            }],
                            "operator" : "and",
                            "dependGroupList" : [],
                            "empty" : false
                        },
                        "valueIntervalInclude" : false
                    }],
                    "properties" : [],
                    "inputLevel" : 0,
                    "isDisplay" : 1
                }, {
                    "id" : "priceSale",
                    "name" : "销售价格（已审批）",
                    "type" : "INPUT",
                    "rules" : [{
                        "name" : "valueTypeRule",
                        "value" : "decimal",
                        "valueIntervalInclude" : false
                    }, {
                        "name" : "minValueRule",
                        "value" : "0.00",
                        "exProperty" : "not include",
                        "valueIntervalInclude" : false
                    }, {
                        "name" : "maxValueRule",
                        "value" : "100000000.00",
                        "exProperty" : "not include",
                        "valueIntervalInclude" : false
                    }, {
                        "name" : "1801802460_1",
                        "value" : "SKU价格受类目限制",
                        "valueIntervalInclude" : false
                    }, {
                        "name" : "requiredRule",
                        "value" : "true",
                        "valueIntervalInclude" : false
                    }],
                    "properties" : [],
                    "inputLevel" : 0,
                    "inputOrgId" : "sku_price",
                    "isDisplay" : 1
                }, {
                    "id" : "qty",
                    "name" : "库存",
                    "type" : "INPUT",
                    "rules" : [{
                        "name" : "valueTypeRule",
                        "value" : "integer",
                        "valueIntervalInclude" : false
                    }, {
                        "name" : "minValueRule",
                        "value" : "0",
                        "exProperty" : "include",
                        "valueIntervalInclude" : true
                    }, {
                        "name" : "maxValueRule",
                        "value" : "1000000",
                        "exProperty" : "not include",
                        "valueIntervalInclude" : false
                    }, {
                        "name" : "requiredRule",
                        "value" : "true",
                        "valueIntervalInclude" : false
                    }],
                    "properties" : [],
                    "inputLevel" : 0,
                    "inputOrgId" : "sku_quantity",
                    "isDisplay" : 1
                }, {
                    "id" : "skuCode",
                    "name" : "skuCode",
                    "type" : "INPUT",
                    "rules" : [{
                        "name" : "valueTypeRule",
                        "value" : "text",
                        "valueIntervalInclude" : false
                    }, {
                        "name" : "maxLengthRule",
                        "value" : "64",
                        "exProperty" : "include",
                        "unit" : "byte",
                        "valueIntervalInclude" : true
                    }, {
                        "name" : "requiredRule",
                        "value" : "true",
                        "valueIntervalInclude" : false
                    }],
                    "properties" : [],
                    "inputLevel" : 0,
                    "inputOrgId" : "sku_outerId",
                    "isDisplay" : 1
                }, {
                    "id" : "barcode",
                    "name" : "barcode",
                    "type" : "INPUT",
                    "rules" : [{
                        "name" : "valueTypeRule",
                        "value" : "text",
                        "valueIntervalInclude" : false
                    }, {
                        "name" : "1227038873_1",
                        "value" : "条形码必须符合EAN和UPC编码规则",
                        "valueIntervalInclude" : false
                    }, {
                        "name" : "requiredRule",
                        "value" : "true",
                        "valueIntervalInclude" : false
                    }],
                    "properties" : [],
                    "inputLevel" : 0,
                    "inputOrgId" : "sku_barcode",
                    "isDisplay" : 1
                }, {
                    "id" : "sku_MarketTime",
                    "name" : "上市时间",
                    "type" : "INPUT",
                    "rules" : [{
                        "name" : "valueTypeRule",
                        "value" : "date",
                        "valueIntervalInclude" : false
                    }, {
                        "name" : "requiredRule",
                        "value" : "true",
                        "valueIntervalInclude" : false
                    }],
                    "properties" : [],
                    "inputLevel" : 0,
                    "isDisplay" : 1
                }, {
                    "id" : "sku_ProductCode",
                    "name" : "货号",
                    "type" : "INPUT",
                    "rules" : [{
                        "name" : "valueTypeRule",
                        "value" : "text",
                        "valueIntervalInclude" : false
                    }, {
                        "name" : "minLengthRule",
                        "value" : "1",
                        "exProperty" : "include",
                        "unit" : "character",
                        "valueIntervalInclude" : true
                    }, {
                        "name" : "maxLengthRule",
                        "value" : "30",
                        "exProperty" : "include",
                        "unit" : "character",
                        "valueIntervalInclude" : true
                    }],
                    "properties" : [],
                    "inputLevel" : 0,
                    "isDisplay" : 1
                }, {
                    "id" : "size",
                    "name" : "尺码",
                    "type" : "LABEL",
                    "rules" : [{
                        "name" : "requiredRule",
                        "value" : "true",
                        "valueIntervalInclude" : false
                    }],
                    "properties" : [],
                    "inputLevel" : 0,
                    "isDisplay" : 1,
                    "labelGroup" : {
                        "labelGroupList" : [],
                        "labelList" : []
                    }
                }, {
                    "id" : "priceMsrp",
                    "name" : "官方建议售价",
                    "type" : "INPUT",
                    "rules" : [{
                        "name" : "requiredRule",
                        "value" : "true",
                        "valueIntervalInclude" : false
                    }],
                    "properties" : [],
                    "inputLevel" : 0,
                    "isDisplay" : 1
                }, {
                    "id" : "priceRetail",
                    "name" : "销售价格（未审批）",
                    "type" : "INPUT",
                    "rules" : [{
                        "name" : "requiredRule",
                        "value" : "true",
                        "valueIntervalInclude" : false
                    }],
                    "properties" : [],
                    "inputLevel" : 0,
                    "isDisplay" : 1
                }, {
                    "id" : "platform",
                    "name" : "平台状态",
                    "type" : "INPUT",
                    "rules" : [{
                        "name" : "requiredRule",
                        "value" : "true",
                        "valueIntervalInclude" : false
                    }],
                    "properties" : [],
                    "inputLevel" : 0,
                    "isDisplay" : 1
                }]
            }
        }

    };
});
