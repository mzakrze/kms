package pl.mzakrze.kms.api;

import org.springframework.web.bind.annotation.*;
import pl.mzakrze.kms.api.model.*;
import pl.mzakrze.kms.user.UserProfile;

@RestController
@RequestMapping("/api/account")
public class AccountController {

    @PutMapping("/git/credentials")
    public String putGitCredentials(UserProfile userProfile, @RequestBody GitCredentials_in req){
        throw new UnsupportedOperationException("plz implement me");
    }

    @PostMapping("/git/commit")
    public String gitCommit(UserProfile userProfile, @RequestBody GitCommit_in req){
        throw new UnsupportedOperationException("plz implement me");
    }

    @PutMapping("/account")
    public String account(UserProfile userProfile, @RequestBody ChangeAccountSettings_in req){
        throw new UnsupportedOperationException("plz implement me");
    }

    @PutMapping("/space")
    public String changeSpace(UserProfile userProfile, @RequestBody ChangeSpace_in req){
        throw new UnsupportedOperationException("plz implement me");
    }

    @PostMapping("/space")
    public String createSpace(UserProfile userProfile, @RequestBody CreateSpace_in req){
        throw new UnsupportedOperationException("plz implement me");
    }

}
