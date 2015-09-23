/**
 * @User: Jonas
 * @Date: 2015/3/22
 * @Version: 0.0.4
 */

define(function () {

    return angular.module("imsModule", [
        "ngGrid",
        "mainModule"
    ])
        .constant("imsAction", {
            'beatIcon': {
                root: '/ims/beatIcon/index/',
                getCarts: "getCarts",
                create: "create",
                saveItems: "saveItems",
                dtGetBeats: "dtGetBeats",
                dtGetItems: "dtGetItems",
                downItems: "downloadItems",
                downErr: "downloadErr",
                addItems: "addItems",
                control: "control",
                setItemPrice: "setItemPrice"
            }
        });

});