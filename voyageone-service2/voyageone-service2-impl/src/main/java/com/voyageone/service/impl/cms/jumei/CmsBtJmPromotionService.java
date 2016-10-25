package com.voyageone.service.impl.cms.jumei;

import com.google.common.base.Preconditions;
import com.voyageone.base.dao.mongodb.JongoQuery;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.components.transaction.VOTransactional;
import com.voyageone.common.configs.Enums.CartEnums;
import com.voyageone.common.masterdate.schema.utils.StringUtil;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.service.bean.cms.jumei.CmsBtJmPromotionSaveBean;
import com.voyageone.service.dao.cms.*;
import com.voyageone.service.dao.cms.mongo.CmsBtJmPromotionImagesDao;
import com.voyageone.service.daoext.cms.CmsBtJmPromotionDaoExt;
import com.voyageone.service.daoext.cms.CmsBtJmPromotionSpecialExtensionDaoExt;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.impl.cms.TagService;
import com.voyageone.service.model.cms.*;
import com.voyageone.service.model.cms.mongo.jm.promotion.CmsBtJmPromotionImagesModel;
import com.voyageone.service.model.util.MapModel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by "some one" on 2016/3/18.
 *
 * @version 2.8.0
 */
@Service
public class CmsBtJmPromotionService extends BaseService {

    private final CmsBtJmPromotionDao dao;
    private final CmsBtJmMasterBrandDao daoCmsBtJmMasterBrand;
    private final CmsBtJmPromotionDaoExt daoExt;
    private final TagService tagService;
    private final CmsBtPromotionDao daoCmsBtPromotion;
    private final CmsBtJmPromotionSpecialExtensionDao jmPromotionExtensionDao;
    private final CmsBtJmPromotionSpecialExtensionDaoExt jmPromotionExtensionDaoExt;
    private final CmsBtJmPromotionImagesDao jmPromotionImagesDao;
    @Autowired
    private CmsBtJmImageTemplateService jmImageTemplateService;

    private static String ORIGINAL_SCENE7_IMAGE_URL = "http://s7d5.scene7.com/is/image/sneakerhead/%s?fmt=jpg&scl=1&qlt=100";

    @Autowired
    public CmsBtJmPromotionService(CmsBtPromotionDao daoCmsBtPromotion,
                                   CmsBtJmPromotionDao dao, CmsBtJmMasterBrandDao daoCmsBtJmMasterBrand,
                                   CmsBtJmPromotionDaoExt daoExt,
                                   TagService tagService, CmsBtJmPromotionSpecialExtensionDao jmPromotionExtensionDao,
                                   CmsBtJmPromotionSpecialExtensionDaoExt jmPromotionExtensionDaoExt, CmsBtJmPromotionImagesDao jmPromotionImagesDao) {
        this.tagService = tagService;
        this.daoCmsBtPromotion = daoCmsBtPromotion;
        this.dao = dao;
        this.daoCmsBtJmMasterBrand = daoCmsBtJmMasterBrand;
        this.daoExt = daoExt;
        this.jmPromotionExtensionDao = jmPromotionExtensionDao;
        this.jmPromotionExtensionDaoExt = jmPromotionExtensionDaoExt;
        this.jmPromotionImagesDao = jmPromotionImagesDao;
    }

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

    /**
     * 取得聚美活动信息
     *
     * @param jmPromotionId 聚美活动ID (对照表cms_bt_jm_promotion.id)
     * @param hasExtInfo    是否取得专场信息和促销信息
     * @return CmsBtJmPromotionSaveBean
     */
    public CmsBtJmPromotionSaveBean getEditModel(int jmPromotionId, boolean hasExtInfo) {
        CmsBtJmPromotionSaveBean info = new CmsBtJmPromotionSaveBean();
        CmsBtJmPromotionModel model = dao.select(jmPromotionId);
        if (model == null) {
            $warn("getEditModel 查询结果为空 id=" + jmPromotionId);
            return info;
        }
        info.setModel(model);
        if (model.getRefTagId() != null && model.getRefTagId() != 0) {
            List<CmsBtTagModel> tagList = tagService.getListByParentTagId(model.getRefTagId());
            info.setTagList(tagList);
        }

        // 取得扩展信息
        if (hasExtInfo) {
            // 活动详情编辑
            info.setExtModel(jmPromotionExtensionDaoExt.selectOne(jmPromotionId));
        }

        return info;
    }

    /**
     * 新建聚美专场(保存活动信息)
     */
    @VOTransactional
    public int saveModel(CmsBtJmPromotionSaveBean parameter, String userName, String channelId) {
        parameter.getModel().setChannelId(channelId);

        // 设置品牌名
        CmsBtJmMasterBrandModel jmMasterBrand = daoCmsBtJmMasterBrand.select(parameter.getModel().getCmsBtJmMasterBrandId());
        if (jmMasterBrand != null) {
            parameter.getModel().setBrand(jmMasterBrand.getName());
        }

        if (parameter.getModel().getId() != null && parameter.getModel().getId() > 0) {
            // 更新
            if (parameter.isHasExt()) {
                setJmPromotionStepStatus(parameter.getModel().getId(), JmPromotionStepNameEnum.PromotionDetail, JmPromotionStepStatusEnum.Error, userName);
            }
            parameter.getModel().setModifier(userName);
            updateModel(parameter);
            saveCmsBtPromotion(parameter.getModel());
            if (parameter.isHasExt()) {
                // 活动详情编辑
                CmsBtJmPromotionSpecialExtensionModel extModel = parameter.getExtModel();
                // 从cms_bt_jm_promotion_special_extension表判断扩展信息是否存在
                Map<String, Object> extParam = new HashMap<>();
                extParam.put("jmpromotionId", parameter.getModel().getId());
                if (jmPromotionExtensionDao.selectCount(extParam) == 1) {
                    // 保存
                    extModel.setModifier(userName);
                    jmPromotionExtensionDaoExt.update(extModel);
                } else {
                    // 新建扩展信息
                    extModel.setCreater(userName);
                    jmPromotionExtensionDaoExt.insert(extModel);
                }
                if (parameter.getSaveType() == 1) {
                    setJmPromotionStepStatus(parameter.getModel().getId(), JmPromotionStepNameEnum.PromotionDetail, JmPromotionStepStatusEnum.Success, userName);
                }
            }
        } else {
            // 新增
            parameter.getModel().setModifier(userName);
            parameter.getModel().setCreater(userName);

            Map<String, Object> param = new HashMap<>();
            param.put("channelId", parameter.getModel().getChannelId());
            param.put("name", parameter.getModel().getName());
            List<MapModel> model = getListByWhere(param);
            if (model == null || model.size() == 0) {
                insertModel(parameter);
                // 可能存在脏数据的情况，这里先检查一遍数据是否正确
                Map<String, Object> qmap = new HashMap<>();
                qmap.put("promotionId", parameter.getModel().getId());
                qmap.put("cartId", CartEnums.Cart.JM.getValue());
                CmsBtPromotionModel promotion = daoCmsBtPromotion.selectOne(qmap);
                if (promotion != null) {
                    // 正常情况下应该没有数据
                    $error("saveModel promotion表和jm_promotion表数据冲突 promotionId=" + parameter.getModel().getId());
                    throw new BusinessException("promotion表和jm_promotion表数据冲突，请联系IT运维人员");
                }
                saveCmsBtPromotion(parameter.getModel());
            } else {
                // 活动名已存在
                throw new BusinessException("4000093");
            }
        }
        return 1;
    }

    private void saveCmsBtPromotion(CmsBtJmPromotionModel model) {
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
        if (model.getActivityStart() != null) {
            promotion.setActivityStart(DateTimeUtil.getDateTime(model.getActivityStart(), "yyyy-MM-dd HH:mm:ss"));
        }
        if (model.getActivityEnd() != null) {
            promotion.setActivityEnd(DateTimeUtil.getDateTime(model.getActivityEnd(), "yyyy-MM-dd HH:mm:ss"));
        }
        promotion.setCartId(CartEnums.Cart.JM.getValue());
        promotion.setPromotionName(model.getName());
        if (model.getPrePeriodStart() != null) {
            promotion.setPrePeriodStart(DateTimeUtil.getDateTime(model.getPrePeriodStart(), "yyyy-MM-dd HH:mm:ss"));
        }
        if (model.getPrePeriodEnd() != null) {
            promotion.setPrePeriodEnd(DateTimeUtil.getDateTime(model.getPrePeriodEnd(), "yyyy-MM-dd HH:mm:ss"));
        }

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

    private int updateModel(CmsBtJmPromotionSaveBean parameter) {
        CmsBtJmPromotionModel promotionModel = parameter.getModel();

        if (promotionModel.getRefTagId() == 0) {
            int refTagId = createPromotionTopTag(promotionModel);
            promotionModel.setRefTagId(refTagId);
        }

        int result = -1;
        if (parameter.isHasExt()) {
            // 聚美专场详细画面中的活动详情子画面来的更新
            daoExt.updateByInput(promotionModel);
        } else {
            // 活动一览画面来的更新
            dao.update(promotionModel);
        }

        setHasFeaturedModule(parameter);

        parameter.getTagList().forEach(cmsBtTagModel -> {

            cmsBtTagModel.setModifier(promotionModel.getModifier());

            if (cmsBtTagModel.getId() != null && cmsBtTagModel.getId() > 0) {
                // 要判断tag被删除的情况 tag.active == -1 表示该tag已被删除
                tagService.updateTagModel(cmsBtTagModel);
            } else {
                addChildTag(cmsBtTagModel, promotionModel);
            }

            ifNoFeaturedModuleThenUseThis(parameter, cmsBtTagModel);
        });
        return result;
    }

    private int insertModel(CmsBtJmPromotionSaveBean parameter) {
        CmsBtJmPromotionModel model = parameter.getModel();
        if (StringUtil.isEmpty(model.getCategory())) {
            model.setCategory("");
        }
        int refTagId = createPromotionTopTag(model);
        model.setRefTagId(refTagId);

        setHasFeaturedModule(parameter);

        // 子TAG追加
        parameter.getTagList().forEach(cmsBtTagModel -> {
            addChildTag(cmsBtTagModel, model);

            ifNoFeaturedModuleThenUseThis(parameter, cmsBtTagModel);
        });

        return dao.insert(model);
    }

    private void ifNoFeaturedModuleThenUseThis(CmsBtJmPromotionSaveBean parameter, CmsBtTagModel tagModel) {
        if (parameter.isHasFeaturedModule())
            return;
        parameter.setHasFeaturedModule(true);

        CmsBtTagJmModuleExtensionModel tagJmModuleExtensionModel = tagService.getJmModule(tagModel);

        if (tagJmModuleExtensionModel != null) {
            tagJmModuleExtensionModel.setFeatured(true);
            tagService.updateTagModel(tagJmModuleExtensionModel);
            return;
        }

        tagJmModuleExtensionModel = tagService.createJmModuleExtension(tagModel);
        tagJmModuleExtensionModel.setFeatured(true);

        tagService.addJmModule(tagJmModuleExtensionModel);
    }

    private void setHasFeaturedModule(CmsBtJmPromotionSaveBean parameter) {
        boolean hasFeaturedModule = tagService.hasFeaturedJmModuleByTopTagId(parameter.getModel().getRefTagId());
        parameter.setHasFeaturedModule(hasFeaturedModule);
    }

    private void addChildTag(CmsBtTagModel tagModel, CmsBtJmPromotionModel promotionModel) {

        Integer promotionTopTagId = promotionModel.getRefTagId();

        tagModel.setChannelId(promotionModel.getChannelId());
        tagModel.setParentTagId(promotionTopTagId);
        tagModel.setTagType(2);
        tagModel.setTagStatus(0);
        tagModel.setTagPathName(String.format("-%s-%s-", promotionModel.getName(), tagModel.getTagName()));
        tagModel.setTagPath("");

        tagModel.setCreater(promotionModel.getModifier());
        tagModel.setModifier(promotionModel.getModifier());

        tagService.insertCmsBtTagAndUpdateTagPath(tagModel,
                consumer -> consumer.setTagPath(String.format("-%s-%s-", promotionTopTagId, consumer.getId())));

        // 同时创建聚美的扩展模块
        CmsBtTagJmModuleExtensionModel jmModuleExtensionModel = tagService.createJmModuleExtension(tagModel);
        tagService.addJmModule(jmModuleExtensionModel);
    }

    private int createPromotionTopTag(CmsBtJmPromotionModel model) {
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

        tagService.insertCmsBtTagAndUpdateTagPath(modelTag,
                consumer -> consumer.setTagPath(String.format("-%s-", consumer.getId())));

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

    // 查询聚美活动一览
    public List<MapModel> getJmPromotionList(Map params) {
        // 过滤参数
        Map sqlParams = (Map) params.get("parameters");
        sqlParams.put("channelId", params.get("channelId"));
        convertParams(sqlParams);

        int pageIndex = (Integer) params.get("pageIndex");
        int pageRowCount = (Integer) params.get("pageRowCount");
        sqlParams.put("offset", (pageIndex - 1) * pageRowCount);
        sqlParams.put("size", pageRowCount);
        List<MapModel> promList = daoExt.getJmPromotionList(sqlParams);
        if (promList != null && promList.size() > 0) {
            // 获取频道页入口图（APP端日常专场图片）
            JongoQuery qryObj = new JongoQuery();
            qryObj.setQuery("{'jmPromotionId':#}");
            qryObj.setProjectionExt("appEntrance");

            for (MapModel promObj : promList) {
                Integer jmId = (Integer) promObj.get("id");
                qryObj.setParameters(jmId);
                CmsBtJmPromotionImagesModel imgObj = jmPromotionImagesDao.selectOneWithQuery(qryObj);
                if (imgObj != null) {
                    String imgName = StringUtils.trimToNull(imgObj.getAppEntrance());
                    if (imgName != null) {
                        if (imgObj.getUseTemplate() != null && imgObj.getUseTemplate())
                            promObj.put("entryImg", jmImageTemplateService.getUrl(imgName, "appEntrance", jmId));
                        else
                            promObj.put("entryImg", String.format(ORIGINAL_SCENE7_IMAGE_URL, imgName));
                    }
                }
            }
        }
        return promList;
    }

    public long getJmPromotionCount(Map params) {
        // 过滤参数
        Map sqlParams = (Map) params.get("parameters");
        sqlParams.put("channelId", params.get("channelId"));
        convertParams(sqlParams);

        return daoExt.getJmPromotionCount(sqlParams);
    }

    public List<Integer> selectCloseJmPromotionId(){
        return daoExt.selectCloseJmPromotionId();
    }

    public List<Integer> selectEffectiveJmPromotionId(){
        return daoExt.selectEffectiveJmPromotionId();
    }

    public List<Map<String,Object>> selectCloseJmPromotionSku(Integer jmPromotionId){
        return daoExt.selectCloseJmPromotionSku(jmPromotionId);
    }

    public int updatePromotionStatus(Integer jmPromotionId, String modifier){
        return daoExt.updatePromotionStatus(jmPromotionId,modifier);
    }

    private void convertParams(Map sqlParams) {
        // 过滤参数
        sqlParams.put("jmActId", StringUtils.trimToNull((String) sqlParams.get("jmActId")));
        sqlParams.put("jmpromName", StringUtils.trimToNull((String) sqlParams.get("jmpromName")));
        sqlParams.put("compareType", StringUtils.trimToNull((String) sqlParams.get("compareType")));
        sqlParams.put("mainCata", StringUtils.trimToNull((String) sqlParams.get("mainCata")));
        String codeListStr = StringUtils.trimToNull((String) sqlParams.get("codeList"));
        if (codeListStr != null) {
            List<String> codeList = Arrays.asList(codeListStr.split("\n"));
            codeList = codeList.stream().map(pCode -> StringUtils.trimToNull(pCode)).filter(pCode -> pCode != null).distinct().collect(Collectors.toList());
            sqlParams.put("codeList", codeList);
        } else {
            sqlParams.put("codeList", null);
        }
    }

    public static enum JmPromotionStepNameEnum {

        SessionsUpload("upload_status"),    // 专场上传
        PromotionDetail("detail_status"),   // 活动信息
        PromotionShelf("shelf_status"),    // 活动货架
        PromotionImage("image_status"),    // 活动图片
        PromotionBayWindow("bay_window_status");    // 活动飘窗

        private String name;

        JmPromotionStepNameEnum(String name) {
            this.name = name;
        }

        public String getValue() {
            return name;
        }
    }
    public static enum JmPromotionStepStatusEnum {

        Default(0),    // 初始状态
        Success(1),  // 提交完成
        Error(2),    // 未提交(暂存) 或 提交失败 /
        ImgComplete(3);  // 活动图片设计完成

        private int type;

        JmPromotionStepStatusEnum(Integer type){
            this.type = type;
        }

        public int getValue()
        {
            return  type;
        }
    }

    /**
     * 设置聚美活动各阶段的状态
     */
    public void setJmPromotionStepStatus(int jmPromId, JmPromotionStepNameEnum stepName, JmPromotionStepStatusEnum stepStatus, String userName) {
        Map<String, Object> param = new HashMap<>();
        param.put("modifier", userName);
        param.put("jmPromotionId", jmPromId);
        param.put("stepName", stepName.getValue());
        param.put("stepStatus", stepStatus.getValue());

        daoExt.setJmPromotionStepStatus(param);
    }

}
