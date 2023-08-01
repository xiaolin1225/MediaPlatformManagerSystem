/*
 * Copyright © 2023 - 现在. LMY. All Rights Reserved.
 */

package com.xiaolin.mpms.utils.platform;

import com.xiaolin.mpms.service.MediaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class WXUtilsTest {

    @Autowired
    private MediaService mediaService;

    @Test
    void getAccessToken() {
        WXUtils.getAccessToken("wx3eb89c91573749ec", "6274d3fbe2b5f24accb4b89ef4f6e52b");
    }

    @Test
    void uploadMedia() {
        File file = new File(mediaService.getRealPath("/upload/887f9ddecec54459aa34e727c235799d.png"));
        System.out.println(WXUtils.uploadMedia("68_otoAlD3pG-tBBWo9sCjkLspCpOH-FYBbXs5LV4A799mrBox3CiUiM7UcChcE7ly7vgDgv10j2KlJwmoBBmTxL1Z1M_olLDc-ooPmfAXpliInO-DTb_ujqeCkW1cQIEeAAAXPL", "image", "/upload/887f9ddecec54459aa34e727c235799d.png"));
    }
}