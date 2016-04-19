package com.voyageone.service.impl.jumei;

import com.voyageone.common.components.transaction.TransactionRunner;
import com.voyageone.components.jumei.bean.JmBrandBean;
import com.voyageone.components.jumei.service.JumeiBrandService;
import com.voyageone.service.dao.jumei.CmsBtJmMasterBrandDao;
import com.voyageone.service.daoext.jumei.CmsBtJmMasterBrandDaoExt;
import com.voyageone.service.impl.jumei.JMProductUpdate.JMShopBeanService;
import com.voyageone.service.model.jumei.CmsBtJmMasterBrandModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
public class CmsBtJmMasterBrandService {
    @Autowired
    JMShopBeanService serviceJMShopBean;
    @Autowired
    JumeiBrandService serviceJumeiBrand;
    @Autowired
    TransactionRunner transactionRunner;
    @Autowired
    CmsBtJmMasterBrandDao dao;
    @Autowired
    CmsBtJmMasterBrandDaoExt daoExtCmsBtJmMasterBrand;

    public void loadJmMasterBrand(String userName,String channelId) throws Exception {
        List<CmsBtJmMasterBrandModel> listCmsBtJmMasterBrand = new ArrayList<>();
        List<JmBrandBean> list = serviceJumeiBrand.getBrands(serviceJMShopBean.getShopBean(channelId));
        CmsBtJmMasterBrandModel model = null;
        for (JmBrandBean bean : list) {
            model = daoExtCmsBtJmMasterBrand.getByJmMasterBrandId(bean.getId());
            if (model == null) {
                model = new CmsBtJmMasterBrandModel();
            }
            listCmsBtJmMasterBrand.add(model);
            model.setJmMasterBrandId(bean.getId());
            model.setEnName(bean.getEnName());
            model.setName(bean.getName());
            model.setCreated(new Date());
            model.setCreater(userName);
            model.setModifier(userName);
        }
        transactionRunner.runWithTran(() -> {
            saveList(listCmsBtJmMasterBrand);                             //保存
        });
    }

    public void saveList(List<CmsBtJmMasterBrandModel> listCmsBtJmMasterBrand) {
        for (CmsBtJmMasterBrandModel model : listCmsBtJmMasterBrand) {
            if (model.getId() > 0) {
                dao.update(model);
            } else {
                dao.insert(model);
            }
        }
    }

    /**
     * 返回聚美所有的brand数据.
     * @return
     */
    public List<CmsBtJmMasterBrandModel> selectAll() {
        return dao.selectList(new HashMap<>());
    }
}
