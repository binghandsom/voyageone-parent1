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
public class GetStoreStockDetailData2 {

    //头信息
    private Header header;
    //库存信息
    private List<Temp> stocks;

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public List<Temp> getStocks() {
        return stocks;
    }

    public void setStocks(List<Temp> stocks) {
        this.stocks = stocks;
    }

    public static class Temp {

        private Map<String, List<Integer>> supplier;

        private Map<String, List<Integer>> store;

        private Base base;

        public Map<String, List<Integer>> getSupplier() {
            return supplier;
        }

        public void setSupplier(Map<String, List<Integer>> supplier) {
            this.supplier = supplier;
        }

        public Map<String, List<Integer>> getStore() {
            return store;
        }

        public void setStore(Map<String, List<Integer>> store) {
            this.store = store;
        }

        public Base getBase() {
            return base;
        }

        public void setBase(Base base) {
            this.base = base;
        }

        public static class Base {
            private List<Integer> total;
            private String origSize;
            private String sku;
            private String saleSize;
            private String barCode;

            public String getBarCode() {
                return barCode;
            }

            public void setBarCode(String barCode) {
                this.barCode = barCode;
            }

            public List<Integer> getTotal() {
                return total;
            }

            public void setTotal(List<Integer> total) {
                this.total = total;
            }

            public String getOrigSize() {
                return origSize;
            }

            public void setOrigSize(String origSize) {
                this.origSize = origSize;
            }

            public String getSku() {
                return sku;
            }

            public void setSku(String sku) {
                this.sku = sku;
            }

            public String getSaleSize() {
                return saleSize;
            }

            public void setSaleSize(String saleSize) {
                this.saleSize = saleSize;
            }
        }
    }

    public static class Header {
        private List<String> supplier;
        private List<String> store;
        private List<String> base;

        public List<String> getSupplier() {
            return supplier;
        }

        public List<String> getStore() {
            return store;
        }

        public List<String> getBase() {
            return base;
        }

        public void setSupplier(List<String> supplier) {
            this.supplier = supplier;
        }

        public void setStore(List<String> store) {
            this.store = store;
        }

        public void setBase(List<String> base) {
            this.base = base;
        }
    }
}
