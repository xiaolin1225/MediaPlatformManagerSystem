/*
 * Copyright © 2023 - 现在. LMY. All Rights Reserved.
 */

package com.xiaolin.mpms.entity.VO;

import com.xiaolin.mpms.entity.content.ContentCheckUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContentCheckVo {
    private List<ContentCheckUser> checkUser;
    private List<ContentVO> contents;

    private Integer id;

    private Integer processIndex;

    private String process;

    private Integer checkVersion;

    private Integer status;

    private String checkResult;

    private String commit;
}
