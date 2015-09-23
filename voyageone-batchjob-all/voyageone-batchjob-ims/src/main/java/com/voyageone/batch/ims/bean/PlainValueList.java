package com.voyageone.batch.ims.bean;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Leo on 15-6-12.
 */
public class PlainValueList {
    private List<PlainValueWord> plainValues;

    public PlainValueList() {
        plainValues = new ArrayList<>();
    }

    public List<PlainValueWord> getPlainValues() {
        return plainValues;
    }

    public void setPlainValues(List<PlainValueWord> plainValues) {
        this.plainValues = plainValues;
    }

    public void addPlainValueWord(PlainValueWord plainValueWord)
    {
        plainValues.add(plainValueWord);
    }

    @Override
    public String toString() {
        if (plainValues != null) {
            StringBuffer stringBuffer = new StringBuffer();
            for (PlainValueWord plainValueWord : plainValues)
            {
               stringBuffer.append(plainValueWord.getValue());
            }
            return stringBuffer.toString();
        }
        return null;
    }

    public Set<String> getUrlSet()
    {
        if (plainValues == null)
            return null;

        Set<String> urlSet = new HashSet<>();

        for (PlainValueWord plainValueWord : plainValues)
        {
            if (plainValueWord.isUrl())
            {
                urlSet.add(plainValueWord.getValue());
            }
        }
        return urlSet;
    }
}
