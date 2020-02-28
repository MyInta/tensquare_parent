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
        final String header = request.getHeader("MyIntaAuthorizationNeedHeader");

        if (header != null && !"".equals(header) && header.startsWith("IntaSaidWeNeedTheStartAndOneEmpty ") ) {
            //得到token
            final String token = header.substring("IntaSaidWeNeedTheStartAndOneEmpty ".length());
            //对令牌进行验证
            Claims claims =  jwtUtil.parseJWT(token);
            if (claims != null) {
                if (claims.get("roles").equals("admin")) {//如果是管理员
                    request.setAttribute("claims_admin", token);
                }
                if (claims.get("roles").equals("user")) {//如果是用户
                    request.setAttribute("claims_user", token);
                }
            }
        }
        return true;
    }
}
