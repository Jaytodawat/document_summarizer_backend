package com.jay.paper_summarizer.filter;

import com.jay.paper_summarizer.config.AppConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
public class JwtTokenValidatorFilter extends GenericFilter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        boolean shouldNotFilter = shouldNotFilter((HttpServletRequest) request);

        if(!shouldNotFilter){
            String jwtToken = ((HttpServletRequest) request).getHeader("Authorization");
            log.info("jwtToken: {}", jwtToken);
            if(jwtToken == null){
                log.warn("Authorization Header is missing");
                throw new BadCredentialsException("Authorization Header is missing");
            }

            try{
                String secret = AppConstants.JWT_SECRET_DEFAULT;
                SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
                Claims claims = Jwts
                        .parser()
                        .verifyWith(secretKey)
                        .build()
                        .parseSignedClaims(jwtToken)
                        .getPayload();
                String userName = claims.get("username", String.class);
                String authorities = claims.get("authorities", String.class);

                log.info("userName: {}", userName);
                log.info("authorities: {}", authorities);
                if(userName != null){
                    Authentication authentication = new UsernamePasswordAuthenticationToken(
                            userName, null, AuthorityUtils.commaSeparatedStringToAuthorityList(authorities));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } else {
                    log.warn("Invalid Token");
                    throw new BadCredentialsException("Invalid Token");
                }

            } catch (Exception e) {
                log.warn("Invalid Token");
                throw new BadCredentialsException("Invalid Token");
            }
        }
        filterChain.doFilter(request, response);
    }


    protected boolean shouldNotFilter(HttpServletRequest request) {
        for(String url : AppConstants.PUBLIC_URLS){
            if(request.getRequestURI().contains(url)){
                return true;
            }
        }
        return false;
    }
}
