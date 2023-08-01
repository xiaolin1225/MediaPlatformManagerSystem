/*
 * Copyright © 2023 - 现在. LMY. All Rights Reserved.
 */

package com.xiaolin.mpms.entity.content;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xiaolin.mpms.entity.common.BaseEntry;
import com.xiaolin.mpms.entity.user.User;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

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
@EqualsAndHashCode(callSuper = true)
@TableName("xl_content_check_user")
@ApiModel(value = "内容审核人员", description = "")
public class ContentCheckUser extends BaseEntry {

    private static final long serialVersionUID = 1L;
    @ApiModelProperty("序号")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("内容ID")
    private Integer checkId;

    @ApiModelProperty("审核人员ID")
    private String uid;

    @ApiModelProperty("审核人员信息")
    @TableField(exist = false)
    private User user;

    @ApiModelProperty("审核级别")
    private String level;

    @ApiModelProperty("审核序列")
    @TableField("`index`")
    private Integer index;

    @ApiModelProperty("审核结果")
    private String checkResult;

    @ApiModelProperty("审核批注")
    private String commit;

    @ApiModelProperty("提交版本")
    private Integer checkVersion;

}
