package com.voyageone.service.bean.cms.task.beat;

import java.util.List;
import java.util.Map;

/**
 * 价格披露结果Bean
 *
 * @Author rex.wu
 * @Create 2017-06-22 18:41
 */
public class SearchTaskJiagepiluResult {

    private List<CmsBtTaskJiagepiluBean> products;
    private int total;

    // 各状态统计
    private List<Map<String, Object>> summary;

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

    public List<Map<String, Object>> getSummary() {
        return summary;
    }

    public void setSummary(List<Map<String, Object>> summary) {
        this.summary = summary;
    }
}
