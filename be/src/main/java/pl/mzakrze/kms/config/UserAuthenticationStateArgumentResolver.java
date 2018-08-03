package pl.mzakrze.kms.config;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import pl.mzakrze.kms.config.auth.tokens.EmailPasswordAuthenticationToken;
import pl.mzakrze.kms.config.auth.tokens.JwtAuthenticationToken;
import pl.mzakrze.kms.user.UserProfile;
import pl.mzakrze.kms.user.UserProfileRepository;

import java.security.Principal;

public class UserAuthenticationStateArgumentResolver implements HandlerMethodArgumentResolver {

    private final UserProfileRepository userRepository;

    public UserAuthenticationStateArgumentResolver(UserProfileRepository userProfileRepository) {
        this.userRepository = userProfileRepository;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(UserProfile.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        Principal principal = webRequest.getUserPrincipal();

        if(principal instanceof JwtAuthenticationToken){
            String jwToken = ((JwtAuthenticationToken) principal).getTmpJwToken();
            return userRepository.getByLoginToken(jwToken); // FIXME - security exception jeśli null
        } else if(principal instanceof EmailPasswordAuthenticationToken){
            String userEmail = ((EmailPasswordAuthenticationToken) principal).getEmail();
            return userRepository.getByEmail(userEmail); // FIXME - security exception jeśli null
        } else {
            return null;
        }
    }
}
