package com.voyageone.service.impl.cms.sx;

import com.voyageone.common.masterdate.schema.option.Option;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by morse.lu on 2016/05/12 (copy and modified from task2 / PriceSectionBuilder)
 */
public class PriceSectionBuilder {

    private List<PriceOption> priceOptions;

    private final List<String> listReplaceWord = new ArrayList<String>(){{
                                                                                add("元");
                                                                                add(" ");
                                                                            }};

    private PriceSectionBuilder() {}

    private class PriceOption {
        private String optionName;
        private String optionValue;

        private PriceSection priceSection;

        private PriceOption(String optionName, String optionValue) {
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

        public PriceSection getPriceSection() {
            return priceSection;
        }

        public void setPriceSection(PriceSection priceSection) {
            this.priceSection = priceSection;
        }
    }

    private class PriceSection {
        private double minPrice;
        private double maxPrice;

        private PriceSection() {
        }

        private PriceSection(double minPrice, double maxPrice) {
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

    public static PriceSectionBuilder createPriceSectionBuilder(List<Option> fieldPriceOptions) {
        PriceSectionBuilder priceSectionBuilder =  new PriceSectionBuilder();
       return priceSectionBuilder.build(fieldPriceOptions);
    }

    public PriceSectionBuilder build(List<Option> fieldPriceOptions) {
        priceOptions = new ArrayList<>();
        for (Option fieldPriceOption : fieldPriceOptions) {
            PriceOption priceOption = new PriceOption(fieldPriceOption.getDisplayName(), fieldPriceOption.getValue());
            PriceSection priceSection = parsePriceSection(priceOption.getOptionName());
            priceOption.setPriceSection(priceSection);
            priceOptions.add(priceOption);
        }
        return this;
    }

    public String getPriceOptionValue(double price) {
        for (PriceOption priceOption : priceOptions) {
            if (inSection(price, priceOption.getPriceSection())) {
                return priceOption.getOptionValue();
            }
        }
        return null;
    }

    private PriceSection parsePriceSection(String platformPropOptionValue) {
        // 替换掉无用的字符
        for (String word : listReplaceWord) {
            platformPropOptionValue = platformPropOptionValue.replace(word, "");
        }

        String sectionSeparator = "-";
        String maxIdentifier = "以下";
        String minIdentifier = "以上";

        PriceSection priceSection = new PriceSection();
        if (platformPropOptionValue.indexOf(sectionSeparator) != -1)  {
            //有最小值和最大值的区间
            String[] sectionStrs = platformPropOptionValue.split(sectionSeparator);
            priceSection.setMinPrice(Double.valueOf(sectionStrs[0]));
            priceSection.setMaxPrice(Double.valueOf(sectionStrs[1]));
        } else if (platformPropOptionValue.indexOf(maxIdentifier) != -1) {
            //有最大值
            String priceMax = platformPropOptionValue.replace(maxIdentifier, "");
            priceSection.setMinPrice(0);
            priceSection.setMaxPrice(Integer.valueOf(priceMax));
        } else if (platformPropOptionValue.indexOf(minIdentifier) != -1) {
            //有最小值
            String priceMin = platformPropOptionValue.replace(minIdentifier, "");
            priceSection.setMaxPrice(Integer.MAX_VALUE);
            priceSection.setMinPrice(Integer.valueOf(priceMin));
        }
        return priceSection;
    }

    private boolean inSection(double price, PriceSection priceSection)  {
        return (price <= priceSection.getMaxPrice()) && (price >= priceSection.getMinPrice());
    }
}
