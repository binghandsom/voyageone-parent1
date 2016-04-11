package com.voyageone.components.gilt.service;

import org.apache.commons.codec.binary.Base64;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


/**
 * @author aooer 2016/2/14.
 * @version 2.0.0
 * @since 2.0.0
 */
public class GiltAuthorizationGetTest {

    public static void main(String[] args) {
        //token
        String token="a197f33c5faafdcd67bfb68132ba834f";
        System.out.println("Authorization: Basic "+ Base64.encodeBase64String(token.getBytes()));

        //user:password
        token="a197f33c5faafdcd67bfb68132ba834f:";
        System.out.println("Authorization: Basic "+ Base64.encodeBase64String(token.getBytes()));
    }

}
