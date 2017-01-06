package com.voyageone.service.daoext.cms;

import com.voyageone.service.model.cms.CmsZzFeedWmfPriceModel;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by gjl on 2017/1/4.
 */
@Repository
public interface CmsZzFeedWmfPriceDaoExt {

    List<CmsZzFeedWmfPriceModel> selectList(Map<String, Object> map);

    CmsZzFeedWmfPriceModel selectOne(Map<String, Object> map);

    int selectCount(Map<String, Object> map);

    CmsZzFeedWmfPriceModel select(Integer id);

    int insert(CmsZzFeedWmfPriceModel record);

    int update(CmsZzFeedWmfPriceModel record);

    int delete(Integer id);

    int deleteBySku(String sku);
}
