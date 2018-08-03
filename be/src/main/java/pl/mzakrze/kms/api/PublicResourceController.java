package pl.mzakrze.kms.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/public")
public class PublicResourceController {

    @GetMapping
    public String get(){
        return "This is public resource";
    }
}
