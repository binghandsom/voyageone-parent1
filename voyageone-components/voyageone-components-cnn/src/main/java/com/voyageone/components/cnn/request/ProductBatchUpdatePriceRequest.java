package com.voyageone.components.cnn.request;

import com.voyageone.components.cnn.enums.CnnConstants;
import com.voyageone.components.cnn.response.ProductBatchUpdatePriceResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * 批量更新商品价格，每次请求最多处理100个商品
 * Created by morse on 2017/7/31.
 */
public class ProductBatchUpdatePriceRequest extends AbstractCnnRequest<ProductBatchUpdatePriceResponse> {

    public ProductBatchUpdatePriceRequest() {
        productList = new ArrayList<>();
    }

    @Override
    public String getUrl() {
        return CnnConstants.CnnApiAction.PRODUCT_BATCH_UPDATE_PRICE;
    }

    private List<ProductInfo> productList;

    public ProductInfo createAddProductInfo() {
        ProductInfo productInfo = new ProductInfo();
        productList.add(productInfo);
        return productInfo;
    }

    public List<ProductInfo> getProductList() {
        return productList;
    }

    public void setProductList(List<ProductInfo> productList) {
        this.productList = productList;
    }

    public class ProductInfo {
        private ProductInfo() {
            skuList = new ArrayList<>();
        }

        private String numIId;
        private List<SkuInfo> skuList;

        public SkuInfo createAddSkuInfo() {
            SkuInfo skuInfo = new SkuInfo();
            skuList.add(skuInfo);
            return skuInfo;
        }

        public String getNumIId() {
            return numIId;
        }

        public void setNumIId(String numIId) {
            this.numIId = numIId;
        }

        public List<SkuInfo> getSkuList() {
            return skuList;
        }

        public void setSkuList(List<SkuInfo> skuList) {
            this.skuList = skuList;
        }
    }

    public class SkuInfo {
        private SkuInfo() {

        }

        private String skuCode;
        private Double msrpPrice; // 市场价
        private Double salePrice; // 最终销售价
        private Double tax; // 税额

        public String getSkuCode() {
            return skuCode;
        }

        public void setSkuCode(String skuCode) {
            this.skuCode = skuCode;
        }

        public Double getMsrpPrice() {
            return msrpPrice;
        }

        public void setMsrpPrice(Double msrpPrice) {
            this.msrpPrice = msrpPrice;
        }

        public Double getSalePrice() {
            return salePrice;
        }

        public void setSalePrice(Double salePrice) {
            this.salePrice = salePrice;
        }

        public Double getTax() {
            return tax;
        }

        public void setTax(Double tax) {
            this.tax = tax;
        }
    }

}
