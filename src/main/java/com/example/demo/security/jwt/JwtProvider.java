package com.example.demo.security.jwt;

import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import io.jsonwebtoken.Jwts;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtProvider {
    private static final Logger logger= LoggerFactory.getLogger(JwtProvider.class);
    private final String JWT_SECRET = "secret key";
    private final int JWT_EXPIRATION= 86400; //time live of token

    public String createToken(UserDetails userDetails){
        return Jwts.builder().setSubject(userDetails.getUsername()).setIssuedAt(new Date()).
                setExpiration(new Date(new Date().getTime()+JWT_EXPIRATION*1000)).
                signWith(SignatureAlgorithm.HS512, JWT_SECRET).compact();
    }

    public boolean validateToken(String token){
        try{
            Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(token);
            return true;
        }catch(Exception e){
            logger.error("error: "+e.getClass());
        }
        return false;
    }
    public String getUsernameFromToken(String token){
        return Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(token).getBody().getSubject();
    }
}
