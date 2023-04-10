/*
 * Copyright © 2023 - 现在. LMY. All Rights Reserved.
 */

package com.xiaolin.mpms.utils;

public class UUIDUtil {

        /**
        * 获取UUID
        * @return UUID
        */
        public static String getUUID(){
            return java.util.UUID.randomUUID().toString().replace("-", "");
        }
}
