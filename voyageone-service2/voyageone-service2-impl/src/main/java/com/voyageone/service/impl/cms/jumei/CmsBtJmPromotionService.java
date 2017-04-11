package com.voyageone.service.impl.cms.jumei;

import com.google.common.base.Preconditions;
import com.voyageone.base.dao.mongodb.JongoQuery;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.Constants;
import com.voyageone.common.components.transaction.VOTransactional;
import com.voyageone.common.configs.Enums.CartEnums;
import com.voyageone.common.configs.TypeChannels;
import com.voyageone.common.configs.beans.TypeChannelBean;
import com.voyageone.common.masterdate.schema.utils.StringUtil;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.ListUtils;
import com.voyageone.service.bean.cms.jumei.CmsBtJmPromotionSaveBean;
import com.voyageone.service.bean.cms.jumei.ProductImportBean;
import com.voyageone.service.bean.cms.jumei.SkuImportBean;
import com.voyageone.service.bean.cms.product.CmsMtBrandsMappingBean;
import com.voyageone.service.dao.cms.*;
import com.voyageone.service.dao.cms.mongo.CmsBtJmPromotionImagesDao;
import com.voyageone.service.dao.wms.WmsBtInventoryCenterLogicDao;
import com.voyageone.service.daoext.cms.*;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.impl.cms.CmsBtJmBayWindowService;
import com.voyageone.service.impl.cms.TagService;
import com.voyageone.service.impl.cms.jumei2.CmsBtJmPromotionImportTask3Service;
import com.voyageone.service.model.cms.*;
import com.voyageone.service.model.cms.mongo.jm.promotion.CmsBtJmBayWindowModel;
import com.voyageone.service.model.cms.mongo.jm.promotion.CmsBtJmPromotionImagesModel;
import com.voyageone.service.model.cms.mongo.jm.promotion.CmsMtJmConfigModel;
import com.voyageone.service.model.util.MapModel;
import com.voyageone.service.model.wms.WmsBtInventoryCenterLogicModel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

/**
 * Created by "some one" on 2016/3/18.
 *
 * @version 2.8.0
 */
@Service
public class CmsBtJmPromotionService extends BaseService {
    private final static String ORIGINAL_SCENE7_IMAGE_URL = "http://s7d5.scene7.com/is/image/sneakerhead/%s?fmt=jpg&scl=1&qlt=100";
    private final CmsBtJmPromotionDao dao;
    private final CmsBtJmMasterBrandDao daoCmsBtJmMasterBrand;
    private final CmsBtJmPromotionDaoExt daoExt;
    private final TagService tagService;
    private final CmsBtPromotionDao daoCmsBtPromotion;
    private final CmsBtJmPromotionSpecialExtensionDao jmPromotionExtensionDao;
    private final CmsBtJmPromotionSpecialExtensionDaoExt jmPromotionExtensionDaoExt;
    private final CmsBtJmPromotionImagesDao jmPromotionImagesDao;
    private final CmsBtJmImageTemplateService jmImageTemplateService;
    private final CmsMtJmConfigService jmConfigService;
    private final CmsBtJmBayWindowService cmsBtJmBayWindowService;
    private final CmsBtJmPromotionBrandLogoDao cmsBtJmPromotionBrandLogoDao;
    CmsBtTagJmModuleExtensionDao tagJmModuleExtensionDao;
    @Autowired
    private WmsBtInventoryCenterLogicDao wmsBtInventoryCenterLogicDao;
    @Autowired
    private CmsMtBrandsMappingDaoExt brandsMappingDaoExt;
    @Autowired
    private CmsBtJmPromotionProductDaoExt cmsBtJmPromotionProductDaoExt;
    @Autowired
    private CmsBtJmPromotionSkuDaoExt cmsBtJmPromotionSkuDaoExt;
    @Autowired
    private CmsBtJmPromotionImportTask3Service cmsBtJmPromotionImportTask3Service;
    @Autowired
    private CmsBtJmPromotionTagProductDaoExt cmsBtJmPromotionTagProductDaoExt;
    @Autowired
    private CmsBtJmPromotionTagProductDao cmsBtJmPromotionTagProductDao;
    @Autowired
    private CmsBtJmPromotionImagesDao cmsBtJmPromotionImagesDao;
    @Autowired
    public CmsBtJmPromotionService(CmsBtPromotionDao daoCmsBtPromotion,
                                   CmsBtJmPromotionDao dao, CmsBtJmMasterBrandDao daoCmsBtJmMasterBrand,
                                   CmsBtJmPromotionDaoExt daoExt,
                                   TagService tagService, CmsBtJmPromotionSpecialExtensionDao jmPromotionExtensionDao,
                                   CmsBtJmPromotionSpecialExtensionDaoExt jmPromotionExtensionDaoExt,
                                   CmsBtJmPromotionImagesDao jmPromotionImagesDao,
                                   CmsBtJmImageTemplateService jmImageTemplateService,
                                   CmsMtJmConfigService jmConfigService,
                                   CmsBtJmBayWindowService cmsBtJmBayWindowService,
                                   CmsBtJmPromotionBrandLogoDao cmsBtJmPromotionBrandLogoDao,CmsBtTagJmModuleExtensionDao tagJmModuleExtensionDao) {
        this.tagService = tagService;
        this.daoCmsBtPromotion = daoCmsBtPromotion;
        this.dao = dao;
        this.daoCmsBtJmMasterBrand = daoCmsBtJmMasterBrand;
        this.daoExt = daoExt;
        this.jmPromotionExtensionDao = jmPromotionExtensionDao;
        this.jmPromotionExtensionDaoExt = jmPromotionExtensionDaoExt;
        this.jmPromotionImagesDao = jmPromotionImagesDao;
        this.jmImageTemplateService = jmImageTemplateService;
        this.jmConfigService = jmConfigService;
        this.cmsBtJmBayWindowService = cmsBtJmBayWindowService;
        this.cmsBtJmPromotionBrandLogoDao = cmsBtJmPromotionBrandLogoDao;
        this.tagJmModuleExtensionDao=tagJmModuleExtensionDao;
    }

    /**
     * 取得画面上的初始数据
     */
    public Map<String, Object> init(String channelId, String langId, boolean hasExt) {
        Map<String, Object> map = new HashMap<>();
        // 专场主品牌
        List<TypeChannelBean> brandList = TypeChannels.getTypeWithLang(Constants.comMtTypeChannel.BRAND_41, channelId, langId);
        if (brandList == null) {
            brandList = new ArrayList<>(0);
        }
        Map<String, Object> selMap = new HashMap<>();
        selMap.put("channelId", channelId);
        selMap.put("cartId", CartEnums.Cart.JM.getValue());
        List<CmsMtBrandsMappingBean> mBrandList = brandsMappingDaoExt.selectList(selMap);
        Map<String, String> brandMap = mBrandList.stream().reduce(new HashMap<String, String>(), (map1, model)->{
            map1.put(model.getCmsBrand(), model.getBrandId());
            return map1;
        }, (a,b) -> a);
        if(brandMap == null) brandMap = new HashMap<>();
        for (TypeChannelBean brandObj : brandList) {
            String brandId = brandMap.get(brandObj.getValue());
            brandObj.setName(brandId==null?"":brandId);
        }
//        for (TypeChannelBean brandObj : brandList) {
//            selMap.put("cmsBrand", brandObj.getValue());
//            List<CmsMtBrandsMappingBean> mBrandList = brandsMappingDaoExt.selectList(selMap);
//            if (mBrandList != null && mBrandList.size() > 0) {
//                brandObj.setName(mBrandList.get(0).getBrandId());
//            } else {
//                brandObj.setName("");
//            }
//        }

        map.put("jmMasterBrandList", brandList);

        // 活动场景
        CmsMtJmConfigModel configModel = jmConfigService.getCmsMtJmConfigById(CmsMtJmConfigService.JmCofigTypeEnum.promotionScene);
        List<Map<String, Object>> valList = null;
        if (configModel != null) {
            valList = configModel.getValues();
        }
        if (valList == null) {
            valList = new ArrayList<>(0);
        }
        map.put("promotionSceneList", valList);

        // 活动类型
        configModel = jmConfigService.getCmsMtJmConfigById(CmsMtJmConfigService.JmCofigTypeEnum.promotionType);
        valList = null;
        if (configModel != null) {
            valList = configModel.getValues();
        }
        if (valList == null) {
            valList = new ArrayList<>(0);
        }
        map.put("promotionTypeList", valList);

        if (hasExt) {
            // 展示平台
            configModel = jmConfigService.getCmsMtJmConfigById(CmsMtJmConfigService.JmCofigTypeEnum.displayPlatform);
            valList = null;
            if (configModel != null) {
                valList = configModel.getValues();
            }
            if (valList == null) {
                valList = new ArrayList<>(0);
            }
            map.put("displayPlatformList", valList);

            // 主频道
            configModel = jmConfigService.getCmsMtJmConfigById(CmsMtJmConfigService.JmCofigTypeEnum.mainChannel);
            valList = null;
            if (configModel != null) {
                valList = configModel.getValues();
            }
            if (valList == null) {
                valList = new ArrayList<>(0);
            }
            map.put("mainChannelList", valList);

            // 专场类型
            configModel = jmConfigService.getCmsMtJmConfigById(CmsMtJmConfigService.JmCofigTypeEnum.sessionType);
            valList = null;
            if (configModel != null) {
                valList = configModel.getValues();
            }
            if (valList == null) {
                valList = new ArrayList<>(0);
            }
            map.put("sessionTypelList", valList);

            // 关联品类，有两级类目，不必作特殊处理，ng-options能整理好数据层次
            configModel = jmConfigService.getCmsMtJmConfigById(CmsMtJmConfigService.JmCofigTypeEnum.sessionCategory);
            valList = null;
            if (configModel != null) {
                valList = configModel.getValues();
            }
            if (valList == null) {
                valList = new ArrayList<>(0);
            }
            map.put("sessionCategoryList", valList);

            // 预展示频道
            configModel = jmConfigService.getCmsMtJmConfigById(CmsMtJmConfigService.JmCofigTypeEnum.preDisplayChannel);
            valList = null;
            if (configModel != null) {
                valList = configModel.getValues();
            }
            if (valList == null) {
                valList = new ArrayList<>(0);
            }
            map.put("preDisplayChannelList", valList);

            //品牌Logo
            Map<String, Object> modelMap = new HashMap<>();
            List<CmsBtJmPromotionBrandLogoModel> listModel = cmsBtJmPromotionBrandLogoDao.selectList(modelMap);
            map.put("preDisplayBrandLogoList", listModel);

            // 直邮信息
            configModel = jmConfigService.getCmsMtJmConfigById(CmsMtJmConfigService.JmCofigTypeEnum.directmailType);
            valList = null;
            if (configModel != null) {
                valList = configModel.getValues();
            }
            if (valList == null) {
                valList = new ArrayList<>(0);
            }
            map.put("directmailTypelList", valList);
        }
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
            List<CmsBtJmPromotionSaveBean.Tag> tagList = tagService.getListByParentTagId(model.getRefTagId())
                    .stream()
                    .map(i -> {
                        CmsBtTagJmModuleExtensionModel tagJmModuleExtensionModel = tagService.getJmModule(i);
                        CmsBtJmPromotionSaveBean.Tag tag = new CmsBtJmPromotionSaveBean.Tag(i);
                        tag.setFeatured(tagJmModuleExtensionModel.getFeatured());
                        return tag;
                    })
                    .sorted((a, b) -> (a.isFeatured() ? 0 : 1) - (b.isFeatured() ? 0 : 1))
                    .collect(toList());
            info.setTagList(tagList);
        }

        // 取得扩展信息
        if (hasExtInfo) {
            // 活动详情编辑
            info.setExtModel(jmPromotionExtensionDaoExt.selectOne(jmPromotionId));
        }

        //判断是否过期
        info.getModel().setPassDated(info.getModel().getActivityEnd().getTime() < new Date().getTime());

        return info;
    }

    /**
     * 新建聚美专场(保存活动信息)
     */
    @VOTransactional
    public int saveModel(CmsBtJmPromotionSaveBean parameter, String userName, String channelId) {
        parameter.getModel().setChannelId(channelId);

        parameter.getTagList().forEach(tag -> tag.getModel().setTagPathName("-" + parameter.getModel().getName() + "-" + tag.getModel().getTagName()));

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
                int promotionId = saveCmsBtPromotion(parameter.getModel());

                if (parameter.getExtModel() != null) {
                    // 活动详情编辑
                    CmsBtJmPromotionSpecialExtensionModel extModel = parameter.getExtModel();
                    extModel.setJmpromotionId(parameter.getModel().getId());
                    extModel.setPromotionId(promotionId);
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
                }else{
                    parameter.setExtModel(new CmsBtJmPromotionSpecialExtensionModel());
                    parameter.getExtModel().setJmpromotionId(parameter.getModel().getId());
                    parameter.getExtModel().setPromotionId(promotionId);
                }
            } else {
                // 活动名已存在
                throw new BusinessException("4000093");
            }
        }
        return 1;
    }

    private int saveCmsBtPromotion(CmsBtJmPromotionModel model) {
        Map<String, Object> map = new HashMap<>();
        map.put("promotionId", model.getId());
        map.put("cartId", CartEnums.Cart.JM.getValue());
        CmsBtPromotionModel promotion = daoCmsBtPromotion.selectOne(map);
        if (promotion == null) {
            promotion = new CmsBtPromotionModel();
            promotion.setChannelId(model.getChannelId());
            promotion.setCreater(model.getCreater());
        }
        promotion.setPromotionId(model.getId());
        promotion.setRefTagId(model.getRefTagId());
        promotion.setModifier(model.getModifier());
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
        return promotion.getId();
    }

    private int updateModel(CmsBtJmPromotionSaveBean parameter) {
        CmsBtJmPromotionModel jmPromotionModel = parameter.getModel();

        if (jmPromotionModel.getRefTagId() == 0) {
            int refTagId = createPromotionTopTag(jmPromotionModel);
            jmPromotionModel.setRefTagId(refTagId);
        }

        int result = -1;
        if (parameter.isHasExt()) {
            // 聚美专场详细画面中的活动详情子画面来的更新
            if (parameter.getSaveType() == 1) {
                jmPromotionModel.setIsFstSave(1);
            }
            daoExt.updateByInput(jmPromotionModel);
        } else {
            // 活动一览画面来的更新
            daoExt.updateByInput(jmPromotionModel);
        }

        List<CmsBtTagJmModuleExtensionModel> moduleExtensionModelList = parameter.getTagList()
                .stream()
                .map(tag -> {
                    CmsBtTagModel tagModel = tag.getModel();
                    tagModel.setModifier(jmPromotionModel.getModifier());
                    CmsBtTagJmModuleExtensionModel module = null;
                    if (tagModel.getId() != null && tagModel.getId() > 0) {
                        tagService.updateTagModel(tagModel);
                        if(tagModel.getActive()==1) {
                            module = tagService.getJmModule(tagModel);
                            if (module.getModuleTitle() != tagModel.getTagName()) {
                                module.setModuleTitle(tagModel.getTagName());
                                tagService.updateTagModel(module);
                            }
                        }
                    } else {
                        addChildTag(tagModel, jmPromotionModel);
                        module = tagService.createJmModuleExtension(tagModel);
                        tagService.addJmModule(module);
                    }
                    return module;

                }).filter(f -> f != null)
                .collect(toList());

        // 更新飘窗
        CmsBtJmBayWindowModel jmBayWindowModel = cmsBtJmBayWindowService.getBayWindowByJmPromotionId(jmPromotionModel.getId());
        if (jmBayWindowModel.getFixed()) {
            List<String> bayWindowTemplateUrls = jmImageTemplateService.getBayWindowTemplateUrls();
            List<CmsBtJmBayWindowModel.BayWindow> lastBayWindows = jmBayWindowModel.getBayWindows();
            List<CmsBtJmBayWindowModel.BayWindow> bayWindowList = cmsBtJmBayWindowService.createBayWindows(moduleExtensionModelList, bayWindowTemplateUrls);
            jmBayWindowModel.setBayWindows(bayWindowList);

            if (lastBayWindows != null && !lastBayWindows.isEmpty()) {
                // 如果之前某些飘窗是配置过启用或禁用的
                // 这里需要同步过来
                Map<String, CmsBtJmBayWindowModel.BayWindow> lastBayWindowMap = lastBayWindows.stream()
                        .collect(toMap(CmsBtJmBayWindowModel.BayWindow::getName, i -> i));
                bayWindowList.forEach(i -> {
                    if (lastBayWindowMap.containsKey(i.getName()))
                        i.setEnabled(lastBayWindowMap.get(i.getName()).getEnabled());
                });
                // 上一次保存的飘窗头标题也要同步过来
                bayWindowList.get(0).setName(lastBayWindows.get(0).getName());
                bayWindowList.get(0).setUrl(lastBayWindows.get(0).getUrl());
            }

            cmsBtJmBayWindowService.update(jmBayWindowModel);
        }

        return result;
    }

    private void insertModel(CmsBtJmPromotionSaveBean parameter) {
        CmsBtJmPromotionModel model = parameter.getModel();
        if (StringUtil.isEmpty(model.getCategory())) {
            model.setCategory("");
        }
        int refTagId = createPromotionTopTag(model);
        model.setRefTagId(refTagId);

        dao.insert(model);

        // 根据 tag 创建模块
        List<CmsBtTagJmModuleExtensionModel> moduleExtensionModelList = parameter.getTagList()
                .stream()
                .map(tag -> {
                    addChildTag(tag.getModel(), model);
                    CmsBtTagJmModuleExtensionModel module = tagService.createJmModuleExtension(tag.getModel());
                    module.setFeatured(tag.isFeatured());
                    tagService.addJmModule(module);
                    return module;
                })
                .collect(toList());

        // 创建飘窗
        List<String> bayWindowTemplateUrls = jmImageTemplateService.getBayWindowTemplateUrls();
        CmsBtJmBayWindowModel jmBayWindowModel = cmsBtJmBayWindowService.createByPromotion(model, model.getModifier());
        List<CmsBtJmBayWindowModel.BayWindow> bayWindowList = cmsBtJmBayWindowService.createBayWindows(moduleExtensionModelList, bayWindowTemplateUrls);
        jmBayWindowModel.setBayWindows(bayWindowList);
        cmsBtJmBayWindowService.insert(jmBayWindowModel);
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

    /**
     * 聚美活动反场
     *
     * @param srcJmPromotionId      原来的活动ID
     * @param cmsBtJmPromotionModel
     * @param userName
     */
    public CmsBtJmPromotionSaveBean promotionCopy(int srcJmPromotionId, CmsBtJmPromotionModel cmsBtJmPromotionModel, String userName) {
        try {
            CmsBtJmPromotionImagesModel cmsBtJmPromotionImagesModel = null;

            CmsBtJmBayWindowModel srcCmsBtJmBayWindowModel = null;

            //取得源聚美活动信息
            CmsBtJmPromotionSaveBean srcJmPromotionSaveBean = getEditModel(srcJmPromotionId, true);
            if (srcJmPromotionSaveBean.getExtModel() != null) {
                // image数据取得
                cmsBtJmPromotionImagesModel = cmsBtJmPromotionImagesDao.selectJmPromotionImage(srcJmPromotionSaveBean.getExtModel().getPromotionId(), srcJmPromotionSaveBean.getExtModel().getJmpromotionId());

                srcCmsBtJmBayWindowModel = cmsBtJmBayWindowService.getBayWindowByJmPromotionId(srcJmPromotionSaveBean.getExtModel().getJmpromotionId());

            }

            Integer srcRefTagId = srcJmPromotionSaveBean.getModel().getRefTagId();
            srcJmPromotionSaveBean.getModel().setId(null);
            srcJmPromotionSaveBean.getModel().setRefTagId(0);
            srcJmPromotionSaveBean.getModel().setName(cmsBtJmPromotionModel.getName());
            srcJmPromotionSaveBean.getModel().setPromotionScene(cmsBtJmPromotionModel.getPromotionScene());
            srcJmPromotionSaveBean.getModel().setPromotionType(cmsBtJmPromotionModel.getPromotionType());
            srcJmPromotionSaveBean.getModel().setSignupDeadline(cmsBtJmPromotionModel.getSignupDeadline());
            srcJmPromotionSaveBean.getModel().setPrePeriodStart(cmsBtJmPromotionModel.getPrePeriodStart());
            srcJmPromotionSaveBean.getModel().setPrePeriodEnd(cmsBtJmPromotionModel.getPrePeriodEnd());
            srcJmPromotionSaveBean.getModel().setActivityStart(cmsBtJmPromotionModel.getActivityStart());
            srcJmPromotionSaveBean.getModel().setActivityEnd(cmsBtJmPromotionModel.getActivityEnd());
            srcJmPromotionSaveBean.getModel().setActivityAppId(cmsBtJmPromotionModel.getActivityAppId());
            srcJmPromotionSaveBean.getModel().setActivityPcId(cmsBtJmPromotionModel.getActivityPcId());
            if (srcJmPromotionSaveBean.getExtModel() != null) {
                srcJmPromotionSaveBean.setHasExt(true);
                srcJmPromotionSaveBean.getExtModel().setId(null);
                srcJmPromotionSaveBean.getExtModel().setJmpromotionId(null);
                srcJmPromotionSaveBean.getExtModel().setPromotionId(null);
                String pageId = srcJmPromotionSaveBean.getExtModel().getAppPageId();
                if(!StringUtil.isEmpty(pageId)){

                    String idTime = Long.toString(Long.parseLong(DateTimeUtil.getDateTime(new Date(), "HHmmssSSS")),32);
                    String temp[] = pageId.split("_");

                    if(temp.length > 2) {
                        srcJmPromotionSaveBean.getExtModel().setAppPageId(srcJmPromotionSaveBean.getExtModel().getAppPageId().replace(temp[temp.length - 2], idTime));
                        srcJmPromotionSaveBean.getExtModel().setPcPageId(srcJmPromotionSaveBean.getExtModel().getPcPageId().replace(temp[temp.length - 2], idTime));
                        if (temp.length > 3 && temp[temp.length - 3].indexOf("20") == 0) {
                            String idDate = DateTimeUtil.getDateTime(cmsBtJmPromotionModel.getActivityStart(), DateTimeUtil.DATE_TIME_FORMAT_3);
                            srcJmPromotionSaveBean.getExtModel().setAppPageId(srcJmPromotionSaveBean.getExtModel().getAppPageId().replace(temp[temp.length - 3], idDate));
                            srcJmPromotionSaveBean.getExtModel().setPcPageId(srcJmPromotionSaveBean.getExtModel().getPcPageId().replace(temp[temp.length - 3], idDate));
                        }
                    }

                }
            }
            srcJmPromotionSaveBean.getTagList().forEach(tag -> {
                CmsBtTagModel newTag = new CmsBtTagModel();
                newTag.setTagName(tag.getModel().getTagName());
                newTag.setActive(tag.getModel().getActive());
                tag.setModel(newTag);
            });
            saveModel(srcJmPromotionSaveBean, userName, srcJmPromotionSaveBean.getModel().getChannelId());

            //List<SkuImportBean > listSkuImport, List<Map<String, Object>> listProducctErrorMap, List<Map<String, Object>> listSkuErrorMap,String userName;
            List<ProductImportBean> listProductImport = cmsBtJmPromotionProductDaoExt.selectProductByJmPromotionId(srcJmPromotionId);
            List<SkuImportBean> listSkuImport = cmsBtJmPromotionSkuDaoExt.selectProductByJmPromotionId(srcJmPromotionId);

            // 排除没有库存的code
            List<String> availabilityCode = inventoryChk(srcJmPromotionSaveBean.getModel().getChannelId(), listProductImport);
            listProductImport = listProductImport.stream().filter(productImportBean -> availabilityCode.contains(productImportBean.getProductCode())).collect(Collectors.toList());
            listSkuImport = listSkuImport.stream().filter(skuImportBean -> availabilityCode.contains(skuImportBean.getProductCode())).collect(Collectors.toList());

            List<Map<String, Object>> listSkuErrorMap = new ArrayList<>();//;错误行集合
            List<Map<String, Object>> listProducctErrorMap = new ArrayList<>();//错误行集合
            cmsBtJmPromotionImportTask3Service.saveImport(srcJmPromotionSaveBean.getModel(), listProductImport, listSkuImport, listProducctErrorMap, listSkuErrorMap, userName, false);

            Integer desRefTagId = srcJmPromotionSaveBean.getModel().getRefTagId();

            sortProduct(srcRefTagId, desRefTagId, userName);

            if (cmsBtJmPromotionImagesModel != null) {
                cmsBtJmPromotionImagesModel.set_id(null);
                cmsBtJmPromotionImagesModel.setCreater(userName);
                cmsBtJmPromotionModel.setModifier(userName);
                cmsBtJmPromotionImagesModel.setJmPromotionId(srcJmPromotionSaveBean.getExtModel().getJmpromotionId());
                cmsBtJmPromotionImagesModel.setPromotionId(srcJmPromotionSaveBean.getExtModel().getPromotionId());
                cmsBtJmPromotionImagesDao.insert(cmsBtJmPromotionImagesModel);
            }

            if (srcCmsBtJmBayWindowModel != null) {
                CmsBtJmBayWindowModel newCmsBtJmBayWindowModel = cmsBtJmBayWindowService.getBayWindowByJmPromotionId(srcJmPromotionSaveBean.getExtModel().getJmpromotionId());
                newCmsBtJmBayWindowModel.setBayWindows(srcCmsBtJmBayWindowModel.getBayWindows());
                newCmsBtJmBayWindowModel.setFixed(srcCmsBtJmBayWindowModel.getFixed());
                cmsBtJmBayWindowService.update(newCmsBtJmBayWindowModel);
            }
            return srcJmPromotionSaveBean;
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            $error(e);
            throw new BusinessException("返场失败");
        }
    }

    @VOTransactional
    public void sortProduct(Integer srcRefTagId, Integer desRefTagId, String userName) {
        List<CmsBtTagModel> srcTag = tagService.getListByParentTagId(srcRefTagId);
        List<CmsBtTagModel> desTags = tagService.getListByParentTagId(desRefTagId);
        srcTag.forEach(srcTagMode -> {
            CmsBtTagModel desTag = desTags.stream().filter(desTagMode -> desTagMode.getTagName().equalsIgnoreCase(srcTagMode.getTagName())).findFirst().orElse(null);
            if (desTag != null) {
                List<CmsBtJmPromotionProductExtModel> srcProducts = cmsBtJmPromotionProductDaoExt.selectProductInfoByTagId2(srcTagMode.getId());
                List<CmsBtJmPromotionProductExtModel> desProducts = cmsBtJmPromotionProductDaoExt.selectProductInfoByTagId2(desTag.getId());
                List<CmsBtJmPromotionProductExtModel> newProducts = new ArrayList<CmsBtJmPromotionProductExtModel>(desProducts.size());
                srcProducts.forEach(srcJmPromotionProductExtModel -> {
                    CmsBtJmPromotionProductExtModel cmsBtJmPromotionProductExtModel = desProducts.stream().filter(desJmPromotionProductExtModel -> desJmPromotionProductExtModel.getProductCode().equalsIgnoreCase(srcJmPromotionProductExtModel.getProductCode())).findFirst().orElse(null);
                    if (cmsBtJmPromotionProductExtModel != null) {
                        newProducts.add(cmsBtJmPromotionProductExtModel);
                    }
                });

                if (!ListUtils.isNull(newProducts)) {
                    cmsBtJmPromotionTagProductDaoExt.deleteByTagId(desTag.getId());
                    newProducts.forEach(cmsBtJmPromotionProductExtModel -> {
                        CmsBtJmPromotionTagProductModel cmsBtJmPromotionTagProductModel = new CmsBtJmPromotionTagProductModel();
                        cmsBtJmPromotionTagProductModel.setChannelId(cmsBtJmPromotionProductExtModel.getChannelId());
                        cmsBtJmPromotionTagProductModel.setCmsBtTagId(desTag.getId());
                        cmsBtJmPromotionTagProductModel.setTagName(desTag.getTagName());
                        cmsBtJmPromotionTagProductModel.setCmsBtJmPromotionProductId(cmsBtJmPromotionProductExtModel.getId());
                        cmsBtJmPromotionTagProductModel.setCreater(userName);
                        cmsBtJmPromotionTagProductModel.setModifier(userName);
                        cmsBtJmPromotionTagProductDao.insert(cmsBtJmPromotionTagProductModel);
                    });
                }

                CmsBtTagJmModuleExtensionModel desJmModuleExtensionModel = tagService.getJmModule(desTag);
                CmsBtTagJmModuleExtensionModel srcJmModuleExtensionModel = tagService.getJmModule(srcTagMode);
                desJmModuleExtensionModel.setModuleTitle(srcJmModuleExtensionModel.getModuleTitle());
                desJmModuleExtensionModel.setHideFlag(srcJmModuleExtensionModel.getHideFlag());
                desJmModuleExtensionModel.setShelfType(srcJmModuleExtensionModel.getShelfType());
                desJmModuleExtensionModel.setImageType(srcJmModuleExtensionModel.getImageType());
                desJmModuleExtensionModel.setProductsSortBy(srcJmModuleExtensionModel.getProductsSortBy());
                desJmModuleExtensionModel.setNoStockToLast(srcJmModuleExtensionModel.getNoStockToLast());
                tagService.updateTagModel(desJmModuleExtensionModel);
            }
        });
    }

    public List<String> inventoryChk(String channelId, List<ProductImportBean> listProductImport) {
        List<WmsBtInventoryCenterLogicModel> wmsBtInventoryCenterLogicModels = wmsBtInventoryCenterLogicDao.getInventoryByCode(channelId, listProductImport);

        List<String> availabilityCode = wmsBtInventoryCenterLogicModels.stream().filter(inventory->inventory.getQtyChina()>0).map(WmsBtInventoryCenterLogicModel::getCode).collect(Collectors.toList());

        return availabilityCode;
    }

    public long getJmPromotionCount(Map params) {
        // 过滤参数
        Map sqlParams = (Map) params.get("parameters");
        sqlParams.put("channelId", params.get("channelId"));
        convertParams(sqlParams);

        return daoExt.getJmPromotionCount(sqlParams);
    }

    public List<Integer> selectCloseJmPromotionId() {
        return daoExt.selectCloseJmPromotionId();
    }

    public List<Integer> selectEffectiveJmPromotionId() {
        return daoExt.selectEffectiveJmPromotionId();
    }

    public List<Map<String, Object>> selectCloseJmPromotionSku(Integer jmPromotionId) {
        return daoExt.selectCloseJmPromotionSku(jmPromotionId);
    }

    public int updatePromotionStatus(Integer jmPromotionId, String modifier) {
        return daoExt.updatePromotionStatus(jmPromotionId, modifier);
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
            codeList = codeList.stream().map(pCode -> StringUtils.trimToNull(pCode)).filter(pCode -> pCode != null).distinct().collect(toList());
            sqlParams.put("codeList", codeList);
        } else {
            sqlParams.put("codeList", null);
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

    public enum JmPromotionStepNameEnum {

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

    public enum JmPromotionStepStatusEnum {

        Default(0),    // 初始状态
        Success(1),  // 提交完成
        Error(2),    // 未提交(暂存) 或 提交失败 /
        ImgComplete(3);  // 活动图片设计完成

        private int type;

        JmPromotionStepStatusEnum(Integer type) {
            this.type = type;
        }

        public int getValue() {
            return type;
        }
    }

}
