package com.wskj.manage.system.security;

import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import java.util.HashSet;
import java.util.Set;

/**
 * @author: xuchang
 * @date: 2019/10/18
 */
public class SystemRealm extends AuthorizingRealm {

    private static final String DEFAULT_USERNAME = "admin";

    private static final String DEFAULT_PASSWORD = "admin";

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken)authenticationToken;
        if (DEFAULT_USERNAME.equals(token.getUsername()) && DEFAULT_PASSWORD.equals(new String(token.getPassword()))) {
            return new SimpleAuthenticationInfo("username",DEFAULT_PASSWORD,getName());
        }
        return null;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        Set<String> roles = new HashSet<>(1);
        roles.add("admin");
        info.setRoles(roles);
        Set<String> permissions = new HashSet<>(1);
        permissions.add("all");
        info.setStringPermissions(permissions);
        return info;
    }

}
