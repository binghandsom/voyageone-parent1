package com.voyageone.security.shiro.filter;


import com.voyageone.security.dao.ComUserDao;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.web.filter.PathMatchingFilter;
import org.springframework.beans.factory.annotation.Autowired;

import javax.inject.Inject;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.util.HashMap;
import java.util.Map;


public class SysUserFilter extends PathMatchingFilter {

	@Autowired
	private ComUserDao comUserDao;

    @Override
    protected boolean onPreHandle(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {

        String username = (String)SecurityUtils.getSubject().getPrincipal();
        Map userFormMap = new HashMap<>();
		userFormMap.put("accountName", "" + username + "");
//        request.setAttribute("user", userMapper.findByNames(userFormMap));

        request.setAttribute("user", comUserDao.selectOne(userFormMap));

        return true;
    }
}