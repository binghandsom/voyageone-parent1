package com.voyageone.web2.sdk.api.response.wms;

import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 从WMS查询sku库存返回数据
 *
 * @Author rex.wu
 * @Create 2017-05-10 16:46
 */
public class GetStoreStockDetailData {

    private static final List<String> HEAD_BASIC = ImmutableList.of( "sku", "origSize", "saleSize", "total" );
    private GetStoreStockDetailData.HeaderBean header;
    private List<GetStoreStockDetailData.StocksBean> stocks = new ArrayList<>();

    public GetStoreStockDetailData.HeaderBean getHeader() { return header;}

    public void setHeader(GetStoreStockDetailData.HeaderBean header) { this.header = header;}

    public List<GetStoreStockDetailData.StocksBean> getStocks() { return stocks;}

    public void setStocks(List<GetStoreStockDetailData.StocksBean> stocks) { this.stocks = stocks;}


    public static class HeaderBean {

        private List<String> base;
        private List<String> inOwn;
        private List<String> inNOwn;
        private List<String> gbOwn;
        private List<String> gbNOwn;

        public HeaderBean() {
        }

        public HeaderBean(final List<String> base, final List<String> inOwn, final List<String> inNOwn, final List<String> gbOwn, final List<String> gbNOwn) {
            this.base = base;
            this.inOwn = inOwn;
            this.inNOwn = inNOwn;
            this.gbOwn = gbOwn;
            this.gbNOwn = gbNOwn;
        }

        public List<String> getBase() {
            return base;
        }

        public void setBase(List<String> base) {
            this.base = base;
        }

        public List<String> getInOwn() {
            return inOwn;
        }

        public void setInOwn(List<String> inOwn) {
            this.inOwn = inOwn;
        }

        public List<String> getInNOwn() {
            return inNOwn;
        }

        public void setInNOwn(List<String> inNOwn) {
            this.inNOwn = inNOwn;
        }

        public List<String> getGbOwn() {
            return gbOwn;
        }

        public void setGbOwn(List<String> gbOwn) {
            this.gbOwn = gbOwn;
        }

        public List<String> getGbNOwn() {
            return gbNOwn;
        }

        public void setGbNOwn(List<String> gbNOwn) {
            this.gbNOwn = gbNOwn;
        }
    }


    public static class StocksBean {

        private BaseBean base;

        //gbOwn : {"total":3000,"LA":1000} 形式为total:下面总仓库销量
        private Map<String, List<Integer>> inOwn; //国内自营仓： 区域是CN、HK，库存是我们管理 并且 是可销售仓库 并且不是聚美、菜鸟保税仓、边境仓
        private Map<String, List<Integer>> inNOwn;//国内第三方仓： 区域是CN、HK， 并且 是可销售仓库 并且 （库存不是我们管理 或者 是聚美、菜鸟保税仓、边境仓 ）
        private Map<String, List<Integer>> gbOwn; //国外自营仓： 区域不是CN、HK，库存是我们管理 并且 是可销售仓库
        private Map<String, List<Integer>> gbNOwn; //国外自营仓： 区域不是CN、HK，库存不是我们管理 并且 是可销售仓库

        public BaseBean getBase() {
            return base;
        }

        public void setBase(BaseBean base) {
            this.base = base;
        }

        public Map<String, List<Integer>> getInOwn() {
            return inOwn;
        }

        public void setInOwn(Map<String, List<Integer>> inOwn) {
            this.inOwn = inOwn;
        }

        public Map<String, List<Integer>> getInNOwn() {
            return inNOwn;
        }

        public void setInNOwn(Map<String, List<Integer>> inNOwn) {
            this.inNOwn = inNOwn;
        }

        public Map<String, List<Integer>> getGbOwn() {
            return gbOwn;
        }

        public void setGbOwn(Map<String, List<Integer>> gbOwn) {
            this.gbOwn = gbOwn;
        }

        public Map<String, List<Integer>> getGbNOwn() {
            return gbNOwn;
        }

        public void setGbNOwn(Map<String, List<Integer>> gbNOwn) {
            this.gbNOwn = gbNOwn;
        }

        /*"base": {
                    "total": [8,0],
                    "origSize": "5.5",
                    "sku": "001001B07-BLK-5.5",
                    "saleSize": ""
                }*/
        public static class BaseBean {

            private List<Integer> total;
            private String sku;
            private String origSize;
            private String saleSize;

            public List<Integer> getTotal() {
                return total;
            }

            public void setTotal(List<Integer> total) {
                this.total = total;
            }

            public String getSku() {
                return sku;
            }

            public void setSku(String sku) {
                this.sku = sku;
            }

            public String getOrigSize() {
                return origSize;
            }

            public void setOrigSize(String origSize) {
                this.origSize = origSize;
            }

            public String getSaleSize() {
                return saleSize;
            }

            public void setSaleSize(String saleSize) {
                this.saleSize = saleSize;
            }
        }
    }
}
