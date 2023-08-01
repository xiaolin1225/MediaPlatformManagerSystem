/*
 * Copyright © 2023 - 现在. LMY. All Rights Reserved.
 */

package com.xiaolin.mpms.utils.text;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class encryptUtilsTest {

    @Test
    void encryptByPrivateKey() throws Exception {
        String text = "BXTAZIZO+4OUApCYUbuIzVatB3nrXKrlKUmFCASl9Md+F1zDPJq3ryA47lAvSrUmsSfWt/PvrMf0ohVzrq8rbWXjCDXPDAAqNfWeymtCrj63ZVsP0AVxpdgnA3KzbthbrGRosFuPWtKYkC9kNulr7qHzktURhxbKX9GgpYjghyY=";
        System.out.println(EncryptUtils.decryptByPrivateKey(text));
    }
}