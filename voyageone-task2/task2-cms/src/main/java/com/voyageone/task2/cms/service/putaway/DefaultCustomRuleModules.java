package com.voyageone.task2.cms.service.putaway;

import com.voyageone.task2.Context;
import com.voyageone.task2.cms.service.putaway.word.*;
import org.springframework.context.ApplicationContext;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Leo on 15-6-16.
 */
public class DefaultCustomRuleModules {
    private static DefaultCustomRuleModules defaultITRuleModules;

    private DefaultCustomRuleModules() {
        customWordModules = new HashSet<>();
        Context context = Context.getContext();
        ApplicationContext ctx = (ApplicationContext) context.getAttribute("springContext");

        customWordModules.add(ctx.getBean(CustomWordModuleGetAllImages.class));
        customWordModules.add(ctx.getBean(CustomWordModuleGetMainPropductImages.class));
        customWordModules.add(ctx.getBean(CustomWordModuleImageWithParam.class));
        customWordModules.add(ctx.getBean(CustomWordModuleGetPaddingImageKey.class));
        customWordModules.add(ctx.getBean(CustomWordModuleIf.class));
        customWordModules.add(ctx.getBean(CustomWordModuleConditionAnd.class));
        customWordModules.add(ctx.getBean(CustomWordModuleConditionEq.class));
        customWordModules.add(ctx.getBean(CustomWordModuleConditionNeq.class));
        customWordModules.add(ctx.getBean(CustomWordModuleConditionLike.class));
        customWordModules.add(ctx.getBean(CustomWordModuleGetCommonImages.class));
    }

    public Set<CustomWordModule> customWordModules;


    public CustomWordModule getRuleModule(String value)
    {
        for (CustomWordModule customWordModule : customWordModules)
        {
            if (customWordModule.getModuleName().equals(value))
                return customWordModule;
        }
        return null;
    }

    public static DefaultCustomRuleModules getInstance()
    {
        if(defaultITRuleModules == null)
        {
            defaultITRuleModules = new DefaultCustomRuleModules();
        }
        return defaultITRuleModules;
    }

}
