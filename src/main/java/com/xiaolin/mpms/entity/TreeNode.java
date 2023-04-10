/*
 * Copyright © 2023 - 现在. LMY. All Rights Reserved.
 */

package com.xiaolin.mpms.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TreeNode implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private Integer pid;
    private String label;
    private Object value;
    private List<TreeNode> children;
    private Boolean isLeaf;
    private Boolean disabled;

    public TreeNode(Integer id, Integer pid, String label, Object value) {
        this.id = id;
        this.pid = pid;
        this.label = label;
        this.value = value;
        this.children = new ArrayList<>();
        this.isLeaf = false;
        this.disabled = false;
    }

    public TreeNode(Integer id, Integer pid, String label, Object value, Boolean isLeaf) {
        this.id = id;
        this.pid = pid;
        this.label = label;
        this.value = value;
        this.children = new ArrayList<>();
        this.isLeaf = isLeaf;
        this.disabled = false;
    }
}
