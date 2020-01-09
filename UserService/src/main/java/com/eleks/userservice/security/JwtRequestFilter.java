package com.eleks.userservice.security;


import com.eleks.userservice.security.model.JwtUserDataClaim;
import com.eleks.userservice.security.model.LoggedInUserPrincipal;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.eleks.userservice.security.SecurityConstants.AUTH_HEADER;
import static com.eleks.userservice.security.SecurityConstants.BEARER_TOKEN_PREFIX;

@Slf4j
public class JwtRequestFilter extends OncePerRequestFilter {

    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    public JwtRequestFilter(JwtTokenUtil jwtTokenUtil) {
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        String requestTokenHeader = request.getHeader(AUTH_HEADER);
        String jwtToken = getTokenFromHeader(requestTokenHeader);
        JwtUserDataClaim userClaim = getUserFromJwtToken(jwtToken);

        if (userClaim != null) {
            LoggedInUserPrincipal principal = new LoggedInUserPrincipal(userClaim.getUsername(), userClaim.getUserId(), jwtToken);
            Authentication authentication = new UsernamePasswordAuthenticationToken(principal, null, null);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        chain.doFilter(request, response);
    }

    private String getTokenFromHeader(String requestTokenHeader) {
        if (requestTokenHeader != null && requestTokenHeader.startsWith(BEARER_TOKEN_PREFIX)) {
            return requestTokenHeader.replace(BEARER_TOKEN_PREFIX, "");
        } else {
            log.warn("JWT Token is missing or does not begin with Bearer string");
            return null;
        }
    }

    private JwtUserDataClaim getUserFromJwtToken(String jwtToken) {
        try {
            return jwtTokenUtil.getUserFromToken(jwtToken);
        } catch (ExpiredJwtException exception) {
            log.warn("Request to parse expired JWT : {} failed : {}", jwtToken, exception.getMessage());
        } catch (UnsupportedJwtException exception) {
            log.warn("Request to parse unsupported JWT : {} failed : {}", jwtToken, exception.getMessage());
        } catch (MalformedJwtException exception) {
            log.warn("Request to parse invalid JWT : {} failed : {}", jwtToken, exception.getMessage());
        } catch (SignatureException exception) {
            log.warn("Request to parse JWT with invalid signature : {} failed : {}", jwtToken, exception.getMessage());
        } catch (IllegalArgumentException exception) {
            log.warn("Request to parse empty or null JWT : {} failed : {}", jwtToken, exception.getMessage());
        } catch (IOException exception) {
            log.warn("JWT parsing exception: {} failed : {}", jwtToken, exception.getMessage());
        }
        return null;
    }
}