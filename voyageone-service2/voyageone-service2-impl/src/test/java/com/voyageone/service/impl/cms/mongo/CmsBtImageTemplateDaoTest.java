package com.voyageone.service.impl.cms.mongo;

import com.voyageone.service.dao.cms.mongo.CmsBtImageTemplateDao;
import com.voyageone.service.model.cms.mongo.channel.CmsBtImageTemplateModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class CmsBtImageTemplateDaoTest {
    @Autowired
    CmsBtImageTemplateDao dao;

    @Test
    public  void  test()
    {
        CmsBtImageTemplateModel model=new CmsBtImageTemplateModel();
        dao.insert(model);
       List<CmsBtImageTemplateModel> list= dao.selectAll();

      //平台  result.put("platformList", TypeChannels.getTypeListSkuCarts((String)param.get("channelId"), "A", (String)param.get("lang")));
// 品牌下拉列表
     //   result.put("brandNameList", TypeChannels.getTypeWithLang(Constants.comMtTypeChannel.BRAND_41, (String)param.get("channelId"), (String)param.get("lang")));
// 产品类型下拉列表
     //   result.put("productTypeList", TypeChannels.getTypeWithLang(Constants.comMtTypeChannel.PROUDCT_TYPE_57, (String)param.get("channelId"), (String)param.get("lang")));
// 尺寸类型下拉列表
     //   result.put("sizeTypeList", TypeChannels.getTypeWithLang(Constants.comMtTypeChannel.PROUDCT_TYPE_58, (String)param.get("channelId"), (String)param.get("lang")));
       // dao.
    }
}
