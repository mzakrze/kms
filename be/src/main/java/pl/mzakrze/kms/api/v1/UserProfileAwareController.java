package pl.mzakrze.kms.api.v1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import pl.mzakrze.kms.user.UserProfile;
import pl.mzakrze.kms.user.UserProfileRepository;

public class UserProfileAwareController {
    @Autowired
    private UserProfileRepository userProfileRepository;

    protected UserProfile getUserAuthenticationState(){
        String userEmail = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(userEmail == null){
            throw new IllegalStateException("User cannot be null here. Security Exception");
        }
        return userProfileRepository.findByLogin(userEmail);
    }
}