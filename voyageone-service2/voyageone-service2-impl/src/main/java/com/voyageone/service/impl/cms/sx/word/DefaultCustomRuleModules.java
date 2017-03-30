package com.voyageone.service.impl.cms.sx.word;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by morse.lu on 16-4-26.(copy from task2 and then modified)
 */
public class DefaultCustomRuleModules {
    private static DefaultCustomRuleModules defaultITRuleModules;

    private DefaultCustomRuleModules() {
        customWordModules = new HashSet<>();
        customWordModules.add(new CustomWordModuleGetAllImages());
        customWordModules.add(new CustomWordModuleGetMainPropductImages());
        customWordModules.add(new CustomWordModuleGetDescImage());
        customWordModules.add(new CustomWordModuleImageWithParam());
        customWordModules.add(new CustomWordModuleGetProductFieldInfo());
        customWordModules.add(new CustomWordModuleGetPaddingImageKey());
        customWordModules.add(new CustomWordModuleIf());
        customWordModules.add(new CustomWordModuleConditionAnd());
        customWordModules.add(new CustomWordModuleConditionEq());
        customWordModules.add(new CustomWordModuleConditionNeq());
        customWordModules.add(new CustomWordModuleConditionLike());
        customWordModules.add(new CustomWordModuleConditionNLike());
        customWordModules.add(new CustomWordModuleGetCommonImages());
        customWordModules.add(new CustomWordModuleTranslateBaidu());
    }

    public Set<CustomWordModule> customWordModules;


    public CustomWordModule getRuleModule(String value) {
        for (CustomWordModule customWordModule : customWordModules) {
            if (customWordModule.getModuleName().equals(value))
                return customWordModule;
        }
        return null;
    }

    public static DefaultCustomRuleModules getInstance() {
        if (defaultITRuleModules == null) {
            defaultITRuleModules = new DefaultCustomRuleModules();
        }
        return defaultITRuleModules;
    }

}
