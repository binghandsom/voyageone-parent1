package com.voyageone.task2.cms.service.product.batch;

import com.voyageone.common.util.JacksonUtil;
import com.voyageone.service.impl.cms.vomq.vomessage.body.AdvSearchExportMQMessageBody;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

/**
 * Created by james on 2017/2/13.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class CmsAdvSearchExportFileServiceTest {

    @Autowired
    CmsAdvSearchExportFileService cmsAdvSearchExportFileService;
    @Test
    public void export() throws Exception {
        String aa = "{\"consumerRetryTimes\":0,\"mqId\":0,\"delaySecond\":0,\"sender\":\"james\",\"cmsBtExportTaskId\":9508,\"searchValue\":{\"compareType\":\"$gt\",\"brand\":null,\"tags\":[],\"priceChgFlg\":\"\",\"priceDiffFlg\":\"\",\"tagTypeSelectValue\":\"0\",\"promotionList\":[],\"catgoryList\":[],\"cidValue\":[],\"promotionTagType\":1,\"freeTagType\":1,\"supplierType\":1,\"brandSelType\":1,\"productSelType\":\"1\",\"sizeSelType\":\"1\",\"salesType\":\"All\",\"custGroupType\":\"1\",\"inventory\":0,\"custAttrMap\":[{\"inputVal\":\"\",\"inputOpts\":\"\",\"inputOptsKey\":\"\"}],\"_selCodeList\":[\"122287\",\"138043\",\"146509\",\"55008\",\"53241\",\"134892\",\"105992\",\"54330\",\"134206\",\"53391\"],\"fileType\":3,\"productStatus\":[],\"platformStatus\":[],\"pRealStatus\":[],\"shopCatStatus\":0,\"pCatStatus\":0,\"_channleId\":\"928\",\"_userName\":\"james\",\"_language\":\"cn\",\"_taskId\":9508,\"_sessionBean\":{\"_adv_search_customProps\":[],\"_adv_search_commonProps\":[{\"valType\":\"string\",\"propId\":\"codeDiff\",\"propName\":\"原始 颜色/口味/香型等\"},{\"valType\":\"string\",\"propId\":\"color\",\"propName\":\"颜色/口味/香型等\"},{\"valType\":\"list-2\",\"propId\":\"productType\",\"propName\":\"产品分类\"},{\"valType\":\"list-2\",\"propId\":\"sizeType\",\"propName\":\"适用人群\"},{\"valType\":\"list-2\",\"propId\":\"productTypeCn\",\"propName\":\"产品分类(中文)\"},{\"valType\":\"list-2\",\"propId\":\"sizeTypeCn\",\"propName\":\"适用人群(中文)\"},{\"valType\":\"list-2\",\"propId\":\"hsCodeCrop\",\"propName\":\"税号集货\"},{\"valType\":\"list-2\",\"propId\":\"hsCodePrivate\",\"propName\":\"税号个人\"},{\"valType\":\"string\",\"propId\":\"origin\",\"propName\":\"产地\"},{\"valType\":\"string\",\"propId\":\"translator\",\"propName\":\"翻译者\"},{\"valType\":\"string\",\"propId\":\"weightLB\",\"propName\":\"重量(lb)\"},{\"valType\":\"string\",\"propId\":\"weightG\",\"propName\":\"重量(克)\"},{\"valType\":\"string\",\"propId\":\"weightKG\",\"propName\":\"重量(千克)\"}],\"_adv_search_props_searchItems\":\"common.fields.codeDiff;common.fields.color;common.fields.productType;common.fields.sizeType;common.fields.productTypeCn;common.fields.sizeTypeCn;common.fields.hsCodeCrop;common.fields.hsCodePrivate;common.fields.origin;common.fields.translator;common.fields.weightLB;common.fields.weightG;common.fields.weightKG;\",\"_adv_search_selSalesType\":[{\"name\":\"总销量\",\"value\":\"sales.codeSumAll.cartId0\"}],\"_adv_search_selBiDataList\":null}}}";
        AdvSearchExportMQMessageBody body = JacksonUtil.json2Bean(aa,AdvSearchExportMQMessageBody.class);
        cmsAdvSearchExportFileService.export(body);
    }

}