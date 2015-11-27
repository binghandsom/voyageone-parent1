package com.voyageone.web2.core.views;

import com.voyageone.web2.base.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Index 路径
 * Created on 11/26/15.
 *
 * @author Jonas
 * @version 2.0.0
 */
@RestController
@RequestMapping(value = "/core/user/access/", method = RequestMethod.POST)
public class IndexController extends BaseController {
    @Autowired
    private IndexService indexService;

    @RequestMapping("login")
    public String login(@RequestBody Map<String, Object> params) {
        return "Hello Web2";
    }
}
