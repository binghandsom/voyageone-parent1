package com.voyageone.service.dao.cms;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.service.model.cms.CmsBtPromotionLogModel;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author gubuchun 15/12/18
 * @version 2.0.0
 */
@Repository
public class CmsBtPromotionLogDao extends BaseDao {
    public List<CmsBtPromotionLogModel> selectPromotionLog(Map<String, Object> param) {
        return updateTemplate.selectList("select_promotion_log", param);
    }

    public int selectPromotionLogCnt(Map<String, Object> param) {
        return updateTemplate.selectOne("select_promotion_log_cnt", param);
    }
}
