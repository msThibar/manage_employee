package com.example.demo.security.jwt;

import com.example.demo.security.userprincal.UserDetailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtTokenFilter extends OncePerRequestFilter {
    private static final Logger logger= LoggerFactory.getLogger(JwtTokenFilter.class);
    @Autowired
    JwtProvider jwtProvider;
    @Autowired
    UserDetailService userDetailService;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try{
            String token= getToken(request);
            if(token!=null&&jwtProvider.validateToken(token)){
                String username= jwtProvider.getUsernameFromToken(token);
                UserDetails userDetails= userDetailService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authenticationToken= new UsernamePasswordAuthenticationToken(userDetails.getUsername(),
                        userDetails.getPassword(), userDetails.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }catch (Exception ex){
            logger.error("error: "+ex.getClass());
        }
        filterChain.doFilter(request, response);
    }
    private String getToken(HttpServletRequest request){
        String authorization= request.getHeader("authorization");
        if(authorization!=null && authorization.startsWith("Bearer")){
            return authorization.substring(7);
        }
        return null;
    }
}
