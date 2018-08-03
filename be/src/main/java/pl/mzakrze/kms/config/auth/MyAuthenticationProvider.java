package pl.mzakrze.kms.config.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import pl.mzakrze.kms.config.auth.tokens.EmailPasswordAuthenticationToken;
import pl.mzakrze.kms.config.auth.tokens.JwtAuthenticationToken;
import pl.mzakrze.kms.user.UserProfile;
import pl.mzakrze.kms.user.UserProfileRepository;

@Component
public class MyAuthenticationProvider implements AuthenticationProvider {

    @Autowired private UserProfileRepository userRepository;
    @Autowired private BCryptPasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        System.out.println("MyAuthenticationProvider, authenticate");
        if(authentication instanceof JwtAuthenticationToken){
            JwtAuthenticationToken auth = (JwtAuthenticationToken) authentication;
            UserProfile userProfile = userRepository.getByLoginToken(auth.getTmpJwToken());
            if(userProfile == null){
                authentication.setAuthenticated(false);
            } else {
                authentication.setAuthenticated(true);
            }
        } else if(authentication instanceof EmailPasswordAuthenticationToken){
            EmailPasswordAuthenticationToken auth = (EmailPasswordAuthenticationToken) authentication;
            UserProfile userProfile = userRepository.getByEmail(auth.getEmail());
            if(passwordEncoder.matches(auth.getPassword(), userProfile.getPassword())){
                authentication.setAuthenticated(true);
            } else {
                authentication.setAuthenticated(false);
            }
        } else {
            throw new IllegalStateException("Invalid Authentication object type");
        }
        return authentication;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(EmailPasswordAuthenticationToken.class) ||
                authentication.equals(JwtAuthenticationToken.class);
    }
}
