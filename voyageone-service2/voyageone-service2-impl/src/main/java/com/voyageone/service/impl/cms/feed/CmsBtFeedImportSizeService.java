package com.voyageone.service.impl.cms.feed;
import com.sun.xml.internal.xsom.impl.scd.Iterators;
import com.voyageone.service.dao.cms.CmsBtFeedImportSizeDao;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.cms.CmsBtFeedImportSizeModel;
import com.voyageone.service.model.cms.mongo.channel.CmsBtSizeChartModel;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by dell on 2016/8/23.
 */
@Service
public class CmsBtFeedImportSizeService extends BaseService {
    @Autowired
    CmsBtFeedImportSizeDao dao;

    public int insert(CmsBtFeedImportSizeModel model) {
        return dao.insert(model);
    }

    public int update(CmsBtFeedImportSizeModel model) {
        return dao.update(model);
    }

    public int saveCmsBtFeedImportSizeModel(CmsBtFeedImportSizeModel model, String modifier) {
        CmsBtFeedImportSizeModel oldModel = get(model.getChannelId(), model.getBrandName(), model.getProductType(), model.getSizeType(), model.getOriginalSize());
        int result = 0;
        if (oldModel == null) {
            result = insert(model);
            model.setCreated(new Date());
            model.setCreater(modifier);
            oldModel.setModifier(modifier);
            oldModel.setModified(new Date());
        } else {
            //更新
            result = update(oldModel);
            oldModel.setModifier(modifier);
            oldModel.setModified(new Date());
        }
        return result;
    }

    public CmsBtFeedImportSizeModel get(String channelId, String brandName, String productType, String sizeType, String OriginalSize) {
        Map<String, Object> map = new HashedMap();
        map.put("channelId", channelId);
        map.put("brandName", brandName);
        map.put("productType", productType);
        map.put("sizeType", sizeType);
        map.put("originalSize", OriginalSize);
        return dao.selectOne(map);
    }

    public List<CmsBtFeedImportSizeModel> getList(String channelId, String brandName, String productType, String sizeType) {
        Map<String, Object> map = new HashedMap();
        map.put("channelId", channelId);
        if (brandName != null) {
            map.put("brandName", brandName);
        }
        if (productType != null) {
            map.put("productType", productType);
        }
        if (sizeType != null) {
            map.put("sizeType", sizeType);
        }
        return dao.selectList(map);
    }

    public List<CmsBtFeedImportSizeModel> getList(String channelId, List<String> listBrandName, List<String> listProductType, List<String> listSizeType) {
        List<CmsBtFeedImportSizeModel> listCmsBtFeedImportSizeModel = new ArrayList<>();
        for (String brandName : listBrandName) {
            if ("All".equalsIgnoreCase(brandName)) {
                brandName = null;
            }
            for (String productType : listProductType) {
                if ("All".equalsIgnoreCase(productType)) {
                    productType = null;
                }
                for (String sizeType : listSizeType) {
                    if ("All".equalsIgnoreCase(sizeType)) {
                        sizeType = null;
                    }
                    List<CmsBtFeedImportSizeModel> list = getList(channelId, brandName, productType, sizeType);
                    if (list != null) {
                        listCmsBtFeedImportSizeModel.addAll(list);
                    }
                }
            }
        }
        return listCmsBtFeedImportSizeModel;
    }
}
