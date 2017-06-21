package com.turbo.repository;

import com.turbo.model.SecurityHeader;
import com.turbo.model.SecurityRole;
import com.turbo.model.Session;
import com.turbo.service.AuthorizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.context.HttpRequestResponseHolder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Collections;

@Component
public class SecurityContextRepositoryImpl implements SecurityContextRepository {

    private final AuthorizationService authorizationService;

    @Autowired
    public SecurityContextRepositoryImpl(AuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
    }

    @Override
    public SecurityContext loadContext(HttpRequestResponseHolder requestResponseHolder) {
        Long sessionId = getSessionIdFromRequest(
                requestResponseHolder.getRequest()
        );
        SecurityContext securityContext = new SecurityContextImpl();

        if (sessionId != null) {
            Session session = authorizationService.getSession(sessionId);
            if (session != null) {
                securityContext.setAuthentication(
                        new UsernamePasswordAuthenticationToken(
                                session,
                                null,
                                Collections.singletonList(new SimpleGrantedAuthority(SecurityRole.USER))
                        )
                );
            }
        }

        return securityContext;
    }

    @Override
    public void saveContext(SecurityContext context, HttpServletRequest request, HttpServletResponse response) {
    }

    @Override
    public boolean containsContext(HttpServletRequest request) {
        return false;
    }

    private Long getSessionIdFromRequest(HttpServletRequest req) {
        if (req.getCookies() == null) return null;
        Cookie cookie = Arrays.stream(req.getCookies())
                .filter((c) -> SecurityHeader.SESSION_COOKIE_NAME.equals(c.getName()))
                .findFirst().orElse(null);
        return cookie == null ? null : Long.valueOf(cookie.getValue());
    }
}
