package com.voyageone.synship.service;

import com.google.gson.Gson;
import com.voyageone.common.Constants;
import com.voyageone.common.components.issueLog.IssueLog;
import com.voyageone.common.components.issueLog.enums.ErrorType;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.configs.ChannelConfigs;
import com.voyageone.common.configs.beans.OrderChannelBean;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.JsonUtil;
import com.voyageone.common.util.StringUtils;
import com.voyageone.synship.SynshipConstants;
import com.voyageone.synship.dao.ProductDao;
import com.voyageone.synship.dao.TaskDao;
import com.voyageone.synship.modelbean.ProductBean;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dell on 2015/7/27.
 */
@Service
public class ProductService {

    private Log logger = LogFactory.getLog(this.getClass());

    @Autowired
    private TaskDao taskDao;

    @Autowired
    private ProductDao productDao
            ;
    @Autowired
    protected IssueLog issueLog;

    /**
     * Product记录导入
     * @param param 检索参数
     */
    public void importProduct(String param) {

        logger.info("Product记录导入开始");
        List<ProductBean> productBeans = new ArrayList<>();

        try {
            productBeans = JsonUtil.jsonToBeanList(param, ProductBean.class);

            String order_channel_id = "";
            if (productBeans !=null  && productBeans.size() > 0){
                order_channel_id = productBeans.get(0).getChannel_id();
            }
            if (StringUtils.isNullOrBlank2(order_channel_id)){
                logger.info("Product记录导入时的渠道没有设定，无法导入");
                return;
            }

            logger.info("Product记录导入时的渠道："+ order_channel_id);
            OrderChannelBean channel = ChannelConfigs.getChannel(order_channel_id);
            if (channel == null) {
                logger.info("Product记录导入时的渠道没有对应的配置，无法导入");
                return;
            }

            String postUri = taskDao.getCfg_val2("WmsAllotInventoryJob","order_channel_id",order_channel_id);

            // 没有设定或者推送方式还是旧方法的话，处理推送记录
            if(!(StringUtils.isNullOrBlank2(postUri) || postUri.contains("aspx"))){
                logger.info("-----"+ channel.getFull_name()+"的Product已由新系统导入，这里忽略导入："+postUri);
                return;
            }

            logger.info("-----"+ channel.getFull_name()+"的Product的需要导入件数："+productBeans.size());

            int intCount = 0;
            List<String>errorCodeList = new ArrayList<>();
            for (ProductBean productBean : productBeans){

                productBean.setCreated(DateTimeUtil.getGMTTime(productBean.getCreated(), SynshipConstants.TimeZone.CN));
                productBean.setModified(DateTimeUtil.getGMTTime(productBean.getModified(), SynshipConstants.TimeZone.CN));

                int result = productDao.insertProduct(productBean);

                if (result == 0) {
                    errorCodeList.add(productBean.getCode());
                    String json = productBean == null ? "" : new Gson().toJson(productBean);
                    logger.info("-----"+ channel.getFull_name()+"的Product导入失败："+json);
                } else {
                    intCount = intCount + 1;
                }
            }

            logger.info("-----"+ channel.getFull_name()+"的Product的导入成功件数："+productBeans.size());

            if (errorCodeList.size() > 0) {
                issueLog.log("importProduct", channel.getFull_name() + "的Product中部分SKU导入失败", ErrorType.WSDL, SubSystem.WMS, makeIssueAttach(errorCodeList));
            }

            logger.info("Product记录导入结束");
        }
        catch (Exception e) {
            logger.info("Product记录导入失败：" + e);
        }


    }

    private String makeIssueAttach(Object attach) {

        Gson gson = new Gson();

        if (attach == null) return Constants.EmptyString;

        if (attach instanceof String) return (String) attach;

        return gson.toJson(attach);
    }



}
