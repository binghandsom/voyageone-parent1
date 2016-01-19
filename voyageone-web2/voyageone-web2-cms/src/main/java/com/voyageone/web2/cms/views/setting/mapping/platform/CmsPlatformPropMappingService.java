package com.voyageone.web2.cms.views.setting.mapping.platform;

import com.taobao.top.schema.exception.TopSchemaException;
import com.taobao.top.schema.factory.SchemaReader;
import com.taobao.top.schema.field.Field;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.cms.service.bean.*;
import com.voyageone.cms.service.dao.mongodb.CmsMtCategorySchemaDao;
import com.voyageone.cms.service.dao.mongodb.CmsMtPlatformCategorySchemaDao;
import com.voyageone.cms.service.dao.mongodb.CmsMtPlatformMappingDao;
import com.voyageone.cms.service.model.CmsMtCategorySchemaModel;
import com.voyageone.cms.service.model.CmsMtPlatformCategorySchemaModel;
import com.voyageone.cms.service.model.CmsMtPlatformMappingModel;
import com.voyageone.ims.rule_expression.RuleExpression;
import com.voyageone.ims.rule_expression.RuleWord;
import com.voyageone.web2.base.BaseAppService;
import com.voyageone.web2.cms.dao.CmsMtDictDao;
import com.voyageone.web2.cms.model.CmsMtDictModel;
import com.voyageone.web2.core.bean.UserSessionBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

/**
 * 当前的功能,仅限支持天猫...完全不支持其他平台
 *
 * @author Jonas, 1/13/16.
 * @version 2.0.0
 * @since 2.0.0
 */
@Service("web2.cms.CmsPlatformPropMappingService")
public class CmsPlatformPropMappingService extends BaseAppService {

    @Autowired
    private CmsMtPlatformCategorySchemaDao platformCategorySchemaDao;

    @Autowired
    private CmsMtPlatformMappingDao platformMappingDao;

    @Autowired
    private CmsMtCategorySchemaDao categorySchemaDao;

    @Autowired
    private CmsMtDictDao dictDao;

    /**
     * 获取平台类目和 Mapping 的所有信息
     *
     * @param categoryId 主数据类目 ID
     * @param cartId     平台 ID
     * @param user       用户配置
     * @return 携带所有信息的 Map 包含: categorySchema / properties / mapping
     * @throws TopSchemaException
     */
    public Map<String, Object> getPlatformCategory(String categoryId, int cartId, UserSessionBean user) throws TopSchemaException {

        CmsMtPlatformMappingModel platformMappingModel =
                platformMappingDao.getMappingByMainCatId(user.getSelChannelId(), cartId, categoryId);

        if (platformMappingModel == null)
            throw new BusinessException("没找到 Mapping");

        CmsMtPlatformCategorySchemaModel platformCatSchemaModel =
                platformCategorySchemaDao.getPlatformCatSchemaModel(platformMappingModel.getPlatformCategoryId(), cartId);

        // 转换类目属性
        Map<String, Field> fieldMap = SchemaReader.readXmlForMap(platformCatSchemaModel.getPropsItem());

        // 转换简化的 mapping 信息
        Map<String, Object> mappingMap = platformMappingModel.getProps().stream()
                .collect(toMap(MappingBean::getPlatformPropId, this::getMatched));

        // 清除信息
        platformCatSchemaModel.setPropsItem(null);

        return new HashMap<String, Object>() {{
            put("categorySchema", platformCatSchemaModel);
            put("properties", fieldMap);
            put("mapping", mappingMap);
        }};
    }

    /**
     * 获取主数据类目 Schema
     * @param categoryId 主数据类目
     * @return CmsMtCategorySchemaModel
     */
    public CmsMtCategorySchemaModel getMainCategorySchema(String categoryId) {

        return categorySchemaDao.getMasterSchemaModelByCatId(categoryId);
    }

    /**
     * 获取当前渠道的所有可用字典
     */
    public List<CmsMtDictModel> getDictList(UserSessionBean user) {

        return dictDao.selectByChannel(user.getSelChannel());
    }

    /**
     * 计算 MappingBean 是否匹配的结果
     *
     * @param mappingBean 任意类型的 Mapping
     * @return 不同类型返回不同, 分别为 Boolean / Map / List[Map]
     */
    private Object getMatched(MappingBean mappingBean) {

        if (mappingBean instanceof SimpleMappingBean) {

            SimpleMappingBean simpleMappingBean = (SimpleMappingBean) mappingBean;

            boolean result;

            RuleExpression ruleExpression = simpleMappingBean.getExpression();

            if (ruleExpression == null) result = false;
            else {
                List<RuleWord> ruleWords = ruleExpression.getRuleWordList();
                result = ruleWords != null && !ruleWords.isEmpty();
            }

            // 简单类型, 返回是否是有效映射
            return result;

        } else if (mappingBean instanceof ComplexMappingBean) {

            ComplexMappingBean complexMappingBean = (ComplexMappingBean) mappingBean;

            List<MappingBean> mappings = complexMappingBean.getSubMappings();

            if (mappings == null || mappings.isEmpty())
                return false;

            // 复杂类型, json 角度考虑, 返回其内部字段的结果对象
            return complexMappingBean.getSubMappings().stream()
                    .collect(toMap(MappingBean::getPlatformPropId, this::getMatched));

        } else if (mappingBean instanceof MultiComplexCustomMappingBean) {

            MultiComplexCustomMappingBean multiComplexCustomMappingBean =
                    (MultiComplexCustomMappingBean) mappingBean;

            List<MultiComplexCustomMappingValue> values = multiComplexCustomMappingBean.getValues();

            if (values == null || values.isEmpty())
                return false;

            return values.stream()
                    .map(value -> value.getSubMappings().stream()
                            .collect(toMap(MappingBean::getPlatformPropId, this::getMatched)))
                    .collect(toList());

        } else {
            throw new BusinessException("不支持的类型");
        }
    }
}
