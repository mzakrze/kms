package pl.mzakrze.kms;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import pl.mzakrze.kms.model.CurrentUser_out;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PublicResourceControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void publicResourceShouldReturnDummyMessage() throws Exception {
        String result = this.restTemplate.getForObject("/api/public", String.class);
        assert result.contains("This is public resource");
    }

    @Test
    public void currentUserForUnauthenticated() throws Exception {
        assert this.restTemplate.getForObject("/api/user/current", CurrentUser_out.class)
                .isAnonymous == true : "User should be anonymous";

        assert this.restTemplate.getForObject("/api/user/current", CurrentUser_out.class)
                .isAnonymous == true : "User should be anonymous";
    }
}