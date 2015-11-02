package com.voyageone.batch.ims.service;

import com.voyageone.batch.ims.bean.tcb.AbortTaskSignalInfo;
import com.voyageone.batch.ims.bean.tcb.TaskSignal;
import com.voyageone.batch.ims.bean.tcb.TaskSignalType;
import com.voyageone.batch.ims.dao.*;
import com.voyageone.batch.ims.modelbean.PlatformPropBean;
import com.voyageone.batch.ims.service.tmall.*;
import com.voyageone.common.configs.Enums.CartEnums;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
    @Autowired
    private PlatformPropDao platformPropDao;
    @Autowired
    private SkuPropValueDao skuPropValueDao;
    @Autowired
    private PlatformSkuInfoDao platformSkuInfoDao;
    @Autowired
    private SkuInfoDao skuInfoDao;
    @Autowired
    private CustomSizePropDao customSizePropDao;
    private Map<CartEnums.Cart, List<Class>> skuFieldBuilderClazzsMap;
    private static Log logger = LogFactory.getLog(SkuFieldBuilderFactory.class);

    public SkuFieldBuilderFactory() {
        skuFieldBuilderClazzsMap = new HashMap<>();
        List<Class> tmallGjSkuFieldBuilderClazzs = new ArrayList<>();
        tmallGjSkuFieldBuilderClazzs.add(TmallGjSkuFieldBuilderImpl_1.class);
        tmallGjSkuFieldBuilderClazzs.add(TmallGjSkuFieldBuilderImpl_2.class);
        skuFieldBuilderClazzsMap.put(CartEnums.Cart.TG, tmallGjSkuFieldBuilderClazzs);
    }

    public AbstractSkuFieldBuilder getSkuFieldBuilder(int cartId, List<PlatformPropBean> platformProps) throws TaskSignal {
        AbstractSkuFieldBuilder skuFieldBuilder;
        List<Class> skuFieldBuilderClazzs = skuFieldBuilderClazzsMap.get(CartEnums.Cart.getValueByID(String.valueOf(cartId)));

        if (skuFieldBuilderClazzs == null)
        {
            throw new TaskSignal(TaskSignalType.ABORT, new AbortTaskSignalInfo("No sku builder for cartId:" + cartId));
        }
        for (Class skuFieldBuilderClazz :  skuFieldBuilderClazzs) {
            try {
                skuFieldBuilder = (AbstractSkuFieldBuilder) skuFieldBuilderClazz.newInstance();
                skuFieldBuilder.setDao(platformPropDao, skuPropValueDao, platformSkuInfoDao, skuInfoDao, customSizePropDao);
                if (skuFieldBuilder.isYourFood(platformProps)) {
                    logger.info("Choose skuBuilder " + skuFieldBuilderClazz.getSimpleName());
                    return skuFieldBuilder;
                }
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        logger.error("No sku build found!");
        return null;
    }
}
