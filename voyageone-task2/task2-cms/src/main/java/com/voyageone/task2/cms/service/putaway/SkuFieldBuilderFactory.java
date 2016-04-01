package com.voyageone.task2.cms.service.putaway;

import com.taobao.top.schema.field.Field;
import com.voyageone.task2.cms.dao.CustomSizePropDao;
import com.voyageone.task2.cms.dao.PlatformSkuInfoDao;
import com.voyageone.task2.cms.service.putaway.tmall.TmallGjSkuFieldBuilderImpl_0;
import com.voyageone.task2.cms.service.putaway.tmall.TmallGjSkuFieldBuilderImpl_1;
import com.voyageone.task2.cms.service.putaway.tmall.TmallGjSkuFieldBuilderImpl_2;
import com.voyageone.task2.cms.service.putaway.tmall.TmallGjSkuFieldBuilderImpl_3;
import com.voyageone.task2.cms.bean.tcb.AbortTaskSignalInfo;
import com.voyageone.task2.cms.bean.tcb.TaskSignal;
import com.voyageone.task2.cms.bean.tcb.TaskSignalType;
import com.voyageone.common.configs.Enums.CartEnums;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Leo on 15-7-14.
 */
@Repository
public class SkuFieldBuilderFactory {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private PlatformSkuInfoDao platformSkuInfoDao;
    @Autowired
    private CustomSizePropDao customSizePropDao;
    private Map<String, List<Class>> skuFieldBuilderClazzsMap;

    public SkuFieldBuilderFactory() {
        skuFieldBuilderClazzsMap = new HashMap<>();
        List<Class> tmallGjSkuFieldBuilderClazzs = new ArrayList<>();
        tmallGjSkuFieldBuilderClazzs.add(TmallGjSkuFieldBuilderImpl_0.class);
        tmallGjSkuFieldBuilderClazzs.add(TmallGjSkuFieldBuilderImpl_1.class);
        tmallGjSkuFieldBuilderClazzs.add(TmallGjSkuFieldBuilderImpl_2.class);
        tmallGjSkuFieldBuilderClazzs.add(TmallGjSkuFieldBuilderImpl_3.class);
        skuFieldBuilderClazzsMap.put(CartEnums.Cart.TG.getId(), tmallGjSkuFieldBuilderClazzs);
    }

    public AbstractSkuFieldBuilder getSkuFieldBuilder(int cartId, List<Field> fields) throws TaskSignal {
        AbstractSkuFieldBuilder skuFieldBuilder;
        List<Class> skuFieldBuilderClazzs = skuFieldBuilderClazzsMap.get(String.valueOf(cartId));

        if (skuFieldBuilderClazzs == null)
        {
            throw new TaskSignal(TaskSignalType.ABORT, new AbortTaskSignalInfo("No sku builder for cartId:" + cartId));
        }
        for (Class skuFieldBuilderClazz :  skuFieldBuilderClazzs) {
            try {
                skuFieldBuilder = (AbstractSkuFieldBuilder) skuFieldBuilderClazz.newInstance();
                skuFieldBuilder.setDao(platformSkuInfoDao, customSizePropDao);
                if (skuFieldBuilder.isYourFood(fields, cartId)) {
                    logger.info("Choose skuBuilder " + skuFieldBuilderClazz.getSimpleName());
                    return skuFieldBuilder;
                }
            } catch (InstantiationException | IllegalAccessException e) {
                logger.error(e.getMessage(), e);
            }
        }

        logger.error("No sku build found!");
        return null;
    }
}
