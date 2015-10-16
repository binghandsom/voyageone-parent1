package com.voyageone.batch.ims.service.rule_parser;

import com.voyageone.batch.ims.modelbean.CmsCodePropBean;
import com.voyageone.batch.ims.modelbean.CmsModelPropBean;
import com.voyageone.ims.enums.CmsFieldEnum;
import com.voyageone.ims.rule_expression.CmsWord;
import com.voyageone.ims.rule_expression.RuleWord;
import com.voyageone.ims.rule_expression.WordType;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Created by Leo on 15-6-18.
 */
public class CmsWordParser {
    private CmsModelPropBean cmsModelProp;

    private CmsCodePropBean mainProduct;

    public CmsWordParser(CmsModelPropBean cmsModelProp, CmsCodePropBean mainProduct) {
        this.cmsModelProp = cmsModelProp;
        this.mainProduct=mainProduct;
    }

    //目前只支持解析model级别的属性
    public String parse(RuleWord ruleWord)
    {
        if (!WordType.CMS.equals(ruleWord.getWordType()))
        {
            return null;
        }
        else
        {
            CmsWord cmsWord = (CmsWord) ruleWord;

            String wordValue = cmsWord.getValue().toString();

            if(wordValue.startsWith("attribute")){

                String[] names = wordValue.split("_");

                int index = Integer.valueOf(names[1]);

                String returnKey = names[2];

                List<Map<String,Object>> attributes =  mainProduct.getAttributeNameValueList();

                if(!attributes.isEmpty()){
                    try{
                        Map<String,Object> tarMap = attributes.get(index-1);

                        StringBuilder resultValue = new StringBuilder();

                        for (Entry<String, Object> entry : tarMap.entrySet()){
                            if("key".equals(returnKey)) {
                                resultValue.append(entry.getKey());
                            }else {
                                resultValue.append(entry.getValue());
                            }
                        }

                        return  resultValue.toString();

                    }catch (Exception e){

                        return "" ;
                    }

                }

                return "";

            } else {
                return cmsModelProp.getProp((CmsFieldEnum.CmsModelEnum) cmsWord.getValue());
            }

        }
    }
}
