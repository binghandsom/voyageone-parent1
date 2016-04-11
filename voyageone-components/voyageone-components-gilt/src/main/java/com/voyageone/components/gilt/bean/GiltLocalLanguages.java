package com.voyageone.components.gilt.bean;

/**
 * @author aooer 2016/2/1.
 * @version 2.0.0
 * @since 2.0.0
 */
public enum GiltLocalLanguages {

    zh_CN(" China "),

    en_US(" United States")
    ;

    private String description;

    GiltLocalLanguages(String description) {
        this.description = description;
    }
}
