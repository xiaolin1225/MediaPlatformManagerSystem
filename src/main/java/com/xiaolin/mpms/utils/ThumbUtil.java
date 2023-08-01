/*
 * Copyright © 2023 - 现在. LMY. All Rights Reserved.
 */

package com.xiaolin.mpms.utils;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class ThumbUtil {
    /**
     * 水印图片
     */
    private static File markIco = null;

    //开机静态加载水印图片
    static {
        try {
            markIco = new File(new File("").getCanonicalPath() + "/upload/system/watermark.png");
            System.out.println("水印图片加载" + (markIco.exists() ? "成功" : "失败"));
        } catch (Exception e) {
            System.out.println("水印图片加载失败");
        }
    }

    public static void createThumb(File sourceFile, File thumbFile, int width, int height) {
        try {
            Thumbnails.of(sourceFile)
                    .size(width, height)
                    .watermark(Positions.BOTTOM_CENTER, ImageIO.read(markIco), 1f)
                    .outputQuality(1f)
                    .toFile(thumbFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 加水印
     */
    public static void photoMark(File sourceFile, File toFile) throws IOException {
        Thumbnails.of(sourceFile)
                .size(600, 450)//尺寸
                .watermark(Positions.BOTTOM_CENTER/*水印位置：中央靠下*/,
                        ImageIO.read(markIco), 0.7f/*质量，越大质量越高(1)*/)
                //.outputQuality(0.8f)
                .toFile(toFile);//保存为哪个文件
    }
}
