package com.voyageone.service.impl.cms.jumei;

import com.google.common.base.Preconditions;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.components.transaction.VOTransactional;
import com.voyageone.common.configs.Enums.CartEnums;
import com.voyageone.common.masterdate.schema.utils.StringUtil;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.DateTimeUtilBeijing;
import com.voyageone.service.bean.cms.jumei.CmsBtJmPromotionSaveBean;
import com.voyageone.service.dao.cms.CmsBtJmMasterBrandDao;
import com.voyageone.service.dao.cms.CmsBtJmPromotionDao;
import com.voyageone.service.dao.cms.CmsBtPromotionDao;
import com.voyageone.service.dao.cms.CmsBtTagDao;
import com.voyageone.service.daoext.cms.CmsBtJmProductDaoExt;
import com.voyageone.service.daoext.cms.CmsBtJmPromotionDaoExt;
import com.voyageone.service.model.cms.CmsBtJmMasterBrandModel;
import com.voyageone.service.model.cms.CmsBtJmPromotionModel;
import com.voyageone.service.model.cms.CmsBtPromotionModel;
import com.voyageone.service.model.cms.CmsBtTagModel;
import com.voyageone.service.model.util.MapModel;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dell on 2016/3/18.
 */
@Service
public class CmsBtJmPromotionService {
    private static final Logger log = LoggerFactory.getLogger(CmsBtJmPromotionService.class);
    @Autowired
    CmsBtJmPromotionDao dao;
    @Autowired
    CmsBtJmMasterBrandDao daoCmsBtJmMasterBrand;
    @Autowired
    CmsBtJmPromotionDaoExt daoExt;
    @Autowired
    CmsBtTagDao daoCmsBtTag;
    @Autowired
    CmsBtPromotionDao daoCmsBtPromotion;
    @Autowired
    CmsBtJmProductDaoExt cmsBtJmProductDaoExt;
    public Map<String, Object> init() {
        Map<String, Object> map = new HashMap<>();
        List<CmsBtJmMasterBrandModel> jmMasterBrandList = daoCmsBtJmMasterBrand.selectList(new HashMap<String, Object>());
        map.put("jmMasterBrandList", jmMasterBrandList);
        return map;
    }

    public CmsBtJmPromotionModel select(int id) {
        return dao.select(id);
    }
    @VOTransactional
   public void delete(int id) {
       CmsBtJmPromotionModel model = dao.select(id);
       model.setActive(0);
       dao.update(model);
       saveCmsBtPromotion(model);
   }
    public int update(CmsBtJmPromotionModel entity) {
        return dao.update(entity);
    }

    public int insert(CmsBtJmPromotionModel entity) {
        return dao.insert(entity);
    }

    public CmsBtJmPromotionSaveBean getEditModel(int id) {
        CmsBtJmPromotionSaveBean info = new CmsBtJmPromotionSaveBean();
        CmsBtJmPromotionModel model = dao.select(id);
        info.setModel(model);
        if (model.getRefTagId()!=null&&model.getRefTagId() != 0) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("parentTagId", model.getRefTagId());
            map.put("active", 1);
            List<CmsBtTagModel> tagList = daoCmsBtTag.selectList(map);
            info.setTagList(tagList);
        }
        long preStartLocalTime = DateTimeUtilBeijing.toLocalTime(model.getPrePeriodStart());//北京时间转本地时区时间戳
        long activityEndTime = DateTimeUtilBeijing.toLocalTime(model.getActivityEnd());//北京时间转本地时区时间戳
        info.setIsBeginPre(preStartLocalTime < new Date().getTime());//活动是否看开始     用预热时间
        info.setIsEnd(activityEndTime < new Date().getTime());//活动是否结束            用活动时间
        return info;
    }
    @VOTransactional
    public int saveModel(CmsBtJmPromotionSaveBean parameter,String userName, String channelId) {
        parameter.getModel().setChannelId(channelId);
        if (parameter.getModel().getActivityAppId()==null) {
            parameter.getModel().setActivityAppId(0L);
        }
        if (parameter.getModel().getActivityPcId()==null) {
            parameter.getModel().setActivityPcId(0L);
        }
        if (com.voyageone.common.util.StringUtils.isEmpty(parameter.getModel().getBrand())) {
            parameter.getModel().setBrand("");
        }
        if (com.voyageone.common.util.StringUtils.isEmpty(parameter.getModel().getCategory())) {
            parameter.getModel().setCategory("");
        }
        if (parameter.getModel().getId() != null && parameter.getModel().getId() > 0) {//更新
            parameter.getModel().setModifier(userName);
            updateModel(parameter);
            saveCmsBtPromotion(parameter.getModel());
        } else {//新增
            parameter.getModel().setModifier(userName);
            parameter.getModel().setCreater(userName);
            Map<String,Object> param = new HashMap<>();
            param.put("channelId",parameter.getModel().getChannelId());
            param.put("name",parameter.getModel().getName());
            List<MapModel> model = getListByWhere(param);
            if(model == null || model.size() == 0){
                insertModel(parameter);
                saveCmsBtPromotion(parameter.getModel());
            }else{
                throw new BusinessException("4000093");
            }
        }
        return 1;
    }
    public void  saveCmsBtPromotion(CmsBtJmPromotionModel model) {
        Map<String, Object> map = new HashMap<>();
        map.put("promotionId", model.getId());
        map.put("cartId", CartEnums.Cart.JM.getValue());
        CmsBtPromotionModel promotion = daoCmsBtPromotion.selectOne(map);
        if (promotion == null) {
            promotion = new CmsBtPromotionModel();
        }
        promotion.setPromotionId(model.getId());
        promotion.setRefTagId(model.getRefTagId());
        promotion.setChannelId(model.getChannelId());
        promotion.setModifier(model.getModifier());
        promotion.setCreater(model.getCreater());
        promotion.setActive(model.getActive());
        promotion.setActivityStart(DateTimeUtil.getDateTime(model.getActivityStart(), "yyyy-MM-dd HH:mm:ss"));
        promotion.setActivityEnd(DateTimeUtil.getDateTime(model.getActivityEnd(), "yyyy-MM-dd HH:mm:ss"));
        promotion.setCartId(CartEnums.Cart.JM.getValue());
        promotion.setPromotionName(model.getName());
        promotion.setPrePeriodStart(DateTimeUtil.getDateTime(model.getPrePeriodStart(), "yyyy-MM-dd HH:mm:ss"));
        promotion.setPrePeriodEnd(DateTimeUtil.getDateTime(model.getPrePeriodEnd(), "yyyy-MM-dd HH:mm:ss"));
        promotion.setPromotionStatus(1);
        promotion.setTejiabaoId("");
        promotion.setIsAllPromotion(0);
        promotion.setActive(model.getActive());
        if (promotion.getId() == null || promotion.getId() == 0) {
            daoCmsBtPromotion.insert(promotion);
        } else {
            daoCmsBtPromotion.update(promotion);
        }
    }
    /*更新
    * */
    private int updateModel(CmsBtJmPromotionSaveBean parameter) {
        int result;
        CmsBtJmPromotionModel model = parameter.getModel();
        if (model.getRefTagId() == 0) {
            int refTagId = addTag(model);
            model.setRefTagId(refTagId);
        }
        result = dao.update(parameter.getModel());
        parameter.getTagList().forEach(cmsBtTagModel -> {
            cmsBtTagModel.setModifier(parameter.getModel().getModifier());
            if (cmsBtTagModel.getId() != null && cmsBtTagModel.getId() > 0) {
                daoCmsBtTag.update(cmsBtTagModel);
            } else {
                cmsBtTagModel.setChannelId(model.getChannelId());
                cmsBtTagModel.setParentTagId(model.getRefTagId());
                cmsBtTagModel.setTagType(2);
                cmsBtTagModel.setTagStatus(0);
                cmsBtTagModel.setTagPathName(String.format("-%s-%s-", model.getName(), cmsBtTagModel.getTagName()));
                cmsBtTagModel.setTagPath("");
                cmsBtTagModel.setCreater(model.getModifier());
                cmsBtTagModel.setModifier(model.getModifier());
                daoCmsBtTag.insert(cmsBtTagModel);
                cmsBtTagModel.setTagPath(String.format("-%s-%s-", cmsBtTagModel.getParentTagId(), cmsBtTagModel.getId()));
                daoCmsBtTag.update(cmsBtTagModel);
            }
        });
        return result;
    }
    /**
     * 新增
     */
    private int insertModel(CmsBtJmPromotionSaveBean parameter) {
        CmsBtJmPromotionModel model = parameter.getModel();
        if(StringUtil.isEmpty(model.getCategory()))
        {
            model.setCategory("");
        }
        int refTagId = addTag(model);
        model.setRefTagId(refTagId);
        // 子TAG追加
        parameter.getTagList().forEach(cmsBtTagModel -> {
            cmsBtTagModel.setChannelId(model.getChannelId());
            cmsBtTagModel.setParentTagId(refTagId);
            cmsBtTagModel.setTagType(2);
            cmsBtTagModel.setTagStatus(0);
            cmsBtTagModel.setTagPathName(String.format("-%s-%s-", model.getName(), cmsBtTagModel.getTagName()));
            cmsBtTagModel.setTagPath("");
            cmsBtTagModel.setCreater(model.getCreater());
            cmsBtTagModel.setModifier(model.getCreater());
            daoCmsBtTag.insert(cmsBtTagModel);
            cmsBtTagModel.setTagPath(String.format("-%s-%s-", refTagId, cmsBtTagModel.getId()));
            daoCmsBtTag.update(cmsBtTagModel);
        });
        return dao.insert(model);
    }
    private int addTag(CmsBtJmPromotionModel model) {
        CmsBtTagModel modelTag = new CmsBtTagModel();
        modelTag.setChannelId(model.getChannelId());
        modelTag.setTagName(model.getName());
        modelTag.setTagType(2);
        modelTag.setTagStatus(0);
        modelTag.setParentTagId(0);
        modelTag.setSortOrder(0);
        modelTag.setTagPath(String.format("-%s-", ""));
        modelTag.setTagPathName(String.format("-%s-", model.getName()));
        modelTag.setModifier(model.getModifier());
        //Tag追加  活动名称
        daoCmsBtTag.insert(modelTag);
        modelTag.setTagPath(String.format("-%s-", modelTag.getId()));
        daoCmsBtTag.update(modelTag);
        return modelTag.getId();
    }

    public List<MapModel> getListByWhere(Map<String, Object> map) {
        if (map.containsKey("state1") && !((Boolean) map.get("state1")))//待进行
        {
            map.remove("state1");  //小于开始时间
        }
        if (map.containsKey("state2") && !((Boolean) map.get("state2")))//进行中
        {
            map.remove("state2"); // 当前时间大于开始时间 小于结束时间
        }
        if (map.containsKey("state3") && !((Boolean) map.get("state3")))//完成
        {
            map.remove("state3"); //当前时间大于结束时间
        }
        return daoExt.selectListByWhere(map);
    }

    public List<MapModel> getJMActivePromotions(int cartId, String channelId) {
        Preconditions.checkArgument(StringUtils.isNotBlank(channelId), "channelId不能为空!");
        Map params = new HashMap<>();
        params.put("cartId", cartId);
//        if (Channels.isUsJoi(channelId)) {
//            params.put("orgChannelId", channelId);
//            params.put("channelId", ChannelConfigEnums.Channel.VOYAGEONE.getId());
//        } else {
            params.put("channelId", channelId); // TODO 在本店铺查询minimall店铺的活动时，再议，还没考虑好怎么做
//        }
        return daoExt.selectActivesOfChannel(params);
    }

}

