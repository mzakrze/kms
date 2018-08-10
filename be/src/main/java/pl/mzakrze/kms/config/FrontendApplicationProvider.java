package pl.mzakrze.kms.config;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

@Controller
public class FrontendApplicationProvider {

    @Autowired private ServletContext servletContext;

    @GetMapping(value = "/")
    public String serveIndexHtml(){
        return "index.html";
    }

    @GetMapping(value = "/bundle.js")
    public String serveBundleJs() {
        return "app-bundle.js";
    }

    @GetMapping(value = "/static/{filename:[a-z]+}{dot:.}{ext:[a-z]+}")
    public ResponseEntity handle(HttpServletResponse response, @PathVariable String filename, @PathVariable String dot, @PathVariable String ext) {
        // TODO - z jakiegoś powodu plik favicon.ico nie spełnia regexa
        InputStream resourceAsStream = this.getClass().getResourceAsStream("/frontend_application/" + filename + "." + ext);

        if(resourceAsStream == null){
            return ResponseEntity.notFound().build();
        }

        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        try {
            IOUtils.copy(resourceAsStream, response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity.ok().build();
    }
}
