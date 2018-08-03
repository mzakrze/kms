package pl.mzakrze.kms.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import pl.mzakrze.kms.user.UserProfile;
import pl.mzakrze.kms.user.UserProfileRepository;

import java.util.List;

@Configuration
public class MvcConfig extends WebMvcConfigurerAdapter {

    @Autowired private UserProfileRepository userProfileRepository;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        System.out.println("adding resolver");
        argumentResolvers.add(new UserAuthenticationStateArgumentResolver(userProfileRepository));
    }
}