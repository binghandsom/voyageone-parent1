package com.voyageone.ims.rule_expression;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Leo on 15-6-2.
 * @version 2.0.0
 * @since 1.1.0
 */
public class RuleExpression {
    private List<RuleWord> ruleWordList;

    public RuleExpression() {
        ruleWordList = new ArrayList<>();
    }

    public void addRuleWord(RuleWord ruleWord) {
        ruleWordList.add(ruleWord);
    }

    public RuleWord removeRuleWord(RuleWord ruleWord) {
        Iterator<RuleWord> it$ = ruleWordList.iterator();
        while (it$.hasNext()) {
            RuleWord iterRuleWord = it$.next();
            if (iterRuleWord.equals(ruleWord)) {
                it$.remove();
                return ruleWord;
            }
        }
        return null;
    }

    public void clear() {
        ruleWordList.clear();
    }

    @Override
    public boolean equals(Object obj) {

        if (obj == null)
            return false;

        if (!(obj instanceof RuleExpression))
            return false;

        RuleExpression ruleExpressionCompare = (RuleExpression) obj;

        for (RuleWord ruleWord : ruleWordList) {
            boolean findIt = false;
            for (RuleWord ruleWordCompare : ruleExpressionCompare.getRuleWordList()) {
                if (ruleWord.equals(ruleWordCompare))
                    findIt = true;
            }
            if (!findIt)
                return false;
        }
        return true;
    }

    public List<RuleWord> getRuleWordList() {
        return ruleWordList;
    }

    public void setRuleWordList(List<RuleWord> ruleWordList) {
        this.ruleWordList = ruleWordList;
    }
}
