package pl.mzakrze.kms.api;

import com.sun.tools.internal.xjc.reader.xmlschema.bindinfo.BIConversion;
import org.springframework.web.bind.annotation.*;
import pl.mzakrze.kms.api.model.CreateNode_in;
import pl.mzakrze.kms.api.model.CreateNode_out;
import pl.mzakrze.kms.api.model.DriveTree_out;
import pl.mzakrze.kms.api.model.SearchDrive_in;
import pl.mzakrze.kms.user.UserProfile;

import java.util.List;

@RestController
@RequestMapping("/api/drive")
public class DriveController {

    @GetMapping("/tree")
    public DriveTree_out tree(UserProfile user){
        throw new UnsupportedOperationException("plz implement me");
    }

    @PutMapping("/move/{objectGid}/{destinationGid}")
    public String move(UserProfile userProfile, @PathVariable("objectGid") String objectGid, @PathVariable("destinationGid") String destinationGid){
        throw new UnsupportedOperationException("plz implement me");
    }

    @PutMapping("/move/{objectGid}/{newName}")
    public String rename(UserProfile userProfile, @PathVariable("objectGid") String objectGid, @PathVariable("newName") String newName){
        throw new UnsupportedOperationException("plz implement me");
    }

    @PostMapping("/create/{type}")
    public CreateNode_out createFolder(UserProfile userProfile, @RequestBody CreateNode_in req){
        throw new UnsupportedOperationException("plz implement me");
    }

    @GetMapping("/tags")
    public List<String> tags(UserProfile userProfile){
        throw new UnsupportedOperationException("plz implement me");
    }

    @GetMapping("/search")
    public DriveTree_out search(UserProfile userProfile, @RequestBody SearchDrive_in req){
        throw new UnsupportedOperationException("plz implement me");
    }



}
