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
import com.voyageone.synship.dao.ItemDetailDao;
import com.voyageone.synship.dao.TaskDao;
import com.voyageone.synship.modelbean.ItemDetailBean;
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
public class ItemDetailService {

    private Log logger = LogFactory.getLog(this.getClass());

    @Autowired
    private TaskDao taskDao;

    @Autowired
    private ItemDetailDao itemDetailDao
            ;
    @Autowired
    protected IssueLog issueLog;

    /**
     * ItemDetail记录导入
     * @param param 检索参数
     */
    public void importItemDetail(String param) {

        logger.info("ItemDetail记录导入开始");
        List<ItemDetailBean> itemDetailBeans = new ArrayList<>();

        try {
            itemDetailBeans = JsonUtil.jsonToBeanList(param, ItemDetailBean.class);

            String order_channel_id = "";
            if (itemDetailBeans !=null  && itemDetailBeans.size() > 0){
                order_channel_id = itemDetailBeans.get(0).getOrder_channel_id();
            }
            if (StringUtils.isNullOrBlank2(order_channel_id)){
                logger.info("ItemDetail记录导入时的渠道没有设定，无法导入");
                return;
            }

            logger.info("ItemDetail记录导入时的渠道："+ order_channel_id);
            OrderChannelBean channel = ChannelConfigs.getChannel(order_channel_id);
            if (channel == null) {
                logger.info("ItemDetail记录导入时的渠道没有对应的配置，无法导入");
                return;
            }

            String postUri = taskDao.getCfg_val2("WmsAllotInventoryJob","order_channel_id",order_channel_id);

            // 没有设定或者推送方式还是旧方法的话，处理推送记录
            if(!(StringUtils.isNullOrBlank2(postUri) || postUri.contains("aspx"))){
                logger.info("-----"+ channel.getFull_name()+"的ItemDetail已由新系统导入，这里忽略导入："+postUri);
                return;
            }

            logger.info("-----"+ channel.getFull_name()+"的ItemDetail的需要导入件数："+itemDetailBeans.size());

            int intCount = 0;
            List<String>errorSKUList = new ArrayList<>();
            for (ItemDetailBean itemDetailBean : itemDetailBeans){

                itemDetailBean.setCreated(DateTimeUtil.getGMTTime(itemDetailBean.getCreated(), SynshipConstants.TimeZone.CN));
                itemDetailBean.setModified(DateTimeUtil.getGMTTime(itemDetailBean.getModified(), SynshipConstants.TimeZone.CN));

                int result = itemDetailDao.insertItemDetail(itemDetailBean);

                if (result == 0) {
                    errorSKUList.add(itemDetailBean.getSku());
                    String json = itemDetailBean == null ? "" : new Gson().toJson(itemDetailBean);
                    logger.info("-----"+ channel.getFull_name()+"的ItemDetail导入失败："+json);
                } else {
                    intCount = intCount + 1;
                }
            }

            logger.info("-----"+ channel.getFull_name()+"的ItemDetail的导入成功件数："+itemDetailBeans.size());

            if (errorSKUList.size() > 0) {
                issueLog.log("importItemDetail", channel.getFull_name() + "的ItemDetail中部分SKU导入失败", ErrorType.WSDL, SubSystem.WMS, makeIssueAttach(errorSKUList));
            }

            logger.info("ItemDetail记录导入结束");
        }
        catch (Exception e) {
            logger.info("ItemDetail记录导入失败：" + e);
        }


    }

    private String makeIssueAttach(Object attach) {

        Gson gson = new Gson();

        if (attach == null) return Constants.EmptyString;

        if (attach instanceof String) return (String) attach;

        return gson.toJson(attach);
    }



}
