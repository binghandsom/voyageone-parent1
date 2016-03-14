package com.voyageone.web2.cms.views.pop.bulkUpdate;

import com.voyageone.common.configs.TypeChannel;
import com.voyageone.common.masterdate.schema.enums.FieldTypeEnum;
import com.voyageone.common.masterdate.schema.field.Field;
import com.voyageone.common.masterdate.schema.field.OptionsField;
import com.voyageone.common.masterdate.schema.option.Option;
import com.voyageone.common.util.CommonUtil;
import com.voyageone.service.model.cms.mongo.CmsMtCommonPropDefModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.web2.base.BaseAppService;
import com.voyageone.web2.cms.CmsConstants;
import com.voyageone.web2.cms.dao.CmsMtCommonPropDefDao;
import com.voyageone.web2.core.bean.UserSessionBean;
import com.voyageone.web2.sdk.api.VoApiDefaultClient;
import com.voyageone.web2.sdk.api.request.ProductGetRequest;
import com.voyageone.web2.sdk.api.request.ProductUpdateRequest;
import com.voyageone.web2.sdk.api.response.ProductGetResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author gubuchun 15/12/9
 * @version 2.0.0
 */
@Service
public class CmsFieldEditService extends BaseAppService {

    @Autowired
    private CmsMtCommonPropDefDao cmsMtCommonPropDefDao;

//    @Autowired
//    private CmsBtProductDao cmsBtProductDao;
    @Autowired
    protected VoApiDefaultClient voApiClient;

    /**
     * 获取pop画面options.
     */
    public List<CmsMtCommonPropDefModel> getPopOptions(String channel_id) {

        List<CmsMtCommonPropDefModel> modelList = cmsMtCommonPropDefDao.selectAll();
        List<CmsMtCommonPropDefModel> resultList = new ArrayList<>();

        for (CmsMtCommonPropDefModel model : modelList) {
            CmsMtCommonPropDefModel resModel = new CmsMtCommonPropDefModel();
            Field field = model.getField();
            if ("optConfig".equals(field.getDataSource())) {
                OptionsField optionsField = (OptionsField) field;
                List<Option> options = TypeChannel.getOptions(optionsField.getId(), channel_id);
                optionsField.setOptions(options);
                resModel.setField(optionsField);
            } else {
                resModel.setField(field);
            }
            resultList.add(resModel);
        }
        return resultList;
    }

    /**
     * 批量修改属性.
     */
    public void setProductFields(Map<String, Object> params, UserSessionBean userInfo) {
        Map<String, Object> prop = (Map<String, Object>) params.get("property");
        String prop_id = prop.get("id").toString();
        List<Long> prodIdList = CommonUtil.changeListType((ArrayList<Integer>) params.get("productIds"));

        for(Long productId : prodIdList) {

            // 获取产品的信息
            ProductGetRequest productGetRequest = new ProductGetRequest();
            productGetRequest.setProductId(productId);
            productGetRequest.setChannelId(userInfo.getSelChannelId());

            ProductGetResponse productGetResponse = voApiClient.execute(productGetRequest);

            Object[] field = getPropValue(params);
            CmsBtProductModel productModel = productGetResponse.getProduct();
            productModel.getFields().setAttribute(field[0].toString(), field[1]);

            // 更新状态以外的属性时,check产品状态如果为Approved,则将产品状态设置成Ready
            if (!"status".equals(prop_id) && CmsConstants.productStatus.APPROVED.equals(productModel.getFields().getStatus()))
                productModel.getFields().setStatus(CmsConstants.productStatus.READY);

            ProductUpdateRequest updateRequest = new ProductUpdateRequest(userInfo.getSelChannelId());
            updateRequest.setProductModel(productModel);
            updateRequest.setIsCheckModifed(false);
            updateRequest.setModifier(userInfo.getUserName());

            voApiClient.execute(updateRequest);
        }
    }

    /**
     * 根据request值获取需要更新的Field数据
     *
     * @param params
     * @return
     */
    private Object[] getPropValue(Map<String, Object> params) {
        try {

//            CmsBtProductModel_Field field = new CmsBtProductModel_Field();
            Object[] field = new Object[2];

            String type = ((Map<String, Object>) params.get("property")).get("type").toString();
//            Field prop = FieldTypeEnum.createField(FieldTypeEnum.getEnum(type));

            switch (FieldTypeEnum.getEnum(type)) {
//                case INPUT:
//                    prop_value = (String)prop.getValue();
//                    break;
//                case MULTIINPUT:
//                    (List<Value>)prop.getValue();
//                    break;
                case SINGLECHECK:
//                    SingleCheckField prop = new SingleCheckField();
//                    BeanUtils.populate(prop, (Map<String, Object>) params.get("property"));
                    Map<String, Object> prop = (Map<String, Object>) params.get("property");
                    String prop_id = prop.get("id").toString();
                    String prop_value = ((Map<String, Object>) prop.get("value")).get("value").toString();
                    field[0] = prop_id;
                    field[1] = prop_value;
                    break;
//                case MULTICHECK:
//                    (List<Value>)prop.getValue(); List<String>
//                        field.put(prop.getId(), ((Value)prop.getValue()).getValue());
//                    break;
//                case COMPLEX:Map<String, Object>
//                    (ComplexValue)prop.getValue();
//                    break;
//                case MULTICOMPLEX:List<Map<String, Object>>
//                    (List<ComplexValue>)prop.getValue();
//                    break;
//                case LABEL:
//                    (String)prop.getValue();
//                    break;
            }
            return field;
        } catch (Exception e) {
            logger.error("CmsPropChangeService: " + e.getMessage());
        }

        return null;
    }
}
