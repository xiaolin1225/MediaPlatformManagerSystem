/*
 * Copyright © 2023 - 现在. LMY. All Rights Reserved.
 */

package com.xiaolin.mpms.entity.user;

import com.alibaba.fastjson2.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthUser implements UserDetails {

    private User user;

    private List<String> permissions;

    @JSONField(serialize = false)
    @JsonIgnore
    private List<SimpleGrantedAuthority> authorities;

    public AuthUser(User user, List<String> list) {
        this.user = user;
        this.permissions = list;
    }

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (authorities != null)
            return authorities;
        // 将permissions中的权限信息封装为SimpleGrantedAuthority对象
        if (permissions == null || permissions.isEmpty()) {
            return new ArrayList<>();
        }
        authorities = permissions.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
        return authorities;
    }


    @Override
    @JsonIgnore
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    @JsonIgnore
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return user.getStatus() != 3;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return user.getStatus() != 4;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return user.getIsEnable();
    }
}
