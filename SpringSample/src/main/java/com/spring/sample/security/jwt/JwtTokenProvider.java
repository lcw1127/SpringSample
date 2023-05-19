package com.spring.sample.security.jwt;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.spring.sample.security.filter.JwtTokenAuthenticationFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
@PropertySource("classpath:config/jwt.properties")
public class JwtTokenProvider {
	private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);
	
    private static final String AUTHORITIES_KEY = "auth";
    
    private static final long EXPIRE_TIME = 1000L * 60 * 60 * 24; // 1일
    
	private final Key key;
	
	public JwtTokenProvider(@Value("${jwt.secret}") String secretKey) {
		logger.info("JwtTokenProvider Constructor");
		
		byte[] keyBytes = Decoders.BASE64.decode(secretKey);
		
		this.key = Keys.hmacShaKeyFor(keyBytes);
	}

	public JwtToken generateToken(Authentication authentication) {
		logger.info("generateToken - authentication {}", authentication);
		
		String authories = authentication.getAuthorities().stream()
				.map(GrantedAuthority::getAuthority)
				.collect(Collectors.joining(","));
		
		String accessToken = Jwts.builder()
					.setSubject("SUBJECT")
					.claim(AUTHORITIES_KEY, authories)
					.setIssuedAt(new Date())
					.setExpiration(new Date(System.currentTimeMillis() + EXPIRE_TIME))
					.signWith(key, SignatureAlgorithm.HS512)
					.compact();
		
		String refreshToken = Jwts.builder()
				.setExpiration(new Date(System.currentTimeMillis() + EXPIRE_TIME))
				.signWith(key, SignatureAlgorithm.HS512)
				.compact();
		
		JwtToken jwtToken = new JwtToken(JwtTokenAuthenticationFilter.TYPE_BEARER, "", accessToken, refreshToken);
		
		return jwtToken;
	}
	
	// JWT 토큰을 복호화하여 토큰에 들어있는 정보를 꺼내는 메서드
    public Authentication getAuthentication(String token) {
    	logger.info("getAuthentication - token {}", token);
    	
        // 토큰 복호화
        Claims claims = this.parseClaims(token);
        
        logger.info("claims {}", claims);
 
        if (claims.get("auth") == null) {
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }
 
        // 클레임에서 권한 정보 가져오기
        Collection<? extends GrantedAuthority> authorities = Arrays
        		.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
        		.filter(auth -> !auth.trim().isEmpty())
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
 
        // UserDetails 객체를 만들어서 Authentication 리턴
        UserDetails principal = new User(claims.getSubject(), "", authorities);
        
        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }
    
	// 토큰 정보를 검증하는 메서드
    public boolean validateToken(String token) {
    	logger.info("validateToken - token {}", token);
    	
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
        	logger.info("Invalid JWT Token", e);
        } catch (ExpiredJwtException e) {
        	logger.info("Expired JWT Token", e);
        } catch (UnsupportedJwtException e) {
        	logger.info("Unsupported JWT Token", e);
        } catch (IllegalArgumentException e) {
        	logger.info("JWT claims string is empty.", e);
        }
        
        return false;
    }
    
    private Claims parseClaims(String token) {
    	logger.info("parseClaims - token {}", token);
    	
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }
}
