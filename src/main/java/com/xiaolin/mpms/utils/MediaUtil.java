/*
 * Copyright © 2023 - 现在. LMY. All Rights Reserved.
 */

package com.xiaolin.mpms.utils;

import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.avcodec;

public class MediaUtil {

    /**
     * 播放时长转为时:分:秒.毫秒
     *
     * @param duration 播放时长
     * @return 时:分:秒.毫秒
     */
    public static String transPlayTime(long duration) {
        int hour = (int) (duration / 3600);
        int minute = (int) (duration % 3600 / 60);
        int second = (int) (duration % 60);
        return String.format("%02d:%02d:%02d", hour, minute, second);
    }


    public static String getCodecById(int id) {
        try (BytePointer bytePointer = avcodec.avcodec_get_name(id)) {
            return bytePointer.getString();
        } catch (Exception e) {
            return "";
        }
    }
}
