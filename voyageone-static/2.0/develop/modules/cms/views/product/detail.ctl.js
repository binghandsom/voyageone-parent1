/**
 * @Description:
 *
 * @User: linanbin
 * @Version: 2.0.0, 15/12/24
 */

define([
    'cms',
    'modules/cms/controller/popup.ctl',
    'modules/cms/service/productDetail.service'
], function (cms) {
    return cms.controller('productDetailController', (function () {

        function productDetailController($routeParams, productDetailService) {

            this.routeParams = $routeParams;
            this.productDetailService = productDetailService;

            this.productDetails = null;
            this.productDetailsCopy = null;


           /* this.productDetails = {
                fields: [
                    {
                        id: "id1",
                        name: "name1",
                        type: "INPUT",
                        rules: [
                            {
                                name: "valueTypeRule",
                                value: "text"
                            },
                            {
                                name: "requiredRule",
                                value: "true"
                            },
                            {
                                name: "tipRule",
                                value: "this is a test"
                            }
                        ],
                        value: "input1"
                    },
                    {
                        id: "id2",
                        name: "name2",
                        type: "INPUT",
                        rules: [
                            {
                                name: "valueTypeRule",
                                value: "decimal"
                            },
                            {
                                name: "requiredRule",
                                value: "true"
                            }
                        ],
                        value: "decimal1"
                    },
                    {
                        id: "id3",
                        name: "name3",
                        type: "INPUT",
                        rules: [
                            {
                                name: "valueTypeRule",
                                value: "integer"
                            }
                        ],
                        value: "integer1"
                    },
                    {
                        id: "id4",
                        name: "name4",
                        type: "INPUT",
                        rules: [
                            {
                                name: "valueTypeRule",
                                value: "long"
                            }
                        ],
                        value: "long1"
                    },
                    {
                        id: "id5",
                        name: "name5",
                        type: "INPUT",
                        rules: [
                            {
                                name: "valueTypeRule",
                                value: "date"
                            }
                        ],
                        value: "2015-12-22"
                    },
                    {
                        id: "id6",
                        name: "name6",
                        type: "INPUT",
                        rules: [
                            {
                                name: "valueTypeRule",
                                value: "time"
                            }
                        ],
                        value: "2015-12-22 13:00:00"
                    },
                    {
                        id: "id7",
                        name: "name7",
                        type: "INPUT",
                        rules: [
                            {
                                name: "valueTypeRule",
                                value: "url"
                            }
                        ],
                        value: "url1"
                    },
                    {
                        id: "id8",
                        name: "name8",
                        type: "INPUT",
                        rules: [
                            {
                                name: "valueTypeRule",
                                value: "textarea"
                            }
                        ],
                        value: "textarea1"
                    },
                    {
                        id: "id9",
                        name: "name9",
                        type: "INPUT",
                        rules: [
                            {
                                name: "valueTypeRule",
                                value: "html"
                            }
                        ],
                        value: "html1"
                    },
                    {
                        id: "id10",
                        name: "name10",
                        type: "SINGLECHECK",
                        options: [
                            {
                                displayName: "single1",
                                value: "1"
                            },
                            {
                                displayName: "single2",
                                value: "2"
                            }
                        ],
                        rules: [
                            {
                                name: "requiredRule",
                                value: "true"
                            }
                        ],
                        value: {
                            id: null,
                            value: "2"
                        }
                    },
                    {
                        id: "id11",
                        name: "name11",
                        type: "RADIO",
                        options: [
                            {
                                displayName: "single1",
                                value: "1"
                            },
                            {
                                displayName: "single2",
                                value: "2"
                            }
                        ],
                        rules: [
                            {
                                name: "requiredRule",
                                value: "true"
                            }
                        ],
                        value: {
                            id: null,
                            value: "2"
                        }
                    },
                    {
                        id: "id12",
                        name: "name12",
                        type: "MULTICHECK",
                        options: [
                            {
                                displayName: "single1",
                                value: "1"
                            },
                            {
                                displayName: "single2",
                                value: "2"
                            },
                            {
                                displayName: "single3",
                                value: "3"
                            }
                        ],
                        rules: [
                            {
                                name: "requiredRule",
                                value: "true"
                            }
                        ],
                        values: [
                            {
                                id: null,
                                value: "2"
                            },
                            {
                                id: null,
                                value: "1"
                            }
                        ]
                    },
                    {
                        id: "id13",
                        name: "name13",
                        type: "MULTICOMPLEX",
                        rules: [
                            {
                                name: "requiredRule",
                                value: "true"
                            },
                            {
                                name: "tipRule",
                                value: "this is a test"
                            },
                            {
                                name: "tipRule",
                                value: "this is a test 2"
                            }
                        ],
                        fields: [
                            {
                                id: "id1",
                                name: "name1",
                                type: "INPUT",
                                rules: [
                                    {
                                        name: "valueTypeRule",
                                        value: "text"
                                    },
                                    {
                                        name: "tipRule",
                                        value: "this is a test"
                                    }
                                ]
                            },
                            {
                                id: "id5",
                                name: "name5",
                                type: "INPUT",
                                rules: [
                                    {
                                        name: "valueTypeRule",
                                        value: "date"
                                    }
                                ]
                            },
                            {
                                id: "id8",
                                name: "name8",
                                type: "INPUT",
                                rules: [
                                    {
                                        name: "valueTypeRule",
                                        value: "textarea"
                                    }
                                ]
                            },
                            {
                                id: "id10",
                                name: "name10",
                                type: "SINGLECHECK",
                                options: [
                                    {
                                        displayName: "single1",
                                        value: "1"
                                    },
                                    {
                                        displayName: "single2",
                                        value: "2"
                                    }
                                ],
                                rules: [
                                    {
                                        name: "requiredRule",
                                        value: "true"
                                    }
                                ]
                            },
                            {
                                id: "id110",
                                name: "name11",
                                type: "RADIO",
                                options: [
                                    {
                                        displayName: "single1",
                                        value: "1"
                                    },
                                    {
                                        displayName: "single2",
                                        value: "2"
                                    }
                                ],
                                rules: [
                                    {
                                        name: "requiredRule",
                                        value: "true"
                                    }
                                ]
                            },
                            {
                                id: "id12",
                                name: "name12",
                                type: "MULTICHECK",
                                options: [
                                    {
                                        displayName: "single1",
                                        value: "1"
                                    },
                                    {
                                        displayName: "single2",
                                        value: "2"
                                    },
                                    {
                                        displayName: "single3",
                                        value: "3"
                                    }
                                ],
                                rules: [
                                    {
                                        name: "requiredRule",
                                        value: "true"
                                    }
                                ]
                            }
                        ],
                        values: [
                            {
                                fieldMap: {
                                    id1: {
                                        id: "id1",
                                        name: "name1",
                                        type: "INPUT",
                                        rules: [
                                            {
                                                name: "valueTypeRule",
                                                value: "text"
                                            },
                                            {
                                                name: "requiredRule",
                                                value: "true"
                                            },
                                            {
                                                name: "tipRule",
                                                value: "this is a test"
                                            }
                                        ],
                                        value: "input1"
                                    },
                                    id5: {
                                        id: "id5",
                                        name: "name5",
                                        type: "INPUT",
                                        rules: [
                                            {
                                                name: "valueTypeRule",
                                                value: "date"
                                            }
                                        ],
                                        value: "2015-12-22"
                                    },
                                    id8: {
                                        id: "id8",
                                        name: "name8",
                                        type: "INPUT",
                                        rules: [
                                            {
                                                name: "valueTypeRule",
                                                value: "textarea"
                                            }
                                        ],
                                        value: "textarea1"
                                    },
                                    id10: {
                                        id: "id10",
                                        name: "name10",
                                        type: "SINGLECHECK",
                                        options: [
                                            {
                                                displayName: "single1",
                                                value: "1"
                                            },
                                            {
                                                displayName: "single2",
                                                value: "2"
                                            }
                                        ],
                                        rules: [
                                            {
                                                name: "requiredRule",
                                                value: "true"
                                            }
                                        ],
                                        value: {
                                            id: null,
                                            value: "2"
                                        }
                                    },
                                    id110: {
                                        id: "id110",
                                        name: "name110",
                                        type: "RADIO",
                                        options: [
                                            {
                                                displayName: "single1",
                                                value: "1"
                                            },
                                            {
                                                displayName: "single2",
                                                value: "2"
                                            }
                                        ],
                                        rules: [
                                            {
                                                name: "requiredRule",
                                                value: "true"
                                            }
                                        ],
                                        value: {
                                            id: null,
                                            value: "1"
                                        }
                                    },
                                    id12: {
                                        id: "id12",
                                        name: "name12",
                                        type: "MULTICHECK",
                                        options: [
                                            {
                                                displayName: "single1",
                                                value: "1"
                                            },
                                            {
                                                displayName: "single2",
                                                value: "2"
                                            },
                                            {
                                                displayName: "single3",
                                                value: "3"
                                            }
                                        ],
                                        rules: [
                                            {
                                                name: "requiredRule",
                                                value: "true"
                                            }
                                        ],
                                        values: [
                                            {
                                                id: null,
                                                value: "2"
                                            },
                                            {
                                                id: null,
                                                value: "1"
                                            }
                                        ]
                                    }
                                }
                            },
                            {
                                fieldMap: {
                                    id1: {
                                        id: "id1",
                                        name: "name1",
                                        type: "INPUT",
                                        rules: [
                                            {
                                                name: "valueTypeRule",
                                                value: "text"
                                            },
                                            {
                                                name: "requiredRule",
                                                value: "true"
                                            },
                                            {
                                                name: "tipRule",
                                                value: "this is a test"
                                            }
                                        ],
                                        value: "input1"
                                    },
                                    id8: {
                                        id: "id8",
                                        name: "name8",
                                        type: "INPUT",
                                        rules: [
                                            {
                                                name: "valueTypeRule",
                                                value: "textarea"
                                            }
                                        ],
                                        value: "textarea1"
                                    },
                                    id10: {
                                        id: "id10",
                                        name: "name10",
                                        type: "SINGLECHECK",
                                        options: [
                                            {
                                                displayName: "single1",
                                                value: "1"
                                            },
                                            {
                                                displayName: "single2",
                                                value: "2"
                                            }
                                        ],
                                        rules: [
                                            {
                                                name: "requiredRule",
                                                value: "true"
                                            }
                                        ],
                                        value: {
                                            id: null,
                                            value: "2"
                                        }
                                    },
                                    id110: {
                                        id: "id110",
                                        name: "name110",
                                        type: "RADIO",
                                        options: [
                                            {
                                                displayName: "single1",
                                                value: "1"
                                            },
                                            {
                                                displayName: "single2",
                                                value: "2"
                                            }
                                        ],
                                        rules: [
                                            {
                                                name: "requiredRule",
                                                value: "true"
                                            }
                                        ],
                                        value: {
                                            id: null,
                                            value: "2"
                                        }
                                    },
                                    id12: {
                                        id: "id12",
                                        name: "name12",
                                        type: "MULTICHECK",
                                        options: [
                                            {
                                                displayName: "single1",
                                                value: "1"
                                            },
                                            {
                                                displayName: "single2",
                                                value: "2"
                                            },
                                            {
                                                displayName: "single3",
                                                value: "3"
                                            }
                                        ],
                                        rules: [
                                            {
                                                name: "requiredRule",
                                                value: "true"
                                            }
                                        ],
                                        values: [
                                            {
                                                id: null,
                                                value: "2"
                                            },
                                            {
                                                id: null,
                                                value: "1"
                                            }
                                        ]
                                    }
                                }
                            }
                        ]
                    },
                    {
                        id: "id14",
                        name: "name14",
                        type: "COMPLEX",
                        rules: [
                            {
                                name: "requiredRule",
                                value: "true"
                            },
                            {
                                name: "tipRule",
                                value: "hello world!!!"
                            }
                        ],
                        fields: [
                            {
                                id: "id1",
                                name: "name1",
                                type: "INPUT",
                                rules: [
                                    {
                                        name: "valueTypeRule",
                                        value: "text"
                                    },
                                    {
                                        name: 'tipRule',
                                        value: "this is a test"
                                    },
                                    {
                                        name: 'tipRule',
                                        value: "this is a test2"
                                    }
                                ]
                            },
                            {
                                id: "id5",
                                name: "name5",
                                type: "INPUT",
                                rules: [
                                    {
                                        name: "valueTypeRule",
                                        value: "date"
                                    }
                                ]
                            },
                            {
                                id: "id8",
                                name: "name8",
                                type: "INPUT",
                                rules: [
                                    {
                                        name: "valueTypeRule",
                                        value: "textarea"
                                    }
                                ]
                            },
                            {
                                id: "id10",
                                name: "name10",
                                type: "SINGLECHECK",
                                options: [
                                    {
                                        displayName: "single1",
                                        value: "1"
                                    },
                                    {
                                        displayName: "single2",
                                        value: "2"
                                    }
                                ],
                                rules: [
                                    {
                                        name: "requiredRule",
                                        value: "true"
                                    }
                                ]
                            },
                            {
                                id: "id11",
                                name: "name11",
                                type: "RADIO",
                                options: [
                                    {
                                        displayName: "single1",
                                        value: "1"
                                    },
                                    {
                                        displayName: "single2",
                                        value: "2"
                                    }
                                ],
                                rules: [
                                    {
                                        name: "requiredRule",
                                        value: "true"
                                    }
                                ]
                            },
                            {
                                id: "id12",
                                name: "name12",
                                type: "MULTICHECK",
                                options: [
                                    {
                                        displayName: "single1",
                                        value: "1"
                                    },
                                    {
                                        displayName: "single2",
                                        value: "2"
                                    },
                                    {
                                        displayName: "single3",
                                        value: "3"
                                    }
                                ],
                                rules: [
                                    {
                                        name: "requiredRule",
                                        value: "true"
                                    }
                                ]
                            },
                            {
                                id: "id90",
                                name: "name90",
                                type: "COMPLEX",
                                rules: [
                                    {
                                        name: "requiredRule",
                                        value: "true"
                                    },
                                    {
                                        name: "tipRule",
                                        value: "this is a test 3"
                                    }
                                ],
                                fields: [
                                    {
                                        id: "id1",
                                        name: "name1",
                                        type: "INPUT",
                                        rules: [
                                            {
                                                name: "valueTypeRule",
                                                value: "text"
                                            },
                                            {
                                                name: 'tipRule',
                                                value: "this is a test"
                                            },
                                            {
                                                name: 'tipRule',
                                                value: "this is a test2"
                                            }
                                        ]
                                    },
                                    {
                                        id: "id5",
                                        name: "name5",
                                        type: "INPUT",
                                        rules: [
                                            {
                                                name: "valueTypeRule",
                                                value: "date"
                                            }
                                        ]
                                    },
                                    {
                                        id: "id8",
                                        name: "name8",
                                        type: "INPUT",
                                        rules: [
                                            {
                                                name: "valueTypeRule",
                                                value: "textarea"
                                            }
                                        ]
                                    },
                                    {
                                        id: "id10",
                                        name: "name10",
                                        type: "SINGLECHECK",
                                        options: [
                                            {
                                                displayName: "single1",
                                                value: "1"
                                            },
                                            {
                                                displayName: "single2",
                                                value: "2"
                                            }
                                        ],
                                        rules: [
                                            {
                                                name: "requiredRule",
                                                value: "true"
                                            }
                                        ]
                                    },
                                    {
                                        id: "id11",
                                        name: "name11",
                                        type: "RADIO",
                                        options: [
                                            {
                                                displayName: "single1",
                                                value: "1"
                                            },
                                            {
                                                displayName: "single2",
                                                value: "2"
                                            }
                                        ],
                                        rules: [
                                            {
                                                name: "requiredRule",
                                                value: "true"
                                            }
                                        ]
                                    },
                                    {
                                        id: "id12",
                                        name: "name12",
                                        type: "MULTICHECK",
                                        options: [
                                            {
                                                displayName: "single1",
                                                value: "1"
                                            },
                                            {
                                                displayName: "single2",
                                                value: "2"
                                            },
                                            {
                                                displayName: "single3",
                                                value: "3"
                                            }
                                        ],
                                        rules: [
                                            {
                                                name: "requiredRule",
                                                value: "true"
                                            }
                                        ]
                                    },
                                    {
                                        id: "id90",
                                        name: "name90",
                                        type: "COMPLEX",
                                        rules: [
                                            {
                                                name: "requiredRule",
                                                value: "true"
                                            },
                                            {
                                                name: "tipRule",
                                                value: "this is a test 3"
                                            }
                                        ],
                                        fields: [
                                            {
                                                id: "id1",
                                                name: "name1",
                                                type: "INPUT",
                                                rules: [
                                                    {
                                                        name: "valueTypeRule",
                                                        value: "text"
                                                    },
                                                    {
                                                        name: 'tipRule',
                                                        value: "this is a test"
                                                    },
                                                    {
                                                        name: 'tipRule',
                                                        value: "this is a test2"
                                                    }
                                                ]
                                            }
                                        ]
                                    }
                                ]
                            },
                            {
                                id: "id13",
                                name: "name13",
                                type: "MULTICOMPLEX",
                                rules: [
                                    {
                                        name: "requiredRule",
                                        value: "true"
                                    },
                                    {
                                        name: "tipRule",
                                        value: "this is a test"
                                    },
                                    {
                                        name: "tipRule",
                                        value: "this is a test 2"
                                    }
                                ],
                                fields: [
                                    {
                                        id: "id1",
                                        name: "name1",
                                        type: "INPUT",
                                        rules: [
                                            {
                                                name: "valueTypeRule",
                                                value: "text"
                                            },
                                            {
                                                name: "tipRule",
                                                value: "this is a test"
                                            }
                                        ]
                                    },
                                    {
                                        id: "id5",
                                        name: "name5",
                                        type: "INPUT",
                                        rules: [
                                            {
                                                name: "valueTypeRule",
                                                value: "date"
                                            }
                                        ]
                                    },
                                    {
                                        id: "id8",
                                        name: "name8",
                                        type: "INPUT",
                                        rules: [
                                            {
                                                name: "valueTypeRule",
                                                value: "textarea"
                                            }
                                        ]
                                    },
                                    {
                                        id: "id10",
                                        name: "name10",
                                        type: "SINGLECHECK",
                                        options: [
                                            {
                                                displayName: "single1",
                                                value: "1"
                                            },
                                            {
                                                displayName: "single2",
                                                value: "2"
                                            }
                                        ],
                                        rules: [
                                            {
                                                name: "requiredRule",
                                                value: "true"
                                            }
                                        ]
                                    },
                                    {
                                        id: "id110",
                                        name: "name11",
                                        type: "RADIO",
                                        options: [
                                            {
                                                displayName: "single1",
                                                value: "1"
                                            },
                                            {
                                                displayName: "single2",
                                                value: "2"
                                            }
                                        ],
                                        rules: [
                                            {
                                                name: "requiredRule",
                                                value: "true"
                                            }
                                        ]
                                    },
                                    {
                                        id: "id12",
                                        name: "name12",
                                        type: "MULTICHECK",
                                        options: [
                                            {
                                                displayName: "single1",
                                                value: "1"
                                            },
                                            {
                                                displayName: "single2",
                                                value: "2"
                                            },
                                            {
                                                displayName: "single3",
                                                value: "3"
                                            }
                                        ],
                                        rules: [
                                            {
                                                name: "requiredRule",
                                                value: "true"
                                            }
                                        ]
                                    }
                                ],
                                values: [
                                    {
                                        fieldMap: {
                                            id1: {
                                                id: "id1",
                                                name: "name1",
                                                type: "INPUT",
                                                rules: [
                                                    {
                                                        name: "valueTypeRule",
                                                        value: "text"
                                                    },
                                                    {
                                                        name: "requiredRule",
                                                        value: "true"
                                                    },
                                                    {
                                                        name: "tipRule",
                                                        value: "this is a test"
                                                    }
                                                ],
                                                value: "input1"
                                            },
                                            id5: {
                                                id: "id5",
                                                name: "name5",
                                                type: "INPUT",
                                                rules: [
                                                    {
                                                        name: "valueTypeRule",
                                                        value: "date"
                                                    }
                                                ],
                                                value: "2015-12-22"
                                            },
                                            id8: {
                                                id: "id8",
                                                name: "name8",
                                                type: "INPUT",
                                                rules: [
                                                    {
                                                        name: "valueTypeRule",
                                                        value: "textarea"
                                                    }
                                                ],
                                                value: "textarea1"
                                            },
                                            id10: {
                                                id: "id10",
                                                name: "name10",
                                                type: "SINGLECHECK",
                                                options: [
                                                    {
                                                        displayName: "single1",
                                                        value: "1"
                                                    },
                                                    {
                                                        displayName: "single2",
                                                        value: "2"
                                                    }
                                                ],
                                                rules: [
                                                    {
                                                        name: "requiredRule",
                                                        value: "true"
                                                    }
                                                ],
                                                value: {
                                                    id: null,
                                                    value: "2"
                                                }
                                            },
                                            id110: {
                                                id: "id110",
                                                name: "name110",
                                                type: "RADIO",
                                                options: [
                                                    {
                                                        displayName: "single1",
                                                        value: "1"
                                                    },
                                                    {
                                                        displayName: "single2",
                                                        value: "2"
                                                    }
                                                ],
                                                rules: [
                                                    {
                                                        name: "requiredRule",
                                                        value: "true"
                                                    }
                                                ],
                                                value: {
                                                    id: null,
                                                    value: "1"
                                                }
                                            },
                                            id12: {
                                                id: "id12",
                                                name: "name12",
                                                type: "MULTICHECK",
                                                options: [
                                                    {
                                                        displayName: "single1",
                                                        value: "1"
                                                    },
                                                    {
                                                        displayName: "single2",
                                                        value: "2"
                                                    },
                                                    {
                                                        displayName: "single3",
                                                        value: "3"
                                                    }
                                                ],
                                                rules: [
                                                    {
                                                        name: "requiredRule",
                                                        value: "true"
                                                    }
                                                ],
                                                values: [
                                                    {
                                                        id: null,
                                                        value: "2"
                                                    },
                                                    {
                                                        id: null,
                                                        value: "1"
                                                    }
                                                ]
                                            }
                                        }
                                    },
                                    {
                                        fieldMap: {
                                            id1: {
                                                id: "id1",
                                                name: "name1",
                                                type: "INPUT",
                                                rules: [
                                                    {
                                                        name: "valueTypeRule",
                                                        value: "text"
                                                    },
                                                    {
                                                        name: "requiredRule",
                                                        value: "true"
                                                    },
                                                    {
                                                        name: "tipRule",
                                                        value: "this is a test"
                                                    }
                                                ],
                                                value: "input1"
                                            },
                                            id8: {
                                                id: "id8",
                                                name: "name8",
                                                type: "INPUT",
                                                rules: [
                                                    {
                                                        name: "valueTypeRule",
                                                        value: "textarea"
                                                    }
                                                ],
                                                value: "textarea1"
                                            },
                                            id10: {
                                                id: "id10",
                                                name: "name10",
                                                type: "SINGLECHECK",
                                                options: [
                                                    {
                                                        displayName: "single1",
                                                        value: "1"
                                                    },
                                                    {
                                                        displayName: "single2",
                                                        value: "2"
                                                    }
                                                ],
                                                rules: [
                                                    {
                                                        name: "requiredRule",
                                                        value: "true"
                                                    }
                                                ],
                                                value: {
                                                    id: null,
                                                    value: "2"
                                                }
                                            },
                                            id110: {
                                                id: "id110",
                                                name: "name110",
                                                type: "RADIO",
                                                options: [
                                                    {
                                                        displayName: "single1",
                                                        value: "1"
                                                    },
                                                    {
                                                        displayName: "single2",
                                                        value: "2"
                                                    }
                                                ],
                                                rules: [
                                                    {
                                                        name: "requiredRule",
                                                        value: "true"
                                                    }
                                                ],
                                                value: {
                                                    id: null,
                                                    value: "2"
                                                }
                                            },
                                            id12: {
                                                id: "id12",
                                                name: "name12",
                                                type: "MULTICHECK",
                                                options: [
                                                    {
                                                        displayName: "single1",
                                                        value: "1"
                                                    },
                                                    {
                                                        displayName: "single2",
                                                        value: "2"
                                                    },
                                                    {
                                                        displayName: "single3",
                                                        value: "3"
                                                    }
                                                ],
                                                rules: [
                                                    {
                                                        name: "requiredRule",
                                                        value: "true"
                                                    }
                                                ],
                                                values: [
                                                    {
                                                        id: null,
                                                        value: "2"
                                                    },
                                                    {
                                                        id: null,
                                                        value: "1"
                                                    }
                                                ]
                                            }
                                        }
                                    }
                                ]
                            }
                        ],
                        complexValue: {
                            fieldMap: {
                                id1: {
                                    id: "id1",
                                    name: "name1",
                                    type: "INPUT",
                                    rules: [
                                        {
                                            name: "valueTypeRule",
                                            value: "text"
                                        },
                                        {
                                            name: 'tipRule',
                                            value: "this is a test"
                                        },
                                        {
                                            name: 'tipRule',
                                            value: "this is a test2"
                                        }
                                    ],
                                    value: "hello1"
                                },
                                id5: {
                                    id: "id5",
                                    name: "name5",
                                    type: "INPUT",
                                    rules: [
                                        {
                                            name: "valueTypeRule",
                                            value: "date"
                                        }
                                    ],
                                    value: "2015-12-30"
                                },
                                id8: {
                                    id: "id8",
                                    name: "name8",
                                    type: "INPUT",
                                    rules: [
                                        {
                                            name: "valueTypeRule",
                                            value: "textarea"
                                        }
                                    ],
                                    value: "this is a textarea"
                                },
                                id10: {
                                    id: "id10",
                                    name: "name10",
                                    type: "SINGLECHECK",
                                    options: [
                                        {
                                            displayName: "single1",
                                            value: "1"
                                        },
                                        {
                                            displayName: "single2",
                                            value: "2"
                                        }
                                    ],
                                    rules: [
                                        {
                                            name: "requiredRule",
                                            value: "true"
                                        }
                                    ],
                                    value: {
                                        id: null, value: "2"
                                    }
                                },
                                id11: {
                                    id: "id11",
                                    name: "name11",
                                    type: "RADIO",
                                    options: [
                                        {
                                            displayName: "single1",
                                            value: "1"
                                        },
                                        {
                                            displayName: "single2",
                                            value: "2"
                                        }
                                    ],
                                    rules: [
                                        {
                                            name: "requiredRule",
                                            value: "true"
                                        }
                                    ],
                                    value: {
                                        id: null, value: "1"
                                    }
                                },
                                id12: {
                                    id: "id12",
                                    name: "name12",
                                    type: "MULTICHECK",
                                    options: [
                                        {
                                            displayName: "single1",
                                            value: "1"
                                        },
                                        {
                                            displayName: "single2",
                                            value: "2"
                                        },
                                        {
                                            displayName: "single3",
                                            value: "3"
                                        }
                                    ],
                                    rules: [
                                        {
                                            name: "requiredRule",
                                            value: "true"
                                        }
                                    ],
                                    values: [
                                        {id: null, value: "1"}, {id:null, value: "3"}
                                    ]
                                },
                                id90: {
                                    id: "id90",
                                    name: "name90",
                                    type: "COMPLEX",
                                    rules: [
                                        {
                                            name: "requiredRule",
                                            value: "true"
                                        },
                                        {
                                            name: "tipRule",
                                            value: "this is a test 3"
                                        }
                                    ],
                                    fields: [
                                        {
                                            id: "id1",
                                            name: "name1",
                                            type: "INPUT",
                                            rules: [
                                                {
                                                    name: "valueTypeRule",
                                                    value: "text"
                                                },
                                                {
                                                    name: 'tipRule',
                                                    value: "this is a test"
                                                },
                                                {
                                                    name: 'tipRule',
                                                    value: "this is a test2"
                                                }
                                            ]
                                        },
                                        {
                                            id: "id5",
                                            name: "name5",
                                            type: "INPUT",
                                            rules: [
                                                {
                                                    name: "valueTypeRule",
                                                    value: "date"
                                                }
                                            ]
                                        },
                                        {
                                            id: "id8",
                                            name: "name8",
                                            type: "INPUT",
                                            rules: [
                                                {
                                                    name: "valueTypeRule",
                                                    value: "textarea"
                                                }
                                            ]
                                        },
                                        {
                                            id: "id10",
                                            name: "name10",
                                            type: "SINGLECHECK",
                                            options: [
                                                {
                                                    displayName: "single1",
                                                    value: "1"
                                                },
                                                {
                                                    displayName: "single2",
                                                    value: "2"
                                                }
                                            ],
                                            rules: [
                                                {
                                                    name: "requiredRule",
                                                    value: "true"
                                                }
                                            ]
                                        },
                                        {
                                            id: "id11",
                                            name: "name11",
                                            type: "RADIO",
                                            options: [
                                                {
                                                    displayName: "single1",
                                                    value: "1"
                                                },
                                                {
                                                    displayName: "single2",
                                                    value: "2"
                                                }
                                            ],
                                            rules: [
                                                {
                                                    name: "requiredRule",
                                                    value: "true"
                                                }
                                            ]
                                        },
                                        {
                                            id: "id12",
                                            name: "name12",
                                            type: "MULTICHECK",
                                            options: [
                                                {
                                                    displayName: "single1",
                                                    value: "1"
                                                },
                                                {
                                                    displayName: "single2",
                                                    value: "2"
                                                },
                                                {
                                                    displayName: "single3",
                                                    value: "3"
                                                }
                                            ],
                                            rules: [
                                                {
                                                    name: "requiredRule",
                                                    value: "true"
                                                }
                                            ]
                                        },
                                        {
                                            id: "id90",
                                            name: "name90",
                                            type: "COMPLEX",
                                            rules: [
                                                {
                                                    name: "requiredRule",
                                                    value: "true"
                                                },
                                                {
                                                    name: "tipRule",
                                                    value: "this is a test 3"
                                                }
                                            ],
                                            fields: [
                                                {
                                                    id: "id1",
                                                    name: "name1",
                                                    type: "INPUT",
                                                    rules: [
                                                        {
                                                            name: "valueTypeRule",
                                                            value: "text"
                                                        },
                                                        {
                                                            name: 'tipRule',
                                                            value: "this is a test"
                                                        },
                                                        {
                                                            name: 'tipRule',
                                                            value: "this is a test2"
                                                        }
                                                    ]
                                                }
                                            ]
                                        }
                                    ],
                                    complexValue: {
                                        fieldMap: {
                                            id12: {
                                                id: "id12",
                                                name: "name12",
                                                type: "MULTICHECK",
                                                options: [
                                                    {
                                                        displayName: "single1",
                                                        value: "1"
                                                    },
                                                    {
                                                        displayName: "single2",
                                                        value: "2"
                                                    },
                                                    {
                                                        displayName: "single3",
                                                        value: "3"
                                                    }
                                                ],
                                                rules: [
                                                    {
                                                        name: "requiredRule",
                                                        value: "true"
                                                    }
                                                ],
                                                values: [{id: null, value: "2"}]
                                            }
                                        }
                                    }
                                },
                                id13: {
                                    id: "id13",
                                    name: "name13",
                                    type: "MULTICOMPLEX",
                                    rules: [
                                        {
                                            name: "requiredRule",
                                            value: "true"
                                        },
                                        {
                                            name: "tipRule",
                                            value: "this is a test"
                                        },
                                        {
                                            name: "tipRule",
                                            value: "this is a test 2"
                                        }
                                    ],
                                    fields: [
                                        {
                                            id: "id1",
                                            name: "name1",
                                            type: "INPUT",
                                            rules: [
                                                {
                                                    name: "valueTypeRule",
                                                    value: "text"
                                                },
                                                {
                                                    name: "tipRule",
                                                    value: "this is a test"
                                                }
                                            ]
                                        },
                                        {
                                            id: "id5",
                                            name: "name5",
                                            type: "INPUT",
                                            rules: [
                                                {
                                                    name: "valueTypeRule",
                                                    value: "date"
                                                }
                                            ]
                                        },
                                        {
                                            id: "id8",
                                            name: "name8",
                                            type: "INPUT",
                                            rules: [
                                                {
                                                    name: "valueTypeRule",
                                                    value: "textarea"
                                                }
                                            ]
                                        },
                                        {
                                            id: "id10",
                                            name: "name10",
                                            type: "SINGLECHECK",
                                            options: [
                                                {
                                                    displayName: "single1",
                                                    value: "1"
                                                },
                                                {
                                                    displayName: "single2",
                                                    value: "2"
                                                }
                                            ],
                                            rules: [
                                                {
                                                    name: "requiredRule",
                                                    value: "true"
                                                }
                                            ]
                                        },
                                        {
                                            id: "id110",
                                            name: "name11",
                                            type: "RADIO",
                                            options: [
                                                {
                                                    displayName: "single1",
                                                    value: "1"
                                                },
                                                {
                                                    displayName: "single2",
                                                    value: "2"
                                                }
                                            ],
                                            rules: [
                                                {
                                                    name: "requiredRule",
                                                    value: "true"
                                                }
                                            ]
                                        },
                                        {
                                            id: "id12",
                                            name: "name12",
                                            type: "MULTICHECK",
                                            options: [
                                                {
                                                    displayName: "single1",
                                                    value: "1"
                                                },
                                                {
                                                    displayName: "single2",
                                                    value: "2"
                                                },
                                                {
                                                    displayName: "single3",
                                                    value: "3"
                                                }
                                            ],
                                            rules: [
                                                {
                                                    name: "requiredRule",
                                                    value: "true"
                                                }
                                            ]
                                        }
                                    ],
                                    values: [
                                        {
                                            fieldMap: {
                                                id1: {
                                                    id: "id1",
                                                    name: "name1",
                                                    type: "INPUT",
                                                    rules: [
                                                        {
                                                            name: "valueTypeRule",
                                                            value: "text"
                                                        },
                                                        {
                                                            name: "requiredRule",
                                                            value: "true"
                                                        },
                                                        {
                                                            name: "tipRule",
                                                            value: "this is a test"
                                                        }
                                                    ],
                                                    value: "input1"
                                                },
                                                id5: {
                                                    id: "id5",
                                                    name: "name5",
                                                    type: "INPUT",
                                                    rules: [
                                                        {
                                                            name: "valueTypeRule",
                                                            value: "date"
                                                        }
                                                    ],
                                                    value: "2015-12-22"
                                                },
                                                id8: {
                                                    id: "id8",
                                                    name: "name8",
                                                    type: "INPUT",
                                                    rules: [
                                                        {
                                                            name: "valueTypeRule",
                                                            value: "textarea"
                                                        }
                                                    ],
                                                    value: "textarea1"
                                                },
                                                id10: {
                                                    id: "id10",
                                                    name: "name10",
                                                    type: "SINGLECHECK",
                                                    options: [
                                                        {
                                                            displayName: "single1",
                                                            value: "1"
                                                        },
                                                        {
                                                            displayName: "single2",
                                                            value: "2"
                                                        }
                                                    ],
                                                    rules: [
                                                        {
                                                            name: "requiredRule",
                                                            value: "true"
                                                        }
                                                    ],
                                                    value: {
                                                        id: null,
                                                        value: "2"
                                                    }
                                                },
                                                id110: {
                                                    id: "id110",
                                                    name: "name110",
                                                    type: "RADIO",
                                                    options: [
                                                        {
                                                            displayName: "single1",
                                                            value: "1"
                                                        },
                                                        {
                                                            displayName: "single2",
                                                            value: "2"
                                                        }
                                                    ],
                                                    rules: [
                                                        {
                                                            name: "requiredRule",
                                                            value: "true"
                                                        }
                                                    ],
                                                    value: {
                                                        id: null,
                                                        value: "1"
                                                    }
                                                },
                                                id12: {
                                                    id: "id12",
                                                    name: "name12",
                                                    type: "MULTICHECK",
                                                    options: [
                                                        {
                                                            displayName: "single1",
                                                            value: "1"
                                                        },
                                                        {
                                                            displayName: "single2",
                                                            value: "2"
                                                        },
                                                        {
                                                            displayName: "single3",
                                                            value: "3"
                                                        }
                                                    ],
                                                    rules: [
                                                        {
                                                            name: "requiredRule",
                                                            value: "true"
                                                        }
                                                    ],
                                                    values: [
                                                        {
                                                            id: null,
                                                            value: "2"
                                                        },
                                                        {
                                                            id: null,
                                                            value: "1"
                                                        }
                                                    ]
                                                }
                                            }
                                        },
                                        {
                                            fieldMap: {
                                                id1: {
                                                    id: "id1",
                                                    name: "name1",
                                                    type: "INPUT",
                                                    rules: [
                                                        {
                                                            name: "valueTypeRule",
                                                            value: "text"
                                                        },
                                                        {
                                                            name: "requiredRule",
                                                            value: "true"
                                                        },
                                                        {
                                                            name: "tipRule",
                                                            value: "this is a test"
                                                        }
                                                    ],
                                                    value: "input1"
                                                },
                                                id8: {
                                                    id: "id8",
                                                    name: "name8",
                                                    type: "INPUT",
                                                    rules: [
                                                        {
                                                            name: "valueTypeRule",
                                                            value: "textarea"
                                                        }
                                                    ],
                                                    value: "textarea1"
                                                },
                                                id10: {
                                                    id: "id10",
                                                    name: "name10",
                                                    type: "SINGLECHECK",
                                                    options: [
                                                        {
                                                            displayName: "single1",
                                                            value: "1"
                                                        },
                                                        {
                                                            displayName: "single2",
                                                            value: "2"
                                                        }
                                                    ],
                                                    rules: [
                                                        {
                                                            name: "requiredRule",
                                                            value: "true"
                                                        }
                                                    ],
                                                    value: {
                                                        id: null,
                                                        value: "2"
                                                    }
                                                },
                                                id110: {
                                                    id: "id110",
                                                    name: "name110",
                                                    type: "RADIO",
                                                    options: [
                                                        {
                                                            displayName: "single1",
                                                            value: "1"
                                                        },
                                                        {
                                                            displayName: "single2",
                                                            value: "2"
                                                        }
                                                    ],
                                                    rules: [
                                                        {
                                                            name: "requiredRule",
                                                            value: "true"
                                                        }
                                                    ],
                                                    value: {
                                                        id: null,
                                                        value: "2"
                                                    }
                                                },
                                                id12: {
                                                    id: "id12",
                                                    name: "name12",
                                                    type: "MULTICHECK",
                                                    options: [
                                                        {
                                                            displayName: "single1",
                                                            value: "1"
                                                        },
                                                        {
                                                            displayName: "single2",
                                                            value: "2"
                                                        },
                                                        {
                                                            displayName: "single3",
                                                            value: "3"
                                                        }
                                                    ],
                                                    rules: [
                                                        {
                                                            name: "requiredRule",
                                                            value: "true"
                                                        }
                                                    ],
                                                    values: [
                                                        {
                                                            id: null,
                                                            value: "2"
                                                        },
                                                        {
                                                            id: null,
                                                            value: "1"
                                                        }
                                                    ]
                                                }
                                            }
                                        }
                                    ]
                                }
                            }
                        }
                    }
                ],
                feedOrgAtts: [
                    {test1: "value1"},
                    {test2: "value2"},
                    {test3: "value3"},
                    {test4: "value4"},
                    {test5: "value5"}
                ],
                feedCnAtts: [
                    {test1: "值1"},
                    {test2: "值2"},
                    {test3: "值3"},
                    {test4: "值4"},
                    {test5: "值5"}
                ],
                feedAtts: [
                    {test1: "value1"},
                    {test2: "value2"},
                    {test3: "value3"},
                    {test4: "value4"},
                    {test5: "value5"},
                    {test11: "value11"},
                    {test21: "value21"},
                    {test31: "value31"},
                    {test41: "value41"},
                    {test51: "value51"}
                ]
            };*/
        }

        productDetailController.prototype = {

            // 获取初始化数据
            initialize: function () {
                var data = {id: this.routeParams.id};
                this.productDetailService.getProductInfo(data)
                    .then(function (res) {
                        this.productDetails = res.data;
                        this.productDetailsCopy = angular.copy(this.productDetails);
                    }.bind(this))
            },

            // 从第三方属性中添加feed属性到product自定义中,或者从product自定义中删除feed属性
            AddFeedAttrToCustom: function () {
                _.each(this.productDetails.feedAtts, function (feedAttr) {
                    if (feedAttr.selected
                        && !_.contains(this.productDetails.feedKeys, feedAttr.key)) {
                        // 设置被选中英文feed属性
                        this.productDetails.feedOrgAtts.push({key: feedAttr.key, value: feedAttr.value, selected: false});
                        // 设置被选中的中文feed属性
                        this.productDetails.feedCnAtts.push({key: feedAttr.key, value: null});
                        // 设置已经被选中的feed属性key
                        this.productDetails.feedKeys.push(feedAttr.key);
                        // 重新赋值productDetailsCopy
                        this.productDetailsCopy = angular.copy(this.productDetails);
                    }
                }.bind(this))
            },

            // 恢复第三方属性的默认选中
            cancelAddFeedAttrToCustom: function () {
                this.productDetails.feedAtts = angular.copy(this.productDetailsCopy.feedAtts);
            }

        };

        return productDetailController
    })());
});