package com.voyageone.web2.cms.views.setting.mapping.feed;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.cms.service.dao.mongodb.CmsMtCategorySchemaDao;
import com.voyageone.cms.service.model.*;
import com.voyageone.cms.service.model.feed.mapping.Prop;
import com.voyageone.web2.base.BaseAppService;
import com.voyageone.web2.cms.bean.setting.mapping.feed.GetFieldMappingBean;
import com.voyageone.web2.cms.bean.setting.mapping.feed.SaveFieldMappingBean;
import com.voyageone.web2.core.bean.UserSessionBean;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

/**
 * 为属性匹配画面提供功能
 *
 * @author Jonas, 12/24/15.
 * @version 2.0.0
 * @since 2.0.0
 */
@Service
public class CmsFeedPropMappingService extends BaseAppService {

    @Autowired
    private CmsMtCategorySchemaDao categorySchemaDao;

    @Autowired
    private CmsFeedMappingService feedMappingService;

    @Autowired
    private com.voyageone.cms.service.CmsFeedMappingService com$feedMappingService;

    /**
     * 通过 Feed 类目,获取其默认匹配的主类目
     *
     * @param feedCategoryPath Feed 类目路径
     * @param userSessionBean  当前用户及配置
     * @return 主类目
     */
    public CmsMtCategorySchemaModel getCategoryPropsByFeed(String feedCategoryPath, UserSessionBean userSessionBean) throws UnsupportedEncodingException {

        CmsMtFeedCategoryTreeModelx treeModel = feedMappingService.getFeedCategoryTree(userSessionBean);

        CmsFeedCategoryModel feedCategoryModel = feedMappingService.findByPath(feedCategoryPath, treeModel);

        if (feedCategoryModel == null)
            throw new BusinessException("根据路径没找到类目");

        CmsFeedMappingModel feedMappingModel = feedMappingService.findMapping(feedCategoryModel, m -> m.getDefaultMapping() == 1);

        if (feedMappingModel == null)
            throw new BusinessException("类目没有默认的类目匹配");

        String categoryId = convertPathToId(feedMappingModel.getMainCategoryPath());

        return categorySchemaDao.getMasterSchemaModelByCatId(categoryId);
    }

    /**
     * 获取具体到字段的 Mapping 设定
     *
     * @param getFieldMappingBean 查询参数
     * @param user                当前用户
     * @return 属性匹配设定
     */
    public Prop getFieldMapping(GetFieldMappingBean getFieldMappingBean, UserSessionBean user) {

        CmsBtFeedMappingModel mappingModel = com$feedMappingService.getMapping(user.getSelChannel(),
                getFieldMappingBean.getFeedCategoryPath(), getFieldMappingBean.getMainCategoryPath());

        List<Prop> props = mappingModel.getProps();

        if (props == null)
            return null;

        return flattenFinalProp(props)
                .filter(p -> p.getProp().equals(getFieldMappingBean.getField()))
                .findFirst()
                .orElse(null);
    }

    /**
     * 获取类目匹配中已匹配的主类目属性
     *
     * @param feedCategoryPath Feed 类目路径
     * @param mainCategoryPath Main 类目路径
     * @param userSessionBean  当前用户
     * @return 属性名集合
     */
    public List<String> getMatched(String feedCategoryPath, String mainCategoryPath, UserSessionBean userSessionBean) {

        CmsBtFeedMappingModel btFeedMappingModel = com$feedMappingService.getMapping(userSessionBean.getSelChannel(),
                feedCategoryPath, mainCategoryPath);

        if (btFeedMappingModel == null)
            throw new BusinessException("没有找到匹配");

        List<Prop> props = btFeedMappingModel.getProps();

        if (props == null)
            return new ArrayList<>(0);

        return flattenFinalProp(props)
                .filter(p -> p.getMappings() != null && p.getMappings().size() > 0)
                .map(Prop::getProp)
                .collect(toList());
    }

    /**
     * 通过 categoryPath 转换获取 categoryId
     *
     * @param categoryPath 类目路径
     * @return String
     */
    public String convertPathToId(String categoryPath) throws UnsupportedEncodingException {

        // 当前为 Path 的 Base64 码
        // 有可能未来更改为 MD5
        return new String(Base64.encodeBase64(categoryPath.getBytes("UTF-8")), "UTF-8");
    }

    /**
     * 按 feedCategoryPath 查找类目并返回类目的属性
     *
     * @param feedCategoryPath 类目路径
     * @param userSessionBean  当前用户
     * @return 类目的属性
     */
    public Map<String, List<String>> getFeedAttributes(String feedCategoryPath, UserSessionBean userSessionBean) {

        CmsMtFeedCategoryTreeModelx treeModelx = feedMappingService.getFeedCategoryTree(userSessionBean);

        CmsFeedCategoryModel feedCategoryModel = feedMappingService.findByPath(feedCategoryPath, treeModelx);

        return feedCategoryModel == null ? null : feedCategoryModel.getAttribute();
    }

    /**
     * 保存一个字段/属性的关联关系
     *
     * @param saveFieldMappingBean 请求参数
     * @param userSessionBean      当前用户
     */
    public void saveFeedMapping(SaveFieldMappingBean saveFieldMappingBean, UserSessionBean userSessionBean) {

        CmsBtFeedMappingModel feedMappingModel = com$feedMappingService.getMapping(userSessionBean.getSelChannel(),
                saveFieldMappingBean.getFeedCategoryPath(), saveFieldMappingBean.getMainCategoryPath());

        if (feedMappingModel == null)
            throw new BusinessException("没找到 Mapping");

        Prop Submitted = saveFieldMappingBean.getPropMapping();
        List<String> fieldPath = saveFieldMappingBean.getFieldPath();

        if (Submitted == null || fieldPath == null || fieldPath.size() < 1)
            throw new BusinessException("没有参数");

        List<Prop> props = feedMappingModel.getProps();

        if (props == null)
            feedMappingModel.setProps(props = new ArrayList<>());

        Prop prop = findProp(fieldPath, props);

        prop.setMappings(Submitted.getMappings());

        com$feedMappingService.setMapping(feedMappingModel);
    }

    /**
     * 按属性路径查找或创建树形的属性结构
     *
     * @param propPath 属性路径
     * @param props    顶层属性集合
     * @return 返回最叶子级
     */
    private Prop findProp(List<String> propPath, List<Prop> props) {

        Prop prop = null;
        String last = propPath.get(propPath.size() - 1);

        for (String name : propPath) {
            // 先找找
            prop = findProp(props, name);
            // 没找到就造个新的,并同时扔到父亲的子集合中
            if (prop == null) {
                props.add(prop = new Prop());
                prop.setProp(name);
            }
            // 看看还会不会在进行下一次查询
            if (last.equals(name)) break;
            // 为遍历的下一次做准备
            props = prop.getChildren();
            // 如果没取到,就更新
            if (props == null)
                prop.setChildren(props = new ArrayList<>());
        }

        return prop;
    }

    private Prop findProp(List<Prop> props, String name) {
        if (props.size() < 1) return null;
        if (props.size() < 2) return props.get(0).getProp().equals(name) ? props.get(0) : null;
        return props.stream().filter(p -> p.getProp().equals(name)).findFirst().orElse(null);
    }

    private Stream<Prop> flattenFinalProp(List<Prop> props) {

        return props.stream().flatMap(this::flattenFinalProp);
    }

    private Stream<Prop> flattenFinalProp(Prop prop) {

        List<Prop> children = prop.getChildren();

        if (children == null || children.size() < 1)
            return Stream.of(prop);

        return flattenFinalProp(children);
    }
}
