package com.wskj.manage.system.security;

import com.wskj.manage.common.enums.ResultCodeEnum;
import com.wskj.manage.common.utils.CookieUtils;
import com.wskj.manage.common.utils.JSONResult;
import com.wskj.manage.common.utils.StringUtils;
import org.apache.shiro.web.filter.AccessControlFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * token拦截器,拦截请求中是否存在token, 不存在则视为非法请求
 */
public class TokenFilter extends AccessControlFilter {

    public TokenFilter() {
        super();
    }

    private String tokenName;

    public void setTokenName(String tokenName) {
        this.tokenName = tokenName;
    }

    /**
     * 判断是否允许访问
     * @param request
     * @param response
     * @param mappedValue
     * @return
     * @throws Exception
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        // 拦截非登录请求
        if (!isLoginRequest(request,response)) {
            HttpServletRequest req = (HttpServletRequest) request;
            HttpServletResponse resp = (HttpServletResponse) response;
            // 先从请求头中获取
            String token = req.getHeader(tokenName);
            // 不存在则从cookie中获取
            token = StringUtils.isEmpty(token) ? CookieUtils.getCookie(req,resp,tokenName) : token;
            // 不存在则从请求参数中获取
            token = StringUtils.isEmpty(token) ? request.getParameter(tokenName) : token;
            return StringUtils.isNotEmpty(token);
        }
        return true;
    }

    /**
     * isAccessAllowed 返回false即没有token时调用
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
        PrintWriter writer = res.getWriter();
        writer.write(new JSONResult(ResultCodeEnum.INVALID_REQUEST.getCode(),ResultCodeEnum.INVALID_REQUEST.getMsg(),null).toString());
        writer.close();
        return false;
    }
}
