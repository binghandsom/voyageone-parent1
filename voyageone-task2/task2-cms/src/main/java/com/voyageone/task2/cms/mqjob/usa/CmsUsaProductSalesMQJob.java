package com.voyageone.task2.cms.mqjob.usa;

import com.voyageone.base.dao.mongodb.JongoQuery;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.service.dao.cms.mongo.CmsMtProdSalesHisDao;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.impl.cms.vomq.vomessage.body.usa.CmsUsaProductSalesMQMessageBody;
import com.voyageone.service.model.cms.CmsMtProdSalesHisModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.task2.cms.mqjob.TBaseMQCmsService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by dell on 2017/7/31.
 * 接受美国销量数据
 */
@Service
@RabbitListener()
public class CmsUsaProductSalesMQJob extends TBaseMQCmsService<CmsUsaProductSalesMQMessageBody> {
    @Autowired
    ProductService productService;
    @Autowired
    CmsMtProdSalesHisDao cmsMtProdSalesHisDao;

    @Override
    public void onStartup(CmsUsaProductSalesMQMessageBody messageBody) throws Exception {
        List<CmsUsaProductSalesMQMessageBody.TargetParam> items = messageBody.getItems();
        for (CmsUsaProductSalesMQMessageBody.TargetParam item : items) {
            JongoQuery jongoQuery = new JongoQuery();
            String time = parseTime(item.getOrderDate());
            Criteria criteria = new Criteria("cart_id").is(item.getCartId()).and("channel_id").is(item.getChannelId()).
                    and("sku").is(item.getSku()).and("date").is(time);
            jongoQuery.setQuery(criteria);
            CmsMtProdSalesHisModel cmsMtProdSalesHisModel = cmsMtProdSalesHisDao.selectOneWithQuery(jongoQuery);
            if (cmsMtProdSalesHisModel != null) {
                //查到了对应的数据
                cmsMtProdSalesHisModel.setModifier(getTaskName());
                cmsMtProdSalesHisModel.setModified(DateTimeUtil.format(new Date(), DateTimeUtil.DEFAULT_DATETIME_FORMAT));
                //1:下单 / 0:取消
                if (item.getStatus() == 1) {
                    //下单
                    cmsMtProdSalesHisModel.setQty(cmsMtProdSalesHisModel.getQty() + item.getQty());
                } else {
                    //取消
                    cmsMtProdSalesHisModel.setQty(cmsMtProdSalesHisModel.getQty() - item.getQty());
                }
                cmsMtProdSalesHisDao.update(cmsMtProdSalesHisModel);
            } else {
                //没有查到对应的数据,新建一条数据
                String code = null;
                CmsBtProductModel cmsBtProductModel = productService.getProductBySku(item.getChannelId(), item.getSku());
                if (cmsBtProductModel != null) {
                    code = cmsBtProductModel.getCommon().getFields().getCode();
                }
                cmsMtProdSalesHisModel = new CmsMtProdSalesHisModel();
                cmsMtProdSalesHisModel.setModified(DateTimeUtil.format(new Date(), DateTimeUtil.DEFAULT_DATETIME_FORMAT));
                cmsMtProdSalesHisModel.setSku(item.getSku());
                cmsMtProdSalesHisModel.setModifier(getTaskName());
                cmsMtProdSalesHisModel.setProdCode(code);
                cmsMtProdSalesHisModel.setCart_id(item.getCartId());
                cmsMtProdSalesHisModel.setChannel_id(item.getChannelId());
                cmsMtProdSalesHisModel.setDate(parseTime(item.getOrderDate()));
                cmsMtProdSalesHisModel.setCreated(DateTimeUtil.format(new Date(), DateTimeUtil.DEFAULT_DATETIME_FORMAT));
                cmsMtProdSalesHisModel.setCreater(getTaskName());
                if (item.getStatus() == 1) {
                    //下单
                    cmsMtProdSalesHisModel.setQty(item.getQty());
                } else {
                    //取消
                    cmsMtProdSalesHisModel.setQty(0 - item.getQty());
                }
                cmsMtProdSalesHisDao.update(cmsMtProdSalesHisModel);
            }
        }
    }
    private String parseTime(Long time) {
        //美国西海岸时间,减八个小时
        Date date = new Date(time);
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.MINUTE, -60 * 8);
        String format = DateTimeUtil.format(c.getTime(), DateTimeUtil.DEFAULT_DATE_FORMAT);
        return format;
    }
}
