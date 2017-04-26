package com.voyageone.web2.sdk.api.response.wms;

import com.google.common.collect.ImmutableList;
import com.voyageone.web2.sdk.api.VoApiResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by james on 2017/4/13.
 *
 */
public class GetStoreStockDetailResponse extends VoApiResponse {
    private static final List<String> HEAD_ORDER = ImmutableList.of("ch", "onHold", "newOrder");
    private static final List<String> HEAD_BASIC = ImmutableList.of( "sku", "origSize", "saleSize", "total" );
    private HeaderBean header;
    private List<StocksBean> stocks = new ArrayList<>();


    public HeaderBean getHeader() { return header;}

    public void setHeader(HeaderBean header) { this.header = header;}

    public List<StocksBean> getStocks() { return stocks;}

    public void setStocks(List<StocksBean> stocks) { this.stocks = stocks;}


    public static class HeaderBean {

        private List<String> inOwn;
        private List<String> inNOwn;
        private List<String> gbOwn;
        private List<String> gbNOwn;

        public HeaderBean(final List<String> inOwn, final List<String> inNOwn, final List<String> gbOwn, final List<String> gbNOwn) {
            this.inOwn = inOwn;
            this.inNOwn = inNOwn;
            this.gbOwn = gbOwn;
            this.gbNOwn = gbNOwn;
        }

        public List<String> getBase(){
            return HEAD_BASIC;
        }

        public List<String> getInOwn() {
            return inOwn;
        }

        public List<String> getInNOwn() {
            return inNOwn;
        }

        public List<String> getGbOwn() {
            return gbOwn;
        }

        public List<String> getGbNOwn() {
            return gbNOwn;
        }

        public List<String> getOrder(){
            return HEAD_ORDER;
        }
    }


    public static class StocksBean {

        private BaseBean base;  //表头

        //gbOwn : {"total":3000,"LA":1000} 形式为total:下面总仓库销量
        private Map<String, Integer> inOwn; //国内自营仓： 区域是CN、HK，库存是我们管理 并且 是可销售仓库 并且不是聚美、菜鸟保税仓、边境仓
        private Map<String, Integer> inNOwn;//国内第三方仓： 区域是CN、HK， 并且 是可销售仓库 并且 （库存不是我们管理 或者 是聚美、菜鸟保税仓、边境仓 ）
        private Map<String, Integer> gbOwn; //国外自营仓： 区域不是CN、HK，库存是我们管理 并且 是可销售仓库
        private Map<String, Integer> gbNOwn; //国外自营仓： 区域不是CN、HK，库存不是我们管理 并且 是可销售仓库
        private OrderBean order;  //订单数据


        public Map<String, Integer> getInOwn() {
            return inOwn;
        }

        public StocksBean setInOwn(final Map<String, Integer> inOwn) {
            this.inOwn = inOwn;
            return this;
        }

        public StocksBean setInNOwn(final Map<String, Integer> inNOwn) {
            this.inNOwn = inNOwn;
            return this;
        }

        public Map<String, Integer> getInNOwn() {
            return inNOwn;
        }

        public Map<String, Integer> getGbOwn() {
            return gbOwn;
        }

        public StocksBean setGbOwn(final Map<String, Integer> gbOwn) {
            this.gbOwn = gbOwn;
            return this;
        }

        public Map<String, Integer> getGbNOwn() {
            return gbNOwn;
        }

        public StocksBean setGbNOwn(final Map<String, Integer> gbNOwn) {
            this.gbNOwn = gbNOwn;
            return this;
        }

        public BaseBean getBase() { return base;}

        public void setBase(BaseBean base) { this.base = base;}

        public OrderBean getOrder() { return order;}

        public void setOrder(OrderBean order) { this.order = order;}


        public static class BaseBean {
            /**
             * sku : 001-123456-001
             * origSize : S
             * saleSize : S
             * total : 1000
             */
            private String sku;
            private String origSize;
            private String saleSize;
            private int total;

            public BaseBean(final String sku, final String origSize, final String saleSize, final int total) {
                this.sku = sku;
                this.origSize = origSize;
                this.saleSize = saleSize;
                this.total = total;
            }

            public String getSku() {
                return sku;
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

            public int getTotal() {
                return total;
            }
        }

        public static class OrderBean {
            private Long ch;  //CH仓库的Open物品
            private Long onHold; //wms_bt_new_order中active=1的物品
            private Long newOrder; //订单表中 pending、in processing 、Approved（tt_reservation表中没有记录）


            public OrderBean(final Long ch, final Long onHold, final Long newOrder) {
                this.ch = ch;
                this.onHold = onHold;
                this.newOrder = newOrder;
            }

            public Long getCh() {
                return ch;
            }

            public OrderBean setCh(final Long ch) {
                this.ch = ch;
                return this;
            }

            public Long getOnHold() {
                return onHold;
            }

            public OrderBean setOnHold(final Long onHold) {
                this.onHold = onHold;
                return this;
            }

            public Long getNewOrder() {
                return newOrder;
            }

            public OrderBean setNewOrder(final Long newOrder) {
                this.newOrder = newOrder;
                return this;
            }
        }
    }
}
