/*
 * Copyright © 2023 - 现在. LMY. All Rights Reserved.
 */

package com.xiaolin.mpms.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author xiaolin
 * @since 2023-02-26
 */

@Data
@AllArgsConstructor
@ApiModel(value = "分页数据实体类", description = "用于保存需要分页的数据")
public class MyPage<T> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "当前页")
    private Integer current;

    @ApiModelProperty(value = "总页数")
    private Integer pageNum;

    @ApiModelProperty(value = "每页条数")
    private Integer size;

    @ApiModelProperty(value = "总条数")
    private Integer total;

    @ApiModelProperty(value = "分页数据")
    private List<T> records;

    public MyPage() {
        this.current = 1;
        this.pageNum = 0;
        this.size = 10;
        this.total = 0;
        this.records = new ArrayList<>();
    }

    public MyPage(Integer current, Integer size) {
        this.current = current;
        this.pageNum = 0;
        this.size = size;
        this.total = 0;
        this.records = new ArrayList<>();
    }
}
