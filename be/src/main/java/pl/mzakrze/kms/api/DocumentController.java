package pl.mzakrze.kms.api;

import org.springframework.web.bind.annotation.*;
import pl.mzakrze.kms.api.model.DocContent_in;
import pl.mzakrze.kms.api.model.DocumentMeta_in;
import pl.mzakrze.kms.user.UserProfile;

@RestController
@RequestMapping("/api/doc")
public class DocumentController {

    @PutMapping("/content/{gid}")
    public String content(UserProfile userProfile, @PathVariable String gid, @RequestBody DocContent_in req){
        throw new UnsupportedOperationException("plz implement me");
    }

    @PostMapping("/meta/{gid}")
    public String meta(UserProfile userProfile, @PathVariable String gid, @RequestBody DocumentMeta_in req) {
        throw new UnsupportedOperationException("plz implement me");
    }

    @PutMapping("/static/{gid}")
    public String putStatic(UserProfile userProfile, @PathVariable String gid){
        throw new UnsupportedOperationException("plz implement me");
    }

    @GetMapping("/static/{gid}")
    public String getStatic(UserProfile userProfile, @PathVariable String gid){
        throw new UnsupportedOperationException("plz implement me");
    }
}
