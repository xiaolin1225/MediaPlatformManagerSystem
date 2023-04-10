/*
 * Copyright © 2023 - 现在. LMY. All Rights Reserved.
 */

package com.xiaolin.mpms.service;

import com.xiaolin.mpms.entity.Captcha;

public interface CaptchaService {
    Captcha generateCaptcha(String codeKey, String type);

    Boolean isCaptchaCorrect(String type, String codeKey, String code);
}
