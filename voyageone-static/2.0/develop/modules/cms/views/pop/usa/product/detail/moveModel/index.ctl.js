/**
 * @description Product move model
 * @author rex.wu
 */
define([
    'cms'
], function (cms) {

    cms.controller('ProductModelMoveController', class ProductModelMoveController {

        constructor(confirm,$modalInstance, context,alert,$usProductDetailService,notify) {
            let self = this;
            this.alert = alert;
            this.confirm = confirm;
            this.$modalInstance = $modalInstance;
            this.context = context;
            this.$usProductDetailService = $usProductDetailService;
            this.notify = notify;

            this.prodId = context.prodId;
            this.code = context.code;
            this.model = context.model;
            this.newModel = "";
        }

        moveModel() {
            let self = this;
            let parameter = {
                prodId:self.prodId,
                model:self.newModel
            };
            self.$usProductDetailService.updateChangeModel(parameter).then(res => {
                if (res.data) {
                    self.$modalInstance.close({model:self.newModel,data:res.data});
                    self.notify.success("Move successed.");
                } else {
                    self.alert("Move failed.")
                }
            });

        }

    })

});