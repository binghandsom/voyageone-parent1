package com.voyageone.service.impl.cms.product;
import com.voyageone.common.PageQueryParameters;
import com.voyageone.common.components.transaction.VOTransactional;
import com.voyageone.common.configs.Enums.CartEnums;
import com.voyageone.common.util.ListUtils;
import com.voyageone.service.bean.cms.product.EnumProductOperationType;
import com.voyageone.service.dao.cms.CmsBtProductStatusHistoryDao;
import com.voyageone.service.daoext.cms.CmsBtProductStatusHistoryDaoExt;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.cms.CmsBtProductStatusHistoryModel;
import com.voyageone.service.model.util.MapModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ProductStatusHistoryService extends BaseService {
    @Autowired
    CmsBtProductStatusHistoryDao dao;
    @Autowired
    CmsBtProductStatusHistoryDaoExt daoExt;

    public List<MapModel> getPage(PageQueryParameters parameters) {
        List<MapModel> list = daoExt.selectPage(parameters.getSqlMapParameter());
        for (MapModel model : list) {
            loadMap(model);
        }
        return list;
    }
    void loadMap(MapModel map) {
        CartEnums.Cart cartEnum = CartEnums.Cart.getValueByID(map.get("cartId").toString());
        if (cartEnum != null) {
            map.put("cartName", cartEnum.name());
        }
        map.put("operationTypeName", EnumProductOperationType.getNameById(map.get("operationType")));
    }
    public long getCount(PageQueryParameters parameters) {
        return daoExt.selectCount(parameters.getSqlMapParameter());
    }
    @VOTransactional
    public void insertList(String channelId, List<String> codes, String status, int cartId, EnumProductOperationType enumProductOperationType,String comment, String modifier) {
        List<CmsBtProductStatusHistoryModel> list = new ArrayList<>();
        CmsBtProductStatusHistoryModel productStatusHistory = null;
        for (String code : codes) {
            productStatusHistory = get(channelId, code, status, cartId, enumProductOperationType, comment,modifier);
            list.add(productStatusHistory);
        }
        List<List<CmsBtProductStatusHistoryModel>> pageList = ListUtils.getPageList(list, 100);//分隔数据源 每页100
        for (List<CmsBtProductStatusHistoryModel> page : pageList) {
            daoExt.insertList(page);
        }
    }
    public void insert(String channelId, String code, String status, int cartId, EnumProductOperationType enumProductOperationType, String Comment,String modifier) {
        CmsBtProductStatusHistoryModel productStatusHistory = get(channelId, code, status, cartId, enumProductOperationType, Comment,modifier);
        dao.insert(productStatusHistory);
    }
    private CmsBtProductStatusHistoryModel get(String channelId, String code, String status, int cartId, EnumProductOperationType enumProductOperationType,String comment, String modifier) {
        CmsBtProductStatusHistoryModel productStatusHistory = new CmsBtProductStatusHistoryModel();
        productStatusHistory.setChannelId(channelId);
        productStatusHistory.setCode(code);
        productStatusHistory.setStatus(status);
        productStatusHistory.setCartId(cartId);
        productStatusHistory.setOperationType(enumProductOperationType.getId());
        productStatusHistory.setModifier(modifier);
        productStatusHistory.setCreater(modifier);
        productStatusHistory.setCreated(new Date());
        productStatusHistory.setComment(comment);
        return productStatusHistory;
    }
}
