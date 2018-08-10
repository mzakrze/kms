package pl.mzakrze.kms;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FrontendApplicationDispatcherTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void indexHtml() throws Exception {
        ResponseEntity<String> indexHtml = restTemplate.exchange(
                "/", HttpMethod.GET, new HttpEntity<>(new HttpHeaders()), String.class, "");

        assertEquals(HttpStatus.OK, indexHtml.getStatusCode());

        assert indexHtml.getBody() != null;
        assert indexHtml.getBody().isEmpty() == false;
    }

    @Test
    public void appBundleJs() throws Exception {
        ResponseEntity<String> appBundleJs = restTemplate.exchange(
                "/bundle.js", HttpMethod.GET, new HttpEntity<>(new HttpHeaders()), String.class, "");

        assertEquals(HttpStatus.OK, appBundleJs.getStatusCode());

        assert appBundleJs.getBody() != null;
        assert appBundleJs.getBody().isEmpty() == false;
    }

    @Test
    public void testPng() throws Exception {
        ResponseEntity<String> faviconIco = restTemplate.exchange(
                "/static/test.png", HttpMethod.GET, new HttpEntity<>(new HttpHeaders()), String.class, "");

        assertEquals(HttpStatus.OK, faviconIco.getStatusCode());
    }

    // TODO favicon.ico
}
