package util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Created by Administrator on 2018/4/11.
 */
@ConfigurationProperties("jwt.config")
public class JwtUtil {

    //密钥
    private String key ;

    private long ttl ;//设一个时间

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public long getTtl() {
        return ttl;
    }

    public void setTtl(long ttl) {
        this.ttl = ttl;
    }

    /**
     * 生成JWT
     *
     * @param id
     * @param subject
     * @return
     */
    public String createJWT(String id, String subject, String roles) {
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);//当前时间
        JwtBuilder builder = Jwts.builder()
                .setId(id)
                .setSubject(subject)
                .setIssuedAt(now)//设置签发时间
                .signWith(SignatureAlgorithm.HS256, key)//设置签名密钥
                .claim("roles", roles);//申明一个key实际意义为用户类别（管理员、普通用户...）
        if (ttl > 0) {
            builder.setExpiration( new Date( nowMillis + ttl));//当前时间往后加ttl时间
        }
        return builder.compact();
    }

    /**
     * 解析JWT
     * @param jwtStr 即 token
     * @return
     */
    public Claims parseJWT(String jwtStr){
        return  Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(jwtStr)
                .getBody();
    }

}
