package pl.mzakrze.kms.api;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Data;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import pl.mzakrze.kms.api.model.LoginAttempt_out;
import pl.mzakrze.kms.config.SecurityConstants;
import pl.mzakrze.kms.user.UserProfile;
import pl.mzakrze.kms.user.UserProfileRepository;

import javax.servlet.http.HttpServletResponse;
import java.sql.Date;

@RestController
@RequestMapping("/api/user")
public class UsersController {

    @Autowired private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired private UserProfileRepository userProfileRepository;


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
    public String register(@RequestBody RegisterAttempt_in req, HttpServletResponse response){

        String encodedPasswd = bCryptPasswordEncoder.encode(req.password);

        String tmpJwToken = RandomStringUtils.randomAlphabetic(20);

        UserProfile newlyRegisteredUser = new UserProfile();
        newlyRegisteredUser.setPassword(encodedPasswd);
        newlyRegisteredUser.setEmail(req.email);
        newlyRegisteredUser.setLoginToken(tmpJwToken);

        userProfileRepository.save(newlyRegisteredUser);

        String token = Jwts.builder()
                .setSubject(tmpJwToken)
                .setExpiration(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SecurityConstants.SECRET.getBytes())
                .compact();
        response.addHeader(SecurityConstants.AUTHENTICATION_HEADER, SecurityConstants.TOKEN_PREFIX + token);

        return "OK";
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
    static class RegisterAttempt_in {
        String email;
        String password;

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

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
