package com.voyageone.service.model.cms.mongo.product;

import com.voyageone.base.dao.mongodb.model.BaseMongoMap;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Piao
 * @description 为了满足产品的图片上传功能
 */
public class CmsBtProductModel_Images extends BaseMongoMap<String, Object> {

    public List<CmsBtProductModel_Field_Image> getImages(CmsBtProductConstants.FieldImageType imageType) {
        List<CmsBtProductModel_Field_Image> result = null;
        if (imageType != null) {
            switch (imageType) {
                case PRODUCT_IMAGE:
                    result = getImages1();
                    break;
                case PACKAGE_IMAGE:
                    result = getImages2();
                    break;
                case ANGLE_IMAGE:
                    result = getImages3();
                    break;
                case CUSTOM_IMAGE:
                    result = getImages4();
                    break;
                case MOBILE_CUSTOM_IMAGE:
                    result = getImages5();
                    break;
                case CUSTOM_PRODUCT_IMAGE:
                    result = getImages6();
                    break;
                case M_CUSTOM_PRODUCT_IMAGE:
                    result = getImages7();
                    break;
                case HANG_TAG_IMAGE:
                    result = getImages8();
                    break;
                case DURABILITY_TAG_IMAGE:
                    result = getImages9();
                    break;
            }
        }
        return result;
    }

    public void setImages(CmsBtProductConstants.FieldImageType imageType, List<CmsBtProductModel_Field_Image> images) {
        if (imageType != null) {
            switch (imageType) {
                case PRODUCT_IMAGE:
                    setImages1(images);
                    break;
                case PACKAGE_IMAGE:
                    setImages2(images);
                    break;
                case ANGLE_IMAGE:
                    setImages3(images);
                    break;
                case CUSTOM_IMAGE:
                    setImages4(images);
                    break;
                case MOBILE_CUSTOM_IMAGE:
                    setImages5(images);
                    break;
                case CUSTOM_PRODUCT_IMAGE:
                    setImages6(images);
                    break;
                case M_CUSTOM_PRODUCT_IMAGE:
                    setImages7(images);
                    break;
                case HANG_TAG_IMAGE:
                    setImages8(images);
                    break;
                case DURABILITY_TAG_IMAGE:
                    setImages9(images);
                    break;
            }
        }
    }

    /**
     * 商品图片
     */
    public List<CmsBtProductModel_Field_Image> getImages1() {
        if (!this.containsKey("images1") || getStringAttribute("images1") == null) {
            setAttribute("images1", new ArrayList<CmsBtProductModel_Field_Image>());
        }
        return getAttribute("images1");
    }

    public void setImages1(List<CmsBtProductModel_Field_Image> images1) {
        setAttribute("images1", images1);
    }

    /**
     * 包装图片
     */
    public List<CmsBtProductModel_Field_Image> getImages2() {
        if (!this.containsKey("images2") || getStringAttribute("images2") == null) {
            setAttribute("images2", new ArrayList<CmsBtProductModel_Field_Image>());
        }
        return getAttribute("images2");
    }

    public void setImages2(List<CmsBtProductModel_Field_Image> images2) {
        setAttribute("images2", images2);
    }

    /**
     * 带角度图片
     */
    public List<CmsBtProductModel_Field_Image> getImages3() {
        if (!this.containsKey("images3") || getStringAttribute("images3") == null) {
            setAttribute("images3", new ArrayList<CmsBtProductModel_Field_Image>());
        }
        return getAttribute("images3");
    }

    public void setImages3(List<CmsBtProductModel_Field_Image> images3) {
        setAttribute("images3", images3);
    }

    /**
     * 自定义图片
     */
    public List<CmsBtProductModel_Field_Image> getImages4() {
        if (!this.containsKey("images4") || getStringAttribute("images4") == null) {
            setAttribute("images4", new ArrayList<CmsBtProductModel_Field_Image>());
        }
        return getAttribute("images4");
    }

    public void setImages4(List<CmsBtProductModel_Field_Image> images4) {
        setAttribute("images4", images4);
    }

    /**
     * 手机端自定义图片
     */
    public List<CmsBtProductModel_Field_Image> getImages5() {
        if (!this.containsKey("images5") || getStringAttribute("images5") == null) {
            setAttribute("images5", new ArrayList<CmsBtProductModel_Field_Image>());
        }
        return getAttribute("images5");
    }

    public void setImages5(List<CmsBtProductModel_Field_Image> images5) {
        setAttribute("images5", images5);
    }

    /**
     * 商品自定义图片
     */
    public List<CmsBtProductModel_Field_Image> getImages6() {
        if (!this.containsKey("images6") || getStringAttribute("images6") == null) {
            setAttribute("images6", new ArrayList<CmsBtProductModel_Field_Image>());
        }
        return getAttribute("images6");
    }

    public void setImages6(List<CmsBtProductModel_Field_Image> images6) {
        setAttribute("images6", images6);
    }

    /**
     * 商品自定义M_CUSTOM_PRODUCT_IMAGE
     */
    public List<CmsBtProductModel_Field_Image> getImages7() {
        if (!this.containsKey("images7") || getStringAttribute("images7") == null) {
            setAttribute("images7", new ArrayList<CmsBtProductModel_Field_Image>());
        }
        return getAttribute("images7");
    }

    public void setImages7(List<CmsBtProductModel_Field_Image> images7) {
        setAttribute("images7", images7);
    }

    /**
     * 商品自定义HANG_TAG_IMAGE
     */
    public List<CmsBtProductModel_Field_Image> getImages8() {
        if (!this.containsKey("images8") || getStringAttribute("images8") == null) {
            setAttribute("images8", new ArrayList<CmsBtProductModel_Field_Image>());
        }
        return getAttribute("images8");
    }

    public void setImages8(List<CmsBtProductModel_Field_Image> images8) {
        setAttribute("images8", images8);
    }

    public List<CmsBtProductModel_Field_Image> getImages9() {
        if (!this.containsKey("images9") || getStringAttribute("images9") == null) {
            setAttribute("images9", new ArrayList<CmsBtProductModel_Field_Image>());
        }
        return getAttribute("images9");
    }

    public void setImages9(List<CmsBtProductModel_Field_Image> images9) {
        setAttribute("images9", images9);
    }

}
