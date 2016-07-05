package com.voyageone.service.impl.cms;

import com.voyageone.common.util.JacksonUtil;
import com.voyageone.service.bean.cms.CmsMtCategoryTreeAllBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author james.li on 2016/5/12.
 * @version 2.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class CategoryTreeServiceTest {

    @Autowired
    private CategoryTreeService categoryTreeService;

    @Autowired
    private CategoryTreeAllService categoryTreeAllService;

    @Test
    public void testAddCategory() throws Exception {
        String catpath[] = {"童装/婴儿装/亲子装>儿童礼服",
                "童装/婴儿装/亲子装>儿童演出服",
                "童装/婴儿装/亲子装>抹胸",
                "童装/婴儿装/亲子装>背心吊带",
                "童装/婴儿装/亲子装>反穿衣/罩衣",
                "童装/婴儿装/亲子装>其它",
                "童装/婴儿装/亲子装>儿童袜子(0-16岁)",
                "童装/婴儿装/亲子装>校服/校服定制",
                "童装/婴儿装/亲子装>亲子装/亲子时装",
                "童装/婴儿装/亲子装>套装",
                "童装/婴儿装/亲子装>棉袄/棉服",
                "童装/婴儿装/亲子装>毛衣/针织衫",
                "童装/婴儿装/亲子装>卫衣/绒衫",
                "童装/婴儿装/亲子装>衬衫",
                "童装/婴儿装/亲子装>T恤",
                "童装/婴儿装/亲子装>裤子",
                "童装/婴儿装/亲子装>马甲",
                "童装/婴儿装/亲子装>披风/斗篷",
                "童装/婴儿装/亲子装>连身衣/爬服/哈衣",
                "童装/婴儿装/亲子装>婴儿礼盒",
                "童装/婴儿装/亲子装>儿童户外服>儿童冲锋衣",
                "童装/婴儿装/亲子装>儿童户外服>儿童运动套装",
                "童装/婴儿装/亲子装>儿童户外服>儿童运动裤",
                "童装/婴儿装/亲子装>儿童户外服>儿童运动衣",
                "童装/婴儿装/亲子装>儿童户外服>儿童软壳裤",
                "童装/婴儿装/亲子装>儿童户外服>儿童滑雪裤",
                "童装/婴儿装/亲子装>儿童户外服>儿童速干裤",
                "童装/婴儿装/亲子装>儿童户外服>儿童冲锋裤",
                "童装/婴儿装/亲子装>儿童户外服>儿童滑雪服",
                "童装/婴儿装/亲子装>儿童户外服>儿童速干衬衫",
                "童装/婴儿装/亲子装>儿童户外服>儿童速干T恤",
                "童装/婴儿装/亲子装>儿童户外服>儿童皮肤衣/防晒衣",
                "童装/婴儿装/亲子装>儿童户外服>儿童软壳衣",
                "童装/婴儿装/亲子装>儿童户外服>儿童抓绒衣",
                "童装/婴儿装/亲子装>羽绒服饰/羽绒内胆>羽绒马甲",
                "童装/婴儿装/亲子装>羽绒服饰/羽绒内胆>羽绒内胆",
                "童装/婴儿装/亲子装>羽绒服饰/羽绒内胆>羽绒连体衣",
                "童装/婴儿装/亲子装>羽绒服饰/羽绒内胆>羽绒裤",
                "童装/婴儿装/亲子装>羽绒服饰/羽绒内胆>羽绒服",
                "童装/婴儿装/亲子装>裙子(新)>连衣裙",
                "童装/婴儿装/亲子装>裙子(新)>半身裙",
                "童装/婴儿装/亲子装>儿童配饰>其他配饰",
                "童装/婴儿装/亲子装>儿童配饰>腰带",
                "童装/婴儿装/亲子装>儿童配饰>太阳镜",
                "童装/婴儿装/亲子装>儿童配饰>手链",
                "童装/婴儿装/亲子装>儿童配饰>领结",
                "童装/婴儿装/亲子装>儿童配饰>领带",
                "童装/婴儿装/亲子装>儿童配饰>项链",
                "童装/婴儿装/亲子装>儿童配饰>假发",
                "童装/婴儿装/亲子装>儿童配饰>发饰",
                "童装/婴儿装/亲子装>儿童配饰>包包",
                "童装/婴儿装/亲子装>儿童配饰>背带",
                "童装/婴儿装/亲子装>儿童泳装>泳衣裤",
                "童装/婴儿装/亲子装>儿童泳装>泳帽",
                "童装/婴儿装/亲子装>儿童家居服>浴袍",
                "童装/婴儿装/亲子装>儿童家居服>家居服套装",
                "童装/婴儿装/亲子装>儿童家居服>家居服连体衣",
                "童装/婴儿装/亲子装>儿童家居服>家居袍/睡袍",
                "童装/婴儿装/亲子装>儿童家居服>家居裙/睡裙",
                "童装/婴儿装/亲子装>儿童家居服>家居裤/睡裤",
                "童装/婴儿装/亲子装>儿童家居服>家居服上装",
                "童装/婴儿装/亲子装>儿童内衣裤>内衣套装",
                "童装/婴儿装/亲子装>儿童内衣裤>内裤",
                "童装/婴儿装/亲子装>儿童内衣裤>保暖裤",
                "童装/婴儿装/亲子装>儿童内衣裤>保暖上装",
                "童装/婴儿装/亲子装>肚兜/肚围/护脐带>肚围/护脐带",
                "童装/婴儿装/亲子装>肚兜/肚围/护脐带>肚兜",
                "童装/婴儿装/亲子装>儿童旗袍/唐装/民族服装>其他民族服装",
                "童装/婴儿装/亲子装>儿童旗袍/唐装/民族服装>旗袍",
                "童装/婴儿装/亲子装>儿童旗袍/唐装/民族服装>唐装",
                "童装/婴儿装/亲子装>帽子/围巾/口罩/手套/耳套/脚套>袖套",
                "童装/婴儿装/亲子装>帽子/围巾/口罩/手套/耳套/脚套>其它",
                "童装/婴儿装/亲子装>帽子/围巾/口罩/手套/耳套/脚套>耳套/耳暖",
                "童装/婴儿装/亲子装>帽子/围巾/口罩/手套/耳套/脚套>口罩",
                "童装/婴儿装/亲子装>帽子/围巾/口罩/手套/耳套/脚套>防抓手套/护脚/护膝",
                "童装/婴儿装/亲子装>帽子/围巾/口罩/手套/耳套/脚套>多件套 帽子、围巾、手套等组合",
                "童装/婴儿装/亲子装>帽子/围巾/口罩/手套/耳套/脚套>手套",
                "童装/婴儿装/亲子装>帽子/围巾/口罩/手套/耳套/脚套>围巾",
                "童装/婴儿装/亲子装>帽子/围巾/口罩/手套/耳套/脚套>帽子",
                "童装/婴儿装/亲子装>外套/夹克/大衣>皮草/仿皮草",
                "童装/婴儿装/亲子装>外套/夹克/大衣>风衣",
                "童装/婴儿装/亲子装>外套/夹克/大衣>呢大衣",
                "童装/婴儿装/亲子装>外套/夹克/大衣>夹克/皮衣",
                "童装/婴儿装/亲子装>外套/夹克/大衣>西服/小西装",
                "童装/婴儿装/亲子装>外套/夹克/大衣>普通外套"};

        List<String> cat = Arrays.asList(catpath);
        cat.forEach(s -> categoryTreeService.addCategory(s, "james1"));
    }


    @Test
    public void testGetCategoryAll() throws Exception {
            List<CmsMtCategoryTreeAllBean> cmsMtCategoryTreeAllBeans =  categoryTreeAllService.getCategoriesByChannelId("010", "cn");
            System.out.println(JacksonUtil.bean2Json(cmsMtCategoryTreeAllBeans));
            cmsMtCategoryTreeAllBeans =  categoryTreeAllService.getCategoriesByChannelId("010", "cn");
            System.out.println(JacksonUtil.bean2Json(cmsMtCategoryTreeAllBeans));
    }
}