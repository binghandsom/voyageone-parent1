package com.voyageone.common.components.gilt;

import com.voyageone.common.components.gilt.base.GiltBase;
import com.voyageone.common.components.gilt.bean.GiltRealTimeInventory;
import com.voyageone.common.components.gilt.bean.GiltSizeChart;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.common.util.StringUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;

/**
 * @author aooer 2016/2/2.
 * @version 2.0.0
 * @since 2.0.0
 */
@Service
public class GiltSizeChartService extends GiltBase {

    private static final String URL = "size-charts";

    /**
     *  根据id 获取尺码
     * @param shopBean shopBean
     * @param id id
     * @return GiltSizeChart
     * @throws Exception
     */
    public GiltSizeChart getSizeChartById(ShopBean shopBean, String id) throws Exception {
        if(StringUtils.isNullOrBlank2(id))
            throw new IllegalArgumentException("id不能为空");
        String result=reqGiltApi(shopBean,URL+"/"+id,new HashMap<String,String>());
        return JacksonUtil.json2Bean(result,GiltSizeChart.class);
    }

}
