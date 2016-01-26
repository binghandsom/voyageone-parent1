package com.voyageone.batch.cms.service;

import com.voyageone.batch.base.BaseTaskService;
import com.voyageone.batch.cms.bean.JmMasterBean;
import com.voyageone.batch.cms.dao.JmCategoryDao;
import com.voyageone.batch.cms.dao.JmMasterDao;
import com.voyageone.batch.core.modelbean.TaskControlBean;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.components.jumei.Bean.JmBrandBean;
import com.voyageone.common.components.jumei.Bean.JmCategoryBean;
import com.voyageone.common.components.jumei.JumeiBrandService;
import com.voyageone.common.components.jumei.JumeiCategoryService;
import com.voyageone.common.configs.Enums.CartEnums;
import com.voyageone.common.configs.ShopConfigs;
import com.voyageone.common.configs.beans.ShopBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author james.li on 2016/1/25.
 * @version 2.0.0
 */
@Service
public class CmsGetJmMasterService extends BaseTaskService {

    @Autowired
    private JmCategoryDao jmCategoryDao;
    @Autowired
    private JmMasterDao jmMasterDao;

    @Autowired
    private JumeiBrandService jumeiBrandService;

    @Autowired
    private JumeiCategoryService jumeiCategoryService;

    @Override
    public SubSystem getSubSystem() {
        return SubSystem.CMS;
    }

    @Override
    public String getTaskName() {
        return "CmsGetJmMasterJob";
    }

    @Override
    protected void onStartup(List<TaskControlBean> taskControlList) throws Exception {

        ShopBean shopBean = ShopConfigs.getShop("001", CartEnums.Cart.JM.getId());
        shopBean.setAppKey("131");
        shopBean.setSessionKey("7e059a48c30c67d2693be14275c2d3be");
        shopBean.setAppSecret("0f9e3437ca010f63f2c4f3a216b7f4bc9698f071");
        insertCategory(shopBean);
        insertBrand(shopBean);

    }

    public void insertCategory(ShopBean shopBean) throws Exception {

        List<JmCategoryBean> categorys = jumeiCategoryService.getCategoryListALL(shopBean);
        jmCategoryDao.clearJmCategory();
        jmCategoryDao.insertJmCategory(categorys);
    }

    public void insertBrand(ShopBean shopBean) throws Exception {
        List<JmBrandBean>JmBrands = jumeiBrandService.getBrands(shopBean);
        jmMasterDao.clearJmMasterByCode("0");
        List<JmMasterBean> jmMasterBeans = new ArrayList<>();
        for(JmBrandBean jmBrandBean: JmBrands){
            jmMasterBeans.add(new JmMasterBean(jmBrandBean,getTaskName()));
        }
        jmMasterDao.insertJmMaster(jmMasterBeans);
    }

}
