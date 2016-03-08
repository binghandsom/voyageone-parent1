package com.voyageone.task2.cms.service.putaway.tmall;

import com.taobao.top.schema.option.Option;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Leo on 15-7-22.
 */
@Repository
public class PriceSectionBuilder {

    public static class PriceOption {
        private String optionName;
        private String optionValue;

        public PriceOption(String optionName, String optionValue) {
            this.optionName = optionName;
            this.optionValue = optionValue;
        }

        public String getOptionName() {
            return optionName;
        }

        public void setOptionName(String optionName) {
            this.optionName = optionName;
        }

        public String getOptionValue() {
            return optionValue;
        }

        public void setOptionValue(String optionValue) {
            this.optionValue = optionValue;
        }
    }

    public static List<PriceOption> transferFromTmall(List<Option> tmallPriceOptions) {
        List<PriceOption> priceOptions = new ArrayList<>();

        for (Option tmallPriceOption : tmallPriceOptions) {
            priceOptions.add(new PriceOption(tmallPriceOption.getDisplayName(), tmallPriceOption.getValue()));
        }
        return priceOptions;
    }

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

    public String autoDetectOptionValue (List<PriceOption> priceOptions, double price) {
        for (PriceOption priceOption : priceOptions)
        {
            PriceSection priceSection = parsePriceSection(priceOption.getOptionName());
            if (inSection(price, priceSection))
            {
                return priceOption.getOptionValue();
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
