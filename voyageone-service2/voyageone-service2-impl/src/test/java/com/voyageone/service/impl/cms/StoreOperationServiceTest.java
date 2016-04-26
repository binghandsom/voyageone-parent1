package com.voyageone.service.impl.cms;

import com.voyageone.BaseTest;
import com.voyageone.service.model.cms.CmsBtStoreOperationHistoryModel;
import org.apache.commons.lang.math.RandomUtils;
import org.junit.Test;

import javax.annotation.Resource;

import java.util.List;

import static org.junit.Assert.*;
import static org.junit.Assume.*;

/**
 * @description
 * @author: holysky.zhao
 * @date: 2016/4/26 19:15
 * COPYRIGHT © 2001 - 2016 VOYAGE ONE GROUP INC. ALL RIGHTS RESERVED.
 */
public class StoreOperationServiceTest extends BaseTest{

    @Resource
    StoreOperationService storeOperationService;

    @Test
    public void testCountProductsThatCanUploaded() throws Exception {

    }

    @Test
    public void testRePublish() throws Exception {

    }

    @Test
    public void testReUpload() throws Exception {

    }

    @Test
    public void testGetHistoryBy() throws Exception {
        CmsBtStoreOperationHistoryModel model = new CmsBtStoreOperationHistoryModel();
        model.setCreater("will"+System.currentTimeMillis());
        model.setOperationType(1+RandomUtils.nextInt(2));

        List<CmsBtStoreOperationHistoryModel> result = storeOperationService.getHistoryBy(null);
        assertTrue(result.size()>0);
    }
}