package com.wskj.manage.system.security;

import com.sun.corba.se.impl.oa.toa.TOA;
import com.wskj.manage.common.utils.JSONResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;

/**
 * 继承shiro 的form表单过滤器
 * @author: xuchang
 * @date: 2019/10/18
 */
@Slf4j
public class CustomFormAuthenticationFilter extends FormAuthenticationFilter {

    private String tokenName;

    public void setTokenName(String tokenName) {
        this.tokenName = tokenName;
    }

    @Override
    protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) {
        // default username
        String username = getUsername(request);
        // default password
        String password = getPassword(request);
        if (password==null){
            password = "";
        }
        return new UsernamePasswordToken(username,password);
    }

    @Override
    protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request, ServletResponse response) throws Exception {
        HttpServletResponse res = (HttpServletResponse) response;
        res.setStatus(HttpServletResponse.SC_OK);
        res.setCharacterEncoding("UTF-8");
        PrintWriter writer = null;
        try {
            writer = res.getWriter();
            writer.write(JSONResult.ok("登录成功",getToken(res)).toString());
            writer.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    private String getToken(HttpServletResponse res) {
        String cookie = res.getHeader("set-cookie");
        if (StringUtils.isNotEmpty(cookie)) {
            tokenName = StringUtils.isNotEmpty(tokenName) ? tokenName :"token";
            String str = tokenName + "=";
            int index = cookie.indexOf(str);
            cookie = cookie.substring(index + str.length());
            cookie = cookie.split(";")[0];
        }
        return cookie;
    }

    /**
     * 登录失败调用事件
     * @param token
     * @param e
     * @param request
     * @param response
     * @return
     */
    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse response) {
        String className = e.getClass().getName();
        String message = null;
        if (IncorrectCredentialsException.class.getName().equals(className)
                || UnknownAccountException.class.getName().equals(className)){
            message = "用户名或密码错误";
        } else if (e.getMessage() != null && StringUtils.startsWith(e.getMessage(), "msg:")){
            message = StringUtils.replace(e.getMessage(), "msg:", "");
        } else{
            message = "系统出现点问题，请稍后再试！";
            // e.printStackTrace(); // 输出到控制台
            log.warn("登录失败",e);
        }
        request.setAttribute("msg", message);
        return true;
    }
}
