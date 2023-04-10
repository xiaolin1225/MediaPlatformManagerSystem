/*
 * Copyright © 2023 - 现在. LMY. All Rights Reserved.
 */

package com.xiaolin.mpms.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author xiaolin
 * @since 2023-03-18
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "Base64验证码", description = "")
public class Captcha implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("验证码KEY")
    private String key;

    @ApiModelProperty("Base64图片")
    private String base64Image;

    @ApiModelProperty("验证码类型")
    private String type;

    @ApiModelProperty("过期时间")
    private Integer timeout;
}
