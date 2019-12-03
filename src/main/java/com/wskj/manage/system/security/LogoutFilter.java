package com.wskj.manage.system.security;

import com.wskj.manage.common.utils.JSONResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.session.SessionException;
import org.apache.shiro.subject.Subject;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * 登出过滤器, 继承shiro内置的logoutFilter
 */
@Slf4j
public class LogoutFilter extends org.apache.shiro.web.filter.authc.LogoutFilter {

    /**
     * 取消重定向,直接返回客户端json
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        Subject subject = getSubject(request, response);
        // String redirectUrl = getRedirectUrl(request, response, subject);
        //try/catch added for SHIRO-298:
        try {
            subject.logout();
            HttpServletResponse res = (HttpServletResponse) response;
            res.setHeader("Access-Control-Allow-Origin","*");
            res.setStatus(HttpServletResponse.SC_OK);
            res.setCharacterEncoding("UTF-8");
            res.setContentType("application/json; charset=utf8");
            PrintWriter writer = res.getWriter();
            writer.write(JSONResult.ok("成功登出").toString());
            writer.close();
        } catch (SessionException ise) {
            log.debug("Encountered session exception during logout.  This can generally safely be ignored.", ise);
        }
        // issueRedirect(request, response, redirectUrl);
        return false;
    }
}
