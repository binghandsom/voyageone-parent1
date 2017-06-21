package com.voyageone.service.daoext.cms;

import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.service.bean.cms.CmsBtBeatInfoBean;
import com.voyageone.service.model.cms.enums.jiagepilu.BeatFlag;
import com.voyageone.service.model.cms.enums.jiagepilu.ImageStatus;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.*;

/**
 * Created by Ethan Shi on 2016/5/11.
 * 转换原 class 形式到 interface 形式, by Jonas on 2016-05-24 11:12:58
 * 测试上下方法逻辑相关。务必确保按照方法放置的顺序执行
 *
 * @version 2.1.0
 * @since 2.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test-context.xml")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CmsBtBeatInfoDaoExtTest {

    @Autowired
    private CmsBtBeatInfoDaoExt beatInfoDaoExt;

    @Test
    public void test01InsertList() throws Exception {

        List<CmsBtBeatInfoBean> modelList = new ArrayList<>();

        // 完全错误
        CmsBtBeatInfoBean bean1 = new CmsBtBeatInfoBean();

        bean1.setTaskId(444);
        bean1.setNumIid(2L);
        bean1.setProductCode("1");
        bean1.setSynFlag(BeatFlag.BEATING);
        bean1.setMessage("test");
        bean1.setImageStatus(ImageStatus.None);
        bean1.setImageTaskId(1);
        bean1.setCreated(new Date());
        bean1.setCreater("testuser");
        bean1.setModified(new Date());
        bean1.setModifier("testuser");
        modelList.add(bean1);

        // Code 错误
        CmsBtBeatInfoBean bean2 = new CmsBtBeatInfoBean();

        bean2.setTaskId(444);
        bean2.setNumIid(525897623218L);
        bean2.setProductCode("51A0HC13E1");
        bean2.setSynFlag(BeatFlag.BEATING);
        bean2.setMessage("test");
        bean2.setImageStatus(ImageStatus.None);
        bean2.setImageTaskId(1);
        bean2.setCreated(new Date());
        bean2.setCreater("testuser");
        bean2.setModified(new Date());
        bean2.setModifier("testuser");
        modelList.add(bean2);

        // Numiid 错误
        CmsBtBeatInfoBean bean3 = new CmsBtBeatInfoBean();

        bean3.setTaskId(444);
        bean3.setNumIid(5258978L);
        bean3.setProductCode("51A0HC13E1-00LCNB0");
        bean3.setSynFlag(BeatFlag.BEATING);
        bean3.setMessage("test");
        bean3.setImageStatus(ImageStatus.None);
        bean3.setImageTaskId(1);
        bean3.setCreated(new Date());
        bean3.setCreater("testuser");
        bean3.setModified(new Date());
        bean3.setModifier("testuser");
        modelList.add(bean3);

        // 完全正确
        CmsBtBeatInfoBean bean4 = new CmsBtBeatInfoBean();

        bean4.setTaskId(444);
        bean4.setNumIid(525897623218L);
        bean4.setProductCode("51A0HC13E1-00LCNB0");
        bean4.setSynFlag(BeatFlag.BEATING);
        bean4.setMessage("test");
        bean4.setImageStatus(ImageStatus.None);
        bean4.setImageTaskId(1);
        bean4.setCreated(new Date());
        bean4.setCreater("testuser");
        bean4.setModified(new Date());
        bean4.setModifier("testuser");
        modelList.add(bean4);

        beatInfoDaoExt.insertList(modelList);
    }

    @Test
    public void test02UpdateNoCodeMessage() throws Exception {
        beatInfoDaoExt.updateNoCodeMessage(444, BeatFlag.CANT_BEAT.getFlag(), "NO CODE");
    }

    @Test
    public void test03UpdateNoNumiidMessage() throws Exception {
        beatInfoDaoExt.updateNoNumiidMessage(444, BeatFlag.CANT_BEAT.getFlag(), "NO NUMIID");
    }

    @Test
    public void test04UpdateCodeNotMatchNumiidMessage() throws Exception {
        beatInfoDaoExt.updateCodeNotMatchNumiidMessage(444, BeatFlag.CANT_BEAT.getFlag(), "CODE NUMIID ERROR");
    }

    @Test
    public void test05SelectListByTask() throws Exception {
        List<CmsBtBeatInfoBean> list = beatInfoDaoExt.selectListByTask(444);
        Assert.assertTrue(list.size() == 4);
        Assert.assertTrue(list.get(0).getProductCode().equals("1"));
        Assert.assertTrue(list.get(1).getProductCode().equals("51A0HC13E1"));
    }

    @Test
    public void test06SelectListByTaskWithPrice() throws Exception {
        List<CmsBtBeatInfoBean> list = beatInfoDaoExt.selectListByTaskWithPrice(444, BeatFlag.CANT_BEAT, null, 0, 10);
        Assert.assertTrue(list.size() == 3);
        Assert.assertTrue(list.get(0).getProductCode().equals("1"));
        Assert.assertTrue(list.get(1).getProductCode().equals("51A0HC13E1"));
    }

    @Test
    public void test07SelectListByTaskCount() throws Exception {
        int count = beatInfoDaoExt.selectListByTaskCount(444, BeatFlag.BEATING, null);
        Assert.assertTrue(count == 1);
    }

    @Test
    public void test08SelectCountInFlags() throws Exception {
        int count = beatInfoDaoExt.selectCountInFlags(444, BeatFlag.CANT_BEAT);
        Assert.assertTrue(count == 3);
    }

    @Test
    public void test09UpdateFlag() throws Exception {

        List<CmsBtBeatInfoBean> beatInfoBeanList = beatInfoDaoExt.selectListByTask(444);

        CmsBtBeatInfoBean beatInfoBean = beatInfoBeanList
                .stream()
                .filter(i -> i.getNumIid() == 525897623218L && i.getProductCode().equals("51A0HC13E1"))
                .findFirst()
                .orElse(null);

        beatInfoBean.setModifier("testUpdateFlag");
        beatInfoBean.setSynFlag(BeatFlag.BEATING);

        int count = beatInfoDaoExt.updateFlag(beatInfoBean);

        Assert.assertTrue(count == 1);

        int count2 = beatInfoDaoExt.selectCountInFlags(444, BeatFlag.BEATING);

        Assert.assertTrue(count2 == 2);
    }

    @Test
    public void test10UpdateFlags() throws Exception {

        int count = beatInfoDaoExt.updateFlags(444, BeatFlag.SUCCESS.getFlag(), ImageStatus.None.getId(), false, "testUpdateFlags");

        Assert.assertTrue(count == 2);
    }

    @Test
    public void test11SelectSummary() throws Exception {

        List<Map<String, Object>> list = beatInfoDaoExt.selectSummary(444);

        Object value = list.get(0).get("count");

        Assert.assertTrue(Integer.parseInt(value.toString()) == 2);
    }

    @Test
    public void test12SelectListByNumiidInOtherTask() throws Exception {

        List<CmsBtBeatInfoBean> list = beatInfoDaoExt.selectListByNumiidInOtherTask(8, 444, "525897623218");

        Assert.assertTrue(list.size() == 0);
    }

    @Test
    public void test13SelectOneByNumiid() throws Exception {
        CmsBtBeatInfoBean cmsBtBeatInfoBean = beatInfoDaoExt.selectOneByNumiid(444, "2");
        Assert.assertTrue(cmsBtBeatInfoBean.getNumIid() == 2L);
    }

    @Test
    public void test14UpdateCode() throws Exception {

        List<CmsBtBeatInfoBean> beatInfoBeanList = beatInfoDaoExt.selectListByTaskWithPrice(444, BeatFlag.SUCCESS, null, 0, 10);

        CmsBtBeatInfoBean beatInfoBean = beatInfoBeanList
                .stream()
                .filter(i -> i.getProductCode().equals("51A0HC13E1"))
                .findFirst()
                .orElse(null);

        beatInfoBean.setProductCode("51A0HC13E1-0");
        beatInfoBean.setModifier("testUpdateCode");

        int count = beatInfoDaoExt.updateCode(beatInfoBean);

        Assert.assertTrue(count == 1);
    }

    @Test
    public void test15UpdateFlagAndMessage() throws Exception {

        List<CmsBtBeatInfoBean> beatInfoBeanList = beatInfoDaoExt.selectListByTaskWithPrice(444, BeatFlag.SUCCESS, null, 0, 10);

        CmsBtBeatInfoBean beatInfoBean = beatInfoBeanList
                .stream()
                .filter(i -> i.getProductCode().equals("51A0HC13E1-0"))
                .findFirst()
                .orElse(null);

        beatInfoBean.setSynFlag(BeatFlag.REVERT);
        beatInfoBean.setMessage("O_YEAH");
        beatInfoBean.setModifier("testUpdateFlagAndMessage");

        int count = beatInfoDaoExt.updateFlagAndMessage(beatInfoBean);

        Assert.assertTrue(count == 1);

        beatInfoBeanList = beatInfoDaoExt.selectListByTaskWithPrice(444, BeatFlag.REVERT, null, 0, 10);

        Assert.assertTrue(beatInfoBeanList.get(0).getMessage().contains("O_YEAH"));
    }

    @Test
    public void test16SelectListNeedBeatFullData() throws Exception {
        List<CmsBtBeatInfoBean> list = beatInfoDaoExt.selectListNeedBeatFullData(
                10,
                BeatFlag.BEATING.getFlag(),
                BeatFlag.REVERT.getFlag(),
                BeatFlag.SUCCESS.getFlag(),
                DateTimeUtil.parse("2017-01-01 00:00:00"), Arrays.asList(23));

        Assert.assertTrue(list.size() == 2);

        CmsBtBeatInfoBean bean = list.get(1);

        Assert.assertTrue(bean.getNumIid() == 525897623218L);
        Assert.assertTrue(bean.getProductCode().equals("51A0HC13E1-00LCNB0"));
    }

    @Test
    public void test17DeleteByTask() throws Exception {
        int count = beatInfoDaoExt.deleteByTask(444);
        Assert.assertTrue(count == 4);
    }
}