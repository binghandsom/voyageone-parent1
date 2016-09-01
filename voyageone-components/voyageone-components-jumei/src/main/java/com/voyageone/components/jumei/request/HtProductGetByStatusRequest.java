package com.voyageone.components.jumei.request;

import com.voyageone.components.jumei.bean.JmProductBean;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * HtProductGetByStatusRequest(国际POP - 根据状态批量获取商城商品[MALL])
 *
 * @author jiangjusheng, 2016/08/31
 * @version 2.0.0
 * @since 2.0.0
 */
public class HtProductGetByStatusRequest implements BaseJMRequest {
    // 访问URL
    private final static String url = "/v1/htProduct/getMallProductsByStatus";

    // mall状态
    private String status;
    // 当前页码
    private Integer page;
    // 每一页显示的数量
    private Integer pageSize;

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public Map<String, Object> getParameter() throws IOException {
        Map<String, Object> params = new HashMap<>();
        params.put("status", status);
        if (page != null) {
            params.put("page", page);
        }
        if (pageSize != null) {
            params.put("pageSize", pageSize);
        }
        return params;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
