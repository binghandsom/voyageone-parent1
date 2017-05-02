/**
 * (c) Copyright Voyageone Corp 2016
 */

package com.voyageone.service.impl.cms.promotion;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.PageQueryParameters;
import com.voyageone.common.components.transaction.VOTransactional;
import com.voyageone.common.configs.Enums.CartEnums;
import com.voyageone.common.masterdate.schema.utils.StringUtil;
import com.voyageone.common.redis.CacheHelper;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.service.bean.cms.CallResult;
import com.voyageone.service.bean.cms.CmsBtPromotion.EditCmsBtPromotionBean;
import com.voyageone.service.bean.cms.CmsBtPromotion.SetPromotionStatusParameter;
import com.voyageone.service.bean.cms.CmsBtPromotionBean;
import com.voyageone.service.bean.cms.CmsBtPromotionHistoryBean;
import com.voyageone.service.bean.cms.CmsTagInfoBean;
import com.voyageone.service.dao.cms.CmsBtJmPromotionDao;
import com.voyageone.service.dao.cms.CmsBtPromotionDao;
import com.voyageone.service.dao.cms.CmsBtTagDao;
import com.voyageone.service.daoext.cms.CmsBtPromotionDaoExt;
import com.voyageone.service.daoext.cms.CmsBtPromotionDaoExtCamel;
import com.voyageone.service.daoext.cms.CmsBtTagDaoExt;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.impl.cms.TagService;
import com.voyageone.service.impl.cms.vomq.CmsMqSenderService;
import com.voyageone.service.impl.cms.vomq.vomessage.body.CmsPromotionMQMessageBody;
import com.voyageone.service.model.cms.CmsBtJmPromotionModel;
import com.voyageone.service.model.cms.CmsBtPromotionModel;
import com.voyageone.service.model.cms.CmsBtTagModel;
import com.voyageone.service.model.util.MapModel;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 *
 * @author aooer 16/01/14
 * @version 2.0.0
 */
@Service
public class PromotionService extends BaseService {

    @Autowired
    CmsBtJmPromotionDao daoCmsBtJMPromotion;
    @Autowired
    private CmsBtPromotionDaoExt cmsBtPromotionDaoExt;
    @Autowired
    private CmsBtTagDaoExt cmsBtTagDaoExt;
    @Autowired
    private TagService tagService;
    @Autowired
    private CmsBtTagDao cmsBtTagDao;
    @Autowired
    private CmsBtPromotionDao promotionDao;
    @Autowired
    private CmsBtPromotionDaoExtCamel daoExtCamelCmsBtPromotionDaoExtCamel;
    @Autowired
    private TagService serviceTag;
    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;
    @Autowired
    private CmsMqSenderService cmsMqSenderService;
    //分页 begin
    public List<MapModel> getPage(PageQueryParameters parameters) {

        List<MapModel> list = daoExtCamelCmsBtPromotionDaoExtCamel.selectPage(parameters.getSqlMapParameter());
        for (MapModel model : list) {
            loadMap(model);
        }
        return list;
    }
    void  loadMap(MapModel map) {
        CartEnums.Cart cartEnum=CartEnums.Cart.getValueByID(map.get("cartId").toString());
        if(cartEnum!=null) {
            map.put("cartName",cartEnum.name() );
        }
    }
    public long getCount(PageQueryParameters parameters) {
       return  daoExtCamelCmsBtPromotionDaoExtCamel.selectCount(parameters.getSqlMapParameter());
    }
    public EditCmsBtPromotionBean getEditModel(int PromotionId) {
        EditCmsBtPromotionBean promotionBean = new EditCmsBtPromotionBean();
        promotionBean.setPromotionModel(getByPromotionId(PromotionId));
        List<CmsBtTagModel> listTagModel = serviceTag.getListByParentTagId(promotionBean.getPromotionModel().getRefTagId());
        promotionBean.setTagList(listTagModel);
        return promotionBean;
    }
    /**
     * 添加或者修改
     */
    @VOTransactional
    public int saveEditModel(EditCmsBtPromotionBean editModel) {
        CmsBtPromotionModel cmsBtPromotionBean = editModel.getPromotionModel();
        if(2 != cmsBtPromotionBean.getTriggerType()) cmsBtPromotionBean.setTriggerTime(null);
        int result;
        if (cmsBtPromotionBean.getId() != null && cmsBtPromotionBean.getId() != 0) {
            CmsBtPromotionModel oldBtPromotion = getPromotion(cmsBtPromotionBean.getId());
            result = promotionDao.update(cmsBtPromotionBean);
            editModel.getTagList().forEach(cmsBtTagModel -> {
                cmsBtTagModel.setModifier(cmsBtPromotionBean.getModifier());
                if (cmsBtTagDao.update(cmsBtTagModel) == 0) {
                    cmsBtTagModel.setChannelId(cmsBtPromotionBean.getChannelId());
                    cmsBtTagModel.setParentTagId(cmsBtPromotionBean.getRefTagId());
                    cmsBtTagModel.setTagType(2);
                    cmsBtTagModel.setTagStatus(0);
                    cmsBtTagModel.setTagPathName(String.format("-%s-%s-", cmsBtPromotionBean.getPromotionName(), cmsBtTagModel.getTagName()));
                    cmsBtTagModel.setTagPath("");
                    cmsBtTagModel.setCreater(cmsBtPromotionBean.getModifier());
                    cmsBtTagDao.insert(cmsBtTagModel);
                    cmsBtTagModel.setTagPath(String.format("-%s-%s-", cmsBtTagModel.getParentTagId(), cmsBtTagModel.getId()));
                    cmsBtTagDao.update(cmsBtTagModel);
                }
            });

            // 判断triggerType 和 定时时间有没有变化
            if(!Objects.equals(oldBtPromotion.getTriggerType(), cmsBtPromotionBean.getTriggerType()) || !Objects.equals(oldBtPromotion.getTriggerTime(), cmsBtPromotionBean.getTriggerTime())){
                sendPromotionMq(editModel.getPromotionModel(),true, cmsBtPromotionBean.getModifier());
            }
        } else {
            editModel.getPromotionModel().setPromotionStatus(1);
            Map<String, Object> param = new HashMap<>();
            param.put("channelId", cmsBtPromotionBean.getChannelId());
            param.put("cartId", cmsBtPromotionBean.getCartId());
            param.put("promotionName", cmsBtPromotionBean.getPromotionName());
            List<CmsBtPromotionBean> promotions = cmsBtPromotionDaoExt.selectByCondition(param);
            if (promotions == null || promotions.isEmpty()) {
                result = promotionDao.insert(insertTagsAndGetNewModel(editModel).getPromotionModel());
                // 如果是定时刷价格的场合 发送一个延时mq
                if(2 == editModel.getPromotionModel().getTriggerType() && editModel.getPromotionModel().getTriggerTime() != null) {
                    sendPromotionMq(editModel.getPromotionModel(), true, cmsBtPromotionBean.getModifier());
                }
            } else {
                throw new BusinessException("4000093");
            }

        }
        return result;
    }
    /**
     * insertTagsAndGetNewModel
     */
    private EditCmsBtPromotionBean insertTagsAndGetNewModel(EditCmsBtPromotionBean editModel) {
        CmsBtPromotionModel cmsBtPromotionBean = editModel.getPromotionModel();
        CmsTagInfoBean requestModel = new CmsTagInfoBean();
        requestModel.setChannelId(cmsBtPromotionBean.getChannelId());
        requestModel.setTagName(cmsBtPromotionBean.getPromotionName());
        requestModel.setTagType(2);
        requestModel.setTagStatus(0);
        requestModel.setParentTagId(0);
        requestModel.setSortOrder(0);
        requestModel.setModifier(cmsBtPromotionBean.getModifier());
        //Tag追加
        int refTagId = tagService.addTag(requestModel);
        cmsBtPromotionBean.setRefTagId(refTagId);

        // 子TAG追加
        editModel.getTagList().forEach(cmsBtTagModel -> {
            cmsBtTagModel.setChannelId(cmsBtPromotionBean.getChannelId());
            cmsBtTagModel.setParentTagId(refTagId);
            cmsBtTagModel.setTagType(2);
            cmsBtTagModel.setTagStatus(0);
            cmsBtTagModel.setTagPathName(String.format("-%s-%s-", cmsBtPromotionBean.getPromotionName(), cmsBtTagModel.getTagName()));
            cmsBtTagModel.setTagPath("");
            cmsBtTagModel.setCreater(cmsBtPromotionBean.getCreater());
            cmsBtTagModel.setModifier(cmsBtPromotionBean.getCreater());
            cmsBtTagDao.insert(cmsBtTagModel);
            cmsBtTagModel.setTagPath(String.format("-%s-%s-", refTagId, cmsBtTagModel.getId()));
            cmsBtTagDao.update(cmsBtTagModel);
        });
        return editModel;
    }


    /**
     * 删除
     */
    @VOTransactional
    public CallResult deleteByPromotionId(int promotionId ) {
        CallResult result=new CallResult();
        CmsBtPromotionModel model = promotionDao.select(promotionId);
        if (model.getCartId() == CartEnums.Cart.JM.getValue()) {
            CmsBtJmPromotionModel jmModel = daoCmsBtJMPromotion.select(model.getPromotionId());
            if(jmModel.getStatus()==1)
            {
                result.setResult(false);
                result.setMsg("已有商品上新,不允许删除！");
                return  result;
            } else {
                jmModel.setActive(0);
                daoCmsBtJMPromotion.update(jmModel);
            }
        }
        Map<String, Object> param = new HashMap<>();
        param.put("promotionId", promotionId);
        param.put("modifier", model.getModifier());
        // 删除对应的tag
        CmsBtTagModel cmsBtTagModel = new CmsBtTagModel();
        cmsBtTagModel.setParentTagId(model.getRefTagId());
        cmsBtTagModel.setId(model.getRefTagId());
        cmsBtTagDaoExt.deleteCmsBtTagByParentTagId(cmsBtTagModel);
        cmsBtTagDaoExt.deleteCmsBtTagByTagId(cmsBtTagModel);
         cmsBtPromotionDaoExt.deleteById(param);
        return  result;
    }

    public int setPromotionStatus(SetPromotionStatusParameter parameter) {
        CmsBtPromotionModel model = promotionDao.select(parameter.getPromotionId());
        model.setPromotionStatus(parameter.getPromotionStatus());
      return   promotionDao.update(model);
    }
    //分页 end

    /**
     * 根据PromotionId查询
     *
     * @param promotionId int
     * @return CmsBtPromotionModel
     */
    public CmsBtPromotionModel getByPromotionId(int promotionId) {
        Map<String, Object> params = new HashMap<>();
        params.put("promotionId", promotionId);
        return cmsBtPromotionDaoExt.selectById(params);
    }

    public CmsBtPromotionModel getPromotion(int promotionId) {
        return promotionDao.select(promotionId);
    }

    /**
     * 根据PromotionId查询
     *
     * @param promotionId int
     * @return CmsBtPromotionModel
     */
    public CmsBtPromotionModel getByPromotionIdOrgChannelId(int promotionId, String orgChannelId) {
        Map<String, Object> params = new HashMap<>();
        params.put("promotionId", promotionId);
        params.put("orgChannelId",orgChannelId);
        return cmsBtPromotionDaoExt.selectById(params);
    }

    /**
     * 根据条件查询
     *
     * @param params Map
     * @return List<CmsBtPromotionModel>
     */
    public List<CmsBtPromotionBean> getByCondition(Map<String, Object> params) {
        return cmsBtPromotionDaoExt.selectByCondition(params);
    }

    public List<CmsBtPromotionBean> getByChannelIdCartId(String channelId, Integer cartId) {

        List<CmsBtPromotionBean> promotions = null;
        Object value = CacheHelper.getValueOperation().get("Promotion_" + channelId + "-" + cartId);
        if (value == null) {
            promotions = cmsBtPromotionDaoExt.selectByChannelIdCartId(channelId, cartId);
            if(promotions != null){
                redisTemplate.opsForValue().set("Promotion_" + channelId + "-" + cartId, promotions);
                redisTemplate.expire("Promotion_" + channelId + "-" + cartId, 1, TimeUnit.MINUTES);
            }
        }else{
            redisTemplate.expire("Promotion_" + channelId + "-" + cartId, 1, TimeUnit.MINUTES);
            promotions = (List<CmsBtPromotionBean>) value;
        }

        return promotions;
    }

    /**
     * 根据channelId获取promotion列表
     */
    public List<CmsBtPromotionBean> getPromotionsByChannelId(String channelId, Map<String, Object> params) {
        Map<String, Object> paramsTmp =  params;
        if (paramsTmp == null) {
            paramsTmp = new HashMap<>();
        }
//        if(Channels.isUsJoi(channelId)){
//            paramsTmp.put("orgChannelId", channelId);
//            paramsTmp.put("channelId", ChannelConfigEnums.Channel.VOYAGEONE.getId());
//        } else {
            paramsTmp.put("channelId", channelId); // TODO 在本店铺查询minimall店铺的活动时，再议，还没考虑好怎么做
//        }
        return this.getByCondition(paramsTmp);
    }

    /**
     * 根据channelId获取promotion列表，只查询有效的活动信息(除了状态外，必须要有tag信息)
     */
    public List<CmsBtPromotionBean> getPromotions4AdvSearch(String channelId, Map<String, Object> params) {
        if (params == null) {
            params = new HashMap<>();
        }
//        if (Channels.isUsJoi(channelId)) {
//            params.put("orgChannelId", channelId);
//            params.put("channelId", ChannelConfigEnums.Channel.VOYAGEONE.getId());
//        } else {
            params.put("channelId", channelId); // TODO 在本店铺查询minimall店铺的活动时，再议，还没考虑好怎么做
//        }
        return cmsBtPromotionDaoExt.select4AdvSearch(params);
    }

    /**
     * getPromotionLogMap
     */
    public Map<String, Object> getPromotionHistory(Map<String, Object> params) {
        Map<String, Object> result = new HashMap<>();
        List<CmsBtPromotionHistoryBean> promotionList = cmsBtPromotionDaoExt.selectPromotionHistory(params);
        int count = cmsBtPromotionDaoExt.selectPromotionHistoryCnt(params);
        result.put("list", promotionList);
        result.put("total", count);
        return result;
    }

    /**
     * 取得按Cart进行分类的活动详情
     * @param params 查询参数
     * @return 活动详情列表
     */
    public Map<String, List<Map<String, Object>>> getUnduePromotion(Map<String, Object> params) {
    	Map<String, List<Map<String, Object>>> result = new HashMap<String, List<Map<String, Object>>>();
    	List<CmsBtPromotionHistoryBean> promotionList = cmsBtPromotionDaoExt.selectUnduePromotion(params);
    	if (CollectionUtils.isNotEmpty(promotionList)) {
    		// 把全部的检索结果按不同的Cart进行分类
    		for (CmsBtPromotionHistoryBean promotion : promotionList) {
    			Map<String, Object> promotionMap = new HashMap<String, Object>();
    			promotionMap.put("activityStart", promotion.getActivityStart());
    			promotionMap.put("activityEnd", promotion.getActivityEnd());
    			promotionMap.put("promotionName", promotion.getPromotionName());
    			if (result.get(promotion.getShortName()) == null) {
    				List<Map<String, Object>> cartPromotionList = new ArrayList<Map<String, Object>>();
    				cartPromotionList.add(promotionMap);
    				result.put(promotion.getShortName(), cartPromotionList);
    			} else {
    				result.get(promotion.getShortName()).add(promotionMap);
    			}
    		}
    	}

    	return result;
    }

    /**
     * 添加或者修改
     */
    @VOTransactional
    public int saveOrUpdate(CmsBtPromotionBean cmsBtPromotionBean) {
        int result;
        if (cmsBtPromotionBean.getId() != null  &&  cmsBtPromotionBean.getId() != 0) {
            result = cmsBtPromotionDaoExt.update(cmsBtPromotionBean);
            cmsBtPromotionBean.getTagList().forEach(cmsBtTagModel -> {
                cmsBtTagModel.setModifier(cmsBtPromotionBean.getModifier());
                if (cmsBtTagDao.update(cmsBtTagModel) == 0) {
                    cmsBtTagModel.setChannelId(cmsBtPromotionBean.getChannelId());
                    cmsBtTagModel.setParentTagId(cmsBtPromotionBean.getRefTagId());
                    cmsBtTagModel.setTagType(2);
                    cmsBtTagModel.setTagStatus(0);
                    cmsBtTagModel.setTagPathName(String.format("-%s-%s-", cmsBtPromotionBean.getPromotionName(), cmsBtTagModel.getTagName()));
                    cmsBtTagModel.setTagPath("");
                    cmsBtTagModel.setCreater(cmsBtPromotionBean.getModifier());
//                    cmsBtTagModel.setModifier(cmsBtPromotionBean.getModifier());
                    cmsBtTagDao.insert(cmsBtTagModel);
                    cmsBtTagModel.setTagPath(String.format("-%s-%s-", cmsBtTagModel.getParentTagId(), cmsBtTagModel.getId()));
                    cmsBtTagDao.update(cmsBtTagModel);
                }
            });
        } else {
            Map<String,Object> param = new HashMap<>();
            param.put("channelId",cmsBtPromotionBean.getChannelId());
            param.put("cartId",cmsBtPromotionBean.getCartId());
            param.put("promotionName",cmsBtPromotionBean.getPromotionName());
            List<CmsBtPromotionBean> promotions = cmsBtPromotionDaoExt.selectByCondition(param);
            if(promotions == null || promotions.isEmpty()){
                result = cmsBtPromotionDaoExt.insert(insertTagsAndGetNewModel(cmsBtPromotionBean));
            }else{
                throw new BusinessException("4000093");
            }

        }
        return result;
    }

    /**
     * insertTagsAndGetNewModel
     */
    private CmsBtPromotionBean insertTagsAndGetNewModel(CmsBtPromotionBean cmsBtPromotionBean) {
        CmsTagInfoBean requestModel = new CmsTagInfoBean();
        requestModel.setChannelId(cmsBtPromotionBean.getChannelId());
        requestModel.setTagName(cmsBtPromotionBean.getPromotionName());
        requestModel.setTagType(2);
        requestModel.setTagStatus(0);
        requestModel.setParentTagId(0);
        requestModel.setSortOrder(0);
        requestModel.setModifier(cmsBtPromotionBean.getModifier());
        //Tag追加
        int refTagId = tagService.addTag(requestModel);
        cmsBtPromotionBean.setRefTagId(refTagId);

        // 子TAG追加
        cmsBtPromotionBean.getTagList().forEach(cmsBtTagModel -> {
            cmsBtTagModel.setChannelId(cmsBtPromotionBean.getChannelId());
            cmsBtTagModel.setParentTagId(refTagId);
            cmsBtTagModel.setTagType(2);
            cmsBtTagModel.setTagStatus(0);
            cmsBtTagModel.setTagPathName(String.format("-%s-%s-", cmsBtPromotionBean.getPromotionName(), cmsBtTagModel.getTagName()));
            cmsBtTagModel.setTagPath("");
            cmsBtTagModel.setCreater(cmsBtPromotionBean.getCreater());
            cmsBtTagModel.setModifier(cmsBtPromotionBean.getCreater());
            cmsBtTagDao.insert(cmsBtTagModel);
            cmsBtTagModel.setTagPath(String.format("-%s-%s-", refTagId, cmsBtTagModel.getId()));
            cmsBtTagDao.update(cmsBtTagModel);
        });
        return cmsBtPromotionBean;
    }

    /**
     * 删除
     */
    @VOTransactional
    public int delete(CmsBtPromotionBean cmsBtPromotionBean) {
        Map<String, Object> param = new HashMap<>();
        param.put("promotionId", cmsBtPromotionBean.getId());
        param.put("modifier", cmsBtPromotionBean.getModifier());

        // 删除对应的tag
        CmsBtTagModel cmsBtTagModel = new CmsBtTagModel();
        cmsBtTagModel.setParentTagId(cmsBtPromotionBean.getRefTagId());
        cmsBtTagModel.setId(cmsBtPromotionBean.getRefTagId());
        cmsBtTagDaoExt.deleteCmsBtTagByParentTagId(cmsBtTagModel);
        cmsBtTagDaoExt.deleteCmsBtTagByTagId(cmsBtTagModel);

        return cmsBtPromotionDaoExt.deleteById(param);
    }

    public Map<String,Object>  getPromotionIDByCartId(String promotionId) {
        return cmsBtPromotionDaoExt.selectPromotionIDByCartId(promotionId);
    }
    public CmsBtPromotionModel getCmsBtPromotionModelByJmPromotionId(int jmPromotionId) {
        Map<String, Object> map = new HashMap<>();
        map.put("promotionId", jmPromotionId);
        map.put("cartId", CartEnums.Cart.JM.getValue());
        CmsBtPromotionModel promotion = promotionDao.selectOne(map);
        return promotion;
    }

    /**
     * 判断当前日期是否存在有效活动(正处于活动中, 活动结束多少天内, 多少天以后活动才开始)
     * @param promotion 活动列表
     * @param date      判断日期
     * @param rangeBef  判断日期前多少天以内有活动刚结束
     * @param rangeAft  判断日期后托少天以内有活动未开始
     * @return List<Integer>
     */
    public List<Integer>  getDateRangePromotionIds(List<CmsBtPromotionBean> promotion, Date date, String rangeBef, String rangeAft){

        Integer rangeA = StringUtil.isEmpty(rangeBef)?0:Integer.parseInt(rangeBef);
        Integer rangeB = StringUtil.isEmpty(rangeAft)?0:Integer.parseInt(rangeAft);
        String dateNow = DateTimeUtil.format(DateTimeUtil.addHours(date, 8), DateTimeUtil.DEFAULT_DATETIME_FORMAT);
        String dateBef = DateTimeUtil.format(DateTimeUtil.addHours(date, 8 + rangeB * -24), DateTimeUtil.DEFAULT_DATETIME_FORMAT);
        String dateAft = DateTimeUtil.format(DateTimeUtil.addHours(date, 8 + rangeA * 24), DateTimeUtil.DEFAULT_DATETIME_FORMAT);

        List<Integer> promotionIds = promotion.stream()
                .filter(cmsBtPromotionBean -> {
                            if (cmsBtPromotionBean.getIsAllPromotion() != 1) {
                                if (cmsBtPromotionBean.getActivityStart().compareTo(dateNow) >= 0 && cmsBtPromotionBean.getActivityStart().compareTo(dateAft) <= 0) {
                                    return true;
                                } else if (cmsBtPromotionBean.getActivityEnd().compareTo(dateBef) >= 0 && cmsBtPromotionBean.getActivityEnd().compareTo(dateNow) <= 0) {
                                    return true;
                                } else if (cmsBtPromotionBean.getActivityStart().compareTo(dateNow) <= 0 && cmsBtPromotionBean.getActivityEnd().compareTo(dateNow) >= 0) {
                                    return true;
                                }
                            }
                            return false;
                        }
                )
                .map(CmsBtPromotionBean::getId)
                .collect(Collectors.toList());
        return promotionIds;
    }

    /**
     * 判断当前日期是否存在有效活动(正处于活动中, 活动还未开始)
     * @param promotion 活动列表
     * @param date 判断日期
     * @return List<Integer>
     */
    public List<Integer>  getDateRangePromotionIds(List<CmsBtPromotionBean> promotion, Date date){

        String dateNow = DateTimeUtil.format(DateTimeUtil.addHours(date, 8), DateTimeUtil.DEFAULT_DATETIME_FORMAT);

        List<Integer> promotionIds = promotion.stream()
                .filter(cmsBtPromotionBean -> {
                            // 活动有效,并且未开始, 或者 当前有活动
                            if (cmsBtPromotionBean.getIsAllPromotion() != 1
                                    && (cmsBtPromotionBean.getActivityStart().compareTo(dateNow) >= 0
                                        || (cmsBtPromotionBean.getActivityStart().compareTo(dateNow) <= 0
                                            && cmsBtPromotionBean.getActivityEnd().compareTo(dateNow) >= 0))) {
                                return true;
                            }
                            return false;
                        }
                )
                .map(CmsBtPromotionBean::getId)
                .collect(Collectors.toList());
        return promotionIds;
    }

    public void sendPromotionMq(int promotionId, boolean sendDelayMq, String modifier){
        CmsBtPromotionModel cmsBtPromotionModel = getByPromotionId(promotionId);
        if(cmsBtPromotionModel != null){
            sendPromotionMq(cmsBtPromotionModel, sendDelayMq, modifier);
        }
    }
    public void sendPromotionMq(CmsBtPromotionModel promotion, boolean sendDelayMq, String modifier){
        // 触发机制是实时场合
        if(1 == promotion.getTriggerType()){
            CmsPromotionMQMessageBody cmsPromotionMQMessageBody = new CmsPromotionMQMessageBody();
            cmsPromotionMQMessageBody.setChannelId(promotion.getChannelId());
            cmsPromotionMQMessageBody.setSender(modifier);
            cmsPromotionMQMessageBody.setPromotionId(promotion.getId());
            cmsMqSenderService.sendMessage(cmsPromotionMQMessageBody);
            //     触发机制是定时场合
        }else if(2 == promotion.getTriggerType() && promotion.getTriggerTime() != null){
            CmsPromotionMQMessageBody cmsPromotionMQMessageBody = new CmsPromotionMQMessageBody();
            cmsPromotionMQMessageBody.setChannelId(promotion.getChannelId());
            cmsPromotionMQMessageBody.setSender(modifier);
            cmsPromotionMQMessageBody.setPromotionId(promotion.getId());
            cmsPromotionMQMessageBody.setTriggerTime(promotion.getTriggerTime().getTime());
            //当前
            Long delaySecond = promotion.getTriggerTime().getTime() - DateTimeUtil.getNowTimeStampLong();
            if(delaySecond > 0){
                if(sendDelayMq) {
                    cmsMqSenderService.sendMessage(cmsPromotionMQMessageBody, delaySecond / 1000);
                }
            }else{
                cmsMqSenderService.sendMessage(cmsPromotionMQMessageBody);
            }
        }
    }
}
