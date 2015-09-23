package com.voyageone.ims.rule_expression;


import com.voyageone.ims.enums.CmsFieldEnum;

/**
 * Created by Leo on 15-6-18.
 */
public class CmsWord extends RuleWord {
    private CmsFieldEnum.CmsFieldEnumIntf value;

    public CmsWord()
    {
        setWordType(WordType.CMS);
    }

    public CmsWord (CmsFieldEnum.CmsFieldEnumIntf value) {
        this();
        this.value = value;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof CmsWord))
        {
            return false;
        }
        CmsWord cmsWord = (CmsWord) obj;
        return cmsWord.getValue().equals(value);
    }

    public CmsFieldEnum.CmsFieldEnumIntf getValue() {
        return value;
    }

    public void setValue(CmsFieldEnum.CmsFieldEnumIntf value) {
        this.value = value;
    }
}
