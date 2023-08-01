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
public class WXBaseMsg {
    private Integer errcode;

    private String errmsg;

    private String publish_id;    //发布任务的id
    private String msg_data_id;    //消息的数据ID
}
