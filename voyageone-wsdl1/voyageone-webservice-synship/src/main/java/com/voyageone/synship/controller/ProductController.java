package com.voyageone.synship.controller;

import com.voyageone.synship.SynshipConstants.ProductUrls;
import com.voyageone.synship.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping(method = RequestMethod.POST, produces = {"text/html;charset=UTF-8"})
public class ProductController {

    @Autowired
    ProductService productService;

    @RequestMapping(value = ProductUrls.IMPORT)
    public void importProduct(@RequestBody String param) throws UnsupportedEncodingException {

        productService.importProduct(param);
    }
}
