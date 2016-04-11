package com.voyageone.service.model.cms.enums;

/**
 * 语言种类
 *
 * @author Edward, 2016/02/04.
 * @version 2.0.0
 * @since 2.0.0
 */
public enum LanguageType {

    ENGLISH("en"),

    CHINESE("cn");

    private String name;

    LanguageType(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
