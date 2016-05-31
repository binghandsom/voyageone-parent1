package com.voyageone.service.daoext.cms;

import com.voyageone.service.bean.cms.CmsBtTasksBean;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by jonasvlag on 16/2/29.
 * 接口改造 by Jonas on 2016-05-24 15:02:29
 *
 * @version 2.1.0
 * @since 2.0.0
 */
@Repository
public interface CmsBtTasksDaoExt {

    /**
     * 更新数据, 只更新 Config 和 Name
     */
    int updateConfig(CmsBtTasksBean model);

    CmsBtTasksBean selectWithPromotion(@Param("task_id") int task_id);

    List<CmsBtTasksBean> selectWithPromotion(
            @Param("promotion_id") int promotion_id, @Param("task_name") String task_name,
            @Param("channelId") String channelId, @Param("task_type") Integer task_type);

    List<CmsBtTasksBean> selectWithPromotion(Map<String, Object> searchInfo);
}
