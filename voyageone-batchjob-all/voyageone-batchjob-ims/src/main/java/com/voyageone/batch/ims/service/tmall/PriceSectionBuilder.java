package com.voyageone.batch.ims.service.tmall;

import com.taobao.top.schema.enums.FieldTypeEnum;
import com.taobao.top.schema.field.Field;
import com.taobao.top.schema.field.SingleCheckField;
import com.voyageone.batch.ims.ImsConstants;
import com.voyageone.batch.ims.dao.PlatformPropDao;
import com.voyageone.batch.ims.modelbean.CmsCodePropBean;
import com.voyageone.batch.ims.modelbean.PlatformPropBean;
import com.voyageone.batch.ims.modelbean.PlatformPropOptionBean;
import com.voyageone.ims.enums.CmsFieldEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Leo on 15-7-22.
 */
@Repository
public class PriceSectionBuilder {
    @Autowired
    private PlatformPropDao platformPropDao;

    private class PriceSection {
        private double minPrice;
        private double maxPrice;

        public PriceSection() {
        }

        public PriceSection(double minPrice, double maxPrice) {
            this.minPrice = minPrice;
            this.maxPrice = maxPrice;
        }

        public double getMinPrice() {
            return minPrice;
        }

        public void setMinPrice(double minPrice) {
            this.minPrice = minPrice;
        }

        public double getMaxPrice() {
            return maxPrice;
        }

        public void setMaxPrice(double maxPrice) {
            this.maxPrice = maxPrice;
        }

    }

    public Field buildPriceSectionField(PlatformPropBean platformProp, CmsCodePropBean cmsCodeProp) {
        if (platformProp.getPlatformPropType() == ImsConstants.PlatformPropType.C_SINGLE_CHECK)
        {
            String priceStr = cmsCodeProp.getProp(CmsFieldEnum.CmsCodeEnum.price);
            double price = Double.valueOf(priceStr);

            String platformPropHash = platformProp.getPlatformPropHash();
            List<PlatformPropOptionBean> platformoOptions = platformPropDao.selectPlatformOptionsByPropHash(platformPropHash);

            for (PlatformPropOptionBean platformPropOption : platformoOptions)
            {
                PriceSection priceSection = parsePriceSection(platformPropOption.getPlatformPropOptionName());
                if (inSection(price, priceSection))
                {
                    SingleCheckField field = (SingleCheckField) FieldTypeEnum.createField(FieldTypeEnum.SINGLECHECK);
                    field.setId(platformProp.getPlatformPropId());
                    field.setValue(platformPropOption.getPlatformPropOptionValue());
                    return field;
                }
            }
        }
        return null;
    }

    private PriceSection parsePriceSection(String platformPropOptionValue) {
        //替换掉无用的字符
        platformPropOptionValue = platformPropOptionValue.replace("元", "");
        platformPropOptionValue = platformPropOptionValue.replace(" ", "");

        String sectionSeparator = "-";
        String maxIdentifier = "以下";
        String minIdentifier = "以上";

        int sectionIndex = platformPropOptionValue.indexOf(sectionSeparator);

        PriceSection priceSection = new PriceSection();
        //有最小值和最大值的区间
        if (sectionIndex != -1)
        {
            String[] sectionStrs = platformPropOptionValue.split(sectionSeparator);

            priceSection.setMinPrice(Double.valueOf(sectionStrs[0]));
            priceSection.setMaxPrice(Double.valueOf(sectionStrs[1]));
        } //有最大值
        else if (platformPropOptionValue.indexOf(maxIdentifier) != -1)
        {
            String priceMax = platformPropOptionValue.replace(maxIdentifier, "");

            priceSection.setMinPrice(0);
            priceSection.setMaxPrice(Integer.valueOf(priceMax));
        } //有最小值
        else if (platformPropOptionValue.indexOf(minIdentifier) != -1)
        {
            String priceMin = platformPropOptionValue.replace(minIdentifier, "");

            priceSection.setMaxPrice(Integer.MAX_VALUE);
            priceSection.setMinPrice(Integer.valueOf(priceMin));
        }
        return priceSection;
    }

    private boolean inSection(double price, PriceSection priceSection)
    {
        return (price <= priceSection.getMaxPrice()) && (price >= priceSection.getMinPrice());
    }
}
