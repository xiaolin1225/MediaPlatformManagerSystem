/*
 * Copyright © 2023 - 现在. LMY. All Rights Reserved.
 */

package com.xiaolin.mpms.utils;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class MPFastAutoGeneratorTest {

    @Test
    public void generator() {
        List<String> includes = new ArrayList<>();
        includes.add("xl_chat_contact");
        includes.add("xl_chat_message");
        List<String> prefix = new ArrayList<>();
        prefix.add("xl_");
        MPFastAutoGenerator.generator("jdbc:mysql://localhost:3306/mpms?useUnicode=true&characterEncoding=utf8&serverTimezone=GMT%2b8",
                "root",
                "Lmy1221@my",
                "D://新建文件夹/mpms/mapper",
                "com.xiaolin",
                "mpms",
                includes,
                prefix
        );

    }
}