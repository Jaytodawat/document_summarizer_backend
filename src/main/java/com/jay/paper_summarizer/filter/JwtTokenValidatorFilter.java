package com.jay.paper_summarizer.filter;

import com.jay.paper_summarizer.config.AppConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class JwtTokenValidatorFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        System.out.println("JwtTokenValidatorFilter.doFilterInternal");
        System.out.println("request.getRequestURI(): " + request.getRequestURI());
        String jwtToken = request.getHeader("Authorization");
        System.out.println("jwtToken: " + jwtToken);
        if(jwtToken != null){
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

                System.out.println("userName: " + userName);
                System.out.println("authorities: " + authorities);
                if(userName != null){
                    Authentication authentication = new UsernamePasswordAuthenticationToken(
                            userName, null, AuthorityUtils.commaSeparatedStringToAuthorityList(authorities));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }

            } catch (Exception e) {
                System.out.println("Bad Credentials");
                throw new BadCredentialsException("Invalid Token", e);
            }
        } else {
            throw new BadCredentialsException("Invalid Token");
        }
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {

        for(String url : AppConstants.PUBLIC_URLS){
            System.out.println("url: " + url);
            System.out.println(request.getRequestURI().contains(url));
            if(request.getRequestURI().contains(url)){
                return true;
            }
        }

        return false;
    }
}
