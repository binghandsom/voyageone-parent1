package com.voyageone.service.impl.cms;

import com.voyageone.common.configs.Enums.CacheKeyEnums;
import com.voyageone.common.masterdate.schema.field.Field;
import com.voyageone.common.masterdate.schema.utils.StringUtil;
import com.voyageone.common.redis.CacheHelper;
import com.voyageone.common.util.ListUtils;
import com.voyageone.common.util.baidu.translate.BaiduTranslateUtil;
import com.voyageone.service.dao.cms.mongo.CmsBtCustomPropDao;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.impl.cms.feed.FeedCategoryAttributeService;
import com.voyageone.service.impl.cms.feed.FeedInfoService;
import com.voyageone.service.model.cms.mongo.CmsBtCustomPropModel;
import com.voyageone.service.model.cms.mongo.CmsBtTranslateModel;
import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedInfoModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Created by james on 2017/2/21.
 */
@Service
public class CmsBtCustomPropService extends BaseService {

    private final CmsBtCustomPropDao cmsBtCustomPropDao;

    private final
    FeedCategoryAttributeService feedCategoryAttributeService;

    private final
    CmsBtTranslateService cmsBtTranslateService;

    private final
    FeedInfoService feedInfoService;

    private final String CacheKey = CacheKeyEnums.KeyEnum.CmsCacheData_Feed_Attribute.name();


    @Autowired
    public CmsBtCustomPropService(CmsBtCustomPropDao cmsBtCustomPropDao, FeedCategoryAttributeService feedCategoryAttributeService, CmsBtTranslateService cmsBtTranslateService, FeedInfoService feedInfoService) {
        this.cmsBtCustomPropDao = cmsBtCustomPropDao;
        this.feedCategoryAttributeService = feedCategoryAttributeService;
        this.cmsBtTranslateService = cmsBtTranslateService;
        this.feedInfoService = feedInfoService;
    }


    /**
     * 检索 自定义属性（不带继承）
     *
     * @param channelId    channelId 渠道
     * @param orgChannelId 子渠道的channel  旗舰店的场合 等于 channelId
     * @param cat          类目路径
     * @return 自定义属性
     */
    public CmsBtCustomPropModel getCustomPropByCatChannel(String channelId, String orgChannelId, String cat) {
        cat = StringUtil.isEmpty(cat) ? "all" : cat;
        return cmsBtCustomPropDao.getCustomPropByCatChannel(channelId, orgChannelId, cat);
    }

    /**
     * 更新/新建 cmsBtCustomProp 数据
     *
     * @param cmsBtCustomPropModel cmsBtCustomProp
     */
    public void update(CmsBtCustomPropModel cmsBtCustomPropModel) {
        if (cmsBtCustomPropModel.get_id() != null) {
            cmsBtCustomPropDao.update(cmsBtCustomPropModel);
        } else {
            CmsBtCustomPropModel old = getCustomPropByCatChannel(cmsBtCustomPropModel.getChannelId(), cmsBtCustomPropModel.getOrgChannelId(), cmsBtCustomPropModel.getCat());
            if (old == null) {
                cmsBtCustomPropDao.insert(cmsBtCustomPropModel);
            } else {
                cmsBtCustomPropModel.set_id(old.get_id());
                cmsBtCustomPropDao.update(cmsBtCustomPropModel);
            }
        }
    }

    /**
     * 根据类目和channel 获取第三放属性 带继承关系的
     *
     * @param channelId channelId
     * @param cat       类目
     * @return CmsBtCustomPropModel
     */
    public CmsBtCustomPropModel getCustomPropByCatChannelExtend(String channelId, String orgChannelId, String cat) {
        cat = StringUtil.isEmpty(cat) ? "all" : cat;
        List<CmsBtCustomPropModel> cmsBtCustomPropModels = new ArrayList<>();
        CmsBtCustomPropModel cmsBtCustomProp = new CmsBtCustomPropModel();
        String cats[] = cat.split(">");
        String catPath = "";
        // 先找以下不带orgChannelId设置
        if (!StringUtil.isEmpty(orgChannelId)) {
            cmsBtCustomPropModels.addAll(getCustomPropByCatPath(channelId, "", cat));
        }
        cmsBtCustomPropModels.addAll(getCustomPropByCatPath(channelId, orgChannelId, cat));

        if (cmsBtCustomPropModels.size() > 0) {
            Collections.reverse(cmsBtCustomPropModels);
            if (cmsBtCustomPropModels.size() > 0) {
                cmsBtCustomProp = cmsBtCustomPropModels.get(0);
                for (int i = 1; i < cmsBtCustomPropModels.size(); i++) {
                    merge(cmsBtCustomProp, cmsBtCustomPropModels.get(i));
                }
            }
        }

        rearrange(cmsBtCustomProp);
        //排除删除的属性
        cmsBtCustomProp.setEntitys(cmsBtCustomProp.getEntitys().stream().filter(entity -> entity.getActive() == null || 1 == entity.getActive()).collect(Collectors.toList()));
        return cmsBtCustomProp;
    }

    /**
     * 设置属性是否打勾
     *
     * @param channelId    channelId
     * @param orgChannelId orgChannelId
     * @param cat          类目
     * @param entity       entity
     * @return CmsBtCustomPropModel
     */
    public CmsBtCustomPropModel setCustomshIsDispPlay(String channelId, String orgChannelId, String cat, CmsBtCustomPropModel.Entity entity) {
        cat = StringUtil.isEmpty(cat) ? "all" : cat;
        CmsBtCustomPropModel cmsBtCustomPropExtend = getCustomPropByCatChannelExtend(channelId, orgChannelId, cat);
        CmsBtCustomPropModel cmsBtCustomPropModel = getCustomPropByCatChannel(channelId, orgChannelId, cat);
        if (cmsBtCustomPropModel == null) {
            cmsBtCustomPropModel = new CmsBtCustomPropModel();
            cmsBtCustomPropModel.setCat(cat);
            cmsBtCustomPropModel.setChannelId(channelId);
            cmsBtCustomPropModel.setOrgChannelId(orgChannelId);
        }
        CmsBtCustomPropModel.Entity item = cmsBtCustomPropModel.getEntitys().stream()
                .filter(entity1 -> entity1.getNameEn().equalsIgnoreCase(entity.getNameEn()) && Objects.equals(entity.getType(), entity1.getType()))
                .findFirst().orElse(null);
        if (item == null) {
            item = new CmsBtCustomPropModel.Entity();
            item.setNameCn(entity.getNameCn());
            item.setNameEn(entity.getNameEn());
            item.setType(entity.getType());
            item.setChecked(entity.getChecked());
            item.setActive(entity.getActive());
            cmsBtCustomPropModel.getEntitys().add(item);
        } else {
            item.setChecked(entity.getChecked());
            item.setActive(entity.getActive());
        }
        cmsBtCustomPropModel.setSort(cmsBtCustomPropExtend.getSort());
        if (entity.getChecked()) {
            cmsBtCustomPropModel.getSort().add(entity.getNameEn());
        } else {
            cmsBtCustomPropModel.getSort().remove(entity.getNameEn());
        }
        update(cmsBtCustomPropModel);
        return getCustomPropByCatChannelExtend(channelId, orgChannelId, cat);
    }

    /**
     * 更新属性的设置
     *
     * @param channelId    channelId
     * @param orgChannelId orgChannelId
     * @param cat          类目
     * @param entity       entity
     * @return CmsBtCustomPropModel
     */
    public CmsBtCustomPropModel updateEntity(String channelId, String orgChannelId, String cat, CmsBtCustomPropModel.Entity entity) {
        cat = StringUtil.isEmpty(cat) ? "all" : cat;
        CmsBtCustomPropModel cmsBtCustomPropModel = getCustomPropByCatChannel(channelId, orgChannelId, cat);
        if (cmsBtCustomPropModel == null) {
            cmsBtCustomPropModel = new CmsBtCustomPropModel();
            cmsBtCustomPropModel.setCat(cat);
            cmsBtCustomPropModel.setChannelId(channelId);
            cmsBtCustomPropModel.setOrgChannelId(orgChannelId);
        }
        CmsBtCustomPropModel.Entity item = cmsBtCustomPropModel.getEntitys().stream()
                .filter(entity1 -> entity1.getNameEn().equalsIgnoreCase(entity.getNameEn()) && Objects.equals(entity.getType(), entity1.getType()))
                .findFirst().orElse(null);
        if (item == null) {
            item = new CmsBtCustomPropModel.Entity();

            item.setNameEn(entity.getNameEn());
            cmsBtCustomPropModel.getEntitys().add(item);
        }
        item.setNameCn(entity.getNameCn());
        item.setType(entity.getType());
        item.setValue(entity.getValue());
        item.setAttributeType(entity.getAttributeType());
        item.setActive(entity.getActive());
        update(cmsBtCustomPropModel);
        return getCustomPropByCatChannelExtend(channelId, orgChannelId, cat);
    }

    /**
     * 删除指定的类目下的某一个属性
     *
     * @param channelId    channelId
     * @param orgChannelId orgChannelId
     * @param cat          类目
     * @param entity       entity
     * @return 新的结果
     */
    public CmsBtCustomPropModel removeEntity(String channelId, String orgChannelId, String cat, CmsBtCustomPropModel.Entity entity) {
        cat = StringUtil.isEmpty(cat) ? "all" : cat;
        CmsBtCustomPropModel cmsBtCustomPropModel = getCustomPropByCatChannel(channelId, orgChannelId, cat);
        if (cmsBtCustomPropModel == null) {
            cmsBtCustomPropModel = new CmsBtCustomPropModel();
            cmsBtCustomPropModel.setCat(cat);
            cmsBtCustomPropModel.setChannelId(channelId);
            cmsBtCustomPropModel.setOrgChannelId(orgChannelId);
        }
        CmsBtCustomPropModel.Entity item = cmsBtCustomPropModel.getEntitys().stream()
                .filter(entity1 -> entity1.getNameEn().equalsIgnoreCase(entity.getNameEn()) && Objects.equals(entity.getType(), entity1.getType()))
                .findFirst().orElse(null);
        if (item == null) {
            item = new CmsBtCustomPropModel.Entity();
            item.setNameCn(entity.getNameCn());
            item.setNameEn(entity.getNameEn());
            item.setActive(0);
            cmsBtCustomPropModel.getEntitys().add(item);
        } else {
            item.setNameCn(entity.getNameCn());
            item.setActive(0);
        }
        update(cmsBtCustomPropModel);
        return getCustomPropByCatChannelExtend(channelId, orgChannelId, cat);
    }

    /**
     * 更新顺序
     *
     * @param channelId    channelId
     * @param orgChannelId orgChannelId
     * @param cat          类目
     * @param sort         顺序
     * @return CmsBtCustomPropModel
     */
    public CmsBtCustomPropModel setSort(String channelId, String orgChannelId, String cat, List<String> sort) {
        cat = StringUtil.isEmpty(cat) ? "all" : cat;
        CmsBtCustomPropModel cmsBtCustomPropModel = getCustomPropByCatChannel(channelId, orgChannelId, cat);
        if (cmsBtCustomPropModel == null) {
            cmsBtCustomPropModel = new CmsBtCustomPropModel();
            cmsBtCustomPropModel.setCat(cat);
            cmsBtCustomPropModel.setChannelId(channelId);
            cmsBtCustomPropModel.setOrgChannelId(orgChannelId);
        }
        cmsBtCustomPropModel.setSort(sort);
        update(cmsBtCustomPropModel);
        return getCustomPropByCatChannelExtend(channelId, orgChannelId, cat);
    }

    /**
     * 给产品数据设置feed数据
     *
     * @param product 产品model
     */
    public void setProductCustomProp(CmsBtProductModel product) {

        CmsBtCustomPropModel cmsBtCustomPropModel = getCustomPropByCatChannelExtend(product.getChannelId(), product.getOrgChannelId(), product.getCommon().getCatPath());

        CmsBtFeedInfoModel cmsBtFeedInfoModel = feedInfoService.getProductByCode(product.getOrgChannelId(), product.getCommon().getFields().getOriginalCode());

        for (CmsBtCustomPropModel.Entity entity : cmsBtCustomPropModel.getEntitys()) {
            if (entity.getAttributeType() != null) {
                String value = (String) product.getFeed().getOrgAtts().get(entity.getNameEn());
                String valueEn = value;
                // 取出英文值
                if (StringUtil.isEmpty(value)) {
                    valueEn = getEntityEnValue(product.getChannelId(), entity, product, cmsBtFeedInfoModel);
                    product.getFeed().getOrgAtts().put(entity.getNameEn(), valueEn);
                }

                // 取出中文值
                value = (String) product.getFeed().getCnAtts().get(entity.getNameEn());
                String valueCn="";
                if (StringUtil.isEmpty(value)) {
                    valueCn = getEntityCnValue(product.getChannelId(), entity, valueEn);
                    product.getFeed().getCnAtts().put(entity.getNameEn(), valueCn);
                }
            }
        }

        List<String> enCustom = new ArrayList<>();
        List<String> cnCustom = new ArrayList<>();
        Map<String, String> attsName = new HashMap<>();

        // 找出打勾并且有中文值的属性名
        cmsBtCustomPropModel.getEntitys().stream().filter(entity -> entity.getChecked() != null && entity.getChecked() && !StringUtil.isEmpty((String) product.getFeed().getCnAtts().get(entity.getNameEn()))).forEach(entity -> {
            enCustom.add(entity.getNameEn());
            cnCustom.add(StringUtil.isEmpty(entity.getNameCn()) ? entity.getNameEn() : entity.getNameCn());
        });

        cmsBtCustomPropModel.getEntitys().stream().filter(entity -> !StringUtil.isEmpty(entity.getNameCn())).forEach(entity -> {
            attsName.put(entity.getNameEn(), entity.getNameCn());
        });

        product.getFeed().setCustomIds(enCustom);
        product.getFeed().setCustomIdsCn(cnCustom);
        product.getFeed().setAttsName(attsName);
    }
    private String getEntityEnValue(String channelId, CmsBtCustomPropModel.Entity entity, CmsBtProductModel cmsBtProduct, CmsBtFeedInfoModel feedInfo) {
        String valueEn = "";
        switch (entity.getType()) {
            case 1:
            case 2:
                if (!StringUtil.isEmpty(entity.getValue())) {
                    valueEn = cmsBtProduct.getCommonNotNull().getFieldsNotNull().getStringAttribute(entity.getValue());
                }
                break;
            case 3:
                if(feedInfo != null) {
                    List<String> values = feedInfo.getAttribute().get(entity.getValue());
                    if (!ListUtils.isNull(values)) {
                        valueEn = values.get(0);
                    }
                }
                break;
            case 4:
                if(entity.getAttributeType() == 2){
                    if (!StringUtil.isEmpty(entity.getValue())) {
                        valueEn =  cmsBtProduct.getCommonNotNull().getFieldsNotNull().getStringAttribute(entity.getValue());
                    }
                    break;
                }else if(entity.getAttributeType() == 3){
                    valueEn =  entity.getValue();
                }
                break;
        }
        return valueEn;
    }
    private String getEntityCnValue(String channelId, CmsBtCustomPropModel.Entity entity, String value) {
        if(StringUtil.isEmpty(value)) return "";
        switch (entity.getAttributeType()) {
            case 1:
                CmsBtTranslateModel cmsBtTranslateModel = cmsBtTranslateService.get(channelId, entity.getType(), entity.getNameEn(), value);
                if (cmsBtTranslateModel != null && !StringUtil.isEmpty(cmsBtTranslateModel.getValueCn())) {
                    return cmsBtTranslateModel.getValueCn();
                } else {
                    String valueCn = "";
                    if (value.length() < 50) {
                        try {
                            valueCn = BaiduTranslateUtil.translate(value);
                        } catch (Exception e) {
                            $warn(Arrays.toString(e.getStackTrace()));
                        }
                        if (!StringUtil.isEmpty(valueCn)) {
                            cmsBtTranslateService.create(channelId, entity.getType(), entity.getNameEn(), value, valueCn);
                        }
                    }
                    return valueCn;
                }
            case 2:
            case 3:
                    return value;
        }
        return "";
    }

    private List<CmsBtCustomPropModel> getCustomPropByCatPath(String channelId, String orgChannelId, String cat) {
        List<CmsBtCustomPropModel> cmsBtCustomPropModels = new ArrayList<>();
        String cats[] = cat.split(">");
        String catPath = "";
        CmsBtCustomPropModel item = null;

        // 先去第三方的feed属性一览
        if (!StringUtil.isEmpty(orgChannelId)) {
            item = getFeedAttributeName(orgChannelId);
        }
        if (item != null) {
            cmsBtCustomPropModels.add(item);
        }

        // 再去全类目的属性一栏
        item = getCustomPropByCatChannel(channelId, orgChannelId, "all");
        if (item != null) {
            cmsBtCustomPropModels.add(item);
        }

        // 取得类目的属性类目的属性
        for (String cat1 : cats) {
            catPath += cat1;
            item = getCustomPropByCatChannel(channelId, orgChannelId, catPath);
            if (item != null) {
                cmsBtCustomPropModels.add(item);
            }
            catPath += ">";
        }
        return cmsBtCustomPropModels;
    }


    // 获取feed属性  redis缓存  有效期未 1天
    private CmsBtCustomPropModel getFeedAttributeName(String orgChannelId) {
        CmsBtCustomPropModel feedCustomProp = new CmsBtCustomPropModel();
        List<CmsBtCustomPropModel.Entity> entitys = (List<CmsBtCustomPropModel.Entity>) CacheHelper.getHashOperation().get(CacheKey, orgChannelId);
        if (entitys == null) {
            entitys = feedCategoryAttributeService.getAttributeNameByChannelId(orgChannelId);
            if (!ListUtils.isNull(entitys)) {
                CacheHelper.getHashOperation().put(CacheKey, orgChannelId, entitys);
                CacheHelper.getCacheTemplate().expire(CacheKey, 1, TimeUnit.DAYS);
            }
        }
        feedCustomProp.setEntitys(entitys);
        return feedCustomProp;
    }

    // 继承计算
    private CmsBtCustomPropModel merge(CmsBtCustomPropModel prop1, CmsBtCustomPropModel prop2) {
        for (CmsBtCustomPropModel.Entity entity : prop2.getEntitys()) {
            CmsBtCustomPropModel.Entity temp = prop1.getEntitys().stream()
                    .filter(entity1 -> entity.getNameEn().equalsIgnoreCase(entity1.getNameEn()))
                    .findFirst().orElse(null);
            if (temp == null) {
                prop1.getEntitys().add(entity);
            } else {
                entity.forEach(temp::putIfAbsent);
            }
        }
        if (ListUtils.isNull(prop1.getSort()) && !ListUtils.isNull(prop2.getSort())) {
            prop1.setSort(prop2.getSort());
        }
        return prop1;
    }

    // 给属性排序
    private void rearrange(CmsBtCustomPropModel cmsBtCustomPropModel) {
        cmsBtCustomPropModel.getEntitys().sort((o1, o2) -> {
            Boolean b1 = o1.getChecked() == null ? false : o1.getChecked();
            Boolean b2 = o2.getChecked() == null ? false : o2.getChecked();
            return b1.compareTo(b2) * -1;
        });

        if (!ListUtils.isNull(cmsBtCustomPropModel.getSort())) {
            cmsBtCustomPropModel.getEntitys().sort((o1, o2) -> {
                Integer b1 = cmsBtCustomPropModel.getSort().indexOf(o1.getNameEn()) == -1 ? Integer.MAX_VALUE : cmsBtCustomPropModel.getSort().indexOf(o1.getNameEn());
                Integer b2 = cmsBtCustomPropModel.getSort().indexOf(o2.getNameEn()) == -1 ? Integer.MAX_VALUE : cmsBtCustomPropModel.getSort().indexOf(o2.getNameEn());
                if(o1.getChecked() == null || true != o1.getChecked()) b1=Integer.MAX_VALUE;
                if(o2.getChecked() == null || true != o2.getChecked()) b2=Integer.MAX_VALUE;
                return b1.compareTo(b2);
            });
        }
    }


}
