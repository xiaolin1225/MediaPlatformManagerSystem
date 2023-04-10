/*
 * Copyright © 2023 - 现在. LMY. All Rights Reserved.
 */
package com.xiaolin.mpms.config.Captcha;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * 验证码配置
 *
 * @author 小林
 * @since 2022-12-12 22:42
 **/

@Configuration
public class CaptchaConfig {

    @Bean
    public DefaultKaptcha mathProducer() {
        // 验证码生成器
        DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
        // 配置
        Properties properties = new Properties();
        // 是否有边框 默认为true 我们可以自己设置yes，no
        properties.setProperty("kaptcha.border", "no");
        //  边框颜色 默认为Color.BLACK
        properties.setProperty("kaptcha.border.color", "64,158,255");
        //  验证码文本字符颜色 默认为Color.BLACK
        properties.setProperty("kaptcha.textproducer.font.color.", "blue");
        //  验证码图片宽度 默认为200
        properties.setProperty("kaptcha.image.width", "150");
        //  验证码图片高度 默认为50
        properties.setProperty("kaptcha.image.height", "50");
        //  验证码文本字符大小 默认为40
        properties.setProperty("kaptcha.textproducer.font.size", "35");
        //  KAPTCHA_SESSION_KEY
        properties.setProperty("kaptcha.session.key", "kaptchaCodeMath");
        //  验证码文本生成器
        properties.setProperty("kaptcha.textproducer.impl", "com.xiaolin.mpms.config.Captcha.CaptchaTextCreator");
        //  验证码文本字符间距 默认为2
        properties.setProperty("kaptcha.textproducer.char.space", "3");
        //  验证码文本字符长度 默认为5
        properties.setProperty("kaptcha.textproducer.char.length", "6");
        //  验证码文本字体样式 默认为new Font("Arial", 1, fontSize), new Font("Courier", 1, fontSize)
        properties.setProperty("kaptcha.textproducer.font.names", "Arial,Courier");
        //  验证码噪点颜色 默认为Color.BLACK
        properties.setProperty("kaptcha.noise.color", "white");
        //  干扰实现类
        properties.setProperty("kaptcha.noise.impl", "com.google.code.kaptcha.impl.NoNoise");
        //  图片样式 水纹com.google.code.kaptcha.impl.WaterRipple 鱼眼com.google.code.kaptcha.impl.FishEyeGimpy 阴影com.google.code.kaptcha.impl.ShadowGimpy
        properties.setProperty("kaptcha.obscurificator.impl", "com.google.code.kaptcha.impl.ShadowGimpy");
        Config config = new Config(properties);
        defaultKaptcha.setConfig(config);
        return defaultKaptcha;
    }

    @Bean
    public DefaultKaptcha textProducer() {
        // 验证码生成器
        DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
        // 配置
        Properties properties = new Properties();
        // 是否有边框 默认为true 我们可以自己设置yes，no
        properties.setProperty("kaptcha.border", "no");
        //  验证码文本字符颜色 默认为Color.BLACK
        properties.setProperty("kaptcha.textproducer.font.color.", "blue");
        //  验证码图片宽度 默认为200
        properties.setProperty("kaptcha.image.width", "160");
        //  验证码图片高度 默认为50
        properties.setProperty("kaptcha.image.height", "60");
        //  验证码文本字符大小 默认为40
        properties.setProperty("kaptcha.textproducer.font.size", "40");
        //  验证码字符长度
        properties.setProperty("kaptcha.textproducer.char.length", "4");
        Config config = new Config(properties);
        defaultKaptcha.setConfig(config);
        return defaultKaptcha;
    }
}
