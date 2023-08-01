/*
 * Copyright © 2023 - 现在. LMY. All Rights Reserved.
 */

package com.xiaolin.mpms.utils.platform;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public
class WXTokenData {
    private String grant_type = "client_credential";

    private String appid;

    private String secret;

    private String access_token;

    private Integer expires_in;

    public WXTokenData(String appid, String secret) {
        this.appid = appid;
        this.secret = secret;
    }
}
