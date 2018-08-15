package pl.mzakrze.kms.api;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Data;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import pl.mzakrze.kms.api.model.LoginAttempt_out;
import pl.mzakrze.kms.api.model.RegisterAttempt_in;
import pl.mzakrze.kms.config.SecurityConstants;
import pl.mzakrze.kms.user.JwtFacade;
import pl.mzakrze.kms.user.UserProfile;
import pl.mzakrze.kms.user.UserProfileFacade;
import pl.mzakrze.kms.user.UserProfileRepository;
import pl.mzakrze.kms.user.exceptions.EmailAlreadyExistsException;
import pl.mzakrze.kms.user.exceptions.IllegalPasswordException;

import javax.servlet.http.HttpServletResponse;
import java.sql.Date;

@RestController
@RequestMapping("/api/user")
public class UsersController {

    @Autowired private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired private UserProfileRepository userProfileRepository;

    @Autowired private UserProfileFacade userProfileFacade;
    @Autowired private JwtFacade jwtFacade;


    @GetMapping("/current")
    public CurrentUser_out current(UserProfile user) {
        CurrentUser_out res = new CurrentUser_out();
        if(user == null){
            res.isAnonymous = true;
        } else {
            res.isAnonymous = false;
            res.email = user.getEmail();
            res.name = "to be implemented";
        }
        return res;
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody RegisterAttempt_in req, HttpServletResponse response){
        try{
            UserProfile newUser = userProfileFacade.registerUser(req.email, req.password);

            String loginToken = newUser.getLoginToken();

            jwtFacade.setAuthenticationHeader(loginToken, response);

            return ResponseEntity.status(HttpStatus.OK).build();
        } catch(EmailAlreadyExistsException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (IllegalPasswordException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PostMapping("/login")
    public LoginAttempt_out login(UserProfile user, HttpServletResponse response){
        LoginAttempt_out res = new LoginAttempt_out();
        if(user == null){
            res.setStatus("FAILED");
        } else {
            res.setStatus("SUCCESS");
        }
        return res;
    }
// TODO - lombok nie działa w testach


    static class CurrentUser_out {
        Boolean isAnonymous;
        String email;
        String name;

        public Boolean getAnonymous() {
            return isAnonymous;
        }

        public void setAnonymous(Boolean anonymous) {
            isAnonymous = anonymous;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
