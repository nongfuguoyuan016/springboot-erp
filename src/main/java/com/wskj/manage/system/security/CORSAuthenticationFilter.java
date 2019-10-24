package com.wskj.manage.system.security;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.web.filter.AccessControlFilter;

/**
 *  自定义shiro过滤器，对 OPTIONS 请求进行过滤。
 *  前后端分离项目中，由于跨域，会导致复杂请求，即会发送preflighted request，
 *  这样会导致在GET／POST等请求之前会先发一个OPTIONS请求，但OPTIONS请求并不带shiro
 *  的SessionId字段，即OPTIONS请求不能通过shiro验证，会返回未认证的信息。
 *
 * @author: xuchang
 * @date: 2019/10/18
 */
public class CORSAuthenticationFilter extends AccessControlFilter {

    private static final String REQ_TYPE = "OPTIONS";

    public CORSAuthenticationFilter() {
        super();
    }

    /**
     * 判断是否允许访问
     * @param request
     * @param response
     * @param mappedValue
     * @return true: 允许; false: 不允许
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue){
        return REQ_TYPE.equals(((HttpServletRequest)request).getMethod().toUpperCase());
    }

    /**
     * 判断访问被拒绝时是否进行处理
     * @param request
     * @param response
     * @return true: 不处理; false: 进行处理
     * @throws Exception
     */
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
//        HttpServletResponse res = (HttpServletResponse) response;
//        res.setHeader("Access-Control-Allow-Origin","*");
//        res.setStatus(HttpServletResponse.SC_OK);
//        res.setCharacterEncoding("UTF-8");
//        PrintWriter writer = res.getWriter();
//        writer.write(JSONResult.fail("未登录").toString());
//        writer.close();
//        return false;
        return true;
    }
}
