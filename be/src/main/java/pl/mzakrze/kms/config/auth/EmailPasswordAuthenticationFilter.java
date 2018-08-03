package pl.mzakrze.kms.config.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import pl.mzakrze.kms.api.model.LoginAttempt_in;
import pl.mzakrze.kms.config.SecurityConstants;
import pl.mzakrze.kms.config.auth.tokens.JwtAuthenticationToken;
import pl.mzakrze.kms.user.UserProfile;
import pl.mzakrze.kms.user.UserProfileRepository;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.sql.Date;
import java.util.Arrays;

@Component
public class EmailPasswordAuthenticationFilter extends OncePerRequestFilter {

    private final UserProfileRepository userProfileRepository;
    private final AntPathRequestMatcher requestMatcher;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;


    public EmailPasswordAuthenticationFilter(UserProfileRepository userProfileRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userProfileRepository = userProfileRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.requestMatcher = new AntPathRequestMatcher("/api/user/login", "POST");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        System.out.println(request.getRequestURL());

        if(requestMatcher.matches(request) == false){
            chain.doFilter(request, response);
            return;
        }

        try {
            LoginAttempt_in credentials = new ObjectMapper()
                    .readValue(request.getInputStream(), LoginAttempt_in.class);

            UserProfile userProfile = userProfileRepository.getByEmail(credentials.getEmail());

            if(userProfile == null) {
                response.setStatus(HttpStatus.FORBIDDEN.value());
                chain.doFilter(request, response);
            }

            if(bCryptPasswordEncoder.matches(credentials.getPassword(), userProfile.getPassword()) == false) {
                response.setStatus(HttpStatus.FORBIDDEN.value());
                chain.doFilter(request, response);
            }

            String tmpJwtToken = RandomStringUtils.randomAlphabetic(20);
            userProfile.setLoginToken(tmpJwtToken);
            userProfileRepository.save(userProfile);

            JwtAuthenticationToken authToken = new JwtAuthenticationToken(tmpJwtToken, Arrays.asList(UserRoles.USER_AUTHORITY));

            SecurityContextHolder.getContext().setAuthentication(authToken);

            String token = Jwts.builder()
                    .setSubject(tmpJwtToken)
                    .setExpiration(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
                    .signWith(SignatureAlgorithm.HS512, SecurityConstants.SECRET.getBytes())
                    .compact();
            response.addHeader(SecurityConstants.AUTHENTICATION_HEADER, SecurityConstants.TOKEN_PREFIX + token);

        } catch (IOException e) {
            //throw new RuntimeException(e);
            // FIXME - wchodzi dwukrotnie do filtra, przez co dwukrotnie czyta filtr, więc leci IOException Stream closed
        }

        chain.doFilter(request, response);
    }


}
