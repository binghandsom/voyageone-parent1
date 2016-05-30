package com.voyageone.service.impl.cms;

import com.google.common.base.Preconditions;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.service.bean.cms.CmsBtStoreOperationHistoryBean;
import com.voyageone.service.model.cms.CmsBtStoreOperationHistoryModel;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import javax.annotation.Resource;
import javax.validation.constraints.AssertTrue;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.Assert.*;
import static org.junit.Assume.*;

/**
 * @description
 * @author: holysky.zhao
 * @date: 2016/4/26 19:15
 * COPYRIGHT © 2001 - 2016 VOYAGE ONE GROUP INC. ALL RIGHTS RESERVED.
 */
public class StoreOperationServiceTest {

    @Resource
    StoreOperationService storeOperationService;
    private static final String channelId = "018";

    @Test
    public void testCountProductsThatCanUploaded() throws Exception {
        long count = storeOperationService.countProductsThatCanUploaded(channelId);
        assertTrue(count > 0);
    }

    @Test
    public void testRePublishPrice() throws Exception {
        storeOperationService.rePublishPrice(channelId, "will");

    }

    @Test
    public void testRePublish() throws Exception {
        storeOperationService.rePublish(channelId, "will");

    }

    @Test
    public void testReUpload() throws Exception {
        storeOperationService.reUpload("018", true, "will");
    }

    public static final ConcurrentHashMap<String, LocalDateTime> lastExecuteTimes = new ConcurrentHashMap<>();


    /**
     * 如果上次执行时间距离现在执行时间超过指定阈值,则抛异常
     */
    public void checkInInterval(String channelId) {
        Preconditions.checkArgument(StringUtils.isNotBlank(channelId), "channelId不能为空!");

        LocalDateTime lastExecuteTime = lastExecuteTimes.get(channelId);
        if (lastExecuteTime == null) {
            lastExecuteTimes.put(channelId, LocalDateTime.now()); //ok
            return;
        }

        int interval = 2;
        boolean inRange = lastExecuteTime.plusHours(interval).isAfter(LocalDateTime.now());
        if (inRange) {
            throw new BusinessException("操作时间间隔必须在" + interval + "小时以上!");
        } else {
            lastExecuteTimes.put(channelId, LocalDateTime.now()); //更新上次操作时间
        }
    }

    @Test(expected = BusinessException.class )
    public void testCheckInterval() throws Exception {
        checkInInterval("010");
        checkInInterval("010");
    }

    public void testGetHistoryBy() throws Exception {
        CmsBtStoreOperationHistoryModel model = new CmsBtStoreOperationHistoryModel();
        model.setCreater("will" + System.currentTimeMillis());
        model.setOperationType(1 + "1");

        List<CmsBtStoreOperationHistoryBean> result = storeOperationService.getHistoryBy(null);
        assertTrue(result.size() > 0);
    }
}
