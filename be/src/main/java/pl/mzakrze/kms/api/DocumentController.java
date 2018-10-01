package pl.mzakrze.kms.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.mzakrze.kms.api.model.DocContent_in;
import pl.mzakrze.kms.api.model.DocumentMeta_in;
import pl.mzakrze.kms.api.model.GetDoc_out;
import pl.mzakrze.kms.user.UserProfile;
import pl.mzakrze.kms.user.UserProfileFacade;
import pl.mzakrze.kms.user_drive.UserDriveFacade;
import pl.mzakrze.kms.user_drive.model.Document;
import pl.mzakrze.kms.user_drive.model.Tag;
import pl.mzakrze.kms.user_drive.model.UserSpace;
import pl.mzakrze.kms.user_drive.repository.DocumentRepository;
import pl.mzakrze.kms.user_drive.repository.TagRepository;
import pl.mzakrze.kms.user_drive.repository.UserSpaceRepository;

@RestController
@RequestMapping("/api/doc")
public class DocumentController {
// TODO - odizolować warstwy controllerów i fasad itp
    @Autowired private UserDriveFacade userDriveFacade;
    @Autowired private DocumentRepository documentRepository;
    @Autowired private TagRepository tagRepository;
    @Autowired private UserSpaceRepository userSpaceRepository;
    @Autowired private UserProfileFacade userProfileFacade;

    @GetMapping("/{gid}")
    public ResponseEntity getDoc(UserProfile userProfile, @PathVariable String gid){
        Document document = documentRepository.findOne(gid);
        if(document == null){
            return ResponseEntity.notFound().build();
        }
        UserSpace userSpace = userSpaceRepository.findByUserProfileAndCurrent(userProfile, true);
        boolean hasPermissions = userDriveFacade.isInSpace(userSpace, document);
        if(hasPermissions){
            GetDoc_out res = new GetDoc_out();
            res.markdownContent = emptyOnNull(document.getContent());
            // res.tags = FIXME
            res.title = document.getTitle();
            return ResponseEntity.ok(res);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    private String emptyOnNull(byte[] s){
        return s == null ? "" : new String(s);
    }

    @GetMapping("/download/{gid}")
    public ResponseEntity download(UserProfile userProfile, @PathVariable String gid, @RequestParam("download_as") String downloadAs){
        Document document = documentRepository.findOne(gid);
        if(document == null){
            return ResponseEntity.notFound().build();
        }
        Boolean hasPermissions = userDriveFacade.hasPermissions(userProfile, document);
        if(hasPermissions == false){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        String fileName = document.getTitle() + "01-01-2018.15:15:46:241.txt"; // TODO data + godzina
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                //.contentLength(document.getContent().length)
                //.header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + fileName + "\"")
//                .header("Content-Disposition", String.format("inline; filename=\"" + fileName + "\""))
//                .header("Content-Type", "application/octet-stream")
//                .header("Pragma: public") // required
//                .header("Expires: 0")
//                .header("Cache-Control: must-revalidate, post-check=0, pre-check=0")
//                //.header("Cache-Control: private",false) // required for certain browsers
//                //.header("Content-Disposition: attachment; filename=\"".basename($filename)."\";" )
//                .header("Content-Transfer-Encoding: binary")
//                .header("Content-Length: ", document.getContent().length + "")
                .body(new String(document.getContent())); // FIXME - przeglądarka nie pobiera pliku
    }

    @PutMapping("/content/{gid}")
    public ResponseEntity putContent(UserProfile userProfile, @PathVariable String gid, @RequestBody DocContent_in req){
        Document document = documentRepository.findOne(gid);
        if(document == null){
            return ResponseEntity.badRequest().build();
        }

        if(userDriveFacade.isInSpace(userDriveFacade.getCurrentSpace(userProfile), document) == false){
            return ResponseEntity.notFound().build();
        }
        document.setContent(req.content.getBytes());
        documentRepository.save(document);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/meta/{gid}")
    public ResponseEntity meta(UserProfile userProfile, @PathVariable String gid, @RequestBody DocumentMeta_in req) {
        Document document = documentRepository.findOne(gid);
        if(document == null){
            return ResponseEntity.badRequest().build();
        }

        if(userDriveFacade.isInSpace(userDriveFacade.getCurrentSpace(userProfile), document) == false){
            return ResponseEntity.notFound().build();
        }

        tagRepository.deleteByDocument(document);

        for (String tagName : req.tags) {
            Tag tag = new Tag();
            tag.setDocument(document);
            tag.setName(tagName);
            tagRepository.save(tag);
        }

        return ResponseEntity.ok().build();
    }

    @PutMapping("/static/{gid}")
    public String putStatic(UserProfile userProfile, @PathVariable String gid){
        throw new UnsupportedOperationException("plz implement me");
    }

    @GetMapping("/static/{gid}")
    public String getStatic(UserProfile userProfile, @PathVariable String gid){
        throw new UnsupportedOperationException("plz implement me");
    }
}
