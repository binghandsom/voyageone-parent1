package com.voyageone.service.impl.cms.sx.rule_parser;

import com.voyageone.common.logger.VOAbsLoggable;
import com.voyageone.ims.rule_expression.RuleWord;
import com.voyageone.ims.rule_expression.SubCodeWord;
import com.voyageone.ims.rule_expression.WordType;
import com.voyageone.service.bean.cms.product.SxData;

/**
 * Created by tom.zhu on 16-12-8.
 * 获取SxData里的productList里， 指定idx商品的指定common属性
 * 注意： 会跳过主商品， 主商品不占用idx
 */
public class SubCodeWordParser extends VOAbsLoggable {

    private SxData sxData;

    public SubCodeWordParser(SxData sxData) {
        this.sxData = sxData;
    }

    // 获取指定index的商品的属性（跳过主商品）
    public String parse(RuleWord ruleWord) throws Exception {

        if (!WordType.SUBCODE.equals(ruleWord.getWordType())) {
            return null;
        } else {
            SubCodeWord subCodeWord = (SubCodeWord) ruleWord;
            int codeIdx = subCodeWord.getCodeIdx(); // 指定第几个商品
            String propName = subCodeWord.getValue(); // 属性名称

            // 获取主商品的code
            String mainCode = sxData.getMainProduct().getCommon().getFields().getCode();
            // 看看主商品的idx
            int idxMain = 999;
            for (int i = 0; i < sxData.getProductList().size(); i++) {
                if (mainCode.equals(sxData.getProductList().get(i).getCommon().getFields().getCode())) {
                    idxMain = i;
                    break;
                }
            }
            if (idxMain <= codeIdx) {
                // 如果要获取的商品之前有个主商品， idx + 1
                codeIdx++;
            }
            // 看看指定的idx是否有存在
            if (codeIdx >= sxData.getProductList().size()) {
                // 超出索引外
                return "";
            }

            // 看看指定的属性是否存在
            if (sxData.getProductList().get(codeIdx).getCommon().getFields().containsKey(propName)) {
                return sxData.getProductList().get(codeIdx).getCommon().getFields().getAttribute(propName);
            }

        }

        return null;
    }

}
