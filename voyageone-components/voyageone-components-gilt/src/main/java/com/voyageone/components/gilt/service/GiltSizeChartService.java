package com.voyageone.components.gilt.service;

import com.voyageone.common.util.JacksonUtil;
import com.voyageone.common.util.StringUtils;
import com.voyageone.components.gilt.GiltBase;
import com.voyageone.components.gilt.bean.GiltSizeChart;
import org.springframework.stereotype.Service;

import java.util.HashMap;

/**
 * @author aooer 2016/2/2.
 * @version 2.0.0
 * @since 2.0.0
 */
@Service
public class GiltSizeChartService extends GiltBase {

    private static final String URI = "size-charts";

    /**
     *  根据id 获取尺码

     * @param id id
     * @return GiltSizeChart
     * @throws Exception
     */
    public GiltSizeChart getSizeChartById(String id) throws Exception {
        if(StringUtils.isNullOrBlank2(id))
            throw new IllegalArgumentException("id不能为空");
        String result=reqGiltApi(URI +"/"+id,new HashMap<String,String>());
        return JacksonUtil.json2Bean(result,GiltSizeChart.class);
    }

}
