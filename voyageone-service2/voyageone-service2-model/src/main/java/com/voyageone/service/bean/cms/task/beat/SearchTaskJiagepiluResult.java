package com.voyageone.service.bean.cms.task.beat;

import java.util.List;

/**
 * 价格披露结果Bean
 *
 * @Author rex.wu
 * @Create 2017-06-22 18:41
 */
public class SearchTaskJiagepiluResult {

    public List<CmsBtTaskJiagepiluBean> products;
    public int total;

    public List<CmsBtTaskJiagepiluBean> getProducts() {
        return products;
    }

    public void setProducts(List<CmsBtTaskJiagepiluBean> products) {
        this.products = products;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
