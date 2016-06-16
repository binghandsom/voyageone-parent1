package com.voyageone.service.daoext.cms;

import com.voyageone.service.model.cms.CmsBtSxWorkloadModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Ethan Shi on 2016/6/16.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test-context.xml")
public class CmsBtSxWorkloadDaoExtTest {

    @Autowired
    CmsBtSxWorkloadDaoExt cmsBtSxWorkloadDaoExt;
    @Test
    public void testUpdatePublishStatus() throws Exception {
//        CmsBtSxWorkloadModel model = new CmsBtSxWorkloadModel();
//        model.setId(50158);
//        model.setGroupId(494097L);
//        model.setCartId(27);
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        model.setModified(sdf.parse("2016-06-15 14:07:59"));
//        model.setPublishStatus(0);
//        model.setModifier("test");
//        model.setChannelId("018");
//        model.setCartId(27);
//        model.setGroupId(494097L);

        List<CmsBtSxWorkloadModel> list = cmsBtSxWorkloadDaoExt.selectSxWorkloadModelWithChannelIdCartId(1, "018", 27);

        CmsBtSxWorkloadModel model =  list.get(0);
        model.setPublishStatus(1);
        model.setModifier("ethan");

        cmsBtSxWorkloadDaoExt.updatePublishStatus(model);
    }
}