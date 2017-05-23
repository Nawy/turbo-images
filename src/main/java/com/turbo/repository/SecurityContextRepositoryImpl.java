package com.turbo.repository;

import com.turbo.model.Session;
import com.turbo.service.AuthorisationService;
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

    private static final String SESSION_COOKIE_NAME = "x-session-id";

    private final AuthorisationService authorisationService;

    @Autowired
    public SecurityContextRepositoryImpl(AuthorisationService authorisationService) {
        this.authorisationService = authorisationService;
    }

    @Override
    public SecurityContext loadContext(HttpRequestResponseHolder requestResponseHolder) {
        String sessionId = getSessionIdFromRequest(
                requestResponseHolder.getRequest()
        );
        SecurityContext securityContext = new SecurityContextImpl();

        if (sessionId != null) {
            Session session = authorisationService.getSession(sessionId);
            if (session != null) {
                securityContext.setAuthentication(
                        new UsernamePasswordAuthenticationToken(
                                session,
                                null,
                                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
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

    private String getSessionIdFromRequest(HttpServletRequest req) {
        if (req.getCookies() == null) return null;
        Cookie cookie = Arrays.stream(req.getCookies())
                .filter((c) -> SESSION_COOKIE_NAME.equals(c.getName()))
                .findFirst().orElse(null);
        return cookie == null ? null : cookie.getValue();
    }
}
