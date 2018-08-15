package pl.mzakrze.kms.auth;

import org.junit.Before;
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
import pl.mzakrze.kms.config.SecurityConstants;
import pl.mzakrze.kms.model.CurrentUser_out;
import pl.mzakrze.kms.user.UserProfile;
import pl.mzakrze.kms.user.UserProfileRepository;
import static org.junit.Assert.*;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableJpaRepositories(basePackages = "pl.mzakrze.kms")
@PropertySource("application.properties")
@EnableTransactionManagement
@Rollback
public class JwTokenAuthenticationTest {

    @Autowired private TestRestTemplate restTemplate;
    @Autowired private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired private UserProfileRepository userProfileRepository;

    @Test
    public void invalidCredentialsShouldNotWork() throws Exception {
        // given
        UserProfile userProfile = new UserProfile();
        userProfile.setEmail("email1");
        userProfile.setPassword("password1");
        userProfile.setLoginToken("valid_login_token");
        userProfile = userProfileRepository.save(userProfile);

        // when
        HttpHeaders headers = new HttpHeaders();
        headers.set(SecurityConstants.AUTHENTICATION_HEADER, "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJEakF0anNIQUdDSmRMbGNDT0RKcCIsImV4cCI6MTUzNDU0MDI0Mn0.aZODjaGidF9bcpokVcF1-sRn6xLCbrwOmCe55XucyBK586L2SBfj_23SriDW28yBusUnSTx_P0dfPWKaRvAGew");
        HttpEntity entity = new HttpEntity(headers);
        ResponseEntity<CurrentUser_out> currentUserResponse = restTemplate.exchange(
                "/api/user/current", HttpMethod.GET, entity, CurrentUser_out.class, "");

        // then
        assert currentUserResponse.getBody().isAnonymous;

        // cleanup
        userProfileRepository.delete(userProfile);
    }

    @Test
    public void validCredentialsShouldWork() throws Exception {
        // given
        UserProfile userProfile = new UserProfile();
        userProfile.setEmail("email1");
        userProfile.setPassword("password1");
        userProfile.setLoginToken("DjAtjsHAGCJdLlcCODJp");
        userProfile = userProfileRepository.save(userProfile);

        // when
        HttpHeaders headers = new HttpHeaders();
        headers.set(SecurityConstants.AUTHENTICATION_HEADER, "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJEakF0anNIQUdDSmRMbGNDT0RKcCIsImV4cCI6MTUzNDU0MDI0Mn0.XZODjaGidF9bcpokVcF1-sRn6xLCbrwOmCe55XucyBK586L2SBfj_23SriDW28yBusUnSTx_P0dfPWKaRvAGew"); // TODO encode
        HttpEntity entity = new HttpEntity(headers);
        ResponseEntity<CurrentUser_out> currentUserResponse = restTemplate.exchange(
                "/api/user/current", HttpMethod.GET, entity, CurrentUser_out.class, "");

        // than
        assertFalse(currentUserResponse.getBody().isAnonymous);

        // cleanup
        userProfileRepository.delete(userProfile);
    }
}
