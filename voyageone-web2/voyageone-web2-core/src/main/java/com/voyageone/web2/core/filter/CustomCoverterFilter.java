package com.voyageone.web2.core.filter;

import com.voyageone.common.spring.serializer.CustomServletOutputStream;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.ws.rs.core.MediaType;

public class CustomCoverterFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // This method intentionally left blank
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse wrappedResponse = new HttpServletResponseWrapper((HttpServletResponse) servletResponse) {
            @Override
            public ServletOutputStream getOutputStream() throws java.io.IOException {
                ServletResponse response = this.getResponse();

                String ct = (response != null) ? response.getContentType() : null;
                boolean isneedCustom = false;
                if (ct != null && ct.toLowerCase().startsWith(MediaType.APPLICATION_JSON)) {
                    isneedCustom = true;
                }

                return new CustomServletOutputStream(super.getOutputStream(), isneedCustom);
            }
        };

        filterChain.doFilter(servletRequest, wrappedResponse);
    }

    @Override
    public void destroy() {
        // This method intentionally left blank
    }
}
