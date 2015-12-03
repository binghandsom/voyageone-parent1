package com.voyageone.cms.service;

import java.util.List;

import com.voyageone.cms.formbean.PopUpMtPropSetValueBean.Word;

public interface PopUpMtPropSetValueService {
    
    public Object doSerialize(List<Word> words);
    
    public Object doDeserialize(String wordValue,String... channelId);
    
    public Object doGetComplexValues(int wordType,String... channelId);
    
    
}
