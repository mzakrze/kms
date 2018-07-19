package pl.mzakrze.kms.api.v1;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AppVersionDispatcher {
    public static final String APP_VERSION_URL = "/app-version";

    @Value("${application.version}")
    private String appVersion;


    @RequestMapping(value = APP_VERSION_URL, method = RequestMethod.GET)
    public String appVersion() {
        return appVersion;
    }
}
