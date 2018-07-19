package pl.mzakrze.kms.config;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FrontendApplicationProvider {

    @GetMapping(value = "/")
    public String serveIndexHtml(){
        return "index.html";
    }

    @GetMapping(value = "/bundle.js")
    public String serveBundleJs() {
        return "app-bundle.js";
    }
}