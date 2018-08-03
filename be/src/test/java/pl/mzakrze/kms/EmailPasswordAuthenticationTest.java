package pl.mzakrze.kms;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import pl.mzakrze.kms.api.model.LoginAttempt_out;
import pl.mzakrze.kms.config.SecurityConstants;
import pl.mzakrze.kms.model.CurrentUser_out;
import pl.mzakrze.kms.model.LoginAttempt_in;
import pl.mzakrze.kms.user.UserProfile;
import pl.mzakrze.kms.user.UserProfileRepository;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableJpaRepositories(basePackages = "pl.mzakrze.kms")
@PropertySource("application.properties") // FIXME - inna baza danych na potrzeby testów
@EnableTransactionManagement
@Rollback
public class EmailPasswordAuthenticationTest {

    @Autowired private TestRestTemplate restTemplate;
    @Autowired private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired private UserProfileRepository userProfileRepository;

    @After
    public void cleanDb(){
        userProfileRepository.deleteAll();
    }

    @Test
    public void invalidCredentialsShouldNotWork() throws Exception {
        // given
        UserProfile userProfile = new UserProfile();
        userProfile.setEmail("test1@gmail.com");
        userProfile.setPassword("$2a$10$J1wxN5NkK9A35a6d9g9ac.IRrnmpBmCaEZB/mp5ftc7g8nVUJnEQK"); // encrypt('123456')
        userProfile = userProfileRepository.save(userProfile);

        // when
        LoginAttempt_in req = new LoginAttempt_in();
        req.setEmail("test1@gmail.com");
        req.setPassword("invalid");
        ResponseEntity<LoginAttempt_out> responseEntity = this.restTemplate.postForEntity("/api/user/login", req, LoginAttempt_out.class);

        // than
        assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());
    }

    @Test
    public void validCredentialsShouldWork() throws Exception {
        // given
        UserProfile userProfile = new UserProfile();
        userProfile.setEmail("test1@gmail.com");
        userProfile.setPassword("$2a$10$J1wxN5NkK9A35a6d9g9ac.IRrnmpBmCaEZB/mp5ftc7g8nVUJnEQK"); // encrypt('123456')
        userProfile = userProfileRepository.save(userProfile);

        // when
        LoginAttempt_in req = new LoginAttempt_in();
        req.setEmail("test1@gmail.com");
        req.setPassword("123456");
        ResponseEntity<LoginAttempt_out> responseEntity = this.restTemplate.postForEntity("/api/user/login", req, LoginAttempt_out.class);

        // than
        assertEquals("Invalid http response code", HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Invalid status of login with supposed to be VALID credentials",
                "SUCCESS", responseEntity.getBody().getStatus());
        List<String> authentication = responseEntity.getHeaders().get(SecurityConstants.AUTHENTICATION_HEADER);
        assert authentication != null : "Authentication header doesn't exist";
        assert authentication.size() == 1 : "Authentication header is empty";
        String jwToken = authentication.get(0);
        HttpHeaders headers = new HttpHeaders();
        headers.set(SecurityConstants.AUTHENTICATION_HEADER, jwToken);
        HttpEntity entity = new HttpEntity(headers);
        ResponseEntity<CurrentUser_out> currentUserResponse = restTemplate.exchange(
                "/api/user/current", HttpMethod.GET, entity, CurrentUser_out.class, "");
        assertEquals("Invalid http response code", HttpStatus.OK, currentUserResponse.getStatusCode());
        assertEquals("Invalid status of login with supposed to be VALID credentials",
                "test1@gmail.com", currentUserResponse.getBody().getEmail());
    }
}
