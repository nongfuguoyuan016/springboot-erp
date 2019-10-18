package com.wskj.manage.system.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.wskj.manage.common.JSONResult;

import javax.servlet.http.HttpServletRequest;

/**
 * @author: xuchang
 * @date: 2019/10/18
 */
@Slf4j
@RestController
public class LoginController {

    /**
     * 登录失败调用事件
     * @param request
     * @return
     */
    @PostMapping("/login")
    public JSONResult loginFail(HttpServletRequest request) {
        log.debug("login failed...");
        String msg = (String)request.getAttribute("msg");
        return JSONResult.fail(msg);
    }

    @GetMapping(value = "/user/info")
    public JSONResult userInfo() {
        JSONObject json = new JSONObject();
        json.put("name","admin");
        json.put("roles","user,admin");
        json.put("permission","all");
        return JSONResult.ok("成功",json);
    }

    @GetMapping("/error")
    public JSONResult error(String code) {
        JSONResult result = null;
        if("403".equals(code)) {
            result = JSONResult.ok("越权操作");
        } else result = JSONResult.fail("无法完成此操作");
        return result;
    }


}
