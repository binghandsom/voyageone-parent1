package com.voyageone.service.impl.cms.sx.sku_field;


import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.configs.Enums.CartEnums;
import com.voyageone.common.masterdate.schema.field.Field;
import com.voyageone.service.dao.cms.CmsMtChannelSkuConfigDao;
import com.voyageone.service.dao.cms.CmsMtPlatformPropSkuDao;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.impl.cms.sx.sku_field.tmall.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by morse.lu 2016/05/06 (copy and modified from task2 / SkuFieldBuilderFactory)
 */
@Repository
public class SkuFieldBuilderService extends BaseService {

    @Autowired
    private CmsMtPlatformPropSkuDao cmsMtPlatformPropSkuDao;
    @Autowired
    private CmsMtChannelSkuConfigDao cmsMtChannelSkuConfigDao;
    private Map<String, List<Class>> skuFieldBuilderClazzsMap;

    public SkuFieldBuilderService() {
        skuFieldBuilderClazzsMap = new HashMap<>();
        List<Class> tmallGjSkuFieldBuilderClazzs = new ArrayList<>();
        tmallGjSkuFieldBuilderClazzs.add(TmallGjSkuFieldBuilderImpl1.class);
        tmallGjSkuFieldBuilderClazzs.add(TmallGjSkuFieldBuilderImpl2.class);
        tmallGjSkuFieldBuilderClazzs.add(TmallGjSkuFieldBuilderImpl3.class);
        tmallGjSkuFieldBuilderClazzs.add(TmallGjSkuFieldBuilderImpl4.class);
        tmallGjSkuFieldBuilderClazzs.add(TmallGjSkuFieldBuilderImpl5.class);
        tmallGjSkuFieldBuilderClazzs.add(TmallGjSkuFieldBuilderImpl6.class);
//        tmallGjSkuFieldBuilderClazzs.add(TmallGjSkuFieldBuilderImpl7.class);
        skuFieldBuilderClazzsMap.put(CartEnums.Cart.TG.getId(), tmallGjSkuFieldBuilderClazzs);
        skuFieldBuilderClazzsMap.put(CartEnums.Cart.TM.getId(), tmallGjSkuFieldBuilderClazzs);
    }

    public AbstractSkuFieldBuilder getSkuFieldBuilder(int cartId, List<Field> fields) throws Exception {
        AbstractSkuFieldBuilder skuFieldBuilder;
        List<Class> skuFieldBuilderClazzs = skuFieldBuilderClazzsMap.get(String.valueOf(cartId));

        if (skuFieldBuilderClazzs == null)
        {
            throw new BusinessException("No sku builder for cartId:" + cartId);
        }
        for (Class skuFieldBuilderClazz :  skuFieldBuilderClazzs) {
            try {
                skuFieldBuilder = (AbstractSkuFieldBuilder) skuFieldBuilderClazz.newInstance();
                skuFieldBuilder.setDao(cmsMtPlatformPropSkuDao, cmsMtChannelSkuConfigDao);
                if (skuFieldBuilder.isYourFood(fields, cartId)) {
                    $info("Choose skuBuilder " + skuFieldBuilderClazz.getSimpleName());
                    return skuFieldBuilder;
                }
            } catch (InstantiationException | IllegalAccessException e) {
                $error(e.getMessage(), e);
            }
        }

        $error("No sku build found!");
        return null;
    }
}
