package com.voyageone.task2.cms.model;
import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedInfoModel;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author morse.lu
 * @version 0.0.1, 16/3/9
 */
public class CmsBtFeedInfoVtmModel extends CmsBtFeedInfoModel {
    private String Manufacturer;
    private String DosageSize;
    private String DosageUnits;
    private String SuggestedUse;
    private String Warnings;
    private String secondaryCategories;
    private String translationName1;
    private String translationValue1;
    private String translationName2;
    private String translationValue2;


    public String getManufacturer() {
        return Manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        Manufacturer = manufacturer;
    }

    public String getDosageSize() {
        return DosageSize;
    }

    public void setDosageSize(String dosageSize) {
        DosageSize = dosageSize;
    }

    public String getDosageUnits() {
        return DosageUnits;
    }

    public void setDosageUnits(String dosageUnits) {
        DosageUnits = dosageUnits;
    }

    public String getSuggestedUse() {
        return SuggestedUse;
    }

    public void setSuggestedUse(String suggestedUse) {
        SuggestedUse = suggestedUse;
    }

    public String getWarnings() {
        return Warnings;
    }

    public void setWarnings(String warnings) {
        Warnings = warnings;
    }

    public String getSecondaryCategories() {
        return secondaryCategories;
    }

    public void setSecondaryCategories(String secondaryCategories) {
        this.secondaryCategories = secondaryCategories;
    }


    public String getTranslationName1() {
        return translationName1;
    }

    public void setTranslationName1(String translationName1) {
        this.translationName1 = translationName1 == null ? null : translationName1.trim();
    }

    public String getTranslationValue1() {
        return translationValue1;
    }

    public void setTranslationValue1(String translationValue1) {
        this.translationValue1 = translationValue1 == null ? null : translationValue1.trim();
    }

    public String getTranslationName2() {
        return translationName2;
    }

    public void setTranslationName2(String translationName2) {
        this.translationName2 = translationName2 == null ? null : translationName2.trim();
    }

    public String getTranslationValue2() {
        return translationValue2;
    }

    public void setTranslationValue2(String translationValue2) {
        this.translationValue2 = translationValue2 == null ? null : translationValue2.trim();
    }

}
