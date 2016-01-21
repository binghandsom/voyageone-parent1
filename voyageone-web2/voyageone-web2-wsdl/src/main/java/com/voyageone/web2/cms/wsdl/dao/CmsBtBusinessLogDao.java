package com.voyageone.web2.cms.wsdl.dao;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.web2.sdk.api.domain.CmsBtBusinessLogModel;
import com.voyageone.web2.sdk.api.domain.CmsBtPromotionGroupModel;
import com.voyageone.web2.sdk.api.domain.CmsBtPromotionModel;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author aooer 2016/1/20.
 * @version 2.0.0
 * @since 2.0.0
 */
@Repository
public class CmsBtBusinessLogDao extends BaseDao {

    /**
     * 条件查询
     *
     * @param condtionParams params
     * @return list
     */
    public List<CmsBtBusinessLogModel> findByCondition(Map<?, ?> condtionParams) {
        return selectList("select_businesslog_By_Condtion", condtionParams);
    }

}
