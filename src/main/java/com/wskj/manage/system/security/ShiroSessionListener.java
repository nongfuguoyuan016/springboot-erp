package com.wskj.manage.system.security;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionListenerAdapter;

import java.time.LocalDateTime;

@Slf4j
public class ShiroSessionListener extends SessionListenerAdapter {

    @Override
    public void onStart(Session session) {
        super.onStart(session);
        log.debug("session[{}] start at {}",session.getId(), LocalDateTime.now());
    }

    @Override
    public void onExpiration(Session session) {
        super.onExpiration(session);
        log.debug("session[{}] expire at {}",session.getId(), LocalDateTime.now());
    }
}
