/*
 * Copyright © 2023 - 现在. LMY. All Rights Reserved.
 */

package com.xiaolin.mpms.utils;

import com.xiaolin.mpms.entity.user.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.*;

@Component
public class JWTUtils {
    private final String CLAIM_KEY_UID = "sub";
    private final String CLAIM_KEY_CREATED = "created";

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    public String getUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * 根据用户信息生成token
     *
     * @param user 用户信息
     * @return token
     */
    public String generateToken(User user, Long customExpiration) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_KEY_UID, user.getUid());
        claims.put(CLAIM_KEY_CREATED, new Date());
        return generateToken(claims, customExpiration);
    }


    /**
     * 根据荷载生成token
     *
     * @param claims 荷载
     * @return token
     */
    public String generateToken(Map<String, Object> claims, Long customExpiration) {
        return Jwts.builder()
                .setId(getUUID())
                .setClaims(claims)
                .setIssuer("小林")
                .setIssuedAt(new Date())
                .setExpiration(generateExpirationDate(customExpiration))
                .signWith(generalKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * 从token中获取username
     *
     * @param token token
     * @return 用户名
     */
    public String getUidFromToken(String token) {
        try {
            Claims claims = getClaimsFromToken(token);
            return claims.getSubject();
        } catch (Exception e) {
            throw new RuntimeException("非法请求");
        }
    }

    /**
     * 从token中获取荷载
     *
     * @param token token
     * @return 荷载
     */
    public Claims getClaimsFromToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parserBuilder()
                    .setSigningKey(generalKey())
                    .build().parseClaimsJws(token).getBody();
        } catch (Exception e) {
            throw new RuntimeException("非法请求");
        }
        return claims;
    }

    /**
     * 验证token是否有效
     *
     * @param token token
     * @param user  用户信息
     * @return token是否有效
     */
    public boolean validateToken(String token, User user) {
        String uid = getUidFromToken(token);
        return uid.equals(user.getUid()) && !isTokenExpired(token);
    }

    /**
     * 判断token是否过期
     *
     * @param token token
     * @return token是否过期
     */
    private boolean isTokenExpired(String token) {
        Date expireDate = getExpireDateFromToken(token);
        return expireDate.before(new Date());
    }

    /**
     * 从token中获取过期时间
     *
     * @param token token
     * @return 过期时间
     */
    private Date getExpireDateFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.getExpiration();
    }

    /**
     * 判断token是否可以被刷新
     *
     * @param token token
     * @return token是否可以被刷新
     */
    public boolean canRefresh(String token) {
        return !isTokenExpired(token);
    }

    /**
     * 刷新token
     *
     * @param token 旧的token
     * @return 新的token
     */
    public String refreshToken(String token, Long customExpiration) {
        Claims claims = getClaimsFromToken(token);
        claims.put(CLAIM_KEY_CREATED, new Date());
        return generateToken(claims, customExpiration);
    }

    /**
     * 生成过期时间
     *
     * @return 过期时间
     */
    private Date generateExpirationDate(Long customExpiration) {
        return new Date(System.currentTimeMillis() + (customExpiration != null ? customExpiration : expiration));
    }

    /**
     * 生成加密后的密匙
     *
     * @return 密匙
     */
    private SecretKey generalKey() {
        byte[] encodedKey = Base64.getDecoder().decode(secret);
        return new SecretKeySpec(encodedKey, 0, encodedKey.length, "HmacSHA256");
    }
}
