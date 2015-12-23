package com.voyageone.synship.controller;

import com.voyageone.synship.SynshipConstants.ItemDetailUrls;
import com.voyageone.synship.service.ItemDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping(method = RequestMethod.POST, produces = {"text/html;charset=UTF-8"})
public class ItemDetailController {

    @Autowired
    ItemDetailService itemDetailService;

    @RequestMapping(value = ItemDetailUrls.IMPORT)
    public void importItemDetail(@RequestBody String param) throws UnsupportedEncodingException {

        itemDetailService.importItemDetail(param);
    }
}
