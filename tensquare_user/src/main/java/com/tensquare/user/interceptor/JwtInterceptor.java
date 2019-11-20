package com.tensquare.user.interceptor;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import util.JwtUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author inta
 * @date 2019/11/6
 * @describe
 */
@Component
public class JwtInterceptor implements HandlerInterceptor {
    @Autowired
    private JwtUtil jwtUtil;

    //实现空方法
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("经过了拦截器");
        //无论如何都会放行，拦截器只是负责把请求头中的token令牌进行一个解析验证
        String header = request.getHeader("MyIntaAuthorizationNeedHeader");

        if (header != null && !"".equals(header)) {
            //查看头信息是否为约定的字符串
            if (header.startsWith("IntaSaidWeNeedTheStartAndOneEmpty ")) {
                //得到token
                String token = header.substring("IntaSaidWeNeedTheStartAndOneEmpty ".length());
                //对令牌进行验证
                try {
                    Claims claims =  jwtUtil.parseJWT(token);
                    String roles = (String) claims.get("roles");
                    if (roles != null) {
                        if (roles.equals("admin")) {
                            request.setAttribute("claims_admin", token);
                        }
                        if (roles.equals("user")) {
                            request.setAttribute("claims_user", token);
                        }
                    }
                } catch (Exception e) {
                    throw new RuntimeException("令牌指令不对！");
                }
            }
        }
        return true;
    }
}
