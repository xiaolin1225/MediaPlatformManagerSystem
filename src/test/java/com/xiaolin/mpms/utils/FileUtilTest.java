/*
 * Copyright © 2023 - 现在. LMY. All Rights Reserved.
 */

package com.xiaolin.mpms.utils;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

class FileUtilTest {

    private static String UPLOAD_PATH;

    @Test
    void getExtensionFromBase64() {
        String avatar = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAALMAAACzCA";
        Pattern pattern = Pattern.compile("^data:image/(.+?);base64,");
        Matcher matcher = pattern.matcher(avatar);
        System.out.println(matcher.replaceAll(""));
        System.out.println(avatar);
    }

    public static String getFileExtensionFromBase64(String base64Data) {
        String fileExtension = null;
        if (base64Data != null && !base64Data.isEmpty()) {
            Pattern pattern = Pattern.compile("^data:image/(.+?);base64,");
            Matcher matcher = pattern.matcher(base64Data);
            if (matcher.find()) {
                fileExtension = matcher.group(1);
            }
        }
        return fileExtension;
    }

}