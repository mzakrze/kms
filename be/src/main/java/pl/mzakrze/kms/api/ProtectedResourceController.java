package pl.mzakrze.kms.api;

import org.springframework.web.bind.annotation.*;
import pl.mzakrze.kms.user.ProtectedResouceRepository;
import pl.mzakrze.kms.user.ProtectedResource;
import pl.mzakrze.kms.user.UserProfile;

@RestController
@RequestMapping("/api/priv")
public class ProtectedResourceController {

    @GetMapping
    public String get(UserProfile user){
        ProtectedResource protectedResource = ProtectedResouceRepository.getInstance().byUserGid(user.getGid());
        return protectedResource == null ? "You have no secrets" : protectedResource.secret;
    }

    @PostMapping("/{newSecret}")
    public String put(UserProfile user, @PathVariable String newSecret){
        ProtectedResouceRepository.getInstance().put(user.getGid(), newSecret);
        return "OK";
    }
}
