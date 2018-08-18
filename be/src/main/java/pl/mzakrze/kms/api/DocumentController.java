package pl.mzakrze.kms.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.mzakrze.kms.api.model.DocContent_in;
import pl.mzakrze.kms.api.model.DocumentMeta_in;
import pl.mzakrze.kms.user.UserProfile;
import pl.mzakrze.kms.user_drive.UserDriveFacade;
import pl.mzakrze.kms.user_drive.model.Document;
import pl.mzakrze.kms.user_drive.model.Tag;
import pl.mzakrze.kms.user_drive.model.UserSpace;
import pl.mzakrze.kms.user_drive.repository.DocumentRepository;
import pl.mzakrze.kms.user_drive.repository.TagRepository;

@RestController
@RequestMapping("/api/doc")
public class DocumentController {

    @Autowired private UserDriveFacade userDriveFacade;
    @Autowired private DocumentRepository documentRepository;
    @Autowired private TagRepository tagRepository;

    @PutMapping("/content/{gid}")
    public ResponseEntity content(UserProfile userProfile, @PathVariable String gid, @RequestBody DocContent_in req){
        Document document = documentRepository.findOne(gid);
        if(document == null){
            return ResponseEntity.badRequest().build();
        }

        if(userDriveFacade.isInSpace(userDriveFacade.getCurrentSpace(userProfile), document) == false){
            return ResponseEntity.notFound().build();
        }

        document.setContent(req.content);
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
