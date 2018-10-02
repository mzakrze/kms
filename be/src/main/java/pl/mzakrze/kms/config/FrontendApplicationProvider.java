package pl.mzakrze.kms.config;

import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.*;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

@Controller
public class FrontendApplicationProvider {

    @Value( "${application.environment}" )
    private String APPLICATION_ENVIRONMENT;

    @Autowired private ServletContext servletContext;

    @GetMapping(value = "/")
    public ResponseEntity serveIndexHtml(HttpServletResponse response) {
        if(APPLICATION_ENVIRONMENT.equals(ApplicationEnvironment.PRODUCTION.getCode())){
            return serve(response, "/frontend_application/index.html", MediaType.TEXT_HTML_VALUE);
        } else {
            return serverFromWebpackServer(response, "/", MediaType.TEXT_HTML_VALUE);
        }
    }

    @GetMapping(value = "/bundle.js")
    public ResponseEntity serveBundleJs(HttpServletResponse response) {
        if(APPLICATION_ENVIRONMENT.equals(ApplicationEnvironment.PRODUCTION.getCode())){
            return serve(response, "/frontend_application/index.html", "application/javascript");
        } else {
            return serverFromWebpackServer(response, "/bundle.js", "application/javascript");
        }
    }

    @GetMapping(value = "/static/{filename:[a-z]+}{dot:.}{ext:[a-z]+}")
    public ResponseEntity handle(HttpServletResponse response, @PathVariable String filename, @PathVariable String dot, @PathVariable String ext) {
        // TODO - z jakiegoś powodu plik favicon.ico nie spełnia regexa
        return serve(response, "/frontend_application/" + filename + "." + ext, MediaType.IMAGE_JPEG_VALUE);
    }

    private ResponseEntity serve(HttpServletResponse response, String fileName, String mediaType){
        InputStream resourceAsStream = this.getClass().getResourceAsStream(fileName);

        if(resourceAsStream == null){
            return ResponseEntity.notFound().build();
        }

        response.setContentType(mediaType + ";charset=UTF-8");
        try {
            IOUtils.copy(resourceAsStream, response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity.ok().build();
    }


    private ResponseEntity serverFromWebpackServer(HttpServletResponse response, String filename, String mediaType) {
        HttpClient client = new HttpClient();
        // TODO - adres webpacka developerskiego jest aktualnie zaszyty, należy wynieść
        GetMethod method = new GetMethod("http://localhost:3000" + filename);
        try {
            client.executeMethod(method);

            try {
                response.setContentType(mediaType + ";charset=UTF-8");
                IOUtils.copy(method.getResponseBodyAsStream(), response.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }

            return null;
        } catch (HttpException e) {
            String msg = "Fatal protocol violation: " + e.getMessage();
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Webpack not available, msg: " + msg);
        } catch (IOException e) {
            String msg = "Fatal transport error: " + e.getMessage();
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Webpack not available, msg: " + msg);
        } finally {
            method.releaseConnection();
        }
    }

    /*
    jeśli uzytkownik wejdzie na stronę np /doc/<some gid> poleci żądanie do serwera.Jest to błąd bo urlami zarządza aplikacja SPA.
    Rozwiązanie: redirect na stronę główną z pathem, aby aplikacja SPA mógła stosownie zaregować.
    Poniższe metody to właśnie implementują
     */

    @GetMapping(value = "/doc/**")
    public ResponseEntity redirectWithPathApplied(HttpServletResponse response, HttpServletRequest request) {
        doRedirectWithPath(response, request);
        return null;
    }

    @GetMapping(value = "/drive/**")
    public ResponseEntity redirectDriveWithPathApplied(HttpServletResponse response, HttpServletRequest request) {
        doRedirectWithPath(response, request);
        return null;
    }

    private void doRedirectWithPath(HttpServletResponse response, HttpServletRequest request){
        String path = "/" + "?path=" + request.getRequestURI();

        response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
        response.setHeader("Location", path);
    }
}
    