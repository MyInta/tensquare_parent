package com.tensquare.jwt;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

/**
 * @author inta
 * @date 2019/11/4
 * @describe
 */
public class CreateJwt {
    public static void main(String[] args) {
        JwtBuilder jwtBuilder = Jwts.builder()
                .setId("666")
                .setSubject("小王")
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS256, "inta")
                .setExpiration(new Date(new Date().getTime() + 60_000))
                .claim("role", "admin");
        System.out.println(jwtBuilder.compact());
    }
}
