package com.voyageone.service.daoext.cms;

import com.voyageone.common.util.JacksonUtil;
import com.voyageone.service.bean.cms.CmsBtBeatInfoBean;
import com.voyageone.service.model.cms.enums.BeatFlag;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by Ethan Shi on 2016/5/11.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test-context.xml")

public class CmsBtBeatInfoDaoExtTest {

    @Autowired
    private CmsBtBeatInfoDaoExt cmsBtBeatInfoDaoExt;

    @Test
    public void testInsertList() throws Exception {
        List<CmsBtBeatInfoBean> list = new ArrayList<CmsBtBeatInfoBean>();
        CmsBtBeatInfoBean cmsBtBeatInfoBean = new CmsBtBeatInfoBean();

        cmsBtBeatInfoBean.setTask_id(1);
        cmsBtBeatInfoBean.setNum_iid(2);
        cmsBtBeatInfoBean.setProduct_code("1");
        cmsBtBeatInfoBean.setSyn_flag(1);
        cmsBtBeatInfoBean.setMessage("test");
        cmsBtBeatInfoBean.setCreated(new Date());
        cmsBtBeatInfoBean.setCreater("testuser");
        cmsBtBeatInfoBean.setModified(new Date());
        cmsBtBeatInfoBean.setModifier("testuser");
        list.add(cmsBtBeatInfoBean);
        cmsBtBeatInfoDaoExt.insertList(list);

    }

    @Test
    public void testUpdateDiffPromotionMessage() throws Exception {

    }

    @Test
    public void testSelectListByTask() throws Exception {
        List<CmsBtBeatInfoBean> list  = cmsBtBeatInfoDaoExt.selectListByTask(4);
        System.out.println(JacksonUtil.bean2Json(list));
    }

    @Test
    public void testSelectListByTask1() throws Exception {

        List<CmsBtBeatInfoBean> list  = cmsBtBeatInfoDaoExt.selectListByTask(68, BeatFlag.BEATING , 1, 1);
        System.out.println(JacksonUtil.bean2Json(list));
    }

    @Test
    public void testSelectListByTaskCount() throws Exception {
        int count  = cmsBtBeatInfoDaoExt.selectListByTaskCount(4, BeatFlag.BEATING);
        System.out.println(count);
    }

    @Test
    public void testDeleteByTask() throws Exception {
        cmsBtBeatInfoDaoExt.deleteByTask(1);

    }

    @Test
    public void testSelectCountInFlags() throws Exception {
        int count = cmsBtBeatInfoDaoExt.selectCountInFlags(4, BeatFlag.BEATING);
        System.out.println(count);

    }

    @Test
    public void testSelectOneById() throws Exception {
        CmsBtBeatInfoBean cmsBtBeatInfoBean  = cmsBtBeatInfoDaoExt.selectOneById(68);
        System.out.println(JacksonUtil.bean2Json(cmsBtBeatInfoBean));
    }

    @Test
    public void testUpdateFlag() throws Exception {
        CmsBtBeatInfoBean cmsBtBeatInfoBean = new CmsBtBeatInfoBean();
        cmsBtBeatInfoBean.setId(71);
        cmsBtBeatInfoBean.setTask_id(1);
        cmsBtBeatInfoBean.setNum_iid(2);
        cmsBtBeatInfoBean.setProduct_code("1");
        cmsBtBeatInfoBean.setSyn_flag(111);
        cmsBtBeatInfoBean.setMessage("test");
        cmsBtBeatInfoBean.setCreated(new Date());
        cmsBtBeatInfoBean.setCreater("testuser");
        cmsBtBeatInfoBean.setModified(new Date());
        cmsBtBeatInfoBean.setModifier("testuser");
        cmsBtBeatInfoDaoExt.updateFlag(cmsBtBeatInfoBean);

    }

    @Test
    public void testUpdateFlags() throws Exception {
        cmsBtBeatInfoDaoExt.updateFlags(1, BeatFlag.BEATING, "testuser");

    }

    @Test
    public void testSelectSummary() throws Exception {
        List<Map> list = cmsBtBeatInfoDaoExt.selectSummary(1);
        System.out.println(JacksonUtil.bean2Json(list));

    }

    @Test
    public void testSelectListByNumiidInOtherTask() throws Exception {
        List<CmsBtBeatInfoBean> list =  cmsBtBeatInfoDaoExt.selectListByNumiidInOtherTask(1, 1, "2");
        System.out.println(JacksonUtil.bean2Json(list));

    }

    @Test
    public void testSelectOneByNumiid() throws Exception {
        CmsBtBeatInfoBean cmsBtBeatInfoBean = cmsBtBeatInfoDaoExt.selectOneByNumiid(1, "2");
        System.out.println(JacksonUtil.bean2Json(cmsBtBeatInfoBean));
    }

    @Test
    public void testUpdateCode() throws Exception {
        CmsBtBeatInfoBean cmsBtBeatInfoBean = new CmsBtBeatInfoBean();
        cmsBtBeatInfoBean.setId(71);
        cmsBtBeatInfoBean.setTask_id(1);
        cmsBtBeatInfoBean.setNum_iid(2);
        cmsBtBeatInfoBean.setProduct_code("1");
        cmsBtBeatInfoBean.setSyn_flag(111);
        cmsBtBeatInfoBean.setMessage("test");
        cmsBtBeatInfoBean.setCreated(new Date());
        cmsBtBeatInfoBean.setCreater("testuser");
        cmsBtBeatInfoBean.setModified(new Date());
        cmsBtBeatInfoBean.setModifier("testuser");
        cmsBtBeatInfoDaoExt.updateCode(cmsBtBeatInfoBean);

    }

    @Test
    public void testSelectListNeedBeatFullData() throws Exception {

        List<CmsBtBeatInfoBean> list = cmsBtBeatInfoDaoExt.selectListNeedBeatFullData(1);
        System.out.println(JacksonUtil.bean2Json(list));

    }

    @Test
    public void testUpdateFlagAndMessage() throws Exception {
        CmsBtBeatInfoBean cmsBtBeatInfoBean = new CmsBtBeatInfoBean();
        cmsBtBeatInfoBean.setId(71);
        cmsBtBeatInfoBean.setTask_id(1);
        cmsBtBeatInfoBean.setNum_iid(2);
        cmsBtBeatInfoBean.setProduct_code("1");
        cmsBtBeatInfoBean.setSyn_flag(111);
        cmsBtBeatInfoBean.setMessage("test");
        cmsBtBeatInfoBean.setCreated(new Date());
        cmsBtBeatInfoBean.setCreater("testuser");
        cmsBtBeatInfoBean.setModified(new Date());
        cmsBtBeatInfoBean.setModifier("testuser");
        cmsBtBeatInfoDaoExt.updateFlagAndMessage(cmsBtBeatInfoBean);

    }
}