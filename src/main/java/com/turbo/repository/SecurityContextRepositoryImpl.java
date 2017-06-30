package com.turbo.repository;

import com.turbo.model.DeviceType;
import com.turbo.model.SecurityHeader;
import com.turbo.model.SecurityRole;
import com.turbo.model.Session;
import com.turbo.repository.util.EncryptionService;
import com.turbo.repository.util.Headers;
import com.turbo.service.SessionService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private final SessionService sessionService;
    private Logger LOG = LoggerFactory.getLogger(SecurityContextRepositoryImpl.class);

    @Autowired
    public SecurityContextRepositoryImpl(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @Override
    public SecurityContext loadContext(HttpRequestResponseHolder requestResponseHolder) {
        HttpServletRequest request = requestResponseHolder.getRequest();
        Long sessionId = getSessionIdFromRequest(request);
        SecurityContext securityContext = new SecurityContextImpl();
        if (sessionId != null) {
            Session session = sessionService.get(sessionId);
            if (session != null) {
                String ipValue = request.getRemoteAddr();
                String deviceTypeValue = request.getHeader(Headers.DEVICE_TYPE);
                Session refreshedSession = new Session(
                        session.getUserId(),
                        DeviceType.getDeviceType(deviceTypeValue),
                        StringUtils.isBlank(ipValue) ? null : ipValue
                );
                sessionService.save(refreshedSession);

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
        if (cookie == null) return null;
        String cookieValue = cookie.getValue();
        if (cookieValue == null) return null;
        try {
            return EncryptionService.decodeHashId(cookieValue);
        } catch (Exception e) {
            LOG.warn("Can't decode cookie sessionId:{}", cookieValue);
            return null;
        }
    }

}
