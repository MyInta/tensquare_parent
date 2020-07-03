package com.tensquare.manager.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import util.JwtUtil;

import javax.servlet.http.HttpServletRequest;

/**
 * @author inta
 * @date 2019/11/10
 * @describe
 */
@Component
public class ManagerFilter extends ZuulFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        System.out.println("经过后台过滤器了");
        RequestContext requestContext = RequestContext.getCurrentContext();
        //request域
        HttpServletRequest request = requestContext.getRequest();
        if (request.getMethod().equals("OPTIONS")) {
            return null;
        }

        if (request.getRequestURI().indexOf("login") > 0) {
            return null;
        }

        //得到头信息
        String header = request.getHeader("MyIntaAuthorizationNeedHeader");
        if (header != null && !"".equals(header)) {
            if (header.startsWith("IntaSaidWeNeedTheStartAndOneEmpty ")) {
                String token = header.substring("IntaSaidWeNeedTheStartAndOneEmpty ".length());
                try {
                    Claims claims = jwtUtil.parseJWT(token);
                    String roles = (String) claims.get("roles");
                    if (roles.equals("admin")) {
                        //把头信息转发下去，并且放行
                        requestContext.addZuulRequestHeader("MyIntaAuthorizationNeedHeader", header);
                        return null;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    requestContext.setSendZuulResponse(false);//终止运行
                }
            }
        }
        requestContext.setSendZuulResponse(false);//终止运行
        requestContext.setResponseStatusCode(403);
        requestContext.setResponseBody("权限不足");
        requestContext.getResponse().setContentType("text/html;charset=utf-8");
        return null;
    }
}
