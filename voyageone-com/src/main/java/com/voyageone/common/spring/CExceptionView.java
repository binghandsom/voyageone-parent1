package com.voyageone.common.spring;

import org.springframework.web.servlet.view.AbstractView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Created by admin on 2014/11/7.
 */
public class CExceptionView extends AbstractView {

    @Override
    protected void renderMergedOutputModel(Map model,
                                           HttpServletRequest req, HttpServletResponse res) throws Exception {


        res.getWriter().print("");
    }
}