package pl.mzakrze.kms.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.mzakrze.kms.api.model.*;
import pl.mzakrze.kms.user.UserProfile;
import pl.mzakrze.kms.user_drive.UserDriveFacade;
import pl.mzakrze.kms.user_drive.model.Document;
import pl.mzakrze.kms.user_drive.model.File;
import pl.mzakrze.kms.user_drive.model.Folder;
import pl.mzakrze.kms.user_drive.repository.DocumentRepository;
import pl.mzakrze.kms.user_drive.repository.FileRepository;
import pl.mzakrze.kms.user_drive.repository.FolderRepository;
import pl.mzakrze.kms.user_drive.repository.TagRepository;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/drive")
public class DriveController {

    @Autowired private DocumentRepository documentRepository;
    @Autowired private FolderRepository folderRepository;
    @Autowired private FileRepository fileRepository;
    @Autowired private TagRepository tagRepository;
    @Autowired private UserDriveFacade userDriveFacade;

    @GetMapping("/tree")
    public DriveTree_out tree(UserProfile user){
        DriveTree_out res = new DriveTree_out();

        res.ownerGid = user.getGid();
        res.root = userDriveFacade.getCurrentSpaceDrive(user);

        return res;
    }

    @PutMapping("/move/{objectGid}/{destinationGid}")
    public ResponseEntity move(UserProfile userProfile, @PathVariable("objectGid") String objectGid, @PathVariable("destinationGid") String destinationGid){
        Folder destination = folderRepository.findOne(destinationGid);
        if(destination == null){
            return ResponseEntity.badRequest().build();
        }
        if(documentRepository.exists(objectGid)){
            Document document = documentRepository.findOne(objectGid);
            document.setParentFolder(destination);
            documentRepository.save(document);
            return ResponseEntity.ok().build();
        } else if(folderRepository.exists(objectGid)) {
            Folder folder = folderRepository.findOne(objectGid);
            folder.setParentFolder(destination);
            folderRepository.save(folder);
            return ResponseEntity.ok().build();
        } else if(fileRepository.exists(objectGid)) {
            File file = fileRepository.findOne(objectGid);
            file.setParentFolder(destination);
            fileRepository.save(file);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/remove/{objectGid}")
    public ResponseEntity remove(UserProfile userProfile, @PathVariable("objectGid") String objectGid){
        if(documentRepository.exists(objectGid)){
            Document document = documentRepository.findOne(objectGid);
            if(userDriveFacade.isInSpace(userDriveFacade.getCurrentSpace(userProfile), document)){
                documentRepository.delete(document);
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        } else if(folderRepository.exists(objectGid)) {
            Folder folder = folderRepository.findOne(objectGid);
            if(userDriveFacade.isInSpace(userDriveFacade.getCurrentSpace(userProfile), folder)){
                folderRepository.delete(folder);
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        } else if(fileRepository.exists(objectGid)) {
            File file = fileRepository.findOne(objectGid);
            if(userDriveFacade.isInSpace(userDriveFacade.getCurrentSpace(userProfile), file)){
                fileRepository.delete(file);
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/rename/{objectGid}")
    public ResponseEntity rename(UserProfile userProfile, @PathVariable("objectGid") String objectGid, @RequestBody RenameNode_in req){
        if(documentRepository.exists(objectGid)){
            Document document = documentRepository.findOne(objectGid);
            document.setTitle(req.newName);
            documentRepository.save(document);
            return ResponseEntity.ok().build();
        } else if(folderRepository.exists(objectGid)) {
            Folder folder = folderRepository.findOne(objectGid);
            folder.setName(req.newName);
            folderRepository.save(folder);
            return ResponseEntity.ok().build();
        } else if(fileRepository.exists(objectGid)) {
            File file = fileRepository.findOne(objectGid);
            file.setName(req.newName);
            fileRepository.save(file);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/create")
    public ResponseEntity createFolder(UserProfile userProfile, @RequestBody CreateNode_in req){
        Folder parentFolder = folderRepository.findOne(req.parentGid);
        if(parentFolder == null){
            return ResponseEntity.badRequest().build();
        }
        CreateNode_out res = new CreateNode_out();
        switch (req.type) {
            case "folder":
                Folder folder = new Folder();
                folder.setParentFolder(parentFolder);
                folder.setName(req.name);
                folderRepository.save(folder);
                return ResponseEntity.ok(res);
            case "document":
                Document document = new Document();
                document.setParentFolder(parentFolder);
                document.setTitle(req.name);
                documentRepository.save(document);
                return ResponseEntity.ok(res);
            case "blob":
                File file = new File();
                file.setParentFolder(parentFolder);
                file.setName(req.name);
                fileRepository.save(file);
                return ResponseEntity.ok(res);
            default:
                return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/tags")
    public List<String> allTagsInSpace(UserProfile userProfile){
        return userDriveFacade.getTagsInUserSpace(userDriveFacade.getCurrentSpace(userProfile));
    }

    @GetMapping("/tags/{documentGid}")
    public ResponseEntity documentsTags(UserProfile userProfile, @PathVariable String documentGid){
        Document document = documentRepository.findOne(documentGid);
        if(userDriveFacade.isInSpace(userDriveFacade.getCurrentSpace(userProfile), document) == false){
            return ResponseEntity.notFound().build();
        }
        List<String> tags = tagRepository.findByDocument(document)
                .stream()
                .map(e -> e.getName())
                .collect(Collectors.toList());
        return ResponseEntity.ok(tags);
    }

    @GetMapping("/search")
    public DriveTree_out search(UserProfile userProfile, @RequestBody SearchDrive_in req){
        // FIXME - this is still not implemented
        DriveTree_out res = new DriveTree_out();

        res.ownerGid = userProfile.getGid();
        res.root = userDriveFacade.getCurrentSpaceDrive(userProfile);

        return res;
    }



}
