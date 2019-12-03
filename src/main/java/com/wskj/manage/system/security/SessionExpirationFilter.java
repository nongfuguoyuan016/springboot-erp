package com.wskj.manage.system.security;

import com.wskj.manage.common.enums.ResultCodeEnum;
import com.wskj.manage.common.utils.JSONResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.web.filter.authc.UserFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * session过期拦截器
 * 当session过期时,直接返回响应,而不是跳转到loginUrl
 */
@Slf4j
public class SessionExpirationFilter extends UserFilter {

    public SessionExpirationFilter() {
        super();
    }


    /**
     * isAccessAllowed 放回false即session失效时调用
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletResponse res = (HttpServletResponse) response;
        res.setHeader("Access-Control-Allow-Origin","*");
        res.setStatus(HttpServletResponse.SC_OK);
        res.setCharacterEncoding("UTF-8");
        res.setContentType("application/json; charset=utf8");
        PrintWriter writer = res.getWriter();
        writer.write(new JSONResult(ResultCodeEnum.NOT_LOGIN.getCode(),"token已过期,请重新获取",null).toString());
        writer.close();
        return false;
    }
}