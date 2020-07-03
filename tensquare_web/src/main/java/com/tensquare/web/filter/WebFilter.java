package com.tensquare.web.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * @author inta
 * @date 2019/11/10
 * @describe
 */
@Component
public class WebFilter extends ZuulFilter {

    /**
     * 前置过滤器
     * @return
     */
    @Override
    public String filterType() {
        return "pre";
    }

    /**
     * 优先级 0最高
     * @return
     */
    @Override
    public int filterOrder() {
        return 0;
    }

    /**
     * 是否执行该过滤器
     * @return
     */
    @Override
    public boolean shouldFilter() {
        return true;
    }

    /**
     * 执行zuul过滤器
     * @return
     * @throws ZuulException
     */
    @Override
    public Object run() throws ZuulException {
        //得到request上下文
        RequestContext requestContext = RequestContext.getCurrentContext();
        //得到request域
        HttpServletRequest request = requestContext.getRequest();
        //得到头信息
        String header = request.getHeader("MyIntaAuthorizationNeedHeader");
        //判断是否有头信息
        if (header != null && !"".equals(header)) {
            System.out.println(header+ "--------------------------------");
            //把头信息继续向下传
            requestContext.addZuulRequestHeader("MyIntaAuthorizationNeedHeader", header);
        }
        return null;
    }
}
