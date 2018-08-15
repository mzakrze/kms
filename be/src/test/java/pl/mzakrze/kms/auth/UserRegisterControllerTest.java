package pl.mzakrze.kms.auth;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import pl.mzakrze.kms.config.SecurityConstants;
import pl.mzakrze.kms.model.CurrentUser_out;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Rollback
@Transactional
public class UserRegisterControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void registerShouldReturnJwt() throws Exception {

        ResponseEntity<CurrentUser_out> currentUserResponseBeforeRegister = restTemplate.exchange(
                "/api/user/current", HttpMethod.GET, new HttpEntity<>(new HttpHeaders()), CurrentUser_out.class, "");
        assert currentUserResponseBeforeRegister.getBody().isAnonymous == true : "User should NOT be authenticated";

        RegisterAttempt_in req = new RegisterAttempt_in();
        req.email = "test@gmail.com";
        req.password = "123456";
        ResponseEntity<String> responseEntity = this.restTemplate.postForEntity("/api/user/register", req, String.class);

        List<String> authentication = responseEntity.getHeaders().get(SecurityConstants.AUTHENTICATION_HEADER);
        assert authentication != null : "Authentication header doesn't exist";
        assert authentication.size() == 1 : "Authentication header is empty";
        String jwToken = authentication.get(0);

        HttpHeaders headers = new HttpHeaders();
        headers.set(SecurityConstants.AUTHENTICATION_HEADER, jwToken);
        HttpEntity entity = new HttpEntity(headers);
        ResponseEntity<CurrentUser_out> currentUserResponseAfterRegister = restTemplate.exchange(
                "/api/user/current", HttpMethod.GET, entity, CurrentUser_out.class, "");
        assert currentUserResponseAfterRegister.getBody().isAnonymous == false : "Newly registered user is anonymous";
        assert "test@gmail.com".equals(currentUserResponseAfterRegister.getBody().email) : "Invalid email returned by API";
    }


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


}