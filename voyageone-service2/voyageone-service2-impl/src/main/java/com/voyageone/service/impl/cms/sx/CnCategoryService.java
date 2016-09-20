package com.voyageone.service.impl.cms.sx;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.masterdate.schema.enums.FieldTypeEnum;
import com.voyageone.common.masterdate.schema.field.Field;
import com.voyageone.common.masterdate.schema.field.InputField;
import com.voyageone.common.masterdate.schema.utils.JsonUtil;
import com.voyageone.components.cn.service.CnSchemaService;
import com.voyageone.service.bean.cms.cn.CnCategoryBean;
import com.voyageone.service.impl.BaseService;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 独立域名类目Service
 *
 * @author morse on 2016/9/20
 * @version 2.6.0
 */
@Service
public class CnCategoryService extends BaseService {

    @Autowired
    private CnSchemaService cnSchemaService;

    /**
     * 创建CnCategoryBean(要推送的属性)
     *
     * @param catFullId 完整的类目路径，例:1>1.2>1.2.1
     * @param separator 类目路径的分隔符，例:>
     * @param name 类目名
     * @param description 类目描述(画面上显示内容)
     * @return
     */
    public CnCategoryBean createCnCategoryBean(String catFullId, String separator, String name, String description) {
        CnCategoryBean bean = new CnCategoryBean();

        String[] catIds = catFullId.split(separator);
        int length = catIds.length;

        bean.setId(catIds[length - 1]); // 类目id
        bean.setUrlKey(catIds[length - 1]); // 唯一（暂定用类目Id）
        if (length > 1) {
            bean.setParentId(catIds[length - 2]); // 父类目Id
        }
        StringBuilder catFullPath = new StringBuilder("");
        for (int i = 0; i < length; i++) {
            catFullPath.append(catIds[i]);
            if (i != length - 1) {
                // 不是最后一个，增加分隔符
                catFullPath.append("/");
            }
        }
        bean.setCategoryPath(catFullPath.toString()); // 类目的path
        bean.setName(name);
        bean.setHeaderTitle(description);

        return bean;
    }

    /**
     * 上传类目(单个)
     *
     * @param bean
     * @param isDelete
     * @return
     */
    public boolean uploadCnCategory(CnCategoryBean bean, boolean isDelete) {
        return uploadCnCategory(new ArrayList<CnCategoryBean>(){{this.add(bean);}}, isDelete);
    }

    /**
     * 上传类目(批量)
     *
     * @param listBean
     * @param isDelete
     * @return
     */
    public boolean uploadCnCategory(List<CnCategoryBean> listBean, boolean isDelete) {
        for (CnCategoryBean bean : listBean) {
            if (isDelete) {
                bean.setIsPublished("0");
                bean.setIsVisibleOnMenu("0");
            } else {
                bean.setIsPublished("1");
                bean.setIsVisibleOnMenu("1");
            }
        }

        return uploadCnCategory(listBean);
    }

    /**
     * 上传类目(批量)
     *
     * @param listBean
     * @return
     */
    public boolean uploadCnCategory(List<CnCategoryBean> listBean) {
        boolean isSuccess = false;

        List<List<Field>> listCatField = new ArrayList<>();
        for (CnCategoryBean bean : listBean) {
            Map<String, Object> mapBean;
            // modified by morse.lu 2016/09/20 start
            // BeanUtils.describe顺序会乱掉,key也会开头变成小写
            // 用json转换过渡下吧
//            try {
//                mapBean = BeanUtils.describe(bean);
//            } catch (Exception e) {
//                throw new BusinessException("类目Bean转换Map失败!请联系管理员!");
//            }
            mapBean = JsonUtil.jsonToMap(JsonUtil.getJsonString(bean));
            // modified by morse.lu 2016/09/20 end

            List<Field> listField = new ArrayList<>();
            listCatField.add(listField);

            mapBean.forEach((key, val) -> {
                InputField field = (InputField) FieldTypeEnum.createField(FieldTypeEnum.INPUT);
                listField.add(field);
                field.setId(key);
                if (val != null) {
                    if (key.equals("DisplayOrder")) {
                        // int型经过json转换后会变成double型，toString的话会带小数，暂时写死一下
                        field.setValue(Integer.toString(((Double) val).intValue()));
                    } else {
                        field.setValue(val.toString());
                    }
                }
            });
        }

        String xml = cnSchemaService.writeCategoryXmlString(listCatField);
        $info("独立域名上传类目xml:" + xml);

        // TODO:doPost

        return isSuccess;
    }

}
