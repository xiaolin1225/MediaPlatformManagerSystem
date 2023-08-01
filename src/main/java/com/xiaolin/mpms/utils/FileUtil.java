/*
 * Copyright © 2023 - 现在. LMY. All Rights Reserved.
 */

package com.xiaolin.mpms.utils;

import com.xiaolin.mpms.exception.FileException;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.RegExUtils;

import java.io.*;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Log4j2
public class FileUtil {

    private static String UPLOAD_PATH;

    private static String SERVER_PATH;

    public static String fileSizeByteToM(Long size) {

        BigDecimal fileSize = new BigDecimal(size);
        BigDecimal param = new BigDecimal(1024);
        int count = 0;
        while (fileSize.compareTo(param) > 0 && count < 5) {
            fileSize = fileSize.divide(param);
            count++;
        }
        DecimalFormat df = new DecimalFormat("#.##");
        String result = df.format(fileSize) + "";
        switch (count) {
            case 0:
                result += "B";
                break;
            case 1:
                result += "KB";
                break;
            case 2:
                result += "MB";
                break;
            case 3:
                result += "GB";
                break;
            case 4:
                result += "TB";
                break;
            case 5:
                result += "PB";
                break;
        }
        return result;
    }


    public static String getUploadPath() {
        if (UPLOAD_PATH == null || UPLOAD_PATH.trim().isEmpty()) {
            String uploadPath;
            File directory = new File("");
            try {
                uploadPath = directory.getCanonicalPath() + File.separator + "upload" + File.separator;
            } catch (IOException e) {
                uploadPath = directory.getAbsolutePath() + File.separator + "upload" + File.separator;
            }
            File uploadFolder = new File(uploadPath);
            if (!uploadFolder.exists()) {
                uploadFolder.mkdirs();
            }
            UPLOAD_PATH = uploadPath;
        }
        return UPLOAD_PATH;
    }

    public static String getServerPath() {
        if (SERVER_PATH == null || SERVER_PATH.trim().isEmpty()) {
            String uploadPath;
            File directory = new File("");
            try {
                uploadPath = directory.getCanonicalPath();
            } catch (IOException e) {
                uploadPath = directory.getAbsolutePath();
            }
            File uploadFolder = new File(uploadPath);
            if (!uploadFolder.exists()) {
                uploadFolder.mkdirs();
            }
            SERVER_PATH = uploadPath;
        }
        return SERVER_PATH;
    }

    public static File base64ToFile(String base64Str, String outputPath) {
        if (base64Str == null) {
            throw new FileException(3001, "文件数据异常");
        }
        Pattern pattern = Pattern.compile("^data:image/(.+?);base64,");
        Matcher matcher = pattern.matcher(base64Str);
        if (!matcher.find()) {
            throw new FileException(3002, "文件格式不支持");
        }
        base64Str = matcher.replaceAll("");
        byte[] bytes = Base64.decodeBase64(base64Str);
        for (int i = 0; i < bytes.length; i++) {
            if (bytes[i] < 0) {
                bytes[i] += 256;
            }
        }
        File file = new File(outputPath);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        try (OutputStream outputStream = Files.newOutputStream(Paths.get(outputPath))) {
            outputStream.write(bytes);
            return new File(outputPath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
