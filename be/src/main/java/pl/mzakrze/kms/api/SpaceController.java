package pl.mzakrze.kms.api;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.mzakrze.kms.api.model.CheckStatus_in;
import pl.mzakrze.kms.user.UserProfile;
import pl.mzakrze.kms.user_drive.UserDriveFacade;

@RestController
@RequestMapping("/api/space")
public class SpaceController {

    @Autowired private UserDriveFacade userDriveFacade;

    @PostMapping("/status")
    public ResponseEntity status(UserProfile userProfile, @RequestBody CheckStatus_in req){
        if(req.password == null || req.password.isEmpty() || req.spaceName == null || req.spaceName.isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        Boolean hasAccess = userDriveFacade.checkSpaceAccess(userProfile, req.spaceName, req.password);
        if(hasAccess){
            return ResponseEntity.ok(req.spaceName);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
}
