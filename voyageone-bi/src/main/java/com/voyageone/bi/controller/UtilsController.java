package com.voyageone.bi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@SessionAttributes
public class UtilsController {
	
    @RequestMapping(value = "/clearCache")
    public String clearCache() {
    	return "manage/utils/clearCache"; 
    }
    

}