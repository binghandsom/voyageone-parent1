package com.voyageone.service.daoext.cms;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.service.bean.cms.task.beat.CmsBtTaskJiagepiluBean;
import com.voyageone.service.bean.cms.task.beat.SearchTaskJiagepiluBean;
import com.voyageone.service.dao.ServiceBaseDao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 扩展Dao
 *
 * @Author rex.wu
 * @Create 2017-06-22 16:27
 */
@Repository
public interface CmsBtTaskJiagepiluDaoExt {

    List<CmsBtTaskJiagepiluBean> search(SearchTaskJiagepiluBean search);

    int count(SearchTaskJiagepiluBean search);

    List<Map<String, Object>> selectSummary(Integer taskId);

    int updateFlags(@Param("taskId") int taskId, @Param("synFlag") int flag, @Param("imageStatus") int imageStatus,
                    @Param("force") boolean force, @Param("modifier") String userName);
}
