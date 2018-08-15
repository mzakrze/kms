package pl.mzakrze.kms.user;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;
import pl.mzakrze.kms.config.SecurityConstants;

import javax.servlet.http.HttpServletResponse;
import java.sql.Date;

@Service
public class JwtFacade {
    public void setAuthenticationHeader(String loginToken, HttpServletResponse response) {
        String token = Jwts.builder()
                .setSubject(loginToken)
                .setExpiration(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SecurityConstants.SECRET.getBytes())
                .compact();
        response.addHeader(SecurityConstants.AUTHENTICATION_HEADER, SecurityConstants.TOKEN_PREFIX + token);
    }
}
