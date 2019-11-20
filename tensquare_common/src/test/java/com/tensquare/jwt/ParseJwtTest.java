package com.tensquare.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import java.text.SimpleDateFormat;

/**
 * @author inta
 * @date 2019/11/5
 * @describe
 */
public class ParseJwtTest {
    public static void main(String[] args) {
        try {
            Claims claims = Jwts.parser().setSigningKey("inta")
                    .parseClaimsJws("eyJhbGciOiJIUzI1NiJ9." +
                            "eyJqdGkiOiI2NjYiLCJzdWIiOiLlsI_njosiLCJpYXQiOjE1NzI5NzIwMzQsImV4cCI6MTU3Mjk3MjA5NCwicm9sZSI6ImFkbWluIn0." +
                            "ih5Y4gsIDzFGlLyhsH9-kTtDj2UoQuOCWRSrhMPSzjY")
                    .getBody();
            System.out.println("用户id: " + claims.getId());
            System.out.println("用户名: " + claims.getSubject());
            System.out.println("登陆时间: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(claims.getIssuedAt()));
            System.out.println("过期时间: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(claims.getExpiration()));
            System.out.println("用户角色: " + claims.get("role"));
        } catch (Exception e) {
            throw new RuntimeException("已过期！");
        }
    }
}
