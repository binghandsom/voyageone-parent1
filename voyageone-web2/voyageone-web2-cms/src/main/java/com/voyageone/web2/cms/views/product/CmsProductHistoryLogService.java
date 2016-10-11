package com.voyageone.web2.cms.views.product;

import com.voyageone.base.dao.mongodb.JongoQuery;
import com.voyageone.common.configs.Enums.TypeConfigEnums;
import com.voyageone.common.configs.Types;
import com.voyageone.service.dao.cms.mongo.CmsBtPlatformActiveLogDao;
import com.voyageone.service.model.cms.mongo.product.CmsBtPlatformActiveLogModel;
import com.voyageone.web2.base.BaseViewService;
import com.voyageone.web2.core.bean.UserSessionBean;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 查询产品的各种变更历史
 * Created by jiangjusheng on 2016/07/15
 * @author jiangjusheng
 */
@Service
public class CmsProductHistoryLogService extends BaseViewService {

    @Autowired
    private CmsBtPlatformActiveLogDao platformActiveLogDao;

    /**
     * 查询商品上下架操作历史
     */
    public List<CmsBtPlatformActiveLogModel> getPutOnOffLogList(Map<String, Object> params, UserSessionBean userInfo, String lang) {
        String prodCode = StringUtils.trimToNull((String) params.get("prodCode"));
        if (prodCode == null) {
            $warn("查询商品上下架操作历史 缺少参数，没有产品code params=" + params.toString());
            return null;
        }

        Integer cartId = (Integer) params.get("cartId");
        if (cartId == null) {
            cartId = 0;
        }
        Integer pageNum = (Integer) params.get("pageNum");
        if (pageNum == null || pageNum == 0) {
            pageNum = 1;
        }
        Integer pageSize = (Integer) params.get("pageSize");
        if (pageSize == null || pageSize == 0) {
            pageSize = 10;
        }

        JongoQuery queryObject = new JongoQuery();
        if (cartId == 0) {
            queryObject.setQuery("{'prodCode':#}");
            queryObject.setParameters(prodCode);
        } else {
            queryObject.setQuery("{'prodCode':#,'cartId':#}");
            queryObject.setParameters(prodCode, cartId);
        }
        queryObject.setSkip((pageNum - 1) * pageSize);
        queryObject.setLimit(pageSize);
        queryObject.setSort("{'created':-1}");

        List<CmsBtPlatformActiveLogModel> prodObjList = platformActiveLogDao.select(queryObject, userInfo.getSelChannelId());
        if (prodObjList == null || prodObjList.isEmpty()) {
            $warn("查询商品上下架操作历史 结果为空 查询条件=：" + queryObject.toString());
            return prodObjList;
        }

        // 画面上的code到名称的转换
        for (CmsBtPlatformActiveLogModel logModel : prodObjList) {
            logModel.setActiveStatus(Types.getTypeName(TypeConfigEnums.MastType.platformActicve.getId(), lang, logModel.getActiveStatus()));
            logModel.setPlatformStatus(Types.getTypeName(TypeConfigEnums.MastType.platformStatus.getId(), lang, logModel.getPlatformStatus()));
            String msg = Types.getTypeName(TypeConfigEnums.MastType.putOnOffStatus.getId(), lang, logModel.getResult());
            if (msg == null) {
                msg = "";
            }
            String failTxt = StringUtils.trimToNull(logModel.getFailedComment());
            if (failTxt != null) {
                msg = msg + "（" + failTxt + "）";
            }
            logModel.setResult(msg);
        }
        return prodObjList;
    }
}
