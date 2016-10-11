package com.voyageone.web2.core.filter;


import com.voyageone.common.spring.serializer.CJacksonSerializerUtil;

import java.io.IOException;
import javax.servlet.*;

public class CustomCoverterFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // This method intentionally left blank
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        CJacksonSerializerUtil.setCustom(true);
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
        // This method intentionally left blank
    }
}
