package com.voyageone.task2.cms.mqjob.usa;

import com.voyageone.base.dao.mongodb.JongoUpdate;
import com.voyageone.common.util.ListUtils;
import com.voyageone.service.dao.cms.mongo.CmsBtProductDao;
import com.voyageone.service.impl.cms.vomq.vomessage.body.usa.CmsBtProductUpdateTagsMQMessageBody;
import com.voyageone.task2.cms.mqjob.TBaseMQCmsSubService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by dell on 2017/7/18.
 *
 */
@Service
@RabbitListener()
public class CmsBtProductUpdateTagsMQJob extends TBaseMQCmsSubService<CmsBtProductUpdateTagsMQMessageBody> {
    @Autowired
    private CmsBtProductDao cmsBtProductDao;

    @Override
    public void onStartup(CmsBtProductUpdateTagsMQMessageBody messageBody) throws Exception {
        if (messageBody != null){
            String channelId = messageBody.getChannelId();
            List<String> prodCodeList = messageBody.getProdCodeList();
            List<String> tagPathList = messageBody.getTagPathList();
            List<String> pathList = new ArrayList<>();
            //将标签转换为list
            if (tagPathList != null) {
                for (String tagPath : tagPathList) {
                    // 生成级联tag path列表
                    String[] pathArr = org.apache.commons.lang3.StringUtils.split(tagPath, '-');
                    for (int j = 0; j < pathArr.length; j++) {
                        StringBuilder curTagPath = new StringBuilder("-");
                        for (int i = 0; i <= j ; i ++) {
                            curTagPath.append(pathArr[i]);
                            curTagPath.append("-");
                        }
                        pathList.add(curTagPath.toString());
                    }
                }
                // 过滤重复项目
                pathList = pathList.stream().distinct().collect(Collectors.toList());
            }
            if (ListUtils.notNull(prodCodeList)){
                for (String prodCode : prodCodeList) {
                    JongoUpdate updObj = new JongoUpdate();
                    updObj.setQuery("{\"common.fields.code\":#}");
                    updObj.setQueryParameters(prodCode);
                    updObj.setUpdate("{$set:{'freeTags':#}}");
                    updObj.setUpdateParameters(pathList);
                    cmsBtProductDao.updateMulti(updObj, channelId);
                }
            }
        }
    }
}
