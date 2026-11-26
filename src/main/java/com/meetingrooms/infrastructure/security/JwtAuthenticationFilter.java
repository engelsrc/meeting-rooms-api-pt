package com.meetingrooms.infrastructure.security;

import com.meetingrooms.shared.ErrorMessages;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final JsonAuthErrorWriter errorWriter;

    public JwtAuthenticationFilter(JwtService jwtService, JsonAuthErrorWriter errorWriter) {
        this.jwtService = jwtService;
        this.errorWriter = errorWriter;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        if (HttpMethod.GET.matches(request.getMethod())) {
            return true;
        }
        String path = request.getRequestURI();
        return HttpMethod.POST.matches(request.getMethod()) && "/api/auth/login".equals(path);
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (header == null || header.isBlank()) {
            errorWriter.write(response, HttpStatus.FORBIDDEN.value(), ErrorMessages.AUTH_UNAUTHENTICATED);
            return;
        }
        if (!header.regionMatches(true, 0, "Bearer ", 0, 7)) {
            errorWriter.write(response, HttpStatus.FORBIDDEN.value(), ErrorMessages.AUTH_UNAUTHENTICATED);
            return;
        }
        String token = header.substring(7).trim();
        if (token.isEmpty()) {
            errorWriter.write(response, HttpStatus.FORBIDDEN.value(), ErrorMessages.AUTH_UNAUTHENTICATED);
            return;
        }
        if (!jwtService.isValid(token)) {
            errorWriter.write(response, HttpStatus.UNAUTHORIZED.value(), ErrorMessages.AUTH_INVALID_TOKEN);
            return;
        }
        String username = jwtService.extractUsername(token);
        var authentication = new UsernamePasswordAuthenticationToken(
                username, null, AuthorityUtils.NO_AUTHORITIES);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }
}
