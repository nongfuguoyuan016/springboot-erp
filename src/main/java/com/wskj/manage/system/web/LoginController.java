package com.wskj.manage.system.web;

import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wskj.manage.common.utils.JSONResult;
import com.wskj.manage.common.utils.StringUtils;
import com.wskj.manage.system.entity.User;
import com.wskj.manage.system.utils.UserUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: xuchang
 * @date: 2019/10/18
 */
@Api(value = "认证Controller",tags = {"认证接口"})
@Slf4j
@RestController
@RequestMapping(value = "authc")
public class LoginController {

    /**
     * 登录失败调用事件
     * @param request
     * @return
     */
    @PostMapping("/login")
    public JSONResult loginFail(HttpServletRequest request) {
        String msg = (String)request.getAttribute("msg");
        log.debug("login failed, because {}",msg);
        return JSONResult.fail(msg);
    }

    @ApiOperation("获取登录用户认证信息")
    @ApiResponses(value = {@ApiResponse(code=0,message = "成功",response = JSONResult.class),@ApiResponse(code=1,message = "失败",response = JSONResult.class)})
    @GetMapping(value = "/info")
    public JSONResult userInfo() {
        JSONResult json = null;
        Subject subject = SecurityUtils.getSubject();
        if (subject !=  null && subject.isAuthenticated()) {
            User user = UserUtils.getUser();
            List<String> permissions = user.getRoleList().stream().map(u -> u.getEnname()).filter(s -> StringUtils.isNotEmpty(s)).collect(Collectors.toList());
            permissions.addAll(UserUtils.getMenuList().stream().map(m->m.getPermission()).filter(s -> StringUtils.isNotEmpty(s)).collect(Collectors.toList()));
            json = JSONResult.ok("成功",permissions);
        }
        return json != null ? json : JSONResult.fail("当前用户未认证");
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
