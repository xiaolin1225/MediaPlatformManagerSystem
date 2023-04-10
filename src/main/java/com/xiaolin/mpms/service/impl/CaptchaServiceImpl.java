/*
 * Copyright © 2023 - 现在. LMY. All Rights Reserved.
 */

package com.xiaolin.mpms.service.impl;

import com.google.code.kaptcha.Producer;
import com.xiaolin.mpms.entity.Captcha;
import com.xiaolin.mpms.service.CaptchaService;
import com.xiaolin.mpms.utils.RedisCache;
import com.xiaolin.mpms.utils.UUIDUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
public class CaptchaServiceImpl implements CaptchaService {

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private Producer mathProducer;

    @Value("${code.timeout:300}")
    private Integer timeout;

    @Override
    public Captcha generateCaptcha(String codeKey, String type) {
        if (Objects.isNull(codeKey) || StringUtils.isEmpty(codeKey)) {
            codeKey = UUIDUtil.getUUID();
        }
        //生成验证码字符文本
        String capText = mathProducer.createText();
        String capStr = capText.substring(0, capText.lastIndexOf("@"));
        String code = capText.substring(capText.lastIndexOf("@") + 1);
        BufferedImage image = mathProducer.createImage(capStr);
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            ImageIO.write(image, "jpg", outputStream);
            // 对字节数组Base64编码
            BASE64Encoder encoder = new BASE64Encoder();
            String headerStr = "data:image/jpeg;base64,";
            String base64Img = headerStr + encoder.encode(outputStream.toByteArray()).replace("\n", "").replace("\r", "");
            Captcha captcha = new Captcha(codeKey, base64Img, type, timeout);
            redisCache.setCacheObject("captcha:" + type + ":" + codeKey, code, timeout, TimeUnit.SECONDS);
            return captcha;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Boolean isCaptchaCorrect(String type, String codeKey, String code) {
        if (StringUtils.isEmpty(codeKey) || StringUtils.isEmpty(code))
            return false;
        String cacheCode = redisCache.getCacheObject("captcha:" + type + ":" + codeKey);
        return StringUtils.equals(code, cacheCode);
    }

}
